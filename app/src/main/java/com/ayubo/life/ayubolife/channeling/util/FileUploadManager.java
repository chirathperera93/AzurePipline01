package com.ayubo.life.ayubolife.channeling.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.sql.SQLOutput;
import java.util.HashMap;

import com.ayubo.life.ayubolife.channeling.model.UploadDataBuilder;

public class FileUploadManager extends AsyncTask<Void, Void, Void> {

    public static final int INTERNAL_ERROR_CODE = 600;

    private UploadDataBuilder uploadDataBuilder;
    private UploadListener listener;
    private Context context;

    public FileUploadManager(Context context, UploadDataBuilder data) {
        this.context = context;
        this.uploadDataBuilder = data;
    }

    public FileUploadManager setUploadListener(UploadListener listener) {
        this.listener = listener;
        return this;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Void doInBackground(Void... voids) {

        String result;
        final DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(uploadDataBuilder.getUrl());

        try {
            if (uploadDataBuilder.getHeaders() != null)
                for (String key : uploadDataBuilder.getHeaders().keySet()) {
                    httppost.setHeader(key, uploadDataBuilder.getHeaders().get(key));
                }

            Log.d(FileUploadManager.class.getSimpleName(), "Server  Upload Request : " + uploadDataBuilder.getUrl());
            if (uploadDataBuilder.getHeaders() != null)
                Log.d(FileUploadManager.class.getSimpleName(), "Server Upload Request Headers : " + uploadDataBuilder.getHeaders().toString());

            if (uploadDataBuilder.getMultipartEntityBuilder() == null) {
                sendFailedObject("No data received", INTERNAL_ERROR_CODE);
                return null;
            }

            try {
                httppost.setEntity(uploadDataBuilder.getMultipartEntityBuilder().build());
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }

            HttpResponse response = httpclient.execute(httppost);

            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            result = in.readLine();

            Log.d(DownloadManager.class.getSimpleName(), "Server Response : " + result);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED ||
                    statusCode == HttpURLConnection.HTTP_ACCEPTED) {
                sendSuccessObject(result, statusCode);
            } else {
                sendFailedObject(result, statusCode);
            }


        } catch (IOException e) {
            e.printStackTrace();
            sendFailedObject(e.getMessage(), INTERNAL_ERROR_CODE);
        }
        return null;

    }

    private void sendSuccessObject(String response, int code) {
        if (listener != null)
            listener.onUploadSuccess(response, uploadDataBuilder.getWhat(), code);
    }

    private void sendFailedObject(String errorMessage, int code) {
        if (listener != null)
            listener.onUploadFailed(errorMessage, uploadDataBuilder.getWhat(), code);
    }

    public interface UploadListener {

        void onUploadSuccess(String response, int what, int code);

        void onUploadFailed(String errorMessage, int what, int code);
    }
}
