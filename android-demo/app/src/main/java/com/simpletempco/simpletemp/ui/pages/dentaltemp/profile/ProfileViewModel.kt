package com.simpletempco.simpletemp.ui.pages.dentaltemp.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.remote.models.request.ActiveNotificationReq
import com.simpletempco.simpletemp.data.remote.models.response.DentalTemp
import com.simpletempco.simpletemp.data.remote.models.response.DentalTempAccountInformation
import com.simpletempco.simpletemp.data.remote.models.response.DentalTempBankInformation
import com.simpletempco.simpletemp.ui.base.BaseViewModel
import com.simpletempco.simpletemp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: AppRepository
) : BaseViewModel() {

    var email = ""
    var notificationState = true

    private val _profileInfoResult = MutableLiveData<DentalTemp?>()
    val profileInfoResult get() = _profileInfoResult

    private val _profileDataResult = SingleLiveEvent<DentalTemp?>()
    val profileDataResult get() = _profileDataResult

    private val _notificationStateResult = MutableLiveData<Boolean>()
    val notificationStateResult: LiveData<Boolean> get() = _notificationStateResult

    private fun updateProfileResult(data: DentalTemp?) {
        notificationState = data?.activeNotification ?: true
        email = data?.email ?: ""
        _profileInfoResult.value = data
        _profileDataResult.value = data
    }

    fun getProfileInfo() = viewModelScope.launch {
        onLoading(true)
        repo.dentalTempProfile().safeApiCall(true) { data ->
            updateProfileResult(data.data)
        }
    }

    private fun doUpdateAccountInfo(
        dentalTempInfo: DentalTempAccountInformation,
        callBack: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.dentalTempAccountInfo(dentalTempInfo)
            .safeApiCall { data ->
                updateProfileResult(data.data)
                callBack.invoke()
            }
    }

    private fun doUpdatePersonalInfo(
        certNo: RequestBody,
        graduationYear: RequestBody,
        specialties: RequestBody,
        dbs: MultipartBody.Part? = null,
        resume: MultipartBody.Part? = null,
        callBack: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.dentalTempPersonalInfo(certNo, graduationYear, specialties, dbs, resume)
            .safeApiCall { data ->
                updateProfileResult(data.data)
                callBack.invoke()
            }
    }

    private fun doUpdateBankInfo(
        bankInfo: DentalTempBankInformation,
        callBack: () -> Unit
    ) = viewModelScope.launch {
        onLoading()
        repo.dentalTempBankInfo(bankInfo)
            .safeApiCall { data ->
                updateProfileResult(data.data)
                callBack.invoke()
            }
    }

    fun updateAccountInfo(
        postalCode: String,
        addressLine1: String,
        addressLine2: String,
        city: String,
        mobile: String,
        callBack: () -> Unit
    ) {
        if (validate(postalCode, addressLine1, city, mobile)) {
            val data = DentalTempAccountInformation(
                addressLine1, addressLine2, city, null, mobile, postalCode
            )
            doUpdateAccountInfo(data, callBack)
        }
    }

    fun updatePersonalInfo(
        certNo: String,
        graduationYear: String,
        specialties: String,
        dbs: File?,
        resume: File?,
        callBack: () -> Unit
    ) {

        val isValidate = if (isHygienistUser())
            validate(certNo, graduationYear)
        else
            validate(certNo, graduationYear, specialties)

        if (isValidate) {

            val certNoRb = certNo.toRequestBody(MultipartBody.FORM)
            val graduationYearRb = graduationYear.toRequestBody(MultipartBody.FORM)
            val specialtiesRb = specialties.toRequestBody(MultipartBody.FORM)

            val dbsMp = if (dbs != null) {
                val requestFile = dbs.asRequestBody(MultipartBody.FORM)
                MultipartBody.Part.createFormData("dbs", dbs.name, requestFile)
            } else {
                null
            }

            val resumeMp = if (resume != null) {
                val requestFile = resume.asRequestBody(MultipartBody.FORM)
                MultipartBody.Part.createFormData("resume", resume.name, requestFile)
            } else {
                null
            }

            doUpdatePersonalInfo(
                certNoRb,
                graduationYearRb,
                specialtiesRb,
                dbsMp,
                resumeMp,
                callBack
            )
        }
    }

    fun updateBankInfo(
        iban: String,
        bic: String,
        bankName: String,
        beneficiaryName: String,
        accountNumber: String,
        sortCode: String,
        callBack: () -> Unit
    ) {
        if (validate(iban, bic, bankName, beneficiaryName, accountNumber, sortCode)) {
            val data = DentalTempBankInformation(
                accountNumber, bankName, beneficiaryName, bic, iban, sortCode
            )
            doUpdateBankInfo(data, callBack)
        }
    }

    fun isHygienistUser(): Boolean {
        return repo.getUserType()?.lowercase() == "hygienist"
    }

    fun updateProfileImage(
        profileImage: File
    ) = viewModelScope.launch {

        val requestFile = profileImage.asRequestBody("image".toMediaTypeOrNull())

        val body =
            MultipartBody.Part.createFormData("profile_photo", profileImage.name, requestFile)

        onLoading()
        repo.uploadDentalTempProfileImage(body).safeApiCall { data ->
            updateProfileResult(data.data)
        }
    }

    fun changeStatNotification(
        active: Boolean
    ) = viewModelScope.launch {
        onLoading()
        val data = ActiveNotificationReq(active)
        repo.toggleNotification(data).safeApiCall {
            notificationState = active
            _notificationStateResult.value = true
        }
    }

    fun getUserType() = repo.getUserType()

    fun logout() = viewModelScope.launch {
        repo.clearData()
    }

}