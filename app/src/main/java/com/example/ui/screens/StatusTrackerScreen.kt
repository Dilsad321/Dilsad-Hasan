package com.example.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ShopConstants
import com.example.data.ShopRepository
import com.example.data.WorkStatusRecord
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandNavy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusTrackerScreen(
    repository: ShopRepository,
    records: List<WorkStatusRecord>,
    initialQuery: String = "",
    onSendCustomerWhatsApp: (WorkStatusRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf(initialQuery) }

    // Admin state
    var showAdminPanel by remember { mutableStateOf(false) }
    var isAdminUnlocked by remember { mutableStateOf(false) }
    var adminPinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    // Add new work state
    var newCustName by remember { mutableStateOf("") }
    var newCustPhone by remember { mutableStateOf("") }
    var newCustService by remember { mutableStateOf("প্যান কার্ড (PAN Card)") }
    var newCustStage by remember { mutableStateOf(1) }
    var newCustNote by remember { mutableStateOf("") }
    var serviceDropdownExpanded by remember { mutableStateOf(false) }
    var stageDropdownExpanded by remember { mutableStateOf(false) }

    val availableServices = listOf(
        "প্যান কার্ড (PAN Card)",
        "PVC কার্ড প্রিন্ট",
        "জমির পর্চা / খাজনা",
        "ট্রেড লাইসেন্স",
        "BDO ইনকাম সার্টিফিকেট",
        "জন্ম/মৃত্যু সার্টিফিকেট",
        "ঐক্যশ্রী / স্কলারশিপ",
        "জাতিগত সার্টিফিকেট (SC/ST/OBC)",
        "AEPS টাকা তোলা"
    )

    val filteredRecords = remember(records, searchQuery) {
        if (searchQuery.isBlank() || searchQuery.equals("all", ignoreCase = true)) {
            records
        } else {
            records.filter { r ->
                r.token.contains(searchQuery, ignoreCase = true) ||
                r.phone.contains(searchQuery, ignoreCase = true) ||
                r.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search Box Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "কাস্টমার কাজের স্ট্যাটাস",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "মোবাইল নম্বর বা টোকেন আইডি দিয়ে চেক করুন",
                                fontSize = 11.5.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF1F5F9),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                            modifier = Modifier
                                .clickable { showAdminPanel = !showAdminPanel }
                                .testTag("toggle_admin_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = "Admin",
                                    tint = BrandNavy,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isAdminUnlocked) "অ্যাডমিন ✓" else "অ্যাডমিন",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandNavy
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text("মোবাইল বা টোকেন (যেমন 9800000001 / TOC-101)", fontSize = 12.sp)
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandNavy,
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("status_query_input")
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { /* state filter auto applies */ },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandNavy),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                            modifier = Modifier.testTag("status_search_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("খুঁজুন", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick lookups
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("চেক করে দেখুন:", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFEFF6FF),
                            modifier = Modifier.clickable { searchQuery = "TOC-101" }
                        ) {
                            Text(
                                "TOC-101 (রেডি)",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1D4ED8),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEF3C7),
                            modifier = Modifier.clickable { searchQuery = "TOC-102" }
                        ) {
                            Text(
                                "TOC-102 (চলছে)",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFB45309),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF1F5F9),
                            modifier = Modifier.clickable { searchQuery = "" }
                        ) {
                            Text(
                                "সব দেখুন",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF475569),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        // Admin Panel Drawer / Card
        if (showAdminPanel) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (!isAdminUnlocked) {
                            // PIN unlock view
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = BrandGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "দোকানদার অ্যাডমিন লগইন",
                                        color = BrandGold,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = "বন্ধ করুন",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    modifier = Modifier.clickable { showAdminPanel = false }
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "নতুন কাজ যোগ বা স্ট্যাটাস আপডেট করতে পিন দিন (ডিফল্ট: 4044)",
                                color = Color(0xFFCBD5E1),
                                fontSize = 11.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = adminPinInput,
                                    onValueChange = {
                                        adminPinInput = it
                                        pinError = false
                                    },
                                    placeholder = { Text("পিন (4044)", color = Color(0xFF64748B), fontSize = 12.sp) },
                                    singleLine = true,
                                    visualTransformation = PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = BrandGold,
                                        unfocusedBorderColor = Color(0xFF475569),
                                        focusedContainerColor = Color(0xFF1E293B),
                                        unfocusedContainerColor = Color(0xFF1E293B)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("admin_pin_input")
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        if (adminPinInput.trim() == ShopConstants.ADMIN_DEFAULT_PIN) {
                                            isAdminUnlocked = true
                                            pinError = false
                                            Toast.makeText(context, "অ্যাডমিন প্যানেল আনলক হয়েছে!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            pinError = true
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BrandGold),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.testTag("admin_unlock_btn")
                                ) {
                                    Text("আনলক", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }

                            if (pinError) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "ভুল পিন! অনুগ্রহ করে 4044 দিন।",
                                    color = Color(0xFFF87171),
                                    fontSize = 11.sp
                                )
                            }
                        } else {
                            // Unlocked admin controls
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "অ্যাডমিন ড্যাশবোর্ড (${ShopConstants.PROPRIETOR_NAME})",
                                        color = Color(0xFF34D399),
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "নতুন কাস্টমারের কাজ যুক্ত করুন",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 11.sp
                                    )
                                }
                                Text(
                                    text = "লক করুন",
                                    color = Color(0xFFF87171),
                                    fontSize = 11.sp,
                                    modifier = Modifier.clickable {
                                        isAdminUnlocked = false
                                        adminPinInput = ""
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Name & Phone fields
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = newCustName,
                                    onValueChange = { newCustName = it },
                                    placeholder = { Text("নাম (উদাঃ রাহুল)", fontSize = 11.sp, color = Color(0xFF64748B)) },
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedContainerColor = Color(0xFF1E293B),
                                        unfocusedContainerColor = Color(0xFF1E293B),
                                        focusedBorderColor = BrandGold,
                                        unfocusedBorderColor = Color(0xFF475569)
                                    ),
                                    modifier = Modifier.weight(1f)
                                )

                                OutlinedTextField(
                                    value = newCustPhone,
                                    onValueChange = { newCustPhone = it },
                                    placeholder = { Text("মোবাইল নম্বর", fontSize = 11.sp, color = Color(0xFF64748B)) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedContainerColor = Color(0xFF1E293B),
                                        unfocusedContainerColor = Color(0xFF1E293B),
                                        focusedBorderColor = BrandGold,
                                        unfocusedBorderColor = Color(0xFF475569)
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Service Selector
                            ExposedDropdownMenuBox(
                                expanded = serviceDropdownExpanded,
                                onExpandedChange = { serviceDropdownExpanded = !serviceDropdownExpanded }
                            ) {
                                OutlinedTextField(
                                    value = newCustService,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("সার্ভিস", color = Color(0xFF94A3B8), fontSize = 10.sp) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceDropdownExpanded) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedContainerColor = Color(0xFF1E293B),
                                        unfocusedContainerColor = Color(0xFF1E293B),
                                        focusedBorderColor = BrandGold,
                                        unfocusedBorderColor = Color(0xFF475569)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = serviceDropdownExpanded,
                                    onDismissRequest = { serviceDropdownExpanded = false }
                                ) {
                                    availableServices.forEach { s ->
                                        DropdownMenuItem(
                                            text = { Text(s, fontSize = 12.sp) },
                                            onClick = {
                                                newCustService = s
                                                serviceDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Note field
                            OutlinedTextField(
                                value = newCustNote,
                                onValueChange = { newCustNote = it },
                                placeholder = { Text("নোট (উদাঃ দোকান থেকে সংগ্রহ করুন)", fontSize = 11.sp, color = Color(0xFF64748B)) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedContainerColor = Color(0xFF1E293B),
                                    unfocusedContainerColor = Color(0xFF1E293B),
                                    focusedBorderColor = BrandGold,
                                    unfocusedBorderColor = Color(0xFF475569)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (newCustName.isBlank() || newCustPhone.isBlank()) {
                                        Toast.makeText(context, "নাম ও ফোন নম্বর পূরণ করুন!", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    val created = repository.addWorkRecord(
                                        name = newCustName,
                                        phone = newCustPhone,
                                        service = newCustService,
                                        stage = newCustStage,
                                        note = newCustNote
                                    )
                                    newCustName = ""
                                    newCustPhone = ""
                                    newCustNote = ""
                                    Toast.makeText(context, "নতুন টোকেন ${created.token} যুক্ত হয়েছে!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandEmerald),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("save_customer_work_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("নতুন কাজ সেভ করুন", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                            }
                        }
                    }
                }
            }
        }

        // Work Records list
        if (filteredRecords.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "কোনো কাজের রেকর্ড পাওয়া যায়নি",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF334155)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "সঠিক মোবাইল নম্বর বা টোকেন আইডি দিয়ে খুঁজুন অথবা দোকানে যোগাযোগ করুন।",
                            fontSize = 11.5.sp,
                            color = Color(0xFF64748B),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        } else {
            items(filteredRecords) { record ->
                WorkRecordCard(
                    record = record,
                    isAdminUnlocked = isAdminUnlocked,
                    onUpdateStage = { stage -> repository.updateRecordStage(record.token, stage) },
                    onDelete = { repository.deleteWorkRecord(record.token) },
                    onSendWhatsApp = { onSendCustomerWhatsApp(record) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun WorkRecordCard(
    record: WorkStatusRecord,
    isAdminUnlocked: Boolean,
    onUpdateStage: (Int) -> Unit,
    onDelete: () -> Unit,
    onSendWhatsApp: () -> Unit
) {
    val stageLabels = listOf("আবেদন জমা", "কাজ চলছে", "সম্পূর্ণ রেডি")
    val badgeBg = when (record.stage) {
        3 -> Color(0xFFD1FAE5)
        2 -> Color(0xFFFEF3C7)
        else -> Color(0xFFDBEAFE)
    }
    val badgeTextColor = when (record.stage) {
        3 -> Color(0xFF065F46)
        2 -> Color(0xFF92400E)
        else -> Color(0xFF1E40AF)
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("work_card_${record.token}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = record.token,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF334155),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${record.name} (${record.phone})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = record.service,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2563EB)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = badgeBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, badgeTextColor.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = stageLabels.getOrElse(record.stage - 1) { "প্রসেসিং" },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3-Step Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (record.stage >= 1) Color(0xFF2563EB) else Color(0xFFE2E8F0))
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (record.stage >= 2) Color(0xFFF59E0B) else Color(0xFFE2E8F0))
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (record.stage >= 3) Color(0xFF10B981) else Color(0xFFE2E8F0))
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "১. জমা হয়েছে",
                    fontSize = 10.sp,
                    fontWeight = if (record.stage >= 1) FontWeight.Bold else FontWeight.Normal,
                    color = if (record.stage >= 1) Color(0xFF1D4ED8) else Color(0xFF94A3B8)
                )
                Text(
                    text = "২. কাজ চলছে",
                    fontSize = 10.sp,
                    fontWeight = if (record.stage >= 2) FontWeight.Bold else FontWeight.Normal,
                    color = if (record.stage >= 2) Color(0xFFB45309) else Color(0xFF94A3B8)
                )
                Text(
                    text = "৩. ডেলিভারি রেডি",
                    fontSize = 10.sp,
                    fontWeight = if (record.stage >= 3) FontWeight.Bold else FontWeight.Normal,
                    color = if (record.stage >= 3) Color(0xFF047857) else Color(0xFF94A3B8)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Remark Note
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = record.note.ifBlank { "কাজটি তামিম অনলাইন সেন্টারে প্রসেসিংয়ে আছে।" },
                        fontSize = 12.sp,
                        color = Color(0xFF334155),
                        lineHeight = 16.sp
                    )
                }
            }

            // Admin Actions Row (if unlocked)
            if (isAdminUnlocked) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEF3C7),
                            modifier = Modifier.clickable { onUpdateStage(2) }
                        ) {
                            Text(
                                "চলছে",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFD1FAE5),
                            modifier = Modifier.clickable { onUpdateStage(3) }
                        ) {
                            Text(
                                "রেডি",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEE2E2),
                            modifier = Modifier.clickable { onDelete() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color(0xFFDC2626),
                                modifier = Modifier
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                                    .size(14.dp)
                            )
                        }
                    }

                    // Direct WhatsApp alert to customer
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BrandEmerald,
                        modifier = Modifier.clickable { onSendWhatsApp() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "গ্রাহককে মেসেজ",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
