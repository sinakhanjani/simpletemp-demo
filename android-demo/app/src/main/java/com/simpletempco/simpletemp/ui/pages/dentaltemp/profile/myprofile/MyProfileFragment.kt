package com.simpletempco.simpletemp.ui.pages.dentaltemp.profile.myprofile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.DentalTemp
import com.simpletempco.simpletemp.databinding.FragmentMyProfileBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.ui.pages.dentaltemp.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileFragment : BaseFragment<ProfileViewModel>() {

    override val viewModel: ProfileViewModel by activityViewModels()

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserve()
    }

    private fun initViews() {

        binding.apply {

            containerAccountInfo.setOnClickListener {
                findNavController().navigate(R.id.action_my_profile_to_account_info)
            }

            containerPersonalInfo.setOnClickListener {
                findNavController().navigate(R.id.action_my_profile_to_personal_info)
            }

            containerBankInfo.setOnClickListener {
                findNavController().navigate(R.id.action_my_profile_to_bank_info)
            }

        }

    }

    private fun initObserve() {
        viewModel.profileInfoResult.observe(viewLifecycleOwner) { data ->
            setInfo(data)
        }
    }

    private fun setInfo(data: DentalTemp?) {
        if (data == null) return

        binding.apply {

            data.profile?.let { profile ->
                profile.accountInformation?.addressLine1?.let { address ->
                    tvAddress.text = address
                }

                profile.accountInformation?.mobile?.let { mobile ->
                    tvCellphone.text = mobile
                }

                profile.personalInformation?.certificationNumber?.let { certNo ->
                    tvCertification.text = certNo
                }

                profile.personalInformation?.graduationYear?.let { certNo ->
                    tvGraduation.text = certNo
                }

                profile.bankInformation?.accountNumber?.let { accountNo ->
                    tvAccountNumber.text = accountNo
                }

                profile.bankInformation?.sortCode?.let { sortCode ->
                    tvSortCode.text = sortCode
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}