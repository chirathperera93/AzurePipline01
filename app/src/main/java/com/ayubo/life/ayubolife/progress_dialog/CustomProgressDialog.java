package com.ayubo.life.ayubolife.progress_dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;

public class CustomProgressDialog extends ProgressDialog {
	
//	private Animation animation;
	private Context context;
//	private ImageView imageView;
	private TextView textView;
	private String msg;
	
	public static ProgressDialog ctor(Context context, String msg) {
		CustomProgressDialog dialog = new CustomProgressDialog (context, msg);

		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		
		return dialog;
	}

	public CustomProgressDialog(Context context, String msg) {
		super(context);
		this.msg = msg;
	}
	
	public CustomProgressDialog(Context context, int theme){
		 super(context, theme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.customprogressdialogview);

	}
	
	@Override
	public void show() {
		super.show();
		
	/*	ImageView imageView = (ImageView) findViewById(R.id.animation);
	    imageView.setBackgroundResource(R.drawable.spinnerwhite);
	    
	    textView = (TextView) findViewById(R.id.message);
	    textView.setText(msg);
	    
	    animation = AnimationUtils.loadAnimation(super.getContext(), R.anim.rotate);
	    imageView.startAnimation(animation);*/
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
	}
	
}
