package com.ayubo.life.ayubolife.channeling.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity;
import com.ayubo.life.ayubolife.channeling.adapter.SearchAdapter;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.channeling.model.AyuboSearchParameters;
import com.ayubo.life.ayubolife.channeling.model.DownloadDataBuilder;
import com.ayubo.life.ayubolife.channeling.model.Specialization;
import com.ayubo.life.ayubolife.channeling.util.AppHandler;
import com.ayubo.life.ayubolife.channeling.util.DownloadManager;

/**
 * Created by Sabri on 3/17/2018. Search Action implemented for Specialty search
 */

public class SearchSpecialtyAction implements SearchActivity.SearchActions, Serializable {

    private AyuboSearchParameters params;

    public SearchSpecialtyAction(AyuboSearchParameters params) {
        this.params = params;
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.search_specialty);
    }

    @Override
    public boolean isValueConsists(Object object, String value) {
        Specialization specialization = (Specialization) object;
        return specialization.getName().toLowerCase().contains(value.toLowerCase());
    }

    @Override
    public boolean onFinish(Activity activity, Object object) {
        Intent result = new Intent();
        if (object != null) {
            result.putExtra(SearchActivity.EXTRA_SEARCH_VALUE, ((Specialization) object).getName());
            result.putExtra(SearchActivity.EXTRA_SEARCH_ID, ((Specialization) object).getId());
            result.putExtra(SearchActivity.EXTRA_RESULT_OBJECT, (Specialization) object);
            activity.setResult(Activity.RESULT_OK, result);
        }
        activity.finish();
        return true;
    }

    @Override
    public List<Object> readObject(JSONObject jsonObject) {
        Type messageType = new TypeToken<List<Specialization>>() {
        }.getType();

        try {

            String jsonString=jsonObject.getJSONArray("data").toString();
            return new Gson().fromJson(jsonString, messageType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getName(Object object) {
        return ((Specialization) object).getName();
    }

    @Override
    public String getValue(Object object) {
        return "";
    }

    @Override
    public String getImageUrl(Object object) {
        return "";
    }

    @Override
    public DownloadDataBuilder getDownloadBuilder() {
        return new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_SPECIALTY_SEARCH, params.getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT);
    }

    @Override
    public int getViewType() {
        return SearchAdapter.SINGLE_TYPE;
    }

    @Override
    public String getOfflineFileName() {
        return AppConfig.OFFLINE_FILE_SPECIALITY;
    }
}
