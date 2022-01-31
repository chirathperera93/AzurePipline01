package com.ayubo.life.ayubolife.channeling.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.channeling.model.DownloadDataBuilder;
import com.ayubo.life.ayubolife.channeling.util.AppHandler;
import com.ayubo.life.ayubolife.channeling.util.DownloadData;
import com.ayubo.life.ayubolife.channeling.util.DownloadManager;
import com.ayubo.life.ayubolife.channeling.view.PinEntryDialogActionView;
import com.ayubo.life.ayubolife.channeling.view.PinEntryEditText;

import java.io.Serializable;

public class CustomDigitEntryDialogActivity extends AppCompatActivity implements Serializable {

    //constant
    public static final String EXTRA_ACTION_OBJECT = "action_object";

    //instances
    protected EntryDialogActionView actionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_digit_entry_dialog);

        readData();
        initViews();
    }

    private void readData() {

        String correlator = getIntent().getStringExtra("correlator");
        String trId = getIntent().getStringExtra("trId");

        actionView= new PinEntryDialogActionView(this,correlator, trId);

//        if (intent != null && intent.hasExtra(EXTRA_ACTION_OBJECT))
//            actionView = (EntryDialogActionView) intent.getSerializableExtra(EXTRA_ACTION_OBJECT);
    }

    private void initViews() {
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.txtTitle.setText(actionView.getTitle(this));
        viewHolder.btnSubmit.setOnClickListener(actionView.setOnClickListener(this, viewHolder.edtValue));
        actionView.initEntryValue(this, viewHolder.edtValue);

        viewHolder.edtValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 4) {
                    getData(viewHolder);
                }
            }
        });

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        viewHolder.edtValue.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                viewHolder.edtValue.requestFocus();
                imm.showSoftInput(viewHolder.edtValue, 0);
            }
        }, 100);
    }

    private void getData(final ViewHolder viewHolder) {
        viewHolder.progressBar.setVisibility(View.VISIBLE);
        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_DAILOG_PIN, actionView.getParams(viewHolder.edtValue))).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        viewHolder.progressBar.setVisibility(View.GONE);
                        actionView.readResponse(CustomDigitEntryDialogActivity.this, viewHolder.edtValue, response);
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        Toast.makeText(CustomDigitEntryDialogActivity.this, "Error - " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }

    class ViewHolder {
        TextView txtTitle, btnSubmit;
        PinEntryEditText edtValue;
        ProgressBar progressBar;

        ViewHolder() {
            txtTitle = findViewById(R.id.txt_title_digit_entry_dialog);
            edtValue = findViewById(R.id.edt_value_entry_dialog);
            btnSubmit = findViewById(R.id.txt_submit_digit_entry_dialog);
            progressBar = findViewById(R.id.prg_entry_dialog);
        }
    }

    public interface EntryDialogActionView {
        String getTitle(Context context);

        View.OnClickListener setOnClickListener(Activity activity, PinEntryEditText value);

        void initEntryValue(Activity activity, PinEntryEditText value);

        void readResponse(Activity activity, PinEntryEditText value, String response);

        String getParams(PinEntryEditText value);
    }
}
