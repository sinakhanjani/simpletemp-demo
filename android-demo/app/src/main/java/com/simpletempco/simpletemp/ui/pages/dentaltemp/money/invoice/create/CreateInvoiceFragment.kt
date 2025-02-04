package com.simpletempco.simpletemp.ui.pages.dentaltemp.money.invoice.create

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.color
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.request.CreateInvoiceReq
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.databinding.FragmentCreateInvoiceBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.ContextUtils.getResColor
import com.simpletempco.simpletemp.util.ContextUtils.showMessageDialog
import com.simpletempco.simpletemp.util.ContextUtils.showSingleChoiceItemsDialog
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_LONG
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_WEEK_DAY_LONG
import com.simpletempco.simpletemp.util.DateUtil.TIME_PATTERN_NORMAL
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import com.simpletempco.simpletemp.util.DateUtil.convert24HourTo12
import com.simpletempco.simpletemp.util.DateUtil.convertDateToTime
import com.simpletempco.simpletemp.util.DateUtil.currentDate
import com.simpletempco.simpletemp.util.DateUtil.getTwoDateDuration
import com.simpletempco.simpletemp.util.DateUtil.getTwoDateDurationByHour
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max

@AndroidEntryPoint
class CreateInvoiceFragment : BaseFragment<CreateInvoiceViewModel>() {

    private var shift: Shift? = null
    private var shiftId: String? = null
    private var rootPage: String? = null

    private var invoiceReq: CreateInvoiceReq? = null

    override val viewModel: CreateInvoiceViewModel by viewModels()

    private var _binding: FragmentCreateInvoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get date from previous page
        arguments?.let {
            shift = it.getParcelable("shift")
            shiftId = it.getString("shift_id")
            rootPage = it.getString("root_page", null)
        }

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { loadData() }

        if (shift == null) {
            loadData()
        } else {
            hideLoading(true)
            initInfo()
        }
    }

    private fun loadData() {
        shiftId?.let { id -> viewModel.getShiftDetails(id) }
    }

    private fun initViews() {

        binding.containerBreakTime.setOnClickListener { showBreakTimeDialog() }

        binding.tvArrivalTime.setOnClickListener { showArrivalTimePickerDialog() }

        binding.tvEndTime.setOnClickListener { showEndTimePickerDialog() }

        binding.btnSendInvoice.setOnClickListener {
            sendInvoice()
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

        binding.tvClinicName.text = shift?.clinic?.fullname

        shift?.clinic?.profile?.accountInformation?.let { info ->
            binding.tvClinicInfo.text = String.format(
                "%s\n%s\n%s",
                info.addressLine1 ?: "",
                info.postalcode ?: "",
                info.city ?: ""
            )
        }

        binding.tvDate.text = currentDate(DATE_PATTERN_WEEK_DAY_LONG)

        binding.tvPostedDate.text = String.format(
            "%s\n%s - %s",
            changeStringDateFormat(
                shift?.date ?: "",
                pattern = DATE_PATTERN_LONG,
                toFormat = DATE_PATTERN_WEEK_DAY_LONG
            ),
            convertDateToTime(shift?.arrivalTime ?: ""),
            convertDateToTime(shift?.endTime ?: "")
        )

        binding.tvHourlyRate.text = String.format("%d£/hr", shift?.preferredPrice ?: 0)

        setupTimes()
        refreshTotals()
    }

    private fun sendInvoice() {
        invoiceReq?.let { date ->
            viewModel.createInvoice(date) {
                hideLoading()
                showInvoiceSentDialog()
            }
        }
    }

    private fun showInvoiceSentDialog() {
        context?.apply {
            showMessageDialog(
                title = getString(R.string.invoice_sent_successfully),
                messageSpannable = SpannableStringBuilder()
                    .color(getResColor(R.color.colorTextSecondary)) {
                        append("To: ${shift?.clinic?.fullname}").append("\n")
                        append(
                            "${convertDateToTime(shift?.arrivalTime!!)} - " +
                                    "${convertDateToTime(shift?.endTime!!)}"
                        ).append("\n")
                        append("Unpaid time: ${binding.tvBreakTime.text}").append("\n")
                        append("Total Payable: ${binding.tvTotal.text}").append("\n\n")
                    }
                    .color(getResColor(R.color.colorTextGray)) {
                        append(getString(R.string.a_copy_of_invoice_emailed_msg))
                    },
                cancelable = false,
                positiveButtonText = getString(R.string.ok),
                onPositiveButtonClick = { sendInvoiceSuccess() }
            )
        }
    }

    private fun sendInvoiceSuccess() {
        when (rootPage) {
            "home" -> {
                navItemSelect(R.id.home_dest)
            }
            "confirmedShifts" -> {
                findNavController().navigate(R.id.action_create_invoice_to_confirmed_shifts)
            }
            "jobHistory" -> {
                navItemSelect(R.id.job_history_dest)
            }
            else -> {
                popBackStack()
            }
        }
    }

    private fun setupTimes() {

        if (invoiceReq == null) initInvoice()

        updateArrivalTime()
        updateEndTime()
        updateBreakTime()


        binding.tblArrivalTime.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                val h = invoiceReq?.arrivalTime?.split(":")?.firstOrNull()?.toInt() ?: 0
                val m = invoiceReq?.arrivalTime?.split(":")?.last()

                invoiceReq?.arrivalTime = if (tab?.position == 0) {
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

                val h = invoiceReq?.endTime?.split(":")?.firstOrNull()?.toInt() ?: 0
                val m = invoiceReq?.endTime?.split(":")?.last()

                invoiceReq?.endTime = if (tab?.position == 0) {
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

    private fun initInvoice() {
        invoiceReq = CreateInvoiceReq(
            shiftId = shift?.id,
            arrivalTime = convertDateToTime(
                shift?.arrivalTime ?: "",
                toFormat = TIME_PATTERN_NORMAL
            ),
            endTime = convertDateToTime(
                shift?.endTime ?: "",
                toFormat = TIME_PATTERN_NORMAL
            ),
            unpaidBreakTime = shift?.unpaidBreakTime
        )
    }

    private fun showBreakTimeDialog() {
        requireContext().showSingleChoiceItemsDialog(
            title = getString(R.string.unpaid_break),
            items = resources.getStringArray(R.array.unpaid_break),
            currentSelect = invoiceReq?.unpaidBreakTime,
            onSelectItemCallback = { selectedItem ->
                invoiceReq?.unpaidBreakTime =
                    if (selectedItem == "Zero") "unpaid" else selectedItem
                updateBreakTime()
                refreshTotals()
            }
        )
    }

    private fun showArrivalTimePickerDialog() {
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText(R.string.shift_arrival_time)
            .setHour(invoiceReq?.arrivalTime?.split(":")?.firstOrNull()?.toInt() ?: 0)
            .setMinute(invoiceReq?.arrivalTime?.split(":")?.last()?.toInt() ?: 0)
            .build()
        timePicker.show(childFragmentManager, "ArrivalTimePicker")

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute

            invoiceReq?.arrivalTime = String.format("%02d:%02d", hour, minute)
            updateArrivalTime()
            refreshTotals()
        }

    }

    private fun showEndTimePickerDialog() {
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText(R.string.shift_end_time)
            .setHour(invoiceReq?.endTime?.split(":")?.firstOrNull()?.toInt() ?: 0)
            .setMinute(invoiceReq?.endTime?.split(":")?.last()?.toInt() ?: 0)
            .build()
        timePicker.show(childFragmentManager, "EndTimePicker")

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            invoiceReq?.endTime = String.format("%02d:%02d", hour, minute)
            updateEndTime()
            refreshTotals()
        }

    }

    private fun refreshTotals() {
        binding.tvTotalTime.text = getTwoDateDurationByHour(
            invoiceReq?.arrivalTime,
            invoiceReq?.endTime
        )

        binding.tvBreakTimeDetail.text = when (invoiceReq?.unpaidBreakTime) {
            "unpaid" -> "Zero"
            else -> String.format("%d Min", invoiceReq?.unpaidBreakTime?.toInt() ?: 60)
        }

        binding.tvBillableTime.text = getTwoDateDurationByHour(
            invoiceReq?.arrivalTime,
            invoiceReq?.endTime,
            invoiceReq?.unpaidBreakTime?.toIntOrNull() ?: 0
        )

        val totalMinutes = getTwoDateDuration(invoiceReq?.arrivalTime, invoiceReq?.endTime) ?: 0
        val billableMinutes =
            max(0, totalMinutes - (invoiceReq?.unpaidBreakTime?.toIntOrNull() ?: 0))

        binding.tvTotal.text = String.format(
            "£%s.00",
            (billableMinutes * ((shift?.preferredPrice ?: 0) / 60f)).toInt()
        )

    }

    private fun updateArrivalTime() {
        val hour = invoiceReq?.arrivalTime?.split(":")?.firstOrNull()?.toInt() ?: 0
        val minute = invoiceReq?.arrivalTime?.split(":")?.last()?.toInt() ?: 0

        binding.tblArrivalTime.getTabAt(if (hour < 12) 0 else 1)?.select()

        binding.tvArrivalTime.text =
            String.format(
                "%02d : %02d",
                convert24HourTo12(hour),
                minute
            )
    }

    private fun updateEndTime() {
        val hour = invoiceReq?.endTime?.split(":")?.firstOrNull()?.toInt() ?: 0
        val minute = invoiceReq?.endTime?.split(":")?.last()?.toInt() ?: 0

        binding.tblEndTime.getTabAt(if (hour < 12) 0 else 1)?.select()

        binding.tvEndTime.text =
            String.format(
                "%02d : %02d",
                convert24HourTo12(hour),
                minute
            )
    }

    private fun updateBreakTime() {
        binding.tvBreakTime.text = when (invoiceReq?.unpaidBreakTime) {
            "unpaid" -> "Zero"
            else -> String.format("%d Min", invoiceReq?.unpaidBreakTime?.toIntOrNull() ?: 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}