package com.simpletempco.simpletemp.ui.pages.common.support.tickets.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Ticket
import com.simpletempco.simpletemp.databinding.FragmentTicketsListBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketsListFragment : BaseFragment<TicketsListViewModel>() {

    private lateinit var ticketsAdapter: TicketsAdapter

    override val viewModel: TicketsListViewModel by viewModels()

    private var _binding: FragmentTicketsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketsListBinding.inflate(inflater, container, false)
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
        loadingMode()
        viewModel.getTickets()
    }

    private fun initViews() {

        setUpRecyclerView()

        binding.fabNew.setOnClickListener {
            navigateToCreateTicket()
        }

        binding.btnNewTicket.setOnClickListener {
            navigateToCreateTicket()
        }
    }

    private fun initObserve() {
        viewModel.ticketsResult.observe(viewLifecycleOwner) { data ->
            hideLoading(true)
            setTicketInfo(data)
        }
    }

    private fun setUpRecyclerView() {

        ticketsAdapter = TicketsAdapter { ticketId ->
            openTicket(ticketId)
        }

        binding.rvTickets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ticketsAdapter
        }
    }

    private fun setTicketInfo(data: List<Ticket>?) {
        if (data.isNullOrEmpty()) {
            binding.llContainer.visibility = VISIBLE
            binding.rvTickets.visibility = GONE
            binding.fabNew.visibility = GONE
        } else {
            binding.llContainer.visibility = GONE
            binding.rvTickets.visibility = VISIBLE
            binding.fabNew.visibility = VISIBLE
            ticketsAdapter.setItems(data)
        }
    }

    private fun openTicket(ticket: Ticket) {
        findNavController().navigate(
            R.id.action_ticket_list_to_messages,
            bundleOf(
                "ticket_id" to ticket.id,
                "state" to ticket.state,
                "subject" to ticket.subject
            )
        )
    }

    private fun navigateToCreateTicket() {
        findNavController().navigate(R.id.action_ticket_list_to_create_ticket)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}