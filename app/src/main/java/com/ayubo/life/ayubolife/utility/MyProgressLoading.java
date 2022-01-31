package com.ayubo.life.ayubolife.utility;

import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressLoading {


   public static ProgressDialog progressDialog;



    public static void dismissDialog(){
        if(progressDialog!=null){
            progressDialog.cancel();
        }

    }
    public static void showLoading(Context c, String msg){

        progressDialog=new ProgressDialog(c);
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setMessage(msg);
    }



    public static ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public static void setProgressDialog(ProgressDialog progressDialog) {
        MyProgressLoading.progressDialog = progressDialog;
    }

}
