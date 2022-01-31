package com.ayubo.life.ayubolife.autocomplete;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.model.DBString;
import com.tokenautocomplete.FilteredArrayAdapter;

/**
 * Created by appdev on 3/29/2017.
 */

public class TokenActivity extends Activity {
    //    ContactsCompletionView completionView;
    DBString[] people;
    ArrayAdapter<DBString> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        people = new DBString[]{
                new DBString("Marshall Weir", "marshall@example.com"),
                new DBString("Margaret Smith", "margaret@example.com"),
                new DBString("Max Jordan", "max@example.com"),
                new DBString("Meg Peterson", "meg@example.com"),
                new DBString("Amanda Johnson", "amanda@example.com"),
                new DBString("Terry Anderson", "terry@example.com")
        };


        //   adapter = new ArrayAdapter<DBString>(this, android.R.layout.simple_list_item_1, people);
        adapter = new FilteredArrayAdapter<DBString>(this, android.R.layout.simple_list_item_1, people) {
            @Override
            protected boolean keepObject(DBString obj, String mask) {
                mask = mask.toLowerCase();
                return obj.getName().toLowerCase().startsWith(mask) || obj.getId().toLowerCase().startsWith(mask);
            }
        };
//        completionView = (ContactsCompletionView) findViewById(R.id.txt_specilist);
//        completionView.setAdapter(adapter);
    }
}

