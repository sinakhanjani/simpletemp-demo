package com.simpletempco.simpletemp.ui.pages.dentaltemp.profile.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.databinding.FragmentAccountSettingsBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.ui.pages.dentaltemp.DentalTempActivity
import com.simpletempco.simpletemp.ui.pages.dentaltemp.profile.ProfileViewModel
import com.simpletempco.simpletemp.util.ContextUtils.navigateToTermsOfServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountSettingsFragment : BaseFragment<ProfileViewModel>() {

    override val viewModel: ProfileViewModel by activityViewModels()

    private var _binding: FragmentAccountSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserve()
    }

    private fun initViews() {

        binding.swEmailNotifications.isChecked = viewModel.notificationState

        binding.swEmailNotifications.setOnCheckedChangeListener { _, isChecked ->
            changeNotificationState(isChecked)
        }

        binding.tvChangePassword.setOnClickListener {
            navigateToChangePassword()
        }

        binding.tvContactUs.setOnClickListener {
            (requireActivity() as DentalTempActivity).navigateToFaq()
        }

        binding.tvTermsOfService.setOnClickListener {
            navigateToTermsOfServices()
        }

    }

    private fun initObserve() {
        viewModel.notificationStateResult.observe(viewLifecycleOwner) { result ->
            if (result) {
                hideLoading()
            }
        }
    }

    private fun changeNotificationState(isChecked: Boolean) {
        viewModel.changeStatNotification(isChecked)
    }

    private fun navigateToChangePassword() {
        findNavController().navigate(
            R.id.action_account_settings_to_forgot_password,
            bundleOf(
                "user_type" to viewModel.getUserType(),
                "email" to viewModel.email,
                "is_profile_route" to true
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}