package com.simpletempco.simpletemp.ui.pages.common.authentication.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.databinding.FragmentAuthBinding
import com.simpletempco.simpletemp.util.ContextUtils.navigateToPrivacyPolicy

class AuthFragment : Fragment() {

    private lateinit var userType: String

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get user type
        userType = arguments?.getString("user_type") ?: ""

        initViews()
    }

    private fun initViews() {

        binding.btnSignUp.setOnClickListener { navigateToSignUp() }

        binding.btnLogin.setOnClickListener { navigateToLogin() }

        binding.tvPrivacyPolicy.setOnClickListener { navigateToPrivacyPolicy() }

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

    }

    private fun navigateToLogin() {
        findNavController().navigate(
            R.id.action_auth_to_login,
            bundleOf("user_type" to userType)
        )
    }

    private fun navigateToSignUp() {
        findNavController().navigate(
            R.id.action_auth_to_register,
            bundleOf("user_type" to userType)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}