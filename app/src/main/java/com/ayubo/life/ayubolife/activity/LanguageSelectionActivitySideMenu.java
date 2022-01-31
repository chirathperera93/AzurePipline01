package com.ayubo.life.ayubolife.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.common.SetDiscoverPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageSelectionActivitySideMenu extends AppCompatActivity {
    private Spinner spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection_side_menu);
        spinner2 = (Spinner) findViewById(R.id.language_selection);
        addItemsOnSpinner2();

    }

    public void goLogin(View v) {
        setLanguage();
//        Intent in = new Intent(LanguageSelectionActivitySideMenu.this, NewHomeWithSideMenuActivity.class);
//        Intent in = new Intent(LanguageSelectionActivitySideMenu.this, LifePlusProgramActivity.class);
//        Intent in = new Intent(getBaseContext(), NewDiscoverActivity.class);
        Intent in = new SetDiscoverPage().getDiscoverIntent(getBaseContext());
        startActivity(in);
    }

    private void setLanguage() {
        String Text = spinner2.getSelectedItem().toString();


        String languageToLoad = null;
        if (Text.equals("English")) {
            languageToLoad = "en";
        } else if (Text.equals("සින්හල")) {
            languageToLoad = "si";
        } else {
            languageToLoad = "en";
        }
        // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public void addItemsOnSpinner2() {

        spinner2 = (Spinner) findViewById(R.id.language_selection);
        List<String> list = new ArrayList<String>();
        list.add("English");
        list.add("සින්හල");
        list.add("ぬふあて");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

}
