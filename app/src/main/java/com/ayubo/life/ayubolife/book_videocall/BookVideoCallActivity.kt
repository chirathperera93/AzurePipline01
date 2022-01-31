package com.ayubo.life.ayubolife.book_videocall

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.book_videocall.model.DoctorList
import com.ayubo.life.ayubolife.channeling.activity.UploadActivity
import com.ayubo.life.ayubolife.channeling.model.Expert
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension
import com.ayubo.life.ayubolife.login.SplashScreen
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.prochat.common.inflate
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import com.facebook.appevents.AppEventsLogger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ask.*
import kotlinx.android.synthetic.main.activity_book_video_call.*
import kotlinx.android.synthetic.main.activity_book_video_call.proDialog_ask
import kotlinx.android.synthetic.main.activity_video_call_booking.*
import kotlinx.android.synthetic.main.programs_store_groupview_cell.view.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class BookVideoCallActivity : BaseActivity(), BookVideoCallActivityAdapter.OnItemClickListener {

    override fun onVideoCallAction(data: Expert) {

        val strInMinist = getInTimeText(data.next!!)
        val intent = Intent(this, UploadActivity::class.java)
        intent.putExtra(UploadActivity.EXTRA_EXPERT_OBJECT, data)
        intent.putExtra(UploadActivity.EXTRA_EXPERT_OBJECT_TIME, strInMinist)
        startActivity(intent)
    }

    var width: Int = 0

    private var newMoviewList: ArrayList<DoctorList>? = null
    private var moviewList2: ArrayList<Expert>? = null
    private var dummyMainList: ArrayList<Expert>? = null

    @Inject
    lateinit var bookVideoCallActivityVM: BookVideoCallActivityVM

    companion object {
        fun startActivity(context: Context?, filtereKey: String) {
            val intent = Intent(context, BookVideoCallActivity::class.java)
            intent.putExtra("filtereKey", filtereKey)
            context!!.startActivity(intent)
        }

        lateinit var array_sort: List<Expert>
        lateinit var mainExpertList: ArrayList<Expert>
    }

    private fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey("filtereKey")) {
            val filtereKey = bundle.getSerializable("filtereKey") as String
            val loggerFB: AppEventsLogger = AppEventsLogger.newLogger(baseContext);




            if (filtereKey == "general") {
                loggerFB.logEvent("Doc_Service_VC")


                SplashScreen.firebaseAnalytics.logEvent(
                    "Doc_Service_VC",
                    null
                );

            } else if (filtereKey == "nutrition") {
                loggerFB.logEvent("Nutri_Service_VC")

                SplashScreen.firebaseAnalytics.logEvent(
                    "Nutri_Service_VC",
                    null
                );

            } else if (filtereKey == "counsellor") {
                loggerFB.logEvent("Mental_Service_VC")

                SplashScreen.firebaseAnalytics.logEvent(
                    "Mental_Service_VC",
                    null
                );

            } else if (filtereKey == "fitness") {
                loggerFB.logEvent("Fitness_Service_VC")

                SplashScreen.firebaseAnalytics.logEvent(
                    "Fitness_Service_VC",
                    null
                );

            }

            getDoctorsListFromServer(filtereKey)
        } else {
            getDoctorsListFromServer("")
        }
    }


    internal var textlength = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_video_call)

        App.getInstance().appComponent.inject(this)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val displayMetrics: DisplayMetrics = DisplayMetrics();
        windowManager.defaultDisplay.getMetrics(displayMetrics);

        val deviceScreenDimension: DeviceScreenDimension = DeviceScreenDimension(baseContext);

        width = deviceScreenDimension.displayWidth



        back_layoutview_bvc.setOnClickListener {
            finish()
        }
        img_backBtn_bvc.setOnClickListener {
            finish()
        }


        readExtras()

        edt_search_value_bvc!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                moviewList2 = bookVideoCallActivityVM.newvideoCallList
                if (edt_search_value_bvc.text.isNotEmpty()) {
                    val longerThan3 = moviewList2?.filter {
                        it.name.contains(
                            edt_search_value_bvc.text,
                            true
                        ) || it.speciality.contains(edt_search_value_bvc.text, true)
                    }
                    dummyMainList = ArrayList<Expert>()
                    dummyMainList = longerThan3 as ArrayList<Expert>
                    moviewList2 = dummyMainList!!
                    updateView()
                } else {
                    updateView()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

    }


    private fun getDoctorsListFromServer(filtereKey: String) {
        val pref = PrefManager(this)
        val userid = pref.loginUser["uid"]
        val tokenKey =
            "70564a326677614f6b575a4955744558356d393633644f2f4d454f6742342b474b696171704d6f6a5768343d"

        val jsonStr = "{" +
                "\"userID\": \"" + userid + "\"," +
                "\"doctorID\": \"" + "" + "\"," +
                "\"filter\": \"" + filtereKey + "\"" +
                "}"

        subscription.add(bookVideoCallActivityVM.getDoctorListsData(
            "videoCallSearch",
            tokenKey,
            jsonStr
        ).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { proDialog_ask.visibility = View.VISIBLE }
            .doOnTerminate { proDialog_ask.visibility = View.GONE }
            .doOnError { proDialog_ask.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {
                    mainExpertList = ArrayList<Expert>()
                    array_sort = ArrayList<Expert>()

                    moviewList2 = bookVideoCallActivityVM.newvideoCallList

                    if (moviewList2!!.size > 0) {
                        mainExpertList = moviewList2!!
                        array_sort = moviewList2!!
                        updateView()
                    } else {
                        Toast.makeText(this, "No doctors available", Toast.LENGTH_LONG).show();
                    }


//                        val timelineAdapter = BookVideoCallActivityAdapter(this@BookVideoCallActivity, moviewList2)


//                        val gridLayoutManager = GridLayoutManager(applicationContext, 2)
//                        recycleview_video_today.layoutManager = gridLayoutManager


//                        timelineAdapter.onitemClickListener = this@BookVideoCallActivity


//                        recycleview_video_today.apply {
//                            layoutManager = gridLayoutManager
//                            adapter = timelineAdapter
//                        }


                } else {
                    showMessage("Failed loading data")
                }
            }, {
                showMessage("Failed loading data")
            })
        )
    }


    private fun updateView() {
        layout_recomonded_programs_video_call.removeAllViews()

        val specsDataList = ArrayList<String>()
        var askSingleCategoryDataList = ArrayList<Expert>()


        newMoviewList = ArrayList<DoctorList>()

        for (c in 0 until moviewList2!!.size) {
            val data = moviewList2!![c]
            if (specsDataList!!.size > 0) {
                if (!specsDataList.contains(data.speciality)) {
                    specsDataList.add(data.speciality)
                }
            } else {
                specsDataList.add(data.speciality)

            }
        }

        for (n in 0 until specsDataList!!.size) {
            askSingleCategoryDataList = ArrayList<Expert>()
            val speciality = specsDataList[n]
            // val spec= data.speciality

            for (i in 0 until moviewList2!!.size) {
                val data = moviewList2!![i]
                if (moviewList2!![i].speciality == speciality) {

                    askSingleCategoryDataList.add(data)
                }
            }

            val singleCategoryConsultList = DoctorList(speciality, askSingleCategoryDataList)
            newMoviewList!!.add(singleCategoryConsultList)
        }

        System.out.println(newMoviewList)

//        for (c in 0 until dataList.size) {
//
//            val secondObj: Expert = dataList[c]
//
//
//            val linearLayout = LinearLayout(this)
//            val prams = ViewGroup.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT)
//            linearLayout.layoutParams = prams
//            linearLayout.orientation = LinearLayout.HORIZONTAL
//
//            val conetentViewSub = LinearLayout(this)
//            conetentViewSub.orientation = LinearLayout.HORIZONTAL
//
//
//            var inflater: LayoutInflater? = null
//            inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
//            assert(inflater != null)
//            val cell = inflater!!.inflate(R.layout.recomonded_program_cell, null)
//            val imgImage = cell.findViewById(R.id.img_image_rpc) as ImageView
//            val txt_program_button = cell.findViewById(R.id.txt_program_button) as TextView
//            val txtProgramHeading = cell.findViewById(R.id.txt_program_heading) as TextView
//            val txtProgramDesc = cell.findViewById(R.id.txt_program_desc) as TextView
//            val txt_offer = cell.findViewById(R.id.txt_offer) as TextView
//            val offer_container = cell.findViewById(R.id.offer_container) as LinearLayout
//
//            txtProgramHeading.setTextColor(Color.parseColor("#ffffff"))
//            txt_program_button.setTextColor(Color.parseColor("#ffffff"))
//            txtProgramDesc.setTextColor(Color.parseColor("#ffffff"))
//            txt_program_button.setBackgroundResource(R.drawable.program_status_bg_green)
//
//
//            txtProgramHeading.text = secondObj.name
//
//            offer_container.visibility = View.GONE
//            txt_program_button.text = "Online"
//
//            txtProgramDesc.text = secondObj.speciality
//
//            var urlImage = secondObj.picture
//
//            //  urlImage=urlImage.replace("zoom_level", "xxxhdpi")
//            imgImage.alpha = 0.6F
//            Log.d("=======Img=====", ApiClient.BASE_URL + urlImage)
//            //  Glide.with(this).load(ApiClient.BASE_URL + urlImage).into(imgImage)
//
//            urlImage = urlImage!!.replace("zoom_level", "xxxhdpi")
//
//            //   img_image_rpc.alpha= 0.6F
//            Glide.with(this).load(urlImage).into(imgImage)
//
//            val w = (width * 39) / 100
//            val h = (width * 43) / 100
//            Log.d("wwwwww", width.toString())
//            Log.d("Sub Tile", w.toString())
//            val lp = LinearLayout.LayoutParams(w, h)
//            lp.setMargins(10, 0, 5, 0)
//            cell.layoutParams = lp
//
//            cell.tag = secondObj
////                cell.setOnClickListener {
////
////                    val obj = cell.tag as AskCustomData
////                    processAction("chat",obj.id)
////
////                }
//
//            conetentViewSub.addView(cell)
//            // layout_recomonded_programs_in_videocall.addView(conetentViewSub)
//        }


        for (c in 0 until newMoviewList!!.size) {

            val innerScrolledList: DoctorList = newMoviewList!![c]

            val conetentViewMain = LinearLayout(this)
            conetentViewMain.orientation = LinearLayout.VERTICAL
            //conetentViewMain.removeAllViews()

            val horizontalScrollView = HorizontalScrollView(this)

            val conetentViewSub = LinearLayout(this)
            conetentViewSub.orientation = LinearLayout.HORIZONTAL

            val sectionHeaderText = TextView(this)
            sectionHeaderText.text = innerScrolledList.speciality
            sectionHeaderText.setPadding(0, 20, 5, 5)
            sectionHeaderText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13F)
            val typeFace: Typeface? =
                ResourcesCompat.getFont(this.applicationContext, R.font.roboto_bold)
            sectionHeaderText.typeface = typeFace
            sectionHeaderText.setTextColor(Color.parseColor("#000000"))
            horizontalScrollView.addView(conetentViewSub)

            conetentViewMain.addView(sectionHeaderText)
            conetentViewMain.addView(horizontalScrollView)

            for (i in 0 until innerScrolledList.doctors!!.size) {


                val linearLayout = LinearLayout(this)
                val prams = ViewGroup.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                linearLayout.layoutParams = prams
                linearLayout.orientation = LinearLayout.HORIZONTAL

                val secondObj: Expert = innerScrolledList.doctors[i]
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

                var urlImage = secondObj.picture

                urlImage = urlImage.replace("zoom_level", "xxxhdpi")
                imgImage.alpha = 0.6F
                Log.d("=======Img=====", ApiClient.BASE_URL + urlImage)
                Glide.with(this).load(urlImage).into(imgImage)

                val w = (width * 39) / 100
                val h = (width * 43) / 100
                Log.d("wwwwww", width.toString())
                Log.d("Sub Tile", w.toString())
                val lp = LinearLayout.LayoutParams(w, h)
                lp.setMargins(0, 0, 4, 0)
                cell.layoutParams = lp

                cell.tag = secondObj
                cell.setOnClickListener {

                    onVideoCallAction(secondObj)

                }

                conetentViewSub.addView(cell)

            }

            proDialog_ask.visibility = View.GONE
            layout_recomonded_programs_video_call.addView(conetentViewMain)
        }


    }


    inner class BookVideoCallActivityAdapterNew(
        val context: Context,
        val mData: ArrayList<Expert>
    ) :
        RecyclerView.Adapter<BookVideoCallActivityAdapterNew.BaseHolder>(), Filterable {

        //   var onitemClickListener: OnItemClickListener? = null


        //        interface OnItemClickListener {
//            fun onVideoCallAction(action: Expert)
//        }
        internal var mfilter: NewFilter

        override fun getFilter(): Filter {
            return mfilter
        }

        init {
            mfilter = NewFilter(this@BookVideoCallActivityAdapterNew)
        }

        inner class NewFilter(var mAdapter: BookVideoCallActivityAdapterNew) : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {

                var mainExpertListNew: ArrayList<Expert> = ArrayList<Expert>()
                val results = FilterResults()

                val originalDataList = bookVideoCallActivityVM.newvideoCallList

                if (charSequence.isEmpty()) {
                    mainExpertListNew.addAll(originalDataList)
                } else {
                    val filterPattern = charSequence.toString().toLowerCase().trim { it <= ' ' }
                    for (listcountry in originalDataList) {
                        if (listcountry.name.toLowerCase().startsWith(filterPattern)) {
                            mainExpertListNew.add(listcountry)
                        }
                    }
                }

                mainExpertList.clear()
                mainExpertList.addAll(mainExpertListNew)
                results.values = mainExpertList
                results.count = mainExpertList.size
                return results
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                this.mAdapter.notifyDataSetChanged()

            }

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
            when (viewType) {

                TYPE_DETAILS -> {
                    return ShowPaymentOption(parent)
                }
                else -> {
                    return ShowPaymentOption(parent)
                }
//            else -> {
//                return ShowHeading(parent)
//            }
            }
        }

        override fun getItemCount() = mData.size

        override fun onBindViewHolder(holder: BaseHolder, position: Int) {
            holder.bind(mData[position])
        }

        override fun getItemViewType(position: Int): Int {

            val objAny = mData[position]
            var cellType: Int = 2
//        if (objAny is String) {
//            cellType=1
//        }
//        if (objAny is BookVideoCallActivityMainResponseData) {
//            cellType=2
//        }


            return cellType
        }

        open inner class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
            open fun bind(item: Any) = with(itemView) {
            }

            open fun release() {
            }
        }

        private fun getDateAsString(s: Timestamp): String {
            try {
                val sdf = SimpleDateFormat("MM/dd/yyyy")
                val netDate = Date(s.time)

                return sdf.format(netDate)
            } catch (e: Exception) {
                return e.toString()
            }
        }

        fun convertDateToLong(date: String): Long {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm")
            return df.parse(date).time
        }

        fun getInTimeText(strTime: String): String {

            var timingText = ""

            val timestamp = Timestamp(convertDateToLong(strTime))
            //Getting today date string.........
            val currentTimestamp = System.currentTimeMillis()
            val timestampToday: Timestamp = Timestamp(currentTimestamp)
            val today: String = getDateAsString(timestampToday)
            val date1 = getDateAsString(timestamp)


            val deff = com.ayubo.life.ayubolife.utility.Utility.getTimpstampDifferent(
                convertDateToLong(strTime), currentTimestamp
            )
            val minutes = TimeUnit.MILLISECONDS.toMinutes(deff)


            if (date1.compareTo(today) == 0) {
                //    System.out.println("Date1 is a Today   "+date1)

                if (minutes > 59) {
                    val result = minutes % 60
                    val inthour = minutes / 60
                    timingText = "in $inthour Hr $result Mins"
                } else {
                    timingText = "in $minutes Mins"
                }


                Log.d(".......minits........", minutes.toString())

            } else {


                var inDays = minutes / 1440
                inDays += 1

                if (inDays.toInt() == 1) {
                    timingText = "in $inDays Day"
                } else {
                    timingText = "in $inDays Days"
                }

                Log.d(".............", strTime + "   " + timingText)
                //    System.out.println("Date1 is a Future   "+date1)

            }

            return timingText
        }


        inner class ShowPaymentOption(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.programs_store_groupview_cell)) {

            override fun bind(item: Any) = with(itemView) {


                val secondObj: Expert = item as Expert
                card_view_store_group.tag = secondObj

                txt_program_heading.text = secondObj.title + secondObj.name
                txt_program_desc.text = secondObj.speciality

                txt_program_heading.setTextColor(Color.parseColor("#ffffff"))
                txt_program_desc.setTextColor(Color.parseColor("#ffffff"))
                txt_program_button.setTextColor(Color.parseColor("#ffffff"))
                txt_program_button.setBackgroundResource(R.drawable.program_status_bg_green)


                offer_container.visibility = View.GONE

                if (secondObj.next != "Not found") {
                    txt_program_button.text = getInTimeText(secondObj.next!!)
                }


                var urlImage = secondObj.picture
                urlImage = urlImage!!.replace("zoom_level", "xxxhdpi")

                img_image_rpc.alpha = 0.6F
                Glide.with(context).load(urlImage).into(img_image_rpc)
                card_view_store_group.setOnClickListener {
                    onVideoCallAction(secondObj)
                    // processAction("chat",secondObj.name)

                }

                return@with
            }
        }


        inner class ShowHeading(parent: ViewGroup) :
            BaseHolder(parent.inflate(R.layout.book_videocall_cell_heading)) {

            override fun bind(item: Any) = with(itemView) {

                //   val data: String = item as String
                //    txt_heading_videocall.text=data

                return@with
            }
        }


    }


    //===ADAPTER ENDED================================

    fun convertDateToLong(date: String): Long {


        val df = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return df.parse(date).time
    }

    private fun getDateAsString(s: Timestamp): String {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(s.time)

            return sdf.format(netDate)
        } catch (e: Exception) {
            return ""
        }
    }


    fun getInTimeText(strTime: String): String {

        var timingText = ""
        Log.d("......time............", strTime)

        val timestamp = Timestamp(convertDateToLong(strTime))
        //Getting today date string.........
        val currentTimestamp = System.currentTimeMillis()
        val timestampToday: Timestamp = Timestamp(currentTimestamp)
        val today: String = getDateAsString(timestampToday)
        val date1 = getDateAsString(timestamp)


        val deff = com.ayubo.life.ayubolife.utility.Utility.getTimpstampDifferent(
            convertDateToLong(strTime),
            currentTimestamp
        )
        val minutes = TimeUnit.MILLISECONDS.toMinutes(deff)


        if (date1.compareTo(today) == 0) {
            //    System.out.println("Date1 is a Today   "+date1)

            if (minutes > 59) {
                val result = minutes % 60
                val inthour = minutes / 60
                timingText = "In $inthour Hr $result Mins"
            } else {
                timingText = "In $minutes Mins"
            }


            Log.d(".......minits........", minutes.toString())

        } else {


            var inDays = minutes / 1440
            inDays += 1

            if (inDays.toInt() == 1) {
                timingText = "in $inDays Day"
            } else {
                timingText = "in $inDays Days"
            }

            Log.d(".............", strTime + "   " + timingText)
            //    System.out.println("Date1 is a Future   "+date1)

        }

        return timingText
    }
}
