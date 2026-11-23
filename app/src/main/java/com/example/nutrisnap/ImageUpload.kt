package com.example.nutrisnap

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

fun createImagePart(
    context: Context,
    imageUri: Uri?,
    bitmap: Bitmap?
): MultipartBody.Part {
    val imageBytes: ByteArray
    var fileName = "food_image.jpg"

    if (imageUri != null) {
        // Read the image selected from the gallery
        imageBytes = context.contentResolver
            .openInputStream(imageUri)
            ?.use { it.readBytes() }
            ?:throw Exception("Unable to read selected image")

        // Retrieve the image filename if available
        val cursor = context.contentResolver.query(
            imageUri,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(
                    android.provider.OpenableColumns.DISPLAY_NAME
                )

                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex) ?: fileName
                }
            }
        }
    } else if (bitmap != null) {

        // Convert camera Bitmap into JPEG bytes
        val outputStream = ByteArrayOutputStream()

        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            90,
            outputStream
        )

        imageBytes = outputStream.toByteArray()

    } else {
        throw Exception("No image selected")
    }

    val requestBody = imageBytes.toRequestBody(
        "image/jpeg".toMediaType()
    )

    return MultipartBody.Part.createFormData(
        "file",
        fileName,
        requestBody
    )
}