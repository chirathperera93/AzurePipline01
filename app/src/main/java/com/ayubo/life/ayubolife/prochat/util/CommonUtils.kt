package com.ayubo.life.ayubolife.prochat.util

import android.annotation.SuppressLint
import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.webkit.MimeTypeMap
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.ayubo.life.ayubolife.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


object CommonUtils {
    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun getTempFile(prefix: String, extension: String): File {
        val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmss").format(Date())
        return File(
            getRootDirectory().toString().plus(File.separator).plus(prefix).plus("_")
                .plus(timeStamp).plus(".").plus(extension)
        )
    }

    fun getFile(fileName: String, extension: String = ""): File {


        return File(
            getRootDirectory().toString().plus(File.separator).plus(fileName).plus(".")
                .plus(extension)
        )
    }

    fun getRootDirectory(): File {
        val file = File(Environment.getExternalStorageDirectory(), "Ayubopro")

        if (!file.exists()) {
            if (!file.mkdirs()) {
                return file
            }
        }

        return file
    }

    fun milliSecondsToTimer(milliseconds: Long): String {
        var finalTimerString = ""
        var secondsString = ""
        var minutesString = ""

        // Convert total duration into time
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours.toString() + ":"
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0$seconds"
        } else {
            secondsString = "" + seconds
        }

        if (minutes < 10) {
            minutesString = "0$minutes"
        } else {
            minutesString = "" + minutes
        }

        finalTimerString = "$finalTimerString$minutesString:$secondsString"

        // return timer string
        return finalTimerString
    }

    fun shareFiles(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val myMime = MimeTypeMap.getSingleton()
        val mimeType = myMime.getMimeTypeFromExtension(
            file.nameWithoutExtension.substring(
                file.nameWithoutExtension.lastIndexOf(".")
            ).substring(1)
        )
        sharingIntent.type = mimeType
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(sharingIntent, "Share file using"))
    }

    fun shareLink(context: Context, link: String) {
        val i = Intent(Intent.ACTION_SEND)
        i.setType("text/plain")
        i.putExtra(Intent.EXTRA_SUBJECT, "")
        i.putExtra(Intent.EXTRA_TEXT, link)
        context.startActivity(Intent.createChooser(i, "Share URL"))
    }

    private fun sendEmailUsingSelectedEmailApp(
        p_context: Activity, p_subject: String?, p_body: String?,
        p_attachments: ArrayList<Uri>?, p_selectedEmailApp: ResolveInfo?, email: String
    ) {
        try {
            val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE)

            val aEmailList = arrayOf(email)

            emailIntent.putExtra(Intent.EXTRA_EMAIL, aEmailList)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, p_subject ?: "")
            emailIntent.putExtra(Intent.EXTRA_TEXT, p_body ?: "")

            if (null != p_attachments && p_attachments.size > 0) {
                val attachmentsUris = ArrayList<Uri>()

                // Convert from paths to Android friendly Parcelable Uri's
                for (fileIn in p_attachments) {
                    attachmentsUris.add(fileIn)
                }
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachmentsUris)
            }

            if (null != p_selectedEmailApp) {
                Log.d("SendEmail", "Sending email using $p_selectedEmailApp")
                emailIntent.component = ComponentName(
                    p_selectedEmailApp.activityInfo.packageName,
                    p_selectedEmailApp.activityInfo.name
                )

                p_context.startActivity(emailIntent)
            } else {
                val emailAppChooser = Intent.createChooser(emailIntent, "Select Email app")

                p_context.startActivity(emailAppChooser)
            }
        } catch (ex: Exception) {
            Log.e("SendEmail", "Error sending email", ex)
        }
    }

    fun shareWithEmail(
        p_context: Activity, icon: Int, email: String, p_subject: String, p_body: String,
        p_attachments: ArrayList<Uri>?
    ) {
        try {
            val pm = p_context.packageManager
            var selectedEmailActivity: ResolveInfo? = null

            val emailDummyIntent = Intent(Intent.ACTION_SENDTO)
            emailDummyIntent.data = Uri.parse("mailto:$email")

            var emailActivities: List<ResolveInfo>? = pm.queryIntentActivities(emailDummyIntent, 0)

            if (null == emailActivities || emailActivities.size == 0) {
                val emailDummyIntentRFC822 = Intent(Intent.ACTION_SEND_MULTIPLE)
                emailDummyIntentRFC822.type = "message/rfc822"

                emailActivities = pm.queryIntentActivities(emailDummyIntentRFC822, 0)
            }

            if (null != emailActivities) {
                if (emailActivities.size == 1) {
                    selectedEmailActivity = emailActivities[0]
                } else {
                    for (currAvailableEmailActivity in emailActivities) {
                        if (currAvailableEmailActivity.isDefault) {
                            selectedEmailActivity = currAvailableEmailActivity
                        }
                    }
                }

                if (null != selectedEmailActivity) {
                    // Send email using the only/default email activity
                    sendEmailUsingSelectedEmailApp(
                        p_context,
                        p_subject,
                        p_body,
                        p_attachments,
                        selectedEmailActivity,
                        email
                    )
                } else {
                    val emailActivitiesForDialog = emailActivities

                    val availableEmailAppsName = arrayOfNulls<String>(emailActivitiesForDialog.size)
                    val icons = arrayOfNulls<Drawable>(emailActivitiesForDialog.size)
                    for (i in emailActivitiesForDialog.indices) {
                        availableEmailAppsName[i] =
                            emailActivitiesForDialog[i].activityInfo.applicationInfo
                                .loadLabel(pm).toString()
                        icons[i] = emailActivitiesForDialog[i].activityInfo.loadIcon(pm)
                    }

                    val adapter = ArrayAdapterWithIcon(p_context, availableEmailAppsName, icons)

                    val builder = AlertDialog.Builder(p_context)
                    builder.setIcon(icon)
                    builder.setTitle("Select email app")
                    builder.setAdapter(adapter) { dialog, which ->
                        sendEmailUsingSelectedEmailApp(
                            p_context, p_subject, p_body, p_attachments,
                            emailActivitiesForDialog[which], email
                        )
                    }


                    builder.show()
                }
            } else {
                sendEmailUsingSelectedEmailApp(
                    p_context,
                    p_subject,
                    p_body,
                    p_attachments,
                    null,
                    email
                )
            }
        } catch (ex: Exception) {
            Log.e("SendEmail", "Can't send email", ex)
        }

    }

    @SuppressLint("WrongConstant")
    fun showNotification(context: Context, file: File) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "my_channel_id_01"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "My Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                )

            // Configure the notification channel.
            notificationChannel.description = "Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }


        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        val pi = PendingIntent.getActivity(
            context,
            0,
            openFile(context, file),
            Intent.FLAG_ACTIVITY_NEW_TASK
        )
        notificationBuilder.setContentIntent(pi)
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setVibrate(longArrayOf(0, 1000, 500, 1000))
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(file.name)
            .setContentText("Downloaded successfully")

        notificationManager.notify(/*notification id*/1, notificationBuilder.build())
    }

    private fun openFile(context: Context, file: File): Intent {
        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )
        val viewFile = Intent(Intent.ACTION_VIEW)
        viewFile.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val myMime = MimeTypeMap.getSingleton()
        val mimeType = myMime.getMimeTypeFromExtension(
            file.nameWithoutExtension.substring(
                file.nameWithoutExtension.lastIndexOf(".")
            ).substring(1)
        )
        viewFile.setDataAndType(uri, mimeType)
        return viewFile
    }

}