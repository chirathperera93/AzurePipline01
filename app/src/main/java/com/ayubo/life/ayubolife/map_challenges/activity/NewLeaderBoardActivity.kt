package com.ayubo.life.ayubolife.map_challenges.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.map_challenges.model.InfoButton
import com.ayubo.life.ayubolife.map_challenges.model.LeaderBoardResponse
import com.ayubo.life.ayubolife.map_challenges.model.Leaderboards
import com.ayubo.life.ayubolife.map_challenges.model.MemberList
import com.ayubo.life.ayubolife.map_challenges.vm.LeaderBoardVM
import com.ayubo.life.ayubolife.payment.EXTRA_CHALLANGE_ID
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.leaderboardmenuone_layout.view.*
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.progressBar
import kotlinx.android.synthetic.main.activity_leader_board.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class NewLeaderBoardActivity : BaseActivity() {

    lateinit var leaderboards: ArrayList<Leaderboards>

    @Inject
    lateinit var leaderBoardVM: LeaderBoardVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)
        App.getInstance().appComponent.inject(this)
        txt_last_update_layout.visibility = View.GONE;
        txt_new_update_layout.visibility = View.GONE;
        readExtras()
    }


    private fun getLeaderboardData(chalalngeID: String) {

        subscription.add(leaderBoardVM.getLeaderBoard(chalalngeID).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
            .doOnTerminate { progressBar.visibility = View.GONE }
            .doOnError { progressBar.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {

                    try {
                        leaderboards = leaderBoardVM.leaderboards

                        if (leaderBoardVM.header_image.isEmpty()) {
                            img_main_image.visibility = View.GONE
                        } else {
                            Glide.with(this@NewLeaderBoardActivity).load(leaderBoardVM.header_image)
                                .into(img_main_image)
                        }

                        txt_header.text = leaderBoardVM.header_text
//                        txt_sub_heading.text = leaderBoardVM.subheading_text

                        if (leaderBoardVM.subheading_text !== "") {
                            txt_last_update_layout.visibility = View.VISIBLE
                            txt_new_update_layout.visibility = View.VISIBLE;

                            val str: String = leaderBoardVM.subheading_text;

                            val splited: List<String> = str.split(" ");

                            txt_last_update.text = splited[0] + " " + splited[1];

                            val splitedTime: List<String> = splited[4].split(":");

                            val month_date = SimpleDateFormat("dd MMM yy", Locale.ENGLISH);

                            val sdf = SimpleDateFormat("yyyy-MM-dd");

                            val actualDate: String = splited[3];

                            val date: Date = sdf.parse(actualDate);

                            val month_name: String = month_date.format(date);

                            txt_last_update_date_time.text =
                                month_name + " | " + splitedTime[0] + ":" + splitedTime[1];


                            val string: String = splited[3] + " " + splited[4];
                            val format: DateFormat =
                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                            val newdate: Date = format.parse(string);

                            val futureDate = addHoursToJavaUtilDate(newdate, 30);

                            val dateFormatNew: DateFormat = SimpleDateFormat("dd MMM yy");
                            val timeFormatNew: DateFormat = SimpleDateFormat("HH:mm");
                            val strDate: String = dateFormatNew.format(futureDate);
                            val strTime: String = timeFormatNew.format(futureDate);

                            txt_new_update_date_time.text = strDate + " | " + strTime;
                        }



                        img_info.tag = leaderBoardVM.infoButton

                        if (leaderBoardVM.infoButton.show!!) {
                            img_info.visibility = View.VISIBLE
                            img_info.setOnClickListener {
                                val info = img_info.tag
                                val infoData: InfoButton = info as InfoButton
                                processAction(infoData.action!!, infoData.meta!!)
                            }
                        } else {
                            img_info.visibility = View.INVISIBLE
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    try {
                        loadMenu(0)
                    } catch (e: Exception) {
                        e.stackTrace
                    }

                } else {
                    showMessage(R.string.service_loading_fail)
                }
            }, {
                progressBar.visibility = View.GONE
                showMessage(R.string.service_loading_fail)
            })
        )
    }

    fun loadMenu(activePosition: Int) {
        val buttonContainer = findViewById<LinearLayout>(R.id.buttonContaner)
        buttonContainer.removeAllViews()

        val display = getWindowManager().getDefaultDisplay()
        val width = display.width
        var buttonW = 0
        buttonW = (width) / 3


        if (leaderBoardVM.leaderboards.size > 1) {

            for (i in 0 until leaderBoardVM.leaderboards.size) {

                val obj = leaderBoardVM.leaderboards[i]

                val buttonItem = LayoutInflater.from(this@NewLeaderBoardActivity)
                    .inflate(R.layout.leaderboardmenuone_layout, null, false) as LinearLayout
                buttonItem.txt_caption.layoutParams = LinearLayout.LayoutParams(
                    buttonW,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                buttonItem.tag = i
                buttonItem.txt_caption.text = obj.title

                if (i == activePosition) {
                    buttonItem.txt_caption.setTextColor(Color.parseColor("#ffffff"))
                    buttonItem.txt_caption.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            this@NewLeaderBoardActivity,
                            R.drawable.leaderboard_button_bg
                        )
                    )
                } else {
                    buttonItem.txt_caption.setTextColor(Color.parseColor("#000000"))
                    buttonItem.txt_caption.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            this@NewLeaderBoardActivity,
                            R.drawable.leaderboard_button_bg_inactive
                        )
                    )
                }
                buttonItem.txt_caption.setOnClickListener {
                    val data = buttonItem.tag
                    val objectPosition: Int = data as Int

                    //  val obj= leaderBoardVM.leaderboards[myData]
                    loadMenu(objectPosition)

                }
                buttonContainer?.addView(buttonItem)
            }
        } else {
            buttonContaner.visibility = View.GONE
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = LeaderboardFragment()
        val bundle = Bundle()


        var transactionList = ArrayList<MemberList>()

        transactionList = leaderboards[activePosition].list as ArrayList<MemberList>

        bundle.putSerializable("leaderboard_data", transactionList)
        fragment.setArguments(bundle)

        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()


    }


    private fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(com.ayubo.life.ayubolife.payment.EXTRA_CHALLANGE_ID)) {
            val challangeID =
                bundle.getSerializable(com.ayubo.life.ayubolife.payment.EXTRA_CHALLANGE_ID) as String
            getLeaderboardData(challangeID)
        }
    }

    companion object {
        fun startActivity(context: Activity, challangeID: String) {
            val intent = Intent(context, NewLeaderBoardActivity::class.java)
            intent.putExtra(EXTRA_CHALLANGE_ID, challangeID)
            context.startActivity(intent)
        }

    }

    fun addHoursToJavaUtilDate(date: Date, mins: Int): Date {
        val calendar: Calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, mins);
        return calendar.getTime();
    }

}






