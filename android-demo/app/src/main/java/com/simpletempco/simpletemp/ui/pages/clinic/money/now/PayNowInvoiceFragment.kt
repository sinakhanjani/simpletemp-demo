package com.simpletempco.simpletemp.ui.pages.clinic.money.now

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Invoice
import com.simpletempco.simpletemp.databinding.FragmentClinicPayInvoiceBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.ContextUtils.showMessageDialog
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_LONG
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_WEEK_DAY_LONG
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import com.simpletempco.simpletemp.util.DateUtil.convertDateToTime
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PayNowInvoiceFragment : BaseFragment<PayNowInvoiceViewModel>() {

    private var invoiceId: String? = null
    private var invoice: Invoice? = null
    private var rootPage: String? = null

    override val viewModel: PayNowInvoiceViewModel by viewModels()

    private var _binding: FragmentClinicPayInvoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClinicPayInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get date from previous page
        arguments?.let {
            invoiceId = it.getString("invoice_id")
            rootPage = it.getString("root_page", null)
        }

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { loadData() }
        loadData()
    }

    private fun loadData() {
        invoiceId?.let { id -> viewModel.getInvoice(id) }
    }

    private fun initViews() {

        binding.btnPayNow.setOnClickListener {
            if (invoice?.shift?.status == "completed") {
                findNavController().navigate(
                    R.id.action_pay_now_invoice_to_review,
                    bundleOf(
                        "invoice" to invoice,
                        "pay_now" to true,
                        "root_page" to "notifications"
                    )
                )
            } else {
                showConfirmPayDialog()
            }
        }

        binding.btnDisputeInvoice.setOnClickListener {
            navigateToDisputeInvoice()
        }

    }

    private fun initObserve() {
        viewModel.invoiceResult.observe(viewLifecycleOwner) { data ->
            hideLoading(true)
            data?.let { setInfoInfo(it) }
        }
    }

    private fun setInfoInfo(invoice: Invoice) {

        this.invoice = invoice

        binding.tvDentalName.text = invoice.dentalTemp?.fullname

        binding.tvDentalType.text =
            invoice.dentalTemp?.userType?.replaceFirstChar { it.uppercase() }

        binding.tvInvoiceDetail.text = String.format("Invoice # %s", invoice.factorID)

        invoice.shiftDate?.let { date ->
            binding.tvDate.text = changeStringDateFormat(
                date,
                pattern = DATE_PATTERN_LONG,
                toFormat = DATE_PATTERN_WEEK_DAY_LONG
            )
        }

        binding.tvHourlyRate.text = String.format("£ %d/hr", invoice.preferredPrice ?: 0)

        invoice.arrivalTime?.let { time ->
            binding.tvArrivalTime.text = convertDateToTime(time)
        }

        invoice.endTime?.let { time ->
            binding.tvEndTime.text = convertDateToTime(time)
        }

        binding.tvTotalTime.text = invoice.totalTime

        invoice.unpaidBreakTime?.let { unpaid ->
            binding.tvUnPaidBreakTime.text = when (unpaid) {
                "unpaid" -> "Zero"
                else -> String.format("%d Min", unpaid.toIntOrNull() ?: 0)
            }
        }

        binding.tvBillableTime.text = invoice.billableTime

        binding.tvTotal.text = invoice.totalPrice

    }

    private fun payNow() {
        invoiceId?.let { id ->
            viewModel.payNowInvoice(id) {
                hideLoading()
                showPayResultDialog()
            }
        }
    }

    private fun navigateToDisputeInvoice() {
        findNavController().navigate(
            R.id.action_pay_now_invoice_to_dispute_invoice,
            bundleOf("invoice" to invoice)
        )
    }

    private fun showConfirmPayDialog() {
        context?.showMessageDialog(
            title = getString(R.string.confirmed_payment),
            message = getString(R.string.payment_terms_are_detailed_msg),
            positiveButtonText = getString(R.string.confirm),
            negativeButtonText = getString(R.string.cancel),
            onPositiveButtonClick = { payNow() }
        )
    }

    private fun showPayResultDialog() {
        context?.showMessageDialog(
            title = getString(R.string.you_are_all_set),
            message = getString(R.string.the_payment_has_been_made),
            positiveButtonText = getString(R.string.ok),
            cancelable = false,
            onPositiveButtonClick = {
                paySuccess()
            }
        )
    }

    private fun paySuccess() {
        when (rootPage) {
            "money" -> {
                navItemSelect(R.id.money_dest)
            }
            "allInvoice" -> {
                findNavController().navigate(R.id.action_pay_now_invoice_to_all_invoice)
            }
            else -> {
                popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}