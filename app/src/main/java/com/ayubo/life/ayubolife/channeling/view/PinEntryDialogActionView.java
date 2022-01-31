package com.ayubo.life.ayubolife.channeling.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.activity.CustomDigitEntryDialogActivity;
import com.ayubo.life.ayubolife.channeling.model.SoapBasicParams;

public class PinEntryDialogActionView implements CustomDigitEntryDialogActivity.EntryDialogActionView ,Serializable{

    //primary data
    private String correlator;
    private String trId;
    private Context context;


    public PinEntryDialogActionView(Context c,String correlator, String trId) {
        this.context=c;
        this.correlator = correlator;
        this.trId = trId;
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.enter_sms);
    }

    @Override
    public View.OnClickListener setOnClickListener(final Activity activity, final PinEntryEditText editText) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setResult(Activity.RESULT_FIRST_USER);
                activity.finish();

            }
        };
    }

    @Override
    public void initEntryValue(final Activity activity, final PinEntryEditText value) {
        value.setInputNumbers(4);
    }

    @Override
    public void readResponse(Activity activity, PinEntryEditText value, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getInt("result") == 0) {
                activity.setResult(Activity.RESULT_OK);
                activity.finish();
            } else {
                value.setText("");
                Toast.makeText(activity, "Error - " + jsonObject.getString("error"), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getParams(PinEntryEditText value) {
        return new VerificationParams(trId, value.getText().toString(), correlator).getSearchParams();
    }

    class VerificationParams extends SoapBasicParams implements Serializable{
        private String trId;
        private String otpcode;
        private String clientcorrelator;

        VerificationParams(String trId, String otpcode, String clientcorrelator) {

            this.trId = trId;
            this.otpcode = otpcode;
            this.clientcorrelator = clientcorrelator;
        }

        public String getSearchParams() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", user_id);
                jsonObject.put("trId", trId);
                jsonObject.put("otpcode", otpcode);
                jsonObject.put("clientcorrelator", clientcorrelator);
                jsonObject.put("token_key", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }
}
