package com.simpletempco.simpletemp.ui.custom.views.calendar

data class SimpleDay(
    val day: Int,
    val tag: String?,
    var isSelect: Boolean = false,
    var isToday: Boolean = false
)
