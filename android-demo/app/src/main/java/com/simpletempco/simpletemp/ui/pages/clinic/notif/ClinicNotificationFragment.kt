package com.simpletempco.simpletemp.ui.pages.clinic.notif

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Meta
import com.simpletempco.simpletemp.data.remote.models.response.Notif
import com.simpletempco.simpletemp.databinding.FragmentClinicNotificationBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.ui.pages.clinic.ClinicActivity
import com.simpletempco.simpletemp.util.AppConfig.NOTIFICATION_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.ContextUtils.showMessageDialog
import com.simpletempco.simpletemp.util.EndlessNestedScrollViewOnScrollListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClinicNotificationFragment : BaseFragment<ClinicNotificationViewModel>() {

    private var page = 0

    private lateinit var notificationAdapter: ClinicNotificationAdapter

    private lateinit var onScrollListener: EndlessNestedScrollViewOnScrollListener

    override val viewModel: ClinicNotificationViewModel by viewModels()

    private var _binding: FragmentClinicNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClinicNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { loadData(page) }

        if (page == 0) {
            loadData(page)
        } else {
            hideLoading(true)
        }
    }

    private fun loadData(currentPage: Int) {
        viewModel.getNotifications(currentPage)
    }

    private fun initViews() {

        setUpRecyclerView()

    }

    private fun initObserve() {
        viewModel.notificationsResult.observe(viewLifecycleOwner) { data ->
            hideLoading(true)
            data?.let { setNotificationInfo(it) }
            setNotificationBadge(0)
        }
    }

    private fun setUpRecyclerView() {

        if (!this::notificationAdapter.isInitialized)
            notificationAdapter = ClinicNotificationAdapter { onItemClicked(it) }
        else
            hideLoading(true)

        // scroll listener for recycler view
        onScrollListener =
            object : EndlessNestedScrollViewOnScrollListener() {
                override fun onLoadMore() {
                    isLoading = true
                    notificationAdapter.showLoading()
                    loadData(page)
                }
            }

        binding.rvNotification.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationAdapter
        }

        binding.nsv.setOnScrollChangeListener(onScrollListener)
    }

    private fun setNotificationInfo(data: List<Notif>) {
        notificationAdapter.hideLoading()
        onScrollListener.isLoading = false

        if (data.isEmpty()) {
            onScrollListener.isLastPage = true
        } else {
            if (data.size < NOTIFICATION_QUERY_PER_PAGE)
                onScrollListener.isLastPage = true
            notificationAdapter.setItems(data)
        }

        page++
    }

    private fun onItemClicked(item: Notif) {
        item.data?.meta?.let { meta ->
            navigateToRelatedPage(meta)
        }
    }

    private fun navigateToRelatedPage(meta: Meta) {
        when (meta.type) {

            "Profile" -> {
                navItemSelect(R.id.profile_dest)
            }

            "Ticket" -> {
                navigateToSupportPage()
            }

            "Offer" -> {
                meta.id?.let { id -> navigateToOffersShift(id) }
            }

            "OfferAccepted" -> {
                findNavController().navigate(
                    R.id.action_notif_dest_to_confirmed_shift,
                    bundleOf("shift_id" to meta.id)
                )
            }

            "Invoice" -> {
                findNavController().navigate(
                    R.id.action_notif_dest_to_pay_now_invoice,
                    bundleOf("invoice_id" to meta.id)
                )
            }

            "Resubmission" -> {
                meta.id?.let { id -> navigateToOffersShiftDetails(id) }
            }

            "Money" -> {
                navItemSelect(R.id.money_dest)
            }

        }
    }

    private fun navigateToOffersShift(shiftId: String) {
        viewModel.getOfferDetails(shiftId) { item ->
            hideLoading()
            if (item?.shift != null) {
                findNavController().navigate(
                    R.id.action_notif_dest_to_offers,
                    bundleOf(
                        "shift" to item.shift,
                        "root_page" to "notifications"
                    )
                )
            } else {
                context?.showMessageDialog(
                    message = getString(R.string.this_offer_is_no_longer_available_msg)
                )
            }
        }
    }

    private fun navigateToOffersShiftDetails(shiftId: String) {
        viewModel.getOfferDetails(shiftId) { item ->
            hideLoading()
            if (item != null) {
                findNavController().navigate(
                    R.id.action_notif_dest_to_offer_details,
                    bundleOf("offer" to item)
                )
            } else {
                context?.showMessageDialog(
                    message = getString(R.string.this_offer_is_no_longer_available_msg)
                )
            }
        }
    }

    private fun navigateToSupportPage() {
        (requireActivity() as ClinicActivity).navigateToSupport()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}