package com.ayubo.life.ayubolife.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.FavouriteActivity;
import com.ayubo.life.ayubolife.model.SettingEntity;
import com.ayubo.life.ayubolife.model.StepRecoder;
import com.ayubo.life.ayubolife.model.User;
import com.ayubo.life.ayubolife.model.profileimageEn;
import com.ayubo.life.ayubolife.model.todaysummery;
import com.ayubo.life.ayubolife.model.workoutEntity;

import com.ayubo.life.ayubolife.utility.Utility;
import com.google.firebase.crash.FirebaseCrash;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by AppDev5 on 1/4/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "hemasactivedb34";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");;
    // Table Names
    private static final String TABLE_FAVOURITE = "favourites";
    private static final String TABLE_STEPRECODER = "steprecoder";
    private static final String TABLE_TODAY_MAIN_SUMMERY = "todaymainsummery";
    private static final String TABLE_DAILY_WORKOUT_SUMMERY = "dailyworkoutsummery";
    private static final String TABLE_DAILY_WORKOUT_SUMMERY2 = "dailyworkoutsummery2";
    private static final String TABLE_USER = "user";
    private static final String TABLE_SETTINGS = "settings";
    private static final String TABLE_PROFILE_IMAGE = "profileimage";
    SQLiteDatabase db;

    private static final String TABLE_NEW_1 = "tableone";
    private static final String TABLE_NEW_2 = "tabletwo";
    private static final String TABLE_NEW_3 = "tablethree";
    private static final String TABLE_NEW_4 = "tablefour";
    private static final String TABLE_NEW_5 = "tablefive";
    private static final String TABLE_NEW_6 = "tablesix";
    private static final String TABLE_NEW_7 = "tableseven";
    private static final String TABLE_NEW_8 = "tableeight";
    private static final String TABLE_NEW_9 = "tablenine";
    private static final String TABLE_NEW_10 = "tableten";

    // TABLE_PROFILE_IMAGE column names
    private static final String KEY_NAME_PROFILE_ID = "userid";
    private static final String KEY_NAME_PROFILE_IMAGE = "image_name";
    private static final String KEY_IMAGE_PROFILE_IMAGE = "image_data";



    private static final String TABLE_NEW_1_1 = "tb1_raw_1";
    private static final String TABLE_NEW_1_2 = "tb1_raw_2";
    private static final String TABLE_NEW_1_3 = "tb1_raw_3";
    private static final String TABLE_NEW_1_4 = "tb1_raw_4";
    private static final String TABLE_NEW_1_5 = "tb1_raw_5";
    private static final String TABLE_NEW_1_6 = "tb1_raw_6";
    private static final String TABLE_NEW_1_7 = "tb1_raw_7";
    private static final String TABLE_NEW_1_8 = "tb1_raw_8";
    private static final String TABLE_NEW_1_9 = "tb1_raw_9";
    private static final String TABLE_NEW_1_10 = "tb1_raw_10";

    //========================================
    private static final String TABLE_NEW_2_1 = "tb2_raw_1";
    private static final String TABLE_NEW_2_2 = "tb2_raw_2";
    private static final String TABLE_NEW_2_3 = "tb2_raw_3";
    private static final String TABLE_NEW_2_4 = "tb2_raw_4";
    private static final String TABLE_NEW_2_5 = "tb2_raw_5";
    private static final String TABLE_NEW_2_6 = "tb2_raw_6";
    private static final String TABLE_NEW_2_7 = "tb2_raw_7";
    private static final String TABLE_NEW_2_8 = "tb2_raw_8";
    private static final String TABLE_NEW_2_9 = "tb2_raw_9";
    private static final String TABLE_NEW_2_10 = "tb2_raw_10";

    //========================================
    private static final String TABLE_NEW_3_1 = "tb3_raw_1";
    private static final String TABLE_NEW_3_2 = "tb3_raw_2";
    private static final String TABLE_NEW_3_3 = "tb3_raw_3";
    private static final String TABLE_NEW_3_4 = "tb3_raw_4";
    private static final String TABLE_NEW_3_5 = "tb3_raw_5";
    private static final String TABLE_NEW_3_6 = "tb3_raw_6";
    private static final String TABLE_NEW_3_7 = "tb3_raw_7";
    private static final String TABLE_NEW_3_8 = "tb3_raw_8";
    private static final String TABLE_NEW_3_9 = "tb3_raw_9";
    private static final String TABLE_NEW_3_10 = "tb3_raw_10";

    //========================================
    private static final String TABLE_NEW_4_1 = "tb4_raw_1";
    private static final String TABLE_NEW_4_2 = "tb4_raw_2";
    private static final String TABLE_NEW_4_3 = "tb4_raw_3";
    private static final String TABLE_NEW_4_4 = "tb4_raw_4";
    private static final String TABLE_NEW_4_5 = "tb4_raw_5";
    private static final String TABLE_NEW_4_6 = "tb4_raw_6";
    private static final String TABLE_NEW_4_7 = "tb4_raw_7";
    private static final String TABLE_NEW_4_8 = "tb4_raw_8";
    private static final String TABLE_NEW_4_9 = "tb4_raw_9";
    private static final String TABLE_NEW_4_10 = "tb4_raw_10";

    //========================================
    private static final String TABLE_NEW_5_1 = "tb5_raw_1";
    private static final String TABLE_NEW_5_2 = "tb5_raw_2";
    private static final String TABLE_NEW_5_3 = "tb5_raw_3";
    private static final String TABLE_NEW_5_4 = "tb5_raw_4";
    private static final String TABLE_NEW_5_5 = "tb5_raw_5";
    private static final String TABLE_NEW_5_6 = "tb5_raw_6";
    private static final String TABLE_NEW_5_7 = "tb5_raw_7";
    private static final String TABLE_NEW_5_8 = "tb5_raw_8";
    private static final String TABLE_NEW_5_9 = "tb5_raw_9";
    private static final String TABLE_NEW_5_10 = "tb5_raw_10";


    // TABLE_SETTINGS Settings Table Columns names
    private static final String KEY_S_USER_ID = "userid";
    private static final String KEY_S_STEP_GOAL = "stepgoal";
    private static final String KEY_S_ENERGY_GOAL = "energygoal";
    private static final String KEY_S_CALERY_GOAL = "calerygoal";
    private static final String KEY_S_HEIGHT_GOAL = "heightgoal";
    private static final String KEY_S_WEIGHT_GOAL = "weightgoal";
    private static final String KEY_S_WAIST_GOAL = "waist";
    private static final String KEY_S_BPM_GOAL = "bpmgoal";
    private static final String KEY_S_SETTING_DATE = "updateddate";
    private static final String KEY_S_UPDATED_STATUS = "updatedstatus";



    // Contacts Table Columns names
    private static final String KEY_USERNAME_ID = "usernameid";
    private static final String KEY_USER_FNAME = "fname";
    private static final String KEY_USER_LNAME = "lname";
    private static final String KEY_USER_IMAGE = "imagepath";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_GENDER = "gender";
    private static final String KEY_USER_BIRTHDAY = "birthday";
    private static final String KEY_USER_DATE = "date";
    private static final String KEY_USER_LASTMODIFIED = "datemodified";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_BODYAGE = "bodyage";

    private static final String KEY_ID = "id";
    // Contacts Table Columns names
    private static final String KEY_F_ID = "id";
    private static final String KEY_F_USER_ID = "userid";
    private static final String KEY_F_NAME = "name";
    private static final String KEY_F_IMAGE = "image";
    private static final String KEY_F_MET = "met";

    // Contacts Table Columns names
    private static final String KEY_REC_ID = "id";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_DATE = "date";
    private static final String KEY_STEPS = "stpes";
    private static final String KEY_RUN_TOT = "runs";
    private static final String KEY_JUMP_TOT = "jumps";
    private static final String KEY_METS = "mets";
    private static final String KEY_ENGY_PERSENT_TOT = "energy_persent_tot";
    private static final String KEY_TIMESTAMP = "timestamp";


    // Contacts Table Columns names
    private static final String KEY_SUM_ID = "id";
    private static final String KEY_STEP_TOT = "step_tot";
    private static final String KEY_MET_TOT = "met_tot";
    private static final String KEY_CAL_TOT = "cal_tot";

    private static final String KEY_W_ORK_ID = "id";
    private static final String KEY_W_USER_ID = "userid";
    private static final String KEY_W_TYPE = "type";
    private static final String KEY_W_STEP = "steps";
    private static final String KEY_W_RUN = "runs";
    private static final String KEY_W_ENERGY = "energy";
    private static final String KEY_W_CAL = "cal";
    private static final String KEY_W_DISTANCE = "distance";
    private static final String KEY_W_START_TIME = "start_time";
    private static final String KEY_W_END_TIME = "end_time";

    private static final String KEY_W_DATE = "date";
    private static final String KEY_W_UPDATE_TYPE = "update_type";
    private static final String KEY_W_WORKOUT_TYPE = "time_duration";
    private static final String KEY_W_SERVER_UPDATED_STATUS = "status";

    private static DatabaseHandler INSTANCE = null;

    private static final AtomicInteger openCounter = new AtomicInteger();

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();

    }

    public static synchronized DatabaseHandler getInstance(final Context c) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseHandler(c.getApplicationContext());
        }
        openCounter.incrementAndGet();
        return INSTANCE;
    }
    @Override
    public void close() {
        if (openCounter.decrementAndGet() == 0) {
            super.close();
        }
    }
//    public static DatabaseHandler getInstance(Context context){
//        if(INSTANCE == null){
//            INSTANCE = new DatabaseHandler(context);
//        }
//        return INSTANCE;
//    }
    // Table create statement
    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE " + TABLE_PROFILE_IMAGE + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_NAME_PROFILE_ID + " TEXT," +
            KEY_NAME_PROFILE_IMAGE + " TEXT," +
            KEY_IMAGE_PROFILE_IMAGE + " TEXT" + ")";

    // Creating Tables
    private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE " + TABLE_SETTINGS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_S_USER_ID + " TEXT,"
            + KEY_S_STEP_GOAL + " TEXT,"
            + KEY_S_ENERGY_GOAL + " TEXT,"
            + KEY_S_CALERY_GOAL + " TEXT,"
            + KEY_S_HEIGHT_GOAL + " TEXT,"
            + KEY_S_WEIGHT_GOAL + " TEXT,"
            + KEY_S_WAIST_GOAL + " TEXT,"
            + KEY_S_BPM_GOAL + " TEXT,"
            + KEY_S_SETTING_DATE + " TEXT,"
            + KEY_S_UPDATED_STATUS + " TEXT" + ")";

    // Creating Tables
    private static final String CREATE_TABLE_NEW_1 = "CREATE TABLE " + TABLE_NEW_1 + "("
            + TABLE_NEW_1_1 + " INTEGER PRIMARY KEY,"
            + TABLE_NEW_1_2 + " TEXT,"
            + TABLE_NEW_1_3 + " TEXT,"
            + TABLE_NEW_1_4 + " TEXT,"
            + TABLE_NEW_1_5 + " TEXT,"
            + TABLE_NEW_1_6 + " TEXT,"
            + TABLE_NEW_1_7 + " TEXT,"
            + TABLE_NEW_1_8 + " TEXT,"
            + TABLE_NEW_1_9 + " TEXT,"
            + TABLE_NEW_1_10 + " TEXT" + ")";

    //========================================
    // Creating Tables
    private static final String CREATE_TABLE_NEW_2 = "CREATE TABLE " + TABLE_NEW_2 + "("
            + TABLE_NEW_2_1 + " INTEGER PRIMARY KEY,"
            + TABLE_NEW_2_2 + " TEXT,"
            + TABLE_NEW_2_3 + " TEXT,"
            + TABLE_NEW_2_4 + " TEXT,"
            + TABLE_NEW_2_5 + " TEXT,"
            + TABLE_NEW_2_6 + " TEXT,"
            + TABLE_NEW_2_7 + " TEXT,"
            + TABLE_NEW_2_8 + " TEXT,"
            + TABLE_NEW_2_9 + " TEXT,"
            + TABLE_NEW_2_10 + " TEXT" + ")";

    // Creating Tables
    private static final String CREATE_TABLE_NEW_3 = "CREATE TABLE " + TABLE_NEW_3 + "("
            + TABLE_NEW_3_1 + " INTEGER PRIMARY KEY,"
            + TABLE_NEW_3_2 + " TEXT,"
            + TABLE_NEW_3_3 + " TEXT,"
            + TABLE_NEW_3_4 + " TEXT,"
            + TABLE_NEW_3_5 + " TEXT,"
            + TABLE_NEW_3_6 + " TEXT,"
            + TABLE_NEW_3_7 + " TEXT,"
            + TABLE_NEW_3_8 + " TEXT,"
            + TABLE_NEW_3_9 + " TEXT,"
            + TABLE_NEW_3_10 + " TEXT" + ")";

    // Creating Tables
    private static final String CREATE_TABLE_NEW_4 = "CREATE TABLE " + TABLE_NEW_4 + "("
            + TABLE_NEW_4_1 + " INTEGER PRIMARY KEY,"
            + TABLE_NEW_4_2 + " TEXT,"
            + TABLE_NEW_4_3 + " TEXT,"
            + TABLE_NEW_4_4 + " TEXT,"
            + TABLE_NEW_4_5 + " TEXT,"
            + TABLE_NEW_4_6 + " TEXT,"
            + TABLE_NEW_4_7 + " TEXT,"
            + TABLE_NEW_4_8 + " TEXT,"
            + TABLE_NEW_4_9 + " TEXT,"
            + TABLE_NEW_4_10 + " TEXT" + ")";

    // Creating Tables
    private static final String CREATE_TABLE_NEW_5 = "CREATE TABLE " + TABLE_NEW_5 + "("
            + TABLE_NEW_5_1 + " INTEGER PRIMARY KEY,"
            + TABLE_NEW_5_2 + " TEXT,"
            + TABLE_NEW_5_3 + " TEXT,"
            + TABLE_NEW_5_4 + " TEXT,"
            + TABLE_NEW_5_5 + " TEXT,"
            + TABLE_NEW_5_6 + " TEXT,"
            + TABLE_NEW_5_7 + " TEXT,"
            + TABLE_NEW_5_8 + " TEXT,"
            + TABLE_NEW_5_9 + " TEXT,"
            + TABLE_NEW_5_10 + " TEXT" + ")";

    private static final String CREATE_TABLE_FAVOURITE = "CREATE TABLE " + TABLE_FAVOURITE + "("
            + KEY_F_ID + " INTEGER PRIMARY KEY,"
            + KEY_F_USER_ID + " TEXT,"
            + KEY_F_NAME + " TEXT,"
            + KEY_F_IMAGE + " TEXT,"
            + KEY_F_MET + " TEXT" + ")";

    private static final String CREATE_USER = "CREATE TABLE " + TABLE_USER + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_USERNAME_ID + " TEXT,"
            + KEY_USER_FNAME + " TEXT,"
            + KEY_USER_LNAME + " TEXT,"
            + KEY_USER_IMAGE + " TEXT,"
            + KEY_USER_EMAIL + " TEXT,"
            + KEY_USER_GENDER + " TEXT,"
            + KEY_USER_BIRTHDAY + " TEXT,"
            + KEY_USER_DATE + " TEXT,"
            + KEY_USER_LASTMODIFIED + " TEXT,"
            + KEY_USER_PASSWORD + " TEXT,"
            + KEY_USER_BODYAGE + " TEXT" + ")";


    private static final String CREATE_TABLE_STEPRECODER = "CREATE TABLE "
            + TABLE_STEPRECODER + "(" + KEY_REC_ID + " INTEGER PRIMARY KEY," + KEY_STEPS
            + " FLOAT," + KEY_METS + " FLOAT," + KEY_TIMESTAMP
            + " TEXT" + ")";

    private static final String CREATE_TABLE_TODAY_MAIN_SUMMERY = "CREATE TABLE " + TABLE_TODAY_MAIN_SUMMERY + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_USER_ID + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_STEP_TOT + " NUMERIC,"
            + KEY_RUN_TOT + " NUMERIC,"
            + KEY_JUMP_TOT + " NUMERIC,"
            + KEY_MET_TOT + " NUMERIC,"
            + KEY_ENGY_PERSENT_TOT + " NUMERIC,"
            + KEY_CAL_TOT + " NUMERIC" + ")";

    private static final String CREATE_TABLE_DAILY_WORKOUT_SUMMERY = "CREATE TABLE " + TABLE_DAILY_WORKOUT_SUMMERY + "("
            + KEY_W_ORK_ID + " INTEGER PRIMARY KEY,"
            + KEY_W_USER_ID + " TEXT,"
            + KEY_W_TYPE + " TEXT,"
            + KEY_W_STEP + " NUMERIC,"
            + KEY_W_RUN + " NUMERIC,"
            + KEY_W_ENERGY + " NUMERIC,"
            + KEY_W_CAL + " NUMERIC,"
            + KEY_W_DISTANCE + " NUMERIC,"
            + KEY_W_START_TIME + " NUMERIC,"
            + KEY_W_END_TIME + " NUMERIC,"
            + KEY_W_DATE + " TEXT,"
            + KEY_W_UPDATE_TYPE + " NUMERIC,"
            + KEY_W_WORKOUT_TYPE + " TEXT,"
            + KEY_W_SERVER_UPDATED_STATUS + " TEXT" + ")";


    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        System.out.println("&&&&&&&&&&&&&&onCreate&&&&&&&&&&&&&&&&&&&&&&&&&");
        try {
            db.execSQL(CREATE_TABLE_FAVOURITE);
            db.execSQL(CREATE_TABLE_STEPRECODER);
            db.execSQL(CREATE_TABLE_TODAY_MAIN_SUMMERY);
            db.execSQL(CREATE_TABLE_DAILY_WORKOUT_SUMMERY);
            db.execSQL(CREATE_USER);
            db.execSQL(CREATE_TABLE_SETTINGS);
            db.execSQL(CREATE_TABLE_IMAGE);
            db.execSQL(CREATE_TABLE_NEW_1);
            db.execSQL(CREATE_TABLE_NEW_2);
            db.execSQL(CREATE_TABLE_NEW_3);
            db.execSQL(CREATE_TABLE_NEW_4);
            db.execSQL(CREATE_TABLE_NEW_5);
        }catch(Exception e){
            System.out.println("&&&&&&&&&Exception&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+e);
        }
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&& NEW DB CREATED &&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&& NEW DB CREATED &&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&& NEW DB CREATED &&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&& NEW DB CREATED &&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&& NEW DB CREATED &&&&&&&&&&&&&&&&&&&&&&&&&&&&&");


    }

    // Upgrading database

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("&&&&&&&&&&onUpgrade&&&&&&&&&&&&&&&&&");
        onCreate(db);
    }

    //    User user2 = null;
//    SQLiteDatabase db = this.getReadableDatabase();
//
//    //   SELECT * FROM tablename ORDER BY column DESC LIMIT 1;
//    Cursor cursor = db.query(TABLE_USER, new String[]{KEY_USERNAME_ID, KEY_USER_FNAME, KEY_USER_LNAME,
//                    KEY_USER_IMAGE, KEY_USER_EMAIL, KEY_USER_GENDER,
//                    KEY_USER_BIRTHDAY,
//                    KEY_USER_DATE, KEY_USER_LASTMODIFIED, KEY_USER_PASSWORD,KEY_USER_BODYAGE},null,
//            null, null, null, null);
//    if (cursor.getCount() <= 0) {
    // Deleting single contact
    public void deleteCurrentUser(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_USERNAME_ID + " = ?",
                new String[]{String.valueOf(userId)});
        db.close();
    }


    public void updateUserStatusToDB(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME_ID, user.getUsernameid());
        values.put(KEY_USER_FNAME, user.getFname());
        values.put(KEY_USER_LNAME, user.getLname());
        values.put(KEY_USER_IMAGE, user.getImagepath());
        values.put(KEY_USER_EMAIL, user.getEmail());
        values.put(KEY_USER_GENDER, user.getGender());
        values.put(KEY_USER_BIRTHDAY, user.getBirthday());
        values.put(KEY_USER_DATE, user.getDate());
        values.put(KEY_USER_LASTMODIFIED, user.getDatemodified());
        values.put(KEY_USER_PASSWORD, user.getPassword());
        // Inserting Row
        db.update(TABLE_USER, values, KEY_USERNAME_ID + " = ?",
                new String[]{String.valueOf(user.getUsernameid())});

        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }
    //============================================================================



    public int updateTodaySummeryMets(String userid, String date, float mets, float cals) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_MET_TOT, mets);
        cv.put(KEY_CAL_TOT, cals);

        int returnStatus = db.update(TABLE_TODAY_MAIN_SUMMERY, cv,
                KEY_USER_ID + "=? AND " + KEY_DATE + "=?",
                new String[]{userid, date});


        //  int returnStatus= db.update(TABLE_DAILY_WORKOUT_SUMMERY, cv, "userid="+userid, null);
        // int returnStatus= db.update(TABLE_TODAY_MAIN_SUMMERY, cv, "userid=" + userid +" AND date="+date , null);
        return returnStatus;
    }

    public SettingEntity getSettingsForHistoryCalculation(String timestamp) {
        SettingEntity contact = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SETTINGS, new String[]
                        {KEY_S_USER_ID, KEY_S_STEP_GOAL, KEY_S_ENERGY_GOAL, KEY_S_CALERY_GOAL, KEY_S_HEIGHT_GOAL, KEY_S_WEIGHT_GOAL,
                                KEY_S_WAIST_GOAL, KEY_S_BPM_GOAL,
                                KEY_S_SETTING_DATE, KEY_S_UPDATED_STATUS
                        }, KEY_S_SETTING_DATE + "=?",
                new String[]{timestamp}, null, null, null, null);

        if (cursor.getCount() <= 0) {
        } else {
            cursor.moveToLast();

            contact = new SettingEntity(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9));
        }
        // return contact
        return contact;
    }

    // Getting single SettingEntity


    public User getUser(String email) {
        System.out.println("============================email_db=======" + email);
        User user = null;
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.query(TABLE_USER, new String[]{
                        KEY_USERNAME_ID, KEY_USER_FNAME, KEY_USER_LNAME, KEY_USER_IMAGE, KEY_USER_EMAIL, KEY_USER_GENDER,
                        KEY_USER_BIRTHDAY,
                        KEY_USER_DATE, KEY_USER_LASTMODIFIED, KEY_USER_PASSWORD,KEY_USER_BODYAGE},
                KEY_USER_EMAIL + "=?",
                new String[]{email}, null, null, null, null);

        if (cursor.getCount() <= 0) {
            //  Redirect to Online Login------------------------
            cursor.close();
        } else {
            cursor.moveToFirst();

            user = new User(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8), cursor.getString(9),
                    cursor.getString(10));
        }
        cursor.close();
        return user;

    }

    public SettingEntity getUserSettingsContact(String uid, String date) {
        SettingEntity contact = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SETTINGS, new String[]
                        {KEY_S_USER_ID, KEY_S_STEP_GOAL, KEY_S_ENERGY_GOAL, KEY_S_CALERY_GOAL, KEY_S_HEIGHT_GOAL, KEY_S_WEIGHT_GOAL
                                , KEY_S_WAIST_GOAL, KEY_S_BPM_GOAL,
                                KEY_S_SETTING_DATE, KEY_S_UPDATED_STATUS
                        },
                KEY_S_USER_ID + "=? AND " + KEY_S_SETTING_DATE + "=?",
                new String[]{uid, date}, null, null, null, null);

        if (cursor.getCount() <= 0) {
        } else {
            cursor.moveToLast();

            contact = new SettingEntity(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9));
        }
        // return contact
        return contact;
    }

    public SettingEntity getUserSettingsForUserId(String userid) {
        SettingEntity contact = null;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SETTINGS, new String[]
                        {KEY_S_USER_ID, KEY_S_STEP_GOAL, KEY_S_ENERGY_GOAL, KEY_S_CALERY_GOAL, KEY_S_HEIGHT_GOAL, KEY_S_WEIGHT_GOAL,
                                KEY_S_WAIST_GOAL, KEY_S_BPM_GOAL,
                                KEY_S_SETTING_DATE, KEY_S_UPDATED_STATUS
                        }, KEY_S_UPDATED_STATUS + "=?",
                new String[]{String.valueOf("0")}, null, null, null, null);

        if (cursor.getCount() <= 0) {
        } else {
            cursor.moveToLast();

            contact = new SettingEntity(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9));
        }
        // return contact
        return contact;
    }

    public SettingEntity getUserSettingsForDateLastRaw(String date) {
        SettingEntity contact = null;
        SQLiteDatabase db = this.getReadableDatabase();



        Cursor cursor = db.rawQuery("select * from " + TABLE_SETTINGS, null);

//        Cursor cursor = db.query(TABLE_SETTINGS, new String[]
//                        {KEY_S_USER_ID, KEY_S_STEP_GOAL, KEY_S_ENERGY_GOAL, KEY_S_CALERY_GOAL, KEY_S_HEIGHT_GOAL, KEY_S_WEIGHT_GOAL,
//                                KEY_S_WAIST_GOAL, KEY_S_BPM_GOAL,
//                                KEY_S_SETTING_DATE, KEY_S_UPDATED_STATUS
//                        }, KEY_S_SETTING_DATE + "=?",
//                new String[]{date}, null, null, null, null);

        if (cursor.getCount() <= 0) {

        }
        if (cursor.getCount() == 1) {

            cursor.moveToLast();

            String useridd=cursor.getString(0);
            String userid=cursor.getString(1);
            String stepGoal=cursor.getString(2);
            String calerygoal=cursor.getString(3);
            String metsgoal=cursor.getString(4);
            String userHeight=cursor.getString(5);
            String userWeight=cursor.getString(6);
            String bpmgoal=cursor.getString(7);
            String updateddate=cursor.getString(8);
            String updatedstatus=cursor.getString(9);

            contact = new SettingEntity(
                    userid,
                    stepGoal,
                    metsgoal,
                    calerygoal,
                    userHeight,
                    userWeight,
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(5),
                    cursor.getString(9));
        }
        if (cursor.getCount() > 1) {
            cursor.moveToLast();

            String useridd=cursor.getString(0);
            String userid=cursor.getString(1);
            String stepGoal=cursor.getString(2);
            String calerygoal=cursor.getString(3);
            String metsgoal=cursor.getString(4);
            String userHeight=cursor.getString(5);
            String userWeight=cursor.getString(6);
            String bpmgoal=cursor.getString(7);
            String updateddate=cursor.getString(8);
            String updatedstatus=cursor.getString(9);

            contact = new SettingEntity(
                    userid,
                    stepGoal,
                    metsgoal,
                    calerygoal,
                    userHeight,
                    userWeight,
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(5),
                    cursor.getString(9));
        }
        // return contact
        return contact;
    }

//    public SettingEntity(
//                        String userid,
//                         String stepgoal,
//                         String energygoal,
//                         String calerygoal,
//                         String heightgoal,
//                         String weightgoal,
//                         String waist,
//                         String bpmgoal,
//                         String updateddate,
//                         String updatedstatus)
// {
//


    public SettingEntity getUserSettingsForDate(String date) {
        SettingEntity contact = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SETTINGS, new String[]
                        {KEY_S_USER_ID, KEY_S_STEP_GOAL, KEY_S_ENERGY_GOAL, KEY_S_CALERY_GOAL, KEY_S_HEIGHT_GOAL, KEY_S_WEIGHT_GOAL,
                                KEY_S_WAIST_GOAL, KEY_S_BPM_GOAL,
                                KEY_S_SETTING_DATE, KEY_S_UPDATED_STATUS
                        }, KEY_S_SETTING_DATE + "=?",
                new String[]{date}, null, null, null, null);

        if (cursor.getCount() <= 0) {

        }
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();

            contact = new SettingEntity(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9));
        }
        if (cursor.getCount() > 1) {
            cursor.moveToLast();

            contact = new SettingEntity(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9));
        }
        // return contact
        return contact;
    }


    public void addInserOrUpdate_SettingEntityToDB(SettingEntity user) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = 0;
        Date dae = new Date();

        String de = sdf.format(dae);

        Cursor cursor = db.query(TABLE_SETTINGS, new String[]
                        {KEY_S_USER_ID, KEY_S_STEP_GOAL, KEY_S_ENERGY_GOAL, KEY_S_CALERY_GOAL, KEY_S_HEIGHT_GOAL,
                                KEY_S_WEIGHT_GOAL, KEY_S_WAIST_GOAL, KEY_S_BPM_GOAL,
                                KEY_S_SETTING_DATE, KEY_S_UPDATED_STATUS
                        }, KEY_S_SETTING_DATE + "=?",
                new String[]{de}, null, null, null, null);
        if (cursor.getCount() <= 0) {

            ContentValues values = new ContentValues();
            values.put(KEY_S_USER_ID, user.getUserid());
            values.put(KEY_S_STEP_GOAL, user.getStepgoal());
            values.put(KEY_S_ENERGY_GOAL, user.getEnergygoal());
            values.put(KEY_S_CALERY_GOAL, user.getCalerygoal());
            values.put(KEY_S_HEIGHT_GOAL, user.getHeightgoal());
            values.put(KEY_S_WEIGHT_GOAL, user.getWeightgoal());
            values.put(KEY_S_WAIST_GOAL, user.getWaist());

            values.put(KEY_S_BPM_GOAL, user.getBpmgoal());
            values.put(KEY_S_SETTING_DATE, de);
            values.put(KEY_S_UPDATED_STATUS, user.getUpdatedstatus());

            // Inserting Row
            db.insert(TABLE_SETTINGS, null, values);
            db.close(); // Closing database connection
        } else {
            ContentValues values = new ContentValues();
            values.put(KEY_S_USER_ID, user.getUserid());
            values.put(KEY_S_STEP_GOAL, user.getStepgoal());
            values.put(KEY_S_ENERGY_GOAL, user.getEnergygoal());
            values.put(KEY_S_CALERY_GOAL, user.getCalerygoal());
            values.put(KEY_S_HEIGHT_GOAL, user.getHeightgoal());
            values.put(KEY_S_WEIGHT_GOAL, user.getWeightgoal());
            values.put(KEY_S_WAIST_GOAL, user.getWaist());

            values.put(KEY_S_BPM_GOAL, user.getBpmgoal());
            values.put(KEY_S_SETTING_DATE, de);
            values.put(KEY_S_UPDATED_STATUS, user.getUpdatedstatus());

            result = db.update(TABLE_SETTINGS, values, KEY_S_SETTING_DATE + " = ?",
                    new String[]{user.getUpdateddate()});
        }
    }

    public void addSettingEntityToDB(SettingEntity user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_S_USER_ID, user.getUserid());
        values.put(KEY_S_STEP_GOAL, user.getStepgoal());
        values.put(KEY_S_ENERGY_GOAL, user.getEnergygoal());
        values.put(KEY_S_CALERY_GOAL, user.getCalerygoal());
        values.put(KEY_S_HEIGHT_GOAL, user.getHeightgoal());
        values.put(KEY_S_WEIGHT_GOAL, user.getWeightgoal());
        values.put(KEY_S_WAIST_GOAL, user.getWaist());

        values.put(KEY_S_BPM_GOAL, user.getBpmgoal());
        values.put(KEY_S_SETTING_DATE, user.getUpdateddate());
        values.put(KEY_S_UPDATED_STATUS, user.getUpdatedstatus());

        // Inserting Row
        db.insert(TABLE_SETTINGS, null, values);
        db.close(); // Closing database connection
    }

    public todaysummery getSelectedDaySummeryFromDB(String dat) {
        todaysummery contactt = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TODAY_MAIN_SUMMERY, new String[]{KEY_ID, KEY_USER_ID, KEY_DATE,
                        KEY_STEP_TOT, KEY_RUN_TOT, KEY_JUMP_TOT, KEY_MET_TOT, KEY_ENGY_PERSENT_TOT, KEY_CAL_TOT},
                KEY_DATE + "=?",
                new String[]{dat}, null, null, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("------------------No Data for Selected Day--------------------");

        } else {
            System.out.println("------------------Have Data for Selected Day--------------------");
            cursor.moveToFirst();

            contactt = new todaysummery(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getFloat(6),
                    cursor.getFloat(7),
                    cursor.getFloat(8));
            // return contact

        }
        return contactt;
    }




    public ArrayList<todaysummery> getThisWeekStepsFromDB(String date) {
        todaysummery contact = null;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<todaysummery> list = new ArrayList<todaysummery>();


        Cursor cursor = db.query(TABLE_TODAY_MAIN_SUMMERY, new String[]{
                        KEY_ID,
                        KEY_USER_ID,
                        KEY_DATE,
                        KEY_STEP_TOT,
                        KEY_RUN_TOT,
                        KEY_JUMP_TOT,
                        KEY_MET_TOT,
                        KEY_CAL_TOT}, KEY_USER_ID + "=?",
                new String[]{date}, null, null, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("------------------Workouts for Selected Day--------------------");
        } else {

            int i = 0;

            while (cursor.moveToNext()) {
                System.out.println("------------------steps for week loaded-----------------");
                contact = new todaysummery(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getFloat(6),
                        cursor.getFloat(7),
                        cursor.getFloat(8));
                list.add(contact);
            }
            cursor.moveToFirst();

        }
        return list;
    }
    public void addProfileImageToDB(String uid, String name, String image) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = 0;

        if (uid != null) {

            Cursor cursor = db.query(TABLE_PROFILE_IMAGE, new String[]{
                            KEY_NAME_PROFILE_ID, KEY_NAME_PROFILE_IMAGE, KEY_IMAGE_PROFILE_IMAGE},
                    KEY_NAME_PROFILE_ID + "=?",
                    new String[]{uid}, null, null, null, null);

            if (cursor.getCount() <= 0) {
                ContentValues values = new ContentValues();
                values.put(KEY_NAME_PROFILE_ID, uid);
                values.put(KEY_NAME_PROFILE_IMAGE, name);
                values.put(KEY_IMAGE_PROFILE_IMAGE, image);

                db.insert(TABLE_PROFILE_IMAGE, null, values);
            } else {
                ContentValues value = new ContentValues();
                value.put(KEY_NAME_PROFILE_ID, uid);
                value.put(KEY_NAME_PROFILE_IMAGE, name);
                value.put(KEY_IMAGE_PROFILE_IMAGE, image);

                result = db.update(TABLE_PROFILE_IMAGE, value, null, null);
            }
        }
    }

    public String getProfileImageFromDB(String userid) {
        System.out.println("------------------USER ID FOR GET DATA--------------------" + userid);
        String imagePath = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILE_IMAGE, new String[]{
                        KEY_NAME_PROFILE_ID, KEY_NAME_PROFILE_IMAGE, KEY_IMAGE_PROFILE_IMAGE},
                KEY_NAME_PROFILE_ID + "=?",
                new String[]{userid}, null, null, null, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("------------------No User Profile details--------------------");
        } else {
            cursor.moveToLast();
            System.out.println("------------------Have User Profile details--------------------");
            imagePath = cursor.getString(1);
            // return contact
        }
        return imagePath;
    }


    public void addUserToDBOld(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME_ID, user.getUsernameid());
        values.put(KEY_USER_FNAME, user.getFname());
        values.put(KEY_USER_LNAME, user.getLname());
        values.put(KEY_USER_IMAGE, user.getImagepath());
        values.put(KEY_USER_EMAIL, user.getEmail());
        values.put(KEY_USER_GENDER, user.getGender());
        values.put(KEY_USER_BIRTHDAY, user.getBirthday());
        values.put(KEY_USER_DATE, user.getDate());
        values.put(KEY_USER_LASTMODIFIED, user.getDatemodified());
        values.put(KEY_USER_PASSWORD, user.getPassword());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }
    public void deleteUser(String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_USERNAME_ID + " = ?",
                new String[]{userid});
        db.close();
    }
    public User getUserProfileDetailsForUserId() {
        // userid="d0d6ca28-776f-bd8e-f110-56c5925546fa";
        User user2 = null;
        SQLiteDatabase db = this.getReadableDatabase();

        //   SELECT * FROM tablename ORDER BY column DESC LIMIT 1;
        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_USERNAME_ID, KEY_USER_FNAME, KEY_USER_LNAME,
                        KEY_USER_IMAGE, KEY_USER_EMAIL, KEY_USER_GENDER,
                        KEY_USER_BIRTHDAY,
                        KEY_USER_DATE, KEY_USER_LASTMODIFIED, KEY_USER_PASSWORD,KEY_USER_BODYAGE},null,
                null, null, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("------------------No User Profile details--------------------");
        } else {
            cursor.moveToLast();
            System.out.println("------------------Have User Profile details--------------------");
            user2 = new User(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8), cursor.getString(9),
                    cursor.getString(10));
            // return contact
        }
        return user2;
    }
    public User getUserProfileDetailsForUserId(String userid) {
        // userid="d0d6ca28-776f-bd8e-f110-56c5925546fa";
        User user2 = null;
        SQLiteDatabase db = this.getReadableDatabase();

        //   SELECT * FROM tablename ORDER BY column DESC LIMIT 1;
        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_USERNAME_ID, KEY_USER_FNAME, KEY_USER_LNAME,
                        KEY_USER_IMAGE, KEY_USER_EMAIL, KEY_USER_GENDER,
                        KEY_USER_BIRTHDAY,
                        KEY_USER_DATE, KEY_USER_LASTMODIFIED, KEY_USER_PASSWORD,KEY_USER_BODYAGE}, KEY_USERNAME_ID + "=?",
                new String[]{userid},
                null, null, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("------------------No User Profile details--------------------");
        } else {
            cursor.moveToLast();
            System.out.println("------------------Have User Profile details--------------------");
            user2 = new User(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8), cursor.getString(9),
                    cursor.getString(10));
            // return contact
        }
        return user2;
    }
    // Getting single contact
    public User getUserProfileDetails() {
        User user2 = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_USERNAME_ID, KEY_USER_FNAME, KEY_USER_LNAME,
                        KEY_USER_IMAGE, KEY_USER_EMAIL, KEY_USER_GENDER,
                        KEY_USER_BIRTHDAY,
                        KEY_USER_DATE, KEY_USER_LASTMODIFIED, KEY_USER_PASSWORD,KEY_USER_BODYAGE}, null,
                null, null, null, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("------------------No User Profile details--------------------");
        } else {
            cursor.moveToFirst();
            System.out.println("------------------Have User Profile details--------------------");
            user2 = new User(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),   cursor.getString(9),
                    cursor.getString(10));
            // return contact
        }
        return user2;
    }


    public void addUserToDB(User user) {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[]{
                        KEY_USERNAME_ID, KEY_USER_FNAME, KEY_USER_LNAME, KEY_USER_IMAGE, KEY_USER_EMAIL, KEY_USER_GENDER,
                        KEY_USER_BIRTHDAY,
                        KEY_USER_DATE, KEY_USER_LASTMODIFIED, KEY_USER_PASSWORD,KEY_USER_BODYAGE},
                KEY_USER_EMAIL + "=?",
                new String[]{user.getEmail()}, null, null, null, null);


        if (cursor.getCount() <= 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_USERNAME_ID, user.getUsernameid());
            values.put(KEY_USER_FNAME, user.getFname());
            values.put(KEY_USER_LNAME, user.getLname());
            values.put(KEY_USER_IMAGE, user.getImagepath());
            values.put(KEY_USER_EMAIL, user.getEmail());
            values.put(KEY_USER_GENDER, user.getGender());
            values.put(KEY_USER_BIRTHDAY, user.getBirthday());
            values.put(KEY_USER_DATE, user.getDate());
            values.put(KEY_USER_LASTMODIFIED, user.getDatemodified());
            values.put(KEY_USER_PASSWORD, user.getPassword());
            values.put(KEY_USER_BODYAGE, user.getBodyage());
            Log.d("Profile added ", "---------------------------------------------------Added New User successfully");
            db.insert(TABLE_USER, null, values);

        } else {

            ContentValues values = new ContentValues();
            values.put(KEY_USERNAME_ID, user.getUsernameid());
            values.put(KEY_USER_FNAME, user.getFname());
            values.put(KEY_USER_LNAME, user.getLname());
            values.put(KEY_USER_IMAGE, user.getImagepath());
            values.put(KEY_USER_EMAIL, user.getEmail());
            values.put(KEY_USER_GENDER, user.getGender());
            values.put(KEY_USER_BIRTHDAY, user.getBirthday());
            values.put(KEY_USER_DATE, user.getDate());
            values.put(KEY_USER_LASTMODIFIED, user.getDatemodified());
            values.put(KEY_USER_PASSWORD, user.getPassword());
            values.put(KEY_USER_BODYAGE, user.getBodyage());
            // updating row
            result = db.update(TABLE_USER, values, KEY_USER_EMAIL + " = ?",
                    new String[]{String.valueOf(user.getEmail())});

            System.out.println("----------Profile Updated --------"+result);
        }

        //===========================


    }


    public int updateTodaySummeryDontUse(todaysummery summery) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STEP_TOT, summery.getStep_tot());
        values.put(KEY_RUN_TOT, summery.getRun_tot());
        values.put(KEY_JUMP_TOT, summery.getJump_tot());
        values.put(KEY_MET_TOT, summery.getMet_tot());
        values.put(KEY_ENGY_PERSENT_TOT, summery.getEnegy_persent());
        values.put(KEY_CAL_TOT, summery.getCal_tot());
        // updating row
        return db.update(TABLE_TODAY_MAIN_SUMMERY, values, KEY_ID + " = ?",
                new String[]{String.valueOf(summery.getId())});
    }


    public int updateDefaultSummeryDuringWorkoutAyuboNew(todaysummery summery) {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Date dae = new Date();
        String de = sdf.format(dae);
      //  TABLE_DAILY_WORKOUT_SUMMERY
        Cursor cursor = db.query(TABLE_TODAY_MAIN_SUMMERY, new String[]{KEY_ID, KEY_USER_ID, KEY_DATE,
                        KEY_STEP_TOT, KEY_RUN_TOT, KEY_JUMP_TOT, KEY_MET_TOT, KEY_ENGY_PERSENT_TOT, KEY_CAL_TOT}, KEY_DATE + "=?",
                new String[]{de}, null, null, null, null);

        if (cursor.getCount() <= 0) {
            System.out.println("-----------------No data for Default Activities--------------------");

        } else {

            ContentValues values = new ContentValues();
            values.put(KEY_DATE, summery.getDate());
            values.put(KEY_USER_ID, summery.getUserid());
            values.put(KEY_STEP_TOT, summery.getStep_tot());
            values.put(KEY_RUN_TOT, summery.getRun_tot());
            values.put(KEY_JUMP_TOT, summery.getJump_tot());
            values.put(KEY_MET_TOT, summery.getMet_tot());
            values.put(KEY_ENGY_PERSENT_TOT, summery.getEnegy_persent());
            values.put(KEY_CAL_TOT, summery.getCal_tot());
            // updating row
            result = db.update(TABLE_TODAY_MAIN_SUMMERY, values, KEY_DATE + " = ?",
                    new String[]{String.valueOf(summery.getDate())});
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("------------------Updated Default Summery During Workout 2--------------------");
        }


        return result;
    }

    public int updateDefaultSummeryDuringWorkout2(todaysummery summery) {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Date dae = new Date();
        String de = sdf.format(dae);

        Cursor cursor = db.query(TABLE_TODAY_MAIN_SUMMERY, new String[]{KEY_ID, KEY_USER_ID, KEY_DATE,
                        KEY_STEP_TOT, KEY_RUN_TOT, KEY_JUMP_TOT, KEY_MET_TOT, KEY_ENGY_PERSENT_TOT, KEY_CAL_TOT}, KEY_DATE + "=?",
                new String[]{de}, null, null, null, null);

        if (cursor.getCount() <= 0) {
            System.out.println("-----------------No data for Default Activities--------------------");

        } else {

            ContentValues values = new ContentValues();
            values.put(KEY_DATE, summery.getDate());
            values.put(KEY_USER_ID, summery.getUserid());
            values.put(KEY_STEP_TOT, summery.getStep_tot());
            values.put(KEY_RUN_TOT, summery.getRun_tot());
            values.put(KEY_JUMP_TOT, summery.getJump_tot());
            values.put(KEY_MET_TOT, summery.getMet_tot());
            values.put(KEY_ENGY_PERSENT_TOT, summery.getEnegy_persent());
            values.put(KEY_CAL_TOT, summery.getCal_tot());
            // updating row

            try {
                result = db.update(TABLE_TODAY_MAIN_SUMMERY, values, KEY_DATE + " = ?",
                        new String[]{String.valueOf(summery.getDate())});
            }catch(Exception e){
                e.printStackTrace();
            }
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("------------------Updated Default Summery During Workout 2--------------------");
        }


        return result;
    }


    public int updateDefaultSummeryDuringWorkout(todaysummery defaultsumery) {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, defaultsumery.getId());
        values.put(KEY_DATE, defaultsumery.getDate());
        values.put(KEY_STEP_TOT, defaultsumery.getStep_tot());
        values.put(KEY_RUN_TOT, defaultsumery.getRun_tot());
        values.put(KEY_JUMP_TOT, defaultsumery.getJump_tot());
        values.put(KEY_MET_TOT, defaultsumery.getMet_tot());
        values.put(KEY_ENGY_PERSENT_TOT, defaultsumery.getEnegy_persent());
        values.put(KEY_CAL_TOT, defaultsumery.getCal_tot());

        result = db.update(TABLE_TODAY_MAIN_SUMMERY, values,
                KEY_DATE + " = ?",
                new String[]{defaultsumery.getDate()});


        return result;
    }


    // Adding new contact
    public void addContact(FavouriteActivity contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_F_USER_ID, contact.getUserid());
        values.put(KEY_F_NAME, contact.getName()); // Contact Name
        values.put(KEY_F_IMAGE, contact.getImage()); // Contact Phone Number
        values.put(KEY_F_MET, contact.getMet());
        // Inserting Row
        db.insert(TABLE_FAVOURITE, null, values);
        db.close(); // Closing database connection
    }

    // Adding new contact
    public void addStepCount(StepRecoder recode) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TIMESTAMP, recode.getTimestamp());
        values.put(KEY_STEPS, recode.getLast_minit_steps());
        values.put(KEY_METS, recode.getMetVlaues());
        // Inserting Row
        db.insert(TABLE_STEPRECODER, null, values);
        db.close(); // Closing database connection
    }

    public void markedAsServerUpdated(String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_W_SERVER_UPDATED_STATUS, "1");
        try {
            db.update(TABLE_DAILY_WORKOUT_SUMMERY, cv, "_id=" + userid, null);

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }
    public ArrayList<workoutEntity> getWorkoutDataListForServerUpdating(String uid) {


        workoutEntity contact = null;
        String status = "0";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<workoutEntity> list = new ArrayList<workoutEntity>();

        Cursor cursor = db.query(TABLE_DAILY_WORKOUT_SUMMERY, new String[]{
                        KEY_W_ORK_ID, KEY_W_USER_ID,
                        KEY_W_TYPE,
                        KEY_W_STEP,
                        KEY_W_RUN,
                        KEY_W_ENERGY,
                        KEY_W_CAL,
                        KEY_W_DISTANCE,
                        KEY_W_START_TIME,
                        KEY_W_END_TIME,
                        KEY_W_DATE,
                        KEY_W_UPDATE_TYPE, KEY_W_WORKOUT_TYPE, KEY_W_SERVER_UPDATED_STATUS},
                KEY_W_SERVER_UPDATED_STATUS + "=? AND " + KEY_W_USER_ID + "=?",
                new String[]{status, uid}, null, null, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("------------------No Data for Workout data for Selected Day--------------------");
        } else {
            System.out.println("------------------Have Data for Workout -------------------------------------");
            int i = 0;
            while (cursor.moveToNext()) {

                contact = new workoutEntity(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getFloat(5),
                        cursor.getFloat(6),
                        cursor.getInt(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getInt(11),
                        cursor.getString(12),
                        cursor.getString(13));
                list.add(contact);
            }
            cursor.moveToFirst();

        }
        return list;
    }



    public void addWorkoutRecord(workoutEntity workout) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_W_USER_ID, workout.getUid());

        values.put(KEY_W_TYPE, workout.getType());
        values.put(KEY_W_STEP, workout.getSteps());
        values.put(KEY_W_RUN, workout.getRuns());
        values.put(KEY_W_ENERGY, workout.getEnergy());
        values.put(KEY_W_CAL, workout.getCal());
        values.put(KEY_W_DISTANCE, workout.getDistance());
        values.put(KEY_W_START_TIME, workout.getStart_time());
        values.put(KEY_W_END_TIME, workout.getEnd_time());
        values.put(KEY_W_DATE, workout.getDate());
        values.put(KEY_W_UPDATE_TYPE, workout.getUpdate_type());
        values.put(KEY_W_WORKOUT_TYPE, workout.getDuration());
        values.put(KEY_W_SERVER_UPDATED_STATUS, workout.getStatus());
        try {
            db.insert(TABLE_DAILY_WORKOUT_SUMMERY, null, values);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void addWorkoutRecord2(workoutEntity workout) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_W_USER_ID, workout.getUid());
        values.put(KEY_W_TYPE, workout.getType());
        values.put(KEY_W_STEP, workout.getSteps());
        values.put(KEY_W_RUN, workout.getRuns());
        values.put(KEY_W_ENERGY, workout.getEnergy());
        values.put(KEY_W_CAL, workout.getCal());
        values.put(KEY_W_DISTANCE, workout.getDistance());
        values.put(KEY_W_START_TIME, workout.getStart_time());
        values.put(KEY_W_END_TIME, workout.getEnd_time());
        values.put(KEY_W_DATE, workout.getDate());
        values.put(KEY_W_UPDATE_TYPE, workout.getUpdate_type());
        values.put(KEY_W_WORKOUT_TYPE, workout.getWorkout_type());
        db.insert(TABLE_DAILY_WORKOUT_SUMMERY2, null, values);
    }

    public int updateWorkoutTable2(workoutEntity workout) {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_DAILY_WORKOUT_SUMMERY, new String[]{
                        KEY_W_ORK_ID, KEY_W_USER_ID, KEY_W_TYPE, KEY_W_STEP, KEY_W_RUN, KEY_W_ENERGY, KEY_W_CAL, KEY_W_DISTANCE,
                        KEY_W_START_TIME, KEY_W_END_TIME, KEY_W_DATE, KEY_W_UPDATE_TYPE, KEY_W_WORKOUT_TYPE},
                KEY_W_DATE + "=? AND " + KEY_W_TYPE + "=?",
                new String[]{workout.getDate(), workout.getType()}, null, null, null, null);

        if (cursor.getCount() <= 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_W_USER_ID, workout.getUid());
            values.put(KEY_W_TYPE, workout.getType());
            values.put(KEY_W_STEP, workout.getSteps());
            values.put(KEY_W_RUN, workout.getRuns());
            values.put(KEY_W_ENERGY, workout.getEnergy());
            values.put(KEY_W_CAL, workout.getCal());
            values.put(KEY_W_DISTANCE, workout.getDistance());
            values.put(KEY_W_START_TIME, workout.getStart_time());
            values.put(KEY_W_END_TIME, workout.getEnd_time());
            values.put(KEY_W_DATE, workout.getDate());
            values.put(KEY_W_UPDATE_TYPE, workout.getUpdate_type());

            values.put(KEY_W_WORKOUT_TYPE, workout.getWorkout_type());
            db.insert(TABLE_DAILY_WORKOUT_SUMMERY, null, values);
        } else {
            ContentValues values = new ContentValues();
            values.put(KEY_W_USER_ID, workout.getUid());
            values.put(KEY_W_TYPE, workout.getType());
            values.put(KEY_W_STEP, workout.getSteps());
            values.put(KEY_W_RUN, workout.getRuns());
            values.put(KEY_W_ENERGY, workout.getEnergy());
            values.put(KEY_W_CAL, workout.getCal());
            values.put(KEY_W_DISTANCE, workout.getDistance());
            values.put(KEY_W_START_TIME, workout.getStart_time());
            values.put(KEY_W_END_TIME, workout.getEnd_time());
            values.put(KEY_W_DATE, workout.getDate());
            values.put(KEY_W_UPDATE_TYPE, workout.getUpdate_type());
            values.put(KEY_W_WORKOUT_TYPE, workout.getWorkout_type());

            result = db.update(TABLE_DAILY_WORKOUT_SUMMERY, values,
                    KEY_W_DATE + " = ? AND " + KEY_W_TYPE + " = ?",
                    new String[]{workout.getDate(), workout.getType()});


        }
        return result;
    }

    public int updateWorkoutTable22(workoutEntity workout) {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_DAILY_WORKOUT_SUMMERY2, new String[]{
                        KEY_W_ORK_ID, KEY_W_USER_ID, KEY_W_TYPE, KEY_W_STEP, KEY_W_RUN, KEY_W_ENERGY, KEY_W_CAL, KEY_W_DISTANCE,
                        KEY_W_START_TIME, KEY_W_END_TIME, KEY_W_DATE, KEY_W_UPDATE_TYPE},
                KEY_W_DATE + "=? AND " + KEY_W_TYPE + "=?",
                new String[]{workout.getDate(), workout.getType()}, null, null, null, null);

        if (cursor.getCount() <= 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_W_USER_ID, workout.getUid());
            values.put(KEY_W_TYPE, workout.getType());
            values.put(KEY_W_STEP, workout.getSteps());
            values.put(KEY_W_RUN, workout.getRuns());
            values.put(KEY_W_ENERGY, workout.getEnergy());
            values.put(KEY_W_CAL, workout.getCal());
            values.put(KEY_W_DISTANCE, workout.getDistance());
            values.put(KEY_W_START_TIME, workout.getStart_time());
            values.put(KEY_W_END_TIME, workout.getEnd_time());
            values.put(KEY_W_DATE, workout.getDate());
            values.put(KEY_W_UPDATE_TYPE, workout.getUpdate_type());
            db.insert(TABLE_DAILY_WORKOUT_SUMMERY2, null, values);
        } else {
            ContentValues values = new ContentValues();
            values.put(KEY_W_USER_ID, workout.getUid());

            values.put(KEY_W_TYPE, workout.getType());
            values.put(KEY_W_STEP, workout.getSteps());
            values.put(KEY_W_RUN, workout.getRuns());
            values.put(KEY_W_ENERGY, workout.getEnergy());
            values.put(KEY_W_CAL, workout.getCal());
            values.put(KEY_W_DISTANCE, workout.getDistance());
            values.put(KEY_W_START_TIME, workout.getStart_time());
            values.put(KEY_W_END_TIME, workout.getEnd_time());
            values.put(KEY_W_DATE, workout.getDate());
            values.put(KEY_W_UPDATE_TYPE, workout.getUpdate_type());

            result = db.update(TABLE_DAILY_WORKOUT_SUMMERY2, values,
                    KEY_W_DATE + " = ? AND " + KEY_W_TYPE + " = ?",
                    new String[]{workout.getDate(), workout.getType()});


        }
        return result;
    }


    // Getting single contact
    public User getUserDetailsFromDBEEEEE() {
        User contact = null;
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //==============================================================================
        if (cursor.getCount() <= 0) {
            //  cursor.close();
            System.out.println("------------------No User Data ---------------" + cursor.getCount());
            //  contact=null;
        } else {
            cursor.moveToFirst();

            contact = new User(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9), cursor.getString(10)
            );
        }
        // return contact
        return contact;
    }

    // Getting single contact
    public profileimageEn getProfileImageFromDB() {

        SQLiteDatabase db = this.getReadableDatabase();

        profileimageEn profimage = null;

        Cursor cursor = db.query(TABLE_PROFILE_IMAGE, new String[]{KEY_NAME_PROFILE_ID, KEY_NAME_PROFILE_IMAGE,
                        KEY_IMAGE_PROFILE_IMAGE}, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("------------------No Profile Image--------------------" + cursor.getCount());

        } else {
            System.out.println("------------------No Profile Image--2------------------" + cursor.getCount());

            System.out.println("------------------No Profile Image--3------------------" + cursor.getString(1));
            System.out.println("------------------No Profile Image--4------------------" + cursor.getString(2));
            profimage = new profileimageEn(
                    cursor.getString(1),
                    cursor.getString(2));

            // cursor.moveToLast();

        }
        return profimage;
    }


    // Getting single contact
    public FavouriteActivity getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVOURITE, new String[]{KEY_F_ID, KEY_F_USER_ID,
                        KEY_F_NAME, KEY_F_IMAGE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        FavouriteActivity contact = null;
        // FavouriteActivity contact = new FavouriteActivity(Integer.parseInt(cursor.getString(0)),
        //         cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return contact
        return contact;
    }

    public int updateContact(FavouriteActivity contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_F_NAME, contact.getName());
        // values.put(KEY_STATUS, contact.getStatus());
        values.put(KEY_F_IMAGE, contact.getImage());
        // updating row
        return db.update(TABLE_FAVOURITE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getUserid())});
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FAVOURITE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public int getTodaySummeryCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TODAY_MAIN_SUMMERY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    //============================================================================


    //============================================================================

    // Getting single contact
    public todaysummery getTodaySummeryFromDBNew1(String date, String uid) {

        if ((Utility.isNotNull(date)) && (Utility.isNotNull(uid))) {

            todaysummery contact = null;
            SQLiteDatabase db =null;
            try{

                db = this.getReadableDatabase();

                Cursor cursor = db.query(TABLE_TODAY_MAIN_SUMMERY, new String[]{KEY_ID, KEY_USER_ID, KEY_DATE,
                                KEY_STEP_TOT, KEY_RUN_TOT, KEY_JUMP_TOT, KEY_MET_TOT, KEY_ENGY_PERSENT_TOT, KEY_CAL_TOT},
                        KEY_DATE + "=? AND " + KEY_USER_ID + " = ?",
                        new String[]{date, uid}, null, null, null, null);
                if (cursor.getCount() <= 0) {
                    cursor.close();
                    System.out.println("------------------No Default data for Selected Day--------------------");
                } else {

                    cursor.moveToFirst();
                    System.out.println("------------------Have Default data for Selected Day--------------------");
                    contact = new todaysummery(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getInt(3),
                            cursor.getInt(4),
                            cursor.getInt(5),
                            cursor.getFloat(6),
                            cursor.getFloat(7),
                            cursor.getFloat(8));
                    // cursor.close();
                }
            }catch (Exception e){
             e.printStackTrace();
            }finally {
                db.close();
            }
            return contact;
        }else{
            return null;
        }
    }

    public ArrayList<todaysummery> getTotalSummeryFromDB(String uid) {
        ArrayList<todaysummery> contactList = new ArrayList<todaysummery>();
        todaysummery contact = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;

        c = db.query(TABLE_TODAY_MAIN_SUMMERY, new String[]{KEY_ID, KEY_USER_ID, KEY_DATE,
                        KEY_STEP_TOT, KEY_RUN_TOT, KEY_JUMP_TOT, KEY_MET_TOT, KEY_ENGY_PERSENT_TOT, KEY_CAL_TOT},
                KEY_USER_ID + " = ?",
                new String[]{uid}, null, null, null, null);
        if (c.getCount() <= 0) {
            c.close();
            System.out.println("------------------No data for Side Menu------*****************-----Empty---------");
        } else {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                System.out.println("------------------No data for Side Menu------******---" + c.getString(3));
                contact = new todaysummery(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getInt(3),
                        c.getInt(4),
                        c.getInt(5),
                        c.getFloat(6),
                        c.getFloat(7),
                        c.getFloat(8));

                contactList.add(contact);

                c.moveToNext();
            }
            // return contact
        }
        return contactList;
    }


    // Getting All Contacts
    public List<FavouriteActivity> getAllContacts(String userid) {

        List<FavouriteActivity> contactList = new ArrayList<FavouriteActivity>();
        // Select All Query
        //  String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITE;s
        SQLiteDatabase db = this.getWritableDatabase();

        if (userid != null) {

            Cursor cursor = db.query(TABLE_FAVOURITE, new String[]{KEY_F_USER_ID,
                            KEY_F_NAME, KEY_F_IMAGE, KEY_F_MET}, KEY_F_USER_ID + "=?",
                    new String[]{userid}, null, null, null, null);

            // Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                contactList = null;
                System.out.println("------------------No Data for Selected Day--------------------");
            } else {

                if (cursor.moveToFirst()) {
                    do {
                        FavouriteActivity contact = new FavouriteActivity();
                        //    contact.setId(Integer.parseInt(cursor.getString(0)));
                        contact.setUserid(cursor.getString(0));
                        contact.setName(cursor.getString(1));
                        contact.setImage(cursor.getString(2));
                        contact.setMet(cursor.getString(3));
                        contactList.add(contact);
                    } while (cursor.moveToNext());
                }
            }

        } else {
            contactList = null;
        }

        return contactList;
    }

    // Deleting single contact
    public void deleteFavourite(FavouriteActivity contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVOURITE, KEY_F_NAME + " = ?",
                new String[]{String.valueOf(contact.getName())});
        db.close();
    }



    public void checkIfEmptyDontUse() {
        Date dae = new Date();

        String de = sdf.format(dae);
        // de="08/02/2016";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TODAY_MAIN_SUMMERY, new String[]{
                        KEY_ID, KEY_DATE,
                        KEY_STEP_TOT, KEY_RUN_TOT, KEY_JUMP_TOT, KEY_MET_TOT, KEY_ENGY_PERSENT_TOT, KEY_CAL_TOT}, KEY_DATE + "=?",
                new String[]{de}, null, null, null, null);
        int sssss = cursor.getCount();

        if (sssss == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_DATE, de);
            values.put(KEY_STEP_TOT, 0);
            values.put(KEY_RUN_TOT, 0);
            values.put(KEY_JUMP_TOT, 0);
            values.put(KEY_MET_TOT, 0);
            values.put(KEY_ENGY_PERSENT_TOT, 0);
            values.put(KEY_CAL_TOT, 0);
            // Inserting Row

            db.insert(TABLE_TODAY_MAIN_SUMMERY, null, values);
            System.out.println("---------------DB TAble initialised---------------------- ");
            db.close(); // Closing database connection
        } else {
            //Just sleep man ..................Do nothing
            System.out.println("---------------DB TAble Already initialised---------------------- ");
        }

    }

    // Getting Array List contact
    public ArrayList<workoutEntity> getWorkoutDataListFromDB(String date, String type) {
        workoutEntity contact = null;

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<workoutEntity> list = new ArrayList<workoutEntity>();
        Cursor cursor = db.query(TABLE_DAILY_WORKOUT_SUMMERY, new String[]{
                        KEY_W_ORK_ID,
                        KEY_W_USER_ID,
                        KEY_W_TYPE,
                        KEY_W_STEP,
                        KEY_W_RUN,
                        KEY_W_ENERGY,
                        KEY_W_CAL,
                        KEY_W_DISTANCE,
                        KEY_W_START_TIME,
                        KEY_W_END_TIME,
                        KEY_W_DATE,
                        KEY_W_UPDATE_TYPE, KEY_W_WORKOUT_TYPE, KEY_W_SERVER_UPDATED_STATUS},
                KEY_W_DATE + "=? AND " + KEY_W_TYPE + " = ?",
                new String[]{date, type}, null, null, null, null);


        System.out.println("-----------------)))))))))))))))-------------------" + date + "   " + type);
///

        if (cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("------------------No Data for Selected Day--------------------");
        } else {
            //
            int i = 0;

            while (cursor.moveToNext()) {

                contact = new workoutEntity(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getFloat(5),
                        cursor.getFloat(6),
                        cursor.getInt(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getInt(11),
                        cursor.getString(13),
                        cursor.getString(12));
                System.out.println("-----------------)))))))))))))))--->-------  " + cursor.getInt(3));
                list.add(contact);
            }
            cursor.moveToFirst();

        }
        return list;
    }

    public ArrayList<workoutEntity> getTodayWorkoutsFromDB(String date) {
        workoutEntity contact = null;

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<workoutEntity> list = new ArrayList<workoutEntity>();

        Cursor cursor = db.query(TABLE_DAILY_WORKOUT_SUMMERY, new String[]{
                        KEY_W_ORK_ID, KEY_W_USER_ID,
                        KEY_W_TYPE,
                        KEY_W_STEP,
                        KEY_W_RUN,
                        KEY_W_ENERGY,
                        KEY_W_CAL,
                        KEY_W_DISTANCE,
                        KEY_W_START_TIME,
                        KEY_W_END_TIME,
                        KEY_W_DATE,
                        KEY_W_UPDATE_TYPE,
                        KEY_W_WORKOUT_TYPE, KEY_W_SERVER_UPDATED_STATUS}, KEY_W_DATE + "=?",
                new String[]{date}, null, null, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("------------------Workouts for Selected Day--------------------");
        } else {

            int i = 0;

            while (cursor.moveToNext()) {
                System.out.println("------------------looppppppppp--0------------------");
                contact = new workoutEntity(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getFloat(5),
                        cursor.getFloat(6),
                        cursor.getInt(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getInt(11),
                        cursor.getString(12),
                        cursor.getString(13));
                list.add(contact);
            }
            cursor.moveToFirst();

        }
        return list;
    }

    // Getting Array List contact
    public ArrayList<workoutEntity> getTotalWorkoutDataList(String uid) {
        workoutEntity contact = null;

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<workoutEntity> list = new ArrayList<workoutEntity>();

        Cursor cursor = db.query(TABLE_DAILY_WORKOUT_SUMMERY, new String[]{
                        KEY_W_ORK_ID, KEY_W_USER_ID,
                        KEY_W_TYPE,
                        KEY_W_STEP,
                        KEY_W_RUN,
                        KEY_W_ENERGY,
                        KEY_W_CAL,
                        KEY_W_DISTANCE,
                        KEY_W_START_TIME,
                        KEY_W_END_TIME,
                        KEY_W_DATE,
                        KEY_W_UPDATE_TYPE, KEY_W_WORKOUT_TYPE, KEY_W_SERVER_UPDATED_STATUS},
                KEY_W_USER_ID + "=?",
                new String[]{uid}, null, null, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("------------------No Data for Total Workout data--------------------");
        } else {
            System.out.println("------------------Have Data for Workout -------------------------------------");
            int i = 0;
            while (cursor.moveToNext()) {

                contact = new workoutEntity(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getFloat(5),
                        cursor.getFloat(6),
                        cursor.getInt(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getInt(11),
                        cursor.getString(12),
                        cursor.getString(13));
                list.add(contact);
            }
            cursor.moveToFirst();

        }
        return list;
    }


    public todaysummery getTodaySummeryFromDB(String date, String uid) {
        todaysummery contact = null;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TODAY_MAIN_SUMMERY, new String[]{KEY_ID, KEY_USER_ID, KEY_DATE,
                        KEY_STEP_TOT, KEY_RUN_TOT, KEY_JUMP_TOT, KEY_MET_TOT, KEY_ENGY_PERSENT_TOT, KEY_CAL_TOT},
                KEY_DATE + "=? AND " + KEY_USER_ID + " = ?",
                new String[]{date, uid}, null, null, null, null);


        if (cursor.getCount() <= 0) {
        } else {

            cursor.moveToFirst();

            contact = new todaysummery(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getFloat(6),
                    cursor.getFloat(7),
                    cursor.getFloat(8));
            // return contact
        }
        return contact;
    }

    // Getting Array List contact
    public ArrayList<workoutEntity> getWorkoutDataListForDateFromDB(String date, String uid) {
        workoutEntity contact = null;
        //  System.out.println("------------------checkinuuuuuuuuuuuuuuuuuu--------------------"+date+"  "+uid);
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<workoutEntity> list = new ArrayList<workoutEntity>();


        Cursor cursor = db.query(TABLE_DAILY_WORKOUT_SUMMERY, new String[]{
                        KEY_W_ORK_ID,
                        KEY_W_USER_ID,
                        KEY_W_TYPE,
                        KEY_W_STEP,
                        KEY_W_RUN,
                        KEY_W_ENERGY,
                        KEY_W_CAL,
                        KEY_W_DISTANCE,
                        KEY_W_START_TIME,
                        KEY_W_END_TIME,
                        KEY_W_DATE,
                        KEY_W_UPDATE_TYPE, KEY_W_WORKOUT_TYPE, KEY_W_SERVER_UPDATED_STATUS},
                KEY_W_USER_ID + "=? AND " + KEY_W_DATE + " = ?",
                new String[]{uid, date}, null, null, null, null);

        //  System.out.println("------------------checkingggggggggggggggg for Workout data for Selected Day--------------------");
        // KEY_W_USER_ID + "=? AND " + KEY_W_DATE + " = ?",
        if (cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("------------------No Data for Workout data for Selected Day--------------------");
        } else {
            //  System.out.println("------------------Have Data for Workout -------------------------------------");
            int i = 0;
            while (cursor.moveToNext()) {

                contact = new workoutEntity(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getFloat(5),
                        cursor.getFloat(6),
                        cursor.getInt(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getInt(11),
                        cursor.getString(13),
                        cursor.getString(12));
                list.add(contact);
            }
            // cursor.moveToFirst();

        }
        return list;
    }


    public ArrayList<workoutEntity> getWorkoutDataListForDateFromDB2(String date) {
        workoutEntity contact = null;

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<workoutEntity> list = new ArrayList<workoutEntity>();

        Cursor cursor = db.query(TABLE_DAILY_WORKOUT_SUMMERY2, new String[]{
                        KEY_W_ORK_ID, KEY_W_USER_ID,
                        KEY_W_TYPE,
                        KEY_W_STEP,
                        KEY_W_RUN,
                        KEY_W_ENERGY,
                        KEY_W_CAL,
                        KEY_W_DISTANCE,
                        KEY_W_START_TIME,
                        KEY_W_END_TIME,
                        KEY_W_DATE,
                        KEY_W_UPDATE_TYPE, KEY_W_WORKOUT_TYPE, KEY_W_SERVER_UPDATED_STATUS}, KEY_W_DATE + "=?",
                new String[]{date}, null, null, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("------------------No Data for Workout data for Selected Day--------------------");
        } else {
            System.out.println("------------------Have Data for Workout -------------------------------------");
            int i = 0;
            while (cursor.moveToNext()) {

                contact = new workoutEntity(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getFloat(5),
                        cursor.getFloat(6),
                        cursor.getInt(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getInt(11),
                        cursor.getString(13),
                        cursor.getString(12));
                list.add(contact);
            }
            cursor.moveToFirst();

        }
        return list;
    }

    // Getting single contact
    public workoutEntity getWorkoutDataFromDB(String date, String type) {
        workoutEntity contact = null;

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_DAILY_WORKOUT_SUMMERY, new String[]{
                        KEY_W_ORK_ID, KEY_W_USER_ID,
                        KEY_W_TYPE,
                        KEY_W_STEP, KEY_W_RUN,
                        KEY_W_ENERGY,
                        KEY_W_CAL,
                        KEY_W_DISTANCE,
                        KEY_W_START_TIME,
                        KEY_W_END_TIME,
                        KEY_W_DATE,
                        KEY_W_UPDATE_TYPE, KEY_W_WORKOUT_TYPE, KEY_W_SERVER_UPDATED_STATUS}, KEY_W_DATE + "=? AND " + KEY_W_TYPE + "=?",
                new String[]{date, type}, null, null, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("------------------No Data for Selected Day--------------------");

        } else {

            cursor.moveToFirst();


            contact = new workoutEntity(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getFloat(5),
                    cursor.getFloat(6),
                    cursor.getInt(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getInt(11),
                    cursor.getString(12),
                    cursor.getString(13));
        }
        return contact;
    }



    //======STEPS UDPDATE QUERY ==========================================
    public int saveOrUpdateSummery(todaysummery summery) {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        try{

            Cursor cursor = db.query(TABLE_TODAY_MAIN_SUMMERY, new String[]{KEY_ID, KEY_USER_ID, KEY_DATE,
                            KEY_STEP_TOT, KEY_RUN_TOT, KEY_JUMP_TOT, KEY_MET_TOT, KEY_ENGY_PERSENT_TOT, KEY_CAL_TOT}, KEY_DATE + "=?",
                    new String[]{summery.getDate()}, null, null, null, null);

            if (cursor.getCount() <= 0) {
                ContentValues values = new ContentValues();
                values.put(KEY_USER_ID, summery.getUserid());
                values.put(KEY_DATE, summery.getDate());
                values.put(KEY_STEP_TOT, summery.getStep_tot());
                values.put(KEY_RUN_TOT, summery.getRun_tot());
                values.put(KEY_JUMP_TOT, summery.getJump_tot());
                values.put(KEY_MET_TOT, summery.getMet_tot());
                values.put(KEY_ENGY_PERSENT_TOT, summery.getEnegy_persent());
                values.put(KEY_CAL_TOT, summery.getCal_tot());
                // Inserting Row
                Long res = db.insert(TABLE_TODAY_MAIN_SUMMERY, null, values);


                cursor.close();

                System.out.println("------------------Add new Raw for New Date for Default Activities--------------------");
                System.out.println("---------------------"+summery.getUserid()+"    "+summery.getDate()+"    "+summery.getStep_tot());

            } else {

                ContentValues values = new ContentValues();
                values.put(KEY_USER_ID, summery.getUserid());
                values.put(KEY_DATE, summery.getDate());
                values.put(KEY_STEP_TOT, summery.getStep_tot());
                values.put(KEY_RUN_TOT, summery.getRun_tot());
                values.put(KEY_JUMP_TOT, summery.getJump_tot());
                values.put(KEY_MET_TOT, summery.getMet_tot());
                values.put(KEY_ENGY_PERSENT_TOT, summery.getEnegy_persent());
                values.put(KEY_CAL_TOT, summery.getCal_tot());
                // updating row

                result = db.update(TABLE_TODAY_MAIN_SUMMERY, values, KEY_DATE + " = ?",
                        new String[]{String.valueOf(summery.getDate())});

                System.out.println("------------------Updated Exisiting Raw for Default Activities--------------------");
                System.out.println("---------------------"+summery.getUserid()+"    "+summery.getDate()+"    "+summery.getStep_tot());
            }
        }catch (Exception e){
          e.printStackTrace();
        }finally{

            db.close();
        }

        return result;
    }
    //================================================

    //======CASH DATA SAVE  OR UDPDATE QUERY ==========================================
    public void updateDoctorData(String doctorData,String type,String suttentTime) {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            Cursor cursor = db.query(TABLE_SETTINGS, new String[]{
                            KEY_ID,
                            KEY_S_USER_ID,
                            KEY_S_STEP_GOAL,
                            KEY_S_ENERGY_GOAL,
                            KEY_S_CALERY_GOAL,
                            KEY_S_HEIGHT_GOAL,
                            KEY_S_WEIGHT_GOAL,
                            KEY_S_WAIST_GOAL,
                            KEY_S_BPM_GOAL,
                            KEY_S_SETTING_DATE,
                            KEY_S_UPDATED_STATUS}, KEY_S_SETTING_DATE + "=?",
                    new String[]{type}, null, null, null, null);


            if (cursor.getCount() <= 0) {
                ContentValues values = new ContentValues();
                values.put(KEY_S_USER_ID,doctorData);
                values.put(KEY_S_STEP_GOAL,suttentTime);
                values.put(KEY_S_ENERGY_GOAL,"");
                values.put(KEY_S_CALERY_GOAL,"");
                values.put(KEY_S_HEIGHT_GOAL,"");
                values.put(KEY_S_WEIGHT_GOAL,"");
                values.put(KEY_S_WAIST_GOAL,"");
                values.put(KEY_S_BPM_GOAL,"");
                values.put(KEY_S_SETTING_DATE,type);
                values.put(KEY_S_UPDATED_STATUS,"");
                // Inserting Row
                Long res = db.insert(TABLE_SETTINGS, null, values);
                cursor.close();

                System.out.println("------------------Add new Raw for New Date for Default Activities--------------------");
            } else {

                ContentValues values = new ContentValues();
                values.put(KEY_S_USER_ID,doctorData);
                values.put(KEY_S_STEP_GOAL,suttentTime);
                values.put(KEY_S_ENERGY_GOAL,"");
                values.put(KEY_S_CALERY_GOAL,"");
                values.put(KEY_S_HEIGHT_GOAL,"");
                values.put(KEY_S_WEIGHT_GOAL,"");
                values.put(KEY_S_WAIST_GOAL,"");
                values.put(KEY_S_BPM_GOAL,"");
                values.put(KEY_S_SETTING_DATE,type);
                values.put(KEY_S_UPDATED_STATUS,"");
                // updating row

                result = db.update(TABLE_SETTINGS, values, KEY_S_SETTING_DATE + " = ?",
                        new String[]{String.valueOf(type)});

                System.out.println("------------------Updated Exisiting Raw for Default Activities--------------------");

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{

            db.close();
        }


    }
    //================================================


    //=============================================
    // Getting CASH DATA FROM DB
    public DBString getCashDataByTypeFromDB(String type) {
        DBString cashedData=null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_SETTINGS, new String[]{
                            KEY_ID,
                            KEY_S_USER_ID,
                            KEY_S_STEP_GOAL,
                            KEY_S_ENERGY_GOAL,
                            KEY_S_CALERY_GOAL,
                            KEY_S_HEIGHT_GOAL,
                            KEY_S_WEIGHT_GOAL,
                            KEY_S_WAIST_GOAL,
                            KEY_S_BPM_GOAL,
                            KEY_S_SETTING_DATE,
                            KEY_S_UPDATED_STATUS},
                    KEY_S_SETTING_DATE + "=?",
                    new String[]{type}, null, null, null, null);


            if (cursor.getCount() <= 0) {
            } else {

                cursor.moveToFirst();
                String data= cursor.getString(1);
                String time= cursor.getString(2);

                cashedData=new DBString(data,time);
             //   System.out.println("==========cashedData=================="+cashedData);
                // return contact
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return cashedData;
    }
    //============================================


}