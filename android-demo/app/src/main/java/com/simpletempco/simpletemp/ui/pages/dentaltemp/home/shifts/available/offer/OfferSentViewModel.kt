package com.simpletempco.simpletemp.ui.pages.dentaltemp.home.shifts.available.offer

import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferSentViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    fun cancelOffer(
        id: String,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.cancelOffer(id).safeApiCall {
            callback.invoke()
        }
    }

}