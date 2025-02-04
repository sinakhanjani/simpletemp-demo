package com.simpletempco.simpletemp.ui.pages.dentaltemp.profile.myprofile.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.DentalTemp
import com.simpletempco.simpletemp.databinding.FragmentAccountInfoBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.ui.pages.dentaltemp.profile.ProfileViewModel
import com.simpletempco.simpletemp.util.ContextUtils.value
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountInfoFragment : BaseFragment<ProfileViewModel>() {

    private var isProfileComplete = false

    override val viewModel: ProfileViewModel by activityViewModels()

    private var _binding: FragmentAccountInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            isProfileComplete = it.getBoolean("is_profile_complete", false)
        }

        initViews()
        initObserve()
    }

    private fun initViews() {

        if (!isProfileComplete) {
            binding.containerStepBar.visibility = GONE
        } else {
            changeTitle(getString(R.string.complete_profile))
            binding.btnConfirm.text = getString(R.string.next_)
        }

        binding.btnConfirm.setOnClickListener {
            updateAccountInfo()
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

            data.profile?.accountInformation?.let { accountInfo ->

                accountInfo.postalcode?.let { postCode ->
                    edtPostCode.setText(postCode)
                }

                accountInfo.addressLine1?.let { address ->
                    edtAddressLine1.setText(address)
                }

                accountInfo.addressLine2?.let { address ->
                    edtAddressLine2.setText(address)
                }

                accountInfo.city?.let { city ->
                    edtTownCity.setText(city)
                }

                accountInfo.mobile?.let { mobile ->
                    edtCellphone.setText(mobile.replace("+44", ""))
                }

            }
        }
    }

    private fun updateAccountInfo() {
        binding.apply {
            viewModel.updateAccountInfo(
                edtPostCode.value(),
                edtAddressLine1.value(),
                edtAddressLine2.value(),
                edtTownCity.value(),
                String.format("+44%s", edtCellphone.unMaskedText)
            ) { saveDataSuccess() }
        }
    }

    private fun saveDataSuccess() {
        hideLoading()
        if (isProfileComplete) {
            findNavController().navigate(
                R.id.action_account_info_to_personal_info,
                bundleOf("is_profile_complete" to true)
            )
        } else {
            popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}