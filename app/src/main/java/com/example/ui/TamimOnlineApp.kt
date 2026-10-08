package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatActionType
import com.example.data.ShopConstants
import com.example.data.ShopRepository
import com.example.data.ShopService
import com.example.ui.components.ChatbotSheet
import com.example.ui.components.ServiceDetailSheet
import com.example.ui.components.ShopHeader
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.PaymentQrScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.screens.StatusTrackerScreen
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandNavy
import com.example.ui.theme.BrandNavyDark

enum class AppTab(val titleBn: String, val testId: String) {
    SERVICES("সার্ভিসসমূহ", "tab_services"),
    STATUS("কাজের স্ট্যাটাস", "tab_status"),
    QR("QR ও পেমেন্ট", "tab_qr"),
    CONTACT("যোগাযোগ", "tab_contact")
}

@Composable
fun TamimOnlineApp(
    repository: ShopRepository,
    currentUser: com.google.firebase.auth.FirebaseUser? = null,
    onSignOut: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentTab by remember { mutableStateOf(AppTab.SERVICES) }
    var selectedServiceForSheet by remember { mutableStateOf<ShopService?>(null) }
    var isChatbotOpen by remember { mutableStateOf(false) }
    var statusSearchFilter by remember { mutableStateOf("") }

    val workRecords by repository.workRecords.collectAsState()

    // BackHandler: if on sub-screens, return to Services tab first
    BackHandler(enabled = currentTab != AppTab.SERVICES) {
        currentTab = AppTab.SERVICES
    }

    // Helper functions for system intents
    fun makePhoneCall() {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${ShopConstants.RAW_PHONE}")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "কল করা যাচ্ছে না: ${ShopConstants.PHONE_NUMBER}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsAppChat(msg: String = "নমস্কার তামিম অনলাইন সেন্টার") {
        try {
            val encodedMsg = Uri.encode(msg)
            val uri = Uri.parse("https://wa.me/91${ShopConstants.RAW_PHONE}?text=$encodedMsg")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp খোলা সম্ভব হয়নি", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsAppToCustomer(phone: String, msg: String) {
        try {
            val cleanPhone = phone.replace("+", "").replace("-", "").trim()
            val encodedMsg = Uri.encode(msg)
            val uri = Uri.parse("https://wa.me/91$cleanPhone?text=$encodedMsg")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "মেসেজ পাঠানো সম্ভব হয়নি", Toast.LENGTH_SHORT).show()
        }
    }

    fun launchUpiIntent(upiUriString: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(upiUriString)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "কোনো পেমেন্ট অ্যাপ পাওয়া যায়নি। UPI ID কপি করে পে করুন।", Toast.LENGTH_LONG).show()
        }
    }

    fun shareShopDetails() {
        try {
            val shareText = """
                *${ShopConstants.SHOP_NAME_BN} (CSC)*
                প্রোঃ ${ShopConstants.PROPRIETOR_NAME}
                ঠিকানাঃ ${ShopConstants.ADDRESS_FULL}
                মোবাইল ও হোয়াটসঅ্যাপঃ ${ShopConstants.PHONE_NUMBER}
                Google Pay UPI ID: ${ShopConstants.UPI_ID}
                
                আধার, প্যান কার্ড, PVC কার্ড প্রিন্ট, AEPS টাকা তোলা, জমির পর্চা, স্কলারশিপ ও সমস্ত অনলাইন কাজ যত্ন সহকারে করা হয়।
            """.trimIndent()
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            context.startActivity(Intent.createChooser(intent, "তামিম অনলাইন সেন্টার শেয়ার করুন"))
        } catch (e: Exception) {
            Toast.makeText(context, "শেয়ার করা যায়নি", Toast.LENGTH_SHORT).show()
        }
    }

    fun openMapLocation() {
        try {
            val uri = Uri.parse(ShopConstants.MAPS_URL)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "ম্যাপ লোড করা যায়নি", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF1F5F9),
        floatingActionButton = {
            // Floating 24/7 AI Chatbot Button
            FloatingActionButton(
                onClick = { isChatbotOpen = true },
                containerColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .testTag("floating_chatbot_fab")
                    .shadow(10.dp, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF1E3A8A),
                                Color(0xFF2563EB),
                                Color(0xFF06B6D4)
                            )
                        )
                    )
                    .border(2.dp, Color.White, RoundedCornerShape(24.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = "AI Chatbot",
                            tint = BrandGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "24/7 সহায়ক",
                            color = Color(0xFFBAE6FD),
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 10.sp
                        )
                        Text(
                            text = "AI চ্যাট",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                shadowElevation = 12.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.White
            ) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 0.dp,
                    modifier = Modifier.height(72.dp)
                ) {
                    NavigationBarItem(
                        selected = currentTab == AppTab.SERVICES,
                        onClick = { currentTab = AppTab.SERVICES },
                        icon = {
                            Icon(imageVector = Icons.Default.Layers, contentDescription = "Services")
                        },
                        label = {
                            Text(text = AppTab.SERVICES.titleBn, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrandNavy,
                            selectedTextColor = BrandNavy,
                            indicatorColor = Color(0xFFEFF6FF),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF64748B)
                        ),
                        modifier = Modifier.testTag(AppTab.SERVICES.testId)
                    )

                    NavigationBarItem(
                        selected = currentTab == AppTab.STATUS,
                        onClick = { currentTab = AppTab.STATUS },
                        icon = {
                            Icon(imageVector = Icons.Default.AssignmentTurnedIn, contentDescription = "Status")
                        },
                        label = {
                            Text(text = AppTab.STATUS.titleBn, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrandNavy,
                            selectedTextColor = BrandNavy,
                            indicatorColor = Color(0xFFEFF6FF),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF64748B)
                        ),
                        modifier = Modifier.testTag(AppTab.STATUS.testId)
                    )

                    NavigationBarItem(
                        selected = currentTab == AppTab.QR,
                        onClick = { currentTab = AppTab.QR },
                        icon = {
                            Box {
                                Icon(imageVector = Icons.Default.QrCode, contentDescription = "QR")
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(BrandEmerald)
                                        .align(Alignment.TopEnd)
                                )
                            }
                        },
                        label = {
                            Text(text = AppTab.QR.titleBn, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrandNavy,
                            selectedTextColor = BrandNavy,
                            indicatorColor = Color(0xFFEFF6FF),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF64748B)
                        ),
                        modifier = Modifier.testTag(AppTab.QR.testId)
                    )

                    NavigationBarItem(
                        selected = currentTab == AppTab.CONTACT,
                        onClick = { currentTab = AppTab.CONTACT },
                        icon = {
                            Icon(imageVector = Icons.Default.HeadsetMic, contentDescription = "Contact")
                        },
                        label = {
                            Text(text = AppTab.CONTACT.titleBn, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrandNavy,
                            selectedTextColor = BrandNavy,
                            indicatorColor = Color(0xFFEFF6FF),
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF64748B)
                        ),
                        modifier = Modifier.testTag(AppTab.CONTACT.testId)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Persistent Brand Top Header
            ShopHeader(
                onOpenQr = { currentTab = AppTab.QR },
                onCallPhone = { makePhoneCall() },
                onOpenMap = { openMapLocation() }
            )

            // Screen Switcher
            Box(modifier = Modifier.weight(1f)) {
                when (currentTab) {
                    AppTab.SERVICES -> {
                        ServicesScreen(
                            services = repository.allServices,
                            onSelectService = { selectedServiceForSheet = it },
                            onNavigateToStatus = { currentTab = AppTab.STATUS }
                        )
                    }
                    AppTab.STATUS -> {
                        StatusTrackerScreen(
                            repository = repository,
                            records = workRecords,
                            initialQuery = statusSearchFilter,
                            onSendCustomerWhatsApp = { rec ->
                                val stageLabels = listOf("আবেদন জমা", "কাজ প্রসেসিং চলছে", "সম্পূর্ণ রেডি")
                                val stageText = stageLabels.getOrElse(rec.stage - 1) { "প্রসেসিং" }
                                val text = "নমস্কার ${rec.name},\nতামিম অনলাইন সেন্টার (মাড়গ্রাম) থেকে জানানো হচ্ছে আপনার '${rec.service}' (টোকেন: ${rec.token})-এর বর্তমান স্ট্যাটাস: *$stageText*।\nনোট: ${rec.note}\nযোগাযোগ: ${ShopConstants.RAW_PHONE}"
                                openWhatsAppToCustomer(rec.phone, text)
                            }
                        )
                    }
                    AppTab.QR -> {
                        PaymentQrScreen(
                            onDirectUpiPay = { upiUri -> launchUpiIntent(upiUri) },
                            onShareShopWhatsApp = { shareShopDetails() },
                            onPrintOrSharePoster = { shareShopDetails() }
                        )
                    }
                    AppTab.CONTACT -> {
                        ContactScreen(
                            currentUser = currentUser,
                            onSignOut = onSignOut,
                            onCallPhone = { makePhoneCall() },
                            onOpenWhatsApp = { openWhatsAppChat("নমস্কার তামিম অনলাইন সেন্টার") },
                            onOpenMap = { openMapLocation() }
                        )
                    }
                }
            }
        }
    }

    // Service Detail Modal Sheet
    if (selectedServiceForSheet != null) {
        ServiceDetailSheet(
            service = selectedServiceForSheet,
            onDismiss = { selectedServiceForSheet = null },
            onSendWhatsApp = { svc ->
                openWhatsAppChat("নমস্কার তামিম অনলাইন সেন্টার, আমি '${svc.title}' সার্ভিসটি নিতে চাই। এর জন্য কী করতে হবে জানাবেন।")
            },
            onGoToPayment = {
                currentTab = AppTab.QR
            }
        )
    }

    // 24/7 AI Chatbot Sheet
    if (isChatbotOpen) {
        ChatbotSheet(
            repository = repository,
            onDismiss = { isChatbotOpen = false },
            onExecuteAction = { action ->
                when (action.actionType) {
                    ChatActionType.OPEN_TAB -> {
                        when (action.payload) {
                            "services" -> currentTab = AppTab.SERVICES
                            "status" -> currentTab = AppTab.STATUS
                            "qr" -> currentTab = AppTab.QR
                            "contact" -> currentTab = AppTab.CONTACT
                        }
                    }
                    ChatActionType.OPEN_SERVICE -> {
                        val svcId = action.payload.toIntOrNull()
                        val target = repository.allServices.find { it.id == svcId }
                        if (target != null) {
                            selectedServiceForSheet = target
                        }
                    }
                    ChatActionType.SEARCH_TOKEN -> {
                        statusSearchFilter = action.payload
                        currentTab = AppTab.STATUS
                    }
                    ChatActionType.CALL_PHONE -> makePhoneCall()
                    ChatActionType.WHATSAPP -> openWhatsAppChat(action.payload)
                    ChatActionType.OPEN_MAP -> openMapLocation()
                    ChatActionType.COPY_UPI -> {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                        clipboard.setPrimaryClip(android.content.ClipData.newPlainText("UPI ID", action.payload))
                        Toast.makeText(context, "UPI ID কপি হয়েছে: ${action.payload}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }
}
