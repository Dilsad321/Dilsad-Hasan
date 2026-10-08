package com.example.util

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.EnumMap

object QrCodeGenerator {
    /**
     * Generates an Android Bitmap containing the QR code for given [content].
     */
    fun generateQrBitmap(
        content: String,
        sizePx: Int = 512,
        darkColor: Int = android.graphics.Color.BLACK,
        lightColor: Int = android.graphics.Color.WHITE
    ): Bitmap {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
            put(EncodeHintType.CHARACTER_SET, "UTF-8")
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
            put(EncodeHintType.MARGIN, 1) // small padding
        }

        val bitMatrix = QRCodeWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            sizePx,
            sizePx,
            hints
        )

        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) darkColor else lightColor
            }
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    /**
     * Returns an [ImageBitmap] directly ready for Compose Image components.
     */
    fun generateQrImageBitmap(
        content: String,
        sizePx: Int = 512,
        darkColor: Int = android.graphics.Color.BLACK,
        lightColor: Int = android.graphics.Color.WHITE
    ): ImageBitmap {
        return generateQrBitmap(content, sizePx, darkColor, lightColor).asImageBitmap()
    }

    /**
     * Builds a standard UPI URI string.
     */
    fun buildUpiUri(
        upiId: String,
        payeeName: String,
        amount: String? = null
    ): String {
        val base = "upi://pay?pa=$upiId&pn=${java.net.URLEncoder.encode(payeeName, "UTF-8")}&cu=INR"
        return if (!amount.isNullOrBlank() && (amount.toDoubleOrNull() ?: 0.0) > 0) {
            "$base&am=${java.net.URLEncoder.encode(amount, "UTF-8")}"
        } else {
            base
        }
    }

    /**
     * Builds a WhatsApp direct chat URL.
     */
    fun buildWhatsAppUrl(
        phoneWithoutPlus: String,
        message: String = "নমস্কার তামিম অনলাইন সেন্টার"
    ): String {
        val encodedMsg = java.net.URLEncoder.encode(message, "UTF-8")
        return "https://wa.me/91$phoneWithoutPlus?text=$encodedMsg"
    }
}
