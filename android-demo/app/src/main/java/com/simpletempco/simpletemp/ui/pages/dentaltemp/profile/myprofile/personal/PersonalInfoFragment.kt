package com.simpletempco.simpletemp.ui.pages.dentaltemp.profile.myprofile.personal

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.anggrayudi.storage.SimpleStorageHelper
import com.anggrayudi.storage.file.fullName
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.data.remote.models.response.DentalTemp
import com.simpletempco.simpletemp.databinding.FragmentPersonalInfoBinding
import com.simpletempco.simpletemp.ui.base.BaseFragment
import com.simpletempco.simpletemp.ui.pages.dentaltemp.profile.ProfileViewModel
import com.simpletempco.simpletemp.util.AppConfig.DOCUMENTS_FORMAT_ALLOWED
import com.simpletempco.simpletemp.util.ContextUtils.getStoragePermission
import com.simpletempco.simpletemp.util.ContextUtils.hasStoragePermission
import com.simpletempco.simpletemp.util.ContextUtils.showMultiChoiceItemsDialog
import com.simpletempco.simpletemp.util.ContextUtils.showSingleChoiceItemsDialog
import com.simpletempco.simpletemp.util.ContextUtils.value
import com.simpletempco.simpletemp.util.changeColor
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.*

const val DBS_REQUEST_CODE = 10
const val RESUME_REQUEST_CODE = 12

@AndroidEntryPoint
class PersonalInfoFragment : BaseFragment<ProfileViewModel>() {

    private var isProfileComplete = false

    private var dbsFile: File? = null
    private var resumeFile: File? = null

    private val storageHelper = SimpleStorageHelper(this)

    override val viewModel: ProfileViewModel by activityViewModels()

    private var _binding: FragmentPersonalInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            isProfileComplete = it.getBoolean("is_profile_complete", false)
        }

        initStorage()
        initViews()
        initObserve()
    }

    private fun initViews() {

        binding.apply {

            if (!isProfileComplete) {
                binding.containerStepBar.visibility = GONE
            } else {
                changeTitle(getString(R.string.complete_profile))
                binding.btnConfirm.text = getString(R.string.next_)
            }

            if (viewModel.isHygienistUser()) {
                specialtiesTitle.visibility = GONE
                tvSpecialties.visibility = GONE
            }

            tvSpecialties.setOnClickListener { showSpecialtiesDialog() }

            tvGraduationYear.setOnClickListener { showGraduationYearDialog() }

            tvDbsCertificate.setOnClickListener { pickFile(DBS_REQUEST_CODE) }

            tvResume.setOnClickListener { pickFile(RESUME_REQUEST_CODE) }

            btnConfirm.setOnClickListener {
                updatePersonalInfo()
            }
        }

    }

    private fun initObserve() {
        viewModel.profileInfoResult.observe(viewLifecycleOwner) { data ->
            setInfo(data)
        }
    }

    private fun initStorage() {
        storageHelper.onFileSelected = { requestCode, files ->
            when (requestCode) {
                DBS_REQUEST_CODE -> {
                    binding.tvDbsCertificate.text = files.first().fullName
                    binding.tvDbsCertificate.changeColor(R.color.colorGreen)
                    dbsFile = File(FileUriUtils.getRealPath(requireContext(), files.first().uri)!!)
                }
                RESUME_REQUEST_CODE -> {
                    binding.tvResume.text = files.first().fullName
                    binding.tvResume.changeColor(R.color.colorGreen)
                    resumeFile =
                        File(FileUriUtils.getRealPath(requireContext(), files.first().uri)!!)
                }
            }
        }
    }

    private fun setInfo(data: DentalTemp?) {
        if (data == null) return

        binding.apply {

            data.profile?.personalInformation?.let { personalInfo ->

                personalInfo.certificationNumber?.let { certNo ->
                    edtCertification.setText(certNo)
                }

                personalInfo.graduationYear?.let { year ->
                    tvGraduationYear.text = year
                }

                personalInfo.specialties?.let { specialties ->
                    tvSpecialties.text = TextUtils.join(", ", specialties)
                }

                personalInfo.dbsCertificationURL?.let { dbsCert ->
                    tvDbsCertificate.text = dbsCert
                    tvDbsCertificate.changeColor(R.color.colorGreen)
                }

                personalInfo.resumeURL?.let { resume ->
                    tvResume.text = resume
                    tvResume.changeColor(R.color.colorGreen)
                }

            }
        }
    }

    private fun showSpecialtiesDialog() {

        binding.edtCertification.clearFocus()

        requireContext().showMultiChoiceItemsDialog(
            title = getString(R.string.specialties),
            items = resources.getStringArray(R.array.specialties),
            currentSelect = binding.tvSpecialties.text.toString()
        ) { selectedItems ->
            binding.tvSpecialties.text = TextUtils.join(", ", selectedItems)
        }
    }

    private fun showGraduationYearDialog() {

        binding.edtCertification.clearFocus()

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val items = (currentYear - 40).rangeTo(currentYear + 1).map { item -> item.toString() }

        requireContext().showSingleChoiceItemsDialog(
            title = getString(R.string.graduation_year),
            items = items.toTypedArray(),
            currentSelect = binding.tvGraduationYear.text.toString()
        ) { selectedItem ->
            binding.tvGraduationYear.text = selectedItem
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Mandatory for Activity, but not for Fragment & ComponentActivity
        storageHelper.storage.onActivityResult(requestCode, resultCode, data)
    }

    private fun pickFile(requestCode: Int) {
        if (hasStoragePermission()) {
            storageHelper.openFilePicker(requestCode, false, *DOCUMENTS_FORMAT_ALLOWED)
        } else {
            getStoragePermission()
        }
    }

    private fun updatePersonalInfo() {
        binding.apply {
            viewModel.updatePersonalInfo(
                edtCertification.value(),
                tvGraduationYear.value(),
                tvSpecialties.value().replace(" ", ""),
                dbsFile,
                resumeFile
            ) {
                saveDataSuccess()
            }
        }
    }

    private fun saveDataSuccess() {
        hideLoading()
        if (isProfileComplete) {
            findNavController().navigate(
                R.id.action_personal_info_to_bank_info,
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