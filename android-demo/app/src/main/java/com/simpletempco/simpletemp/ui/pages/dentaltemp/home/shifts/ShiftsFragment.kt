package com.simpletempco.simpletemp.ui.pages.dentaltemp.home.shifts

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Calendar
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.databinding.FragmentShiftsBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.ui.custom.views.calendar.DateTag
import com.simpletempco.simpletemp.util.ContextUtils.checkLocationPermissions
import com.simpletempco.simpletemp.util.ContextUtils.isLocationEnable
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_DISPLAY_LONG
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import com.simpletempco.simpletemp.util.DateUtil.convertDateToRangeDate
import com.simpletempco.simpletemp.util.ShiftAdapterCallback
import com.simpletempco.simpletemp.util.SimpleCalendarCallback
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShiftsFragment : BaseFragment<ShiftsViewModel>(), SimpleCalendarCallback,
    ShiftAdapterCallback {

    private var currentDate: String? = null
    private var curLocation: Location? = null
    private var isPermissionRequested = false

    private var calendarList: List<Calendar>? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var shiftAdapter: AvailableShiftDetailAdapter

    override val viewModel: ShiftsViewModel by activityViewModels()

    private var _binding: FragmentShiftsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShiftsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get date from previous page
        arguments?.let {
            if (currentDate == null) currentDate = it.getString("date")
        }

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { setupData() }

        if (calendarList == null) {
            setupData()
        } else {
            hideLoading(true)
            if (viewModel.needUpdateCalendarData) loadData()
        }
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

    private fun setupData() {
        if (checkLocationPermissions() && isLocationEnable()) {
            getLocation()
        } else {
            if (!isPermissionRequested) {
                requestLocationPermissions()
            } else {
                loadData()
            }
        }
    }

    private fun loadData() {
        currentDate?.let { date ->
            val dates = convertDateToRangeDate(date)
            viewModel.getCalendarInfo(
                dates.first!!,
                dates.second!!,
                curLocation?.latitude?.toString(),
                curLocation?.longitude?.toString()
            )
        }
    }

    private fun initViews() {

        if (!this::shiftAdapter.isInitialized)
            shiftAdapter = AvailableShiftDetailAdapter()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        shiftAdapter.addCallback(this)

        binding.rvOffers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shiftAdapter
        }

        updateDateTitle()

        currentDate?.let { date -> binding.simpleCalendar.setDate(date) }
        binding.simpleCalendar.addSelectDateCallBack(this)

    }

    private fun initObserve() {

        viewModel.calendarResult.observe(viewLifecycleOwner) { items ->
            if (items != null) {
                hideLoading(true)
                calendarList = items
                updateInfo()
            }
            binding.simpleCalendar.setTags(items?.map { DateTag(it.date, it.tag) })
        }

    }

    private fun updateInfo() {
        currentDate?.let { curDate ->
            shiftAdapter.setItems(
                calendarList?.find { it.date?.contains(curDate) == true }?.shifts ?: emptyList()
            )
        }
    }

    private fun updateDateTitle() {
        currentDate?.let { date ->
            binding.tvSelectedDate.text =
                changeStringDateFormat(date, toFormat = DATE_PATTERN_DISPLAY_LONG)
        }
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

    override fun onDateSelect(date: String) {
        currentDate = date
        updateInfo()
        updateDateTitle()
    }

    override fun onChangeMonth(date: String?) {
        date?.let { curDate ->
            convertDateToRangeDate(curDate).let { range ->
                viewModel.getCalendarInfo(
                    range.first!!,
                    range.second!!,
                    curLocation?.latitude?.toString(),
                    curLocation?.longitude?.toString()
                )
            }
        }
    }

    override fun onItemClick(shift: Shift) {

        when (shift.tag) {
            "blue" -> {
                findNavController().navigate(
                    R.id.action_shifts_to_available_shift,
                    bundleOf("shift" to shift)
                )
            }

            "orange" -> {
                findNavController().navigate(
                    R.id.action_shifts_to_offer_placed_shift_detail,
                    bundleOf("shift" to shift)
                )
            }

            "green" -> {
                findNavController().navigate(
                    R.id.action_shifts_to_confirmed_shift_detail,
                    bundleOf("shift" to shift)
                )
            }

            "gray" -> {
                findNavController().navigate(
                    R.id.action_shifts_to_completed_shift_detail,
                    bundleOf("shift" to shift)
                )
            }

        }
    }

}