package com.simpletempco.simpletemp.ui.pages.clinic.money.paid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.simpletempco.simpletemp.data.remote.models.response.Invoice
import com.simpletempco.simpletemp.databinding.FragmentClinicPaidInvoiceBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_LONG
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_WEEK_DAY_LONG
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import com.simpletempco.simpletemp.util.DateUtil.convertDateToTime
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaidInvoiceFragment : BaseFragment<PaidInvoiceViewModel>() {

    private var invoiceId: String? = null

    override val viewModel: PaidInvoiceViewModel by viewModels()

    private var _binding: FragmentClinicPaidInvoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClinicPaidInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get date from previous page
        arguments?.let {
            invoiceId = it.getString("invoice_id")
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

    }

    private fun initObserve() {
        viewModel.invoiceResult.observe(viewLifecycleOwner) { data ->
            hideLoading(true)
            data?.let { setInfoInfo(it) }
        }
    }

    private fun setInfoInfo(invoice: Invoice) {

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}