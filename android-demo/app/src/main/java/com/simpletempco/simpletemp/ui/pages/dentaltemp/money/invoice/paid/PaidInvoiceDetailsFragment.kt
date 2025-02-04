package com.simpletempco.simpletemp.ui.pages.dentaltemp.money.invoice.paid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.simpletempco.simpletemp.data.remote.models.response.InvoiceDetail
import com.simpletempco.simpletemp.databinding.FragmentPaidInvoiceDetailsBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_LONG
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_WEEK_DAY_LONG
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import com.simpletempco.simpletemp.util.DateUtil.convertDateToTime
import com.simpletempco.simpletemp.util.DateUtil.currentDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaidInvoiceDetailsFragment : BaseFragment<PaidInvoiceDetailsViewModel>() {

    private var shiftId: String? = null

    private var invoiceId: String? = null

    override val viewModel: PaidInvoiceDetailsViewModel by viewModels()

    private var _binding: FragmentPaidInvoiceDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaidInvoiceDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get date from previous page
        arguments?.let {
            shiftId = it.getString("shift_id")
        }

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { loadData() }
        loadData()
    }

    private fun loadData() {
        shiftId?.let { id -> viewModel.invoiceDetailsByShiftId(id) }
    }

    private fun initViews() {

        binding.btnEmailMeACopy.setOnClickListener {
            invoiceId?.let { id ->
                viewModel.emailMeInvoice(id) {
                    hideLoading()
                    // TODO: back 2 level
                    popBackStack()
                }
            }
        }

    }

    private fun initObserve() {
        viewModel.invoiceResult.observe(viewLifecycleOwner) { data ->
            hideLoading(true)
            data?.let { setInfoInfo(it) }
        }
    }

    private fun setInfoInfo(invoiceDetail: InvoiceDetail) {

        invoiceId = invoiceDetail.invoice?.id

        binding.tvClinicName.text = invoiceDetail.clinic?.fullname

        invoiceDetail.clinic?.profile?.accountInformation?.let { info ->
            binding.tvClinicInfo.text = String.format(
                "%s\n%s\n%s",
                info.addressLine1 ?: "",
                info.postalcode ?: "",
                info.city ?: ""
            )
        }

        binding.tvInvoice.text = String.format("Invoice # %s", invoiceDetail.invoice?.factorID)

        binding.tvDate.text = currentDate(DATE_PATTERN_WEEK_DAY_LONG)

        binding.tvPostedDate.text = String.format(
            "%s\n%s - %s",
            changeStringDateFormat(
                invoiceDetail.invoice?.shiftDate ?: "",
                pattern = DATE_PATTERN_LONG,
                toFormat = DATE_PATTERN_WEEK_DAY_LONG
            ),
            convertDateToTime(invoiceDetail.invoice?.arrivalTime ?: ""),
            convertDateToTime(invoiceDetail.invoice?.endTime ?: "")
        )

        binding.tvHourlyRate.text =
            String.format("£ %d/hr", invoiceDetail.invoice?.preferredPrice ?: 0)

        invoiceDetail.invoice?.let { invoice ->

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}