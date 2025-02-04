package com.simpletempco.simpletemp.util

import com.simpletempco.simpletemp.data.remote.models.response.Invoice
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.ui.custom.dialogs.RatingDialog

interface MessageDialogCallback {
    fun onDone()
    fun onCancel()
}

interface RatingDialogCallback {
    fun onSubmit(ratingDialog: RatingDialog)
}

interface SimpleCalendarCallback {
    fun onDateSelect(date: String)
    fun onChangeMonth(date: String?)
}

interface SimpleCalendarItemCallback {
    fun onDaySelect(day: Int)
}

interface ShiftAdapterCallback {
    fun onItemClick(shift: Shift)
}

interface InvoiceAdapterCallback {
    fun onPayNowClicked(invoice: Invoice)
    fun onPayManuallyClicked(invoice: Invoice)
    fun onPaidItemClicked(invoice: Invoice)
}

interface ShiftsAdapterCallback {
    fun onPendingItemClicked(shift: Shift)
    fun onConfirmedItemClicked(shift: Shift)
}

interface AppRatingBarChangeListener {
    fun onRatingChange(rating: Int)
}