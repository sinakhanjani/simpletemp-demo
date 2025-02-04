package com.simpletempco.simpletemp.ui.pages.clinic.notif

import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.ShiftIdReq
import com.simpletempco.simpletemp.data.remote.models.response.Notif
import com.simpletempco.simpletemp.data.remote.models.response.Offer
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.AppConfig.NOTIFICATION_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClinicNotificationViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _notificationsResult = SingleLiveEvent<List<Notif>?>()
    val notificationsResult get() = _notificationsResult

    fun getNotifications(
        page: Int
    ) = viewModelScope.launch {
        if (page == 0) onLoading(true)
        repo.clinicNotification(
            NOTIFICATION_QUERY_PER_PAGE,
            page
        ).safeApiCall(page == 0) { data ->
            _notificationsResult.value = data.data
        }
    }

    fun getOfferDetails(
        shiftId: String,
        callback: (offer: Offer?) -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.offer(ShiftIdReq(shiftId)).safeApiCall { data ->
            callback.invoke(data.data?.find { it.shift?.id == shiftId })
        }
    }

}