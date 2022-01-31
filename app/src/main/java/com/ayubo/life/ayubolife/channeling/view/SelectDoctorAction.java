package com.ayubo.life.ayubolife.channeling.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.utility.Ram;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.activity.DoctorActivity;
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity;
import com.ayubo.life.ayubolife.channeling.adapter.SearchAdapter;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.channeling.model.ChannelDoctor;
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters;
import com.ayubo.life.ayubolife.channeling.model.DownloadDataBuilder;
import com.ayubo.life.ayubolife.channeling.model.SoapBasicParams;
import com.ayubo.life.ayubolife.channeling.util.AppHandler;
import com.ayubo.life.ayubolife.channeling.util.DownloadManager;

/**
 * Created by Sabri on 3/19/2018. View for Select doctor
 */

public class SelectDoctorAction implements SearchActivity.SearchActions, Serializable {

    private DocSearchParameters params;
    private Context context;
    public SelectDoctorAction(DocSearchParameters param) {
        this.params = param;
        params.setUser_id(Ram.getUserid());
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.visit_a_doctor_two);
    }

    @Override
    public boolean isValueConsists(Object object, String value) {
        return object instanceof ChannelDoctor && ((ChannelDoctor) object).getDoctor_name().toLowerCase().contains(value.toLowerCase());
    }

    @Override
    public boolean onFinish(Activity activity, Object object) {
        if (object != null && object instanceof ChannelDoctor) {

            Intent intent = new Intent(activity, DoctorActivity.class);
            intent.putExtra(DoctorActivity.EXTRA_DOCTOR_DETAILS, (ChannelDoctor) object);
            activity.startActivity(intent);
            activity.finish();
            return true;
        }
        activity.finish();
        return false;
    }

    @Override
    public List<Object> readObject(JSONObject jsonObject) {
        Type messageType = new TypeToken<HashMap<String, ChannelDoctor>>() {
        }.getType();

        try {
            HashMap<String, ChannelDoctor> dataArray = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), messageType);

            return new ArrayList<Object>(dataArray.values());
        } catch (JSONException | JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getName(Object object) {
        if (object instanceof ChannelDoctor)
            return ((ChannelDoctor) object).getDoctor_name();
        else
            return (String) object;
    }

    @Override
    public String getValue(Object object) {
        if (object instanceof ChannelDoctor)
            return ((ChannelDoctor) object).getSpecialization();
        else
            return "";
    }

    @Override
    public String getImageUrl(Object object) {
        if (object instanceof ChannelDoctor)
            return ((ChannelDoctor) object).getDoc_image();
        else
            return "";
    }

    @Override
    public DownloadDataBuilder getDownloadBuilder() {

        if(params != null){
            Context c=Ram.getMycontext();
            if(c!=null){
                PrefManager pref = new PrefManager(c);
                String user_id = pref.getLoginUser().get("uid");
                params.setUser_id(user_id);
                Log.d("getSearchParams","=================================Context not null"+user_id);
            }else{
                Log.d("getSearchParams","==============================Context  null");
            }

        }

        if (params != null)

            return new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                    setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_GET_CHANNELLING_SEARCH, params.getSearchParams())).
                    setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT);
        else
            return new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                    setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_GET_NEW_FAVORITES, new SoapBasicParams().getSearchParams())).
                    setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT);
    }

    @Override
    public int getViewType() {
        return SearchAdapter.DETAIL_TYPE;
    }

    @Override
    public String getOfflineFileName() {
        if (params != null)
            return null;
        else
            return AppConfig.OFFLINE_FILE_SELECT_DOCTOR;
    }

}
