package com.ayubo.life.ayubolife.channeling.view;

import android.app.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.activity.DetailActivity;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.channeling.model.DetailRow;
import com.ayubo.life.ayubolife.channeling.model.DownloadDataBuilder;
import com.ayubo.life.ayubolife.channeling.model.Expert;
import com.ayubo.life.ayubolife.channeling.model.VideoSession;
import com.ayubo.life.ayubolife.channeling.util.TimeFormatter;

public class DetailVideoView implements DetailActivity.DetailAction, Serializable {

    //constants
    private static final String DOCTOR_FULL_NAME_WITH_TITLE = "%s. %s";
    private static final String DOCTOR_FULL_NAME_WITHOUT_TITLE = "%s";

    //instances
    Expert expert;
    VideoSession session;

    public DetailVideoView(Expert expert, VideoSession session) {
        this.expert = expert;
        this.session = session;
    }

    @Override
    public String getDocName() {
        if (expert.getTitle() != null && expert.getTitle().equals("null"))
            return String.format(Locale.getDefault(), DOCTOR_FULL_NAME_WITH_TITLE, expert.getTitle(), expert.getName());
        else
            return String.format(Locale.getDefault(), DOCTOR_FULL_NAME_WITHOUT_TITLE, expert.getName());
    }

    @Override
    public String getSpecialisation() {
        return expert.getSpeciality();
    }

    @Override
    public Double getTotalPrice() {
        return 0.0d;
    }

    @Override
    public DownloadDataBuilder getAddDownloadBuilder() {
        return null;
    }

    @Override
    public DownloadDataBuilder getRemoveDownloadBuilder(Activity activity) {
        return null;
    }

    @Override
    public boolean readData(Activity activity, String response) {
        return false;
    }

    @Override
    public void onFinish(Activity activity) {
        activity.finish();
    }

    @Override
    public String getDoctorNote() {
        return null;
    }

    @Override
    public boolean hasData(Activity activity) {
        return false;
    }

    @Override
    public List<Object> getPreList() {
        List<Object> objects = new ArrayList<>();

        return objects;
    }

    @Override
    public List<Object> getPostList() {
        List<Object> objects = new ArrayList<>();

        return objects;
    }
}
