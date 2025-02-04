package com.simpletempco.simpletemp.ui.pages.dentaltemp.home.shifts.confirmed

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.text.bold
import androidx.core.text.color
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.databinding.FragmentConfirmedShiftDetailBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.ui.custom.dialogs.RatingDialog
import com.simpletempco.simpletemp.ui.pages.dentaltemp.home.shifts.ShiftsViewModel
import com.simpletempco.simpletemp.util.ContextUtils.checkLocationPermissions
import com.simpletempco.simpletemp.util.ContextUtils.getResColor
import com.simpletempco.simpletemp.util.ContextUtils.isLocationEnable
import com.simpletempco.simpletemp.util.ContextUtils.showMessageDialog
import com.simpletempco.simpletemp.util.DateUtil
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_LONG
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import com.simpletempco.simpletemp.util.DateUtil.convertDateToTime
import com.simpletempco.simpletemp.util.collapse
import com.simpletempco.simpletemp.util.expand
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmedShiftDetailFragment : BaseFragment<ShiftsViewModel>() {

    private var shift: Shift? = null
    private var shiftId: String? = null

    private var curLocation: Location? = null
    private var isPermissionRequested = false

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var previousPage: String? = null

    override val viewModel: ShiftsViewModel by activityViewModels()

    private var _binding: FragmentConfirmedShiftDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmedShiftDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get date from previous page
        arguments?.let {
            shift = it.getParcelable("shift")
            shiftId = it.getString("shift_id")
            previousPage = it.getString("previous_page", null)
        }

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { loadData() }

        if (shift == null) {
            setupData()
        } else {
            hideLoading(true)
            initInfo()
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
        shiftId?.let { id ->
            viewModel.getShiftDetails(
                id,
                curLocation?.latitude?.toString(),
                curLocation?.longitude?.toString()
            )
        }
    }

    private fun initViews() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.containerMoreDetail.setOnClickListener {
            changeMoreDetailState()
        }

        binding.btnMoreDetailExpand.setOnClickListener {
            changeMoreDetailState()
        }

        binding.btnMarkAsComplete.setOnClickListener {
            markAsCompleteShift()
        }

        binding.btnCancel.setOnClickListener {
            showCancelShiftDialog()
        }

    }

    private fun initObserve() {
        viewModel.shiftDetailsResult.observe(viewLifecycleOwner) { data ->
            hideLoading()
            hideLoading(true)
            shift = data
            initInfo()
        }
    }

    private fun initInfo() {
        if (shift == null) return
        binding.apply {

            val arrivalTime = convertDateToTime(shift?.arrivalTime ?: "")
            val endTime = convertDateToTime(shift?.endTime ?: "")
            tvTime.text = String.format("%s - %s", arrivalTime, endTime)

            shift?.clinic?.let { clinic ->
                tvName.text = clinic.fullname
                tvAddress.text = String.format(
                    "%s, %s • %s",
                    clinic.profile?.accountInformation?.addressLine1 ?: "",
                    clinic.profile?.accountInformation?.city ?: "",
                    clinic.distance ?: ""
                )
            }

            shift?.date?.let { date ->
                tvDate.text = changeStringDateFormat(
                    date,
                    DATE_PATTERN_LONG,
                    DateUtil.DATE_PATTERN_WEEK_DAY_LONG
                )
            }

            shift?.unpaidBreakTime?.let { breakTime ->
                tvUnpaidBreakTime.text = if (breakTime.lowercase() == "unpaid") {
                    getString(R.string.zero)
                } else {
                    String.format("%s min", breakTime)
                }
            }

            shift?.clinic?.profile?.detailInformation?.let { details ->
                tvSoftware.text = details.software
                tvCharting.text = details.charting
                tvUltrasonic.text = details.ultrasonic
                tvRadiography.text = details.radiography
                tvParking.text = details.parking
            }

            shift?.offers?.firstOrNull()?.preferredPrice?.let { price ->
                tvHourlyPrice.text = String.format("%s (£/hr)", price)
            }

            shift?.cost?.let { cost ->
                binding.tvPrice.text = cost
            }

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

    private fun changeMoreDetailState() {
        if (binding.tvMoreDetail.isShown) {
            binding.btnMoreDetailExpand.animate().rotation(0F)
            binding.tvMoreDetail.collapse()
        } else {
            binding.btnMoreDetailExpand.animate().rotation(-90F)
            binding.tvMoreDetail.expand()
        }
    }

    private fun markAsCompleteShift() {
        viewModel.markAsCompleteShift(shift!!.id!!) {
            hideLoading()
            viewModel.needUpdateCalendarData = true
            showRatingDialog()
        }
    }

    private fun showRatingDialog() {
        RatingDialog.Builder()
            .shiftData(shift)
            .build(requireContext()) { dialog, rate, description ->
                rateToClinic(dialog, rate, description)
            }.apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                show()
            }
    }

    private fun rateToClinic(
        dialog: RatingDialog,
        rate: Int,
        description: String?
    ) {
        viewModel.clinicRate(shift?.id!!, rate, description) {
            hideLoading()
            dialog.dismiss()
            viewModel.needUpdateCalendarData = true
            showShiftMarkedAsCompleteDialog()
        }
    }

    private fun showShiftMarkedAsCompleteDialog() {
        requireContext().apply {
            showMessageDialog(
                title = getString(R.string.shift_was_marked_as_completed),
                messageSpannable = SpannableStringBuilder()
                    .color(getResColor(R.color.colorTextSecondary)) {
                        append(getString(R.string.thank_you_for_taking_the_time_msg))
                    }
                    .bold {
                        color(getResColor(R.color.colorTextGray)) {
                            append(getString(R.string.you_sure_cancel_this_shift_message))
                        }
                    },
                negativeButtonText = getString(R.string.later),
                positiveButtonText = getString(R.string.create_an_invoice),
                onNegativeButtonClick = { backToPreviousPage() },
                onPositiveButtonClick = { navigateToCreateInvoice() },
                cancelable = false
            )
        }
    }

    private fun navigateToCreateInvoice() {
        findNavController().navigate(
            R.id.action_confirmed_shift_detail_to_create_invoice,
            bundleOf(
                "shift" to shift,
                "root_page" to previousPage
            )
        )
    }

    private fun navigateToConfirmedShifts() {
        findNavController().navigate(R.id.action_confirmed_shift_detail_to_confirmed_shifts)
    }

    private fun backToPreviousPage() {
        when (previousPage) {
            "home" -> {
                navItemSelect(R.id.home_dest)
            }
            "confirmedShifts" -> {
                navigateToConfirmedShifts()
            }
            else -> {
                popBackStack()
            }
        }
    }

    private fun showCancelShiftDialog() {

        requireContext().apply {
            showMessageDialog(
                title = getString(R.string.sure_to_cancel_this_shift_title),
                messageSpannable = SpannableStringBuilder()
                    .color(getResColor(R.color.colorTextSecondary)) {
                        append(
                            changeStringDateFormat(
                                shift?.date ?: "",
                                pattern = DATE_PATTERN_LONG
                            )
                        )
                            .append("\n")
                            .append(binding.tvTime.text)
                    }
                    .color(getResColor(R.color.colorTextGray)) {
                        append(getString(R.string.you_sure_cancel_this_shift_message))
                    }.apply {
                        val index = indexOf("Important")
                        setSpan(
                            ForegroundColorSpan(getResColor(R.color.colorTextTitle)),
                            index,
                            index + 15,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    },
                positiveButtonText = getString(R.string.no),
                negativeButtonText = getString(R.string.yes),
                onNegativeButtonClick = { cancelShift() }
            )
        }

    }

    private fun cancelShift() {
        shift?.offers?.firstOrNull()?.id?.let { id ->
            viewModel.cancelShift(id) { msg ->
                hideLoading()
                viewModel.needUpdateCalendarData = true
                showCancelShiftResultDialog(msg)
            }
        }
    }

    private fun showCancelShiftResultDialog(msg: String?) {
        requireContext().apply {
            showMessageDialog(
                title = getString(R.string.shift_cancelled),
                messageSpannable = SpannableStringBuilder()
                    .color(getResColor(R.color.colorTextSecondary)) {
                        append(getString(R.string.your_shift_for)).append("\n")
                            .append(shift?.clinic?.fullname ?: "").append("\n")
                            .append(
                                changeStringDateFormat(
                                    shift?.date ?: "",
                                    pattern = DATE_PATTERN_LONG
                                )
                            ).append("\n")
                            .append(binding.tvTime.text).append("\n")
                            .append("${shift?.offers?.firstOrNull()?.preferredPrice} £/hr")
                            .append("\n")
                            .append(getString(R.string.was_cancelled)).append("\n\n")
                    }
                    .color(getResColor(R.color.colorTextGray)) {
                        append(msg)
                    },
                positiveButtonText = getString(R.string.ok),
                onPositiveButtonClick = { backToPreviousPage() }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}