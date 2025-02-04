package com.simpletempco.simpletemp.ui.pages.dentaltemp.money

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.text.color
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Money
import com.simpletempco.simpletemp.data.remote.models.response.Offer
import com.simpletempco.simpletemp.databinding.FragmentMoneyBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.AppConfig.MONEY_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.ContextUtils.checkLocationPermissions
import com.simpletempco.simpletemp.util.ContextUtils.getResColor
import com.simpletempco.simpletemp.util.ContextUtils.isLocationEnable
import com.simpletempco.simpletemp.util.DateUtil.convertUtcToLocal
import com.simpletempco.simpletemp.util.DateUtil.currentDate
import com.simpletempco.simpletemp.util.EndlessNestedScrollViewOnScrollListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoneyFragment : BaseFragment<MoneyViewModel>() {

    private var page = 0
    private var isPaid = false
    private var curLocation: Location? = null
    private var isPermissionRequested = false

    private var headerData: Money? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var jobsAdapter: MoneyAdapter

    private lateinit var onScrollListener: EndlessNestedScrollViewOnScrollListener

    override val viewModel: MoneyViewModel by viewModels()

    private var _binding: FragmentMoneyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { setupData() }

        if (headerData == null) {
            setupData()
        } else {
            hideLoading(true)
        }
    }

    private fun setupData() {
        if (checkLocationPermissions() && isLocationEnable()) {
            getLocation()
        } else if (!isPermissionRequested) {
            requestLocationPermissions()
        } else {
            loadData()
        }
    }

    private fun loadData(isLoading: Boolean = true) {
        viewModel.getJobs(
            page,
            isPaid,
            isLoading,
            curLocation?.latitude.toString(),
            curLocation?.longitude.toString()
        )
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            getLocation()
        } else {
            loadData()
        }
    }

    private fun initViews() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.tblShiftStatus.getTabAt(if (isPaid) 0 else 1)?.select()

        binding.tblShiftStatus.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                isPaid = tab?.position == 0
                page = 0
                jobsAdapter.clearAllItems()
                onScrollListener.resetOnLoadMore()
                loadData(false)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        setUpRecyclerView()

        binding.tvEditBank.setOnClickListener {
            findNavController().navigate(
                R.id.action_money_dest_to_bank_info,
                bundleOf("is_edit_bank" to true)
            )
        }

    }

    private fun initObserve() {
        viewModel.finishedJobsResult.observe(viewLifecycleOwner) { data ->
            hideLoading(true)
            hideLoading()
            data?.let { setJobsInfo(it) }
            page++
        }
    }

    private fun setUpRecyclerView() {

        if (!this::jobsAdapter.isInitialized) {
            jobsAdapter = MoneyAdapter { onItemClicked(it) }
        } else {
            headerData?.let { setHeaderInfo(it) }
            hideLoading(true)
        }

        // scroll listener for recycler view
        onScrollListener =
            object : EndlessNestedScrollViewOnScrollListener() {
                override fun onLoadMore() {
                    isLoading = true
                    jobsAdapter.showLoading()
                    loadData()
                }
            }

        binding.rvJobs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jobsAdapter
        }

        binding.nsv.setOnScrollChangeListener(onScrollListener)
    }

    private fun setJobsInfo(data: Money) {
        jobsAdapter.hideLoading()
        onScrollListener.isLoading = false

        if (page == 0) {
            setHeaderInfo(data)
        }

        if (data.offers.isNullOrEmpty()) {
            onScrollListener.isLastPage = true
        } else {
            if (data.offers!!.size < MONEY_QUERY_PER_PAGE)
                onScrollListener.isLastPage = true
            jobsAdapter.setItems(data.offers!!)
        }

        page++
    }

    private fun setHeaderInfo(data: Money) {

        headerData = data

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

        data.income?.let { info ->
            binding.tvCostMonth.text = (info.monthlyIncome ?: 0).toString()
            binding.tvCostTotal.text = (info.totalIncome ?: 0).toString()
        }

        data.me?.profile?.bankInformation?.let { bankInfo ->
            binding.tvBankInfo.text = SpannableStringBuilder()
                .color(requireContext().getResColor(R.color.colorTextSecondary)) {
                    append(getString(R.string.account_number)).append(": ")
                }
                .color(requireContext().getResColor(R.color.colorTextGray)) {
                    append(bankInfo.accountNumber).append("\n")
                }
                .color(requireContext().getResColor(R.color.colorTextSecondary)) {
                    append(getString(R.string.sort_code)).append(": ")
                }
                .color(requireContext().getResColor(R.color.colorTextGray)) {
                    append(bankInfo.sortCode)
                }
        }

    }

    // Request permissions if not granted before
    private fun requestLocationPermissions() {
        isPermissionRequested = true
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                curLocation = location
                loadData()
            }
    }


    private fun onItemClicked(item: Offer) {
        if (isPaid) {
            findNavController().navigate(
                R.id.action_money_dest_to_paid_invoice_details,
                bundleOf("shift_id" to item.shift?.id)
            )
        } else {
            findNavController().navigate(
                R.id.action_money_dest_to_completed_shift_detail,
                bundleOf("shift_id" to item.shift?.id)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}