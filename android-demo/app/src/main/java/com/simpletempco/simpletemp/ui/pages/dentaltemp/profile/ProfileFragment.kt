package com.simpletempco.simpletemp.ui.pages.dentaltemp.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.simpletempco.simpletemp.BuildConfig
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.DentalTemp
import com.simpletempco.simpletemp.databinding.FragmentProfileBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.ui.pages.common.support.tickets.SupportActivity
import com.simpletempco.simpletemp.ui.pages.dentaltemp.DentalTempActivity
import com.simpletempco.simpletemp.util.ContextUtils.navigateToPrivacyPolicy
import com.simpletempco.simpletemp.util.ContextUtils.navigateToTermsOfServices
import com.simpletempco.simpletemp.util.ContextUtils.showMessageDialog
import com.simpletempco.simpletemp.util.changeColor
import com.simpletempco.simpletemp.util.loadCircularImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel>() {

    override val viewModel: ProfileViewModel by activityViewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserve()
        initNonData(binding.nonDataContainer) { loadData() }
        loadData()
    }

    private fun loadData() {
        viewModel.getProfileInfo()
    }

    private fun initViews() {

        binding.apply {

            tvVersion.text = String.format("Version %s", BuildConfig.VERSION_NAME)

            btnProfileComplete.setOnClickListener {
                findNavController().navigate(
                    R.id.action_profile_dest_to_account_info,
                    bundleOf("is_profile_complete" to true)
                )
            }

            fabProfileImage.setOnClickListener { showImagePicker() }

            tvTermsOfService.setOnClickListener { navigateToTermsOfServices() }

            tvPrivacyPolicy.setOnClickListener { navigateToPrivacyPolicy() }

            tvMyProfile.setOnClickListener {
                navigateToMyProfile()
            }

            tvSettings.setOnClickListener {
                navigateToAccountSettings()
            }

            tvRatings.setOnClickListener {
                navigateToMyRating()
            }

            tvSupport.setOnClickListener { navigateToSupport() }

            tvLogout.setOnClickListener { logout() }

        }
    }

    private fun initObserve() {
        viewModel.profileDataResult.observe(viewLifecycleOwner) { data ->
            hideLoading()
            hideLoading(true)
            setProfileInfo(data)
        }
    }

    private fun navigateToSupport() {
        requireActivity().apply {
            val intent = Intent(this, SupportActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.page_enter, R.anim.page_exit)
        }

    }

    private fun navigateToMyProfile() {
        findNavController().navigate(R.id.action_profile_dest_to_my_profile)
    }

    private fun navigateToAccountSettings() {
        findNavController().navigate(R.id.action_profile_dest_to_account_settings)
    }

    private fun navigateToMyRating() {
        findNavController().navigate(R.id.action_profile_dest_to_my_rating)
    }

    private fun setProfileInfo(data: DentalTemp?) {
        if (data == null) return

        binding.apply {

            tvEmail.text = data.email
            tvName.text = data.fullname
            pgbProfile.progress = data.profile?.percentage ?: 0
            cpProfile.percent(data.profile?.percentage ?: 0)
            tvProfileProgress.text = String.format(
                "%d%% %s",
                data.profile?.percentage ?: 0,
                getString(R.string.complete_profile)
            )

            ivProfile.loadCircularImage(data.photoURL)

            data.badge?.let { badge -> setNotificationBadge(badge) }

            if (data.profile?.percentage == 100) {
                btnProfileComplete.visibility = GONE
                tvMyProfile.visibility = VISIBLE
                dividerMyProfile.root.visibility = VISIBLE
                ivStatus.setImageResource(R.drawable.ic_status_done)
            } else {
                tvMyProfile.visibility = GONE
                dividerMyProfile.root.visibility = GONE
                if (data.profile?.isPendingIdentity == true) {
                    btnProfileComplete.visibility = VISIBLE
                    btnProfileComplete.changeColor(R.color.colorBtnOrange)
                    btnProfileComplete.text = getString(R.string.pending_authentication)
                } else {
                    btnProfileComplete.visibility = VISIBLE
                    btnProfileComplete.changeColor(R.color.colorPrimaryDark)
                    btnProfileComplete.text = getString(R.string.complete_profile)
                }
            }
        }
    }

    private fun showImagePicker() {
        ImagePicker.with(this)
            .cropSquare()
            .compress(1024)
            .maxResultSize(720, 720)
            .createIntent { intent ->
                profileImageResult.launch(intent)
            }
    }

    private val profileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                data?.data?.let { mUri ->
                    viewModel.updateProfileImage(mUri.toFile())
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                context?.showMessageDialog(
                    message = ImagePicker.getError(data)
                )
            }
        }

    private fun logout() {
        requireContext().showMessageDialog(
            title = getString(R.string.confirm_logout),
            message = getString(R.string.logout_msg),
            negativeButtonText = getString(R.string.cancel),
            positiveButtonText = getString(R.string.ok),
            onPositiveButtonClick = {
                (requireActivity() as DentalTempActivity).logout()
            }
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        showBottomNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}