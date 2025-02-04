package com.simpletempco.simpletemp.ui.pages.common.support.tickets.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.simpletempco.simpletemp.data.remote.models.response.Message
import com.simpletempco.simpletemp.databinding.FragmentMessagesBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.ContextUtils.value
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : BaseFragment<MessageViewModel>() {

    private lateinit var ticketId: String
    private lateinit var state: String
    private lateinit var subject: String

    private lateinit var messageAdapter: MessageAdapter

    override val viewModel: MessageViewModel by viewModels()

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.apply {
            ticketId = getString("ticket_id") ?: ""
            state = getString("state") ?: ""
            subject = getString("subject") ?: ""
        }

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { loadData(ticketId) }
        loadData(ticketId)
    }

    private fun loadData(ticketId: String) {
        viewModel.getMessages(ticketId)
    }

    private fun initViews() {

        binding.tvSubject.text = subject

        if (state != "pending") {
            binding.containerSend.visibility = GONE
        }

        setUpRecyclerView()

        binding.btnSend.setOnClickListener {
            viewModel.sendMessage(ticketId, binding.edtMessage.value())
        }
    }

    private fun initObserve() {
        viewModel.messagesResult.observe(viewLifecycleOwner) { data ->
            hideLoading(true)
            setMessageInfo(data)
        }

        viewModel.sendMessagesResult.observe(viewLifecycleOwner) { message ->
            hideLoading()
            binding.edtMessage.setText("")
            message?.let { messageAdapter.addItem(it) }
        }

    }

    private fun setUpRecyclerView() {

        messageAdapter = MessageAdapter()

        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messageAdapter
        }
    }

    private fun setMessageInfo(data: List<Message>?) {
        messageAdapter.setItems(data ?: emptyList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}