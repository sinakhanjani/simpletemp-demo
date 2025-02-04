package com.simpletempco.simpletemp.util

object AppConfig {

    const val BASE_URL = "https://simpletemp.co.uk/api/v1/"
    const val PRIVACY_POLICY_URL = "https://simpletemp.co.uk/privacy"
    const val TERMS_OF_SERVICE_URL = "https://simpletemp.co.uk/termsofservice"
    const val OPEN_DOCUMENT_BASE_URL = "https://drive.google.com/viewerng/viewer?embedded=true&url="

    const val SPLASH_DELAY = 1000L

    val SUSPEND_CODES = listOf(641, 644)
    const val ADMIN_SUSPEND_CODE = 403

    val DOCUMENTS_FORMAT_ALLOWED = arrayOf(
        "image/*",
        "application/pdf",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/msword"
    )

    const val FAQ_QUERY_PER_PAGE = 15
    const val MONEY_QUERY_PER_PAGE = 10
    const val OFFERS_QUERY_PER_PAGE = 10
    const val RATING_QUERY_PER_PAGE = 10
    const val INVOICE_QUERY_PER_PAGE = 10
    const val JOB_HISTORY_QUERY_PER_PAGE = 10
    const val NOTIFICATION_QUERY_PER_PAGE = 10
    const val CONFIRMED_SHIFTS_QUERY_PER_PAGE = 10

    const val MAP_ZOOM_LEVEL = 14F

    const val GIFT_ITEMS_COUNT = 10

}