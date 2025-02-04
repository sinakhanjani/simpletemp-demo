package com.simpletempco.simpletemp.ui.pages.common.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.simpletempco.simpletemp.App
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.databinding.FragmentRegisterBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.ContextUtils.navigateToPrivacyPolicy
import com.simpletempco.simpletemp.util.ContextUtils.navigateToTermsOfServices
import com.simpletempco.simpletemp.util.ContextUtils.value
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<RegisterViewModel>() {

    private lateinit var userType: String

    override val viewModel: RegisterViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get user type
        userType = arguments?.getString("user_type") ?: ""

        initViews()
        initObserve()
    }

    private fun initViews() {

        binding.btnSignUp.setOnClickListener {
            signUp()
        }

        binding.tvLogin.setOnClickListener {
            navigateToLogin()
        }

        binding.tvPrivacyPolicy.setOnClickListener {
            navigateToPrivacyPolicy()
        }

        binding.tvTermsOfService.setOnClickListener {
            navigateToTermsOfServices()
        }

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

    }

    private fun initObserve() {

        viewModel.registerResult.observe(viewLifecycleOwner) {
            if (it) {
                hideLoading()
                updateFcmToken()
                navigateToVerification()
            }
        }

    }

    private fun signUp() {
        binding.apply {
            viewModel.register(
                edtName.value(),
                edtEmail.value(),
                tilPassword.value(),
                tilRePassword.value(),
                userType
            )
        }
    }

    private fun updateFcmToken() {
        (requireActivity().applicationContext as App).initFcmToken()
    }

    private fun navigateToVerification() {
        findNavController().navigate(
            R.id.action_register_to_verification,
            bundleOf(
                "email" to binding.edtEmail.value(),
                "user_type" to userType
            )
        )
    }

    private fun navigateToLogin() {
        findNavController().navigate(
            R.id.action_register_to_login,
            bundleOf("user_type" to userType)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}