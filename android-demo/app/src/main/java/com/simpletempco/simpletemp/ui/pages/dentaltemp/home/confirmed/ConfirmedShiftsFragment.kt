package com.simpletempco.simpletemp.ui.pages.dentaltemp.home.confirmed

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.databinding.FragmentConfirmedShiftsBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.AppConfig.CONFIRMED_SHIFTS_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.EndlessNestedScrollViewOnScrollListener
import com.simpletempco.simpletemp.util.ShiftAdapterCallback
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConfirmedShiftsFragment : BaseFragment<ConfirmedShiftsViewModel>(), ShiftAdapterCallback {

    private var page = 0

    private var curLocation: Location? = null
    private var isPermissionRequested = false

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var shiftsAdapter: ConfirmedShiftsAdapter

    private lateinit var onScrollListener: EndlessNestedScrollViewOnScrollListener

    override val viewModel: ConfirmedShiftsViewModel by viewModels()

    private var _binding: FragmentConfirmedShiftsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmedShiftsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { setupData() }

        if (page == 0) {
            setupData()
        } else {
            hideLoading(true)
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            getLocation()
        } else {
            loadData(page)
        }
    }

    private fun setupData() {
        if (checkLocationPermissions()) {
            getLocation()
        } else {
            if (!isPermissionRequested)
                requestLocationPermissions()
            else
                getLocation()
        }
    }

    private fun loadData(currentPage: Int) {
        viewModel.getConfirmedShifts(
            currentPage,
            curLocation?.latitude?.toString(),
            curLocation?.longitude?.toString()
        )
    }

    private fun initViews() {

        if (!this::shiftsAdapter.isInitialized)
            shiftsAdapter = ConfirmedShiftsAdapter()

        // scroll listener for recycler view
        onScrollListener =
            object : EndlessNestedScrollViewOnScrollListener() {
                override fun onLoadMore() {
                    isLoading = true
                    shiftsAdapter.showLoading()
                    loadData(page)
                }
            }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        shiftsAdapter.addCallback(this)

        binding.rvShifts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shiftsAdapter
        }

        binding.nsv.setOnScrollChangeListener(onScrollListener)

    }

    private fun initObserve() {
        viewModel.confirmedShiftsResult.observe(viewLifecycleOwner) { data ->
            if (page == 0) hideLoading(true)
            shiftsAdapter.hideLoading()
            onScrollListener.isLoading = false

            if (data.isNullOrEmpty()) {
                onScrollListener.isLastPage = true
            } else {
                if (data.size < CONFIRMED_SHIFTS_QUERY_PER_PAGE)
                    onScrollListener.isLastPage = true
                shiftsAdapter.setItems(data)
            }
            page++
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                curLocation = location
                loadData(page)
            }
    }

    private fun checkLocationPermissions(): Boolean {
        return arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).all { permission ->
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(shift: Shift) {
        findNavController().navigate(
            R.id.action_confirmed_shifts_to_confirmed_shift_detail,
            bundleOf(
                "shift" to shift,
                "previous_page" to "confirmedShifts"
            )
        )
    }

}