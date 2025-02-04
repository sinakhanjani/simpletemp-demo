package com.simpletempco.simpletemp.ui.pages.dentaltemp.money.invoice.disputed

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.color
import androidx.fragment.app.viewModels
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.InvoiceDetail
import com.simpletempco.simpletemp.databinding.FragmentDisputedInvoiceBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.ContextUtils.getResColor
import com.simpletempco.simpletemp.util.ContextUtils.showMessageDialog
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_LONG
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_WEEK_DAY_LONG
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import com.simpletempco.simpletemp.util.DateUtil.convertDateToTime
import com.simpletempco.simpletemp.util.DateUtil.currentDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisputedInvoiceFragment : BaseFragment<DisputedInvoiceViewModel>() {

    private var disputedInvoiceId: String? = null
    private var invoiceDetail: InvoiceDetail? = null

    override val viewModel: DisputedInvoiceViewModel by viewModels()

    private var _binding: FragmentDisputedInvoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisputedInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get date from previous page
        arguments?.let {
            disputedInvoiceId = it.getString("disputed_invoice_id")
        }

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { loadData() }
        loadData()
    }

    private fun loadData() {
        disputedInvoiceId?.let { id -> viewModel.getDisputedInvoiceDetails(id) }
    }

    private fun initViews() {

        binding.btnAgreeAndSend.setOnClickListener {
            disputedInvoiceId?.let { id ->
                viewModel.agreeDisputeInvoice(id) {
                    hideLoading()
                    // TODO: need to check
                    popBackStack()
                }
            }
        }

        binding.btnDisagree.setOnClickListener {
            showAreYouSureDisputeDialog()
        }

    }

    private fun initObserve() {
        viewModel.invoiceResult.observe(viewLifecycleOwner) { data ->
            hideLoading(true)
            data?.let { setInfoInfo(it) }
        }
    }

    private fun setInfoInfo(invoiceDetail: InvoiceDetail) {

        disputedInvoiceId = invoiceDetail.disputeInvoice?.id
        this.invoiceDetail = invoiceDetail

        binding.tvClinicName.text = invoiceDetail.clinic?.fullname

        invoiceDetail.clinic?.profile?.accountInformation?.let { info ->
            binding.tvClinicInfo.text = String.format(
                "%s\n%s\n%s",
                info.addressLine1 ?: "",
                info.postalcode ?: "",
                info.city ?: ""
            )
        }

        binding.tvInvoice.text =
            String.format("Invoice # %s", invoiceDetail.disputeInvoice?.factorID)

        binding.tvDate.text = currentDate(DATE_PATTERN_WEEK_DAY_LONG)

        binding.tvPostedDate.text = String.format(
            "%s\n%s - %s\n%s",
            changeStringDateFormat(
                invoiceDetail.invoice?.shiftDate ?: "",
                pattern = DATE_PATTERN_LONG,
                toFormat = DATE_PATTERN_WEEK_DAY_LONG
            ),
            convertDateToTime(invoiceDetail.invoice?.arrivalTime ?: ""),
            convertDateToTime(invoiceDetail.invoice?.endTime ?: ""),
            when (invoiceDetail.invoice?.unpaidBreakTime) {
                "unpaid" -> "zero break"
                else -> String.format(
                    "%d min break",
                    invoiceDetail.invoice?.unpaidBreakTime?.toIntOrNull() ?: 0
                )
            }
        )

        binding.tvHourlyRate.text =
            String.format("£ %d/hr", invoiceDetail.disputeInvoice?.preferredPrice ?: 0)

        invoiceDetail.disputeInvoice?.let { invoice ->

            invoice.arrivalTime?.let { time ->
                binding.tvArrivalTime.text = convertDateToTime(time)
            }

            invoice.endTime?.let { time ->
                binding.tvEndTime.text = convertDateToTime(time)
            }

            binding.tvTotalTime.text = invoice.totalTime

            invoice.unpaidBreakTime?.let { unpaid ->
                binding.tvUnpaidBreakTime.text = when (unpaid) {
                    "unpaid" -> "Zero"
                    else -> String.format("%d Min", unpaid.toIntOrNull() ?: 0)
                }
            }

            binding.tvBillableTime.text = invoice.billableTime

            binding.tvTotal.text = invoice.totalPrice

        }

    }

    private fun showAreYouSureDisputeDialog() {
        requireContext().showMessageDialog(
            title = getString(R.string.are_you_sure_disagree_title),
            message = "",
            positiveButtonText = getString(R.string.yes_im_sure),
            negativeButtonText = getString(R.string.go_back),
            onPositiveButtonClick = { disagreeDispute() }
        )
    }

    private fun disagreeDispute() {
        disputedInvoiceId?.let { id ->
            viewModel.disagreeDisputeInvoice(id) {
                hideLoading()
                showDisputeUnResolveDialog()
            }
        }
    }

    private fun showDisputeUnResolveDialog() {
        context?.apply {
            showMessageDialog(
                title = getString(R.string.dispute_un_resolved),
                messageSpannable = SpannableStringBuilder()
                    .color(getResColor(R.color.colorTextSecondary)) {
                        append(getString(R.string.you_have_disagreed_with_msg))
                        append(" ${invoiceDetail?.clinic?.fullname} .").append("\n\n")
                    }
                    .color(getResColor(R.color.colorTextGray)) {
                        append(getString(R.string.we_have_created_a_ticket_to_help_msg))
                    },
                positiveButtonText = getString(R.string.ok),
                cancelable = false,
                onPositiveButtonClick = {
                    // TODO: need to check
                    popBackStack()
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}