package com.example.nutrisnap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun loadSoftwareBitmap(
    context: Context,
    imageUri: Uri?,
    bitmap: Bitmap?
): Bitmap {
    val rawBitmap = when {
        imageUri != null -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                }
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            }
        }
        bitmap != null -> bitmap
        else -> throw Exception("No image selected")
    }

    return if (rawBitmap.config == Bitmap.Config.ARGB_8888) {
        rawBitmap
    } else {
        rawBitmap.copy(Bitmap.Config.ARGB_8888, false)
    }
}