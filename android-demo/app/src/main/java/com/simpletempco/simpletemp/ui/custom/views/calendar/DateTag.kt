package com.simpletempco.simpletemp.ui.custom.views.calendar

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DateTag(
    val date: String?,
    val tag: String?
) : Parcelable
