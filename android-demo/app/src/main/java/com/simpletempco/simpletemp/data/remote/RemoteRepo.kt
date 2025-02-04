package com.simpletempco.simpletemp.data.remote

import com.simpletempco.simpletemp.data.remote.models.request.*
import com.simpletempco.simpletemp.data.remote.models.response.*
import dagger.hilt.android.scopes.ActivityScoped
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ActivityScoped
class RemoteRepo @Inject constructor(private val api: SimpleTempApi) {

    suspend fun dentalTempLogin(
        loginReq: LoginReq
    ) = api.dentalTempLogin(loginReq)

    suspend fun clinicLogin(
        loginReq: LoginReq
    ) = api.clinicLogin(loginReq)

    suspend fun register(
        registerReq: RegisterReq
    ) = api.register(registerReq)

    suspend fun verifyCode(
        verifyCodeReq: VerifyCodeReq
    ) = api.verifyCode(verifyCodeReq)

    suspend fun sendCode(
        codeReq: CodeReq
    ) = api.sendCode(codeReq)

    suspend fun changePassword(
        changePasswordReq: ChangePasswordReq
    ) = api.changePassword(changePasswordReq)

    suspend fun dentalTempLogout(
        token: String?
    ) = api.dentalTempLogout(token)

    suspend fun clinicLogout(
        token: String?
    ) = api.clinicLogout(token)

    suspend fun checkUpdate(
        userType: String?
    ) = api.checkUpdate(userType)

    suspend fun dentalTempProfile(
        token: String?
    ) = api.dentalTempProfile(token)

    suspend fun dentalTempAccountInfo(
        token: String?,
        dentalTempInfo: DentalTempAccountInformation
    ) = api.dentalTempAccountInfo(token, dentalTempInfo)

    suspend fun dentalTempBankInfo(
        token: String?,
        bankInfo: DentalTempBankInformation
    ) = api.dentalTempBankInfo(token, bankInfo)

    suspend fun dentalTempPersonalInfo(
        token: String?,
        certNo: RequestBody?,
        graduationYear: RequestBody?,
        specialties: RequestBody?,
        dbs: MultipartBody.Part? = null,
        resume: MultipartBody.Part? = null
    ) = api.dentalTempPersonalInfo(token, certNo, graduationYear, specialties, dbs, resume)

    suspend fun uploadDentalTempProfileImage(
        token: String?,
        profileImage: MultipartBody.Part
    ) = api.uploadDentalTempProfileImage(token, profileImage)

    suspend fun faq(
        limit: Int,
        page: Int,
        userType: String?
    ) = api.faq(limit, page, userType)

    suspend fun ticket(token: String?) = api.ticket(token)

    suspend fun message(
        token: String?,
        ticketId: String?
    ) = api.message(token, ticketId)

    suspend fun sendMessage(
        token: String?,
        ticketId: String?,
        message: MessageReq
    ) = api.sendMessage(token, ticketId, message)

    suspend fun createTicket(
        token: String?,
        ticketReq: TicketReq
    ) = api.createTicket(token, ticketReq)

    suspend fun toggleNotification(
        token: String?,
        activeNotif: ActiveNotificationReq
    ) = api.toggleNotification(token, activeNotif)

    suspend fun getRating(
        token: String?,
        limit: Int,
        page: Int
    ) = api.getRating(token, limit, page)

    suspend fun notification(
        token: String?,
        limit: Int,
        page: Int
    ) = api.notification(token, limit, page)

    suspend fun jobHistory(
        token: String?,
        limit: Int,
        page: Int
    ) = api.jobHistory(token, limit, page)

    suspend fun dentaltempMoney(
        token: String?,
        limit: Int,
        page: Int,
        isPaid: Boolean,
        lat: String?,
        long: String?,
    ) = api.dentaltempMoney(token, limit, page, isPaid, lat, long)

    suspend fun updateDentalTempFcmToken(
        token: String?,
        fcmToken: FcmTokenReq
    ) = api.updateDentalTempFcmToken(token, fcmToken)

    suspend fun updateClinicFcmToken(
        token: String?,
        fcmToken: FcmTokenReq
    ) = api.updateClinicFcmToken(token, fcmToken)

    suspend fun shiftCalendar(
        token: String?,
        from: String,
        to: String,
        lat: String?,
        long: String?,
    ) = api.shiftCalendar(token, from, to, lat, long)

    suspend fun sendOffer(
        token: String?,
        offerReq: OfferReq
    ) = api.sendOffer(token, offerReq)

    suspend fun resubmittedOffer(
        token: String?,
        offerIdReq: OfferIdReq
    ) = api.resubmittedOffer(token, offerIdReq)

    suspend fun cancelOffer(
        token: String?,
        offerId: String?
    ) = api.cancelOffer(token, offerId)

    suspend fun markAsCompleteShift(
        token: String?,
        shiftIdReq: ShiftIdReq
    ) = api.markAsCompleteShift(token, shiftIdReq)

    suspend fun clinicRate(
        token: String?,
        clinicRateReq: ClinicRateReq
    ) = api.clinicRate(token, clinicRateReq)

    suspend fun shift(
        token: String?,
        shiftIdReq: ShiftIdReq,
        lat: String?,
        long: String?
    ) = api.shift(token, shiftIdReq, lat, long)

    suspend fun requestShift(
        token: String?,
        requestShiftReq: RequestShiftReq
    ) = api.requestShift(token, requestShiftReq)

    suspend fun searchClinic(
        token: String?
    ) = api.searchClinic(token)

    suspend fun confirmedShifts(
        token: String?,
        limit: Int,
        page: Int,
        lat: String?,
        long: String?
    ) = api.confirmedShifts(token, limit, page, lat, long)

    suspend fun createInvoice(
        token: String?,
        invoiceReq: CreateInvoiceReq
    ) = api.createInvoice(token, invoiceReq)

    suspend fun invoiceDetailsByShiftId(
        token: String?,
        shiftId: String?
    ) = api.invoiceDetailsByShiftId(token, shiftId)

    suspend fun invoiceDetails(
        token: String?,
        invoiceId: String?
    ) = api.invoiceDetails(token, invoiceId)

    suspend fun disputedInvoiceDetails(
        token: String?,
        disputedInvoiceId: String?
    ) = api.disputedInvoiceDetails(token, disputedInvoiceId)

    suspend fun dentaltempHome(
        token: String?
    ) = api.dentaltempHome(token)

    suspend fun resendInvoice(
        token: String?,
        invoiceIdReq: InvoiceIdReq
    ) = api.resendInvoice(token, invoiceIdReq)

    suspend fun emailMeInvoice(
        token: String?,
        invoiceIdReq: InvoiceIdReq
    ) = api.emailMeInvoice(token, invoiceIdReq)

    suspend fun markAsPaidInvoice(
        token: String?,
        shiftIdReq: ShiftIdReq
    ) = api.markAsPaidInvoice(token, shiftIdReq)

    suspend fun agreeDisputeInvoice(
        token: String?,
        disputedInvoiceIdReq: DisputedInvoiceIdReq
    ) = api.agreeDisputeInvoice(token, disputedInvoiceIdReq)

    suspend fun disagreeDisputeInvoice(
        token: String?,
        disputedInvoiceIdReq: DisputedInvoiceIdReq
    ) = api.disagreeDisputeInvoice(token, disputedInvoiceIdReq)

    suspend fun clinicProfile(
        token: String?
    ) = api.clinicProfile(token)

    suspend fun uploadClinicProfileImage(
        token: String?,
        profileImage: MultipartBody.Part
    ) = api.uploadClinicProfileImage(token, profileImage)

    suspend fun clinicInfo(
        token: String?,
        clinicInfo: ClinicAccountInformation
    ) = api.clinicInfo(token, clinicInfo)

    suspend fun clinicDetailInfo(
        token: String?,
        clinicDetailInfo: ClinicDetailInformation
    ) = api.clinicDetailInfo(token, clinicDetailInfo)

    suspend fun setupIntent(
        token: String?
    ) = api.setupIntent(token)

    suspend fun getClinicRatings(
        token: String?,
        limit: Int,
        page: Int
    ) = api.getClinicRatings(token, limit, page)

    suspend fun toggleClinicNotification(
        token: String?,
        activeNotif: ActiveNotificationReq
    ) = api.toggleClinicNotification(token, activeNotif)

    suspend fun clinicTicket(token: String?) = api.clinicTicket(token)

    suspend fun createClinicTicket(
        token: String?,
        ticketReq: TicketReq
    ) = api.createClinicTicket(token, ticketReq)

    suspend fun clinicMessage(
        token: String?,
        ticketId: String?
    ) = api.clinicMessage(token, ticketId)

    suspend fun sendClinicMessage(
        token: String?,
        ticketId: String?,
        message: MessageReq
    ) = api.sendClinicMessage(token, ticketId, message)

    suspend fun uploadDoc(
        token: String?,
        description: RequestBody?,
        doc: MultipartBody.Part? = null
    ) = api.uploadDoc(token, description, doc)

    suspend fun clinicNotification(
        token: String?,
        limit: Int,
        page: Int
    ) = api.clinicNotification(token, limit, page)

    suspend fun clinicShiftCalendar(
        token: String?,
        from: String,
        to: String
    ) = api.clinicShiftCalendar(token, from, to)

    suspend fun setting() = api.setting()

    suspend fun postShift(
        token: String?,
        shift: Shift
    ) = api.postShift(token, shift)

    suspend fun offer(
        token: String?,
        shiftIdReq: ShiftIdReq,
        limit: Int,
        page: Int
    ) = api.offer(token, shiftIdReq, limit, page)

    suspend fun cancelShift(
        token: String?,
        shiftId: String
    ) = api.cancelShift(token, shiftId)

    suspend fun acceptOffer(
        token: String?,
        offerIdReq: OfferIdReq
    ) = api.acceptOffer(token, offerIdReq)

    suspend fun resubmissionOffer(
        token: String?,
        offerIdReq: OfferIdReq
    ) = api.resubmissionOffer(token, offerIdReq)

    suspend fun rateDentalTemp(
        token: String?,
        reviewReq: ReviewReq
    ) = api.rateDentalTemp(token, reviewReq)

    suspend fun getInvoices(
        token: String?,
        limit: Int,
        page: Int,
        isPaid: Boolean?,
        userType: String?
    ) = api.getInvoices(token, limit, page, isPaid, userType)

    suspend fun payNowInvoice(
        token: String?,
        invoiceId: String
    ) = api.payNowInvoice(token, invoiceId)

    suspend fun payManuallyInvoice(
        token: String?,
        invoiceId: String
    ) = api.payManuallyInvoice(token, invoiceId)

    suspend fun getInvoiceDetail(
        token: String?,
        invoiceId: String
    ) = api.getInvoiceDetail(token, invoiceId)

    suspend fun disputeInvoice(
        token: String?,
        disputeInvoice: DisputeInvoiceReq
    ) = api.disputeInvoice(token, disputeInvoice)

    suspend fun homePendingShifts(
        token: String?,
    ) = api.homePendingShifts(token)

    suspend fun homeConfirmedShifts(
        token: String?,
    ) = api.homeConfirmedShifts(token)

}