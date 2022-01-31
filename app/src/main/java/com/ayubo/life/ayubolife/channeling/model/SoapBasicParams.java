package com.ayubo.life.ayubolife.channeling.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.channeling.activity.DashboardActivity;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.utility.Ram;

/**
 * Created by Sabri on 3/30/2018. Soap request basic params
 */

public class SoapBasicParams {

  //  protected String user_id;
   // protected String token_key;
  //protected String user_id;
//     public SoapBasicParams(Context c){
//
//
//
//    }
   protected String user_id;
   // protected String user_id = AppConfig.HEMAS_USER_ID;
    protected String token_key = AppConfig.HEMAS_SERVER_REQUEST_TOKEN;



    public String getSearchParams() {
        Context c=null;
        c=  Ram.getMycontext();
        if(c!=null){

            PrefManager pref = new PrefManager(c);
            user_id = pref.getLoginUser().get("uid");
           // user_id="ce7ec81d-c8f9-2d77-fca7-5dee1e275be6";

            Log.d("getSearchParams","=================================Context not null");
        }else{
            Log.d("getSearchParams","==============================Context  null");
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
            jsonObject.put("token_key", token_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getSearchParams(String query) {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
            jsonObject.put("q", query);
            jsonObject.put("token_key", token_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
