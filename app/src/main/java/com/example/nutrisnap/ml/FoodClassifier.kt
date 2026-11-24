package com.example.nutrisnap.ml

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import android.graphics.Bitmap
import com.example.nutrisnap.data.remote.FoodPrediction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.FloatBuffer
import kotlin.math.exp
import kotlin.math.round

class FoodClassifier(context: Context) : AutoCloseable {

    private val env = OrtEnvironment.getEnvironment()
    private val session: OrtSession
    private val labels: List<String>

    private val mean = floatArrayOf(0.485f, 0.456f, 0.406f)
    private val std = floatArrayOf(0.229f, 0.224f, 0.225f)

    init {
        val modelBytes = context.assets.open("food_model.onnx").readBytes()
        session = env.createSession(modelBytes, OrtSession.SessionOptions())
        labels = context.assets.open("labels.txt").bufferedReader().readLines()
    }

    suspend fun classify(bitmap: Bitmap, topK: Int = 5): List<FoodPrediction> =
        withContext(Dispatchers.Default) {
            val cropSize = 224
            val croppedBitmap = resizeAndCenterCrop(bitmap, resizeShortEdge = 256, cropSize = cropSize)
            val floatBuffer = bitmapToFloatBuffer(croppedBitmap, cropSize)

            val shape = longArrayOf(1, 3, cropSize.toLong(), cropSize.toLong())
            val inputName = session.inputNames.iterator().next()

            OnnxTensor.createTensor(env, floatBuffer, shape).use { inputTensor ->
                session.run(mapOf(inputName to inputTensor)).use { results ->
                    val logits = (results[0].value as Array<FloatArray>)[0]
                    val probs = softmax(logits)

                    probs.indices
                        .sortedByDescending { probs[it] }
                        .take(topK)
                        .map { idx ->
                            val pct = round(probs[idx] * 10000.0) / 100.0
                            FoodPrediction(
                                food_name = labels.getOrElse(idx) { "unknown" },
                                confidence = pct
                            )
                        }
                }
            }
        }

    private fun resizeAndCenterCrop(
        source: Bitmap,
        resizeShortEdge: Int = 256,
        cropSize: Int = 224
    ): Bitmap {
        val width = source.width
        val height = source.height

        val (scaledW, scaledH) = if (width < height) {
            resizeShortEdge to (height * (resizeShortEdge.toFloat() / width)).toInt()
        } else {
            (width * (resizeShortEdge.toFloat() / height)).toInt() to resizeShortEdge
        }

        val resized = Bitmap.createScaledBitmap(source, scaledW, scaledH, true)
        val xOffset = ((scaledW - cropSize) / 2).coerceAtLeast(0)
        val yOffset = ((scaledH - cropSize) / 2).coerceAtLeast(0)

        return Bitmap.createBitmap(resized, xOffset, yOffset, cropSize, cropSize)
    }

    private fun bitmapToFloatBuffer(bitmap: Bitmap, size: Int): FloatBuffer {
        val buffer = FloatBuffer.allocate(1 * 3 * size * size)
        val pixels = IntArray(size * size)
        bitmap.getPixels(pixels, 0, size, 0, 0, size, size)

        val stride = size * size
        for (i in pixels.indices) {
            val pixel = pixels[i]
            val r = ((pixel shr 16) and 0xFF) / 255f
            val g = ((pixel shr 8) and 0xFF) / 255f
            val b = (pixel and 0xFF) / 255f

            buffer.put(i, (r - mean[0]) / std[0])
            buffer.put(stride + i, (g - mean[1]) / std[1])
            buffer.put(2 * stride + i, (b - mean[2]) / std[2])
        }
        buffer.rewind()
        return buffer
    }

    private fun softmax(logits: FloatArray): FloatArray {
        val maxLogit = logits.maxOrNull() ?: 0f
        val exps = FloatArray(logits.size) { exp(logits[it] - maxLogit) }
        val sum = exps.sum()
        return FloatArray(logits.size) { exps[it] / sum }
    }

    override fun close() {
        session.close()
        env.close()
    }
}