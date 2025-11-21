package com.mambo.iris.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

/**
 * Utilidad para cargar bitmaps grandes con downsampling.
 * Evita OutOfMemory en imágenes 360° muy pesadas.
 */
object BitmapLoader {

    fun decodeSampledBitmapFromResource(
        res: Resources,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap? {
        // 1) Solo leemos dimensiones
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(res, resId, options)

        val width = options.outWidth
        val height = options.outHeight
        if (width <= 0 || height <= 0) return null

        // 2) Calculamos inSampleSize
        options.inSampleSize = calculateInSampleSize(
            width,
            height,
            reqWidth,
            reqHeight
        )

        // 3) Decodificamos de verdad con downsampling
        options.inJustDecodeBounds = false
        options.inPreferredConfig = Bitmap.Config.RGB_565 // menos memoria

        return BitmapFactory.decodeResource(res, resId, options)
    }

    private fun calculateInSampleSize(
        width: Int,
        height: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            var halfHeight = height / 2
            var halfWidth = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight &&
                (halfWidth / inSampleSize) >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}

