package com.simpletempco.simpletemp.ui.pages.dentaltemp.home.shifts.available

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.HourlyPrices
import com.simpletempco.simpletemp.data.remote.models.response.Offer
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.databinding.FragmentAvailableShiftDetailBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.ui.pages.dentaltemp.home.shifts.ShiftsViewModel
import com.simpletempco.simpletemp.util.ContextUtils.showMessageDialog
import com.simpletempco.simpletemp.util.ContextUtils.showSingleChoiceItemsDialog
import com.simpletempco.simpletemp.util.DateUtil
import com.simpletempco.simpletemp.util.DateUtil.DATE_PATTERN_LONG
import com.simpletempco.simpletemp.util.DateUtil.changeStringDateFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvailableShiftDetailFragment : BaseFragment<ShiftsViewModel>() {

    private var shift: Shift? = null
    private var hourlyPrices: HourlyPrices? = null
    private var preferredPrice: Int? = null

    override val viewModel: ShiftsViewModel by activityViewModels()

    private var _binding: FragmentAvailableShiftDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAvailableShiftDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get date from previous page
        arguments?.let {
            shift = it.getParcelable("shift")
        }

        initViews()
        initObserve()
        initInfo()
    }

    private fun initViews() {

        binding.tvHourlyRate.setOnClickListener {
            setupHourlyRate()
        }

        binding.btnSendOffer.setOnClickListener {
            showSendOfferDialog()
        }

    }

    private fun initObserve() {
        viewModel.hourlyPricesResult.observe(viewLifecycleOwner) {
            hideLoading()
            hourlyPrices = it
            showHourlyRateDialog()
        }
    }

    private fun initInfo() {
        if (shift == null) return

        binding.apply {

            val arrivalTime = DateUtil.convertDateToTime(shift?.arrivalTime ?: "")
            val endTime = DateUtil.convertDateToTime(shift?.endTime ?: "")
            tvTime.text = String.format("%s - %s", arrivalTime, endTime)

            shift?.clinic?.let { clinic ->
                tvName.text = clinic.fullname
                tvAddress.text = String.format(
                    "%s, %s • %s",
                    clinic.profile?.accountInformation?.addressLine1 ?: "",
                    clinic.profile?.accountInformation?.city ?: "",
                    clinic.distance ?: ""
                )
            }

            shift?.date?.let { date ->
                tvDate.text = changeStringDateFormat(
                    date,
                    DATE_PATTERN_LONG,
                    DateUtil.DATE_PATTERN_WEEK_DAY_LONG
                )
            }

            shift?.unpaidBreakTime?.let { breakTime ->
                tvUnpaidBreakTime.text = if (breakTime.lowercase() == "unpaid") {
                    getString(R.string.zero)
                } else {
                    String.format("%s min", breakTime)
                }
            }

            shift?.clinic?.profile?.detailInformation?.let { details ->
                tvSoftware.text = details.software
                tvCharting.text = details.charting
                tvUltrasonic.text = details.ultrasonic
                tvRadiography.text = details.radiography
                tvParking.text = details.parking
            }

            preferredPrice?.let { price ->
                tvHourlyRate.text = String.format("%s (£/hr)", price)
            }

        }
    }

    private fun showSendOfferDialog() {
        requireContext().showMessageDialog(
            title = getString(R.string.send_offer_title),
            message = getShiftDetailMessage(),
            positiveButtonText = getString(R.string.yes),
            negativeButtonText = getString(R.string.no),
            onPositiveButtonClick = { sendOffer() }
        )
    }

    private fun sendOffer() {
        shift?.id?.let { id ->
            viewModel.sendOffer(id, preferredPrice) { offer ->
                hideLoading()
                viewModel.needUpdateCalendarData = true
                navigateToOfferConfirmation(offer)
            }
        }
    }

    private fun setupHourlyRate() {
        if (hourlyPrices == null) {
            viewModel.getHourlyPrices()
        } else {
            showHourlyRateDialog()
        }
    }

    private fun getShiftDetailMessage(): String {
        return String.format(
            "%s\n%s\n%s\n%s £/hr",
            binding.tvName.text,
            changeStringDateFormat(
                shift?.date!!,
                pattern = DATE_PATTERN_LONG,
                toFormat = DateUtil.DATE_PATTERN_WEEK_DAY
            ),
            binding.tvTime.text,
            preferredPrice ?: ""
        )
    }

    private fun showHourlyRateDialog() {

        val current: String
        val items = if (viewModel.isNurseUser()) {

            current = if (
                preferredPrice == shift?.preferredPrice ||
                preferredPrice == null
            ) {
                "${shift?.preferredPrice} £/hr (Clinics Preferred Rate)"
            } else {
                String.format("%s £/hr", preferredPrice)
            }

            hourlyPrices?.nurseHourlyPrices?.map {
                if (it == shift?.preferredPrice)
                    "$it £/hr (Clinics Preferred Rate)"
                else
                    "$it £/hr"
            }
        } else {

            current = if (
                preferredPrice == shift?.preferredPrice ||
                preferredPrice == null
            ) {
                "${shift?.preferredPrice} £/hr (Clinics Preferred Rate)"
            } else {
                String.format("%s £/hr", preferredPrice)
            }

            hourlyPrices?.hygienistHourlyPrices?.map {
                if (it == shift?.preferredPrice)
                    "$it £/hr (Clinics Preferred Rate)"
                else
                    "$it £/hr"
            }
        }

        requireContext().showSingleChoiceItemsDialog(
            title = getString(R.string.select_your_hourly_rate),
            items = items?.toTypedArray() ?: emptyArray(),
            currentSelect = current,
            onSelectItemCallback = { selectedItem ->
                preferredPrice = selectedItem.split(" £").first().toIntOrNull()
                binding.tvHourlyRate.text =
                    String.format("%s (£/hr)", preferredPrice)
            }
        )
    }

    private fun navigateToOfferConfirmation(offer: Offer?) {
        offer?.let { data ->
            findNavController().navigate(
                R.id.action_available_shift_to_offer_sent,
                bundleOf(
                    "offer_id" to data.id,
                    "shift_detail" to getShiftDetailMessage()
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}