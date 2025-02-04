package com.simpletempco.simpletemp.ui.pages.dentaltemp.home.shifts.available.offer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.databinding.FragmentOfferSentBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.ContextUtils.showMessageDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfferSentFragment : BaseFragment<OfferSentViewModel>() {

    private var offerId: String? = null
    private var shiftDetail: String? = null

    override val viewModel: OfferSentViewModel by viewModels()

    private var _binding: FragmentOfferSentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOfferSentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get date from previous page
        arguments?.let {
            offerId = it.getString("offer_id")
            shiftDetail = it.getString("shift_detail")
        }

        initViews()
    }

    private fun initViews() {

        binding.tvShiftInfo.text = shiftDetail

        binding.btnBookAnotherShift.setOnClickListener {
            navigateToShifts()
        }

        binding.btnCancel.setOnClickListener {
            showCancelOfferDialog()
        }

    }

    private fun showCancelOfferDialog() {
        requireContext().showMessageDialog(
            title = getString(R.string.cancel_offer_title),
            message = shiftDetail,
            positiveButtonText = getString(R.string.no),
            negativeButtonText = getString(R.string.yes),
            onNegativeButtonClick = { cancelOffer() }
        )
    }

    private fun cancelOffer() {
        offerId?.let { id ->
            viewModel.cancelOffer(id) {
                hideLoading()
                showOfferWasCancelled()
            }
        }
    }

    private fun showOfferWasCancelled() {
        requireContext().showMessageDialog(
            title = getString(R.string.offer_cancelled),
            message = String.format("Your offer for\n%s\nwas cancelled", shiftDetail),
            positiveButtonText = getString(R.string.ok),
            onPositiveButtonClick = { popBackStack() },
            cancelable = false
        )
    }

    private fun navigateToShifts() {
        findNavController().popBackStack(R.id.available_shifts, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}