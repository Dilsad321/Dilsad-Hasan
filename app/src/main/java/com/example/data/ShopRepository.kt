package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class ShopRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("tamim_online_prefs", Context.MODE_PRIVATE)

    // Complete list of all 9 shop services
    val allServices: List<ShopService> = listOf(
        ShopService(
            id = 1,
            category = ServiceCategory.CARD_PRINT,
            title = "আধার ও প্যান কার্ড",
            sub = "নতুন আবেদন, সংশোধন ও ডাউনলোড",
            iconKey = "id_card",
            time = "দ্রুত অনলাইন প্রসেসিং",
            description = "নতুন প্যান কার্ড তৈরি, প্যান কার্ড সংশোধন, আধার কার্ড ডাউনলোড, মোবাইল লিংক চেক এবং আধারের সাথে প্যান লিংক করা হয়।",
            documents = listOf(
                "আধার কার্ডের কপি বা নম্বর",
                "২ কপি পাসপোর্ট সাইজ রঙিন ছবি",
                "জন্ম প্রমাণের সার্টিফিকেট বা মাধ্যমিকের অ্যাডমিট",
                "সচল মোবাইল নম্বর"
            ),
            primaryColorHex = 0xFFE11D48,
            accentColorHex = 0xFFDC2626
        ),
        ShopService(
            id = 2,
            category = ServiceCategory.BANKING_BILL,
            title = "AEPS টাকা তোলা ও ব্যাংকিং",
            sub = "আধারের মাধ্যমে টাকা তোলা হয়",
            iconKey = "bank",
            time = "সাথে সাথে ক্যাশ পেমেন্ট",
            description = "আঙুলের ছাপ (Biometric) দিয়ে যেকোনো ব্যাংকের অ্যাকাউন্ট থেকে আধার কার্ডের মাধ্যমে টাকা তোলা, ব্যালেন্স চেক ও মানি ট্রান্সফার করা হয়।",
            documents = listOf(
                "আধার নম্বর (ব্যাংকের সাথে লিংক থাকা আবশ্যক)",
                "ব্যাংকের নাম",
                "গ্রাহকের উপস্থিতি ও আঙুলের ছাপ"
            ),
            primaryColorHex = 0xFF2563EB,
            accentColorHex = 0xFF1D4ED8
        ),
        ShopService(
            id = 3,
            category = ServiceCategory.CARD_PRINT,
            title = "PVC কার্ড প্রিন্ট",
            sub = "ঝকঝকে প্লাস্টিক ওয়াটারপ্রুফ কার্ড",
            iconKey = "badge",
            time = "মাত্র ৫ মিনিটে ডেলিভারি",
            description = "আধার কার্ড, প্যান কার্ড, ভোটার কার্ড, ড্রাইভিং লাইসেন্স, স্বাস্থ্য সাথী ও আইডেন্টিটি কার্ডের হাই-কোয়ালিটি PVC প্লাস্টিক কার্ড প্রিন্ট করা হয়।",
            documents = listOf(
                "কার্ডের PDF ফাইল বা পরিষ্কার ছবি",
                "অথবা অরিজিনাল কার্ড স্ক্যান করে প্রিন্ট করা হয়"
            ),
            primaryColorHex = 0xFF7C3AED,
            accentColorHex = 0xFF6D28D9
        ),
        ShopService(
            id = 4,
            category = ServiceCategory.GOVT_LAND,
            title = "স্কলারশিপ ও কলেজ ভর্তি",
            sub = "ঐক্যশ্রী, স্বামী বিবেকানন্দ ও ভর্তি",
            iconKey = "school",
            time = "সঠিক ফর্ম ফিলাপ গ্যারান্টি",
            description = "স্কুল ও কলেজের অনলাইন ভর্তি এবং ঐক্যশ্রী (Aikyashree), স্বামী বিবেকানন্দ (SVMCM), OASIS ও ন্যাশনাল স্কলারশিপের নির্ভুল আবেদন করা হয়।",
            documents = listOf(
                "শেষ পরীক্ষার মার্কশিট ও ভর্তির রিসিপ্ট",
                "ব্যাংক পাসবইয়ের প্রথম পাতার কপি",
                "আধার কার্ড ও পাসপোর্ট সাইজ ছবি",
                "ইনকাম সার্টিফিকেট ও মোবাইল নম্বর"
            ),
            primaryColorHex = 0xFFD97706,
            accentColorHex = 0xFFB45309
        ),
        ShopService(
            id = 5,
            category = ServiceCategory.GOVT_LAND,
            title = "জমির পর্চা, খাজনা ও দাগ",
            sub = "ডিজিটাল ROR পর্চা ও দাগের তথ্য",
            iconKey = "landscape",
            time = "অনলাইন ডিজিটাল কপি",
            description = "বাংলার ভূমি পোর্টাল থেকে জমির ডিজিটাল সার্টিফাইড পর্চা (ROR), দাগের ও খতিয়ানের তথ্য বার করা এবং অনলাইনে জমির খাজনা জমা দেওয়া হয়।",
            documents = listOf(
                "মৌজার নাম ও জে.এল (J.L.) নম্বর",
                "খতিয়ান নম্বর অথবা দাগ নম্বর",
                "জমির মালিকের নাম ও মোবাইল নম্বর"
            ),
            primaryColorHex = 0xFF059669,
            accentColorHex = 0xFF047857
        ),
        ShopService(
            id = 6,
            category = ServiceCategory.GOVT_LAND,
            title = "জন্ম-মৃত্যু ও BDO সার্টিফিকেট",
            sub = "Birth/Death, ইনকাম ও SC/ST/OBC",
            iconKey = "description",
            time = "সরকারি পোর্টাল আবেদন",
            description = "জন্ম ও মৃত্যু সার্টিফিকেট ডাউনলোড ও আবেদন, BDO ইনকাম সার্টিফিকেট, ডোমিসাইল এবং SC/ST/OBC জাতিগত শংসাপত্রের অনলাইন কাজ করা হয়।",
            documents = listOf(
                "আধার কার্ড ও ভোটার কার্ড",
                "পঞ্চায়েত প্রধানের ইনকাম বা রেসিডেন্সিয়াল সার্টিফিকেট",
                "পাসপোর্ট সাইজ ছবি ও মোবাইল নম্বর"
            ),
            primaryColorHex = 0xFF0284C7,
            accentColorHex = 0xFF0369A1
        ),
        ShopService(
            id = 7,
            category = ServiceCategory.GOVT_LAND,
            title = "ট্রেড লাইসেন্স ও উদ্যম",
            sub = "পঞ্চায়েত ট্রেড লাইসেন্স ও MSME",
            iconKey = "business_center",
            time = "সঙ্গে সঙ্গে লাইসেন্স ডাউনলোড",
            description = "শিল্পসাথী পোর্টাল থেকে পঞ্চায়েত এলাকার ব্যবসার নতুন ট্রেড লাইসেন্স ও রিনিউয়াল এবং আধার উদ্যম (Udyam Registration) সার্টিফিকেট করা হয়।",
            documents = listOf(
                "আধার কার্ড ও প্যান কার্ড",
                "দোকান বা ব্যবসার জায়গার খাজনা রশিদ / পর্চা / ভাড়ার চুক্তি",
                "ব্যবসার নাম ও মোবাইল নম্বর"
            ),
            primaryColorHex = 0xFF4F46E5,
            accentColorHex = 0xFF4338CA
        ),
        ShopService(
            id = 8,
            category = ServiceCategory.BANKING_BILL,
            title = "ইলেকট্রিসিটি বিল ও রিচার্জ",
            sub = "WBSEDCL বিল, মোবাইল ও DTH",
            iconKey = "bolt",
            time = "ইনস্ট্যান্ট পাকা রিসিপ্ট",
            description = "পশ্চিমবঙ্গ রাজ্য বিদ্যুৎ বণ্টন কোম্পানির (WBSEDCL) ইলেকট্রিক বিল ভরা এবং সমস্ত কোম্পানির মোবাইল ও টিভি (DTH) রিচার্জ করা হয়।",
            documents = listOf(
                "ইলেকট্রিক বিলের Consumer ID বা পুরনো বিল",
                "কাস্টমারের মোবাইল নম্বর"
            ),
            primaryColorHex = 0xFFEAB308,
            accentColorHex = 0xFFCA8A04
        ),
        ShopService(
            id = 9,
            category = ServiceCategory.CARD_PRINT,
            title = "পাসপোর্ট ছবি, জেরক্স ও প্রিন্ট",
            sub = "আর্জেন্ট ফটো, কালার প্রিন্ট ও ল্যামিনেশন",
            iconKey = "print",
            time = "৫ মিনিটে আর্জেন্ট ডেলিভারি",
            description = "ঝকঝকে পাসপোর্ট সাইজ ছবি তৈরি, হোয়াটসঅ্যাপ থেকে ডকুমেন্ট প্রিন্ট (সাদা-কালো ও কালার), জেরক্স, স্ক্যান এবং ডকুমেন্ট ল্যামিনেশন করা হয়।",
            documents = listOf(
                "সরাসরি দোকানে এসে ছবি তুলতে পারেন",
                "অথবা হোয়াটসঅ্যাপে (7478654044) ছবি/PDF পাঠাতে পারেন"
            ),
            primaryColorHex = 0xFF0D9488,
            accentColorHex = 0xFF0F766E
        )
    )

    private val _workRecords = MutableStateFlow<List<WorkStatusRecord>>(emptyList())
    val workRecords: StateFlow<List<WorkStatusRecord>> = _workRecords.asStateFlow()

    init {
        loadWorkRecords()
    }

    private fun loadWorkRecords() {
        val savedJson = prefs.getString("saved_work_records", null)
        if (savedJson != null) {
            try {
                val list = mutableListOf<WorkStatusRecord>()
                val array = JSONArray(savedJson)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        WorkStatusRecord(
                            token = obj.getString("token"),
                            name = obj.getString("name"),
                            phone = obj.getString("phone"),
                            service = obj.getString("service"),
                            stage = obj.getInt("stage"),
                            date = obj.optString("date", "আজ"),
                            note = obj.optString("note", "")
                        )
                    )
                }
                _workRecords.value = list
                return
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Default initial records
        val defaults = listOf(
            WorkStatusRecord(
                token = "TOC-101",
                name = "সোহেল রানা",
                phone = "9800000001",
                service = "PVC কার্ড প্রিন্ট ও প্যান কার্ড",
                stage = 3,
                date = "আজ",
                note = "আপনার PVC কার্ড প্রিন্ট সম্পূর্ণ রেডি। দোকান থেকে সংগ্রহ করুন।"
            ),
            WorkStatusRecord(
                token = "TOC-102",
                name = "মৌসুমী খাতুন",
                phone = "9800000002",
                service = "ঐক্যশ্রী স্কলারশিপ আবেদন",
                stage = 2,
                date = "গতকাল",
                note = "অনলাইন ভেরিফিকেশন চলছে, অ্যাপ্লিকেশন প্রিন্ট কপি রেডি।"
            ),
            WorkStatusRecord(
                token = "TOC-103",
                name = "রফিকুল ইসলাম",
                phone = "9800000003",
                service = "পঞ্চায়েত ট্রেড লাইসেন্স",
                stage = 3,
                date = "আজ",
                note = "ট্রেড লাইসেন্স ডাউনলোড হয়ে গেছে ও ল্যামিনেশন রেডি।"
            )
        )
        _workRecords.value = defaults
        saveWorkRecords(defaults)
    }

    private fun saveWorkRecords(list: List<WorkStatusRecord>) {
        try {
            val array = JSONArray()
            for (rec in list) {
                val obj = JSONObject().apply {
                    put("token", rec.token)
                    put("name", rec.name)
                    put("phone", rec.phone)
                    put("service", rec.service)
                    put("stage", rec.stage)
                    put("date", rec.date)
                    put("note", rec.note)
                }
                array.put(obj)
            }
            prefs.edit().putString("saved_work_records", array.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addWorkRecord(name: String, phone: String, service: String, stage: Int, note: String): WorkStatusRecord {
        val current = _workRecords.value
        val nextNumber = 101 + current.size
        val newRecord = WorkStatusRecord(
            token = "TOC-$nextNumber",
            name = name.trim(),
            phone = phone.trim(),
            service = service.trim(),
            stage = stage,
            date = "আজ",
            note = if (note.isNotBlank()) note.trim() else "আপনার কাজটি তামিম অনলাইন সেন্টারে নথিভুক্ত হয়েছে।"
        )
        val updated = listOf(newRecord) + current
        _workRecords.value = updated
        saveWorkRecords(updated)
        return newRecord
    }

    fun updateRecordStage(token: String, newStage: Int) {
        val updated = _workRecords.value.map { rec ->
            if (rec.token == token) {
                val updatedNote = if (newStage == 3) {
                    "আপনার কাজটি সম্পূর্ণ রেডি! দোকান থেকে সংগ্রহ করুন।"
                } else rec.note
                rec.copy(stage = newStage, note = updatedNote)
            } else rec
        }
        _workRecords.value = updated
        saveWorkRecords(updated)
    }

    fun deleteWorkRecord(token: String) {
        val updated = _workRecords.value.filter { it.token != token }
        _workRecords.value = updated
        saveWorkRecords(updated)
    }

    // Smart Bot response helper
    fun processBotQuery(userQuery: String): ChatMessage {
        val q = userQuery.lowercase()
        val currentRecords = _workRecords.value

        // 1. Check if token or phone matches work records
        val matched = currentRecords.find { rec ->
            q.contains(rec.token.lowercase()) ||
            q.contains(rec.token.replace("-", "").lowercase()) ||
            (rec.phone.isNotBlank() && q.contains(rec.phone)) ||
            (rec.name.isNotBlank() && q.contains(rec.name.lowercase()))
        }

        if (matched != null) {
            val stages = listOf("১. আবেদন জমা হয়েছে", "২. প্রসেসিং চলছে", "৩. সম্পূর্ণ রেডি (ডেলিভারি)")
            val stageDesc = stages.getOrElse(matched.stage - 1) { "প্রসেসিং" }
            val answer = "🔎 আপনার কাজের লাইভ স্ট্যাটাস পাওয়া গেছে!\n\n" +
                    "• টোকেন: ${matched.token}\n" +
                    "• গ্রাহকের নাম: ${matched.name}\n" +
                    "• সার্ভিস: ${matched.service}\n" +
                    "• বর্তমান অবস্থা: $stageDesc\n" +
                    "• নোট: ${matched.note}"

            return ChatMessage(
                isUser = false,
                text = answer,
                actionButtons = listOf(
                    ChatAction("📊 স্ট্যাটাস পেজে যান", ChatActionType.OPEN_TAB, "status"),
                    ChatAction("📞 দোকানে কল করুন", ChatActionType.CALL_PHONE, ShopConstants.RAW_PHONE)
                )
            )
        }

        // 2. Specific Service inquiries
        val matchingService = when {
            q.contains("প্যান") || q.contains("pan") || q.contains("আধার") || q.contains("aadhaar") -> allServices[0]
            q.contains("টাকা তোলা") || q.contains("aeps") || q.contains("ব্যাংক") || q.contains("ব্যালেন্স") -> allServices[1]
            q.contains("pvc") || q.contains("পিভিসি") || q.contains("প্লাস্টিক") || q.contains("কার্ড প্রিন্ট") -> allServices[2]
            q.contains("স্কলারশিপ") || q.contains("scholarship") || q.contains("ঐক্যশ্রী") || q.contains("বিবেকানন্দ") -> allServices[3]
            q.contains("পর্চা") || q.contains("খাজনা") || q.contains("জমি") || q.contains("দাগ") || q.contains("খতিয়ান") -> allServices[4]
            q.contains("জন্ম") || q.contains("মৃত্যু") || q.contains("bdo") || q.contains("ইনকাম") || q.contains("জাতিগত") || q.contains("obc") -> allServices[5]
            q.contains("ট্রেড") || q.contains("trade") || q.contains("লাইসেন্স") || q.contains("উদ্যম") -> allServices[6]
            q.contains("বিল") || q.contains("ইলেকট্রিক") || q.contains("বিদ্যুৎ") || q.contains("রিচার্জ") -> allServices[7]
            q.contains("ছবি") || q.contains("ফটো") || q.contains("জেরক্স") || q.contains("ল্যামিনেশন") || q.contains("প্রিন্ট") -> allServices[8]
            else -> null
        }

        if (matchingService != null) {
            val docsFormatted = matchingService.documents.joinToString("\n") { "  ✓ $it" }
            val answer = "📌 ${matchingService.title}\n" +
                    "⏱️ সময়: ${matchingService.time}\n\n" +
                    "${matchingService.description}\n\n" +
                    "📋 প্রয়োজনীয় ডকুমেন্টস:\n$docsFormatted"

            return ChatMessage(
                isUser = false,
                text = answer,
                actionButtons = listOf(
                    ChatAction("📄 সম্পূর্ণ কার্ড দেখুন", ChatActionType.OPEN_SERVICE, matchingService.id.toString()),
                    ChatAction("💬 WhatsApp-এ ডকুমেন্ট পাঠান", ChatActionType.WHATSAPP, "আমি ${matchingService.title} সার্ভিসটি নিতে চাই।")
                )
            )
        }

        // 3. Payment / UPI info
        if (q.contains("পেমেন্ট") || q.contains("upi") || q.contains("gpay") || q.contains("google pay") || q.contains("টাকা") || q.contains("qr")) {
            val answer = "💳 অনলাইন পেমেন্ট ও UPI তথ্য:\n\n" +
                    "• দোকানের নাম: ${ShopConstants.SHOP_NAME_EN}\n" +
                    "• Google Pay / PhonePe নম্বর: ${ShopConstants.RAW_PHONE}\n" +
                    "• অফিশিয়াল UPI ID: ${ShopConstants.UPI_ID}\n\n" +
                    "অ্যাপের 'QR ও পেমেন্ট' ট্যাব থেকে আপনি যেকোনো অ্যামাউন্ট দিয়ে সরাসরি QR স্ক্যান বা পেমেন্ট অ্যাপ খুলতে পারবেন।"

            return ChatMessage(
                isUser = false,
                text = answer,
                actionButtons = listOf(
                    ChatAction("📲 QR স্ক্যান ও পেমেন্ট", ChatActionType.OPEN_TAB, "qr"),
                    ChatAction("📋 UPI ID কপি করুন", ChatActionType.COPY_UPI, ShopConstants.UPI_ID)
                )
            )
        }

        // 4. Address, Contact, Timings, Map & Directions
        if (q.contains("ঠিকানা") || q.contains("কোথায়") || q.contains("সময়") || q.contains("সময়") || q.contains("ফোন") || q.contains("নম্বর") || q.contains("মালিক") || q.contains("দিলসাদ") || q.contains("ম্যাপ") || q.contains("map") || q.contains("লোকেশন") || q.contains("location")) {
            val answer = "🏪 তামিম অনলাইন সেন্টার (CSC) তথ্য:\n\n" +
                    "• প্রোঃ ${ShopConstants.PROPRIETOR_NAME}\n" +
                    "• CSC ID: ${ShopConstants.CSC_ID}\n" +
                    "• ঠিকানা: ${ShopConstants.ADDRESS_FULL}\n" +
                    "• গুগল ম্যাপস লিংক: ${ShopConstants.MAPS_URL}\n" +
                    "• মোবাইল ও WhatsApp: ${ShopConstants.PHONE_NUMBER}\n" +
                    "• খোলা থাকে: ${ShopConstants.OPENING_HOURS}"

            return ChatMessage(
                isUser = false,
                text = answer,
                actionButtons = listOf(
                    ChatAction("🗺️ গুগল ম্যাপে ডিরেকশন", ChatActionType.OPEN_MAP, ShopConstants.MAPS_URL),
                    ChatAction("📞 কল করুন", ChatActionType.CALL_PHONE, ShopConstants.RAW_PHONE),
                    ChatAction("💬 WhatsApp চ্যাট", ChatActionType.WHATSAPP, "নমস্কার তামিম অনলাইন সেন্টার"),
                    ChatAction("📍 যোগাযোগ পেজ", ChatActionType.OPEN_TAB, "contact")
                )
            )
        }

        // 5. Default welcoming response with options
        val serviceListSummary = allServices.mapIndexed { idx, s -> "${idx + 1}. ${s.title}" }.joinToString("\n")
        val answer = "নমস্কার! 🙏 আমি তামিম অনলাইন সেন্টার-এর ডিজিটাল সহায়ক।\n\n" +
                "আমাদের দোকানে নিচের সমস্ত ডিজিটাল কাজ দ্রুত ও যত্ন সহকারে করা হয়:\n\n" +
                "$serviceListSummary\n\n" +
                "আপনার কাজের স্ট্যাটাস জানতে টোকেন আইডি (যেমন TOC-101) লিখুন অথবা নিচের যেকোনো বিষয়ে ট্যাপ করুন:"

        return ChatMessage(
            isUser = false,
            text = answer,
            actionButtons = listOf(
                ChatAction("🪪 প্যান ও আধার", ChatActionType.OPEN_SERVICE, "1"),
                ChatAction("💳 PVC প্রিন্ট", ChatActionType.OPEN_SERVICE, "3"),
                ChatAction("🔍 TOC-101 স্ট্যাটাস", ChatActionType.SEARCH_TOKEN, "TOC-101"),
                ChatAction("📲 Google Pay QR", ChatActionType.OPEN_TAB, "qr")
            )
        )
    }
}
