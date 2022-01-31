package com.ayubo.life.ayubolife.rest;

import com.ayubo.life.ayubolife.post.util.CustomInterceptor;
import com.flavors.changes.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    //Live


    public static final String MAIN_URL_LIVE_HAPPY = Constants.type == Constants.Type.HEMAS ? "https://livehappy.hemashealth.com/" : "https://livehappy.ayubo.life/";
    public static final String MAIN_URL_APPS = Constants.type == Constants.Type.HEMAS ? "https://apps.hemashealth.com/" : "https://apps.ayubo.life/";
    public static final String MAIN_URL_CONNECT = Constants.type == Constants.Type.HEMAS ? "https://connect.hemashealth.com/" : "https://connect.ayubo.life/";


    public static final String BASE_URL_live_v6 = MAIN_URL_LIVE_HAPPY + "custom/service/v7/rest.php/";


    public static final String BASE_URL_live = MAIN_URL_LIVE_HAPPY + "custom/service/v7/rest.php/";
    public static final String BASE_URL = MAIN_URL_LIVE_HAPPY;
    public static final String BASE_URL_entypoint = MAIN_URL_LIVE_HAPPY + "index.php?entryPoint=";
    public static final String BASE_URL_GOALEX = MAIN_URL_CONNECT + "api/v1/goal/";
    public static final String BASE_URL_NEW = MAIN_URL_LIVE_HAPPY + "api.ayubo.life/public/api/v1/";
    public static final String BASE_URL_NEW_SHORT = MAIN_URL_LIVE_HAPPY + "api.ayubo.life/public/api/";
    public static final String BASE_URL_AUTH = MAIN_URL_LIVE_HAPPY + "api.ayubo.life/public/";
    public static final String BASE_URL_REPORT = MAIN_URL_LIVE_HAPPY + "api.ayubo.life/public/api/v1/report/";
    public static final String BASE_URL_CONNECT = MAIN_URL_CONNECT + "api/v1/";
    public static final String BASE_URL_NEW_V2 = MAIN_URL_LIVE_HAPPY + "api.ayubo.life/public/api/v2/";
    public static final String AZURE_BASE_URL_V1 = "https://dev-ayubo-apim.azure-api.net/";
    public static final String STEP_GOAL = MAIN_URL_LIVE_HAPPY + "api.ayubo.life/public/api/v1/";
    public static final String ORDER_MEDICINE_MAIN = "https://dev-ayubo-apim.azure-api.net/ayubo-medicine/v1/";
    public static final String DEV_AYUBO_USERS = "https://dev-ayubo-apim.azure-api.net/dev-ayubo-users/v1/";
    public static final String DEV_AYUBO_USERS_V3 = "https://dev-ayubo-apim.azure-api.net/dev-ayubo-users/v3/";
    public static final String AZURE_NEW_CHAT_BASE_URL = "https://dev-ayubo-apim.azure-api.net/dev-ayubo-chat/v1/";

    public static final String AZURE_BASE_URL_V1_FOR_DASHBOARD =
            Constants.type == Constants.Type.HEMAS ?
                    "https://hemas-apim.azure-api.net/hemas-user-dashboard/v1/"
                    : "https://dev-ayubo-apim.azure-api.net/dev-ayubo-dashboard/v2/";

    public static final String AZURE_BASE_URL_V3_FOR_DASHBOARD =
            Constants.type == Constants.Type.HEMAS ?
                    "https://hemas-apim.azure-api.net/hemas-user-dashboard/v3/"
                    : "https://dev-ayubo-apim.azure-api.net/dev-ayubo-dashboard/v3/";

    public static final String AZURE_BASE_URL_V1_FOR_TODO =
            Constants.type == Constants.Type.HEMAS ?
                    "https://hemas-apim.azure-api.net/hemas-user-todo/v2/"
                    : "https://dev-ayubo-apim.azure-api.net/dev-ayubo-todo/v2/";


    public static final String AZURE_BASE_URL_V1_FOR_TODO_UPDATE =
            Constants.type == Constants.Type.HEMAS ?
                    "https://hemas-apim.azure-api.net/hemas-user-todo/v1/"
                    : "https://dev-ayubo-apim.azure-api.net/dev-ayubo-todo/v1/";

    public static final String AZURE_BASE_URL_V1_NEW_REPORTS =
            Constants.type == Constants.Type.HEMAS ?
                    "https://hemas-apim.azure-api.net/hemas-records/v1/"
                    : "https://dev-ayubo-apim.azure-api.net/dev-ayubo-records/v1/";

    public static final String AZURE_BASE_URL_V1_NEW_REPORTS_READ_RECORD =
            Constants.type == Constants.Type.HEMAS ?
                    "https://hemas-apim.azure-api.net/hemas-records/v1/"
                    : "https://dev-ayubo-apim.azure-api.net/dev-ayubo-records/v1/";


//    public static final String BASE_URL_live_v6 = "https://devo.ayubo.life/custom/service/v7/rest.php/";
//    public static final String BASE_URL_live = "https://devo.ayubo.life/custom/service/v7/rest.php/";
//    public static final String BASE_URL = "https://devo.ayubo.life/";
//    public static final String BASE_URL_entypoint = "https://devo.ayubo.life/index.php?entryPoint=";
//    public static final String BASE_URL_GOALEX = "http://connect.ayubo.life/api/v1/goal/";
//    public static final String BASE_URL_NEW = "https://devo.ayubo.life/api.ayubo.life/public/api/v1/";
//    public static final String BASE_URL_NEW_SHORT = "https://devo.ayubo.life/api.ayubo.life/public/api/";
//    public static final String BASE_URL_AUTH = "https://devo.ayubo.life/api.ayubo.life/public/";
//    public static final String BASE_URL_REPORT = "https://devo.ayubo.life/api.ayubo.life/public/api/v1/report/";
//    public static final String BASE_URL_CONNECT = "https://devo.ayubo.life/api.ayubo.life/public/api/v1/";


    public static Retrofit getClientBase() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_NEW_SHORT)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }

    public static Retrofit getClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_live)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }

    public Retrofit getClientWith_V6() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_live_v6)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }


    public Retrofit getClient3() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        CustomInterceptor logging = new CustomInterceptor("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvbGl2ZWhhcHB5LmF5dWJvLmxpZmVcL2FwaS5heXViby5saWZlXC9wdWJsaWNcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNTM3Nzk4OTk3LCJleHAiOjE2MDA4NzA5OTcsIm5iZiI6MTUzNzc5ODk5NywianRpIjoiMUo3cllIcVdvMXFCUUxxVyIsInN1YiI6IjEwMDBjMzI5LTJjNDQtNTBiMC05MDcxLTVhOTY3OTBmMTRlZCIsInBydiI6Ijg3ZTBhZjFlZjlmZDE1ODEyZmRlYzk3MTUzYTE0ZTBiMDQ3NTQ2YWEiLCJpZCI6IjEwMDBjMzI5LTJjNDQtNTBiMC05MDcxLTVhOTY3OTBmMTRlZCJ9.7i3twPkXjTRQYAiNu6SEVL_vzXoBVXpQZuKdkD2EycI");

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_CONNECT)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }


    public static Retrofit getGoalExApiClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_GOALEX)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }


    public static Retrofit getGoalExApiClientNew() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(STEP_GOAL)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }


    public static Retrofit getReportsApiClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_REPORT)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }

    public static Retrofit getReportsApiClientNew() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AZURE_BASE_URL_V1)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }


    public static Retrofit getBaseApiClientNew() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AZURE_BASE_URL_V1)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }

    public static Retrofit getReportsApiClientNewReadRecord() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AZURE_BASE_URL_V1_NEW_REPORTS_READ_RECORD)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }

    public static Retrofit getReportsApiClientNewForHemas() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AZURE_BASE_URL_V1_NEW_REPORTS)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }

    public static Retrofit getNewApiClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_NEW)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }


    public static Retrofit getAzureApiClientV1() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AZURE_BASE_URL_V1)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }


    public static Retrofit getAzureChatBaseUrl() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AZURE_NEW_CHAT_BASE_URL)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }

    public static Retrofit getAzureApiClientV1ForToDoUpdate() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AZURE_BASE_URL_V1_FOR_TODO_UPDATE)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }

    public static Retrofit getAzureApiClientV1ForTodo() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AZURE_BASE_URL_V1_FOR_TODO)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }

    public static Retrofit getAzureApiClientV1ForDashBoard() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AZURE_BASE_URL_V1_FOR_DASHBOARD)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }

    public static Retrofit getAzureApiClientV3ForDashBoard() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AZURE_BASE_URL_V3_FOR_DASHBOARD)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }


    public static Retrofit getAzureApiClientV1ForOrderMedicine() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ORDER_MEDICINE_MAIN)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }

    public static Retrofit getAzureApiClientV1ForUsers() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DEV_AYUBO_USERS)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }


    public static Retrofit getAzureApiClientV3ForUsers() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DEV_AYUBO_USERS_V3)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }


    public static Retrofit getNewApiClient_AUTH() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_AUTH)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }


    public static Retrofit getNewApiClientV2() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient clientt;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientt = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_NEW_V2)
                .client(clientt)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;
    }


}
