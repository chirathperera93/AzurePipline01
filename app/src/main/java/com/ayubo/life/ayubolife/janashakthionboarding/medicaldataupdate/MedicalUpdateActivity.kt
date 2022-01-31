package com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.NumberPicker
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.janashakthionboarding.reportupload.ReportUploadActivity
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.prochat.common.REQUEST_CODE_REPORT_UPLOAD_HBALC
import com.ayubo.life.ayubolife.prochat.common.REQUEST_CODE_REPORT_UPLOAD_HDL
import com.ayubo.life.ayubolife.prochat.common.REQUEST_CODE_REPORT_UPLOAD_OTHER
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.webrtc.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_medical_update.*
import java.io.File
import java.util.*
import javax.inject.Inject


class MedicalUpdateActivity : BaseActivity() {

    @Inject
    lateinit var medicalUpdateVM: MedicalUpdateVM
    private var images = ArrayList<ImageFileData>()

    lateinit var pref: PrefManager


    companion object {
        @JvmStatic
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MedicalUpdateActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_update)
        App.getInstance().appComponent.inject(this)

        pref = PrefManager(this)

        imgHdlStatus.setImageResource(R.drawable.add_btn)
        imghbaStatus.setImageResource(R.drawable.add_btn)
        imgOtherStatus.setImageResource(R.drawable.add_btn)
    }

    fun onHbalcClick(view: View) {
        ReportUploadActivity.startActivity(this, REQUEST_CODE_REPORT_UPLOAD_HBALC)
    }

    fun onHdlClick(view: View) {
        ReportUploadActivity.startActivity(this, REQUEST_CODE_REPORT_UPLOAD_HDL)
    }

    fun onOtherClick(view: View) {
        ReportUploadActivity.startActivity(this, REQUEST_CODE_REPORT_UPLOAD_OTHER)
    }


    var feetSize = 0
    var inchSize = 0
    fun onShowHeightPicker(view: View) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_height_picker)
        dialog.setTitle(R.string.title_height)
        val feet = dialog.findViewById<NumberPicker>(R.id.numberPickerFeet)
        feet.minValue = 0
        feet.maxValue = 8
        val inch = dialog.findViewById<NumberPicker>(R.id.numberPickerInch)
        inch.minValue = 0
        inch.maxValue = 11
        val cancelButton = dialog.findViewById<Button>(R.id.btnCancel)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        val okButton = dialog.findViewById<Button>(R.id.btnOk)
        okButton.setOnClickListener {
            if (feet.value == 0) {
                showMessage("Please enter valid height")
                return@setOnClickListener
            }
            dialog.dismiss()
            feetSize = feet.value
            inchSize = inch.value
            etHeight.text =
                feet.value.toString().plus("'").plus(" ").plus(inch.value.toString().plus("''"))
        }
        dialog.show()

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER

        dialog.window?.attributes = lp
    }

    fun onClickSkip(view: View) {


        if (etHeight.text.isNullOrEmpty()) {
            showAlert(
                "Please enter height",
                null,
                DialogInterface.OnClickListener { p0, p1 -> p0.dismiss() })
            return
        }
        if (etWeight.text.isNullOrEmpty()) {
            showAlert(
                "Please enter weight",
                null,
                DialogInterface.OnClickListener { p0, p1 -> p0.dismiss() })
            return
        }

        subscription.add(medicalUpdateVM.uploadMedicalReport(
            pref.loginUser["uid"]!!,
            pref.getRelateID(),
            feetSize,
            inchSize,
            etWeight.text.toString().toInt(),
            0,
            0,
            images
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
            .doOnTerminate { progressBar.visibility = View.GONE }
            .doOnError { progressBar.visibility = View.GONE }.subscribe({
                if (it.isSuccess) {

                    finish()

//                        showAlert("Submitted successfully", "Success", object : DialogInterface.OnClickListener {
//                            override fun onClick(p0: DialogInterface?, p1: Int) {
//                                p0?.dismiss()
//                                finish()
//                            }
//
//                        })
                } else {
                    showMessage("Failed loading questionnaire")
                }
            }, {
                progressBar.visibility = View.GONE
                showMessage("Failed loading questionnaire")
            })
        )
    }

    fun onDoneClick(view: View) {
        var etSysVal: Int = 0
        var etDiaVal: Int = 0

        if (etSys.text.toString().length == 0) {
            etSysVal = 0
        } else {
            etSysVal = Integer.parseInt(etSys.text.toString())
        }
        if (etDia.text.toString().length == 0) {
            etDiaVal = 0
        } else {
            etDiaVal = Integer.parseInt(etDia.text.toString())
        }


        if (etHeight.text.isNullOrEmpty()) {
            showAlert(
                "Please enter height",
                null,
                DialogInterface.OnClickListener { p0, p1 -> p0.dismiss() })
            return
        }
        if (etWeight.text.isNullOrEmpty()) {
            showAlert(
                "Please enter weight",
                null,
                DialogInterface.OnClickListener { p0, p1 -> p0.dismiss() })
            return
        }


        subscription.add(medicalUpdateVM.uploadMedicalReport(
            pref.loginUser["uid"]!!,
            pref.getRelateID(),
            feetSize,
            inchSize,
            etWeight.text.toString().toInt(),
            etSysVal,
            etDiaVal,
            images
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
            .doOnTerminate { progressBar.visibility = View.GONE }
            .doOnError { progressBar.visibility = View.GONE }.subscribe({
                if (it.isSuccess) {

                    finish()

//                        showAlert("Submitted successfully", "Success", object : DialogInterface.OnClickListener {
//                            override fun onClick(p0: DialogInterface?, p1: Int) {
//                                p0?.dismiss()
//                                finish()
//                            }
//
//                        })
                } else {
                    showMessage("Failed loading questionnaire")
                }
            }, {
                progressBar.visibility = View.GONE
                showMessage("Failed loading questionnaire")
            })
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                when (requestCode) {
                    REQUEST_CODE_REPORT_UPLOAD_HBALC -> {
                        images.add(
                            ImageFileData(
                                File(data.getStringExtra("file")),
                                btnHbalc.tag as String,
                                data.getStringExtra("date")!!
                            )
                        )
                        imghbaStatus.setImageResource(R.drawable.ic_ok_tick)
                    }
                    REQUEST_CODE_REPORT_UPLOAD_HDL -> {
                        images.add(
                            ImageFileData(
                                File(data.getStringExtra("file")),
                                btnHdl.tag as String,
                                data.getStringExtra("date")!!
                            )
                        )
                        imgHdlStatus.setImageResource(R.drawable.ic_ok_tick)
                    }
                    REQUEST_CODE_REPORT_UPLOAD_OTHER -> {
                        images.add(
                            ImageFileData(
                                File(data.getStringExtra("file")),
                                Ram.getMedicalReportObject().id!!,
                                data.getStringExtra("date")!!
                            )
                        )
                        imgOtherStatus.setImageResource(R.drawable.ic_ok_tick)
                    }
                }
            }
        }
    }
}
