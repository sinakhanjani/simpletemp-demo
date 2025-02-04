package com.simpletempco.simpletemp.ui.pages.dentaltemp.notif

import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.OfferIdReq
import com.simpletempco.simpletemp.data.remote.models.response.Notif
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.AppConfig.NOTIFICATION_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _notificationsResult = SingleLiveEvent<List<Notif>?>()
    val notificationsResult get() = _notificationsResult

    fun getNotifications(
        page: Int
    ) = viewModelScope.launch {
        if (page == 0) onLoading(true)
        repo.notification(
            NOTIFICATION_QUERY_PER_PAGE,
            page
        ).safeApiCall(page == 0) { data ->
            _notificationsResult.value = data.data
        }
    }

    fun resubmitOffer(
        offerId: String,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.resubmittedOffer(OfferIdReq(offerId)).safeApiCall {
            callback.invoke()
        }
    }

}