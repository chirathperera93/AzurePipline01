package com.ayubo.life.ayubolife.ask

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ask.*
import javax.inject.Inject

class AskActivity : BaseActivity() {

    @Inject
    lateinit var askVM: AskVM
    var width: Int = 0


    companion object {
        fun startActivity(context: Context?, filtereKey: String) {
            val intent = Intent(context, AskActivity::class.java)
            intent.putExtra("filtereKey", filtereKey)
            context!!.startActivity(intent)
        }
    }

    private fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey("filtereKey")) {
            val filtereKey = bundle.getSerializable("filtereKey") as String

            getAskData(filtereKey)

        } else {
            getAskData("")
        }
    }

    private var moviewList: ArrayList<AskCustomData>? = null
    private var dummyMainList: ArrayList<AskCustomObject>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask)

        App.getInstance().appComponent.inject(this)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        width = getScreenWidth()


        initUI()

        readExtras()

    }

    private fun initUI() {
        edt_search_valueaskview!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                if (edt_search_valueaskview.text.isNotEmpty()) {
                    val longerThan3 = moviewList!!.filter {
                        it.name.contains(
                            edt_search_valueaskview.text,
                            true
                        ) || it.speciality.contains(edt_search_valueaskview.text, true)
                    }
                    dummyMainList = ArrayList<AskCustomObject>()
                    var obj: AskCustomObject =
                        AskCustomObject("Results", longerThan3 as ArrayList<AskCustomData>)
                    dummyMainList!!.add(obj)
                    updateView(dummyMainList!!)
                } else {
                    updateView(askVM.askDataList!!)
                }


            }

            override fun afterTextChanged(editable: Editable) {}
        })

        // BACK EVENT START
        back_layout_ask.setOnClickListener { finish() }
        img_backBtn_ask.setOnClickListener { finish() }
        // BACK BUTTON EVENT END
    }


    private fun updateView(dataList: ArrayList<AskCustomObject>) {
        layout_recomonded_programs_inask.removeAllViews()
        for (c in 0 until dataList.size) {

            val innerScrolledList: AskCustomObject = dataList[c]

            val conetentViewMain = LinearLayout(this)
            conetentViewMain.orientation = LinearLayout.VERTICAL
            //conetentViewMain.removeAllViews()

            val horizontalScrollView = HorizontalScrollView(this)

            val conetentViewSub = LinearLayout(this)
            conetentViewSub.orientation = LinearLayout.HORIZONTAL

            val sectionHeaderText = TextView(this)
            sectionHeaderText.text = innerScrolledList.title
            sectionHeaderText.setPadding(0, 20, 5, 5)
            sectionHeaderText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13F)
            val typeFace: Typeface? =
                ResourcesCompat.getFont(this.applicationContext, R.font.roboto_bold)
            sectionHeaderText.typeface = typeFace
            sectionHeaderText.setTextColor(Color.parseColor("#000000"))
            horizontalScrollView.addView(conetentViewSub)

            conetentViewMain.addView(sectionHeaderText)
            conetentViewMain.addView(horizontalScrollView)

            for (i in 0 until innerScrolledList.list!!.size) {


                val linearLayout = LinearLayout(this)
                val prams = ViewGroup.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                linearLayout.layoutParams = prams
                linearLayout.orientation = LinearLayout.HORIZONTAL

                val secondObj: AskCustomData = innerScrolledList.list[i]
                var inflater: LayoutInflater? = null
                inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
                assert(inflater != null)
                val cell = inflater!!.inflate(R.layout.recomonded_program_cell, null)
                val imgImage = cell.findViewById(R.id.img_image_rpc) as ImageView
                val txt_program_button = cell.findViewById(R.id.txt_program_button) as TextView
                val txtProgramHeading = cell.findViewById(R.id.txt_program_heading) as TextView
                val txtProgramDesc = cell.findViewById(R.id.txt_program_desc) as TextView
                val txt_offer = cell.findViewById(R.id.txt_offer) as TextView
                val offer_container = cell.findViewById(R.id.offer_container) as LinearLayout

                txtProgramHeading.setTextColor(Color.parseColor("#ffffff"))
                txt_program_button.setTextColor(Color.parseColor("#ffffff"))
                txtProgramDesc.setTextColor(Color.parseColor("#ffffff"))
                txt_program_button.setBackgroundResource(R.drawable.program_status_bg_green)


                txtProgramHeading.text = secondObj.name

                offer_container.visibility = View.GONE
                txt_program_button.text = "Online"

                txtProgramDesc.text = secondObj.speciality

                var urlImage = secondObj.image

                urlImage = urlImage.replace("zoom_level", "xxxhdpi")
                imgImage.alpha = 0.6F
                Log.d("=======Img=====", ApiClient.BASE_URL + urlImage)
                Glide.with(this).load(ApiClient.BASE_URL + urlImage).into(imgImage)

                val w = (width * 39) / 100
                val h = (width * 43) / 100
                Log.d("wwwwww", width.toString())
                Log.d("Sub Tile", w.toString())
                val lp = LinearLayout.LayoutParams(w, h)
                lp.setMargins(0, 0, 4, 0)
                cell.layoutParams = lp

                cell.tag = secondObj
                cell.setOnClickListener {

                    val obj = cell.tag as AskCustomData
                    processAction("chat", obj.id)

                }

                conetentViewSub.addView(cell)

            }


            layout_recomonded_programs_inask.addView(conetentViewMain)
        }
    }


    private fun getAskData(filtereKey: String) {

        val pref = PrefManager(this)

        val userid = pref.loginUser["uid"]

        val jsonStr = "{" +
                "\"user_id\": \"" + userid + "\"," +
                "\"filter\": \"" + filtereKey + "\"" +
                "}"


        subscription.add(askVM.getAskData("getChatExperts", jsonStr).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { proDialog_ask.visibility = View.VISIBLE }
            .doOnTerminate { proDialog_ask.visibility = View.GONE }
            .doOnError { proDialog_ask.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {
                    val dataList = askVM.askDataList

                    moviewList = ArrayList<AskCustomData>()


                    for (i in 0 until dataList!!.size) {
                        val datalis = dataList[i].list
                        moviewList!!.addAll(datalis!!)
                    }
                    updateView(dataList!!)

                } else {
                    showMessage("Failed loading data")
                }
            }, {
                showMessage("Failed loading data")
            })
        )
    }

}
