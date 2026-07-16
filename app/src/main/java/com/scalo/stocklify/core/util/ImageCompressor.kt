package com.scalo.stocklify.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import kotlin.math.max

object ImageCompressor {

    /**
     * Downscales the image at [uri] so its longest edge is at most [maxDimensionPx],
     * corrects orientation using EXIF data, and encodes it as WebP.
     */
    fun compressToWebp(context: Context, uri: Uri, maxDimensionPx: Int, quality: Int): ByteArray {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, bounds)
        } ?: throw IllegalArgumentException("Unable to open image at $uri")

        val sampleSize = calculateInSampleSize(bounds.outWidth, bounds.outHeight, maxDimensionPx)
        val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        val decoded = context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, decodeOptions)
        } ?: throw IllegalArgumentException("Unable to decode image at $uri")

        val rotated = applyExifRotation(context, uri, decoded)
        val scaled = downscaleIfNeeded(rotated, maxDimensionPx)

        val output = ByteArrayOutputStream()
        scaled.compress(webpFormat(), quality, output)

        if (scaled !== decoded) scaled.recycle()
        if (rotated !== decoded) rotated.recycle()
        decoded.recycle()

        return output.toByteArray()
    }

    private fun calculateInSampleSize(width: Int, height: Int, maxDimensionPx: Int): Int {
        var sampleSize = 1
        var longestEdge = max(width, height)
        while (longestEdge / (sampleSize * 2) >= maxDimensionPx) {
            sampleSize *= 2
        }
        return sampleSize
    }

    private fun downscaleIfNeeded(bitmap: Bitmap, maxDimensionPx: Int): Bitmap {
        val longestEdge = max(bitmap.width, bitmap.height)
        if (longestEdge <= maxDimensionPx) return bitmap

        val scale = maxDimensionPx.toFloat() / longestEdge
        val targetWidth = (bitmap.width * scale).toInt().coerceAtLeast(1)
        val targetHeight = (bitmap.height * scale).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    }

    private fun applyExifRotation(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
        val orientation = context.contentResolver.openInputStream(uri)?.use {
            ExifInterface(it).getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        } ?: ExifInterface.ORIENTATION_NORMAL

        val rotationDegrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
        if (rotationDegrees == 0f) return bitmap

        val matrix = android.graphics.Matrix().apply { postRotate(rotationDegrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    @Suppress("DEPRECATION")
    private fun webpFormat(): Bitmap.CompressFormat =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            Bitmap.CompressFormat.WEBP
        }
}
