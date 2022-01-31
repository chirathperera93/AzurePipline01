package com.ayubo.life.ayubolife.programs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.fragments.CircleTransform
import com.ayubo.life.ayubolife.goals_extention.DashBoard_Activity
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.programs.data.model.Experts
import com.ayubo.life.ayubolife.programs.data.model.Program
import com.ayubo.life.ayubolife.programs.settings.ProgramSettingsActivity
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_experts.progressBar
import kotlinx.android.synthetic.main.activity_program.*
import kotlinx.android.synthetic.main.ask_main_cell.view.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ProgramActivity : BaseActivity(), ProgramAdapter.OnItemClickListener {

    override fun onProcessActionClick(activityName: String, meta: String) {
        processAction(activityName, meta)
    }

    var todayProgramCount = 0

    @Inject
    lateinit var programsVM: ProgramActivityVM


    var listHistroyArray = ArrayList<Program>()
    var listFutureArray = ArrayList<Program>()
    var listTodayArray = ArrayList<Program>()

    var listHistroyArrayString = ArrayList<String>()
    var listFutureArrayString = ArrayList<String>()
    var listTodayArrayString = ArrayList<String>()

    lateinit var pref: PrefManager


    var policyusermasterid: String = ""
    var completedCount = 0
    var todayFullCount = 0

    var listMainArray = ArrayList<Any>()

    companion object {
        @JvmStatic
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ProgramActivity::class.java))
        }
    }

    private var programAdapter: ProgramAdapter? = null

    private fun getDateAsString(s: Timestamp): String {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(s.time)

            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun getTodayLastTimeStamp(): Timestamp {
        val dat = Date()
        var cal = Calendar.getInstance()
        cal.time = dat
        cal.set(Calendar.HOUR, 23)
        cal.set(Calendar.MINUTE, 57)

        var timestmp = Timestamp(cal.timeInMillis)

        Log.d("Today Last Timestamp", timestmp.toString())
        return timestmp
    }

    fun gettingDataArrays(dataList: ArrayList<Program>): ArrayList<Any> {

        if (listHistroyArray.size > 0) {
            listHistroyArray.clear()
        }
        if (listFutureArray.size > 0) {
            listFutureArray.clear()
        }
        if (listTodayArray.size > 0) {
            listTodayArray.clear()
        }

        if (listHistroyArrayString.size > 0) {
            listHistroyArrayString.clear()
        }
        if (listFutureArrayString.size > 0) {
            listFutureArrayString.clear()
        }
        if (listTodayArrayString.size > 0) {
            listTodayArrayString.clear()
        }

        policyusermasterid = ""
        completedCount = 0
        todayFullCount = 0


        for (i in 0 until dataList.size) {
            val program: Program = dataList[i]

            var timestamp = Timestamp(program.timestamp.toLong() * 1000)

            //Getting today date string.........
            val todayTimestamp = System.currentTimeMillis()
            var timestampToday: Timestamp = Timestamp(todayTimestamp)
            var today: String = getDateAsString(timestampToday)


            val date1 = getDateAsString(timestamp)

            if (date1.compareTo(today) < 0) {
                //    System.out.println("Date1 is a History   "+date1)
                listHistroyArray.add(program)
                if (!listHistroyArrayString.contains(date1)) {
                    listHistroyArrayString.add(date1)
                }

            } else if (date1.compareTo(today) > 0) {
                // System.out.println("Date1 is Future   "+date1)
                listFutureArray.add(program)
                if (!listFutureArrayString.contains(date1)) {
                    listFutureArrayString.add(date1)
                }
            } else if (date1.compareTo(today) == 0) {
                //  System.out.println("Date1 is Today ...   "+date1)
                listTodayArray.add(program)
                if (!listTodayArrayString.contains(date1)) {
                    listTodayArrayString.add(date1)
                }
            }


        }
        var listUniqDates = ArrayList<Any>()


        //ADDING HISTORY DATA
        if (listHistroyArrayString.size > 0) {
            for (i in 0 until listHistroyArrayString.size) {
                val dateString: String = listHistroyArrayString[i]

                listUniqDates.add(dateString)
                for (a in 0 until listHistroyArray.size) {
                    val program = listHistroyArray[a]
                    var timestamp = Timestamp(program.timestamp.toLong() * 1000)

                    var srtDate = getDateAsString(timestamp)
                    if (dateString == srtDate) {
                        listUniqDates.add(program)
                    }
                }
            }
        }
        System.out.println("Date1.......................")
        //ADDING TODAY DATA
        if (listTodayArrayString.size > 0) {
            for (i in 0 until listTodayArrayString.size) {
                val dateString: String = listTodayArrayString[i]

                todayProgramCount = listUniqDates.size
                listUniqDates.add(dateString)

                for (a in 0 until listTodayArray.size) {
                    val program = listTodayArray[a]
                    var timestamp = Timestamp(program.timestamp.toLong() * 1000)

                    var srtDate = getDateAsString(timestamp)
                    if (dateString == srtDate) {
                        listUniqDates.add(program)
                    }
                }
            }
        } else {
            todayProgramCount = listUniqDates.size
            listUniqDates.add("kite")

        }
        //ADDING FUTURE DATA
        if (listFutureArrayString.size > 0) {
            for (i in 0 until listFutureArrayString.size) {
                val dateString: String = listFutureArrayString[i]

                listUniqDates.add(dateString)
                for (a in 0 until listFutureArray.size) {
                    val program = listFutureArray[a]
                    var timestamp = Timestamp(program.timestamp.toLong() * 1000)

                    var srtDate = getDateAsString(timestamp)
                    if (dateString == srtDate) {
                        listUniqDates.add(program)
                    }
                }
            }
        }
        // FINISHED ALL FILTERING ...............

        for (a in 0 until listTodayArray.size) {
            val program = listTodayArray[a]
            if (program.progress_status == "complete") {
                completedCount++
            }
        }
        todayFullCount = listTodayArray.size


        return listUniqDates;
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program)
        App.getInstance().appComponent.inject(this)

        pref = PrefManager(this)


        policyusermasterid = intent.getStringExtra("meta").toString()
        pref.programID = policyusermasterid

        subscription.add(programsVM.getProgramsList(policyusermasterid).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
            .doOnTerminate { progressBar.visibility = View.GONE }
            .doOnError { progressBar.visibility = View.GONE }.subscribe({
                if (it.isSuccess) {
                    val mlayoutManager = LinearLayoutManager(this@ProgramActivity)

                    programAdapter = ProgramAdapter(
                        this@ProgramActivity,
                        gettingDataArrays(programsVM.list),
                        completedCount,
                        todayFullCount
                    )
                    programAdapter!!.onitemClickListener = this@ProgramActivity
                    listPrograms.apply {
                        layoutManager = mlayoutManager
                        adapter = programAdapter
                    }



                    if (Ram.getProgramLastPosition() == 0) {
                        Ram.setProgramLastPosition(todayProgramCount)
                    } else {

                    }

                    listPrograms.scrollToPosition(todayProgramCount)

                    var headerObj = programsVM.headerObj
                    var settingObj = programsVM.settingObj


                    Glide.with(this@ProgramActivity)
                        .load(headerObj!!.header_logo)
                        .into(img_header_logo)

                    if (headerObj.show_dashboard == 0) {
                        txt_dashboard.visibility = View.GONE
                    } else {
                        txt_dashboard.visibility = View.VISIBLE
                    }
                    txt_deactivate.setOnClickListener {
                        ProgramSettingsActivity.startActivity(
                            this@ProgramActivity,
                            pref.programID,
                            settingObj!!.program_name,
                            settingObj.status,
                            settingObj.image_url
                        )
                    }

                    var expertList = ArrayList<Experts>()
                    expertList = headerObj.experts as ArrayList<Experts>

                    txt_number_of_today.text = headerObj.day.toString()
                    txt_number_of_days.text = headerObj.max_day.toString()
                    val requestOptions =
                        RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transforms(CircleTransform(this@ProgramActivity))

                    if (expertList.size == 1) {
                        val exp: Experts = expertList[0]
                        Glide.with(this@ProgramActivity).load(exp.profile_picture)
                            .apply(requestOptions).into(img_one)

                        img_one.visibility = View.VISIBLE
                        img_two.visibility = View.GONE
                        img_three.visibility = View.GONE
                    }
                    if (expertList.size == 2) {
                        val exp: Experts = expertList[0]
                        Glide.with(this@ProgramActivity).load(exp.profile_picture)
                            .apply(requestOptions).into(img_one)

                        val exp2: Experts = expertList[1]
                        Glide.with(this@ProgramActivity).load(exp2.profile_picture)
                            .apply(requestOptions).into(img_two)

                        img_one.visibility = View.VISIBLE
                        img_two.visibility = View.VISIBLE
                        img_three.visibility = View.GONE
                    }
                    if (expertList.size == 3) {
                        val exp: Experts = expertList[0]
                        Glide.with(this@ProgramActivity).load(exp.profile_picture)
                            .apply(requestOptions).into(img_one)

                        val exp2: Experts = expertList[1]
                        Glide.with(this@ProgramActivity).load(exp2.profile_picture)
                            .apply(requestOptions).into(img_two)

                        val exp3: Experts = expertList[2]
                        Glide.with(this@ProgramActivity).load(exp3.profile_picture)
                            .apply(requestOptions).into(img_three)

                        img_one.visibility = View.VISIBLE
                        img_two.visibility = View.VISIBLE
                        img_three.visibility = View.VISIBLE

                    }
                    lay_viewexperts.setOnClickListener {
                        onShowExperts(expertList)
                    }
                    if (expertList.isEmpty()) {
                        lay_viewexperts.visibility = View.GONE
                    }

                } else {
                    showMessage("Failed loading data")
                }
            }, {

                Log.d("Error", it.stackTrace.toString())
                progressBar.visibility = View.GONE
                showMessage("Failed loading data")
            })
        )

        txt_dashboard.setOnClickListener {
            policyusermasterid = intent.getStringExtra("meta").toString()
            val inten = Intent(this, DashBoard_Activity::class.java)
            inten.putExtra("meta", policyusermasterid)
            startActivity(inten)
        }


    }

    override fun onResume() {
        super.onResume()

        policyusermasterid = pref.programID

        if (programsVM.list != null) {
            if (programsVM.list.size > 0) {
                subscription.add(programsVM.getProgramsList(policyusermasterid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { }
                    .doOnTerminate {}
                    .doOnError { }.subscribe({
                        if (it.isSuccess) {
                            val mlayoutManager = LinearLayoutManager(this@ProgramActivity)

                            programAdapter = ProgramAdapter(
                                this@ProgramActivity,
                                gettingDataArrays(programsVM.list),
                                completedCount,
                                todayFullCount
                            )
                            programAdapter!!.onitemClickListener = this@ProgramActivity
                            listPrograms.apply {
                                layoutManager = mlayoutManager
                                adapter = programAdapter
                            }

                            listPrograms.scrollToPosition(Ram.getProgramLastPosition())


                            var headerObj = programsVM.headerObj


                            Glide.with(this@ProgramActivity)
                                .load(headerObj!!.header_logo)
                                .into(img_header_logo)

                            if (headerObj.show_dashboard == 0) {
                                txt_dashboard.visibility = View.GONE
                            } else {
                                txt_dashboard.visibility = View.VISIBLE
                            }

                            var expertList = ArrayList<Experts>()
                            expertList = headerObj.experts as ArrayList<Experts>

                            txt_number_of_today.text = headerObj.day.toString()
                            txt_number_of_days.text = headerObj.max_day.toString()

                            if (expertList.isEmpty()) {
                                lay_viewexperts.visibility = View.GONE
                            } else {
                                lay_viewexperts.visibility = View.VISIBLE
                            }

                            val requestOptions =
                                RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .transforms(CircleTransform(this@ProgramActivity))



                            if (expertList.size == 1) {
                                val exp: Experts = expertList[0]
                                Glide.with(this@ProgramActivity).load(exp.profile_picture)
                                    .apply(requestOptions).into(img_one)

                                img_one.visibility = View.VISIBLE
                                img_two.visibility = View.GONE
                                img_three.visibility = View.GONE
                            }
                            if (expertList.size == 2) {
                                val exp: Experts = expertList[0]
                                Glide.with(this@ProgramActivity).load(exp.profile_picture)
                                    .apply(requestOptions).into(img_one)

                                val exp2: Experts = expertList[1]
                                Glide.with(this@ProgramActivity).load(exp2.profile_picture)
                                    .apply(requestOptions).into(img_two)

                                img_one.visibility = View.VISIBLE
                                img_two.visibility = View.VISIBLE
                                img_three.visibility = View.GONE
                            }
                            if (expertList.size == 3) {
                                val exp: Experts = expertList[0]
                                Glide.with(this@ProgramActivity).load(exp.profile_picture)
                                    .apply(requestOptions).into(img_one)

                                val exp2: Experts = expertList[1]
                                Glide.with(this@ProgramActivity).load(exp2.profile_picture)
                                    .apply(requestOptions).into(img_two)

                                val exp3: Experts = expertList[2]
                                Glide.with(this@ProgramActivity).load(exp3.profile_picture)
                                    .apply(requestOptions).into(img_three)

                                img_one.visibility = View.VISIBLE
                                img_two.visibility = View.VISIBLE
                                img_three.visibility = View.VISIBLE

                            }
                            lay_viewexperts.setOnClickListener {
                                onShowExperts(expertList)
                            }


                        } else {
                            showMessage("Failed loading data")
                        }
                    }, {

                        Log.d("Error", it.stackTrace.toString())
                        progressBar.visibility = View.GONE
                        showMessage("Failed loading data")
                    })
                )
            }
        }

    }


    fun showAlert_Success(msg: String) {

        val builder = android.app.AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutView = inflater.inflate(R.layout.alert_common_with_success_ok_only, null, false)

        builder.setView(layoutView)
        val dialog = builder.create()
        dialog.setCancelable(false)

        val lbl_alert_message = layoutView.findViewById<View>(R.id.lbl_alert_message) as TextView
        lbl_alert_message.text = msg

        val btn_ok = layoutView.findViewById<View>(R.id.btn_ok) as TextView
        btn_ok.setOnClickListener {
            dialog.cancel()

            finish()
        }

        dialog.show()
    }

    fun showAlert_Add(msg: String) {

        val builder = android.app.AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutView = inflater.inflate(R.layout.alert_common_with_cancel_only, null, false)

        builder.setView(layoutView)
        val dialog = builder.create()
        dialog.setCancelable(false)

        val lbl_alert_message = layoutView.findViewById<View>(R.id.lbl_alert_message) as TextView
        lbl_alert_message.text = msg

        val btn_no = layoutView.findViewById<View>(R.id.btn_no) as TextView
        btn_no.setOnClickListener {
            dialog.cancel()
        }
        val btn_yes = layoutView.findViewById<View>(R.id.btn_yes) as TextView
        btn_yes.setOnClickListener {
            dialog.cancel()
            policyusermasterid = pref.programID
            setUnsubscribeProgram(policyusermasterid)

        }
        dialog.show()
    }


    private fun setUnsubscribeProgram(policy_user_master_id: String) {

        subscription.add(programsVM.setUnsubscribeProgram(policy_user_master_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
            .doOnTerminate { progressBar.visibility = View.GONE }
            .doOnError { progressBar.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {
                    showAlert_Success("You have successfully unsubscribed from the program!")
                } else {

                    showMessage("Failed loading data")
                }
            }, {

                progressBar.visibility = View.GONE
                showMessage("Failed loading data")
            })
        )
    }


    override fun onUpdateAdapter(activityName: String, meta: String) {
        programAdapter!!.notifyDataSetChanged()
    }


}
