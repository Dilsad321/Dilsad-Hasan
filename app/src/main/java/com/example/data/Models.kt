package com.example.data

enum class ServiceCategory(val displayNameBn: String) {
    ALL("সব সার্ভিস"),
    CARD_PRINT("কার্ড ও প্রিন্ট"),
    GOVT_LAND("সরকারি সার্টিফিকেট ও জমি"),
    BANKING_BILL("ব্যাংকিং ও বিল")
}

data class ShopService(
    val id: Int,
    val category: ServiceCategory,
    val title: String,
    val sub: String,
    val iconKey: String,
    val time: String,
    val description: String,
    val documents: List<String>,
    val primaryColorHex: Long,
    val accentColorHex: Long
)

data class WorkStatusRecord(
    val token: String,
    val name: String,
    val phone: String,
    val service: String,
    val stage: Int, // 1: Submitted (আবেদন জমা), 2: Processing (কাজ চলছে), 3: Ready (সম্পূর্ণ রেডি)
    val date: String,
    val note: String
)

enum class ChatActionType {
    OPEN_TAB,
    OPEN_SERVICE,
    SEARCH_TOKEN,
    CALL_PHONE,
    WHATSAPP,
    COPY_UPI,
    OPEN_MAP
}

data class ChatAction(
    val label: String,
    val actionType: ChatActionType,
    val payload: String
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val isUser: Boolean,
    val text: String,
    val actionButtons: List<ChatAction> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

object ShopConstants {
    const val SHOP_NAME_BN = "তামিম অনলাইন সেন্টার"
    const val SHOP_NAME_EN = "TAMIM ONLINE CENTRE"
    const val PROPRIETOR_NAME = "দিলসাদ হাসান (Dilsad Hasan)"
    const val CSC_ID = "612117330014"
    const val PHONE_NUMBER = "+91 7478654044"
    const val RAW_PHONE = "7478654044"
    const val UPI_ID = "7478654044@okbizaxis"
    const val ADDRESS_FULL = "মাড়গ্রাম, খাঁড়পুকুর পাড়া, বীরভূম, পিন - 731202"
    const val ADDRESS_SHORT = "মাড়গ্রাম, খাঁড়পুকুর পাড়া, বীরভূম (731202)"
    const val OPENING_HOURS = "প্রতিদিন সকাল ৮:০০ টা থেকে রাত ৯:০০ টা"
    const val ADMIN_DEFAULT_PIN = "4044"
    const val MAPS_URL = "https://maps.app.goo.gl/MdBi9eNL1VivCgwz9"
}
