package com.example.nutrisnap.ml

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.FloatBuffer
import kotlin.math.exp

data class FoodValidationResult(
    val isFood: Boolean,
    val foodConfidence: Float,
    val nonFoodConfidence: Float
)


class FoodValidator(context: Context) : AutoCloseable {

    private val env = OrtEnvironment.getEnvironment()
    private val session: OrtSession

    private val inputSize = 512

    private val mean = floatArrayOf(
        0.485f, 0.456f, 0.406f
    )

    private val std = floatArrayOf(
        0.229f, 0.224f, 0.225f
    )

    init {
        val modelFile = File(
            context.filesDir,
            "food_validator.onnx"
        )

        context.assets.open("food_validator.onnx").use { input ->
            modelFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        context.assets.open("food_validator.onnx.data").use { input ->
            File(
                context.filesDir,
                "food_validator.onnx.data"
            ).outputStream().use { output ->
                input.copyTo(output)
            }
        }

        session = env.createSession(
            modelFile.absolutePath,
            OrtSession.SessionOptions()
        )
    }

    suspend fun validate(
        bitmap: Bitmap,
        threshold: Float = 0.80f
    ): FoodValidationResult = withContext(Dispatchers.Default) {

        val processedBitmap = resizeAndCenterCrop(bitmap)

        val floatBuffer = bitmapToFloatBuffer(processedBitmap)

        val shape = longArrayOf(
            1,
            3,
            inputSize.toLong(),
            inputSize.toLong()
        )

        val inputName = session.inputNames.first()

        OnnxTensor.createTensor(
            env,
            floatBuffer,
            shape
        ).use { inputTensor ->

            session.run(
                mapOf(inputName to inputTensor)
            ).use { results ->

                val logits = (results[0].value as Array<FloatArray>)[0]

                require(logits.size == 2) {
                    "Expected 2 output classes, got ${logits.size}"
                }

                val probabilities = softmax(logits)

                val foodConfidence = probabilities[0]
                val nonFoodConfidence = probabilities[1]

                FoodValidationResult(
                    isFood = foodConfidence >= threshold,
                    foodConfidence = foodConfidence,
                    nonFoodConfidence = nonFoodConfidence
                )
            }
        }
    }

    private fun resizeAndCenterCrop(source: Bitmap): Bitmap {

        val width = source.width
        val height = source.height

        // Resize the shorter edge to 512 pixels
        val scale = inputSize.toFloat() /
                minOf(width, height)


        val scaledWidth = (width * scale).toInt()
        val scaledHeight = (height * scale).toInt()

        val resized = Bitmap.createScaledBitmap(
            source,
            scaledWidth,
            scaledHeight,
            true
        )

        // Center crop to 512 x 512
        val xOffset = (scaledWidth - inputSize) / 2
        val yOffset = (scaledHeight - inputSize) / 2

        return Bitmap.createBitmap(
            resized,
            xOffset,
            yOffset,
            inputSize,
            inputSize
        )
    }

    private fun bitmapToFloatBuffer(
        bitmap: Bitmap
    ): FloatBuffer {

        val stride = inputSize * inputSize

        val buffer = FloatBuffer.allocate(3 * stride)

        val pixels = IntArray(stride)

        bitmap.getPixels(
            pixels,
            0,
            inputSize,
            0,
            0,
            inputSize,
            inputSize
        )

        // ONNX input format: NCHW (RGB)
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

        val exps = FloatArray(logits.size) {
            exp(logits[it] - maxLogit)
        }

        val sum = exps.sum()

        return FloatArray(logits.size) {
            exps[it] / sum
        }
    }

    override fun close() {
        session.close()
    }
}
