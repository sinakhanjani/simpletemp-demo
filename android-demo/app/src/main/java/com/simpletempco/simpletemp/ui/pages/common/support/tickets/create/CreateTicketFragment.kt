package com.simpletempco.simpletemp.ui.pages.common.support.tickets.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.databinding.FragmentCreateTicketBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.ContextUtils.showMessageDialog
import com.simpletempco.simpletemp.util.ContextUtils.value
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTicketFragment : BaseFragment<CreateTicketViewModel>() {

    override val viewModel: CreateTicketViewModel by viewModels()

    private var _binding: FragmentCreateTicketBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserve()
    }

    private fun initViews() {

        binding.tvPriority.setOnClickListener { showPriorityDialog() }

        binding.tvCategory.setOnClickListener { showCategoryDialog() }

        binding.btnSend.setOnClickListener { createTicket() }

    }

    private fun initObserve() {
        viewModel.createTicketResult.observe(viewLifecycleOwner) {
            hideLoading()
            successCreateTicket()
        }
    }

    private fun createTicket() {
        binding.apply {
            viewModel.createTicket(
                edtSubject.value(),
                edtDescription.value(),
                tvPriority.value(),
                tvCategory.value()
            )
        }
    }

    private fun showPriorityDialog() {

        val priorities = resources.getStringArray(R.array.priority)
        val currentSelect = binding.tvPriority.text.toString()
        val selectedItem = priorities.indexOf(currentSelect)

        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.priority))
            setSingleChoiceItems(
                priorities,
                selectedItem
            ) { dialog, which ->
                binding.tvPriority.text = priorities[which]
                dialog.dismiss()
            }
            show()
        }
    }

    private fun showCategoryDialog() {

        val departments = resources.getStringArray(R.array.departments)
        val currentSelect = binding.tvPriority.text.toString()
        val selectedItem = departments.indexOf(currentSelect)

        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.category))
            setSingleChoiceItems(
                departments,
                selectedItem
            ) { dialog, which ->
                binding.tvCategory.text = departments[which]
                dialog.dismiss()
            }
            show()
        }
    }

    private fun successCreateTicket() {
        requireContext().showMessageDialog(
            title = getString(R.string.message_sent_successfully),
            message = getString(R.string.message_sent_msg),
            positiveButtonText = getString(R.string.done),
            cancelable = false,
            onPositiveButtonClick = {
                popBackStack()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}