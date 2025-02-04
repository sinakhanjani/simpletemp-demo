package com.simpletempco.simpletemp.ui.pages.dentaltemp.jobs

import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.response.JobHistory
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.AppConfig.JOB_HISTORY_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobHistoryViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    private val _historyResult = SingleLiveEvent<JobHistory?>()
    val historyResult get() = _historyResult

    fun getJobs(
        page: Int
    ) = viewModelScope.launch {
        if (page == 0) onLoading(true)
        repo.jobHistory(
            JOB_HISTORY_QUERY_PER_PAGE,
            page
        ).safeApiCall(page == 0) { data ->
            _historyResult.value = data.data
        }
    }

}