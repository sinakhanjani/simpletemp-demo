package com.simpletempco.simpletemp.data.remote

import com.simpletempco.simpletemp.data.remote.models.request.*
import com.simpletempco.simpletemp.data.remote.models.response.*
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface SimpleTempApi {

    @POST("common/login")
    suspend fun dentalTempLogin(
        @Body loginReq: LoginReq
    ): ApiResponse<ApiResult<DentalTemp>>

    @POST("common/login")
    suspend fun clinicLogin(
        @Body loginReq: LoginReq
    ): ApiResponse<ApiResult<Clinic>>

    @POST("common/register")
    suspend fun register(
        @Body registerReq: RegisterReq
    ): ApiResponse<ApiResult<Nothing>>

    @POST("common/verifyCode")
    suspend fun verifyCode(
        @Body verifyCodeReq: VerifyCodeReq
    ): ApiResponse<ApiResult<DentalTemp>>

    @POST("common/sendcode")
    suspend fun sendCode(
        @Body codeReq: CodeReq
    ): ApiResponse<ApiResult<Nothing>>

    @POST("common/changepassword")
    suspend fun changePassword(
        @Body changePasswordReq: ChangePasswordReq
    ): ApiResponse<ApiResult<DentalTemp>>

    @POST("dentaltemp/profile/logout")
    suspend fun dentalTempLogout(
        @Header("Authorization") token: String?
    ): ApiResponse<ApiResult<Nothing>>

    @POST("clinic/profile/logout")
    suspend fun clinicLogout(
        @Header("Authorization") token: String?
    ): ApiResponse<ApiResult<Nothing>>

    @GET("common/version")
    suspend fun checkUpdate(
        @Query("userType") userType: String?
    ): ApiResponse<ApiResult<List<Version>>>

    @GET("dentaltemp/profile/me")
    suspend fun dentalTempProfile(
        @Header("Authorization") token: String?
    ): ApiResponse<ApiResult<DentalTemp>>

    @POST("dentaltemp/profile/accountinfo")
    suspend fun dentalTempAccountInfo(
        @Header("Authorization") token: String?,
        @Body accountInfo: DentalTempAccountInformation
    ): ApiResponse<ApiResult<DentalTemp>>

    @POST("dentaltemp/profile/bankinfo")
    suspend fun dentalTempBankInfo(
        @Header("Authorization") token: String?,
        @Body accountInfo: DentalTempBankInformation
    ): ApiResponse<ApiResult<DentalTemp>>

    @Multipart
    @POST("dentaltemp/profile/personalinfo")
    suspend fun dentalTempPersonalInfo(
        @Header("Authorization") token: String?,
        @Part("certificationNumber") certNo: RequestBody?,
        @Part("graduationYear") graduationYear: RequestBody?,
        @Part("specialties") specialties: RequestBody?,
        @Part dbs: MultipartBody.Part?,
        @Part resume: MultipartBody.Part?
    ): ApiResponse<ApiResult<DentalTemp>>

    @Multipart
    @POST("dentaltemp/uploadphoto")
    suspend fun uploadDentalTempProfileImage(
        @Header("Authorization") token: String?,
        @Part profile_photo: MultipartBody.Part
    ): ApiResponse<ApiResult<DentalTemp>>

    @GET("common/faq")
    suspend fun faq(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("userType") userType: String?
    ): ApiResponse<ApiResult<List<FaqItem>>>

    @GET("dentaltemp/ticket")
    suspend fun ticket(
        @Header("Authorization") token: String?
    ): ApiResponse<ApiResult<List<Ticket>>>

    @GET("dentaltemp/message/{ticketId}")
    suspend fun message(
        @Header("Authorization") token: String?,
        @Path("ticketId") ticketId: String?
    ): ApiResponse<ApiResult<List<Message>>>

    @POST("dentaltemp/message/send/{ticketId}")
    suspend fun sendMessage(
        @Header("Authorization") token: String?,
        @Path("ticketId") ticketId: String?,
        @Body messageReq: MessageReq?
    ): ApiResponse<ApiResult<Message?>>

    @POST("dentaltemp/ticket/create")
    suspend fun createTicket(
        @Header("Authorization") token: String?,
        @Body tickReq: TicketReq?
    ): ApiResponse<ApiResult<Ticket?>>

    @POST("dentaltemp/profile/togglenotification")
    suspend fun toggleNotification(
        @Header("Authorization") token: String?,
        @Body activeNotificationReq: ActiveNotificationReq?
    ): ApiResponse<ApiResult<Nothing>>

    @GET("dentaltemp/rate")
    suspend fun getRating(
        @Header("Authorization") token: String?,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): ApiResponse<ApiResult<Rating>>

    @GET("dentaltemp/notification")
    suspend fun notification(
        @Header("Authorization") token: String?,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): ApiResponse<ApiResult<List<Notif>>>

    @GET("dentaltemp/shift/jobhistory")
    suspend fun jobHistory(
        @Header("Authorization") token: String?,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): ApiResponse<ApiResult<JobHistory>>

    @GET("dentaltemp/shift/money")
    suspend fun dentaltempMoney(
        @Header("Authorization") token: String?,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("isPaid") isPaid: Boolean,
        @Query("lat") lat: String?,
        @Query("long") long: String?
    ): ApiResponse<ApiResult<Money>>

    @POST("dentaltemp/profile/updatefcmtoken")
    suspend fun updateDentalTempFcmToken(
        @Header("Authorization") token: String?,
        @Body fcmTokenReq: FcmTokenReq?
    ): ApiResponse<ApiResult<Nothing>>

    @GET("dentaltemp/shift/calendar")
    suspend fun shiftCalendar(
        @Header("Authorization") token: String?,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("lat") lat: String?,
        @Query("long") long: String?
    ): ApiResponse<ApiResult<List<Calendar>>>

    @POST("dentaltemp/offer")
    suspend fun sendOffer(
        @Header("Authorization") token: String?,
        @Body offerReq: OfferReq
    ): ApiResponse<ApiResult<Offer?>>

    @POST("dentaltemp/offer/resubmitted")
    suspend fun resubmittedOffer(
        @Header("Authorization") token: String?,
        @Body offerIdReq: OfferIdReq
    ): ApiResponse<ApiResult<Nothing>>

    @POST("dentaltemp/offer/delete/{offerId}")
    suspend fun cancelOffer(
        @Header("Authorization") token: String?,
        @Path("offerId") offerId: String?
    ): ApiResponse<ApiResult<Nothing>>

    @POST("dentaltemp/shift/markascomplete")
    suspend fun markAsCompleteShift(
        @Header("Authorization") token: String?,
        @Body shiftIdReq: ShiftIdReq
    ): ApiResponse<ApiResult<Nothing>>

    @POST("dentaltemp/rate/clinic")
    suspend fun clinicRate(
        @Header("Authorization") token: String?,
        @Body clinicRateReq: ClinicRateReq
    ): ApiResponse<ApiResult<Nothing>>

    @POST("dentaltemp/shift")
    suspend fun shift(
        @Header("Authorization") token: String?,
        @Body shiftIdReq: ShiftIdReq,
        @Query("lat") lat: String?,
        @Query("long") long: String?
    ): ApiResponse<ApiResult<Shift?>>

    @POST("dentaltemp/shift/requestshift")
    suspend fun requestShift(
        @Header("Authorization") token: String?,
        @Body postShiftReq: RequestShiftReq
    ): ApiResponse<ApiResult<Nothing?>>

    @GET("dentaltemp/shift/clinic/search")
    suspend fun searchClinic(
        @Header("Authorization") token: String?
    ): ApiResponse<ApiResult<List<Clinic>?>>

    @GET("dentaltemp/shift/confirmed")
    suspend fun confirmedShifts(
        @Header("Authorization") token: String?,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("lat") lat: String?,
        @Query("long") long: String?
    ): ApiResponse<ApiResult<List<Shift>>>

    @POST("dentaltemp/invoice/create")
    suspend fun createInvoice(
        @Header("Authorization") token: String?,
        @Body invoiceReq: CreateInvoiceReq
    ): ApiResponse<ApiResult<Nothing>>

    @POST("dentaltemp/invoice/resend")
    suspend fun resendInvoice(
        @Header("Authorization") token: String?,
        @Body invoiceIdReq: InvoiceIdReq
    ): ApiResponse<ApiResult<Nothing>>

    @POST("dentaltemp/invoice/emailme")
    suspend fun emailMeInvoice(
        @Header("Authorization") token: String?,
        @Body invoiceIdReq: InvoiceIdReq
    ): ApiResponse<ApiResult<Nothing>>

    @POST("dentaltemp/shift/markaspaid")
    suspend fun markAsPaidInvoice(
        @Header("Authorization") token: String?,
        @Body shiftIdReq: ShiftIdReq
    ): ApiResponse<ApiResult<Nothing>>

    @POST("dentaltemp/invoice/agreedispute")
    suspend fun agreeDisputeInvoice(
        @Header("Authorization") token: String?,
        @Body disputedInvoiceIdReq: DisputedInvoiceIdReq
    ): ApiResponse<ApiResult<Nothing>>

    @POST("dentaltemp/invoice/disagreedispute")
    suspend fun disagreeDisputeInvoice(
        @Header("Authorization") token: String?,
        @Body disputedInvoiceIdReq: DisputedInvoiceIdReq
    ): ApiResponse<ApiResult<Nothing>>

    @GET("dentaltemp/invoice/shift/{shiftId}")
    suspend fun invoiceDetailsByShiftId(
        @Header("Authorization") token: String?,
        @Path("shiftId") shiftId: String?
    ): ApiResponse<ApiResult<InvoiceDetail>>

    @GET("dentaltemp/invoice/findone/{invoiceId}")
    suspend fun invoiceDetails(
        @Header("Authorization") token: String?,
        @Path("invoiceId") invoiceId: String?
    ): ApiResponse<ApiResult<InvoiceDetail>>

    @GET("dentaltemp/disputedinvoice/findone/{disputedInvoiceId}")
    suspend fun disputedInvoiceDetails(
        @Header("Authorization") token: String?,
        @Path("disputedInvoiceId") disputedInvoiceId: String?
    ): ApiResponse<ApiResult<InvoiceDetail>>

    @GET("dentaltemp/shift/home")
    suspend fun dentaltempHome(
        @Header("Authorization") token: String?
    ): ApiResponse<ApiResult<DentaltempHome>>

    @POST("clinic/profile/updatefcmtoken")
    suspend fun updateClinicFcmToken(
        @Header("Authorization") token: String?,
        @Body fcmTokenReq: FcmTokenReq?
    ): ApiResponse<ApiResult<Nothing>>

    @GET("clinic/profile/me")
    suspend fun clinicProfile(
        @Header("Authorization") token: String?
    ): ApiResponse<ApiResult<Clinic>>

    @Multipart
    @POST("clinic/profile/uploadphoto")
    suspend fun uploadClinicProfileImage(
        @Header("Authorization") token: String?,
        @Part profile_photo: MultipartBody.Part
    ): ApiResponse<ApiResult<Clinic>>

    @POST("clinic/profile/accountinfo")
    suspend fun clinicInfo(
        @Header("Authorization") token: String?,
        @Body clinicInfo: ClinicAccountInformation
    ): ApiResponse<ApiResult<Clinic>>

    @POST("clinic/profile/detailinfo")
    suspend fun clinicDetailInfo(
        @Header("Authorization") token: String?,
        @Body clinicDetailInfo: ClinicDetailInformation
    ): ApiResponse<ApiResult<Clinic>>

    @POST("clinic/profile/setupintent")
    suspend fun setupIntent(
        @Header("Authorization") token: String?
    ): ApiResponse<ApiResult<IntentRes>>

    @GET("clinic/rate")
    suspend fun getClinicRatings(
        @Header("Authorization") token: String?,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): ApiResponse<ApiResult<Rating>>

    @POST("clinic/profile/togglenotification")
    suspend fun toggleClinicNotification(
        @Header("Authorization") token: String?,
        @Body activeNotificationReq: ActiveNotificationReq?
    ): ApiResponse<ApiResult<Nothing>>

    @GET("clinic/ticket")
    suspend fun clinicTicket(
        @Header("Authorization") token: String?
    ): ApiResponse<ApiResult<List<Ticket>>>

    @POST("clinic/ticket/create")
    suspend fun createClinicTicket(
        @Header("Authorization") token: String?,
        @Body tickReq: TicketReq?
    ): ApiResponse<ApiResult<Ticket?>>

    @GET("clinic/message/{ticketId}")
    suspend fun clinicMessage(
        @Header("Authorization") token: String?,
        @Path("ticketId") ticketId: String?
    ): ApiResponse<ApiResult<List<Message>>>

    @POST("clinic/message/send/{ticketId}")
    suspend fun sendClinicMessage(
        @Header("Authorization") token: String?,
        @Path("ticketId") ticketId: String?,
        @Body messageReq: MessageReq?
    ): ApiResponse<ApiResult<Message?>>

    @Multipart
    @POST("clinic/profile/uploaddoc")
    suspend fun uploadDoc(
        @Header("Authorization") token: String?,
        @Part("description") certNo: RequestBody?,
        @Part doc: MultipartBody.Part?
    ): ApiResponse<ApiResult<Clinic>>

    @GET("clinic/notification")
    suspend fun clinicNotification(
        @Header("Authorization") token: String?,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): ApiResponse<ApiResult<List<Notif>>>

    @GET("clinic/shift/calendar")
    suspend fun clinicShiftCalendar(
        @Header("Authorization") token: String?,
        @Query("from") from: String,
        @Query("to") to: String
    ): ApiResponse<ApiResult<List<Calendar>>>

    @GET("common/setting")
    suspend fun setting(): ApiResponse<ApiResult<HourlyPrices>>

    @POST("clinic/shift")
    suspend fun postShift(
        @Header("Authorization") token: String?,
        @Body shift: Shift
    ): ApiResponse<ApiResult<Nothing>>

    @POST("clinic/offer")
    suspend fun offer(
        @Header("Authorization") token: String?,
        @Body shiftIdReq: ShiftIdReq,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): ApiResponse<ApiResult<List<Offer>?>>

    @POST("clinic/shift/delete/{shiftId}")
    suspend fun cancelShift(
        @Header("Authorization") token: String?,
        @Path("shiftId") shiftId: String
    ): ApiResponse<ApiResult<Nothing>>

    @POST("clinic/offer/accept")
    suspend fun acceptOffer(
        @Header("Authorization") token: String?,
        @Body offerIdReq: OfferIdReq
    ): ApiResponse<ApiResult<Nothing>>

    @POST("clinic/offer/resubmission")
    suspend fun resubmissionOffer(
        @Header("Authorization") token: String?,
        @Body offerIdReq: OfferIdReq
    ): ApiResponse<ApiResult<Nothing>>

    @GET("clinic/invoice/read")
    suspend fun getInvoices(
        @Header("Authorization") token: String?,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("isPaid") isPaid: Boolean?,
        @Query("userType") userType: String?
    ): ApiResponse<ApiResult<Invoices>>

    @POST("clinic/rate/dentaltemp")
    suspend fun rateDentalTemp(
        @Header("Authorization") token: String?,
        @Body reviewReq: ReviewReq
    ): ApiResponse<ApiResult<Nothing>>

    @POST("clinic/invoice/pay/{invoiceId}")
    suspend fun payNowInvoice(
        @Header("Authorization") token: String?,
        @Path("invoiceId") shiftId: String
    ): ApiResponse<ApiResult<Nothing>>

    @POST("clinic/invoice/paymanually/{invoiceId}")
    suspend fun payManuallyInvoice(
        @Header("Authorization") token: String?,
        @Path("invoiceId") shiftId: String
    ): ApiResponse<ApiResult<Nothing>>

    @GET("clinic/invoice/findone/{invoiceId}")
    suspend fun getInvoiceDetail(
        @Header("Authorization") token: String?,
        @Path("invoiceId") shiftId: String
    ): ApiResponse<ApiResult<Invoice>>

    @POST("clinic/invoice/dispute")
    suspend fun disputeInvoice(
        @Header("Authorization") token: String?,
        @Body disputeInvoice: DisputeInvoiceReq
    ): ApiResponse<ApiResult<Nothing>>

    @GET("clinic/shift/home/pendingshifts")
    suspend fun homePendingShifts(
        @Header("Authorization") token: String?
    ): ApiResponse<ApiResult<List<HomeShiffts>>>

    @GET("clinic/shift/home/confirmedshifts")
    suspend fun homeConfirmedShifts(
        @Header("Authorization") token: String?
    ): ApiResponse<ApiResult<List<HomeShiffts>>>

}