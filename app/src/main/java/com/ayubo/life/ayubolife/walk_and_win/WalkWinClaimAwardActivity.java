package com.ayubo.life.ayubolife.walk_and_win;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.ayubo.life.ayubolife.BuildConfig;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.bumptech.glide.Glide;
import com.flavors.changes.Constants;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class WalkWinClaimAwardActivity extends BaseActivity {

    PrefManager prefManager;

    ImageView walk_win_reward_icon, walk_win_sponsor_ad, ad_info_icon, ad_close_icon;

    Button walk_and_win_award_share, walk_and_win_new_button;

    TextView walk_win_username, detail_textView, congratulation_textView, walk_win_sub_heading;

    String shareSubject = "";

    RelativeLayout parent, claim_reward_ad_layout;

    LinearLayout getScreenshotArea;

    String action, meta, sponsorLink = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_win_claim_award);
        prefManager = new PrefManager(this);
        walk_win_reward_icon = findViewById(R.id.walk_win_reward_icon);
        detail_textView = findViewById(R.id.detail_textView);
        walk_win_username = findViewById(R.id.walk_win_username);
        walk_and_win_new_button = findViewById(R.id.walk_and_win_new_button);
        congratulation_textView = findViewById(R.id.congratulation_textView);
        ad_info_icon = findViewById(R.id.ad_info_icon);
        ad_close_icon = findViewById(R.id.ad_close_icon);
        walk_win_sponsor_ad = findViewById(R.id.walk_win_sponsor_ad);
        walk_win_sub_heading = findViewById(R.id.walk_win_sub_heading);
        claim_reward_ad_layout = findViewById(R.id.claim_reward_ad_layout);
        getScreenshotArea = findViewById(R.id.getScreenshotArea);
        walk_win_reward_icon.setImageResource(0);
        Integer challengeId = Integer.valueOf(prefManager.getWalkWinChallengeId());
        walk_win_username.setText("");
        walk_win_username.setText("Hi, " + prefManager.getLoginUser().get("name"));
        walk_and_win_new_button.setVisibility(View.GONE);

        parent = findViewById(R.id.parent);

        walk_and_win_award_share = findViewById(R.id.walk_and_win_award_share);

        Bundle bundle = getIntent().getExtras();
        String claimRewardResponse = bundle.getString("claimRewardResponse");
        String gradientStartColor = bundle.getString("gradientStartColor");
        String gradientEndColor = bundle.getString("gradientEndColor");

        JsonObject jsonObject = new JsonParser().parse(claimRewardResponse).getAsJsonObject();


        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor(gradientStartColor), Color.parseColor(gradientEndColor)});
        gd.setCornerRadius(25f);

        walk_win_username.setTextColor(Color.parseColor(gradientEndColor));

        walk_and_win_new_button.setBackgroundDrawable(gd);
        walk_and_win_new_button.setTextColor(getResources().getColor(R.color.white));

        walk_and_win_award_share.setBackgroundDrawable(gd);
        walk_and_win_award_share.setTextColor(getResources().getColor(R.color.white));


        JsonParser jsonParser = new JsonParser();
        JsonObject gsonSponsorObject = (JsonObject) jsonParser.parse(prefManager.getWnWSponsorDetail());

        System.out.println(gsonSponsorObject);

        switch (challengeId) {

            case 1:
                walk_win_reward_icon.setImageResource(R.drawable.daily_reward_icon);
                shareSubject = "My daily walk";
                break;

            case 2:
                walk_win_reward_icon.setImageResource(R.drawable.weekly_reward_icon);
                shareSubject = "My weekly walk";
                break;

            case 3:
                walk_win_reward_icon.setImageResource(R.drawable.monthly_reward_icon);
                shareSubject = "My monthly walk";
                break;

            default:
                walk_win_reward_icon.setImageResource(0);
                congratulation_textView.setText("");
                walk_win_sub_heading.setText("");
                detail_textView.setText("");
                shareSubject = "";
                break;
        }

        congratulation_textView.setText(jsonObject.get("messages").getAsJsonObject().get("title").getAsString());
        walk_win_sub_heading.setText(jsonObject.get("messages").getAsJsonObject().get("subheading").getAsString());
        detail_textView.setText(jsonObject.get("messages").getAsJsonObject().get("description").getAsString());
        action = "";
        meta = "";
        if (
                !jsonObject.get("click").getAsJsonObject().get("action").getAsString().equals("")
                        && !jsonObject.get("click").getAsJsonObject().get("meta").getAsString().equals("")) {
            action = jsonObject.get("click").getAsJsonObject().get("action").getAsString();
            meta = jsonObject.get("click").getAsJsonObject().get("meta").getAsString();
            walk_and_win_new_button.setVisibility(View.VISIBLE);
            walk_and_win_new_button.setText(jsonObject.get("click").getAsJsonObject().get("button_text").getAsString());

        }

        walk_and_win_award_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshotAndShare();
            }
        });

        if (!checkPermissions(WalkWinClaimAwardActivity.this)) {
            ActivityCompat.requestPermissions(WalkWinClaimAwardActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }

        walk_and_win_new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processAction(action, meta);
            }
        });

        String ads = prefManager.getWnWSponsorDetail();

        try {
            JSONObject jsonObject1 = new JSONObject(ads);

            JsonParser jParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jParser.parse(jsonObject1.toString());

            if (!ads.equals("") && gsonObject.size() > 0) {
                claim_reward_ad_layout.setVisibility(View.VISIBLE);
                Glide.with(getBaseContext()).load(gsonObject.get("sp_creatives").getAsJsonObject().get("image_url").getAsString()).centerCrop().into(walk_win_sponsor_ad);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        ad_info_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sponsorLink));
                startActivity(browserIntent);
            }
        });

        ad_close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claim_reward_ad_layout.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        prefManager.saveWnWSponsorDetail(new JsonObject());
    }

    public static boolean checkPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void takeScreenshotAndShare() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";


            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            Bitmap bitmap = loadBitmapFromView(getScreenshotArea, getScreenshotArea.getWidth(), getScreenshotArea.getHeight());
            bitmap = addWaterMark(bitmap);
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            shareScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void shareScreenshot(File imageFile) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(WalkWinClaimAwardActivity.this, BuildConfig.APPLICATION_ID, imageFile));
        sendIntent.setType("image/*");
        Intent shareIntent = Intent.createChooser(sendIntent, "Share Walk and Win Award");
        startActivity(shareIntent);
    }

    private Bitmap addWaterMark(Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Bitmap waterMark;
        if (Constants.type == Constants.Type.AYUBO) {
            waterMark = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.newlogo);
            waterMark = Bitmap.createScaledBitmap(waterMark, 295, 197, true);
        } else {
            waterMark = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher);
        }
        canvas.drawBitmap(waterMark, (canvas.getWidth() / 2) - (waterMark.getWidth() / 2), 50, null);
        return result;
    }

    public Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap returnedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else {
            canvas.drawColor(Color.WHITE);
            v.draw(canvas);
        }
        return returnedBitmap;
    }

}