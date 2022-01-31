package com.ayubo.life.ayubolife.utility;

/**
 * Created by appdev on 12/30/2016.
 */



import android.content.Context;
import android.graphics.Bitmap;

import com.ayubo.life.ayubolife.channeling.model.ChannelDoctor;
import com.ayubo.life.ayubolife.goals_extention.models.dashboard.Data;
import com.ayubo.life.ayubolife.home_popup_menu.AppointmentCommonObj;
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.QuestionDetails;
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.Report;
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.Settings;
import com.ayubo.life.ayubolife.map_challenges.model.NewLeaderboard;
import com.ayubo.life.ayubolife.map_challenges.model.Leaderboards;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.Item;
import com.ayubo.life.ayubolife.model.MenuObj;
import com.ayubo.life.ayubolife.model.User;
import com.ayubo.life.ayubolife.payment.model.ApiPAayList;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Ram implements Serializable {

    public static Integer questionCount;
    public static boolean isFromSetting;
    public static Integer recyclePosition;
    public static Context mycontext;

    public static List<LatLng> pointsNew;
    public static boolean scnnedQRCode;
    public static boolean isDirectHourView;
    public static boolean isDirectCalenderView;

    public static boolean isMedicalReports_Fragment;

    public static boolean isChallenges_Fragment;

    private static ChannelDoctor channelDoctorList=null;

    public static boolean isOne_DoctorSearch;
    public static boolean isTwo_DoctorSearch;

    public static boolean isDashboardFirsttime;
    public static boolean isMyHistory;

    public static boolean isOne_Medicine;
    public static boolean isTwo_Medicine;
    public static boolean isThree_Medicine;

    public static com.ayubo.life.ayubolife.goals_extention.models.dashboard.Data goalObject;

    private static boolean workoutStarted;
    private static final long serialVersionUID = 1L;
    private static ArrayList<DBString> specilistList = new ArrayList<DBString>();
    private static ArrayList<DBString> doctorNamesList = new ArrayList<DBString>();
    private static ArrayList<DBString> locationList = new ArrayList<DBString>();
    private static String comment;
    private static Integer currentPage;
    private static final ArrayList<Item> itemList = new ArrayList<Item>();
    private static boolean isReader = false;

    private static ArrayList<String> availableFmilyMemberList;

    private static ArrayList<String> availableReportTypeList;

    private static ArrayList<QuestionDetails> questionList;
    private static Settings questionSetting=null;


    private static float METS;
    private static int stepCount;
    private static int stepCountLastMinit;
    private static double height;
    private static int programLastPosition;

    public static String doctorImage;

    public static String username;
    public static String password;
    public static String userid;

    public static List<ApiPAayList> successPaymentDataList;


    public static String firstName;
    public static String lastName;

    public static String profileImagePath;
    public static String profileImageUri;

    public static Report medicalReportObject;

    public static ArrayList<Report> medicalReportList;

    public static boolean isIsFromSetting() {
        return isFromSetting;
    }

    public static void setIsFromSetting(boolean isFromSetting) {
        Ram.isFromSetting = isFromSetting;
    }

    public static Boolean isPaymentShortCut;

    public static Boolean getIsPaymentShortCut() {
        return isPaymentShortCut;
    }

    public static void setIsPaymentShortCut(Boolean isPaymentShortCut) {
        Ram.isPaymentShortCut = isPaymentShortCut;
    }

    public static List<ApiPAayList> getSuccessPaymentDataList() {
        return successPaymentDataList;
    }

    public static void setSuccessPaymentDataList(List<ApiPAayList> successPaymentDataList) {
        Ram.successPaymentDataList = successPaymentDataList;
    }

    public static Integer getCurrentPage() {
        return currentPage;
    }

    public static void setCurrentPage(Integer currentPage) {
        Ram.currentPage = currentPage;
    }

    public static Integer getQuestionCount() {
        return questionCount;
    }

    public static void setQuestionCount(Integer questionCount) {
        Ram.questionCount = questionCount;
    }

    public static Integer getRecyclePosition() {
        return recyclePosition;
    }

    public static void setRecyclePosition(Integer recyclePosition) {
        Ram.recyclePosition = recyclePosition;
    }

    public static Data getGoalObject() {
        return goalObject;
    }

    public static void setGoalObject(Data goalObject) {
        Ram.goalObject = goalObject;
    }

    public static ArrayList<QuestionDetails> getQuestionList() {
        return questionList;
    }

    public static void setQuestionList(ArrayList<QuestionDetails> questionList) {
        Ram.questionList = questionList;
    }

    public static Settings getQuestionSetting() {
        return questionSetting;
    }

    public static void setQuestionSetting(Settings questionSetting) {
        Ram.questionSetting = questionSetting;
    }

    public static ArrayList<Report> getMedicalReportList() {
        return medicalReportList;
    }

    public static void setMedicalReportList(ArrayList<Report> medicalReportList) {
        Ram.medicalReportList = medicalReportList;
    }

    public static Report getMedicalReportObject() {
        return medicalReportObject;
    }

    public static void setMedicalReportObject(Report medicalReportObject) {
        Ram.medicalReportObject = medicalReportObject;
    }

    public static Context getMycontext() {
        return mycontext;
    }

    public static void setMycontext(Context mycontext) {
        Ram.mycontext = mycontext;
    }

    public static String getDoctorImage() {
        return doctorImage;
    }

    public static void setDoctorImage(String doctorImage) {
        Ram.doctorImage = doctorImage;
    }

    public static ChannelDoctor getChannelDoctorList() {
        return channelDoctorList;
    }

    public static void setChannelDoctorList(ChannelDoctor channelDoctorList) {
        Ram.channelDoctorList = channelDoctorList;
    }

    public static String getProfileImagePath() {
        return profileImagePath;
    }

    public static void setProfileImagePath(String profileImagePath) {
        Ram.profileImagePath = profileImagePath;
    }

    public static String getProfileImageUri() {
        return profileImageUri;
    }

    public static void setProfileImageUri(String profileImageUri) {
        Ram.profileImageUri = profileImageUri;
    }

    public static int currentWalkStepCount=0;
    public static int currentRunStepCount=0;


    public static boolean googleFitEnable_AlreadyRegisted;
    public static boolean googleFitEnable;
    public static boolean locationStatus;
    public static boolean locationDisabledStatus;

    public static boolean writeStatusGFit;
    public static boolean readStatusGFit;
    public static User currentUser;

    public static String server_url;

    public static int stepsForEver;
    public static float metsForEver;
    public static String fullName;

    public static String sideMenuItemNumber;

    public static String familyMemberName;
    public static String reportType;
    public static String imageAbsoulutePath;
    public static List<String>  multipleImageAbsolutePath;


    public static boolean profileImageStatus;
    public static Bitmap mapSreenshot;
    public static String topMenuTabName;
    public static String shareMessage;

    public static List<DBString> listDataHeader;
    public static HashMap<String, List<AppointmentCommonObj>> listDataChild;

    public static String serviceName;
    public static ArrayList<String> selectedReportIDList;
    public static String activityName;
    public static String activityId;
    public static String mets;
    public static int appointRawCount;

    public static String dateWithString;


    public static String reportsId;
    public static String reportsName;
    public static String reportsRelationship;
    public static String reportsAssigned_user_id;
    public static String reportsUser_pic;
    public static String reportsUhid;
    public static String reportsType;
    public static com.ayubo.life.ayubolife.pojo.timeline.Post postObject;

    public static ArrayList<NewLeaderboard> leaderboardList;

    public static ArrayList<String> getSelectedReportIDList() {
        return selectedReportIDList;
    }

    public static void setSelectedReportIDList(ArrayList<String> selectedReportIDList) {
        Ram.selectedReportIDList = selectedReportIDList;
    }

    public static ArrayList<NewLeaderboard> getLeaderboardList() {
        return leaderboardList;
    }

//    public static void
//(List<NewLeaderboard> leaderboardList) {
//        Ram.leaderboardList = leaderboardList;
//    }



    public static int getProgramLastPosition() {
        return programLastPosition;
    }

    public static void setProgramLastPosition(int programLastPosition) {
        Ram.programLastPosition = programLastPosition;
    }

    public static String getDateWithString() {
        return dateWithString;
    }

    public static void setDateWithString(String dateWithString) {
        Ram.dateWithString = dateWithString;
    }

    public static String getReportsId() {
        return reportsId;
    }

    public static void setReportsId(String reportsId) {
        Ram.reportsId = reportsId;
    }

    public static String getReportsName() {
        return reportsName;
    }

    public static void setReportsName(String reportsName) {
        Ram.reportsName = reportsName;
    }

    public static String getReportsRelationship() {
        return reportsRelationship;
    }

    public static void setReportsRelationship(String reportsRelationship) {
        Ram.reportsRelationship = reportsRelationship;
    }

    public static String getReportsAssigned_user_id() {
        return reportsAssigned_user_id;
    }

    public static void setReportsAssigned_user_id(String reportsAssigned_user_id) {
        Ram.reportsAssigned_user_id = reportsAssigned_user_id;
    }

    public static String getReportsUser_pic() {
        return reportsUser_pic;
    }

    public static void setReportsUser_pic(String reportsUser_pic) {
        Ram.reportsUser_pic = reportsUser_pic;
    }

    public static String getReportsUhid() {
        return reportsUhid;
    }

    public static void setReportsUhid(String reportsUhid) {
        Ram.reportsUhid = reportsUhid;
    }

    public static String getReportsType() {
        return reportsType;
    }

    public static void setReportsType(String reportsType) {
        Ram.reportsType = reportsType;
    }


    public static ArrayList<MenuObj> menuIconList;

    public static String googleFitSteps;

    public static ArrayList<MenuObj> getMenuIconList() {
        return menuIconList;
    }

    public static void setMenuIconList(ArrayList<MenuObj> menuIconList) {
        Ram.menuIconList = menuIconList;
    }



    public static boolean isWorkoutStarted() {
        return workoutStarted;
    }

    public static void setWorkoutStarted(boolean workoutStarted) {
        Ram.workoutStarted = workoutStarted;
    }









    public static String getServiceName() {
        return serviceName;
    }

    public static void setServiceName(String serviceName) {
        Ram.serviceName = serviceName;
    }

    public static int getAppointRawCount() {
        return appointRawCount;
    }

    public static void setAppointRawCount(int appointRawCount) {
        Ram.appointRawCount = appointRawCount;
    }

    public static List<DBString> getListDataHeader() {
        return listDataHeader;
    }

    public static void setListDataHeader(List<DBString> listDataHeader) {
        Ram.listDataHeader = listDataHeader;
    }

    public static HashMap<String, List<AppointmentCommonObj>> getListDataChild() {
        return listDataChild;
    }

    public static void setListDataChild(HashMap<String, List<AppointmentCommonObj>> listDataChild) {
        Ram.listDataChild = listDataChild;
    }

    private static  ArrayList<Object> doctorDataList;;



    public static String getActivityName() {
        return activityName;
    }

    public static void setActivityName(String activityName) {
        Ram.activityName = activityName;
    }

    public static String getActivityId() {
        return activityId;
    }

    public static void setActivityId(String activityId) {
        Ram.activityId = activityId;
    }

    public static String getMets() {
        return mets;
    }

    public static void setMets(String mets) {
        Ram.mets = mets;
    }

    public static String getShareMessage() {
        return shareMessage;
    }

    public static void setShareMessage(String shareMessage) {
        Ram.shareMessage = shareMessage;
    }

    public static Bitmap getMapSreenshot() {
        return mapSreenshot;
    }

    public static void setMapSreenshot(Bitmap mapSreenshot) {
        Ram.mapSreenshot = mapSreenshot;
    }

    public static List<LatLng> getPointsNew() {
        return pointsNew;
    }

    public static void setPointsNew(List<LatLng> pointsNew) {
        Ram.pointsNew = pointsNew;
    }

    public static String getTopMenuTabName() {
        return topMenuTabName;
    }

    public static void setTopMenuTabName(String topMenuTabName) {
        Ram.topMenuTabName = topMenuTabName;
    }

    public static boolean isProfileImageStatus() {
        return profileImageStatus;
    }

    public static void setProfileImageStatus(boolean profileImageStatus) {
        Ram.profileImageStatus = profileImageStatus;
    }





    public static ArrayList<Object> getDoctorDataList() {
        return doctorDataList;
    }

    public static void setDoctorDataList(ArrayList<Object> doctorDataList) {
        Ram.doctorDataList = doctorDataList;
    }









    public static String getImageAbsoulutePath() {
        return imageAbsoulutePath;
    }

    public static void setImageAbsoulutePath(String imageAbsoulutePath) {
        Ram.imageAbsoulutePath = imageAbsoulutePath;
    }

    public static List<String> getMultipleImageAbsolutePath() {
        return multipleImageAbsolutePath;
    }

    public static void setMultipleImageAbsolutePath(List<String> imageAbsolutePath) {
        Ram.multipleImageAbsolutePath = imageAbsolutePath;
    }

    public static ArrayList<String> getAvailableReportTypeList() {
        return availableReportTypeList;
    }

    public static void setAvailableReportTypeList(ArrayList<String> availableReportTypeList) {
        Ram.availableReportTypeList = availableReportTypeList;
    }

    public static String getReportType() {
        return reportType;
    }

    public static void setReportType(String reportType) {
        Ram.reportType = reportType;
    }

    public static String getFamilyMemberName() {
        return familyMemberName;
    }

    public static void setFamilyMemberName(String familyMemberName) {
        Ram.familyMemberName = familyMemberName;
    }

    public static ArrayList<String> getAvailableFmilyMemberList() {
        return availableFmilyMemberList;
    }

    public static void setAvailableFmilyMemberList(ArrayList<String> availableFmilyMemberList) {
        Ram.availableFmilyMemberList = availableFmilyMemberList;
    }

    public static String getSideMenuItemNumber() {
        return sideMenuItemNumber;
    }

    public static void setSideMenuItemNumber(String sideMenuItemNumber) {
        Ram.sideMenuItemNumber = sideMenuItemNumber;
    }

    public static boolean isScnnedQRCode() {
        return scnnedQRCode;
    }

    public static void setScnnedQRCode(boolean scnnedQRCode) {
        Ram.scnnedQRCode = scnnedQRCode;
    }

    public static boolean isDashboardFirsttime() {
        return isDashboardFirsttime;
    }

    public static void setIsDashboardFirsttime(boolean isMyAppointment) {
        Ram.isDashboardFirsttime = isMyAppointment;
    }

    public static boolean isOne_Medicine() {
        return isOne_Medicine;
    }

    public static void setIsOne_Medicine(boolean isOne_Medicine) {
        Ram.isOne_Medicine = isOne_Medicine;
    }

    public static boolean isThree_Medicine() {
        return isThree_Medicine;
    }

    public static void setIsThree_Medicine(boolean isThree_Medicine) {
        Ram.isThree_Medicine = isThree_Medicine;
    }

    public static boolean isDirectHourView() {
        return isDirectHourView;
    }

    public static void setIsDirectHourView(boolean isDirectHourView) {
        Ram.isDirectHourView = isDirectHourView;
    }

    public static boolean isDirectCalenderView() {
        return isDirectCalenderView;
    }

    public static void setIsDirectCalenderView(boolean isDirectCalenderView) {
        Ram.isDirectCalenderView = isDirectCalenderView;
    }

    public static boolean isTwo_Medicine() {
        return isTwo_Medicine;
    }

    public static void setIsTwo_Medicine(boolean isTwo_Medicine) {
        Ram.isTwo_Medicine = isTwo_Medicine;
    }

    public static boolean isMyHistory() {
        return isMyHistory;
    }

    public static void setIsMyHistory(boolean isMyHistory) {
        Ram.isMyHistory = isMyHistory;
    }

    public static boolean isOne_DoctorSearch() {
        return isOne_DoctorSearch;
    }

    public static void setIsOne_DoctorSearch(boolean isOne_DoctorSearch) {
        Ram.isOne_DoctorSearch = isOne_DoctorSearch;
    }

    public static boolean isTwo_DoctorSearch() {
        return isTwo_DoctorSearch;
    }

    public static void setIsTwo_DoctorSearch(boolean isTwo_DoctorSearch) {
        Ram.isTwo_DoctorSearch = isTwo_DoctorSearch;
    }







    public static void setIsMedicalReports_Fragment(boolean isMedicalReports_Fragment) {
        Ram.isMedicalReports_Fragment = isMedicalReports_Fragment;
    }



    public static void setIsChallenges_Fragment(boolean isChallenges_Fragment) {
        Ram.isChallenges_Fragment = isChallenges_Fragment;
    }











    public static ArrayList<DBString> getSpecilistList() {
        return specilistList;
    }

    public static void setSpecilistList(ArrayList<DBString> specilistList) {
        Ram.specilistList = specilistList;
    }


    public static int getCurrentWalkStepCount() {
        return currentWalkStepCount;
    }

    public static void setCurrentWalkStepCount(int currentWalkStepCount) {
        Ram.currentWalkStepCount = currentWalkStepCount;
    }


    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Ram.currentUser = currentUser;
    }


    public static String getUserid() {
        return userid;
    }

    public static void setUserid(String userid) {
        Ram.userid = userid;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Ram.password = password;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Ram.username = username;
    }

    private static float today_total_steps;

    public static float getToday_total_steps() {
        return today_total_steps;
    }

    public static void setToday_total_steps(float today_total_steps) {
        Ram.today_total_steps = today_total_steps;
    }

    public static double getHeight() {
        return height;
    }

    public static void setHeight(double height) {
        Ram.height = height;
    }

    public static int getStepCountLastMinit() {
        return stepCountLastMinit;
    }

    public static void setStepCountLastMinit(int stepCountLastMinit) {
        Ram.stepCountLastMinit = stepCountLastMinit;
    }

    public static int getStepCount() {
        return stepCount;
    }

    public static void setStepCount(int stepCount) {
        Ram.stepCount = stepCount;
    }

    public static float getMETS() {
        return METS;
    }

    public static void setMETS(float METS) {
        Ram.METS = METS;
    }


    public static ArrayList<Item> getItemList() {
        return itemList;
    }

    protected Ram() {
    }



    public static String getComment() {
        return comment;
    }

    public static void setComment(String comment) {
        Ram.comment = comment;
    }

    public static boolean isReader() {
        return isReader;
    }

    public static void setIsReader(boolean isReader) {
        Ram.isReader = isReader;
    }

    public static void setLeaderboardList(@NotNull List<Leaderboards> dataList) {
    }
}