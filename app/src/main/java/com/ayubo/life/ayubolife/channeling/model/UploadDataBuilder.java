package com.ayubo.life.ayubolife.channeling.model;

import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.HashMap;

public class UploadDataBuilder {

    //constants
    private static final String URL_PARAMS_ADAPTER = "/?";
    private static final String URL_PARAMS_SEPARATOR = "&";

    private int what;
    private String url;
    private HashMap<String, String> headers;
    private MultipartEntityBuilder multipartEntityBuilder;

    public UploadDataBuilder init(String url, int what) {
        this.what = what;
        this.url = url;
        return this;
    }

    public int getWhat() {
        return what;
    }

    public UploadDataBuilder setWhat(int what) {
        this.what = what;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public UploadDataBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public UploadDataBuilder setUrlParams(HashMap<String, String> urlParams) {
        String separator = "";
        url += URL_PARAMS_ADAPTER;

        for(String key: urlParams.keySet()) {
            url += separator + key + "=" + urlParams.get(key);
            separator = URL_PARAMS_SEPARATOR;
        }
        return this;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public UploadDataBuilder setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public MultipartEntityBuilder getMultipartEntityBuilder() {
        return multipartEntityBuilder;
    }

    public UploadDataBuilder setMultipartEntityBuilder(MultipartEntityBuilder multipartEntityBuilder) {
        this.multipartEntityBuilder = multipartEntityBuilder;
        return this;
    }
}
