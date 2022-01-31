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
import com.ayubo.life.ayubolife.channeling.model.VisitLocation;
import com.ayubo.life.ayubolife.channeling.util.AppHandler;
import com.ayubo.life.ayubolife.channeling.util.DownloadManager;

/**
 * Created by Sabri on 3/17/2018. Search Action implemented for Hospitals search
 */

public class SearchLocationAction implements SearchActivity.SearchActions, Serializable {

    private AyuboSearchParameters params;

    public SearchLocationAction(AyuboSearchParameters params) {
        this.params = params;
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.search_location);
    }

    @Override
    public boolean isValueConsists(Object object, String value) {
        VisitLocation location = (VisitLocation) object;
        return location.getName().toLowerCase().contains(value.toLowerCase());
    }

    @Override
    public boolean onFinish(Activity activity, Object object) {
        Intent result = new Intent();
        if(object != null) {
            result.putExtra(SearchActivity.EXTRA_SEARCH_VALUE, ((VisitLocation) object).getName());
            result.putExtra(SearchActivity.EXTRA_SEARCH_ID, ((VisitLocation) object).getId());
            result.putExtra(SearchActivity.EXTRA_RESULT_OBJECT, (VisitLocation) object);
            activity.setResult(Activity.RESULT_OK, result);
        }
        activity.finish();
        return true;
    }

    @Override
    public List<Object> readObject(JSONObject jsonObject) {
        Type messageType = new TypeToken<List<VisitLocation>>() {
        }.getType();

        try {
            return new Gson().fromJson(jsonObject.getJSONArray("data").toString(), messageType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getName(Object object) {
        return ((VisitLocation) object).getName();
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
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_LOCATION_SEARCH, params.getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT);
    }

    @Override
    public int getViewType() {
        return SearchAdapter.SINGLE_TYPE;
    }

    @Override
    public String getOfflineFileName() {
        return AppConfig.OFFLINE_FILE_LOCATION;
    }
}
