package com.simpletempco.simpletemp.ui.pages.dentaltemp.home.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.request.RequestShiftReq
import com.simpletempco.simpletemp.data.remote.models.response.Clinic
import com.simpletempco.simpletemp.data.remote.models.response.HourlyPrices
import com.simpletempco.simpletemp.databinding.FragmentRequestShiftBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.ContextUtils.showSingleChoiceItemsDialog
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import com.simpletempco.simpletemp.util.DateUtil.convertDateToStrDate
import com.simpletempco.simpletemp.util.DateUtil.convertStrDateToDate
import com.simpletempco.simpletemp.util.DateUtil.currentDate
import com.simpletempco.simpletemp.util.DateUtil.currentTime24Hour
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class RequestShiftFragment : BaseFragment<RequestShiftViewModel>() {

    private var reqShift: RequestShiftReq? = null
    private var hourlyPrices: HourlyPrices? = null
    private var clinicList: List<Clinic>? = null
    private var clinic: Clinic? = null

    override val viewModel: RequestShiftViewModel by activityViewModels()

    private var _binding: FragmentRequestShiftBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestShiftBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserve()
    }

    private fun initViews() {

        setupTimes()

        binding.tvClinicName.setOnClickListener {
            if (clinicList.isNullOrEmpty()) {
                viewModel.getClinicList()
            } else {
                navigateToClinicList()
            }
        }

        binding.tvDateOfShift.setOnClickListener { showDatePicker() }

        binding.tvHourlyRate.setOnClickListener {
            if (hourlyPrices == null) {
                viewModel.getHourlyPrices()
            } else {
                showHourlyRateDialog()
            }
        }

        binding.tvArrivalTime.setOnClickListener { showArrivalTimePickerDialog() }

        binding.tvEndTime.setOnClickListener { showEndTimePickerDialog() }

        binding.btnSendOffer.setOnClickListener {
            viewModel.requestShift(reqShift) { requestedSuccess() }
        }
    }

    private fun initObserve() {
        viewModel.hourlyPrices.observe(viewLifecycleOwner) { data ->
            hideLoading()
            hourlyPrices = data
            showHourlyRateDialog()
        }

        viewModel.clinicList.observe(viewLifecycleOwner) { data ->
            hideLoading()
            clinicList = data
            navigateToClinicList()
        }

        viewModel.clinicSelect.observe(viewLifecycleOwner) { data ->
            reqShift?.clinicId = data.id
            clinic = data
            updateClinicName()
        }

    }

    private fun setupTimes() {
        if (reqShift == null) initShift()
        updateClinicName()
        updateArrivalTime()
        updateEndTime()
        updateShiftDate()
        updatePreferredPrice()
    }

    private fun initShift() {
        reqShift = RequestShiftReq(
            date = currentDate(DATE_PATTERN),
            arrivalTime = currentTime24Hour(),
            endTime = currentTime24Hour(),
            clinicId = null,
            preferredPrice = null
        )
    }

    private fun requestedSuccess() {
        hideLoading()
        popBackStack()
    }

    private fun navigateToClinicList() {
        findNavController().navigate(
            R.id.action_request_shift_to_clinic_list,
            bundleOf(
                "current_item_checked" to binding.tvClinicName.text?.toString(),
                "clinic_list" to clinicList
            )
        )
    }

    private fun showHourlyRateDialog() {

        val list = if (viewModel.isNurseUser())
            hourlyPrices?.nurseHourlyPrices?.map { it.toString() }
        else
            hourlyPrices?.hygienistHourlyPrices?.map { it.toString() }

        requireContext().showSingleChoiceItemsDialog(
            title = getString(R.string.preferred_hourly_rate),
            items = list?.toTypedArray() ?: emptyArray(),
            currentSelect = reqShift?.preferredPrice?.toString(),
            onSelectItemCallback = { selectedItem ->
                reqShift?.preferredPrice = selectedItem.toInt()
                updatePreferredPrice()
            }
        )
    }

    private fun showDatePicker() {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.date_of_shift).apply {
                reqShift?.date?.let { convertStrDateToDate(it) }?.let {
                    setSelection(it.time + TimeUnit.DAYS.toMillis(1))
                }
            }.build().apply {
                addOnPositiveButtonClickListener { time ->
                    reqShift?.date = convertDateToStrDate(Date(time))
                    updateShiftDate()
                }
                show(this@RequestShiftFragment.childFragmentManager, "DatePicker")
            }
    }

    private fun showArrivalTimePickerDialog() {
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText(R.string.shift_arrival_time)
            .setHour(reqShift?.arrivalTime?.split(":")?.firstOrNull()?.toInt() ?: 0)
            .setMinute(reqShift?.arrivalTime?.split(":")?.last()?.toInt() ?: 0)
            .build()
        timePicker.show(childFragmentManager, "ArrivalTimePicker")

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute

            reqShift?.arrivalTime = String.format("%02d:%02d", hour, minute)
            updateArrivalTime()
        }

    }

    private fun showEndTimePickerDialog() {
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText(R.string.shift_end_time)
            .setHour(reqShift?.endTime?.split(":")?.firstOrNull()?.toInt() ?: 0)
            .setMinute(reqShift?.endTime?.split(":")?.last()?.toInt() ?: 0)
            .build()
        timePicker.show(childFragmentManager, "EndTimePicker")

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            reqShift?.endTime = String.format("%02d:%02d", hour, minute)
            updateEndTime()
        }

    }

    private fun updateArrivalTime() {
        val hour = reqShift?.arrivalTime?.split(":")?.firstOrNull()?.toInt() ?: 0
        val minute = reqShift?.arrivalTime?.split(":")?.last()?.toInt() ?: 0

        binding.tvArrivalTime.text =
            String.format(
                "%02d : %02d",
                hour,
                minute
            )
    }

    private fun updateEndTime() {
        val hour = reqShift?.endTime?.split(":")?.firstOrNull()?.toInt() ?: 0
        val minute = reqShift?.endTime?.split(":")?.last()?.toInt() ?: 0

        binding.tvEndTime.text =
            String.format(
                "%02d : %02d",
                hour,
                minute
            )
    }

    private fun updateShiftDate() {
        reqShift?.date?.let { date ->
            binding.tvDateOfShift.text = changeStringDateFormat(
                date = date,
                toFormat = "yyyy MM dd"
            )
        }
    }

    private fun updatePreferredPrice() {
        reqShift?.preferredPrice?.let { price ->
            binding.tvHourlyRate.text = String.format(
                "%s %s",
                price,
                getString(R.string.e_per_hr)
            )
        }
    }

    private fun updateClinicName() {
        clinic?.fullname?.let { name -> binding.tvClinicName.text = name }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}