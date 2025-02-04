package com.simpletempco.simpletemp.data

import com.simpletempco.simpletemp.data.local.LocalRepo
import com.simpletempco.simpletemp.data.remote.RemoteRepo
import com.simpletempco.simpletemp.data.remote.models.request.*
import com.simpletempco.simpletemp.data.remote.models.response.*
import com.simpletempco.simpletemp.util.AppConfig.OFFERS_QUERY_PER_PAGE
import com.skydoves.sandwich.ApiResponse
import dagger.hilt.android.scopes.ActivityScoped
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ActivityScoped
class AppRepository @Inject constructor(
    private val remote: RemoteRepo,
    private val local: LocalRepo
) {

    suspend fun dentalTempLogin(
        loginReq: LoginReq
    ) = remote.dentalTempLogin(loginReq)

    suspend fun clinicLogin(
        loginReq: LoginReq
    ) = remote.clinicLogin(loginReq)

    suspend fun register(
        registerReq: RegisterReq
    ) = remote.register(registerReq)

    suspend fun verifyCode(
        verifyCodeReq: VerifyCodeReq
    ) = remote.verifyCode(verifyCodeReq)

    suspend fun sendCode(
        codeReq: CodeReq
    ) = remote.sendCode(codeReq)

    suspend fun changePassword(
        changePasswordReq: ChangePasswordReq
    ) = remote.changePassword(changePasswordReq)

    suspend fun checkUpdate() = remote.checkUpdate(getUserType())

    suspend fun dentalTempLogout() = remote.dentalTempLogout(getToken())

    suspend fun clinicLogout() = remote.clinicLogout(getToken())

    suspend fun dentalTempProfile() = remote.dentalTempProfile(getToken())

    suspend fun clinicProfile() = remote.clinicProfile(getToken())

    suspend fun dentalTempAccountInfo(
        dentalTempInfo: DentalTempAccountInformation
    ) = remote.dentalTempAccountInfo(getToken()!!, dentalTempInfo)

    suspend fun dentalTempBankInfo(
        bankInfo: DentalTempBankInformation
    ) = remote.dentalTempBankInfo(getToken()!!, bankInfo)

    suspend fun dentalTempPersonalInfo(
        certNo: RequestBody?,
        graduationYear: RequestBody?,
        specialties: RequestBody?,
        dbs: MultipartBody.Part? = null,
        resume: MultipartBody.Part? = null
    ) = remote.dentalTempPersonalInfo(
        getToken()!!,
        certNo,
        graduationYear,
        specialties,
        dbs,
        resume
    )

    suspend fun uploadDentalTempProfileImage(
        profileImage: MultipartBody.Part
    ) = remote.uploadDentalTempProfileImage(getToken()!!, profileImage)

    suspend fun faq(
        limit: Int = 10,
        page: Int = 0,
        userType: String
    ) = remote.faq(limit, page, userType)

    suspend fun ticket(): ApiResponse<ApiResult<List<Ticket>>> {
        return if (getUserType()?.lowercase() == "clinic") {
            remote.clinicTicket(getToken()!!)
        } else {
            remote.ticket(getToken()!!)
        }
    }

    suspend fun message(
        ticketId: String?
    ): ApiResponse<ApiResult<List<Message>>> {
        return if (getUserType()?.lowercase() == "clinic") {
            remote.clinicMessage(getToken()!!, ticketId)
        } else {
            remote.message(getToken()!!, ticketId)
        }
    }

    suspend fun sendMessage(
        ticketId: String?,
        message: MessageReq
    ): ApiResponse<ApiResult<Message?>> {
        return if (getUserType()?.lowercase() == "clinic") {
            remote.sendClinicMessage(getToken()!!, ticketId, message)
        } else {
            remote.sendMessage(getToken()!!, ticketId, message)
        }
    }

    suspend fun createTicket(
        ticketReq: TicketReq
    ): ApiResponse<ApiResult<Ticket?>> {
        return if (getUserType()?.lowercase() == "clinic") {
            remote.createClinicTicket(getToken()!!, ticketReq)
        } else {
            remote.createTicket(getToken()!!, ticketReq)
        }
    }

    suspend fun toggleNotification(
        activeNotif: ActiveNotificationReq
    ) = remote.toggleNotification(getToken()!!, activeNotif)

    suspend fun getRating(
        limit: Int,
        page: Int
    ) = remote.getRating(getToken()!!, limit, page)

    suspend fun notification(
        limit: Int,
        page: Int
    ) = remote.notification(getToken()!!, limit, page)

    suspend fun jobHistory(
        limit: Int,
        page: Int
    ) = remote.jobHistory(getToken()!!, limit, page)

    suspend fun dentaltempMoney(
        limit: Int,
        page: Int,
        isPaid: Boolean,
        lat: String?,
        long: String?
    ) = remote.dentaltempMoney(getToken()!!, limit, page, isPaid, lat, long)

    suspend fun updateFcmToken(fcmToken: String): ApiResponse<ApiResult<Nothing>> {
        val fcmTokenReq = FcmTokenReq(fcmToken)
        return if (getUserType()?.lowercase() == "clinic") {
            remote.updateClinicFcmToken(getToken()!!, fcmTokenReq)
        } else {
            remote.updateDentalTempFcmToken(getToken()!!, fcmTokenReq)
        }
    }

    suspend fun uploadClinicProfileImage(
        profileImage: MultipartBody.Part
    ) = remote.uploadClinicProfileImage(getToken()!!, profileImage)

    suspend fun shiftCalendar(
        from: String,
        to: String,
        lat: String?,
        long: String?,
    ) = remote.shiftCalendar(getToken()!!, from, to, lat, long)

    suspend fun sendOffer(
        offerReq: OfferReq
    ) = remote.sendOffer(getToken()!!, offerReq)

    suspend fun resubmittedOffer(
        offerIdReq: OfferIdReq
    ) = remote.resubmittedOffer(getToken()!!, offerIdReq)

    suspend fun cancelOffer(
        offerId: String?
    ) = remote.cancelOffer(getToken()!!, offerId)

    suspend fun markAsCompleteShift(
        shiftIdReq: ShiftIdReq
    ) = remote.markAsCompleteShift(getToken()!!, shiftIdReq)

    suspend fun clinicRate(
        clinicRateReq: ClinicRateReq
    ) = remote.clinicRate(getToken()!!, clinicRateReq)

    suspend fun shift(
        shiftIdReq: ShiftIdReq,
        lat: String? = null,
        long: String? = null
    ) = remote.shift(getToken()!!, shiftIdReq, lat, long)

    suspend fun requestShift(
        requestShiftReq: RequestShiftReq
    ) = remote.requestShift(getToken()!!, requestShiftReq)

    suspend fun searchClinic() = remote.searchClinic(getToken()!!)

    suspend fun confirmedShifts(
        limit: Int,
        page: Int,
        lat: String?,
        long: String?
    ) = remote.confirmedShifts(getToken()!!, limit, page, lat, long)

    suspend fun createInvoice(
        invoiceReq: CreateInvoiceReq
    ) = remote.createInvoice(getToken()!!, invoiceReq)

    suspend fun invoiceDetailsByShiftId(
        shiftId: String?
    ) = remote.invoiceDetailsByShiftId(getToken()!!, shiftId)

    suspend fun invoiceDetails(
        invoiceId: String?
    ) = remote.invoiceDetails(getToken()!!, invoiceId)

    suspend fun disputedInvoiceDetails(
        disputedInvoiceId: String?
    ) = remote.disputedInvoiceDetails(getToken()!!, disputedInvoiceId)

    suspend fun dentaltempHome() = remote.dentaltempHome(getToken()!!)

    suspend fun resendInvoice(
        invoiceIdReq: InvoiceIdReq
    ) = remote.resendInvoice(getToken()!!, invoiceIdReq)

    suspend fun emailMeInvoice(
        invoiceIdReq: InvoiceIdReq
    ) = remote.emailMeInvoice(getToken()!!, invoiceIdReq)

    suspend fun markAsPaidInvoice(
        shiftIdReq: ShiftIdReq
    ) = remote.markAsPaidInvoice(getToken()!!, shiftIdReq)

    suspend fun agreeDisputeInvoice(
        disputedInvoiceIdReq: DisputedInvoiceIdReq
    ) = remote.agreeDisputeInvoice(getToken()!!, disputedInvoiceIdReq)

    suspend fun disagreeDisputeInvoice(
        disputedInvoiceIdReq: DisputedInvoiceIdReq
    ) = remote.disagreeDisputeInvoice(getToken()!!, disputedInvoiceIdReq)

    suspend fun clinicInfo(
        clinicInfo: ClinicAccountInformation
    ) = remote.clinicInfo(getToken()!!, clinicInfo)

    suspend fun clinicDetailInfo(
        clinicDetailInfo: ClinicDetailInformation
    ) = remote.clinicDetailInfo(getToken()!!, clinicDetailInfo)

    suspend fun setupIntent() = remote.setupIntent(getToken()!!)

    suspend fun getClinicRatings(
        limit: Int,
        page: Int
    ) = remote.getClinicRatings(getToken()!!, limit, page)

    suspend fun toggleClinicNotification(
        activeNotif: ActiveNotificationReq
    ) = remote.toggleClinicNotification(getToken()!!, activeNotif)

    suspend fun uploadDoc(
        description: RequestBody?,
        doc: MultipartBody.Part? = null
    ) = remote.uploadDoc(getToken()!!, description, doc)

    suspend fun clinicNotification(
        limit: Int,
        page: Int
    ) = remote.clinicNotification(getToken()!!, limit, page)

    suspend fun clinicShiftCalendar(
        from: String,
        to: String
    ) = remote.clinicShiftCalendar(getToken()!!, from, to)

    suspend fun setting() = remote.setting()

    suspend fun postShift(
        shift: Shift
    ) = remote.postShift(getToken()!!, shift)

    suspend fun offer(
        shiftIdReq: ShiftIdReq,
        limit: Int = OFFERS_QUERY_PER_PAGE,
        page: Int = 0
    ) = remote.offer(getToken()!!, shiftIdReq, limit, page)

    suspend fun cancelShift(
        shiftId: String
    ) = remote.cancelShift(getToken()!!, shiftId)

    suspend fun acceptOffer(
        offerIdReq: OfferIdReq
    ) = remote.acceptOffer(getToken()!!, offerIdReq)

    suspend fun resubmissionOffer(
        offerIdReq: OfferIdReq
    ) = remote.resubmissionOffer(getToken()!!, offerIdReq)

    suspend fun rateDentalTemp(
        reviewReq: ReviewReq
    ) = remote.rateDentalTemp(getToken()!!, reviewReq)

    suspend fun getInvoices(
        limit: Int,
        page: Int,
        isPaid: Boolean? = null,
        userType: String? = null
    ) = remote.getInvoices(getToken()!!, limit, page, isPaid, userType)

    suspend fun payNowInvoice(
        invoiceId: String
    ) = remote.payNowInvoice(getToken()!!, invoiceId)

    suspend fun payManuallyInvoice(
        invoiceId: String
    ) = remote.payManuallyInvoice(getToken()!!, invoiceId)

    suspend fun getInvoiceDetail(
        invoiceId: String
    ) = remote.getInvoiceDetail(getToken()!!, invoiceId)

    suspend fun disputeInvoice(
        disputeInvoice: DisputeInvoiceReq
    ) = remote.disputeInvoice(getToken()!!, disputeInvoice)

    suspend fun homePendingShifts() = remote.homePendingShifts(getToken()!!)

    suspend fun homeConfirmedShifts() = remote.homeConfirmedShifts(getToken()!!)

    fun isLogin() = local.isLogin()
    fun setLogin(isLogin: Boolean) = local.setLogin(isLogin)

    fun getToken() = local.getToken()
    fun saveToken(token: String?) = local.saveToken(token)

    fun getUserType() = local.getUserType()
    fun saveUserType(
        userType: String
    ) = local.saveUserType(userType)

    fun getName() = local.getName()
    fun saveName(
        name: String
    ) = local.saveName(name)

    fun clearData() = local.clearData()

}