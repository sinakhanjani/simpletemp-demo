package com.simpletempco.simpletemp.ui.pages.clinic.money.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Invoice
import com.simpletempco.simpletemp.data.remote.models.response.Invoices
import com.simpletempco.simpletemp.databinding.FragmentAllInvoiceBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.AppConfig.INVOICE_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.EndlessNestedScrollViewOnScrollListener
import com.simpletempco.simpletemp.util.InvoiceAdapterCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllInvoiceFragment : BaseFragment<AllInvoiceViewModel>(), InvoiceAdapterCallback {

    private var page = 0
    private var isStatusPaid = false
    private var userType: String? = null

    private lateinit var invoiceAdapter: InvoiceAdapter

    private lateinit var onScrollListener: EndlessNestedScrollViewOnScrollListener

    override val viewModel: AllInvoiceViewModel by viewModels()

    private var _binding: FragmentAllInvoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { loadData(true) }

        if (page == 0) {
            loadData(true)
        } else {
            hideLoading(true)
        }
    }

    private fun loadData(needLoading: Boolean = false) {
        viewModel.getInvoices(needLoading, page, isStatusPaid, userType)
    }

    private fun initViews() {

        if (!this::invoiceAdapter.isInitialized)
            invoiceAdapter = InvoiceAdapter()


        invoiceAdapter.addCallback(this)

        // scroll listener for recycler view
        onScrollListener =
            object : EndlessNestedScrollViewOnScrollListener() {
                override fun onLoadMore() {
                    isLoading = true
                    invoiceAdapter.showLoading()
                    loadData()
                }
            }

        binding.rvInvoice.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = invoiceAdapter
        }

        binding.tblInvoiceStatus.getTabAt(if (isStatusPaid) 0 else 1)?.select()

        binding.tblInvoiceStatus.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                isStatusPaid = tab?.position == 0
                setupLoadData()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.rgFilter.setOnCheckedChangeListener { _, checkedId ->
            userType = when (checkedId) {
                R.id.rb_hygienist -> "hygienist"
                R.id.rb_nurse -> "nurse"
                else -> null
            }
            setupLoadData()
        }

        binding.nsv.setOnScrollChangeListener(onScrollListener)

    }

    private fun setupLoadData() {
        page = 0
        invoiceAdapter.clearItems()
        onScrollListener.resetOnLoadMore()
        loadData()
    }

    private fun initObserve() {
        viewModel.invoiceResult.observe(viewLifecycleOwner) { data ->
            hideLoading()
            hideLoading(true)
            data?.let { setInfo(it) }
        }
    }

    private fun setInfo(data: Invoices) {
        invoiceAdapter.hideLoading()
        onScrollListener.isLoading = false

        if (data.invoices.isNullOrEmpty()) {
            onScrollListener.isLastPage = true
        } else {
            if (data.invoices!!.size < INVOICE_QUERY_PER_PAGE)
                onScrollListener.isLastPage = true
            invoiceAdapter.setItems(data.invoices!!)
        }

        page++
    }

    override fun onPayNowClicked(invoice: Invoice) {
        if (invoice.shift?.status == "completed") {
            navigateToReview(invoice, true)
        } else {
            findNavController().navigate(
                R.id.action_all_invoice_to_pay_now_invoice,
                bundleOf(
                    "invoice_id" to invoice.id,
                    "root_page" to "allInvoice"
                )
            )
        }
    }

    override fun onPayManuallyClicked(invoice: Invoice) {
        if (invoice.shift?.status == "completed") {
            navigateToReview(invoice, false)
        } else {
            findNavController().navigate(
                R.id.action_all_invoice_to_pay_manually_invoice,
                bundleOf(
                    "invoice_id" to invoice.id,
                    "root_page" to "allInvoice"
                )
            )
        }
    }

    private fun navigateToReview(invoice: Invoice, isPayNow: Boolean) {
        findNavController().navigate(
            R.id.action_all_invoice_to_review,
            bundleOf(
                "invoice" to invoice,
                "pay_now" to isPayNow,
                "root_page" to "allInvoice"
            )
        )
    }

    override fun onPaidItemClicked(invoice: Invoice) {
        findNavController().navigate(
            R.id.action_all_invoice_to_paid_invoice,
            bundleOf("invoice_id" to invoice.id)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}