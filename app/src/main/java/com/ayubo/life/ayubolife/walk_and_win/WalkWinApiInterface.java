package com.ayubo.life.ayubolife.walk_and_win;

import com.ayubo.life.ayubolife.channeling.activity.DoctorsListResponse;
import com.ayubo.life.ayubolife.channeling.model.Expert;
import com.ayubo.life.ayubolife.masterapiconfig.DevAPIConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface WalkWinApiInterface {

    @POST(DevAPIConfig.JOIN_CHALLENGE)
    Call<WalkWinStepsResponse> joinChallenge(@Header("app_id") String app_id, @Header("Authorization") String authorization, @Body WalkWinJoinChallengeObj body);


    @POST(DevAPIConfig.JOIN_CHALLENGE_HEMAS)
    Call<WalkWinStepsResponse> joinChallengeHemas(@Header("app_id") String app_id, @Header("Authorization") String authorization, @Body WalkWinJoinChallengeObj body);

    @POST(DevAPIConfig.SAVE_DAILY_STEP)
    Call<WalkWinStepsResponse> saveDailySteps(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Body List<StepObj> body,
            @Query("timezone") String timezone
    );

    @POST(DevAPIConfig.SAVE_DAILY_STEP_FOR_HEMAS)
    Call<WalkWinStepsResponse> saveDailyStepsForHemas(@Header("app_id") String app_id, @Header("Authorization") String authorization, @Body List<StepObj> body);

    @POST(DevAPIConfig.CLAIM_REWARD)
    Call<WalkWinRewardResponse> claimReward(@Header("app_id") String app_id, @Header("Authorization") String authorization, @Body WalkWinJoinChallengeObj challenge_id);


    @POST(DevAPIConfig.CLAIM_REWARD_HEMAS)
    Call<WalkWinRewardResponse> claimRewardHemas(@Header("app_id") String app_id, @Header("Authorization") String authorization, @Body WalkWinJoinChallengeObj challenge_id);


    @GET(DevAPIConfig.GET_DAILY_TOTAL_STEP_BY_DATES)
    Call<WalkWinStepsResponse> getTotalStepsByDates(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("startdatetime") Long startdatetime,
            @Query("enddatetime") Long enddatetime
    );


    @GET(DevAPIConfig.GET_CHALLENGE_BY_ID)
    Call<WalkWinStepsResponse> getChallengeById(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("challenge_id") String challenge_id
    );


    @GET(DevAPIConfig.GET_CHALLENGE_BY_ID_FOR_HEMAS)
    Call<WalkWinStepsResponse> getChallengeByIdForHemas(
            @Header("app_id") String app_id,
            @Header("Authorization") String authorization,
            @Query("challenge_id") String challenge_id
    );

    @FormUrlEncoded
    @POST(DevAPIConfig.GET_DOCTORS_LIST)
    Call<DoctorsListResponse> getDoctorListsData(
            @Header("app_id") String app_id,
            @Field("method") String method,
            @Field("input_type") String JSON,
            @Field("response_type") String JSON2,
            @Field("rest_data") String jsonStr
    );


}
