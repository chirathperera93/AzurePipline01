package com.ayubo.life.ayubolife.webrtc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;




import java.util.ArrayList;
import java.util.List;

/**
 * Created by tereha on 17.05.16.
 */
public class QbUsersDbManager {
    private static String TAG = QbUsersDbManager.class.getSimpleName();

    private static QbUsersDbManager instance;
    private Context mContext;

    private QbUsersDbManager(Context context) {
        this.mContext = context;
    }

    public static QbUsersDbManager getInstance(Context context) {
        if (instance == null) {
            instance = new QbUsersDbManager(context);
        }

        return instance;
    }


    public void clearDB() {
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbHelper.DB_TABLE_NAME, null, null);
        dbHelper.close();
    }


}

