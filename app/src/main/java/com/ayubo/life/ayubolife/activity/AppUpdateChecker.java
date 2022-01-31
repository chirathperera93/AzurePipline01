package com.ayubo.life.ayubolife.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.flavors.changes.Constants;

import org.jsoup.Jsoup;

import java.util.concurrent.TimeUnit;

public class AppUpdateChecker {
    private Activity activity;
    private PrefManager pref;

    public AppUpdateChecker(Activity activity) {

        this.activity = activity;
        pref = new PrefManager(this.activity);
    }

    //current version of app installed in the device
    private String getCurrentVersion() {
        PackageManager pm = activity.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo = pm.getPackageInfo(activity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }

        return pInfo.versionName;
    }

    private class GetLatestVersion extends AsyncTask<String, String, String> {
        private String latestVersion;
        private ProgressDialog progressDialog;
        private boolean manualCheck;

        GetLatestVersion(boolean manualCheck) {
            this.manualCheck = manualCheck;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (manualCheck) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }


            String currentVersion = getCurrentVersion();
            //If the versions are not the same

            // check internet connection
            if (isNetworkAvailable()) {
                try {
                    if (latestVersion != null && !latestVersion.equals("Varies with device")) {
                        String[] currentVersionData = currentVersion.split("\\.");
                        int s1 = currentVersionData[2].length();
                        if (s1 == 1) {
                            currentVersionData[2] = currentVersionData[2] + "0";
                        }
                        String currentVersionString = currentVersionData[0] + currentVersionData[1] + currentVersionData[2];


                        String[] latestVersionData = latestVersion.split("\\.");
                        int s2 = latestVersionData[2].length();
                        if (s2 == 1) {
                            latestVersionData[2] = latestVersionData[2] + "0";
                        }
                        String latestVersionString = latestVersionData[0] + latestVersionData[1] + latestVersionData[2];


                        Boolean isOpenApp = pref.getOpenAppUpdateChecker();


                        int currentAppVersion = Integer.parseInt(currentVersionString);
                        int playStoreAppVersion = Integer.parseInt(latestVersionString);


                        if (!isOpenApp && playStoreAppVersion > currentAppVersion) {


//            if (!currentVersion.equals(latestVersion) && latestVersion != null) {


//                            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//                            builder.setTitle("New Update!");
//                            builder.setMessage("There is a newer version of this app available.");
//                            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //Click button action
//                                    pref.setOpenAppUpdateChecker(false);
//                                    dialog.dismiss();
//                                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
//
//                                }
//                            });
//                            builder.setNegativeButton("Remind me tomorrow", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    pref.setUpdateAvailableCountDownTime(System.currentTimeMillis());
//                                    pref.setOpenAppUpdateChecker(false);
//                                    dialog.dismiss();
//                                }
//                            });
////                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
////                        @Override
////                        public void onDismiss(DialogInterface dialogInterface) {
////
////                        }
////                    });
//                            builder.setCancelable(false);
//                            builder.show();
//                            pref.setOpenAppUpdateChecker(true);
//
////                        } else {
//

                            AlertDialog dialogView;
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View layoutView = inflater.inflate(R.layout.new_common_alert_popup, null, false);
                            alertDialogBuilder.setView(layoutView);
                            dialogView = alertDialogBuilder.create();
//                            dialogView.setCancelable(false);

                            dialogView.show();

                            pref.setOpenAppUpdateChecker(true);

                            dialogView.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    try {
                                        pref.setOpenAppUpdateChecker(false);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            TextView title = (TextView) layoutView.findViewById(R.id.title);
                            title.setText("New Update!");

                            TextView description = (TextView) layoutView.findViewById(R.id.description);
                            description.setText("There is a newer version of this app available.");


                            TextView no_btn = (TextView) layoutView.findViewById(R.id.no_btn);
                            no_btn.setText("Remind me tomorrow");
                            no_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        dialogView.dismiss();
                                        pref.setUpdateAvailableCountDownTime(System.currentTimeMillis());
                                        pref.setOpenAppUpdateChecker(false);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });


                            Button yes_btn = (Button) layoutView.findViewById(R.id.yes_btn);

                            if (Constants.type == Constants.Type.LIFEPLUS) {
                                yes_btn.setBackgroundResource(R.drawable.life_plus_all_corners_rounded_gradient);
                            } else {
                                yes_btn.setBackgroundResource(R.drawable.ayubo_life_all_corners_rounded_gradient);
                            }

                            yes_btn.setText("Update");
                            yes_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        dialogView.dismiss();
                                        pref.setOpenAppUpdateChecker(false);
                                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            if (manualCheck) {
                                Toast.makeText(activity, "No Update Available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (manualCheck) {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage("Checking For Update.....");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime

                String val = "https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "&hl=en";
                System.out.println(val);

                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(7)
                        .ownText();
                return latestVersion;
            } catch (Exception e) {
                return latestVersion;
            }
        }
    }

    public void checkForUpdate(boolean manualCheck) {


        long updateAvailableCountDownTime = Long.parseLong(pref.getUpdateAvailableCountDownTime());
//
//

        if (updateAvailableCountDownTime != 0) {

            long currentDateTime = System.currentTimeMillis();

            long milliseconds = (currentDateTime - updateAvailableCountDownTime);

//            int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

            long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
//
//        long minutes = (time / 1000) / 60;

            if (hours > 23) {
//                pref.setOpenAppUpdateChecker(false);
                new GetLatestVersion(manualCheck).execute();
            }


        } else {
            new GetLatestVersion(manualCheck).execute();
        }

    }

    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager = (ConnectivityManager)  getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
