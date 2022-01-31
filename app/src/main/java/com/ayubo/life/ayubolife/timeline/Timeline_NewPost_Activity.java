package com.ayubo.life.ayubolife.timeline;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.home.PostDataList_Activity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.revamp.v1.fragment.V1NewFeedActivity;
import com.ayubo.life.ayubolife.utility.AndroidMultiPartEntity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenVideo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;


public class Timeline_NewPost_Activity extends AppCompatActivity {
    String appToken = null;

    final int PERMISSIONS_SELECT_VIDEO_REQUEST = 5333;
    final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    int SELECT_FILE = 1;
    String image_absolute_path;
    ImageView post_img;
    Bitmap selectedImageBitmap;
    File newf;
    EditText txt_posttext_tv;
    private Uri fileUri;
    boolean isImageAvailable = false;
    ProgressDialog prgDialog;
    ImageButton btn_select_commiunity;
    TextView txt_select_commiunity;
    LinearLayout comminity_view;
    String message, userid_ExistingUser, postType, communityID;
    long totalSize;
    VideoPicker videoPicker = null;
    String videoThumb = null;
    Bitmap videoThumbBitmap = null;
    Bitmap bitmap;
    File videoFileObj = null;
    long videoSize;
    String community_ids = null;
    String selectedImageType = null;
    Uri selectedImageUri = null;
    PrefManager pref;
    String commiunityName;
    //io.github.ponnamkarthik.richlinkpreview.RichLinkView richLinkView;

    ImageView web_link_icon, imgv_user;
    TextView web_link_title, web_link_desc, web_link_url, txt_username;
    LinearLayout richLinkView;
    // private io.github.ponnamkarthik.richlinkpreview.MetaData data;
    String webLink, webLinkDone, thumbnail_url, link_url, userName, userImage;
    boolean isWebPreviewLoaded = false;
    String URL = null;
    String linkDesc = null;
    //    LinkPreviewCallback linkPreviewCallback = null;
    LinearLayout txt_btn_post_layout, lay_bottom_menu;
    //    TextCrawler textCrawler;
    RelativeLayout post_img_layout;
    ImageButton btn_deleted_image;
    File outputFile = null;
    String service_unavailable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String noticolor = "#000000";
        int whiteInt = Color.parseColor(noticolor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(whiteInt);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();

            //    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            decor.setSystemUiVisibility(0);
            //   decor.setSystemUiVisibility(0);
            // }
        }

        //  setContentView(R.layout.activity_new_home_with_side_menu_lipfeplus);


        setContentView(R.layout.activity_timeline__new_post_);

        pref = new PrefManager(this);
        postType = "NORMAL";
        pref = new PrefManager(Timeline_NewPost_Activity.this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        userName = pref.getUserProfile().get("name");
        userImage = pref.getLoginUser().get("image_path");
        Random rand = new Random();
        int n = rand.nextInt(50) + 1;
        String rannum = Integer.toString(n);
        userImage = MAIN_URL_LIVE_HAPPY + userImage + "&cache=" + rannum;

        service_unavailable = "Server is not available";


        appToken = pref.getUserToken();
        URL = ApiClient.BASE_URL_NEW;

        videoPicker = new VideoPicker(Timeline_NewPost_Activity.this);
        videoPicker.setVideoPickerCallback(new VideoPickerCallback() {
                                               @Override
                                               public void onVideosChosen(List<ChosenVideo> list) {
                                                   System.out.println("sdsfsd");
                                                   ChosenVideo obj = list.get(0);
                                                   videoThumb = obj.getPreviewThumbnail();
                                               }

                                               @Override
                                               public void onError(String message) {
                                                   System.out.println(message);
                                                   // Do error handling
                                               }
                                           }
        );
        richLinkView = (LinearLayout) findViewById(R.id.richLinkView);
        web_link_title = (TextView) findViewById(R.id.web_link_title);
        web_link_desc = (TextView) findViewById(R.id.web_link_desc);
        web_link_url = (TextView) findViewById(R.id.web_link_url);
        web_link_icon = (ImageView) findViewById(R.id.web_link_icon);
        imgv_user = (ImageView) findViewById(R.id.imgv_user);

        lay_bottom_menu = (LinearLayout) findViewById(R.id.lay_bottom_menu);

        post_img_layout = (RelativeLayout) findViewById(R.id.post_img_layout);

        btn_deleted_image = (ImageButton) findViewById(R.id.btn_deleted_image);
        btn_deleted_image.setVisibility(View.GONE);

        txt_username = (TextView) findViewById(R.id.txt_username);
        RequestOptions requestOptions = new RequestOptions()
                .fitCenter()
                .transform(new CircleTransform(Timeline_NewPost_Activity.this))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(Timeline_NewPost_Activity.this).load(userImage)
                .transition(withCrossFade())
                .thumbnail(0.5f)
                .apply(requestOptions)
                .into(imgv_user);

        txt_username.setText(userName);

        txt_btn_post_layout = (LinearLayout) findViewById(R.id.txt_btn_post_layout);
//        textCrawler = new TextCrawler();

//        linkPreviewCallback = new LinkPreviewCallback() {
//            @Override
//            public void onPre() {
//                System.out.println("======onPre==========");
//                // Any work that needs to be done before generating the preview. Usually inflate
//                // your custom preview layout here.
//            }
//
//            @Override
//            public void onPos(SourceContent sourceContent, boolean b) {
//
//                richLinkView.setVisibility(View.VISIBLE);
//
//                String image = sourceContent.getImages().toString();
//                String title = sourceContent.getTitle().toString();
//                linkDesc = sourceContent.getDescription().toString();
//                link_url = sourceContent.getUrl().toString();
//
//                web_link_title.setText(title);
//                web_link_desc.setText(linkDesc);
//                web_link_url.setText(link_url);
//
//                thumbnail_url = image.substring(1, image.length() - 1);
//                RequestOptions requestOptions = new RequestOptions()
//                        .fitCenter()
//                        .diskCacheStrategy(DiskCacheStrategy.NONE);
//                Glide.with(Timeline_NewPost_Activity.this).load(thumbnail_url)
//                        .apply(requestOptions)
//                        .into(web_link_icon);
//
//                System.out.println("======onPos==========" + title);
//                System.out.println("======onPos==========" + linkDesc);
//                System.out.println("======onPos==========" + link_url);
//                System.out.println("======onPos==========" + image);
//
//                if (title.length() > 2) {
//                    webLinkDone = link_url;
//                    isWebPreviewLoaded = true;
//                    lay_bottom_menu.setVisibility(View.GONE);
//                    post_img_layout.setVisibility(View.GONE);
//                }
//
//                // Populate your preview layout with the results of sourceContent.
//            }
//        };

        richLinkView.setVisibility(View.GONE);

        prgDialog = new ProgressDialog(Timeline_NewPost_Activity.this);
        prgDialog.setMessage("Uploading post...");
        prgDialog.setCancelable(false);

        btn_select_commiunity = (ImageButton) findViewById(R.id.btn_select_commiunity);
        txt_select_commiunity = (TextView) findViewById(R.id.txt_select_commiunity);
        comminity_view = (LinearLayout) findViewById(R.id.comminity_view);

        post_img = (ImageView) findViewById(R.id.post_img);

        txt_posttext_tv = (EditText) findViewById(R.id.txt_posttext_tv);
//        txt_posttext_tv.addTextChangedListener(new android.text.TextWatcher() {
//            public void afterTextChanged(android.text.Editable s) {
//                // Do something after Text Change
//                extractUrlsNew2(s.toString(), textCrawler);
//                //  extractUrlsNew(s.toString(),richPreview);
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // Do something before Text Change
//                System.out.println("==========================" + s.toString());
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // Do something while Text Change
//                System.out.println("==========================" + s.toString());
//            }
//        });


        ImageButton btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);

        comminity_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Timeline_NewPost_Activity.this, PostDataList_Activity.class);
                intent.putExtra("from", "NewPost");
                startActivity(intent);

            }
        });
        btn_select_commiunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Timeline_NewPost_Activity.this, PostDataList_Activity.class);
                startActivity(intent);
            }
        });

        txt_select_commiunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Timeline_NewPost_Activity.this, PostDataList_Activity.class);
                startActivity(intent);
            }
        });


        LinearLayout btn_backImgBtn_layout = (LinearLayout) findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton txt_btn_post = (ImageButton) findViewById(R.id.txt_btn_post);

        btn_deleted_image.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Bitmap myBitmap = BitmapFactory.decodeResource(Timeline_NewPost_Activity.this.getResources(), R.drawable.picturephold);
                                                     post_img.setImageBitmap(myBitmap);
                                                     post_img.setImageBitmap(myBitmap);
                                                     newf = null;
                                                     videoFileObj = null;
                                                 }
                                             }
        );


        txt_btn_post_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = txt_posttext_tv.getText().toString();

                if (isWebPreviewLoaded) {
                    postType = "LINK";
                }

                if ((!postType.equals("LINK")) && (newf == null) && (videoFileObj == null) && (message.length() > 2)) {
                    postType = "NORMAL";
                }

                if (postType.equals("NORMAL")) {
                    System.out.println("================NORMAL=====================");
                    if ((message.equals(""))) {
                        Toast.makeText(getBaseContext(), "Please type a message", Toast.LENGTH_LONG).show();
                        return;
                    }
                    new UploadNormal().execute();
                } else if (postType.equals("IMAGE")) {
                    System.out.println("================IMAGE=====================");
                    if (newf == null) {
                        Toast.makeText(getBaseContext(), "Please select a image", Toast.LENGTH_LONG).show();
                    } else {
                        new UploadImage().execute();
                    }
                } else if (postType.equals("VIDEO")) {
                    System.out.println("================VIDEO=====================");
                    if (videoFileObj == null) {
                        Toast.makeText(getBaseContext(), "Please select a video", Toast.LENGTH_LONG).show();
                    } else {
                        new UploadVideo().execute();
                    }
                } else if (postType.equals("LINK")) {
                    System.out.println("================VIDEO=====================");
                    new UploadLink().execute();
                } else if (postType.equals("GIF")) {
                    System.out.println("================GIF=====================");
                    if (newf == null) {
                        Toast.makeText(getBaseContext(), "Please select a image", Toast.LENGTH_LONG).show();
                    } else {
                        new UploadGif().execute();
                    }
                }

            }
        });


        txt_btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = txt_posttext_tv.getText().toString();

                if (isWebPreviewLoaded) {
                    postType = "LINK";
                }

                if ((!postType.equals("LINK")) && (newf == null) && (videoFileObj == null) && (message.length() > 2)) {
                    postType = "NORMAL";
                }

                if (postType.equals("NORMAL")) {
                    System.out.println("================NORMAL=====================");
                    if ((message.equals(""))) {
                        Toast.makeText(getBaseContext(), "Please type a message", Toast.LENGTH_LONG).show();
                        return;
                    }
                    new UploadNormal().execute();
                } else if (postType.equals("IMAGE")) {
                    System.out.println("================IMAGE=====================");
                    if (newf == null) {
                        Toast.makeText(getBaseContext(), "Please select a image", Toast.LENGTH_LONG).show();
                    } else {
                        new UploadImage().execute();
                    }
                } else if (postType.equals("VIDEO")) {
                    System.out.println("================VIDEO=====================");
                    if (videoFileObj == null) {
                        Toast.makeText(getBaseContext(), "Please select a video", Toast.LENGTH_LONG).show();
                    } else {
                        new UploadVideo().execute();
                    }
                } else if (postType.equals("LINK")) {
                    System.out.println("================VIDEO=====================");
                    new UploadLink().execute();
                } else if (postType.equals("GIF")) {
                    System.out.println("================GIF=====================");
                    if (newf == null) {
                        Toast.makeText(getBaseContext(), "Please select a image", Toast.LENGTH_LONG).show();
                    } else {
                        new UploadGif().execute();
                    }
                }

            }
        });


        ImageButton btn_add_video = (ImageButton) findViewById(R.id.btn_add_video);
        btn_add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionOpenSDCard_Video();
            }
        });

        TextView btn_text_add_video = (TextView) findViewById(R.id.btn_text_add_video);
        btn_text_add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionOpenSDCard_Video();

            }
        });

        ImageButton btn_add_photo = (ImageButton) findViewById(R.id.btn_add_photo);
        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionOpenSDCard();
            }
        });

        TextView btn_text_add_photo = (TextView) findViewById(R.id.btn_text_add_photo);
        btn_text_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermissionOpenSDCard();

            }
        });


    }

//    public void extractUrlsNew2(String input, TextCrawler view) {
//
//        String test = input.toLowerCase();
//
//        List<String> result = new java.util.ArrayList<String>();
//        String[] words = test.split("\\s+");
//
//        //  String pattern ="^((https:\\/\\/www\\.)|(http:\\/\\/www\\.)|(www\\.)|(http:\\/\\/))[a-zA-Z0-9._-]+\\.[a-zA-Z.]{2,5}$";
//        String pattern = "^(https?:\\/\\/)?(www\\.)?[a-zA-Z0-9._-]{2,}\\.[a-zA-Z0-9._-]{2,}$";
//        //  java.util.regex.Pattern pattern = android.util.Patterns;
//        java.util.regex.Pattern patt = java.util.regex.Pattern.compile(pattern);
//        String text;
//        for (String word : words) {
//            System.out.println("=====w==========" + word);
//
//
//            if (patt.matcher(word).find()) {
//                System.out.println("====matched=========" + word);
//                text = word;
//                if (!word.toLowerCase().contains("http://") && !word.toLowerCase().contains("https://")) {
//                    word = "http://" + word;
//                    webLink = text;
//                    if (!isWebPreviewLoaded) {
//                        view.makePreview(linkPreviewCallback, word);
//                    } else {
//                        if (webLinkDone.equals(text)) {
//
//                            view.makePreview(linkPreviewCallback, word);
//                        } else {
//                        }
//
//                    }
//                    System.out.println("====matched===0======" + word);
//                } else {
//
//                    view.makePreview(linkPreviewCallback, word);
//                    System.out.println("====matched===1======" + word);
//                }
//            } else {
//                if (webLinkDone != null && (webLinkDone.length() > 3)) {
//                    if (input.contains(webLinkDone)) {
//                    } else {
//                        web_link_title.setText("");
//                        web_link_desc.setText("");
//                        web_link_url.setText("");
//
//                        richLinkView.setVisibility(View.GONE);
//                        isWebPreviewLoaded = false;
//                    }
//                }
//            }
//        }
//        // return result;
//    }


    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new java.util.ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(urlRegex, java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }


    @Override
    protected void onResume() {
        super.onResume();

        String com_name = pref.getUserSelectedCommiunityName().get("user_selected_commiunity_name");
        community_ids = pref.getUserSelectedCommiunity().get("user_selected_commiunity");

        if ((community_ids.equals("0")) || (community_ids.equals(""))) {
            commiunityName = "Public";
            community_ids = "all";
        } else {
            commiunityName = com_name;

        }
        txt_select_commiunity.setText(commiunityName);
    }


    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        String sPath = null;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            sPath = contentURI.getPath();
            return sPath;

        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            sPath = cursor.getString(idx);
            return sPath;
        }
    }


    void stringToPojoCast() {

    }


    void callFinished(String dat) {

        Gson gson = new GsonBuilder().create();
        com.ayubo.life.ayubolife.pojo.timeline.Post dataObj = gson.fromJson(dat, com.ayubo.life.ayubolife.pojo.timeline.Post.class);
//        NewHomeDesign.timelinePostlist.add(1, dataObj);
        V1NewFeedActivity.timelinePostlist.add(1, dataObj);

        Toast.makeText(Timeline_NewPost_Activity.this, "Posted successfully", Toast.LENGTH_LONG).show();

        pref.setUserSelectedCommiunity("0");
        pref.setIsNewPostAdded("yes");
        clearView();

//        Intent intent = new Intent(Timeline_NewPost_Activity.this, NewHomeWithSideMenuActivity.class);
//        startActivity(intent);
        finish();
        //  finish();
    }


    private class UploadLink extends AsyncTask<Void, Integer, String> {
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            prgDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL + "post/new/link_post");
            httppost.addHeader("Authorization", appToken);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                entity.addPart("heading", new StringBody(linkDesc));
                entity.addPart("body", new StringBody(message));
                entity.addPart("community_ids", new StringBody(community_ids));
                entity.addPart("thumbnail_url", new StringBody(thumbnail_url));
                entity.addPart("link_url", new StringBody(link_url));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                r_entity.toString();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = org.apache.http.util.EntityUtils.toString((HttpEntity) r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            // showing the server response in an alert dialog
            //  showAlert(result);
            if (prgDialog != null) {
                prgDialog.cancel();
            }

            //   Toast.makeText(Timeline_NewPost_Activity.this, responseString, Toast.LENGTH_LONG).show();

            super.onPostExecute(result);
            JSONObject jsonObj = null;
            String res = null;
            try {
                jsonObj = new JSONObject(result);
                res = jsonObj.optString("result").toString();
                if (res != null) {
                    if (res.equals("0")) {
                        String data = jsonObj.optString("data").toString();
                        callFinished(data);
                    } else {
                        System.out.println("Message posting error " + result);
                        Toast.makeText(Timeline_NewPost_Activity.this, service_unavailable, Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(Timeline_NewPost_Activity.this, service_unavailable, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private class UploadVideo extends AsyncTask<Void, Integer, String> {
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // prgDialog.setMessage("Uploading post...");
            prgDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {

            // appToken="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2FwaS5heXViby5saWZlL3B1YmxpYy9hdXRoL2xvZ2luIiwiaWF0IjoxNTI4NDM0MTQ3LCJleHAiOjE1MzEzNDcyNjcsIm5iZiI6MTUyODQzNDE0NywianRpIjoiYmI1NjgwNzk1NGNiOGU4YzRkM2YzNjU0YzljNmY1OTgiLCJzdWIiOjEwMDU3NiwiaWQiOjEwMDU3Nn0.t7wITdW6aEBKXbDCws8zeUitVkZ6zw3qIh2gaBCrntc";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL + "post/new/video_post");

            httppost.addHeader("Authorization", appToken);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File videoThumbBitmapFile = savePNGBitmap(videoThumbBitmap, "timelinevideo.png");

                entity.addPart("heading", new StringBody(""));
                entity.addPart("body", new StringBody(message));
                entity.addPart("community_ids", new StringBody(community_ids));
                entity.addPart("video", new FileBody(videoFileObj));
                entity.addPart("thumbnail", new FileBody(videoThumbBitmapFile));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                r_entity.toString();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = org.apache.http.util.EntityUtils.toString((HttpEntity) r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            if (prgDialog != null) {
                prgDialog.cancel();
            }
            //   Toast.makeText(Timeline_NewPost_Activity.this, responseString, Toast.LENGTH_LONG).show();

            super.onPostExecute(result);
            JSONObject jsonObj = null;
            String res = null;
            try {
                jsonObj = new JSONObject(result);
                res = jsonObj.optString("result").toString();
                if (res != null) {
                    if (res.equals("0")) {
                        String data = jsonObj.optString("data").toString();
                        callFinished(data);
                    } else {
                        System.out.println("Message posting error " + result);
                        Toast.makeText(Timeline_NewPost_Activity.this, service_unavailable, Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(Timeline_NewPost_Activity.this, service_unavailable, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private class UploadImage extends AsyncTask<Void, Integer, String> {
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            prgDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL + "post/new/image_post");

            httppost.addHeader("Authorization", appToken);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                entity.addPart("heading", new StringBody(""));
                entity.addPart("body", new StringBody(message));
                entity.addPart("community_ids", new StringBody(community_ids));
                entity.addPart("image", new FileBody(newf));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                r_entity.toString();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = org.apache.http.util.EntityUtils.toString((HttpEntity) r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            if (prgDialog != null) {
                prgDialog.cancel();
            }
            //   Toast.makeText(Timeline_NewPost_Activity.this, responseString, Toast.LENGTH_LONG).show();
            System.out.println("=============result==============" + result);
            super.onPostExecute(result);
            JSONObject jsonObj = null;
            String res = null;
            try {
                jsonObj = new JSONObject(result);
                res = jsonObj.optString("result").toString();
                if (res != null) {
                    if (res.equals("0")) {
                        String data = jsonObj.optString("data").toString();
                        callFinished(data);
                    } else {
                        System.out.println("Message posting error " + result);
                        Toast.makeText(Timeline_NewPost_Activity.this, service_unavailable, Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(Timeline_NewPost_Activity.this, service_unavailable, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


        }

    }

    private class UploadNormal extends AsyncTask<Void, Integer, String> {
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            prgDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(URL + "post/new/normal_post");
            httppost.addHeader("Authorization", appToken);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                entity.addPart("heading", new StringBody(""));
                entity.addPart("body", new StringBody(message));
                entity.addPart("community_ids", new StringBody(community_ids));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                r_entity.toString();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = org.apache.http.util.EntityUtils.toString((HttpEntity) r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            if (prgDialog != null) {
                prgDialog.cancel();
            }
            super.onPostExecute(result);
            JSONObject jsonObj = null;
            String res = null;
            System.out.println("============result================" + result);
            try {
                jsonObj = new JSONObject(result);
                res = jsonObj.optString("result").toString();
                if (res != null) {
                    if (res.equals("0")) {
                        String data = jsonObj.optString("data").toString();
                        callFinished(data);
                    } else {
                        Toast.makeText(Timeline_NewPost_Activity.this, service_unavailable, Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(Timeline_NewPost_Activity.this, service_unavailable, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }

    }

    private class UploadGif extends AsyncTask<Void, Integer, String> {
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog.setMessage("Uploading post...");
            prgDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "yourfile");


            HttpPost httppost = new HttpPost(URL + "post/new/gif_post");
            HttpClient httpclient = new DefaultHttpClient();

            httppost.addHeader("Authorization", appToken);
            //  httppost.setHeader(HTTP.CONTENT_TYPE,"image/gif");
            try {

                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                entity.addPart("heading", new StringBody(""));
                entity.addPart("body", new StringBody(message));
                entity.addPart("community_ids", new StringBody(community_ids));
                entity.addPart("image", new FileBody(newf));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);


                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                r_entity.toString();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = org.apache.http.util.EntityUtils.toString((HttpEntity) r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            // showing the server response in an alert dialog
            //  showAlert(result);
            prgDialog.cancel();
            //  Toast.makeText(Timeline_NewPost_Activity.this, responseString, Toast.LENGTH_LONG).show();

            super.onPostExecute(result);
            JSONObject jsonObj = null;
            String res = null;
            try {
                jsonObj = new JSONObject(result);
                res = jsonObj.optString("result").toString();
                if (res != null) {
                    if (res.equals("0")) {
                        String data = jsonObj.optString("data").toString();
                        callFinished(data);
                    } else {
                        System.out.println("Posting error " + result);
                        Toast.makeText(Timeline_NewPost_Activity.this, service_unavailable, Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(Timeline_NewPost_Activity.this, service_unavailable, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


        }

    }

    public File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            //  Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }


    void clearView() {
        post_img.setVisibility(View.VISIBLE);
        videoFileObj = null;
        newf = null;
        message = "";
        linkDesc = "";
        community_ids = "all";
        thumbnail_url = "";
        link_url = "";


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String ur = null;
        clearView();
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == Picker.PICK_VIDEO_DEVICE) {

                    videoPicker.submit(data);
                    Uri selectedImageUri = data.getData();
                    String path2 = getPath(selectedImageUri);
                    videoThumbBitmap = ThumbnailUtils.createVideoThumbnail(path2,
                            MediaStore.Images.Thumbnails.MINI_KIND);
                    post_img.setImageBitmap(videoThumbBitmap);

                    btn_deleted_image.setVisibility(View.VISIBLE);

                    videoFileObj = new File(path2);

                    postType = "VIDEO";
                } else if (requestCode == PERMISSIONS_MULTIPLE_REQUEST) {
                    ur = fileUri.getPath();
                    image_absolute_path = fileUri.getPath();
                    setImageToUI(fileUri.getPath());
                    btn_deleted_image.setVisibility(View.VISIBLE);
                } else if (requestCode == SELECT_FILE) {
                    // postType="IMAGE";
                    btn_deleted_image.setVisibility(View.VISIBLE);

                    selectedImageUri = data.getData();


                    onSelectFromGalleryResult(data);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case PERMISSIONS_MULTIPLE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    selectImagePopup();

                } else {
                    // write your logic code if permission already granted
                    //    captureImage_camera();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case PERMISSIONS_SELECT_VIDEO_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    videoPicker.pickVideo();

                } else {
                    // write your logic code if permission already granted
                    //    captureImage_camera();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    String getImageType(Uri uri) {
        String type = null;
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        type = mime.getExtensionFromMimeType(cR.getType(uri));

        return type;
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        //  Ram.setHomeFragmentNumber("1");
        //  Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        try {
            data.getData();
            selectedImageUri = data.getData();


            selectedImageType = getImageType(selectedImageUri);

            Cursor cursor;
            String[] projection = {MediaStore.MediaColumns.DATA};
            String selectedImagePath = null;

            cursor = Timeline_NewPost_Activity.this.getContentResolver().query(selectedImageUri, projection, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {

                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                selectedImagePath = cursor.getString(column_index);
                image_absolute_path = selectedImagePath;
                System.out.println("=============================" + image_absolute_path);


                setImageToUI(image_absolute_path);

            } else {
                Bitmap myBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.camera);
                //  Bitmap circularBitmap = getRoundedCornerBitmap(myBitmap, 150);
                post_img.setImageBitmap(myBitmap);
            }
            //========================
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRealPathFromURI2(Context context, Uri contentUri) {
        //     fileUri.getPath()
        //   fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        String path = null;

        path = fileUri.getPath();
        Cursor cursor = null;
        return path;
//        try {
//            String[] proj = { MediaStore.Images.Media.DATA };
//            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
    }

    private void setImageToUI(String uri) {


        int orientation = getOrientation(uri);
        System.out.println("==========================orientation=======" + orientation);


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        selectedImageBitmap = BitmapFactory.decodeFile(image_absolute_path, options);
        Matrix matrix = new Matrix();

        if (orientation == 90) {
            matrix.postRotate(90);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(selectedImageBitmap, 200, 200, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            post_img.setImageBitmap(rotatedBitmap);
        } else if (orientation == 180) {
            matrix.postRotate(180);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(selectedImageBitmap, 200, 200, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            post_img.setImageBitmap(rotatedBitmap);
        } else if (orientation == 270) {
            matrix.postRotate(270);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(selectedImageBitmap, 200, 200, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            post_img.setImageBitmap(rotatedBitmap);
        } else {
            post_img.setImageBitmap(selectedImageBitmap);
        }

        if (selectedImageType.equals("gif")) {
            postType = "GIF";
            newf = new File(image_absolute_path);

        } else {
            postType = "IMAGE";
            newf = resizeAndCompressImageBeforeSend(Timeline_NewPost_Activity.this, image_absolute_path, "postImage.jpeg");
        }
    }


    private File createUploadFile(Intent data) {
        File file = null;
        Uri selectedImageUri = data.getData();

        String path = selectedImageUri.getPath();

        System.out.println(path);
        // String path= getRealPathFromURI(Timeline_NewPost_Activity.this,selectedImageUri);
        // file= new File(selectedImageUri.getPath());

        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        //  File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            //  bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private static File savePNGBitmap(Bitmap bm, String fileName) {
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Aubopost";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            //   bm.compress(Bitmap.CompressFormat., 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private static File saveGIFBitmap(Bitmap bm, String fileName) {
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Aubopost";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            // FileOutputStream fOut = new FileOutputStream(file);
            // bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            //   bm.compress(Bitmap.CompressFormat., 90, fOut);
            //  fOut.flush();
            //  fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public int getOrientation(String path) {
        int rotate = 0;
        try {
            //getContentResolver().notifyChange(photoUri, null);

            File imageFile = new File(path);
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imageFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    rotate = 0;
                    break;
            }
            //Log.v(Common.TAG, "Exif orientation: " + orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
        /****** Image rotation ****/

    }

    private void checkPermissionOpenSDCard_Video() {
        if (ContextCompat
                .checkSelfPermission(Timeline_NewPost_Activity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (Timeline_NewPost_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar.make(Timeline_NewPost_Activity.this.findViewById(android.R.id.content),
                        "This app needs storage permission.",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 23)
                                    requestPermissions(
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            PERMISSIONS_SELECT_VIDEO_REQUEST);
                            }
                        }).show();
            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_SELECT_VIDEO_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            videoPicker.pickVideo();

        }
    }

    private void checkPermissionOpenSDCard() {
        if (ContextCompat
                .checkSelfPermission(Timeline_NewPost_Activity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (Timeline_NewPost_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar.make(Timeline_NewPost_Activity.this.findViewById(android.R.id.content),
                        "This app needs storage permission.",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 23)
                                    requestPermissions(
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            selectImagePopup();


        }
    }

    private void selectImagePopup() {


        try {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File resizeAndCompressImageBeforeSend(Context context, String filePath, String fileName) {
        final int MAX_IMAGE_SIZE = 200 * 200; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 300, 300);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath, options);

        Bitmap bmpPicNew = null;

        int compressQuality = 5; // quality decreasing by 5 every loop.
        int streamLength;
        do {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength / 1024 + " kb");
        } while (streamLength >= MAX_IMAGE_SIZE);

        try {

            //save the resized and compressed file to disk cache
            Log.d("compressBitmap", "cacheDir: " + context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir() + fileName);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file");
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmpPic.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sp = context.getCacheDir() + "/" + fileName;
        //return the path of resized and compressed file
        return destination;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag, "image height: " + height + "---image width: " + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }

    public void cannotUploadAlert(Context c, String msg) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_ok_only, null, false);

        builder.setView(layoutView);
        // alert_ask_go_back_to_map
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        //  TextView lbl_alert_title = (TextView) layoutView.findViewById(R.id.lbl_alert_title);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);


        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_ok);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post_img.setVisibility(View.INVISIBLE);
                dialog.cancel();

                //   finish();

            }
        });

        dialog.show();
    }


}
