package com.scalo.stocklify.feature.products.domain.preprocessor

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.RotatedRect
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

object ImagePreProcessor {

    fun process(source: Bitmap): Bitmap {
        val src = Mat()
        Utils.bitmapToMat(source, src)

        val gray = Mat()
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_RGBA2GRAY)
        src.release()

        val denoised = Mat()
        Imgproc.GaussianBlur(gray, denoised, Size(3.0, 3.0), 0.0)
        gray.release()

        val deskewed = deskew(denoised)
        denoised.release()

        val contrasted = Mat()
        Imgproc.createCLAHE(2.0, Size(8.0, 8.0)).apply(deskewed, contrasted)
        deskewed.release()

        val sharpened = sharpen(contrasted)
        contrasted.release()

        val binary = Mat()
        Imgproc.adaptiveThreshold(
            sharpened, binary,
            255.0,
            Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
            Imgproc.THRESH_BINARY,
            15, 10.0
        )
        sharpened.release()

        val result = createBitmap(source.width, source.height)
        Utils.matToBitmap(binary, result)
        binary.release()

        return result
    }

    private fun deskew(src: Mat): Mat {
        val edges = Mat()
        Imgproc.Canny(src, edges, 50.0, 150.0)

        val contours = mutableListOf<MatOfPoint>()
        Imgproc.findContours(edges, contours, Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)
        edges.release()

        if (contours.isEmpty()) return src.clone()

        // Collect angles from all sufficiently large contours
        val angles = contours
            .filter { Imgproc.contourArea(it) > 100 }
            .map { contour ->
                val points = org.opencv.core.MatOfPoint2f(*contour.toArray())
                Imgproc.minAreaRect(points).angle
            }

        if (angles.isEmpty()) return src.clone()

        // Median angle is more robust than mean against stray contours
        val medianAngle = angles.sorted().let { it[it.size / 2] }

        // Only correct if the skew is meaningful but not a 90° rotation
        val correctionAngle = when {
            medianAngle < -45.0 -> medianAngle + 90.0
            medianAngle > 45.0  -> medianAngle - 90.0
            else                -> medianAngle
        }

        if (kotlin.math.abs(correctionAngle) < 0.5) return src.clone()

        val center = org.opencv.core.Point(src.width() / 2.0, src.height() / 2.0)
        val rotMat = Imgproc.getRotationMatrix2D(center, correctionAngle, 1.0)
        val rotated = Mat()
        Imgproc.warpAffine(src, rotated, rotMat, src.size(), Imgproc.INTER_LINEAR)
        rotMat.release()

        return rotated
    }

    private fun sharpen(src: Mat): Mat {
        val blurred = Mat()
        Imgproc.GaussianBlur(src, blurred, Size(0.0, 0.0), 3.0)
        val sharpened = Mat()
        Core.addWeighted(src, 1.5, blurred, -0.5, 0.0, sharpened)
        blurred.release()
        return sharpened
    }
}
