package com.simpletempco.simpletemp.ui.pages.common.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.simpletempco.simpletemp.App
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.databinding.FragmentLoginBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.ui.pages.common.authentication.AuthActivity
import com.simpletempco.simpletemp.util.ContextUtils.value
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel>() {

    private lateinit var userType: String

    override val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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

        binding.btnLogin.setOnClickListener { login() }

        binding.tvSignUp.setOnClickListener { navigateToSignUp() }

        binding.tvForgotPassword.setOnClickListener { navigateToForgotPassword() }

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

    }

    private fun initObserve() {

        viewModel.loginResult.observe(viewLifecycleOwner) {
            if (it) {
                hideLoading()
                updateFcmToken()
                when (userType) {
                    "clinic" -> navigateToClinic()
                    else -> navigateToDentalTemp()
                }
            }
        }

    }

    private fun login() {
        binding.apply {
            viewModel.login(
                edtEmail.value(),
                tilPassword.value(),
                userType
            )
        }
    }

    private fun updateFcmToken() {
        (requireActivity().applicationContext as App).initFcmToken()
    }

    private fun navigateToSignUp() {
        findNavController().navigate(
            R.id.action_login_to_register,
            bundleOf("user_type" to userType)
        )
    }

    private fun navigateToForgotPassword() {
        findNavController().navigate(
            R.id.action_login_to_forgot_password,
            bundleOf("user_type" to userType)
        )
    }

    private fun navigateToDentalTemp() {
        (activity as AuthActivity).navigateToDentalTemp()
    }

    private fun navigateToClinic() {
        (activity as AuthActivity).navigateToClinic()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}