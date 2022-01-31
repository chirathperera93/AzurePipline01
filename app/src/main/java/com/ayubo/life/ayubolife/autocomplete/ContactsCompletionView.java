package com.ayubo.life.ayubolife.autocomplete;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.utility.Ram;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;


/**
 * Sample token completion view for basic contact info
 * <p>
 * Created on 9/12/13.
 *
 * @author mgod
 */
public class ContactsCompletionView extends TokenCompleteTextView<DBString> {
    ArrayList<DBString> specilistList = new ArrayList<DBString>();

    public ContactsCompletionView(Context context) {
        super(context);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean containsId(ArrayList<DBString> list, String id) {
        for (DBString object : list) {
            if (object.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected View getViewForObject(DBString person) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TokenTextView token = (TokenTextView) l.inflate(R.layout.contact_token, (ViewGroup) getParent(), false);

        specilistList = Ram.getSpecilistList();

        if (containsId(specilistList, person.getId())) {
            System.out.println("-----------------Account already exist ");
            token.setText("");
        } else {
            specilistList.add(person);
            token.setText(person.getName());
            System.out.println("-----------------Account added ..");
        }

        Ram.setSpecilistList(specilistList);

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
