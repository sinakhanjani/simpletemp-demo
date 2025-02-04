package com.simpletempco.simpletemp.ui.pages.dentaltemp.profile.myprofile.bank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.DentalTemp
import com.simpletempco.simpletemp.databinding.FragmentBankInfoBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.ui.pages.dentaltemp.profile.ProfileViewModel
import com.simpletempco.simpletemp.util.ContextUtils.value
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BankInfoFragment : BaseFragment<ProfileViewModel>() {

    private var isEditBank = false
    private var isProfileComplete = false

    override val viewModel: ProfileViewModel by activityViewModels()

    private var _binding: FragmentBankInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBankInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            isProfileComplete = it.getBoolean("is_profile_complete", false)
            isEditBank = it.getBoolean("is_edit_bank", false)
        }

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { loadData() }

        if (isEditBank) {
            loadData()
        } else {
            hideLoading(true)
        }
    }

    private fun loadData() {
        viewModel.getProfileInfo()
    }

    private fun initViews() {

        if (!isProfileComplete) {
            binding.containerStepBar.visibility = View.GONE
        } else {
            changeTitle(getString(R.string.complete_profile))
        }

        binding.btnBtnSave.setOnClickListener {
            updateBankInfo()
        }

    }

    private fun initObserve() {
        viewModel.profileInfoResult.observe(viewLifecycleOwner) { data ->
            hideLoading()
            hideLoading(true)
            setInfo(data)
        }
    }

    private fun setInfo(data: DentalTemp?) {
        if (data == null) return

        binding.apply {

            data.profile?.bankInformation?.let { bankInfo ->

                bankInfo.iban?.let { iban ->
                    edtIban.setText(iban)
                }

                bankInfo.bic?.let { bic ->
                    edtBic.setText(bic)
                }

                bankInfo.bankName?.let { bankName ->
                    edtBankName.setText(bankName)
                }

                bankInfo.beneficiaryName?.let { beneficiaryName ->
                    edtBeneficiaryName.setText(beneficiaryName)
                }

                bankInfo.accountNumber?.let { accountNumber ->
                    edtBankAccountNumber.setText(accountNumber)
                }

                bankInfo.sortCode?.let { sortCode ->
                    edtSortNumber.setText(sortCode)
                }

            }
        }
    }

    private fun updateBankInfo() {
        binding.apply {
            viewModel.updateBankInfo(
                edtIban.value(),
                edtBic.value(),
                edtBankName.value(),
                edtBeneficiaryName.value(),
                edtBankAccountNumber.value(),
                edtSortNumber.value()
            ) { saveDataSuccess() }
        }
    }

    private fun saveDataSuccess() {
        hideLoading()
        if (isProfileComplete) {
            findNavController().popBackStack(R.id.profile_dest, false)
        } else if (isEditBank) {
            navItemSelect(R.id.money_dest)
        } else {
            popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}