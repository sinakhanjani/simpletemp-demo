package com.simpletempco.simpletemp.ui.pages.clinic.money.dispute

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.request.DisputeInvoiceReq
import com.simpletempco.simpletemp.data.remote.models.response.Invoice
import com.simpletempco.simpletemp.databinding.FragmentClinicDisputeInvoiceBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.ContextUtils.showMessageDialog
import com.simpletempco.simpletemp.util.ContextUtils.showSingleChoiceItemsDialog
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_LONG
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_WEEK_DAY_LONG
import com.simpletempco.simpletemp.util.DateUtil.TIME_PATTERN_NORMAL
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import com.simpletempco.simpletemp.util.DateUtil.convert24HourTo12
import com.simpletempco.simpletemp.util.DateUtil.convertDateToTime
import com.simpletempco.simpletemp.util.DateUtil.getTwoDateDuration
import com.simpletempco.simpletemp.util.DateUtil.getTwoDateDurationByHour
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max

@AndroidEntryPoint
class DisputeInvoiceFragment : BaseFragment<DisputeInvoiceViewModel>() {

    private var invoice: Invoice? = null

    private var disputeReq: DisputeInvoiceReq? = null

    override val viewModel: DisputeInvoiceViewModel by viewModels()

    private var _binding: FragmentClinicDisputeInvoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClinicDisputeInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get date from previous page
        arguments?.let {
            invoice = it.getParcelable("invoice")
        }

        initViews()
        initInfo()
    }

    private fun initViews() {

        binding.containerBreakTime.setOnClickListener { showBreakTimeDialog() }

        binding.tvArrivalTime.setOnClickListener { showArrivalTimePickerDialog() }

        binding.tvEndTime.setOnClickListener { showEndTimePickerDialog() }

        binding.btnSendInvoice.setOnClickListener {
            sendInvoice()
        }
    }

    private fun initInfo() {

        binding.tvDentalName.text = invoice?.dentalTemp?.fullname

        binding.tvDentalType.text =
            invoice?.dentalTemp?.userType?.replaceFirstChar { it.uppercase() }

        binding.tvInvoiceDetail.text = String.format("Invoice # %s", invoice?.factorID)

        invoice?.shiftDate?.let { date ->
            binding.tvDate.text = changeStringDateFormat(
                date,
                pattern = DATE_PATTERN_LONG,
                toFormat = DATE_PATTERN_WEEK_DAY_LONG
            )
        }

        binding.tvHourlyRate.text = String.format("£ %d/hr", invoice?.preferredPrice ?: 0)

        setupTimes()
        refreshTotals()
    }

    private fun sendInvoice() {
        disputeReq?.let { request ->
            viewModel.disputeInvoice(request) {
                showInvoiceSentDialog()
            }
        }
    }

    private fun showInvoiceSentDialog() {
        context?.apply {
            showMessageDialog(
                title = getString(R.string.invoice_sent_successfully),
                message = getString(R.string.please_wait_for_the_corrected_invoice_msg),
                positiveButtonText = getString(R.string.ok),
                cancelable = false,
                onPositiveButtonClick = {
                    navItemSelect(R.id.money_dest)
                }
            )
        }
    }

    private fun setupTimes() {

        if (disputeReq == null) initDisputeInvoice()

        updateArrivalTime()
        updateEndTime()
        updateBreakTime()


        binding.tblArrivalTime.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                val h = disputeReq?.arrivalTime?.split(":")?.firstOrNull()?.toInt() ?: 0
                val m = disputeReq?.arrivalTime?.split(":")?.last()

                disputeReq?.arrivalTime = if (tab?.position == 0) {
                    String.format("%02d:%s", h % 12, m)
                } else {
                    String.format("%02d:%s", if (h < 12) h + 12 else h, m)
                }
                updateArrivalTime()
                refreshTotals()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.tblEndTime.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                val h = disputeReq?.endTime?.split(":")?.firstOrNull()?.toInt() ?: 0
                val m = disputeReq?.endTime?.split(":")?.last()

                disputeReq?.endTime = if (tab?.position == 0) {
                    String.format("%02d:%s", h % 12, m)
                } else {
                    String.format("%02d:%s", if (h < 12) h + 12 else h, m)
                }
                updateEndTime()
                refreshTotals()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    private fun initDisputeInvoice() {
        disputeReq = DisputeInvoiceReq(
            invoiceId = invoice?.id,
            arrivalTime = convertDateToTime(
                invoice?.arrivalTime ?: "",
                toFormat = TIME_PATTERN_NORMAL
            ),
            endTime = convertDateToTime(
                invoice?.endTime ?: "",
                toFormat = TIME_PATTERN_NORMAL
            ),
            unpaidBreakTime = invoice?.unpaidBreakTime
        )
    }

    private fun showBreakTimeDialog() {
        requireContext().showSingleChoiceItemsDialog(
            title = getString(R.string.unpaid_break),
            items = resources.getStringArray(R.array.unpaid_break),
            currentSelect = disputeReq?.unpaidBreakTime,
            onSelectItemCallback = { selectedItem ->
                disputeReq?.unpaidBreakTime =
                    if (selectedItem == "Zero") "unpaid" else selectedItem
                updateBreakTime()
                refreshTotals()
            }
        )
    }

    private fun showArrivalTimePickerDialog() {
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText(R.string.shift_arrival_time)
            .setHour(disputeReq?.arrivalTime?.split(":")?.firstOrNull()?.toInt() ?: 0)
            .setMinute(disputeReq?.arrivalTime?.split(":")?.last()?.toInt() ?: 0)
            .build()
        timePicker.show(childFragmentManager, "ArrivalTimePicker")

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute

            disputeReq?.arrivalTime = String.format("%02d:%02d", hour, minute)
            updateArrivalTime()
            refreshTotals()
        }

    }

    private fun showEndTimePickerDialog() {
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText(R.string.shift_end_time)
            .setHour(disputeReq?.endTime?.split(":")?.firstOrNull()?.toInt() ?: 0)
            .setMinute(disputeReq?.endTime?.split(":")?.last()?.toInt() ?: 0)
            .build()
        timePicker.show(childFragmentManager, "EndTimePicker")

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            disputeReq?.endTime = String.format("%02d:%02d", hour, minute)
            updateEndTime()
            refreshTotals()
        }

    }

    private fun refreshTotals() {
        binding.tvTotalTime.text = getTwoDateDurationByHour(
            disputeReq?.arrivalTime,
            disputeReq?.endTime
        )

        binding.tvBreakTimeDetail.text = when (disputeReq?.unpaidBreakTime) {
            "unpaid" -> "Zero"
            else -> String.format("%d Min", disputeReq?.unpaidBreakTime?.toInt() ?: 60)
        }

        binding.tvBillableTime.text = getTwoDateDurationByHour(
            disputeReq?.arrivalTime,
            disputeReq?.endTime,
            disputeReq?.unpaidBreakTime?.toIntOrNull() ?: 0
        )

        val totalMinutes = getTwoDateDuration(disputeReq?.arrivalTime, disputeReq?.endTime) ?: 0
        val billableMinutes =
            max(0, totalMinutes - (disputeReq?.unpaidBreakTime?.toIntOrNull() ?: 0))

        binding.tvTotal.text = String.format(
            "£%s.00",
            (billableMinutes * ((invoice?.preferredPrice ?: 0) / 60f)).toInt()
        )

    }

    private fun updateArrivalTime() {
        val hour = disputeReq?.arrivalTime?.split(":")?.firstOrNull()?.toInt() ?: 0
        val minute = disputeReq?.arrivalTime?.split(":")?.last()?.toInt() ?: 0

        binding.tblArrivalTime.getTabAt(if (hour < 12) 0 else 1)?.select()

        binding.tvArrivalTime.text =
            String.format(
                "%02d : %02d",
                convert24HourTo12(hour),
                minute
            )
    }

    private fun updateEndTime() {
        val hour = disputeReq?.endTime?.split(":")?.firstOrNull()?.toInt() ?: 0
        val minute = disputeReq?.endTime?.split(":")?.last()?.toInt() ?: 0

        binding.tblEndTime.getTabAt(if (hour < 12) 0 else 1)?.select()

        binding.tvEndTime.text =
            String.format(
                "%02d : %02d",
                convert24HourTo12(hour),
                minute
            )
    }

    private fun updateBreakTime() {
        binding.tvBreakTime.text = when (disputeReq?.unpaidBreakTime) {
            "unpaid" -> "Zero"
            else -> String.format("%d Min", disputeReq?.unpaidBreakTime?.toIntOrNull() ?: 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}