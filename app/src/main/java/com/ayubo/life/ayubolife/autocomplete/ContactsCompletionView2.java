package com.ayubo.life.ayubolife.autocomplete;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.model.DBString;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;


/**
 * Sample token completion view for basic contact info
 * <p>
 * Created on 9/12/13.
 *
 * @author mgod
 */
public class ContactsCompletionView2 extends TokenCompleteTextView<DBString> {
    ArrayList<DBString> doctorNamesList = new ArrayList<DBString>();
    ;

    public ContactsCompletionView2(Context context) {
        super(context);
    }

    public ContactsCompletionView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactsCompletionView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(DBString person) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TokenTextView token = (TokenTextView) l.inflate(R.layout.contact_token, (ViewGroup) getParent(), false);

        System.out.println("-----------------Doctor Names----------------...>>>" + person.getName());
        token.setText(person.getName());
        // token.setText("");
        doctorNamesList.add(person);
        //   Ram.setDoctorNamesList(doctorNamesList);

        System.out.println("......fg........" + person.getName());
        return token;
    }

    @Override
    protected DBString defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
        int index = completionText.indexOf('@');

        if (index == -1) {
            return new DBString(completionText, completionText.replace(" ", "") + "");
        } else {
            // return new Person(completionText.substring(0, index), completionText);
            return new DBString(completionText.substring(0, index), completionText);
        }
    }
}
