package com.simpletempco.simpletemp.ui.pages.common.authentication.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.simpletempco.simpletemp.databinding.FragmentNewPasswordBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.util.ContextUtils.value
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPasswordFragment : BaseFragment<NewPasswordViewModel>() {

    private var isProfileRoute = false
    private lateinit var passwordToken: String
    private lateinit var userType: String
    private lateinit var email: String

    override val viewModel: NewPasswordViewModel by viewModels()

    private var _binding: FragmentNewPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get info from previous fragment
        arguments?.apply {
            userType = getString("user_type") ?: ""
            email = getString("email") ?: ""
            passwordToken = getString("password_token") ?: ""
            isProfileRoute = getBoolean("is_profile_route", false)
        }

        initViews()
        initObserve()
    }

    private fun initViews() {

        binding.btnResetPassword.setOnClickListener { resetPassword() }

    }

    private fun initObserve() {

        viewModel.newPasswordResult.observe(viewLifecycleOwner) {
            hideLoading()
            navigateToNextPage()
        }

    }

    private fun resetPassword() {
        viewModel.resetPassword(
            email,
            binding.tilPassword.value(),
            binding.tilConfirmPassword.value(),
            passwordToken,
            userType
        )
    }

    private fun navigateToNextPage() {
        // navigate to login page
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}