package com.ayubo.life.ayubolife.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.login.LetsGo;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {
    EditText txtEmailAddress;
    //String server_URL;
    String msg,userid_ExistingUser,statusFromServiceAPI_db; ProgressDialog prgDialog;
    SharedPreferences prefs;
    SimpleDateFormat sdf;

    EditText txt_pwd,txt_cpwd,txt_email;
    String s_email,s_pwd,s_cpwd;

    LayoutInflater inflatert;
    View layoutt;
    TextView textt;
    Toast toastt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // server_URL="http://hemaswellness.cloudapp.net/development.hemas.life/custom/service/v4_1_custom/rest.php";
        prefs = getApplicationContext().getSharedPreferences("ayubolife", Context.MODE_PRIVATE);

        userid_ExistingUser= prefs.getString("uid", "0");

        prgDialog = new ProgressDialog(this);
        prgDialog.setCancelable(false);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        inflatert = getLayoutInflater();
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(getApplicationContext());
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);


        txt_email=(EditText)findViewById(R.id.txt_email);

        txt_pwd=(EditText)findViewById(R.id.txt_pwd);
        txt_cpwd=(EditText)findViewById(R.id.txt_cpwd);

    }
    public boolean isPasswordhaveSixDigits() {
        boolean status;

        if (s_pwd.length() > 7) {
            status = true;
        } else {
            status = false;

            textt.setText("Password must contain minimum 8 digits");
            toastt.setView(layoutt);
            toastt.show();
        }
        return status;
    }
    public boolean isPasswordEmpty() {
        boolean status;

        if (Utility.isNotNull(s_pwd)) {
            status = true;
        } else {
            status = false;

            textt.setText("Password cannot be empty");
            toastt.setView(layoutt);
            toastt.show();


        }
        return status;
    }
    public boolean isConfirmPasswordEmpty() {
        boolean status;

        if (Utility.isNotNull(s_cpwd)) {
            status = true;
        } else {
            status = false;

            textt.setText("Confirm password cannot be empty");
            toastt.setView(layoutt);
            toastt.show();


        }
        return status;
    }
    public boolean isConfirmPasswordCorrect() {
        boolean status;

        if (s_cpwd.equals(s_pwd)) {
            status = true;
        } else {
            status = false;

            textt.setText("Password and confirmed password does not match");
            toastt.setView(layoutt);
            toastt.show();


        }
        return status;
    }

    public boolean isEmailCorrectFormt(String email) {
        boolean status;

        if (Utility.validateEmail(email)) {
            status = true;
        } else {
            status = false;

            textt.setText("Invalid email address");
            toastt.setView(layoutt);
            toastt.show();


        }
        return status;
    }
    public boolean isEmailEmpty() {
        boolean status;

        if (Utility.isNotNull(s_email)) {
            status = true;
        } else {
            status = false;

            textt.setText("Email cannot be empty");
            toastt.setView(layoutt);
            toastt.show();


        }
        return status;
    }
    public void clickNext(View v) {
        s_email = txt_email.getText().toString();
        s_pwd = txt_pwd.getText().toString();
        s_cpwd = txt_cpwd.getText().toString();

        s_email = s_email.trim();
        s_email = s_email.replaceAll(" ", "");
        s_pwd = s_pwd.trim();
        s_pwd = s_pwd.replaceAll(" ", "");
        s_cpwd = s_cpwd.trim();
        s_cpwd = s_cpwd.replaceAll(" ", "");

            if (isEmailEmpty() && isEmailCorrectFormt(s_email) && isPasswordEmpty() && isPasswordhaveSixDigits() && isConfirmPasswordEmpty() && isConfirmPasswordCorrect()) {

                Intent intent = new Intent(getBaseContext(), LetsGo.class);

                intent.putExtra("ex_email", s_email);
                intent.putExtra("ex_password", s_pwd);
                startActivity(intent);
            }else{

            }


    }
    public void clickSignin(View v) {
      //  Intent in = new Intent(Register.this, MainActivity.class);
      //  startActivity(in);
    }
    public void clickForgotPassword(View v) {
        getForgotPassword().show();
    }



    private AlertDialog getForgotPassword() {

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();
        final View layoutView = inflater.inflate(R.layout.forgotpasswordalert, null, false);
        builder2.setView(layoutView);

        txtEmailAddress = (EditText) layoutView.findViewById(R.id.txtEmailAddress);

//===============================================================================
        builder2.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {


                msg = txtEmailAddress.getText().toString();

                if (msg == null || msg.isEmpty() || msg.equals("")) {

                    textt.setText("Please enter email");
                    toastt.setView(layoutt);
                    toastt.show();


                } else {
                    forgotPasswordSent();
                }

            }
        });

        builder2.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder2.create();

    }

    public void forgotPasswordSent() {
        if (Utility.isInternetAvailable(getApplicationContext())) {

            prgDialog.show();
            prgDialog.setMessage("Please wait...");

            updateOnline_forgotPassword task = new updateOnline_forgotPassword();
            task.execute(new String[]{ApiClient.BASE_URL_live});


        } else {

            textt.setText("Unable to detect an active internet connection");
            toastt.setView(layoutt);
            toastt.show();


        }

    }

    private class updateOnline_forgotPassword extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            makePostRequest_updateOnline_forgotPassword();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            prgDialog.cancel();
            if (statusFromServiceAPI_db.equals("0")) {
                System.out.println(".........Passord Reset Email Sucessfully Sent !.........." + statusFromServiceAPI_db);
                // Toast.makeText(getApplicationContext(), "Passord Reset Email Sucessfully Sent !", Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setTitle("Please check email to reset password !");
                // builder.setMessage("Please visit your mailbox and verify email to activate your account.");
                DialogInterface.OnClickListener diocl = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // finish();
                        dialog.cancel();
                    }
                };
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
//                        Intent in = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(in);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
            if (statusFromServiceAPI_db.equals("17")) {

                textt.setText("Invalid User !");
                toastt.setView(layoutt);
                toastt.show();


            }
            if (statusFromServiceAPI_db.equals("14")) {
                textt.setText("Invalid Email Address !");
                toastt.setView(layoutt);
                toastt.show();


            }
        }

    }

    private void makePostRequest_updateOnline_forgotPassword() {
        //  prgDialog.show();
        HttpClient httpClient = new DefaultHttpClient();
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));


        String jsonStr =
                "{" +
                        "\"email\": \"" + msg + "\"" +
                        "}";


        nameValuePair.add(new BasicNameValuePair("method", "forgotPassword"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));

        System.out.println("...........FEEDBACK SENDING.................." + nameValuePair.toString());

        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            HttpResponse response = httpClient.execute(httpPost);


            String responseString = null;
            try {
                responseString = EntityUtils.toString(response.getEntity());

            } catch (IOException e) {
                e.printStackTrace();
            }


            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(responseString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            statusFromServiceAPI_db = jsonObj.optString("result").toString();

                        String res = jsonObj.optString("recived_data").toString();
                        System.out.println("..forgot password  .response sukri......................................." + res);


                        System.out.println("...........forgot password STATUS..---....................." + statusFromServiceAPI_db);
                        //  String errors = jsonObj.optString("errors").toString();

                        // System.out.println("..............********..........................." + userIdFromServiceAPI);



        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }


    }






}
