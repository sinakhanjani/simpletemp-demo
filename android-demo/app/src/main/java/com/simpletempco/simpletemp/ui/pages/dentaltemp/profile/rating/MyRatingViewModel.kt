package com.simpletempco.simpletemp.ui.pages.dentaltemp.profile.rating

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.response.Rating
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.AppConfig.RATING_QUERY_PER_PAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRatingViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _ratingResult = MutableLiveData<Rating?>()
    val ratingResult get() = _ratingResult

    fun getRating(
        page: Int,
        needLoading: Boolean = true
    ) = viewModelScope.launch {
        if (needLoading) onLoading(true)
        repo.getRating(
            RATING_QUERY_PER_PAGE,
            page
        ).safeApiCall(true) { data ->
            _ratingResult.value = data.data
        }
    }

}