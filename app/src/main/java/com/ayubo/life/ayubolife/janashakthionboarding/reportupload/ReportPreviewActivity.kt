package com.ayubo.life.ayubolife.janashakthionboarding.reportupload

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.prochat.common.REQUEST_CODE_REPORT_PREVIEW
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_report_preview.*
import java.io.File
import java.util.*

import android.widget.*
import android.widget.TextView
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.Report
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.webrtc.App



class ReportPreviewActivity : BaseActivity(), DatePickerDialog.OnDateSetListener{
    var file: File? = null
    var isReportTypeON:String?=null
    var isReportTypeOn:String="no"
    var reportObject:Report?=null

    companion object {
        fun startActivity(context: AppCompatActivity, file: File, isReportTypeON: String ) {
            val intent = Intent(context, ReportPreviewActivity::class.java)
            intent.putExtra("file", file.absolutePath)
            intent.putExtra("isReportTypeON", isReportTypeON)
            context.startActivityForResult(intent, REQUEST_CODE_REPORT_PREVIEW)
        }
    }

    override fun onResume() {
        super.onResume()


        if(isReportTypeON=="yes"){
            txt_reportType.visibility=View.VISIBLE
            if (Ram.getMedicalReportObject() != null){
                reportObject= Ram.getMedicalReportObject()
                if (reportObject != null) {
                    txt_reportType.text=reportObject!!.name
                }
            }
            else{
                txt_reportType.text="Select Report Type"
            }
        }else{
            txt_reportType.visibility=View.GONE
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_preview)

        App.getInstance().appComponent.inject(this)

        if (intent.hasExtra("file")) {
            file = File(intent.getStringExtra("file"))

            isReportTypeON = intent.getStringExtra("isReportTypeON")

            Glide.with(this).load(file).into(imgPreview)

            val txt_reportType = findViewById<TextView>(R.id.txt_reportType)






        }
    }



    fun onSubmitClick(view: View) {
        if(file == null){
            showMessage("Please select a report to upload")
            return
        }

        if(txtDate.text.isNullOrEmpty()){
            showMessage("Please select a date")
            return
        }
        val data = Intent()
        data.putExtra("file", file?.absolutePath)
        data.putExtra("date", txtDate.text)
        data.putExtra("type", isReportTypeON)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    val currentDate = Calendar.getInstance()




    fun onReportTypeClick(view: View) {

        val intent = Intent(this, SelectReportsTypesActivity::class.java)

        startActivity(intent)
    }


    fun onCalenderClick(view: View) {
        val startYear = currentDate.get(Calendar.YEAR)
        val starthMonth = currentDate.get(Calendar.MONTH)
        val startDay = currentDate.get(Calendar.DAY_OF_MONTH)
        var datepicker = DatePickerDialog(this, this, startYear, starthMonth, startDay)
        datepicker.show()
    }

    fun onDeleteImageClick(view: View) {
        onBackPressed()
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        txtDate.text = year.toString().plus("-").plus(monthOfYear.toString().padStart(2,'0')).plus("-").plus(dayOfMonth.toString().padStart(2,'0'))
    }
}
