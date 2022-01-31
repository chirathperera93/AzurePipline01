package com.ayubo.life.ayubolife.rest;


import com.ayubo.life.ayubolife.body.WorkoutMainResponse;
import com.ayubo.life.ayubolife.channeling.activity.VideoSessionResponseData;
import com.ayubo.life.ayubolife.goals_extention.models.MainResponse_History;
import com.ayubo.life.ayubolife.goals_extention.models.dash_category.CategoryListMainResponse;
import com.ayubo.life.ayubolife.goals_extention.models.dashboard.DashboardMainResponse;
import com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails.DashDetailsMainResponse;
import com.ayubo.life.ayubolife.health.OMAddress;
import com.ayubo.life.ayubolife.health.OMUpdateOrder;
import com.ayubo.life.ayubolife.health.OrderMedicineCreateObject;
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.ReportTypesMainResponse;
import com.ayubo.life.ayubolife.lifeplus.FeedBackResponse;
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.CreateDashboardLogObj;
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData;
import com.ayubo.life.ayubolife.lifeplus.NewToDo.RequestToDoAction;
import com.ayubo.life.ayubolife.login.model.GetAllCitiesDataResponse;
import com.ayubo.life.ayubolife.login.model.GetDefaultLanguageMainResponse;
import com.ayubo.life.ayubolife.login.model.MembershipCardResponse;
import com.ayubo.life.ayubolife.login.model.NewUpdateCity;
import com.ayubo.life.ayubolife.login.model.RegisterMainResponse;
import com.ayubo.life.ayubolife.login.model.SendVerificationObject;
import com.ayubo.life.ayubolife.map_challenges.model.LeaderboardMainResponse;
import com.ayubo.life.ayubolife.masterapiconfig.DevAPIConfig;
import com.ayubo.life.ayubolife.model.JoinChallengeObj;
import com.ayubo.life.ayubolife.model.PayHeraMainResponse;
import com.ayubo.life.ayubolife.notification.model.NotiCountMainResponse;
import com.ayubo.life.ayubolife.notification.model.NotificationsMainResponse;
import com.ayubo.life.ayubolife.pojo.TokenMainResponse;
import com.ayubo.life.ayubolife.pojo.doctor_search.MainResponse;
import com.ayubo.life.ayubolife.pojo.goals.addNewGoal.AddAGoalResult;
import com.ayubo.life.ayubolife.post.model.ConciergeMainResponse;
import com.ayubo.life.ayubolife.programs.data.model.NewDashboardMainResponse;
import com.ayubo.life.ayubolife.reports.activity.ReportByIdResponse;
import com.ayubo.life.ayubolife.reports.model.AllRecordsMainResponse;
import com.ayubo.life.ayubolife.reports.model.AssignUserMainResponse;
import com.ayubo.life.ayubolife.reports.model.ChartDataMainResponse;
import com.ayubo.life.ayubolife.reports.model.CreateNewMember;
import com.ayubo.life.ayubolife.reports.model.DeleteMemberMainResponse;
import com.ayubo.life.ayubolife.reports.model.DeleteReportObject;
import com.ayubo.life.ayubolife.reports.model.DeleteReportResponse;
import com.ayubo.life.ayubolife.reports.model.FamilyMemberMainResponse;
import com.ayubo.life.ayubolife.retro_models.retro_mobilelogin_model;
import com.ayubo.life.ayubolife.revamp.v1.model.GetV1DashboardItemCloseResponse;
import com.ayubo.life.ayubolife.revamp.v1.model.GetV1DashboardResponse;
import com.ayubo.life.ayubolife.revamp.v1.model.GetYTDSummaryResponse;
import com.ayubo.life.ayubolife.revamp.v1.model.UpdateCityResponse;
import com.ayubo.life.ayubolife.revamp.v1.model.V1GetLeaderboardResponse;
import com.ayubo.life.ayubolife.revamp.v1.model.V1GetStepSummaryResponse;
import com.ayubo.life.ayubolife.timeline.models.PopupMainResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface ApiInterface {
    @GET("api/RetrofitAndroidImageResponses")
    Call<ResponseBody> getImageDetailsOld(
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST
    @FormUrlEncoded
    Call<ConciergeMainResponse> getConciergeCallBack(
            @Url String url,
            @Header("app_id") String app_id,
            @Header("api_key") String aoi_ky,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("native_post_template")
    @FormUrlEncoded
    Call<ResponseBody> getNativePost(
            @Header("app_id") String app_id,
            @Header("Authorization") String api_key,
            @Field("content_id") String id);

    @POST("getMedicalExpertsNew")
    @FormUrlEncoded
    Call<MainResponse> getDoctors(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("getLifeStyleExpertsPhysioTherapy")
    @FormUrlEncoded
    Call<MainResponse> getPhysio(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("pickGoal")
    @FormUrlEncoded
    Call<Object> getTimeLineData(
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("pickGoal")
    @FormUrlEncoded
    Call<Object> pickAGoal(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("adventure_challenge_info")
    @FormUrlEncoded
    Call<JoinChallengeObj> getJoinChallenge(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );
//   @GET("content_list")
//   fun getProgramsList(@Url url: String,
//                       @Header("Authorization") authorization: String): Flowable<Response<ResponseProgramsList.ResponseProgramList>>


    @POST("homePageTiles")
    @FormUrlEncoded
    Call<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> callMainService(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("homePageTiles")
    @FormUrlEncoded
    Call<com.ayubo.life.ayubolife.pojo.timeline.main.MainResponse> callFilteredMainService(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );


    @GET("posts?")
    Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> getAllPost(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("page") int page_number,
            @Query("offset") int offset,
            @Query("main_timeline") boolean main_timeline,
            @Query("max_post_id") String post_id);


    @GET("posts?")
    Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> getFilteredTimeline(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("page") int page_number,
            @Query("offset") int offset,
            @Query("main_timeline") boolean main_timeline,
            @Query("date") String date,
            @Query("max_post_id") String post_id);


    @GET("posts?")
    Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> getTimelineSinglePost(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("created_by_id") String var,
            @Query("page") int page_number,
            @Query("offset") int offset,
            @Query("max_post_id") String post_id);

    @GET("post/{post_id}")
    Call<com.ayubo.life.ayubolife.pojo.timeline.single_post.MainResponse> getSinglePost(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Path("post_id") Integer var);


    @GET("posts?")
    Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> getTimelineProgramPost(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("related_primary_id") String var,
            @Query("page") int page_number,
            @Query("offset") int offset,
            @Query("max_post_id") String post_id);


    @POST("chart_and_table_data")
    @FormUrlEncoded
    Call<ChartDataMainResponse> getReportsChartData(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("patient_id") String patient_id,
            @Field("enc_id") String enc_id,
            @Field("report_type") String report_type);

    @POST("all_records")
    @FormUrlEncoded
    Call<AllRecordsMainResponse> getAllReports(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("patient_id") String patient_id);

    @GET("get-all")
    Call<AllRecordsMainResponse> getAllReportsNew(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization);


    @POST("assign_to_user")
    @FormUrlEncoded
    Call<AssignUserMainResponse> assignAReportsToUser(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("patient_id") String patient_id,
            @Field("hos_uid") String hos_uid);

    @POST("member/{patient_id}/delete")
    Call<DeleteMemberMainResponse> deleteFamilyMember(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Path("patient_id") String patient_id);


    @POST("delete")
    @FormUrlEncoded
    Call<Object> deletedMemberReports(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("enc_id") String enc_id,
            @Field("table_id") String table_id,
            @Field("report_type") String report_type);

    @POST("/delete-record")
    Call<DeleteReportResponse> deletedMemberReportsNew(
            @Header("app_id") String app_id,
            @Header("Authorization") String Authorization,
            @Body DeleteReportObject deleteReportObject);


    @POST("read_report")
    @FormUrlEncoded
    Call<Object> getReadReports(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("enc_id") String enc_id,
            @Field("table_id") String table_id,
            @Field("report_type") String report_type);

    @POST("read-record")
    Call<DeleteReportResponse> getReadReportsNew(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Body DeleteReportObject deleteReportObject);

    @GET("member/all")
    Call<FamilyMemberMainResponse> getAllFamilyMembersOld(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization
    );

    @POST("notification/read")
    @FormUrlEncoded
    Call<Object> setReadNotification(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("id") String id);

    @GET("notification/get_notifications")
    Call<NotificationsMainResponse> getAllNotifications(
            @Header("Authorization") String app_id
    );

    @GET("notification/count")
    Call<NotiCountMainResponse> getNotificationCount(
            @Header("Authorization") String app_id
    );

    @POST("pay")
    @FormUrlEncoded
    Call<PayHeraMainResponse> setPayLink(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("service") String service);

    @POST("initiate_trial")
    @FormUrlEncoded
    Call<PayHeraMainResponse> getTrailRun(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("service") String service);


    @Multipart
    @POST("member/new")
    Call<CreateNewMember> upload(
            @Header("app_id") String app_id,
            @Part("name") String name,
            @Part("relationship") String relationship,
            @Part("date_of_birth") String date_of_birth,
            @Part("mobile") String mobile,
            @Part MultipartBody.Part file
    );

    @GET("post/{post_id}")
    Call<com.ayubo.life.ayubolife.pojo.timeline.single_post.MainResponse> getLeaderboard(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,

            @Path("post_id") Integer var);


    @GET("post/{post_id}/comments")
    Call<com.ayubo.life.ayubolife.pojo.timeline.Comment> getAllComment(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Path("post_id") String post_id);


    @GET("post/{post_id}/liked_users")
    Call<com.ayubo.life.ayubolife.pojo.timeline.likedUsers.LikedUsersMainResponse> getLikedUsers(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Path("post_id") String post_id);

    @GET("average_category_list")
    Call<CategoryListMainResponse> getDashboardCategorys(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization
    );


    @POST("dashboard_details")
    @FormUrlEncoded
    Call<DashDetailsMainResponse> getWellnessDashboardDetails(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("duration") String date,
            @Field("average_category_id") String average_category_id);


    @POST("challenge_native_leaderboard")
    @FormUrlEncoded
    Call<LeaderboardMainResponse> goalLeaderboard(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );


    @GET("popup/get_popups")
    Call<PopupMainResponse> getPopupData(
            @Header("Authorization") String authorization
    );

    @POST("program_dashboard/read")
    @FormUrlEncoded
    Call<Object> setReadPrograms(
            @Header("Authorization") String authorization,
            @Field("id") String method
    );


    @POST("popup/read")
    @FormUrlEncoded
    Call<Object> setReadPopup(
            @Header("Authorization") String authorization,
            @Field("id") String method
    );


    @GET("goal/steps_history")
    Call<MainResponse_History> getDashboardStepHistory(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization
    );

    @POST("dashboard")
    @FormUrlEncoded
    Call<DashboardMainResponse> getWellnessDashboard(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("date") String date,
            @Field("average_category_id") String average_category_id);


    @PUT("post/{post_id}/like")
    @FormUrlEncoded
    Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> LikeAPost(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,

            @Path("post_id") String var,
            @Field("like") int like);

    @POST("auth/login")
    @FormUrlEncoded
    Call<TokenMainResponse> getToken(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("user_id") String user_id);


    @DELETE("post/{post_id}/delete")
    Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> DeleteAPost(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,

            @Path("post_id") String var);


    @DELETE("post/{post_id}/comment/{comment_id}/delete")
    Call<Object> DeleteAComment(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,

            @Path("post_id") String post_id,
            @Path("comment_id") String comment_id);

    @Multipart
    @POST("video_post")
    Call<Object> makeVideoPost(@Header("Authorization") String authorization,
                               @Part("heading") RequestBody file,
                               @Part("body") RequestBody body,
                               @Part("community_ids") RequestBody community_ids,
                               @Part("video") RequestBody video,
                               @Part("thumbnail") RequestBody thumbnail);


    @POST("achieveGoal")
    @FormUrlEncoded
    Call<com.ayubo.life.ayubolife.pojo.goals.achieveGoal.MainResponse> achieveGoal(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("janashakthi_proceed_with_questionnaire")
    @FormUrlEncoded
    Call<com.ayubo.life.ayubolife.lifepoints.MainResponse> getJanashakthiAccept(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("getLifePoints")
    @FormUrlEncoded
    Call<com.ayubo.life.ayubolife.lifepoints.MainResponse> getLifePoints(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("get_report_master")
    @FormUrlEncoded
    Call<ReportTypesMainResponse> getReportMaster(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("janashakthi_proceed_with_questionnaire")
    @FormUrlEncoded
    Call<Object> proceedWithQuestions(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("user_id") String user_id,
            @Field("policy_user_master_id") String policy_user_master_id
    );

    @POST("viewGoals")
    @FormUrlEncoded
    Call<com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse> viewGoals(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("goalDelete")
    @FormUrlEncoded
    Call<com.ayubo.life.ayubolife.pojo.goals.deleteGoal.MainResponse> goalDelete(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("addGoal")
    @FormUrlEncoded
    Call<com.ayubo.life.ayubolife.pojo.goals.addGoal.MainResponse> goalAdd(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("editGoal")
    @FormUrlEncoded
    Call<com.ayubo.life.ayubolife.pojo.goals.editGoals.MainResponse> editGoal(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("getGoalImages")
    @FormUrlEncoded
    Call<com.ayubo.life.ayubolife.pojo.goalCategory.MainResponse> getGoalCategories(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );


    @POST("getGoalImages")
    @FormUrlEncoded
    Call<AddAGoalResult> addAGoalResult(
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("getGoalImages")
    @FormUrlEncoded
    Call<com.ayubo.life.ayubolife.pojo.goals.MainResponse> getGoalImages(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("v2/default_languages")
    @FormUrlEncoded
    Call<GetDefaultLanguageMainResponse> getDefaultLanguage(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("button_id") String method
    );

    @POST("v2/default_language_select")
    @FormUrlEncoded
    Call<GetDefaultLanguageMainResponse> setDefaultLanguage(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("lang_id") String lang_id
    );


    @POST("chat/button_pressed")
    @FormUrlEncoded
    Call<com.ayubo.life.ayubolife.pojo.goals.MainResponse> setButtonPressed(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("button_id") String method
    );

    @POST("program_dashboard/dashboard")
    @FormUrlEncoded
    Call<NewDashboardMainResponse> getDashBoardData(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Field("policy_user_master_id") String policy_user_master_id
    );

    @POST("getHistoryCategoryList")
    @FormUrlEncoded
    Call<com.ayubo.life.ayubolife.pojo.reports.MainResponse> getReports(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );


    @POST("loginVerficationWithMobile")
    @FormUrlEncoded
    Call<retro_mobilelogin_model> callLoginVerficationWithMobile(
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );


    @POST("createAMobileUser")
    @FormUrlEncoded
    Call<RegisterMainResponse> createAMobileUserNew(
            @Header("app_id") String app_id,
            @Header("api_key") String api_key,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );


    @POST("loginVerficationWithMobile")
    @FormUrlEncoded
    Call<Object> loginVerficationWithMobile(
            @Header("app_id") String app_id,
            @Header("api_key") String api_key,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );


    @POST("loginVerficationWithMobile")
    @FormUrlEncoded
    Call<Object> loginVerficationWithoutMobile(
            @Header("app_id") String app_id,
            @Header("api_key") String api_key,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @POST("loginVerficationWithMobile")
    @FormUrlEncoded
    Call<WorkoutMainResponse> getWorkoutDataList(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );

    @GET("membership/cards")
    Call<MembershipCardResponse> getMembershipCards(
            @Header("Authorization") String authorization,
            @Header("app_id") String app_id,
            @Query("group_id") String group_id
    );

    @GET("membership/cards/{card_id}")
    Call<MembershipCardResponse> getMembershipDetailsByCardId(
            @Header("Authorization") String authorization,
            @Header("app_id") String app_id,
            @Path("card_id") Integer var
    );

    @GET("get-dashboard")
    Call<ProfileDashboardResponseData> getNewDashboardData(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("group_id") String group_id,
            @Query("date") Long date,
            @Query("timezone") String timezone,
            @Query("device_model") String device_modal,
            @Query("version") String version);

    @GET("get-all")
    Call<ProfileDashboardResponseData> getAllUserToDoLists(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("group_id") String group_id,
            @Query("date") Long date,
            @Query("timezone") String timezone);

    @PUT("update-status")
    Call<ProfileDashboardResponseData> toDoUpdateStatus(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Body RequestToDoAction body);

    @GET("dev-ayubo-history/v1/get-all")
    Call<ProfileDashboardResponseData> getUserPurchaseHistoryData(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("group_id") String group_id,
            @Query("date") Long date);

    @GET("get-all-badges")
    Call<ProfileDashboardResponseData> getAllBadgesByUser(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization);

    @GET("dev-ayubo-dashboard/v1/update-badge-status")
    Call<ProfileDashboardResponseData> updateBadgeStatus(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("badge_id") String badgeId);

    @GET("get-report-by-id")
    Call<ReportByIdResponse> getReportById(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("report_id") Integer report_id);

    @GET(DevAPIConfig.GET_FEED_BACK)
    Call<FeedBackResponse> getFeedbackData(
            @Header("Authorization") String authorization,
            @Header("app_id") String app_id,
            @Query("rating") Integer rating,
            @Query("type") String type
    );

    @GET("dev-ayubo-dashboard/v1/get-leaderboard")
    Call<ProfileDashboardResponseData> getPointsLeaderBoardData(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("community") String community,
            @Query("type") String type,
            @Query("date") Long date,
            @Query("timezone") String timezone,
            @Query("device_model") String device_modal

    );

    @GET("dev-ayubo-wnw-stepdata/v1/get-step-summary")
    Call<ProfileDashboardResponseData> getStepSummary(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("period") String period,
            @Query("date") Long date,
            @Query("timezone") String timezone,
            @Query("device_model") String device_modal);

    @GET("ayubo-discover/v2/get-discover")
    Call<ProfileDashboardResponseData> getDiscoverData(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization);

    @GET("dev-ayubo-chat/v1/get-sessions")
    Call<ProfileDashboardResponseData> getSessions(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization);


    @POST
    @FormUrlEncoded
    Call<VideoSessionResponseData> getVideoCallSessions(
            @Url String url,
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String input_type,
            @Field("response_type") String response_type,
            @Field("rest_data") String rest_data);


    @GET("get-my-orders")
    Call<ProfileDashboardResponseData> getMyOrders(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("type") String type,
            @Query("top") String top);

    @POST("create-order")
    Call<ProfileDashboardResponseData> createOrderMedicine(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Body OrderMedicineCreateObject body);

    @GET("get-addresses")
    Call<ProfileDashboardResponseData> getAddresses(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization);

    @PUT("update-order")
    Call<ProfileDashboardResponseData> updateOrder(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Body OMUpdateOrder oMUpdateOrder
    );


    @POST("create-address")
    Call<ProfileDashboardResponseData> createAddress(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Body OMAddress oMAddress
    );

    @PUT("update-address")
    Call<ProfileDashboardResponseData> updateAddress(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Body OMAddress oMAddress
    );

    @GET("get-partners")
    Call<ProfileDashboardResponseData> getPartners(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization
    );

    @GET("update-order-status")
    Call<ProfileDashboardResponseData> updateOrderStatus(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("order_id") String order_id,
            @Query("status") String status,
            @Query("reason") String reason
    );


    @GET("devayubo-walknwin/v1/wnw-validate")
    Call<ProfileDashboardResponseData> getWnWValidate(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization);

    @DELETE("delete-address")
    Call<ProfileDashboardResponseData> deleteAddress(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("address_id") String address_id
    );

    @GET("get-order-tracking")
    Call<ProfileDashboardResponseData> getOrderTracking(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("order_id") String order_id
    );

    @GET("get-messages")
    Call<ProfileDashboardResponseData> getChatDataBySessionId(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("sessionID") String session_id
    );

    @POST("ayubo-sms/v1/send-verification")
    Call<ProfileDashboardResponseData> sendVerification(
            @Header("app_id") String app_id,
            @Body SendVerificationObject sendVerificationObject
    );

    @GET("ayubo-sms/v1/verify")
    Call<ProfileDashboardResponseData> verifyCode(
            @Header("app_id") String app_id,
            @Query("code") String code,
            @Query("user_id") String user_id
    );

    @POST("https://dev-ayubo-apim.azure-api.net/dev-ayubo-dashboard/v1/create-dashboard-log")
    Call<ProfileDashboardResponseData> createDashboardLog(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Body CreateDashboardLogObj createDashboardLogObj
    );


    @POST
    @FormUrlEncoded
    Call<VideoSessionResponseData> videoCallFileUpload(
            @Url String url,
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String input_type,
            @Field("response_type") String response_type,
            @Field("rest_data") String rest_data);

    @GET("https://dev-ayubo-apim.azure-api.net/ayubo-ytd-challenge/v3/get-summary")
    Call<GetYTDSummaryResponse> getYtdSummary(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("id") String id
    );


    @GET("get-dashboard")
    Call<GetV1DashboardResponse> getNewV1DashboardData(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("group_id") String group_id,
            @Query("date") Long date,
            @Query("timezone") String timezone,
            @Query("device_model") String device_modal,
            @Query("version") String version);

    @GET("close-upcoming-event")
    Call<GetV1DashboardItemCloseResponse> closeUpcomingEvent(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("upcoming_event_id") String upcoming_event_id
    );

    @GET("dev-ayubo-dashboard/v1/get-leaderboard")
    Call<V1GetLeaderboardResponse> v1NewGetPointsLeaderBoardData(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("community") String community,
            @Query("type") String type,
            @Query("date") Long date,
            @Query("timezone") String timezone,
            @Query("device_model") String device_modal

    );

    @GET("dev-ayubo-wnw-stepdata/v2/get-step-summary")
    Call<V1GetStepSummaryResponse> v1NewGetStepSummary(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("period") String period,
            @Query("date") Long date,
            @Query("timezone") String timezone,
            @Query("device_model") String device_modal

    );

    @PUT("update-city")
    Call<UpdateCityResponse> updateCity(
            @Header("Authorization") String authorization,
            @Header("app_id") String app_id,
            @Body NewUpdateCity newUpdateCity
    );

    @GET("get-cities")
    Call<GetAllCitiesDataResponse> getAllCities(
            @Header("Authorization") String authorization,
            @Header("app_id") String app_id

    );
}
