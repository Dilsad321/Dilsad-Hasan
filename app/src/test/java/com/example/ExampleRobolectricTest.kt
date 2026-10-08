package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ShopConstants
import com.example.data.ShopRepository
import com.example.util.QrCodeGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("তামিম অনলাইন", appName)
    }

    @Test
    fun `repository provides all 9 shop services`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = ShopRepository(context)
        assertEquals(9, repository.allServices.size)
        assertTrue(repository.allServices.any { it.title.contains("প্যান") })
        assertTrue(repository.allServices.any { it.title.contains("PVC") })
        assertTrue(repository.allServices.any { it.title.contains("AEPS") })
        assertTrue(repository.allServices.any { it.title.contains("পর্চা") })
    }

    @Test
    fun `repository manages work status records correctly`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = ShopRepository(context)
        val initialCount = repository.workRecords.value.size

        val added = repository.addWorkRecord(
            name = "পরীক্ষামূলক গ্রাহক",
            phone = "9876543210",
            service = "প্যান কার্ড",
            stage = 1,
            note = "আবেদন গ্রহণ করা হয়েছে।"
        )
        assertNotNull(added.token)
        assertTrue(added.token.startsWith("TOC-"))
        assertEquals(initialCount + 1, repository.workRecords.value.size)

        // Update stage to 3 (Ready)
        repository.updateRecordStage(added.token, 3)
        val updated = repository.workRecords.value.first { it.token == added.token }
        assertEquals(3, updated.stage)
    }

    @Test
    fun `qr code generator builds valid UPI uri and bitmap`() {
        val upiUri = QrCodeGenerator.buildUpiUri(
            upiId = ShopConstants.UPI_ID,
            payeeName = ShopConstants.SHOP_NAME_EN,
            amount = "150"
        )
        assertTrue(upiUri.contains("7478654044@okbizaxis"))
        assertTrue(upiUri.contains("am=150"))

        val bitmap = QrCodeGenerator.generateQrBitmap(upiUri, sizePx = 200)
        assertNotNull(bitmap)
        assertEquals(200, bitmap.width)
        assertEquals(200, bitmap.height)
    }

    @Test
    fun `bot engine responds to status and service queries`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = ShopRepository(context)

        // Query by token TOC-101
        val replyToken = repository.processBotQuery("TOC-101 এর স্ট্যাটাস কী?")
        assertTrue(replyToken.text.contains("TOC-101"))
        assertTrue(replyToken.text.contains("সোহেল রানা"))

        // Query about PAN card
        val replyPan = repository.processBotQuery("প্যান কার্ড করতে কী কী লাগবে?")
        assertTrue(replyPan.text.contains("প্যান"))
        assertTrue(replyPan.text.contains("ডকুমেন্টস"))

        // Query about UPI
        val replyUpi = repository.processBotQuery("দোকানের Google Pay UPI ID কী?")
        assertTrue(replyUpi.text.contains(ShopConstants.UPI_ID))

        // Query about location / map
        val replyMap = repository.processBotQuery("দোকানের ম্যাপের লিংক দিন")
        assertTrue(replyMap.text.contains("maps.app.goo.gl/MdBi9eNL1VivCgwz9"))
    }

    @Test
    fun `shop constants contains correct Google Maps link`() {
        assertEquals("https://maps.app.goo.gl/MdBi9eNL1VivCgwz9", ShopConstants.MAPS_URL)
    }
}
