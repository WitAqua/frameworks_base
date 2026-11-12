/*
 * Copyright (C) 2023 The Android Open Source Project
 *               2026 WitAqua
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settingslib

import android.annotation.ColorInt
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import java.nio.charset.StandardCharsets
import java.util.EnumMap

object BarCodeGenerator {
    /**
     * Generates a barcode image with [contents].
     *
     * @param contents The contents to encode in the barcode
     * @param width    The preferred image width in pixels
     * @param height   The preferred image height in pixels
     * @param invert   Whether to invert the black/white pixels (e.g. for dark mode)
     * @return Barcode bitmap
     */
    @JvmStatic
    @Throws(WriterException::class, java.lang.IllegalArgumentException::class)
    fun encodeBarCode(contents: String, width: Int, height: Int, invert: Boolean): Bitmap =
        encodeBarCode(contents, width, height, DEFAULT_MARGIN, invert)

    private const val DEFAULT_MARGIN = -1

    /**
     * Generates a barcode image with [contents].
     *
     * @param contents The contents to encode in the barcode
     * @param width    The preferred image width in pixels
     * @param height   The preferred image height in pixels
     * @param margin   The margin around the actual barcode
     * @param invert   Whether to invert the black/white pixels (e.g. for dark mode)
     * @return Barcode bitmap
     */
    @JvmOverloads
    @JvmStatic
    @Throws(WriterException::class, IllegalArgumentException::class)
    fun encodeBarCode(
        contents: String,
        width: Int,
        height: Int,
        margin: Int = DEFAULT_MARGIN,
        invert: Boolean = false,
    ): Bitmap {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        if (margin != DEFAULT_MARGIN) {
            hints[EncodeHintType.MARGIN] = margin
        }
        val barBits = MultiFormatWriter().encode(contents, BarcodeFormat.CODE_128, width, height, hints)
        @ColorInt val setColor = if (invert) Color.WHITE else Color.BLACK
        @ColorInt val unsetColor = if (invert) Color.BLACK else Color.WHITE
        @ColorInt val pixels = IntArray(width * height)
        for (x in 0 until width) {
            for (y in 0 until height) {
                pixels[y * width + x] = if (barBits[x, y]) setColor else unsetColor
            }
        }
        return Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }
}
