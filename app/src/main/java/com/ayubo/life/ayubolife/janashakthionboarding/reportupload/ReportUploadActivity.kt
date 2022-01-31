package com.ayubo.life.ayubolife.janashakthionboarding.reportupload

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.View

import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateVM
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.prochat.captureimages.ChooseImageDialog
import com.ayubo.life.ayubolife.prochat.captureimages.ImageCompresser
import com.ayubo.life.ayubolife.prochat.common.REQUEST_CODE_REPORT_PREVIEW
import com.ayubo.life.ayubolife.utility.Ram
import java.io.File
import javax.inject.Inject

class ReportUploadActivity : BaseActivity() {
    var requestCode:String="no"
    private val MYPERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 111
   // private val requestCod=0



    companion object {
        @JvmStatic
        fun startActivity(context: AppCompatActivity, requestCode: Int) {
            val intent = Intent(context, ReportUploadActivity::class.java)
       //     intent.putExtra("file", file.absolutePath)
            intent.putExtra("requestCode", requestCode.toString())
//            context.startActivityForResult(intent, REQUEST_CODE_REPORT_PREVIEW)
            context.startActivityForResult(intent, requestCode)

           // context.startActivityForResult(Intent(context, ReportUploadActivity::class.java),requestCode)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_report_upload)

        if (intent.hasExtra("requestCode")) {
            var code = intent.getStringExtra("requestCode")

            requestCode = if(code=="1008"){
                if (Ram.getMedicalReportObject() != null){
                    requestCode= Ram.getMedicalReportObject().id!!
                }

                "yes"
            }else{
                "no"
            }
        }



    }

    fun onUploadClick(view: View){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MYPERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            onUploadClickWithPermission()
        }

    }

    fun onUploadClickWithPermission() {

        ChooseImageDialog.newInstance {
            if (it?.toString() == null || it.toString().isEmpty()) {
                showAlertOneButton(
                        R.string.title_failed,
                        R.string.msg_upload_failed,
                        R.string.btn_text_ok,
                        object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0!!.dismiss()
                                return@onClick
                            }
                        })
                return@newInstance
            }


            val compress = ImageCompresser(this)
            val filePath = compress.compressImage(it.toString(), 600F, 800F)
            if (filePath == null) {
                showAlertOneButton(
                        R.string.title_failed,
                        R.string.msg_upload_failed,
                        R.string.btn_text_ok,
                        object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0!!.dismiss()
                                return@onClick
                            }
                        })
                return@newInstance
            }
            val file = File(filePath)
            ReportPreviewActivity.startActivity(this, file, requestCode)
        }.show(supportFragmentManager, "ChooseImageDialog")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_CODE_REPORT_PREVIEW) {
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }
    }
}
