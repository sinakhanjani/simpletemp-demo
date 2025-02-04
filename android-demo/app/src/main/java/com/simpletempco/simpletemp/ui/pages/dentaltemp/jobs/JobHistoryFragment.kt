package com.simpletempco.simpletemp.ui.pages.dentaltemp.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.JobHistory
import com.simpletempco.simpletemp.data.remote.models.response.Offer
import com.simpletempco.simpletemp.databinding.FragmentJobsHistoryBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.AppConfig.JOB_HISTORY_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.DateUtil.convertUtcToLocal
import com.simpletempco.simpletemp.util.DateUtil.currentDate
import com.simpletempco.simpletemp.util.EndlessNestedScrollViewOnScrollListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobHistoryFragment : BaseFragment<JobHistoryViewModel>() {

    private var page = 0
    private var headerData: JobHistory? = null

    private lateinit var jobHistoryAdapter: JobHistoryAdapter

    private lateinit var onScrollListener: EndlessNestedScrollViewOnScrollListener

    override val viewModel: JobHistoryViewModel by viewModels()

    private var _binding: FragmentJobsHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobsHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { loadData(page) }

        if (headerData == null) {
            loadData(page)
        } else {
            hideLoading(true)
            setHeaderInfo(headerData!!)
        }
    }

    private fun loadData(currentPage: Int) {
        viewModel.getJobs(currentPage)
    }

    private fun initViews() {

        setUpRecyclerView()

    }

    private fun initObserve() {
        viewModel.historyResult.observe(viewLifecycleOwner) { data ->
            hideLoading(true)
            data?.let { setJobsInfo(it) }
        }
    }

    private fun setUpRecyclerView() {

        if (!this::jobHistoryAdapter.isInitialized) {
            jobHistoryAdapter = JobHistoryAdapter { onItemClicked(it) }
        }

        // scroll listener for recycler view
        onScrollListener = object : EndlessNestedScrollViewOnScrollListener() {
            override fun onLoadMore() {
                isLoading = true
                jobHistoryAdapter.showLoading()
                loadData(page)
            }
        }

        binding.rvNotification.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jobHistoryAdapter
        }

        binding.nsv.setOnScrollChangeListener(onScrollListener)
    }

    private fun setJobsInfo(data: JobHistory) {
        jobHistoryAdapter.hideLoading()
        onScrollListener.isLoading = false

        if (page == 0) {
            headerData = data
            setHeaderInfo(data)
        }

        if (data.offers.isNullOrEmpty()) {
            onScrollListener.isLastPage = true
        } else {
            if (data.offers!!.size < JOB_HISTORY_QUERY_PER_PAGE)
                onScrollListener.isLastPage = true
            jobHistoryAdapter.setItems(data.offers!!)
        }

        page++
    }

    private fun setHeaderInfo(data: JobHistory) {

        binding.tvThisMonth.text = String.format(
            "%s, %s",
            getString(R.string.this_month),
            currentDate("MMM yyyy")
        )

        data.me?.createdAt?.let {
            binding.tvTotal.text = String.format(
                "%s, %s",
                getString(R.string.total_from),
                convertUtcToLocal(it, "MMM yyyy")
            )
        }

        data.jobActivity?.let { info ->
            binding.tvCountMonth.text = (info.perMonth ?: 0).toString()
            binding.tvCountTotal.text = (info.sum ?: 0).toString()
        }
    }

    private fun onItemClicked(item: Offer) {

        when (item.status) {
            "paid" -> {
                findNavController().navigate(
                    R.id.action_job_history_dest_to_paid_invoice_details,
                    bundleOf("shift_id" to item.shift?.id)
                )
            }
            "rated" -> {
                findNavController().navigate(
                    R.id.action_job_history_dest_to_create_invoice,
                    bundleOf(
                        "shift_id" to item.shift?.id,
                        "root_page" to "jobHistory"
                    )
                )
            }
            "invoiced" -> {
                findNavController().navigate(
                    R.id.action_job_history_dest_to_invoice_details,
                    bundleOf("shift_id" to item.shift?.id)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}