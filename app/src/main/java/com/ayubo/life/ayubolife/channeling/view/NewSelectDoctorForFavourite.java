package com.ayubo.life.ayubolife.channeling.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.activity.DoctorActivity;
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity;
import com.ayubo.life.ayubolife.channeling.adapter.SearchAdapter;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;

import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters;
import com.ayubo.life.ayubolife.channeling.model.DownloadDataBuilder;
import com.ayubo.life.ayubolife.channeling.model.NewFavDoctor;
import com.ayubo.life.ayubolife.channeling.model.SoapBasicParams;
import com.ayubo.life.ayubolife.channeling.util.AppHandler;
import com.ayubo.life.ayubolife.channeling.util.DownloadManager;
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

public class NewSelectDoctorForFavourite implements SearchActivity.SearchActions, Serializable {

    private DocSearchParameters params;
    private Context context;

    public NewSelectDoctorForFavourite(DocSearchParameters params) {
        this.params = params;

    }


    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.visit_a_doctor_two);
    }

    @Override
    public boolean isValueConsists(Object object, String value) {
        return object instanceof NewFavDoctor && ((NewFavDoctor) object).getDoctor_name().toLowerCase().contains(value.toLowerCase());
    }

    @Override
    public boolean onFinish(Activity activity, Object object) {
        if (object != null && object instanceof NewFavDoctor) {

            Intent intent = new Intent(activity, DoctorActivity.class);
            intent.putExtra(DoctorActivity.EXTRA_DOCTOR_DETAILS, (NewFavDoctor) object);
            activity.startActivity(intent);
            activity.finish();
            return true;
        }
        activity.finish();
        return false;
    }

    @Override
    public List<Object> readObject(JSONObject jsonObject) {
        Type messageType = new TypeToken<List<NewFavDoctor>>() {
        }.getType();

        try {
            List<NewFavDoctor> dataArray = new Gson().fromJson(jsonObject.getJSONArray("data").toString(), messageType);

            return new ArrayList<Object>(dataArray);
        } catch (JSONException | JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getName(Object object) {
        if (object instanceof NewFavDoctor )
            return ((NewFavDoctor) object).getDoctor_name();
        else
            return (String) object;
    }

    @Override
    public String getValue(Object object) {
        if (object instanceof NewFavDoctor)
            return ((NewFavDoctor) object).getSpecialization();
        else
            return "";
    }

    @Override
    public String getImageUrl(Object object) {
        if (object instanceof NewFavDoctor)
            return ((NewFavDoctor) object).getDoc_image();
        else
            return "";
    }

    @Override
    public DownloadDataBuilder getDownloadBuilder() {

        if (params.getDoctorId() != null)
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
