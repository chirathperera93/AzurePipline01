package com.ayubo.life.ayubolife.prochat.data.sources.remote

import com.ayubo.life.ayubolife.ask.AskMainResponse
import com.ayubo.life.ayubolife.book_videocall.model.BookVideoCallActivityMainResponse
import com.ayubo.life.ayubolife.challenges.AdventureChallengeRouteMainResponse
import com.ayubo.life.ayubolife.challenges.ChallanheDataMainResponse
import com.ayubo.life.ayubolife.discover_search.DiscoverSearchMainResponse
import com.ayubo.life.ayubolife.home_group_view.GroupViewMainResponse
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.JanashakthiAnswer
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.JanashakthiMultiOptionAnswer
import com.ayubo.life.ayubolife.janashakthionboarding.data.source.reponses.*
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.MultiQuestionSubmitMainResponse
import com.ayubo.life.ayubolife.lifeplus.LifePlusProgramMainResponse
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.login.model.*
import com.ayubo.life.ayubolife.map_challange.model.roadJSONMainResponse
import com.ayubo.life.ayubolife.map_challenges.model.LeaderBoardMainResponse
import com.ayubo.life.ayubolife.map_challenges.treasureview.MainTreasureResponse
import com.ayubo.life.ayubolife.new_payment.model.*
import com.ayubo.life.ayubolife.payment.model.*
import com.ayubo.life.ayubolife.prochat.data.sources.remote.requests.RequestChatAll
import com.ayubo.life.ayubolife.prochat.data.sources.remote.responses.ResponseAddChat
import com.ayubo.life.ayubolife.prochat.data.sources.remote.responses.ResponseDeleteChat
import com.ayubo.life.ayubolife.prochat.data.sources.remote.responses.ResponseSearchPatient
import com.ayubo.life.ayubolife.prochat.data.sources.remote.responses.SimpleResponse
import com.ayubo.life.ayubolife.programs.data.api_source.ResponseProgramList
import com.ayubo.life.ayubolife.programs.settings.ProgramSettingsResponse
import com.ayubo.life.ayubolife.reports.getareview.GetAReviewMainResponse
import com.ayubo.life.ayubolife.reports.response.AddReportReviewMainResponse
import com.ayubo.life.ayubolife.reports.upload.ReportUploadResponse
import com.ayubo.life.ayubolife.revamp.v1.model.*
import com.ayubo.life.ayubolife.twilio.TwillioDataMainResponse
import com.ayubo.mobileemr.data.sources.remote.responses.ResponseAppointmentHistory
import com.ayubo.mobileemr.data.sources.remote.responses.ResponsePendingAppointmentCount
import com.ayubo.mobileemr.data.sources.remote.responses.ResponseSingleChat
import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {


    @GET("api.ayubo.life/public/api/v1/appointment_history")
    fun getAppointmentHistory(@Header("Authorization") authorization: String): Flowable<Response<ResponseAppointmentHistory>>


    @GET("api.ayubo.life/public/api/v1/pending_appointment_list")
    fun getPendingAppointments(@Header("Authorization") authorization: String): Flowable<Response<ResponseAppointmentHistory>>

    @GET("api.ayubo.life/public/api/v1/single_chat/{appointmentId}")
    fun getSingleChat(
        @Header("Authorization") authorization: String,
        @Path("appointmentId") appointmentId: String
    ): Flowable<Response<ResponseSingleChat>>

    @POST("api.ayubo.life/public/api/v1/chat/all")
    fun getChatAll(
        @Header("Authorization") authorization: String,
        @Body requestChatAll: RequestChatAll
    ): Flowable<Response<ResponseSingleChat>>

    @Multipart
    @POST("api.ayubo.life/public/api/v1/chat/post_chat")
    fun addChat(
        @Header("Authorization") authorization: String,
        @Query("contact_id") contact_id: String,
        @Part file: MultipartBody.Part,
        @Query("text") text: String,
        @Query("type") type: String
    ): Flowable<Response<ResponseAddChat>>

    @POST("api.ayubo.life/public/api/v1/chat/post_chat")
    fun addChat(
        @Header("Authorization") authorization: String,
        @Query("contact_id") contact_id: String,
        @Query("text") text: String,
        @Query("type") type: String
    ): Flowable<Response<ResponseAddChat>>

    @GET
    fun downloadFileWithDynamicUrlSync(@Url fileUrl: String): Flowable<ResponseBody>

    @POST("api.ayubo.life/public/api/v1/status_change")
    fun updateStatus(
        @Header("Authorization") authorization: String,
        @Query("appointment_id") appointment_id: String,
        @Query("action") action: String
    ): Flowable<Response<ResponseAddChat>>

    @GET("patients_of_doctor")
    fun getPatientOfDoctor(@Header("Authorization") authorization: String): Flowable<Response<ResponseSearchPatient>>

    @GET("appointments/{patientId}")
    fun getAppointmentsForPatient(
        @Header("Authorization") authorization: String,
        @Path("patientId") patientId: String
    ): Flowable<Response<ResponseAppointmentHistory>>


    @GET("pending_appointment_count")
    fun getPendingAppointmentCount(@Header("Authorization") authorization: String): Flowable<Response<ResponsePendingAppointmentCount>>

    @GET("v1/janashakthi_questionnaire")
    fun getJanashakthiQuestionnaire(@Header("Authorization") authorization: String): Flowable<Response<ResponseGetQuestionnaire>>


    @GET("api.ayubo.life/public/api/v1/janashakthi_questionnaire")
    fun getSeaShellsQuestionnaire(
        @Header("Authorization") authorization: String,
        @Query("group_id") group_id: String
    ): Flowable<Response<ResponseGetSeaShellQuestionnaire>>

    @POST("api.ayubo.life/public/api/v1/mood/post_mood_type")
    @FormUrlEncoded
    fun getMoodCalenderData(
        @Header("Authorization") authorization: String,
        @Field("mood_type") mood_type: String
    ): Flowable<Response<ResponseGetMoodData>>

    @POST("api.ayubo.life/public/api/v1/mood/feedback")
    @FormUrlEncoded
    fun setMoodResponse(
        @Header("Authorization") authorization: String,
        @Field("id") id: String,
        @Field("feedback") feedback: String
    ): Flowable<Response<ResponseSetMoodData>>

    @POST("api.ayubo.life/public/api/v1/janashakthi_submit_questionnaire")
    fun submitAnswers(
        @Header("Authorization") authorization: String,
        @Body answers: List<JanashakthiAnswer>
    ): Flowable<Response<SimpleResponse>>

    @POST("custom/service/v7/rest.php/")
    @FormUrlEncoded
    fun getAskData(
        @Header("app_id") app_id: String,
        @Field("method") method: String,
        @Field("input_type") JSON: String,
        @Field("response_type") JSON2: String,
        @Field("rest_data") jsonStr: String
    ): Flowable<Response<AskMainResponse>>


    @POST
    @FormUrlEncoded
    fun getDoctorListsData(
        @Url url: String,
        @Header("app_id") app_id: String,
        @Header("api_key") api_key: String,
        @Field("method") method: String,
        @Field("input_type") JSON: String,
        @Field("response_type") JSON2: String,
        @Field("rest_data") jsonStr: String
    ): Flowable<Response<BookVideoCallActivityMainResponse>>


    @POST("custom/service/v6/rest.php")
    @FormUrlEncoded
    fun getRoadJSON(
        @Header("app_id") app_id: String,
        @Field("method") method: String,
        @Field("input_type") JSON: String,
        @Field("response_type") JSON2: String,
        @Field("rest_data") jsonStr: String
    ): Flowable<Response<roadJSONMainResponse>>

    @POST("custom/service/v7/rest.php")
    @FormUrlEncoded
    fun getChallangeData(
        @Header("app_id") app_id: String,
        @Field("method") method: String,
        @Field("input_type") JSON: String,
        @Field("response_type") JSON2: String,
        @Field("rest_data") jsonStr: String
    ): Flowable<Response<ChallanheDataMainResponse>>


    @POST("custom/service/v7/rest.php")
    @FormUrlEncoded
    fun getAdventureChallengeRoutes(
        @Header("app_id") app_id: String,
        @Field("method") method: String,
        @Field("input_type") JSON: String,
        @Field("response_type") JSON2: String,
        @Field("rest_data") jsonStr: String
    ): Flowable<Response<AdventureChallengeRouteMainResponse>>


    @POST("api.ayubo.life/public/api/v2/janashakthi_submit_questionnaire_multi")
    fun submitAnswersMultiOptions(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Body answers: List<JanashakthiMultiOptionAnswer>
    ): Flowable<Response<MultiQuestionSubmitMainResponse>>

    @GET("api.ayubo.life/public/api/v1/janashakthi_expert_list")
    fun getExpertList(@Header("Authorization") authorization: String): Flowable<Response<ResponseExpertsList>>


    @GET("api.ayubo.life/public/api/ehr/v1/categorized_doctor_list")
    fun getExpertsForReview(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Query("filter") filter: String
    ): Flowable<Response<GetAReviewMainResponse>>

    @GET("api.ayubo.life/public/api/ehr/v1/categorized_doctor_list")
    fun getExpertsForReviewNew(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Query("filter") filter: String,
        @Query("doctor_id") doctor_id: String
    ): Flowable<Response<GetAReviewMainResponse>>


    @POST("v2/program_list")
    fun getLifePlusProgramsOld(@Header("Authorization") authorization: String): Flowable<Response<LifePlusProgramMainResponse>>

    @POST("api.ayubo.life/public/api/v1/store")
    fun getLifePlusPrograms(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String
    ): Flowable<Response<LifePlusProgramMainResponse>>

    @POST("api.ayubo.life/public/api/v1/store")
    @FormUrlEncoded
    fun getDiscoverSearchedResults(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("type") type: String
    ): Flowable<Response<LifePlusProgramMainResponse>>


    @POST("api.ayubo.life/public/api/v1/store_suggestions")
    @FormUrlEncoded
    fun getSearchResults(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Field("key_word") key_word: String
    ): Flowable<Response<DiscoverSearchMainResponse>>


    @Multipart
    @POST
    fun uploadUserReports(
        @Url url: String,
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Part("doctor_id") doctor_id: RequestBody,
        @Part("notes") notes: RequestBody,
        @Part("free_appointment") free_appointment: RequestBody,
        @Part("appointment_source") appointment_source: RequestBody,
        @Part report_image: List<MultipartBody.Part>,
        @Part report_file: List<MultipartBody.Part>
    ): Flowable<Response<ReportUploadResponse>>


    @POST("api.ayubo.life/public/api/v1/program_dashboard/content_list")
    @FormUrlEncoded
    fun getProgramsList(
        @Header("Authorization") authorization: String,
        @Field("policy_user_master_id") method: String
    ): Flowable<Response<ResponseProgramList>>

    @POST("api.ayubo.life/public/api/v1/report/all_records")
    @FormUrlEncoded
    fun getAllReports(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Field("patient_id") patient_id: String
    ): Flowable<Response<AddReportReviewMainResponse>>


    @POST("api.ayubo.life/public/api/v1/program_dashboard/header")
    @FormUrlEncoded
    fun getProgramsHeader(
        @Header("Authorization") authorization: String,
        @Field("policy_user_master_id") method: String
    ): Flowable<Response<ResponseProgramList>>

    @POST("api.ayubo.life/public/api/v1/payment/add_to_bill/add_mobile")
    @FormUrlEncoded
    fun sendDialogNumberToGetOTP(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Field("mobile_number") mobile_number: String,
        @Field("type") type: String
    ): Flowable<Response<DialogPinNumberResponse>>


    @POST("api.ayubo.life/public/api/v2/open_treasure")
    @FormUrlEncoded
    fun getTreasureData(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Field("challenge_id") challenge_id: String,
        @Field("treasure_id") treasure_id: String
    ): Flowable<Response<MainTreasureResponse>>


    @POST("api.ayubo.life/public/api/v1/payment/add_to_bill/add_verification")
    @FormUrlEncoded
    fun verifyDialogOTP(
        @Header("Authorization") authorization: String,
        @Field("otp") pin: String,
        @Field("referance_code") serverRef: String
    ): Flowable<Response<ResponseBody>>

    @POST("api.ayubo.life/public/api/rest.php/")
    @FormUrlEncoded
    fun getExpertData(
        @Url url: String,
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Field("method") method: String,
        @Field("input_type") input_type: String,
        @Field("response_type") response_type: String,
        @Field("rest_data") jsonStr: String
    ): Flowable<Response<DialogPinNumberResponse>>


    @POST("api.ayubo.life/public/api/")
    @FormUrlEncoded
    fun getTwillioData(
        @Url url: String,
        @Header("app_id") app_id: String,
        @Header("api_key") api_key: String,
        @Field("method") method: String,
        @Field("input_type") input_type: String,
        @Field("response_type") response_type: String,
        @Field("rest_data") rest_data: String
    ): Flowable<Response<TwillioDataMainResponse>>


    @POST("api.ayubo.life/public/api/v1/program_unsubscribe")
    @FormUrlEncoded
    fun unsubscribeProgram(
        @Header("Authorization") authorization: String,
        @Field("policy_user_master_id") pin: String
    ): Flowable<Response<Any>>


    @POST("api.ayubo.life/public/api/v1/payment/payment_options")
    fun sendPaymentOptions(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String
    ): Flowable<Response<PaymentSettingsResponse>>

    //    @POST("api.ayubo.life/public/api/v1/payment/payment_methods")
    @POST("api.ayubo.life/public/api/v2/payment/payment_methods")
    @FormUrlEncoded
    fun getPaymentMethods(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String,
        @Field("item_master_id") item_master_id: String
    ): Flowable<Response<PaymentMethodsResponse>>


    @POST("api.ayubo.life/public/api/v2/leaderboard")
    @FormUrlEncoded
    fun getLeaderBoard(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String,
        @Field("challenge_id") item_master_id: String
    ): Flowable<Response<LeaderBoardMainResponse>>


    @POST("api.ayubo.life/public/api/v1/program_exe_pause")
    @FormUrlEncoded
    fun setProgramPause(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String,
        @Field("policy_user_master_id") policy_user_master_id: String
    ): Flowable<Response<ProgramSettingsResponse>>

    @POST("api.ayubo.life/public/api/v1/program_exe_resume")
    @FormUrlEncoded
    fun setProgramResume(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String,
        @Field("policy_user_master_id") policy_user_master_id: String
    ): Flowable<Response<ProgramSettingsResponse>>


    @POST("api.ayubo.life/public/api/v1/program_exe_unsubscribe")
    @FormUrlEncoded
    fun setProgramUnsubscribe(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String,
        @Field("policy_user_master_id") policy_user_master_id: String
    ): Flowable<Response<ProgramSettingsResponse>>


    @POST("api.ayubo.life/public/api/v1/program_exe_restart")
    @FormUrlEncoded
    fun setProgramSettingsRestart(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String,
        @Field("policy_user_master_id") policy_user_master_id: String
    ): Flowable<Response<ProgramSettingsResponse>>


    @POST("api.ayubo.life/public/api/v2/user_profile")
    @FormUrlEncoded
    fun updateUserProfile(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("date_of_birth") date_of_birth: String,
        @Field("email") email: String,
        @Field("gender") gender: String,
        @Field("nic") nic: String
    ): Flowable<Response<ResponseBody>>


    @GET("api.ayubo.life/public/api/v2/user_profile")
    fun getProfile(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String
    ): Flowable<Response<UserProfileMainResponse>>


    //    @POST("api.ayubo.life/public/api/v1/payment/payment_confirmation")
    @POST("api.ayubo.life/public/api/v2/payment/payment_confirmation")
    @FormUrlEncoded
    fun getPaymentConfirmation(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String,
        @Field("item_price_master_id") item_price_master_id: Int,
        @Field("text") text: String,
        @Field("amount") amount: String,
        @Field("relate_type_id") relate_type_id: String,
        @Field("related_id") related_id: String,
        @Field("payment_source_id") payment_source_id: String,
        @Field("payment_frequency") payment_frequency: String,
        @Field("service_payment_frequency_source_id") payment_frequency_id: String,
        @Field("custom_param") custom_param: String,
        @Field("user_payment_method_id") user_payment_method_id: String
    ): Flowable<Response<PaymentConfirmMainResponseNew>>

    @POST("api.ayubo.life/public/api/v1/payment/other_payment_option")
    @FormUrlEncoded
    fun getOtherPaymentOptions(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String,
        @Field("service_payment_frequency_id") item_master_id: String
    ): Flowable<Response<OtherPaymentOptionsResponse>>

    @POST("api.ayubo.life/public/api/v1/store_group_items")
    @FormUrlEncoded
    fun getAllGroupItems(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String,
        @Field("group_id") group_id: String
    ): Flowable<Response<GroupViewMainResponse>>


//    @POST("")
//    fun sendPaymentOptions(@Url url: String,@Header("Authorization") authorization: String,
//                           @Header("app_id") user_id: String): Flowable<Response<PaymentOptionsResponse>>


//    @GET("get_report_master")
//    fun getJanashakthiReportType(@Url url: String,
//                                  @Header("Authorization") authorization: String): Flowable<Response<ResponseAppointmentHistory>>

    @GET()
    fun getJanashakthiReportTypes(
        @Url fileUrl: String,
        @Header("Authorization") authorization: String
    ): Flowable<Response<Any>>


    @POST("v1/get_report_master")
    @FormUrlEncoded
    fun getTimeLineData(
        @Url url: String,
        @Header("Authorization") authorization: String,
        @Field("method") method: String,
        @Field("input_type") input_type: String,
        @Field("response_type") response_type: String,
        @Field("rest_data") jsonStr: String
    ): Call<Any>


    @POST
    @FormUrlEncoded
    fun makeAPayment(
        @Url url: String,
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @FieldMap your_variable_name: Map<String, String>
    ): Flowable<Response<ApiPAayMainResponse>>


    @Multipart
    @POST("api.ayubo.life/public/api/")
    fun uploadMedicalData(
        @Url url: String,
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Header("applicationtoken") applicationtoken: String,
        @Part("userid") userid: RequestBody,
        @Part("relate_id") relate_id: RequestBody,
        @Part("height_feet") height_feet: RequestBody,
        @Part("height_inches") height_inches: RequestBody,
        @Part("weight") weight: RequestBody,
        @Part("sys") sys: RequestBody,
        @Part("dia") dia: RequestBody,
        @Part reportFiles: List<MultipartBody.Part>,
        @Part reportIds: List<MultipartBody.Part>,
        @Part reportDates: List<MultipartBody.Part>
    ): Flowable<Response<Any>>

    @POST("api.ayubo.life/public/api/v2/payment/pin")
    @FormUrlEncoded
    fun submitPin(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String,
        @Field("payment_id") payment_id: Int,
        @Field("pin") pin: Int
    ): Flowable<Response<SubmitPinResponse>>

    @GET("api.ayubo.life/public/api/v2/payment/summary")
    fun getPaymentSummary(
        @Header("Authorization") authorization: String,
        @Header("Authorization") app_id: String,
        @Query("payment_id") payment_id: Int
    ): Flowable<Response<PaymentSummaryResponse>>

    @POST("api.ayubo.life/public/api/v1/chat/delete")
    fun deleteSingleChat(
        @Header("Authorization") authorization: String,
        @Header("Authorization") app_id: String,
        @Query("conversation_ids") conversation_ids: String
    ): Flowable<Response<ResponseDeleteChat>>


    @GET("api.ayubo.life/public/api/v2/payment/summary")
    fun getUserPurchaseHistoryItems(
        @Header("Authorization") authorization: String,
        @Header("Authorization") app_id: String,
        @Query("payment_id") payment_id: Int
    ): Flowable<Response<PaymentSummaryResponse>>


    @GET("https://dev-ayubo-apim.azure-api.net/ayubo-discover/v2/get-discover")
    fun getAyuboDiscoverBannerData(
        @Header("app_id") app_id: String,
        @Header("Authorization") authorization: String
    ): Flowable<Response<ProfileDashboardResponseData>>


    @GET("https://dev-ayubo-apim.azure-api.net/dev-ayubo-users/v1/get-my-profile")
    fun getNewProfile(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String
    ): Flowable<Response<NewUserProfileMainResponse>>


    @PUT("https://dev-ayubo-apim.azure-api.net/dev-ayubo-users/v1/update-user")
    fun updateNewUserProfile(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String,
        @Body newUserProfileData: NewUserProfileData
    ): Flowable<Response<ResponseBody>>

    @GET("https://dev-ayubo-apim.azure-api.net/dev-ayubo-users/v1/get-my-profile")
    fun getNewProfileWhenLogin(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String
    ): Call<NewUserProfileMainResponse>

    @GET("https://dev-ayubo-apim.azure-api.net/dev-ayubo-users/v1/get-user-details")
    fun getNewProfileById(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Query("user_id") user_id: String
    ): Flowable<Response<NewUserProfileMainResponse>>

    @PUT("https://dev-ayubo-apim.azure-api.net/dev-ayubo-users/v1/update-user-image")
    fun updateUserImage(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Body newUserProfileData: NewUserProfileData
    ): Flowable<Response<NewUserProfileMainResponse>>


    @GET("https://dev-ayubo-apim.azure-api.net/ayubo-payments/v1/get-my-cards")
    fun getMyCards(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String
    ): Flowable<Response<NewGetMyCardsResponse>>

    @GET("https://dev-ayubo-apim.azure-api.net/ayubo-payments/v1/get-payment-details")
    fun getNewPaymentDetails(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Query("payment_id") payment_id: String
    ): Flowable<Response<NewPaymentDetailResponse>>


    @GET("https://dev-ayubo-apim.azure-api.net/ayubo-payments/v1/get-payment-methods")
    fun getNewPaymentMethods(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Query("payment_id") payment_id: String
    ): Flowable<Response<NewPaymentMethodResponse>>

    @POST("https://dev-ayubo-apim.azure-api.net/ayubo-payments/v1/charge")
    fun doChargePayment(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Body chargePaymentItem: ChargePaymentItem
    ): Flowable<Response<ChargePaymentResponse>>

    @POST("https://dev-ayubo-apim.azure-api.net/ayubo-payments/v1/create-payment")
    fun createNewPayment(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Body createPaymentItem: CreatePaymentItem
    ): Flowable<Response<NewPaymentDetailResponse>>

    @GET("https://dev-ayubo-apim.azure-api.net/dev-ayubo-dashboard/v3/get-widgets")
    fun getWidgets(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String
    ): Flowable<Response<GetWidgetsResponse>>

    @GET("https://dev-ayubo-apim.azure-api.net/dev-ayubo-dashboard/v3/update-widget-status")
    fun updateWidgetStatus(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Query("widget_id") widget_id: Int,
        @Query("activated_status") activated_status: String
    ): Flowable<Response<PostWidgetsResponse>>

    @GET("https://dev-ayubo-apim.azure-api.net/dev-ayubo-dashboard/v3/get-weight-summary")
    fun getWeightSummary(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String
    ): Flowable<Response<GetWeightSummaryResponse>>


    @GET("https://dev-ayubo-apim.azure-api.net/dev-ayubo-users/v3/get-cities")
    fun getAllCities(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String
    ): Flowable<Response<GetAllCitiesDataResponse>>

    @PUT("https://dev-ayubo-apim.azure-api.net/dev-ayubo-users/v1/update-city")
    fun updateCity(
        @Header("Authorization") authorization: String,
        @Header("app_id") user_id: String,
        @Body newUpdateCity: NewUpdateCity
    ): Flowable<Response<ResponseBody>>

    @GET("https://dev-ayubo-apim.azure-api.net/ayubo-campaign-config/v1/campaign-join-page")
    fun getCampaignDetails(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Query("campaign_id") activated_status: String
    ): Flowable<Response<GetCampaignDetailResponse>>

    @POST("https://dev-ayubo-apim.azure-api.net/ayubo-campaign-config/v1/campaign-join")
    fun joinCampaign(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Body joinCampaignRequestBody: JoinCampaignRequestBody
    ): Flowable<Response<JoinCampaignResponse>>

    @GET("https://dev-ayubo-apim.azure-api.net/ayubo-campaign-config/v1/get-all-rewards")
    fun getAllRewards(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String
    ): Flowable<Response<GetAllRewardsResponse>>

    @GET("https://dev-ayubo-apim.azure-api.net/ayubo-campaign-config/v1/redeem_reward")
    fun doRedeemRewards(
        @Header("Authorization") authorization: String,
        @Header("app_id") app_id: String,
        @Body doRedeemRewardBody: DoRedeemRewardBody
    ): Flowable<Response<DoRedeemRewardsResponse>>
}