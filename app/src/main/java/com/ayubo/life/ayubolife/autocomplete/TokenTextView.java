package com.ayubo.life.ayubolife.autocomplete;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;

/**
 * Created by mgod on 5/27/15.
 *
 * Simple custom view example to show how to get selected events from the token
 * view. See ContactsCompletionView and contact_token.xml for usage
 */
public class TokenTextView extends TextView {

    public TokenTextView(Context context) {
        super(context);
    }

    public TokenTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
       setCompoundDrawablesWithIntrinsicBounds(0, 0, false ? R.drawable.close_xx : 0, 0);
    }


    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setCompoundDrawablesWithIntrinsicBounds(0, 0, selected ? R.drawable.close_xx : 0, 0);
    }
}
