package com.simpletempco.simpletemp.ui.pages.dentaltemp.notif

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
import com.simpletempco.simpletemp.databinding.FragmentNotificationBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.ui.pages.dentaltemp.DentalTempActivity
import com.simpletempco.simpletemp.util.AppConfig.NOTIFICATION_QUERY_PER_PAGE
import com.simpletempco.simpletemp.util.EndlessNestedScrollViewOnScrollListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment<NotificationViewModel>() {

    private var page = 0

    private lateinit var notificationAdapter: NotificationAdapter

    private lateinit var onScrollListener: EndlessNestedScrollViewOnScrollListener

    override val viewModel: NotificationViewModel by viewModels()

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
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
            notificationAdapter = NotificationAdapter { onItemClicked(it) }
        else
            hideLoading(true)

        // scroll listener for recycler view
        onScrollListener = object : EndlessNestedScrollViewOnScrollListener() {
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

            "Rate" -> {
                findNavController().navigate(R.id.action_notif_dest_to_my_rating)
            }

            "Ticket" -> {
                navigateToSupportPage()
            }

            "OfferAccepted" -> {
                findNavController().navigate(
                    R.id.action_notif_dest_to_confirmed_shift_detail,
                    bundleOf("shift_id" to meta.id)
                )
            }

            "Dispute" -> {
                findNavController().navigate(
                    R.id.action_notif_dest_to_disputed_invoice,
                    bundleOf("disputed_invoice_id" to meta.id)
                )
            }

            "Resubmission" -> {
                meta.id?.let { id -> resubmitOffer(id) }
            }

        }
    }

    private fun resubmitOffer(offerId: String) {
        viewModel.resubmitOffer(offerId) {
            hideLoading()

        }
    }

    private fun navigateToSupportPage() {
        (requireActivity() as DentalTempActivity).navigateToSupport()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}