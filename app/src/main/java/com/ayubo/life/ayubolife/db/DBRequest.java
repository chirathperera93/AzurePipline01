package com.ayubo.life.ayubolife.db;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.todaysummery;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DBRequest {

    public static int getYesterdayFinalReading(Context c,String userid_ExistingUser) {
        int dbSteps=0;
        if(c!=null) {
            DatabaseHandler db = new DatabaseHandler(c);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Calendar calObj = Calendar.getInstance();
            calObj.setTime(new Date());
            calObj.add(Calendar.DATE, -1);
            String de = sdf.format(calObj.getTime());

            // String de = sdf.format(dae);
            todaysummery data = null;
            if (de != null && userid_ExistingUser != null) {
                try {
                    data = db.getTodaySummeryFromDB(de, userid_ExistingUser);

                } catch (Exception e) {
                }
            }
            if (data == null) {
            } else {
                dbSteps = data.getJump_tot();

            }
        }
        return  dbSteps;
    }

    public static boolean hasStepsToday(Context c,String userid_ExistingUser) {
        boolean isSteps=false;
        if(c!=null) {
            DatabaseHandler db = new DatabaseHandler(c);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dae = new Date();
            String de = sdf.format(dae);
            todaysummery data = null;
            if (de != null && userid_ExistingUser != null) {
                try {
                    data = db.getTodaySummeryFromDB(de, userid_ExistingUser);
                } catch (Exception e) {
                }
            }
            if (data == null) {
                isSteps = false;
            } else {
                isSteps = true;

            }
        }
        return  isSteps;
    }

    public static int getTodayStepsCount(Context c, String userid_ExistingUser) {
        int dbSteps = 0;
        if(c!=null) {

            DatabaseHandler db = new DatabaseHandler(c);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dae = new Date();
            String de = sdf.format(dae);
            todaysummery data = null;
            if (de != null && userid_ExistingUser != null) {
                try {
                    data = db.getTodaySummeryFromDB(de, userid_ExistingUser);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.close();
                }
            }
            if (data == null) {
            } else {
                dbSteps = data.getStep_tot();

            }
            System.out.println("========getTodayStepsCount===============" + de + "==========" + dbSteps);
        }
        return  dbSteps;
    }

    public static void updateDoctorData(Context c, String doctorData,String type) {
        if(c!=null){
            DatabaseHandler db = new DatabaseHandler(c);
            Long now = System.currentTimeMillis(); // See note below obj.getTimestamp()
            String time=now.toString();
            try {
                db.updateDoctorData(doctorData, type,time);
            }catch(Exception e){
                e.printStackTrace();
            }
            finally {
                db.close();
            }
        }

    }

    public static DBString getCashDataByType(Context c,String type){
        DBString cashedData=null;
        if(c!=null) {
            DatabaseHandler db = new DatabaseHandler(c);
            try {
                cashedData = db.getCashDataByTypeFromDB(type);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return cashedData;
    }

    public static void updateToSummeryTable(Context c,String userid, String da, int steps, int runs, int jumps, float mets, float enegy_persnt, float cals) {

        if(c!=null) {
            DatabaseHandler db = new DatabaseHandler(c);

            String dat = da;
            int tot_steps = steps;
            int tot_runs = runs;
            int tot_jumps = jumps;
            float tot_mets = mets;
            float tot_clas = cals;
            float tot_enegy_presnt = enegy_persnt;
            System.out.println("=========New Sensor steps======UPDATINGGGGGGGGGGG==============================" + tot_steps);
            try {

                db.saveOrUpdateSummery(new todaysummery(1, userid, dat, tot_steps, tot_runs, tot_jumps, tot_mets, tot_enegy_presnt, tot_clas));
                Log.d("Good Bye ", " Current Summery updated successfully");
            } catch (SQLiteException e) {
                Log.d("Error: ", "Inserting .." + e);
            } finally {
                db.close();
            }
        }
    }



}
