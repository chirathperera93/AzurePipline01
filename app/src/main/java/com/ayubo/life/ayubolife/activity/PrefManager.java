package com.ayubo.life.ayubolife.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.Ringtone;
import android.os.Vibrator;
import android.util.Base64;

import com.ayubo.life.ayubolife.reports.model.AllRecordsMainResponse;
import com.ayubo.life.ayubolife.revamp.v1.model.V1DashboardData;
import com.ayubo.life.ayubolife.utility.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Ravi on 08/07/15.
 */
public class PrefManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "ayubolife";
    private static final String KEY_TIMESTAMP = "timestamp";
    // All Shared Preferences Keys
    private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
    private static final String KEY_MOBILE_NUMBER = "mobile_number";
    private static final String KEY_APP_VERSION = "app_version";
    private static final String KEY_QR_LINK = "qr_code";
    private static final String KEY_UID = "uid";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_NAME = "name";

    private static final String KEY_BNAME_SHOW = "bshow";
    private static final String KEY_BNAME_HEADING = "bheading";
    private static final String KEY_BNAME_TEXT = "btext";
    private static final String KEY_BNAME_BUTTON_TEXT = "bbtext";
    private static final String KEY_BNAME_IMAGE = "bimage";
    private static final String KEY_BNAME_ACTION = "baction";
    private static final String KEY_BNAME_META = "bmeta";


    private static final String KEY_CALLERNAME = "videocallername";

    private static final String KEY_MapReload = "isneedmapreload";
    private static final String KEY_LANGUAGE_SELECTION = "isneedlanguageselection";

    private static final String KEY_SELECTED_POST = "selected_post_object";

    private static final String KEY_LANGUAGE = "language";

    private static final String KEY_VEDIO_CALL = "videocall";

    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_NIC = "nic";

    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_HASH_KEY = "hashkey";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_COUNTRY_CODE = "KEY_COUNTRY_CODE";
    private static final String KEY_IMAGE_URL = "KEY_IMAGE_URL";
    private static final String KEY_ID = "KEY_ID";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";

    private static final String KEY_STEPS = "steps";
    private static final String KEY_CALARIES = "calories";
    private static final String KEY_ENERGY = "energy";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_DURATION = "duration";

    private static final String KEY_MY_GOAL_SPONSER_LARGE_IMAGE = "my_goal_sponser_large_image";
    private static final String KEY_MY_GOAL_SHARE_LARGE_IMAGE = "my_goal_share_large_image";

    private static final String KEY_PUSH_OPEN_META = "push_open_meta";

    private static final String KEY_IS_FROM_PUSH = "is_from_push";


    private static final String KEY_ICON = "icon";
    private static final String KEY_STEPDEVICE = "stepdevice";
    private static final String KEY_QUESTION_GROUP = "question_group_id";
    // public void createUserData(String steps,String calories, String energy, String distance,String duration)

    private static final String KEY_USER_REGISTER_STATUS = "userRegisterStatus";

    private static final String KEY_MAP_ZOOMLEVEL = "map_zoom_level";
    private static final String KEY_GOOGLEFIT_ENABLED = "googlefitenabled";
    private static final String KEY_WORKOUT_FIRSTTIME = "workoutfirsttime";
    private static final String KEY_HOME_FIRSTTIME = "homefirsttime";
    private static final String KEY_GOOGLE_FITDISABLED = "0";
    private static final String KEY_RELAX_DATA = "key_relax_data";
    private static final String KEY_EAT_DATA = "key_eat_data";
    private static final String KEY_WORKOUT_DATA = "key_workout_data";
    private static final String KEY_NOTIFICATION_DATA = "key_notification_data";
    private static final String KEY_FIREBASE_NOTIFICATION_ACTION = "key_firebase_notification_action";
    private static final String KEY_FIREBASE_NOTIFICATION_META = "key_firebase_notification_meta";


    private static final String KEY_EXPERT_NEW_DATA = "key_expertnew_data";
    private static final String KEY_HEALTH_DATA = "key_health_data";
    private static final String KEY_ASK_DATA = "key_ask_data";

    private static final String KEY_CHALLANGE_ID = "key_new_challneo_id";


    private static final String KEY_USER_TOKEN = "key_user_token";

    private static final String KEY_GOAL_CATEGORY = "key_goal_category";
    private static final String KEY_GOAL_CATEGORY_NAME = "key_goal_category_name";


    private static final String KEY_IS_GOOGLEFIT_CONNECTED = "key_is_google_fit_connected";


    private static final String KEY_COMMIUNITY_DATA = "key_commiunity_data";

    private static final String KEY_LAST_STEPS = "key_last_steps";

    private static final String KEY_HOMEPAGE_MAINMENU_DATA = "key_homepage_mainmenu";
    private static final String KEY_HOMEPAGE_SERVICEMENU_DATA = "key_homepage_servicemenu";

    private static final String KEY_DOCTOR_DATA = "key_doctor_data";
    private static final String KEY_REPORTS_DATA = "key_reports_data";
    private static final String KEY_FAMILYMEMBER_DATA = "key_familymember_data";

    private static final String KEY_TIMELINE_FIRSTPAGE_DATA = "key_timeline_firstpage";

    private static final String KEY_LATEST_GFIT_STEPS = "key_latest_gfit_steps";

    private static final String KEY_NEW_SENSOR_STEPS = "key_new_sensor_steps";
    private static final String KEY_OLD_SENSOR_STEPS = "key_old_sensor_steps";
    private static final String KEY_STEPS_SENSOR_NAME = "key_step_sensor_name";

    private static final String KEY_TIMELINE_MAIN_RESPONSE_DATA = "key_timeline_mainresponse";
    private static final String KEY_TIMELINE_BOTTOM_DATA = "key_timeline_bottom";

    private static final String KEY_NEW_STEP_SERVICE_RUNNING = "newsteps_service_running";

    private static final String KEY_SELECTED_GOAL = "selected_goal_catego";
    private static final String KEY_SELECTED_GOAL_ID = "selected_goal_catego_id";

    private static final String KEY_SELECTED_GOAL_IMAGE = "selected_goal_image";
    private static final String KEY_ENTEREDED_GOAL_NAME = "entered_goal_name";
    private static final String KEY_SENSOR_DATE = "sendor_date";
    private static final int KEY_SENSOR_COUNT = 0;
    private static final int KEY_SENSOR_FINALREADING = 0;

    private static final String KEY_PREVIOUS_POSITION_ADDED = "timeline_previous_posiotn";

    private static final String KEY_SGOAL_DATA = "goal_data_string";

    private static final String KEY_REPORT_LOADING_NEED = "report_reading_need";


    private static final String KEY_SELECTED_FAMILY_MEMBER = "selected_family_memeber";

    private static final String KEY_FROM_SEND_TO_BASE_VIEW = "is_from_send_to_base_view";
    private static final String KEY_CASH_REFRESH_DATA_TRPE = "refresh_data_type";

    private static final String KEY_STEPS_UPDATED_DATE = "step_updated_date";


    private static final String KEY_NEW_POST_ADDED = "is_new_post_added";
    private static final String KEY_NEW_COMMENT_ADDED = "is_new_comment_added";
    private static final String KEY_NEW_GOAL_ADDED = "is_new_goal_added";

    private static final String KEY_AFTER_GFIT_ENABLE = "is_gfit_enable_from_workout";

    private static final String KEY_REPORT_LAST_UPDATED = "repors_service_last_call_time";


    private static final String KEY_CHANGE_GOAL_STATUS = "is_goal_status_change";


    private static final String KEY_GOAL_LIST = "goal_list";
    private static final String KEY_GOAL_IMAGES_LIST = "goal_images_list";
    private static final String KEY_GOAL_CATEGORY_LIST = "goal_category_list";

    private static final String KEY_MY_GOAL_ID = "my_goal_id";
    private static final String KEY_MY_GOAL_NAME = "my_goal_name";
    private static final String KEY_MY_GOAL_IMAGE = "my_goal_image";
    private static final String KEY_MY_GOAL_LARGE_IMAGE = "my_goal_large_image";
    private static final String KEY_MY_GOAL_BG = "my_goal_bg";

    private static final String KEY_MY_GOAL_STATUS = "my_goal_status";
    private static final String KEY_MY_GOAL_BUTTON_TEXT = "my_goal_button_text";

    private static final String KEY_SELECTED_COMMIUNITY = "user_selected_commiunity";
    private static final String KEY_SELECTED_COMMIUNITY_NAME = "user_selected_commiunity_name";
    private static final String KEY_HOURLY_ALARM_SET = "hourLy_alarm_set";
    private static final String KEY_MIDNIGHT_ALARM_SET = "midnight_alarm_set";
    private static final String KEY_DASHBOARD_DESCRIPTION = "KEY_DASHBOARD_DESCRIPTION";

    private static final String KEY_TODAY = "today";

    private static final String KEY_VIDEOCALL_ROOM_NAME = "KEY_VIDEOCALL_ROOM_NAME";
    private static final String KEY_VIDEOCALL_TOKEN = "KEY_VIDEOCALL_TOKEN";
    private static final String KEY_VIDEOCALL_CALLER_ID = "KEY_VIDEOCALL_CALLER_ID";
    private static final String KEY_VIDEOCALL_CALLER_NAME = "KEY_VIDEOCALL_CALLER_NAME";
    private static final String KEY_VIDEOCALL_ROOMNAME = "KEY_VIDEOCALL_ROOMNAME";
    private static final String WALK_WIN_CHALLENGE_ID = "0";


    private static final String KEY_VIDEOCALL_CONSULTANT_SPECIALIZATION = "KEY_VIDEOCALL_CONSULTANT_SPECIALIZATION";
    private static final String KEY_VIDEOCALL_CONSULTANT_PIC = "KEY_VIDEOCALL_CONSULTANT_PIC";
    private static final String KEY_VIDEOCALL_TITLE = "KEY_VIDEOCALL_TITLE";


    private static final Boolean KEY_JANASHAKTHI_WELCOME = false;
    private static final String KEY_RELATE_ID = "relate_id";
    private static final String KEY_PAYMENT_ID = "payment_id";


    private static final String TOTAL_STEPS = "TOTAL_STEPS";
    private static final String TOTAL_CHALLENGE_STEPS = "TOTAL_CHALLENGE_STEPS";
    private static final String KEY_IS_FIRST_TIME_RUN = "KEY_IS_FIRST_TIME_RUN";
    private static final String KEY_INSURANCE_CHAT = "KEY_INSURANCE_CHAT";

    private static final String OPEN_APP_UPDATE = "true";
    private static final String DASHBOARD_META = "DASHBOARD_META";
    private static final String V1_DASHBOARD_GROUP_NAME = "V1_DASHBOARD_GROUP_NAME";
    private static final String DISCOVER_TITLE_NAME = "DISCOVER_TITLE_NAME";


    private static final String STEPSYNCTIME = "STEPSYNCTIME";
    private static final String IS_LINKED = "IS_LINKED";

    private static final String MEDIA_UPLOAD_MEMBERS = "MEDIA_UPLOAD_MEMBERS";
    private static final String FB_ACTION = "FB_ACTION";
    private static final String FB_META = "FB_META";
    private static final String Update_Available_Count_Down_Time = "Update_Available_Count_Down_Time";
    private static final String NEW_DASHBOARD_DATA = "NEW_DASHBOARD_DATA";
    private static final String NEW_V1_DASHBOARD_DATA = "NEW_V1_DASHBOARD_DATA";
    private static final String NEW_WNW_DASHBOARD_DATA = "NEW_WNW_DASHBOARD_DATA";
    private static final String AYUBO_DISCOVER_DASHBOARD_DATA = "AYUBO_DISCOVER_DASHBOARD_DATA";
    private static final String AYUBO_DISCOVER_BANNER_DATA = "AYUBO_DISCOVER_BANNER_DATA";
    private static final String OM_CREATED_ORDER = "OM_CREATED_ORDER";
    private static final String OM_CREATED_ORDER_FROM_HISTORY = "OM_CREATED_ORDER";
    private static final String OM_CREATED_ORDER_FROM_COMMON = "OM_CREATED_ORDER_FROM_COMMON";
    private static final String WNW_SPONSOR_DATA = "WNW_SPONSOR_DATA";
    private static final String KEY_PROFILE_DATA = "KEY_PROFILE_DATA";

    private static final String UNREAD_MESSAGE_LABEL = "UNREAD_MESSAGE_LABEL";
    private static final String OLD_NOTIFICATION_DATA = "OLD_NOTIFICATION_DATA";
    private static final String MY_PAYMENT_CARD = "MY_PAYMENT_CARD";
    private static final String REDEEM_POINTS = "REDEEM_POINTS";
    private static final String IS_ALL_TABS_DISABLE = "false";
    private static NotificationManager Notification_Manager = null;
    private static Ringtone Ringtone_Data = null;
    private static Vibrator Vibrator_Data = null;


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setMyGoalData(String id, String name, String image, String large_image, String status, String buttonText, String share_image, String sponser_image, String bg_image) {
        editor.putString(KEY_MY_GOAL_ID, id);
        editor.putString(KEY_MY_GOAL_NAME, name);
        editor.putString(KEY_MY_GOAL_IMAGE, image);
        editor.putString(KEY_MY_GOAL_LARGE_IMAGE, large_image);
        editor.putString(KEY_MY_GOAL_STATUS, status);
        editor.putString(KEY_MY_GOAL_BUTTON_TEXT, buttonText);
        editor.putString(KEY_MY_GOAL_SHARE_LARGE_IMAGE, share_image);
        editor.putString(KEY_MY_GOAL_SPONSER_LARGE_IMAGE, sponser_image);
        editor.putString(KEY_MY_GOAL_BG, bg_image);
        editor.commit();
    }

    public HashMap<String, String> getMyGoalData() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("my_goal_id", pref.getString(KEY_MY_GOAL_ID, ""));
        profile.put("my_goal_name", pref.getString(KEY_MY_GOAL_NAME, ""));
        profile.put("my_goal_image", pref.getString(KEY_MY_GOAL_IMAGE, ""));
        profile.put("my_goal_bg", pref.getString(KEY_MY_GOAL_BG, ""));
        profile.put("my_goal_large_image", pref.getString(KEY_MY_GOAL_LARGE_IMAGE, ""));
        profile.put("my_goal_status", pref.getString(KEY_MY_GOAL_STATUS, ""));
        profile.put("my_goal_button_text", pref.getString(KEY_MY_GOAL_BUTTON_TEXT, ""));

        profile.put("my_goal_sponser_large_image", pref.getString(KEY_MY_GOAL_SPONSER_LARGE_IMAGE, ""));
        profile.put("my_goal_share_large_image", pref.getString(KEY_MY_GOAL_SHARE_LARGE_IMAGE, ""));
        return profile;
    }

    public void saveToday() {
        editor.putString(KEY_TODAY, Util.getTodayString()).apply();
    }


    public String getLastTodayDate() {
        return pref.getString(KEY_TODAY, "");
    }

    public void setGoalCategoryList(String name) {
        editor.putString(KEY_GOAL_CATEGORY_LIST, name);
        editor.commit();
    }

    public HashMap<String, String> getGoalCategoryList() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("goal_category_list", pref.getString(KEY_GOAL_CATEGORY_LIST, ""));
        return profile;
    }

    public void setUserSelectedCommiunityName(String name) {
        editor.putString(KEY_SELECTED_COMMIUNITY_NAME, name);
        editor.commit();
    }

    public void setIsRunFirstTime(String name) {
        editor.putString(KEY_IS_FIRST_TIME_RUN, name);
        editor.commit();
    }

    public String getIsRunFirstTime() {
        return pref.getString(KEY_IS_FIRST_TIME_RUN, "false");
    }

    public HashMap<String, String> getUserSelectedCommiunityName() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("user_selected_commiunity_name", pref.getString(KEY_SELECTED_COMMIUNITY_NAME, ""));
        return profile;
    }

    public void setUserSelectedCommiunity(String name) {
        editor.putString(KEY_SELECTED_COMMIUNITY, name);
        editor.commit();
    }

    public void setInsuranceChat(String insuranceChatData) {
        editor.putString(KEY_INSURANCE_CHAT, insuranceChatData);
        editor.commit();
    }

    public String getInsuranceChat() {
        return pref.getString(KEY_INSURANCE_CHAT, "");
    }

    public HashMap<String, String> getUserSelectedCommiunity() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("user_selected_commiunity", pref.getString(KEY_SELECTED_COMMIUNITY, ""));
        return profile;
    }


    public void setGoalImagesList(String name) {
        editor.putString(KEY_GOAL_IMAGES_LIST, name);
        editor.commit();
    }

    public HashMap<String, String> getGoalImagesList() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("goal_images_list", pref.getString(KEY_GOAL_IMAGES_LIST, ""));
        return profile;
    }

    public void setGoalList(String name) {
        editor.putString(KEY_GOAL_LIST, name);
        editor.commit();
    }

    public HashMap<String, String> getGoalList() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("goal_list", pref.getString(KEY_GOAL_LIST, ""));
        return profile;
    }


    public void setVRoomName(String data) {
        editor.putString(KEY_VIDEOCALL_ROOMNAME, data);
        editor.commit();
    }

    public String getVRoomName() {
        return pref.getString(KEY_VIDEOCALL_ROOMNAME, "");
    }

    public void setVCallerName(String data) {
        editor.putString(KEY_VIDEOCALL_CALLER_NAME, data);
        editor.commit();
    }

    public String getVCallerName() {
        return pref.getString(KEY_VIDEOCALL_CALLER_NAME, "");
    }

    public void setVCallerId(String data) {
        editor.putString(KEY_VIDEOCALL_CALLER_ID, data);
        editor.commit();
    }

    public String getVCallerId() {
        return pref.getString(KEY_VIDEOCALL_CALLER_ID, "");
    }

    public void setVRoomToken(String data) {
        editor.putString(KEY_VIDEOCALL_TOKEN, data);
        editor.commit();
    }

    public String getVRoomToken() {
        return pref.getString(KEY_VIDEOCALL_TOKEN, "");
    }


    public void setEnteredGoalName(String data) {
        editor.putString(KEY_ENTEREDED_GOAL_NAME, data);
        editor.commit();
    }

    public String getEnteredGoalName() {
        return pref.getString(KEY_ENTEREDED_GOAL_NAME, "");
    }

    public void setPreviousPosition(Integer data) {
        editor.putInt(KEY_PREVIOUS_POSITION_ADDED, data);
        editor.commit();
    }

    public Integer getPreviousPosition() {
        return pref.getInt(KEY_PREVIOUS_POSITION_ADDED, 0);
    }

    public void setIsFromPush(Boolean data) {
        editor.putBoolean(KEY_IS_FROM_PUSH, data);
        editor.commit();
    }

    public Boolean getIsFromPush() {
        return pref.getBoolean(KEY_IS_FROM_PUSH, false);
    }

    public void setIsFromJoinChallenge(Boolean data) {
        editor.putBoolean("from_join_challenge", data);
        editor.commit();
    }

    public Boolean isFromJoinChallenge() {
        return pref.getBoolean("from_join_challenge", false);
    }

    public void setPushMeta(String data) {
        editor.putString(KEY_PUSH_OPEN_META, data);
        editor.commit();
    }

    public String getPushMeta() {
        return pref.getString(KEY_PUSH_OPEN_META, "");
    }

    public void setQuestionGroupId(String data) {
        editor.putString(KEY_QUESTION_GROUP, data);
        editor.commit();
    }

    public String getQuestionGroupId() {
        return pref.getString(KEY_QUESTION_GROUP, "1");
    }


    public void setIsNewCommentAdded(String data) {
        editor.putString(KEY_NEW_COMMENT_ADDED, data);
        editor.commit();
    }

    public String getIsNewCommentAdded() {
        return pref.getString(KEY_NEW_COMMENT_ADDED, "");
    }

    public void setIsGoalAdded(String data) {
        editor.putString(KEY_NEW_GOAL_ADDED, data);
        editor.commit();
    }

    public String getIsGoalAdded() {
        return pref.getString(KEY_NEW_GOAL_ADDED, "");
    }

    public void setReportLastUpdated(boolean data) {
        editor.putBoolean(KEY_REPORT_LAST_UPDATED, data);
        editor.commit();
    }

    public boolean getReportLastUpdated() {
        return pref.getBoolean(KEY_REPORT_LAST_UPDATED, false);
    }

    public void setAfterGFitEnabled(String data) {
        editor.putString(KEY_AFTER_GFIT_ENABLE, data);
        editor.commit();
    }

    public String isAfterGFitEnabled() {
        return pref.getString(KEY_AFTER_GFIT_ENABLE, "");
    }


    public void setChangeGoalStatus(String data) {
        editor.putString(KEY_CHANGE_GOAL_STATUS, data);
        editor.commit();
    }

    public String getIsChangeGoalStatus() {
        return pref.getString(KEY_CHANGE_GOAL_STATUS, "");
    }

    public void setFamilyMemberId(String data) {
        editor.putString(KEY_SELECTED_FAMILY_MEMBER, data);
        editor.commit();
    }

    public String getFamilyMemberId() {
        return pref.getString(KEY_SELECTED_FAMILY_MEMBER, "0");
    }

    public void setReportLoadingStatus(String data) {
        editor.putString(KEY_REPORT_LOADING_NEED, data);
        editor.commit();
    }

    public String getReportLoadingStatus() {
        return pref.getString(KEY_REPORT_LOADING_NEED, "0");
    }


    public void setStepsUpdatedDate(String data) {
        editor.putString(KEY_STEPS_UPDATED_DATE, data);
        editor.commit();
    }

    public String getStepsUpdatedDate() {
        return pref.getString(KEY_STEPS_UPDATED_DATE, "no");
    }


    public void setRelateID(String data) {
        editor.putString(KEY_RELATE_ID, data);
        editor.commit();
    }

    public String getRelateID() {
        return pref.getString(KEY_RELATE_ID, "no");
    }

    public void setProgramID(String data) {
        editor.putString("program_meta", data);
        editor.commit();
    }

    public String getProgramID() {
        return pref.getString("program_meta", "no");
    }


    public void setIsJanashakthiWelcomee(Boolean data) {
        editor.putBoolean("is_janashakthi_welcome", data);
        editor.commit();
    }

    public Boolean isFromJanashakthiWelcomee() {
        return pref.getBoolean("is_janashakthi_welcome", false);
    }

    public void setIsJanashakthiDyanamic(Boolean data) {
        editor.putBoolean("is_janashakthi_dynamic", data);
        editor.commit();
    }

    public Boolean isFromJanashakthiDyanamic() {
        return pref.getBoolean("is_janashakthi_dynamic", false);
    }


    public void setRefreshDataType(String data) {
        editor.putString(KEY_CASH_REFRESH_DATA_TRPE, data);
        editor.commit();
    }

    public String getRefreshDataType() {
        return pref.getString(KEY_CASH_REFRESH_DATA_TRPE, "no");
    }

    public void setFromSendToBaseView(String data) {
        editor.putString(KEY_FROM_SEND_TO_BASE_VIEW, data);
        editor.commit();
    }

    public String getFromSendToBaseView() {
        return pref.getString(KEY_FROM_SEND_TO_BASE_VIEW, "no");
    }


    public void setIsNewPostAdded(String data) {
        editor.putString(KEY_NEW_POST_ADDED, data);
        editor.commit();
    }

    public String getIsNewPostAdded() {
        return pref.getString(KEY_NEW_POST_ADDED, "");
    }

    public void setSelectedPostDataString(String data) {
        editor.putString(KEY_SELECTED_POST, data);
        editor.commit();
    }

    public String getSelectedPostDataString() {
        return pref.getString(KEY_SELECTED_POST, "");
    }


    public void setSelectedGoalImage(String data) {
        editor.putString(KEY_SELECTED_GOAL_IMAGE, data);
        editor.commit();
    }

    public String getSelectedGoalImage() {
        return pref.getString(KEY_SELECTED_GOAL_IMAGE, "");
    }

    public void setSelectedGoal(String goalName, String goalId) {
        editor.putString(KEY_SELECTED_GOAL, goalName);
        editor.putString(KEY_SELECTED_GOAL_ID, goalId);
        editor.commit();
    }

    public HashMap<String, String> getSelectedGoal() {

        HashMap<String, String> profile = new HashMap<>();
        profile.put("selected_goal_catego", pref.getString(KEY_SELECTED_GOAL, ""));
        profile.put("selected_goal_catego_id", pref.getString(KEY_SELECTED_GOAL_ID, ""));

        return profile;
    }


    public void setLastStepsData(int data) {
        editor.putInt(KEY_LAST_STEPS, data);
        editor.commit();
    }

    public int getLastStepsData() {
        return pref.getInt(KEY_LAST_STEPS, 0);
    }

    public void setAllWorkoutData(String data) {
        editor.putString(KEY_WORKOUT_DATA, data);
        editor.commit();
    }

    public String getAllWorkoutData() {
        return pref.getString(KEY_WORKOUT_DATA, "");
    }

    public void setAllNotificationData(String data) {
        editor.putString(KEY_NOTIFICATION_DATA, data);
        editor.commit();
    }

    public void setAllFireBaseNotificationData(String action, String meta) {
        editor.putString(KEY_FIREBASE_NOTIFICATION_ACTION, action);
        editor.putString(KEY_FIREBASE_NOTIFICATION_META, meta);
        editor.commit();
    }

    public String getAllFireBaseNotificationAction() {
        return pref.getString(KEY_FIREBASE_NOTIFICATION_ACTION, "");
    }

    public String getAllFireBaseNotificationMeta() {
        return pref.getString(KEY_FIREBASE_NOTIFICATION_META, "");
    }

    public String getAllNotificationData() {
        return pref.getString(KEY_NOTIFICATION_DATA, "");
    }

    public void setCommiunityData(String data) {
        editor.putString(KEY_COMMIUNITY_DATA, data);
        editor.commit();
    }

    public String getCommiunityData() {
        return pref.getString(KEY_COMMIUNITY_DATA, "");
    }


    public void setAskData(String data) {
        editor.putString(KEY_ASK_DATA, data);
        editor.commit();
    }

    public String getAskData() {
        return pref.getString(KEY_ASK_DATA, "");
    }


    public void setChallangeID(String data) {
        editor.putString(KEY_CHALLANGE_ID, data);
        editor.commit();
    }

    public String getChallangeID() {
        return pref.getString(KEY_CHALLANGE_ID, "");
    }


    public void setHealthData(String data) {
        editor.putString(KEY_HEALTH_DATA, data);
        editor.commit();
    }

    public String getHealthData() {
        return pref.getString(KEY_HEALTH_DATA, "");
    }


    public void setUserToken(String data) {
        editor.putString(KEY_USER_TOKEN, data);
        editor.commit();
    }

    public String getUserToken() {
        return pref.getString(KEY_USER_TOKEN, "");
    }

    public void setGoalCategory(String data) {
        editor.putString(KEY_GOAL_CATEGORY, data);
        editor.commit();
    }

    public String getGoalCategory() {
        return pref.getString(KEY_GOAL_CATEGORY, "");
    }

    public void setGoalCategoryName(String data) {
        editor.putString(KEY_GOAL_CATEGORY_NAME, data);
        editor.commit();
    }

    public String getGoalCategoryName() {
        return pref.getString(KEY_GOAL_CATEGORY_NAME, "All users avg");
    }

    public void setAllEatData(String data) {
        editor.putString(KEY_EAT_DATA, data);
        editor.commit();
    }

    public String getAllEatData() {
        return pref.getString(KEY_EAT_DATA, "");
    }

    public void setAllRelaxData(String data) {
        editor.putString(KEY_RELAX_DATA, data);
        editor.commit();
    }

    public String getAllRelaxData() {
        return pref.getString(KEY_RELAX_DATA, "");
    }


    public void setHomePageMainMenu(String data) {
        editor.putString(KEY_HOMEPAGE_MAINMENU_DATA, data);
        editor.commit();
    }

    public String getHomePageMainMenu() {
        return pref.getString(KEY_HOMEPAGE_MAINMENU_DATA, "");
    }

    public void setHomePageServiceMenu(String data) {
        editor.putString(KEY_HOMEPAGE_SERVICEMENU_DATA, data);
        editor.commit();
    }

    public String getHomePageServiceMenu() {
        return pref.getString(KEY_HOMEPAGE_SERVICEMENU_DATA, "");
    }


    public void setNewServiceLastRunningTime(String data) {
        editor.putString(KEY_NEW_STEP_SERVICE_RUNNING, data);
        editor.commit();
    }

    public String getNewServiceLastRunningTime() {
        return pref.getString(KEY_NEW_STEP_SERVICE_RUNNING, "0");
    }


    public void setRepostsData(String data) {
        editor.putString(KEY_REPORTS_DATA, data);
        editor.commit();
    }

    public String getRepostsData() {
        return pref.getString(KEY_REPORTS_DATA, "");
    }

    public void setFamilyMemberData(String data) {
        editor.putString(KEY_FAMILYMEMBER_DATA, data);
        editor.commit();
    }

    public String getFamilyMemberData() {
        return pref.getString(KEY_FAMILYMEMBER_DATA, "");
    }


    public void setTimeLineFirstPage(String data) {
        editor.putString(KEY_TIMELINE_FIRSTPAGE_DATA, data);
        editor.commit();
    }

    public String getTimeLineFirstPage() {
        return pref.getString(KEY_TIMELINE_FIRSTPAGE_DATA, "");
    }


    public void setLatestGFitSteps(String data) {
        editor.putString(KEY_LATEST_GFIT_STEPS, data);
        editor.commit();
    }

    public String getLatestGFitSteps() {
        return pref.getString(KEY_LATEST_GFIT_STEPS, "0");
    }


    public void setTimeLineTopData(String data) {
        editor.putString(KEY_TIMELINE_MAIN_RESPONSE_DATA, data);
        editor.commit();
    }

    public String getTimeLineTopData() {
        return pref.getString(KEY_TIMELINE_MAIN_RESPONSE_DATA, "");
    }

    public void setTimeLineBottomData(String data) {
        editor.putString(KEY_TIMELINE_BOTTOM_DATA, data);
        editor.commit();
    }

    public String getTimeLineBottomData() {
        return pref.getString(KEY_TIMELINE_BOTTOM_DATA, "");
    }

    private String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    private String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

    public void setLastDataUpdateTimestamp(String st) {
        //editor.putString(encrypt(KEY_TIMESTAMP), encrypt(st));
        editor.putString(KEY_TIMESTAMP, st);
        editor.commit();
    }

    public String getLastDataUpdateTimestamp() {
        //  return pref.getString(encrypt(KEY_TIMESTAMP), encrypt("0"));
        return pref.getString(KEY_TIMESTAMP, "0");
    }

    public void setUserRegisterStatus(String st) {
        editor.putString(KEY_USER_REGISTER_STATUS, st);
        editor.commit();
    }

    public String getUserRegisterStatus() {
        return pref.getString(KEY_USER_REGISTER_STATUS, "0");
    }

    //=============================
    public void setWorkoutFirsttime(String st) {
        editor.putString(KEY_WORKOUT_FIRSTTIME, st);
        editor.commit();
    }

    public String isWorkoutFirsttime() {
        return pref.getString(KEY_WORKOUT_FIRSTTIME, "false");
    }

    public void setHomeFirsttime(String st) {
        editor.putString(KEY_HOME_FIRSTTIME, st);
        editor.commit();
    }

    public String isHomeFirsttime() {
        return pref.getString(KEY_HOME_FIRSTTIME, "false");
    }

    //==============================

    public void setGoogleFitEnabled(String st) {
        editor.putString(KEY_GOOGLEFIT_ENABLED, st);
        editor.commit();
    }

    public String isGoogleFitEnabled() {
        return pref.getString(KEY_GOOGLEFIT_ENABLED, "false");
    }

    public void setMapZoomLevel(String st) {
        editor.putString(KEY_MAP_ZOOMLEVEL, st);
        editor.commit();
    }

    public String isMapZoomLevel() {
        return pref.getString(KEY_MAP_ZOOMLEVEL, "5");
    }

    public void setGoogleFitDisabledTimes(String st) {
        editor.putString(KEY_GOOGLE_FITDISABLED, st);
        editor.commit();
    }

    // KEY_GOOGLE_FITDISABLED
    public String isGoogleFitDisabledTimes() {
        return pref.getString(KEY_GOOGLE_FITDISABLED, "0");
    }


    public void setIsWaitingForSms(boolean isWaiting) {
        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();
    }

    public boolean isWaitingForSms() {
        return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
    }

    public String getMobileNumber() {
        return pref.getString(KEY_MOBILE_NUMBER, "0");
    }

    public String getQRLink() {
        return pref.getString(KEY_QR_LINK, "0");
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        editor.commit();
    }

    public void setQRLink(String mobileNumber) {
        editor.putString(KEY_QR_LINK, mobileNumber);
        editor.commit();
    }

    public void setAppVersion(String mobileNumber) {
        editor.putString(KEY_APP_VERSION, mobileNumber);
        editor.commit();
    }

    public String getAppVersion() {
        return pref.getString(KEY_APP_VERSION, "0");
    }


    public void setVideocall(String mobileNumber) {
        editor.putString(KEY_VEDIO_CALL, mobileNumber);
        editor.commit();
    }

    public String getVideocall() {
        return pref.getString(KEY_VEDIO_CALL, "no");
    }


    public void setLanguage(String mobileNumber) {
        editor.putString(KEY_LANGUAGE, mobileNumber);
        editor.commit();
    }

    public String getLanguage() {
        return pref.getString(KEY_LANGUAGE, "0");
    }


    public void setPrefVdioCallerName(String name) {
        editor.putString(KEY_CALLERNAME, name);
        editor.commit();
    }

    public String getPrefVdioCallerName() {
        return pref.getString(KEY_CALLERNAME, "");
    }

    public void setMapReload(Boolean status) {
        editor.putBoolean(KEY_MapReload, status);
        editor.commit();
    }

    public Boolean isNeedMapReload() {
        return pref.getBoolean(KEY_MapReload, false);
    }

    public void setLanguageSelection(Boolean status) {
        editor.putBoolean(KEY_LANGUAGE_SELECTION, status);
        editor.commit();
    }

    public Boolean isNeedLanguageSelection() {
        return pref.getBoolean(KEY_LANGUAGE_SELECTION, false);
    }


    public void setPrefName(String name) {
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

    public String getPrefName() {
        return pref.getString(KEY_NAME, "");
    }


    public void createLogin(String name, String email, String mobile) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public void saveSensorStep(String sendor_date, int sendor_count, int sendor_final_reading) {
        editor.putString("sendor_date", sendor_date);
        editor.putInt("sendor_count", sendor_count);
        editor.putInt("sendor_final_reading", sendor_final_reading);
        editor.commit();
    }

    public HashMap<String, String> getSensorStep() {
        HashMap<String, String> profile = new HashMap<>();
        // return pref.getInt(KEY_LAST_STEPS, 0);
        profile.put("sendor_date", pref.getString("sendor_date", ""));
        profile.put("sendor_count", Integer.toString(pref.getInt("sendor_count", 0)));
        profile.put("sendor_count", Integer.toString(pref.getInt("sendor_final_reading", 0)));
        return profile;
    }

    public void createLoginUser(String uid, String name, String email, String mobile, String hashkey, String image, String country_code) {
        editor.putString(KEY_UID, uid);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_HASH_KEY, hashkey);
        editor.putString(KEY_IMAGE_PATH, image);
        editor.putString(KEY_COUNTRY_CODE, country_code);
        editor.commit();
    }

    public HashMap<String, String> getUserProfile() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("name", pref.getString(KEY_NAME, ""));
        profile.put("email", pref.getString(KEY_EMAIL, ""));
        profile.put("birthday", pref.getString(KEY_BIRTHDAY, ""));
        profile.put("gender", pref.getString(KEY_GENDER, ""));
        profile.put("nic", pref.getString(KEY_NIC, ""));
        profile.put("KEY_MOBILE_NUMBER", pref.getString(KEY_MOBILE_NUMBER, ""));
        profile.put("KEY_COUNTRY_CODE", pref.getString(KEY_COUNTRY_CODE, ""));
        profile.put("KEY_IMAGE_URL", pref.getString(KEY_IMAGE_URL, ""));
        profile.put("KEY_ID", pref.getString(KEY_ID, ""));
        profile.put("KEY_USER_NAME", pref.getString(KEY_USER_NAME, ""));
        return profile;
    }

    public void createUserProfile(
            String name,
            String email,
            String birthday,
            String gender,
            String nic,
            String mobileNumber,
            String countryCode,
            String imageURL,
            String id,
            String userName
    ) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_BIRTHDAY, birthday);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_NIC, nic);
        editor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        editor.putString(KEY_COUNTRY_CODE, countryCode);
        editor.putString(KEY_IMAGE_URL, imageURL);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_USER_NAME, userName);
        editor.commit();
    }


    public HashMap<String, String> getBannerData() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("bshow", pref.getString(KEY_BNAME_SHOW, ""));
        profile.put("bheading", pref.getString(KEY_BNAME_HEADING, ""));
        profile.put("btext", pref.getString(KEY_BNAME_TEXT, ""));
        profile.put("bbtext", pref.getString(KEY_BNAME_BUTTON_TEXT, ""));
        profile.put("bimage", pref.getString(KEY_BNAME_IMAGE, ""));
        profile.put("baction", pref.getString(KEY_BNAME_ACTION, ""));
        profile.put("bmeta", pref.getString(KEY_BNAME_META, ""));
        return profile;
    }

    public void createBanner(String bshow, String bheading, String btext, String bbtext, String bimage, String baction, String bmeta) {
        editor.putString(KEY_BNAME_SHOW, bshow);
        editor.putString(KEY_BNAME_HEADING, bheading);
        editor.putString(KEY_BNAME_TEXT, btext);
        editor.putString(KEY_BNAME_BUTTON_TEXT, bbtext);
        editor.putString(KEY_BNAME_IMAGE, bimage);
        editor.putString(KEY_BNAME_ACTION, baction);
        editor.putString(KEY_BNAME_META, bmeta);
        editor.commit();
    }

    public void createUserData(String steps, String calories, String energy, String distance, String duration) {
        editor.putString(KEY_STEPS, steps);
        editor.putString(KEY_CALARIES, calories);
        editor.putString(KEY_ENERGY, energy);
        editor.putString(KEY_DISTANCE, distance);
        editor.putString(KEY_DURATION, duration);
        editor.commit();
    }

    public HashMap<String, String> getUserData() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("steps", pref.getString(KEY_STEPS, "0"));
        profile.put("calories", pref.getString(KEY_CALARIES, "0"));
        profile.put("energy", pref.getString(KEY_ENERGY, "0"));
        profile.put("distance", pref.getString(KEY_DISTANCE, "0"));
        profile.put("duration", pref.getString(KEY_DURATION, "0"));
        return profile;
    }

    public void createDeviceData(String icon, String stepdevice) {
        editor.putString(KEY_ICON, icon);
        editor.putString(KEY_STEPDEVICE, stepdevice);
        editor.commit();
    }

    public HashMap<String, String> getDeviceData() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("icon", pref.getString(KEY_ICON, ""));
        profile.put("stepdevice", pref.getString(KEY_STEPDEVICE, "activity_AYUBO"));

        return profile;
    }

    public HashMap<String, String> getLoginUser() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("uid", pref.getString(KEY_UID, ""));
        profile.put("name", pref.getString(KEY_NAME, ""));
        profile.put("email", pref.getString(KEY_EMAIL, ""));
        profile.put("mobile", pref.getString(KEY_MOBILE, ""));
        profile.put("hashkey", pref.getString(KEY_HASH_KEY, ""));
        profile.put("image_path", pref.getString(KEY_IMAGE_PATH, ""));
        profile.put("KEY_COUNTRY_CODE", pref.getString(KEY_COUNTRY_CODE, ""));

        return profile;
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("name", pref.getString(KEY_NAME, null));
        profile.put("email", pref.getString(KEY_EMAIL, null));
        profile.put("mobile", pref.getString(KEY_MOBILE, null));
        return profile;
    }

    public void setHourlyAlarmStatus(boolean status) {
        editor.putBoolean(KEY_HOURLY_ALARM_SET, status);
    }

    public boolean getHourlyAlarmStatus() {
        return pref.getBoolean(KEY_HOURLY_ALARM_SET, false);
    }

    public void setHourlyMidNightAlarmStatus(boolean status) {
        editor.putBoolean(KEY_MIDNIGHT_ALARM_SET, status);
    }

    public boolean getHourlyMidNightAlarmStatus() {
        return pref.getBoolean(KEY_MIDNIGHT_ALARM_SET, false);
    }

    public void createChallangeData(
            String challenge_id,
            String white_lines,
            String noof_day,
            String cards,
            String showpopup,
            String service_checkpoints,
            String enabled_checkpoints,
            String Treatures,
            String steps,
            String weekSteps,
            String tip_icon,
            String tip,
            String tipheading,
            String tip_header_1,
            String tip_header_2,
            String tip_type,
            String tip_meta
    ) {
        editor.putString("challenge_id", challenge_id);
        editor.putString("white_lines", white_lines);
        editor.putString("noof_day", noof_day);
        editor.putString("cards", cards);
        editor.putString("showpopup", showpopup);
        editor.putString("service_checkpoints", service_checkpoints);
        editor.putString("enabled_checkpoints", enabled_checkpoints);
        editor.putString("Treatures", Treatures);
        editor.putString("steps", steps);
        editor.putString("weekSteps", weekSteps);
        editor.putString("tip_icon", tip_icon);
        editor.putString("tip", tip);
        editor.putString("tipheading", tipheading);
        editor.putString("tip_header_1", tip_header_1);
        editor.putString("tip_header_2", tip_header_2);
        editor.putString("tip_type", tip_type);
        editor.putString("tip_meta", tip_meta);

        editor.commit();
    }

    public HashMap<String, String> getChallangeData() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("challenge_id", pref.getString("challenge_id", ""));
        profile.put("white_lines", pref.getString("white_lines", ""));
        profile.put("noof_day", pref.getString("noof_day", ""));
        profile.put("cards", pref.getString("cards", ""));
        profile.put("showpopup", pref.getString("showpopup", ""));
        profile.put("service_checkpoints", pref.getString("service_checkpoints", ""));
        profile.put("enabled_checkpoints", pref.getString("enabled_checkpoints", ""));
        profile.put("Treatures", pref.getString("Treatures", ""));
        profile.put("steps", pref.getString("steps", ""));
        profile.put("weekSteps", pref.getString("weekSteps", ""));
        profile.put("tip_icon", pref.getString("tip_icon", ""));
        profile.put("tip", pref.getString("tip", ""));
        profile.put("tipheading", pref.getString("tipheading", ""));
        profile.put("tip_header_2", pref.getString("tip_header_2", ""));
        profile.put("tip_header_1", pref.getString("tip_header_1", ""));
        profile.put("tip_type", pref.getString("tip_type", ""));
        profile.put("tip_meta", pref.getString("tip_meta", ""));
        return profile;
    }

    public void setPaymentId(String data) {
        editor.putString(KEY_PAYMENT_ID, data);
        editor.commit();
    }

    public String getPaymentId() {
        return pref.getString(KEY_PAYMENT_ID, "no");
    }

    public void setWalkWinChallengeId(String data) {
        editor.putString(WALK_WIN_CHALLENGE_ID, data);
        editor.commit();
    }

    public String getWalkWinChallengeId() {
        return pref.getString(WALK_WIN_CHALLENGE_ID, "0");
    }

    public void setWalkWinDetail(String totalSteps, String walkAndWinChallengeSteps) {
        editor.putString(TOTAL_STEPS, totalSteps);
        editor.putString(TOTAL_CHALLENGE_STEPS, walkAndWinChallengeSteps);
        editor.commit();
    }

    public String getWalkWinTotalSteps() {
        return pref.getString(TOTAL_STEPS, "0");
    }

    public String getWalkWinChallengeSteps() {
        return pref.getString(TOTAL_CHALLENGE_STEPS, "0");
    }

    public void setOpenAppUpdateChecker(Boolean isNeverOpenAppUpdate) {
        editor.putBoolean(OPEN_APP_UPDATE, isNeverOpenAppUpdate);
        editor.commit();
    }

    public void setNewDashBoardData(String dashboardMeta) {
        editor.putString(DASHBOARD_META, dashboardMeta);
        editor.commit();
    }

    public void setNewV1DashBoardGroupName(String dashboardMeta) {
        editor.putString(V1_DASHBOARD_GROUP_NAME, dashboardMeta);
        editor.commit();
    }

    public String getNewDashBoardData() {
        return pref.getString(DASHBOARD_META, "");
    }

    public String getNewV1DashBoardGroupName() {
        return pref.getString(V1_DASHBOARD_GROUP_NAME, "");
    }

    public Boolean getOpenAppUpdateChecker() {
        return pref.getBoolean(OPEN_APP_UPDATE, false);
    }


    public void setDiscoverTileName(String discoverTileName) {
        editor.putString(DISCOVER_TITLE_NAME, discoverTileName);
        editor.commit();
    }

    public String getDiscoverTileName() {
        return pref.getString(DISCOVER_TITLE_NAME, "");
    }


    public void saveStepSyncTime(Long stepSyncTime) {
        editor.putLong(STEPSYNCTIME, stepSyncTime);
        editor.commit();
    }

    public Long getStepSyncTime() {
        return pref.getLong(STEPSYNCTIME, 0L);
    }


    public void setLinkedWithHuaweiHealth(Boolean isLinked) {
        editor.putBoolean(IS_LINKED, isLinked);
        editor.commit();
    }

    public Boolean getLinkedWithHuaweiHealth() {
        return pref.getBoolean(IS_LINKED, false);
    }

    public void setReportMembers(List<AllRecordsMainResponse.AllRecordsMember> memberList) {
        editor.putString(MEDIA_UPLOAD_MEMBERS, memberList.toString());
        editor.commit();
    }

    public void setFbEvenActionMeta(String ac, String meta) {
        editor.putString(FB_ACTION, ac);
        editor.putString(FB_META, meta);
        editor.commit();
    }


    public String getFbEvenAction() {
        return pref.getString(FB_ACTION, "");
    }

    public String getFbEvenMeta() {
        return pref.getString(FB_META, "");
    }


    public String getReportMembers() {
        return pref.getString(MEDIA_UPLOAD_MEMBERS, "");
    }


    public void setDisableAllTabs(Boolean isAllTabsDisable) {
        editor.putBoolean(IS_ALL_TABS_DISABLE, isAllTabsDisable);
        editor.commit();
    }

    public Boolean getDisableAllTabs() {

        return pref.getBoolean(IS_ALL_TABS_DISABLE, false);
    }

    public void setUpdateAvailableCountDownTime(Long time) {
        editor.putString(Update_Available_Count_Down_Time, time.toString());
        editor.commit();
    }

    public String getUpdateAvailableCountDownTime() {
        return pref.getString(Update_Available_Count_Down_Time, "0");
    }

    public void setNotificationMgr(NotificationManager notificationManager) {
        Notification_Manager = notificationManager;

    }


    public NotificationManager getNotificationMgr() {
        return Notification_Manager;
    }

    public void setRingingTone(Ringtone ringtone) {
        Ringtone_Data = ringtone;

    }

    public Ringtone getRingtone() {
        return Ringtone_Data;
    }

    public void setVibrator(Vibrator vibrator) {
        Vibrator_Data = vibrator;

    }

    public Vibrator getVibrator() {
        return Vibrator_Data;
    }

    public void setVConsultantPic(String consultant_pic) {
        editor.putString(KEY_VIDEOCALL_CONSULTANT_PIC, consultant_pic);
        editor.commit();
    }

    public void setVConsultantSpecilization(String consultant_specilization) {
        editor.putString(KEY_VIDEOCALL_CONSULTANT_SPECIALIZATION, consultant_specilization);
        editor.commit();
    }


    public String getVConsultantPic() {
        return pref.getString(KEY_VIDEOCALL_CONSULTANT_PIC, "");
    }

    public String getVConsultantSpecilization() {
        return pref.getString(KEY_VIDEOCALL_CONSULTANT_SPECIALIZATION, "");
    }

    public void setVTitle(String title) {
        editor.putString(KEY_VIDEOCALL_TITLE, title);
        editor.commit();
    }

    public String getVTitle() {
        return pref.getString(KEY_VIDEOCALL_TITLE, "");
    }


    public void saveDashboardData(JsonObject dashboardData) {
        editor.putString(NEW_DASHBOARD_DATA, dashboardData.toString());
        editor.commit();
    }

    public void saveV1DashboardData(V1DashboardData v1DashboardData) {
        JsonObject changedJsonObject = new Gson().toJsonTree(v1DashboardData).getAsJsonObject();
        editor.putString(NEW_V1_DASHBOARD_DATA, changedJsonObject.toString());
        editor.commit();
    }

    public void saveWnwDashboardData(JsonObject dashboardData) {
        editor.putString(NEW_WNW_DASHBOARD_DATA, dashboardData.toString());
        editor.commit();
    }

    public String getSavedDashboardData() {
        return pref.getString(NEW_DASHBOARD_DATA, "");
    }

    public V1DashboardData getSavedV1DashboardData() {
        String data = pref.getString(NEW_V1_DASHBOARD_DATA, "");
        return new Gson().fromJson(data, V1DashboardData.class);

    }


    public String getSavedWnwDashboardData() {
        return pref.getString(NEW_WNW_DASHBOARD_DATA, "");
    }

    public void setOrderMedicineCreatedOrder(JsonObject oMCreatedOrderObj) {
        editor.putString(OM_CREATED_ORDER, oMCreatedOrderObj.toString());
        editor.commit();
    }

    public String getOrderMedicineCreatedOrder() {
        return pref.getString(OM_CREATED_ORDER, "");
    }

    public void saveWnWSponsorDetail(JsonObject sponsorData) {
        editor.putString(WNW_SPONSOR_DATA, sponsorData.toString());
        editor.commit();
    }

    public String getWnWSponsorDetail() {
        return pref.getString(WNW_SPONSOR_DATA, "");
    }

    public String getOrderMedicineCreatedOrderFromHistory() {
        return pref.getString(OM_CREATED_ORDER_FROM_HISTORY, "");
    }

    public void setOrderMedicineCreatedOrderFromHistory(JsonObject oMCreatedOrderObj) {
        editor.putString(OM_CREATED_ORDER_FROM_HISTORY, oMCreatedOrderObj.toString());
        editor.commit();
    }

    public String getOrderMedicineCreatedOrderFromCommon() {
        return pref.getString(OM_CREATED_ORDER_FROM_COMMON, "");
    }

    public void setOrderMedicineCreatedOrderFromCommon(JsonObject oMCreatedOrderObj) {
        editor.putString(OM_CREATED_ORDER_FROM_COMMON, oMCreatedOrderObj.toString());
        editor.commit();
    }

    public void setOldNotificationData(JsonArray oldNotificationData) {
        editor.putString(OLD_NOTIFICATION_DATA, oldNotificationData.toString());
        editor.commit();
    }

    public String getOldNotificationData() {
        return pref.getString(OLD_NOTIFICATION_DATA, "");
    }

    public void setProfileDataSend(Boolean send) {
        editor.putBoolean(KEY_PROFILE_DATA, send);
        editor.commit();
    }

    public Boolean isSentProfileData() {
        return pref.getBoolean(KEY_PROFILE_DATA, false);
    }

    public void saveAyuboDiscoverData(JsonObject dashboardData) {
        if (dashboardData.size() > 0) {
            editor.putString(AYUBO_DISCOVER_DASHBOARD_DATA, dashboardData.toString());
        } else {
            editor.putString(AYUBO_DISCOVER_DASHBOARD_DATA, "");
        }

        editor.commit();
    }

    public String getSavedAyuboDiscoverData() {
        return pref.getString(AYUBO_DISCOVER_DASHBOARD_DATA, "");
    }

    public void saveAyuboDiscoverBanner(JsonObject dashboardData) {
        editor.putString(AYUBO_DISCOVER_BANNER_DATA, dashboardData.toString());
        editor.commit();
    }

    public String getSavedAyuboDiscoverBanner() {
        return pref.getString(AYUBO_DISCOVER_BANNER_DATA, "");
    }

    public void setShowDashboardDescription(boolean data) {
        editor.putBoolean(KEY_DASHBOARD_DESCRIPTION, data);
        editor.commit();
    }

    public boolean getShowDashboardDescription() {
        return pref.getBoolean(KEY_DASHBOARD_DESCRIPTION, true);
    }


    public void saveMyPaymentCard(JsonObject newMyCardItemJsonObject) {
        editor.putString(MY_PAYMENT_CARD, newMyCardItemJsonObject.toString());
        editor.commit();
    }

    public String getMyPaymentCard() {
        return pref.getString(MY_PAYMENT_CARD, "");
    }

    public void savePointCards(JsonArray redeemPointDataArrayList) {
        editor.putString(REDEEM_POINTS, redeemPointDataArrayList.toString());
        editor.commit();
    }

    public String getPointCards() {
        return pref.getString(REDEEM_POINTS, "");
    }
}
