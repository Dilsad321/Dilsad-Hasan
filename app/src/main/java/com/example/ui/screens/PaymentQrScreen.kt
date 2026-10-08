package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ShopConstants
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandNavy
import com.example.ui.theme.GPayBlue
import com.example.ui.theme.GPayGreen
import com.example.ui.theme.GPayRed
import com.example.ui.theme.GPayYellow
import com.example.util.QrCodeGenerator

enum class QrPosterMode(val label: String) {
    UPI("Google Pay"),
    WHATSAPP("WhatsApp QR"),
    DUAL("ডুয়াল পোস্টার")
}

@Composable
fun PaymentQrScreen(
    onDirectUpiPay: (String) -> Unit,
    onShareShopWhatsApp: () -> Unit,
    onPrintOrSharePoster: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedMode by remember { mutableStateOf(QrPosterMode.UPI) }
    var customAmount by remember { mutableStateOf("") }

    val currentUpiUri = remember(customAmount) {
        QrCodeGenerator.buildUpiUri(
            upiId = ShopConstants.UPI_ID,
            payeeName = ShopConstants.SHOP_NAME_EN,
            amount = customAmount
        )
    }

    val whatsAppUri = remember {
        QrCodeGenerator.buildWhatsAppUrl(ShopConstants.RAW_PHONE, "Hello Tamim Online Centre")
    }

    val upiQrBitmap = remember(currentUpiUri) {
        try {
            QrCodeGenerator.generateQrImageBitmap(currentUpiUri, sizePx = 250)
        } catch (e: Exception) {
            null
        }
    }

    val waQrBitmap = remember(whatsAppUri) {
        try {
            QrCodeGenerator.generateQrImageBitmap(whatsAppUri, sizePx = 200)
        } catch (e: Exception) {
            null
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Mode Selector and Custom Amount Controls
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "পোস্টার ও QR মোড বেছে নিন:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        QrPosterMode.values().forEach { mode ->
                            val isSelected = selectedMode == mode
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) BrandNavy else Color(0xFFF1F5F9),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) BrandNavy else Color(0xFFCBD5E1)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedMode = mode }
                                    .testTag("qr_mode_${mode.name}")
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = mode.label,
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else Color(0xFF334155)
                                    )
                                }
                            }
                        }
                    }

                    if (selectedMode == QrPosterMode.UPI) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFFF1F5F9))
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = customAmount,
                                onValueChange = { customAmount = it.filter { char -> char.isDigit() } },
                                placeholder = { Text("নির্দিষ্ট টাকার পরিমাণ লিখুন (ঐচ্ছিক)", fontSize = 11.5.sp) },
                                leadingIcon = {
                                    Text("₹", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BrandNavy,
                                    unfocusedBorderColor = Color(0xFFCBD5E1),
                                    focusedContainerColor = Color(0xFFF8FAFC),
                                    unfocusedContainerColor = Color(0xFFF8FAFC)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("custom_amount_input")
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = { onDirectUpiPay(currentUpiUri) },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandEmerald),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("direct_upi_pay_button")
                            ) {
                                Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("পেমেন্ট", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // PRINTABLE GOOGLE PAY POSTER CARD
        item {
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFCBD5E1)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("printable_poster_card")
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Authentic 4-Color Google Pay top bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f).height(7.dp).background(GPayBlue))
                        Box(modifier = Modifier.weight(1f).height(7.dp).background(GPayGreen))
                        Box(modifier = Modifier.weight(1f).height(7.dp).background(GPayYellow))
                        Box(modifier = Modifier.weight(1f).height(7.dp).background(GPayRed))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // CSC Header Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFEFF6FF),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
                        ) {
                            Text(
                                text = "CSC ID: ${ShopConstants.CSC_ID} • কমন সার্ভিস সেন্টার",
                                color = Color(0xFF1E40AF),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        com.example.ui.components.Stylish3DShopTitle(
                            fontSize = 30.sp,
                            isDarkBackground = false,
                            showEnglishSub = true,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Google Pay Branding Badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "G",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = GPayBlue
                            )
                            Text(
                                text = "o",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = GPayRed
                            )
                            Text(
                                text = "o",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = GPayYellow
                            )
                            Text(
                                text = "g",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = GPayBlue
                            )
                            Text(
                                text = "l",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = GPayGreen
                            )
                            Text(
                                text = "e ",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = GPayRed
                            )
                            Text(
                                text = "Pay",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF334155)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = BrandEmerald, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(ShopConstants.PHONE_NUMBER, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Owner and address pills
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFEFF6FF),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
                            ) {
                                Text(
                                    text = "প্রোঃ ${ShopConstants.PROPRIETOR_NAME}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E3A8A),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFFEF3C7),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                            ) {
                                Text(
                                    text = ShopConstants.ADDRESS_SHORT,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // QR CODE CONTAINER
                        when (selectedMode) {
                            QrPosterMode.UPI -> {
                                Text(
                                    text = "Scan & Pay with any UPI App",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White,
                                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE2E8F0)),
                                    shadowElevation = 3.dp,
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(14.dp)
                                    ) {
                                        if (upiQrBitmap != null) {
                                            Image(
                                                bitmap = upiQrBitmap,
                                                contentDescription = "Google Pay UPI QR",
                                                modifier = Modifier.size(200.dp)
                                            )
                                        }

                                        if (customAmount.isNotBlank() && (customAmount.toDoubleOrNull() ?: 0.0) > 0) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0xFFD1FAE5)
                                            ) {
                                                Text(
                                                    text = "পেমেন্ট অ্যামাউন্ট: ₹$customAmount",
                                                    color = Color(0xFF065F46),
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            QrPosterMode.WHATSAPP -> {
                                Text(
                                    text = "হোয়াটসঅ্যাপে ডকুমেন্ট পাঠাতে স্ক্যান করুন",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White,
                                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFA7F3D0)),
                                    shadowElevation = 3.dp,
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(14.dp)
                                    ) {
                                        if (waQrBitmap != null) {
                                            Image(
                                                bitmap = waQrBitmap,
                                                contentDescription = "WhatsApp QR",
                                                modifier = Modifier.size(200.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            QrPosterMode.DUAL -> {
                                // Side by side dual QR
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // UPI QR Column
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = Color(0xFFEFF6FF),
                                        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFBFDBFE)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.padding(8.dp)
                                        ) {
                                            Text(
                                                text = "পেমেন্ট স্ক্যান",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF1E40AF)
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            if (upiQrBitmap != null) {
                                                Image(
                                                    bitmap = upiQrBitmap,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(120.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("UPI ID", fontSize = 9.sp, color = Color(0xFF64748B))
                                        }
                                    }

                                    // WhatsApp QR Column
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = Color(0xFFECFDF5),
                                        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFA7F3D0)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.padding(8.dp)
                                        ) {
                                            Text(
                                                text = "ডকুমেন্ট পাঠান",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF065F46)
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            if (waQrBitmap != null) {
                                                Image(
                                                    bitmap = waQrBitmap,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(120.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("WhatsApp", fontSize = 9.sp, color = Color(0xFF64748B))
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // UPI ID copy box
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "UPI ID: ${ShopConstants.UPI_ID}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFEFF6FF),
                                    modifier = Modifier.clickable {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(ClipData.newPlainText("UPI ID", ShopConstants.UPI_ID))
                                        Toast.makeText(context, "UPI ID কপি হয়েছে: ${ShopConstants.UPI_ID}", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copy",
                                            tint = BrandNavy,
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text("কপি", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Supported Payment Apps strip
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("GPay", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = GPayBlue)
                            Text("•", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            Text("Paytm", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0284C7))
                            Text("•", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            Text("PhonePe", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7C3AED))
                            Text("•", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            Text("BHIM UPI", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF059669))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Footer tag
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF0F172A),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "আধার • প্যান কার্ড • AEPS টাকা তোলা • PVC কার্ড প্রিন্ট • জমির পর্চা",
                                color = Color(0xFFE2E8F0),
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Action Buttons Row below Poster
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onPrintOrSharePoster,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandNavy),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("print_poster_btn")
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("পোস্টার শেয়ার", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onShareShopWhatsApp,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandEmerald),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("share_whatsapp_btn")
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("কাস্টমারকে শেয়ার", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
