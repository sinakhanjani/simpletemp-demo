package com.simpletempco.simpletemp.ui.pages.clinic.home

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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.HomeShiffts
import com.simpletempco.simpletemp.data.remote.models.response.Shift
import com.simpletempco.simpletemp.databinding.FragmentClinicHomeBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.DateUtil.currentDate
import com.simpletempco.simpletemp.util.DateUtil.getWeekDaysDate
import com.simpletempco.simpletemp.util.ShiftsAdapterCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel>(), ShiftsAdapterCallback {

    private var selectedDate: String? = null
    private var isStatusPending = true

    private var pendingShifts: List<HomeShiffts>? = null
    private var confirmedShifts: List<HomeShiffts>? = null

    private lateinit var weekDaysAdapter: WeekDaysAdapter
    private lateinit var shiftsAdapter: ShiftsAdapter

    override val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentClinicHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClinicHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { loadData() }
    }

    private fun loadData() {
        viewModel.getPendingShifts()
    }

    private fun initViews() {

        if (!this::weekDaysAdapter.isInitialized) {
            weekDaysAdapter = WeekDaysAdapter { onWeekDayClicked(it) }
            val weekDays = getWeekDaysDate()
            weekDaysAdapter.setItems(weekDays)
            selectedDate = weekDays.first().date
        }

        if (!this::shiftsAdapter.isInitialized) {
            shiftsAdapter = ShiftsAdapter()
            shiftsAdapter.addCallback(this)
        }

        binding.rvWeekDays.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = weekDaysAdapter
        }

        binding.rvShifts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shiftsAdapter
        }

        binding.tblShiftStatus.getTabAt(if (isStatusPending) 0 else 1)?.select()

        binding.tblShiftStatus.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                isStatusPending = tab?.position == 0
                updateShifts()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.btnPostedShifts.setOnClickListener {
            findNavController().navigate(
                R.id.action_home_dest_to_clinic_shifts,
                bundleOf("date" to currentDate(toFormat = "yyyy-MM-dd"))
            )
        }

        binding.btnPostShift.setOnClickListener {
            navItemSelect(R.id.post_shift_dest)
        }

        binding.tvFinishedJobs.setOnClickListener {
            // TODO: need to check
            navItemSelect(R.id.money_dest)
        }

    }

    private fun initObserve() {
        viewModel.shiftsResult.observe(viewLifecycleOwner) {
            hideLoading(true)
            parseData(it.first, it.second)
        }
    }

    private fun parseData(
        pendingShifts: List<HomeShiffts>?,
        confirmedShifts: List<HomeShiffts>?
    ) {
        this.pendingShifts = pendingShifts
        this.confirmedShifts = confirmedShifts

        hideAllViews()
        if (pendingShifts.isNullOrEmpty() && confirmedShifts.isNullOrEmpty()) {
            binding.tvWelcome.visibility = VISIBLE
            binding.btnPostShift.visibility = VISIBLE
        } else if (confirmedShifts.isNullOrEmpty() && pendingShifts?.size == 1) {
            showOnePostShiftViews()
            updateShifts()
        } else if (!confirmedShifts.isNullOrEmpty()) {
            showOnePostShiftViews()
            binding.tvFinishedJobs.visibility = VISIBLE
            updateShifts()
        }
    }

    private fun updateShifts() {
        if (isStatusPending) {
            val items = pendingShifts?.find { it.date == selectedDate }?.shifts ?: emptyList()
            shiftsAdapter.setItems(items, true)
        } else {
            val items = confirmedShifts?.find { it.date == selectedDate }?.shifts ?: emptyList()
            shiftsAdapter.setItems(items, false)
        }
    }

    private fun onWeekDayClicked(date: String?) {
        selectedDate = date
        updateShifts()
    }

    override fun onPendingItemClicked(shift: Shift) {
        findNavController().navigate(
            R.id.action_home_dest_to_offers,
            bundleOf(
                "shift" to shift,
                "root_page" to "home"
            )
        )
    }

    override fun onConfirmedItemClicked(shift: Shift) {
        findNavController().navigate(
            R.id.action_home_dest_to_confirmed_shift,
            bundleOf(
                "shift_id" to shift.id,
                "from_root" to true
            )
        )
    }

    private fun hideAllViews() {
        binding.rvShifts.visibility = GONE
        binding.tvWelcome.visibility = GONE
        binding.rvWeekDays.visibility = GONE
        binding.btnPostShift.visibility = GONE
        binding.tvFinishedJobs.visibility = GONE
        binding.containerArrivalTime.visibility = GONE
        binding.btnPostedShifts.visibility = GONE
    }

    private fun showOnePostShiftViews() {
        binding.rvShifts.visibility = VISIBLE
        binding.rvWeekDays.visibility = VISIBLE
        binding.containerArrivalTime.visibility = VISIBLE
        binding.btnPostedShifts.visibility = VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
