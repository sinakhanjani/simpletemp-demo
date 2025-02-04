package com.simpletempco.simpletemp.ui.pages.clinic.money

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Invoice
import com.simpletempco.simpletemp.data.remote.models.response.Invoices
import com.simpletempco.simpletemp.databinding.FragmentClinicMoneyBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.InvoiceAdapterCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class MoneyFragment : BaseFragment<MoneyViewModel>(), InvoiceAdapterCallback {

    private lateinit var invoicePagerAdapter: InvoicePagerAdapter

    override val viewModel: MoneyViewModel by viewModels()

    private var _binding: FragmentClinicMoneyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClinicMoneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { loadData() }
        loadData()
    }

    private fun loadData() {
        viewModel.getInvoices()
    }

    private fun initViews() {

        invoicePagerAdapter = InvoicePagerAdapter()
        invoicePagerAdapter.addCallback(this)

        setupViewPager()

        binding.tvAllInvoices.setOnClickListener {
            findNavController().navigate(R.id.action_money_dest_to_all_invoice)
        }

    }

    private fun initObserve() {
        viewModel.invoiceResult.observe(viewLifecycleOwner) { data ->
            hideLoading()
            hideLoading(true)
            data?.let { setInfo(it) }
        }
    }

    private fun setInfo(data: Invoices) {

        invoicePagerAdapter.setItems(data.invoices ?: emptyList())

        binding.tvNursesAmount.text = data.expenses?.totalAmountNurses
        binding.tvHygienistsAmount.text = data.expenses?.totalAmountHygienists
    }

    private fun setupViewPager() {
        binding.viewPager.apply {
            adapter = invoicePagerAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 2
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(20))
                addTransformer { page, position ->
                    val r = 1 - abs(position)
                    page.scaleY = 0.85f + (r * 0.15f)
                }
            })
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    invoicePagerAdapter.setCurrentItem(position)
                }
            })
        }
    }

    override fun onPayNowClicked(invoice: Invoice) {
        if (invoice.shift?.status == "completed") {
            navigateToReview(invoice, true)
        } else {
            findNavController().navigate(
                R.id.action_money_dest_to_pay_now_invoice,
                bundleOf(
                    "invoice_id" to invoice.id,
                    "root_page" to "money"
                )
            )
        }
    }

    override fun onPayManuallyClicked(invoice: Invoice) {
        if (invoice.shift?.status == "completed") {
            navigateToReview(invoice, false)
        } else {
            findNavController().navigate(
                R.id.action_money_dest_to_pay_manually_invoice,
                bundleOf(
                    "invoice_id" to invoice.id,
                    "root_page" to "money"
                )
            )
        }
    }

    private fun navigateToReview(invoice: Invoice, isPayNow: Boolean) {
        findNavController().navigate(
            R.id.action_money_dest_to_review,
            bundleOf(
                "invoice" to invoice,
                "pay_now" to isPayNow,
                "root_page" to "money"
            )
        )
    }

    override fun onPaidItemClicked(invoice: Invoice) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}