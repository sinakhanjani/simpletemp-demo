package com.simpletempco.simpletemp.ui.pages.clinic.money.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.Invoice
import com.simpletempco.simpletemp.databinding.FragmentRatingBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.AppRatingBarChangeListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : BaseFragment<ReviewViewModel>() {

    private var isPayNow = true
    private var rootPage: String? = null
    private var invoice: Invoice? = null

    override val viewModel: ReviewViewModel by viewModels()

    private var _binding: FragmentRatingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            invoice = it.getParcelable("invoice")
            isPayNow = it.getBoolean("pay_now", true)
            rootPage = it.getString("root_page", null)
        }

        initViews()
    }

    private fun initViews() {

        binding.tvTitle.text = String.format(
            "%s %s",
            getString(R.string.please_write_a_review_about),
            invoice?.dentalTemp?.fullname
        )

        binding.rbRating.addRatingChangeListener(object : AppRatingBarChangeListener {
            override fun onRatingChange(rating: Int) {
                binding.tvRatingAvg.text = String.format("%d.0", rating)
            }
        })

        binding.btnSubmit.setOnClickListener {
            rating()
        }

    }

    private fun rating() {
        binding.apply {
            viewModel.review(
                shiftId = invoice?.shift?.id,
                rate = rbRating.getRating(),
                competencyAndSkills = rbRatingCompetencyAndSkills.getRating(),
                timekeeping = rbRatingTimekeeping.getRating(),
                professionalism = rbRatingProfessionalism.getRating(),
                description = edtReview.text?.toString()
            ) {
                hideLoading()
                navigateToPay()
            }
        }
    }

    private fun navigateToPay() {
        if (isPayNow) {
            findNavController().navigate(
                if (rootPage == "notifications") {
                    R.id.action_review_to_pay_now_invoice_again
                } else {
                    R.id.action_review_to_pay_now_invoice
                },
                bundleOf(
                    "invoice_id" to invoice?.id,
                    "root_page" to rootPage
                )
            )
        } else {
            findNavController().navigate(
                R.id.action_review_to_pay_manually_invoice,
                bundleOf("invoice_id" to invoice?.id)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}