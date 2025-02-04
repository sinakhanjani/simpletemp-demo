package com.simpletempco.simpletemp.ui.pages.clinic.money.review

import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.ReviewReq
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    fun review(
        shiftId: String?,
        rate: Int?,
        competencyAndSkills: Int?,
        timekeeping: Int?,
        professionalism: Int?,
        description: String?,
        callback: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.rateDentalTemp(
            ReviewReq(
                shiftId = shiftId,
                rate = rate,
                competencyAndSkills = competencyAndSkills,
                timekeeping = timekeeping,
                professionalism = professionalism,
                description = description
            )
        ).safeApiCall {
            callback.invoke()
        }
    }

}