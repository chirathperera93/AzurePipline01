package com.ayubo.life.ayubolife.programs.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.payment.*
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.*
import kotlinx.android.synthetic.main.activity_program_settings.*
import javax.inject.Inject

class ProgramSettingsActivity : BaseActivity() {

    var status: String? = null
    var policy_user_master_id: String? = null

    @Inject
    lateinit var programSettingsVM: ProgramSettingsVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program_settings)
        App.getInstance().appComponent.inject(this)

        initUI()

        readExtras()

    }


    private fun setProgramSettingsRestart(policy_user_master_id: String) {
        subscription.add(programSettingsVM.setProgramSettingsRestart(policy_user_master_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBarProgramSettings.visibility = View.VISIBLE }
            .doOnTerminate { progressBarProgramSettings.visibility = View.GONE }
            .doOnError { progressBarProgramSettings.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {
                    status = "Active"
                    setupUI(status)
                } else {
                    //  showMessage("Failed loading data")
                }
            }, {
                progressBarProgramSettings.visibility = View.GONE
                showMessage("Failed loading data")
            })
        )
    }

    private fun setProgramPause(policy_user_master_id: String) {
        subscription.add(programSettingsVM.setProgramPause(policy_user_master_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBarProgramSettings.visibility = View.VISIBLE }
            .doOnTerminate { progressBarProgramSettings.visibility = View.GONE }
            .doOnError { progressBarProgramSettings.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {
                    status = "Pause"
                    setupUI(status)
                } else {
                    //  showMessage("Failed loading data")
                }
            }, {
                progressBarProgramSettings.visibility = View.GONE
                showMessage("Failed loading data")
            })
        )
    }

    private fun setProgramResume(policy_user_master_id: String) {
        subscription.add(programSettingsVM.setProgramResume(policy_user_master_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBarProgramSettings.visibility = View.VISIBLE }
            .doOnTerminate { progressBarProgramSettings.visibility = View.GONE }
            .doOnError { progressBarProgramSettings.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {
                    status = "Active"
                    setupUI(status)
                } else {
                    //  showMessage("Failed loading data")
                }
            }, {
                progressBarProgramSettings.visibility = View.GONE
                showMessage("Failed loading data")
            })
        )
    }

    private fun setProgramUnsubscribe(policy_user_master_id: String) {
        subscription.add(programSettingsVM.setProgramUnsubscribe(policy_user_master_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBarProgramSettings.visibility = View.VISIBLE }
            .doOnTerminate { progressBarProgramSettings.visibility = View.GONE }
            .doOnError { progressBarProgramSettings.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {
                    status = "Inactive"
                    //  setupUI(status)
                    finish()
                    //DO changes ............................
                } else {
                    //  showMessage("Failed loading data")
                }
            }, {
                progressBarProgramSettings.visibility = View.GONE
                showMessage("Failed loading data")
            })
        )
    }

    private fun initUI() {

        txt_program_status.setTextColor(Color.parseColor("#ffffff"))
        txt_program_status.setBackgroundResource(R.drawable.program_status_bg_green)

        txt_btnActive.setOnClickListener {
            if (txt_btnActive.text == "Restart") {
                showAlert_Confirm("Restart", getString(R.string.program_setting_alert_active))
            }
            if (txt_btnActive.text == "Resume") {
                showAlert_Confirm("Resume", getString(R.string.program_setting_alert_resume))
            }
        }

        txt_btnPause.setOnClickListener {
            showAlert_Confirm("Pause", getString(R.string.program_setting_alert_pause))
        }

        txt_btnDeactivate.setOnClickListener {
            showAlert_Confirm("Inactive", getString(R.string.program_setting_alert_inactive))
        }


        // BACK EVENT START
        val viewbackbutton = findViewById<View>(R.id.lay_back_button)
        val backLayout = viewbackbutton.findViewById<LinearLayout>(R.id.btn_backImgBtn_layout)
        val backButton = viewbackbutton.findViewById<ImageButton>(R.id.btn_backImgBtn)
        backLayout.setOnClickListener { finish() }
        backButton.setOnClickListener { finish() }
        // BACK BUTTON EVENT END
    }


    private fun readExtras() {

        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_PROGRAM_SETTING_NAME)) {
            val program_setting_name = bundle.getSerializable(EXTRA_PROGRAM_SETTING_NAME) as String
            status = bundle.getSerializable(EXTRA_PROGRAM_SETTING_SUBHEADING) as String
            val program_setting_image =
                bundle.getSerializable(EXTRA_PROGRAM_SETTING_IMAGE) as String
            policy_user_master_id = bundle.getSerializable(EXTRA_PROGRAM_MASTER_ID) as String


            Log.d("======muid=========", policy_user_master_id!!)
            txt_programname.text = program_setting_name
            Glide.with(this@ProgramSettingsActivity).load(program_setting_image).into(main_bg_image)

            setupUI(status)

        }
    }

    fun setupUI(status: String?) {

        txt_program_status.text = status

        if (status == "Active") {
            txt_btnActive.text = "Restart"
            txt_btnPause.text = "Pause"
            txt_btnDeactivate.text = "Deactivate"

            txt_btnActive.setBackgroundResource(R.drawable.program_setting_button_active)
            txt_btnPause.setBackgroundResource(R.drawable.program_setting_button_active)
            txt_btnDeactivate.setBackgroundResource(R.drawable.program_setting_button_active)

            txt_btnActive.isEnabled = true
            txt_btnPause.isEnabled = true
            txt_btnDeactivate.isEnabled = true

            //  all active
        }
        if (status == "Pause") {
            txt_btnActive.text = "Resume"
            txt_btnPause.text = "Pause"
            txt_btnDeactivate.text = "Deactivate"
            //  txt_btnActive active
            txt_btnActive.setBackgroundResource(R.drawable.program_setting_button_active)
            txt_btnPause.setBackgroundResource(R.drawable.program_setting_button_deactive)
            txt_btnDeactivate.setBackgroundResource(R.drawable.program_setting_button_deactive)

            txt_btnActive.isEnabled = true
            txt_btnPause.isEnabled = false
            txt_btnDeactivate.isEnabled = false
        }
        if (status == "Inactive") {
            txt_btnActive.text = "Restart"
            txt_btnPause.text = "Pause"
            txt_btnDeactivate.text = "Deactivate"
            //  txt_btnActive active,txt_btnDeactivate
            txt_btnActive.setBackgroundResource(R.drawable.program_setting_button_active)
            txt_btnPause.setBackgroundResource(R.drawable.program_setting_button_deactive)
            txt_btnDeactivate.setBackgroundResource(R.drawable.program_setting_button_deactive)

            txt_btnActive.isEnabled = true
            txt_btnPause.isEnabled = false
            txt_btnDeactivate.isEnabled = false
        }


    }

    fun showAlert_Confirm(type: String, msg: String) {

        val builder = android.app.AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutView = inflater.inflate(R.layout.alert_common_with_cancel_only, null, false)

        builder.setView(layoutView)
        val dialog = builder.create()
        dialog.setCancelable(false)

        val lbl_alert_message = layoutView.findViewById<View>(R.id.lbl_alert_message) as TextView
        lbl_alert_message.text = msg

        val btn_yes = layoutView.findViewById<View>(R.id.btn_yes) as TextView
        val btn_no = layoutView.findViewById<View>(R.id.btn_no) as TextView
        btn_yes.text = "Confirm"
        btn_no.text = "Cancel"

        btn_yes.setOnClickListener {
            dialog.cancel()

            if (type == "Restart") {
                setProgramSettingsRestart(policy_user_master_id!!)
            }
            if (type == "Resume") {
                setProgramResume(policy_user_master_id!!)
            }
            if (type == "Pause") {
                setProgramPause(policy_user_master_id!!)
            }
            if (type == "Inactive") {
                setProgramUnsubscribe(policy_user_master_id!!)
            }

        }


        btn_no.setOnClickListener {
            dialog.cancel()
        }


        dialog.show()
    }

    companion object {
        fun startActivity(
            context: Activity,
            program_master_id: String,
            program_setting_name: String,
            program_setting_status: String,
            program_setting_image: String
        ) {
            val intent = Intent(context, ProgramSettingsActivity::class.java)
            intent.putExtra(EXTRA_PROGRAM_MASTER_ID, program_master_id)
            intent.putExtra(EXTRA_PROGRAM_SETTING_NAME, program_setting_name)
            intent.putExtra(EXTRA_PROGRAM_SETTING_SUBHEADING, program_setting_status)
            intent.putExtra(EXTRA_PROGRAM_SETTING_IMAGE, program_setting_image)
            context.startActivity(intent)
        }
    }

}
