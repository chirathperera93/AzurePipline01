package com.ayubo.life.ayubolife.lifeplus.NewToDo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.ask.AskActivity
import com.ayubo.life.ayubolife.challenges.NewCHallengeActivity
import com.ayubo.life.ayubolife.channeling.activity.DashboardActivity
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity
import com.ayubo.life.ayubolife.channeling.activity.VisitDoctorActivity
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction
import com.ayubo.life.ayubolife.common.SetDiscoverPage
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.experts.Activity.MyDoctor_Activity
import com.ayubo.life.ayubolife.fragments.HomePage_Utility
import com.ayubo.life.ayubolife.goals.AchivedGoal_Activity
import com.ayubo.life.ayubolife.goals.PickAGoal_Activity
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity
import com.ayubo.life.ayubolife.health.OMCommon
import com.ayubo.life.ayubolife.health.OMMainPage
import com.ayubo.life.ayubolife.home_group_view.GroupViewActivity
import com.ayubo.life.ayubolife.home_popup_menu.AskQuestion_Activity
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.IntroActivity
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateActivity
import com.ayubo.life.ayubolife.janashakthionboarding.welcome.JanashakthiWelcomeActivity
import com.ayubo.life.ayubolife.lifeplus.DiscoverToNewDashboard
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.lifeplus.TimeZone
import com.ayubo.life.ayubolife.lifeplus.WeeklyCalendar.WeeklyCalendarAdapter
import com.ayubo.life.ayubolife.lifeplus.WeeklyCalendar.WeeklyItem
import com.ayubo.life.ayubolife.map_challenges.Badges_Activity
import com.ayubo.life.ayubolife.map_challenges.MapChallengeActivity
import com.ayubo.life.ayubolife.map_challenges.activity.NewLeaderBoardActivity
import com.ayubo.life.ayubolife.map_challenges.treasureview.TreasureViewActivity
import com.ayubo.life.ayubolife.notification.activity.SingleTimeline_Activity
import com.ayubo.life.ayubolife.payment.EXTRA_TREASURE_KEY
import com.ayubo.life.ayubolife.payment.activity.PaymentActivity
import com.ayubo.life.ayubolife.payment.activity.PaymentPinSubmitActivity
import com.ayubo.life.ayubolife.payment.activity.PaymentSummaryViewActivity
import com.ayubo.life.ayubolife.payment.model.PriceList
import com.ayubo.life.ayubolife.post.activity.NativePostActivity
import com.ayubo.life.ayubolife.post.activity.NativePostJSONActivity
import com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo
import com.ayubo.life.ayubolife.programs.ProgramActivity
import com.ayubo.life.ayubolife.programs.data.model.Experts
import com.ayubo.life.ayubolife.reports.activity.ReportDetailsActivity
import com.ayubo.life.ayubolife.reports.getareview.GetAReviewActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity
import com.ayubo.life.ayubolife.timeline.OpenPostActivity
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.walk_and_win.WalkWinMainActivity
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import org.michaelbel.bottomsheet.BottomSheet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewProfileToDo.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewProfileToDo : Fragment(),
    WeeklyCalendarAdapter.OnWeeklyCalendarItemClickListner,
    ToDoItemAdapter.OnToDoItemCardClickListener,
    ToDoItemAdapter.OnToDoItemCardLongPressListener {

    lateinit var pref: PrefManager;
    lateinit var appToken: String;
    lateinit var groupId: String;
    lateinit var newToDoLoading: ProgressAyubo;
    lateinit var toDoWeeklyDateRecyclerView: RecyclerView;
    lateinit var to_do_list_empty_linear_layout: RelativeLayout;
    lateinit var to_do_item_recycler_view_relative_layout: RelativeLayout;
    lateinit var to_do_weekly_date_recycler_view_relative_layout: RelativeLayout;
    lateinit var mainView: View;
    var isVisibleCalendar: Boolean = true;
    var IsEmptyData: Boolean = false;
    var isShowMoreOpened: Boolean = false;
    var selectedTime: Long = 0L;

    var daysOfWeek: ArrayList<String> = ArrayList<String>();
    var weeklyItemsArrayList = ArrayList<WeeklyItem>();

    lateinit var calendarView: com.prolificinteractive.materialcalendarview.MaterialCalendarView;
    lateinit var viewCalendarTopicText: TextView;
    lateinit var viewCalendarShowLessTextView: TextView;
    lateinit var future_appointment: ImageView;
    lateinit var previous_appointment: ImageView;
    lateinit var calendarViewLinearLayout: LinearLayout;
    lateinit var get_action_text: LinearLayout;
    lateinit var notificationsForWeek: JsonArray;
    var selectedDateFromCalendar: String = "";

    var sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy");
    var holidayDates: HashSet<CalendarDay>? = null;

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    var oldSelectedDate: CalendarDay? = null;

    fun toCalendar(date: Date): Calendar {
        val cal: Calendar = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_new_profile_to_do, container, false)

        toDoWeeklyDateRecyclerView =
            mainView.findViewById(R.id.to_do_weekly_date_recycler_view) as RecyclerView

        get_action_text = mainView.findViewById(R.id.get_action_text) as LinearLayout

        get_action_text.visibility = View.GONE

        newToDoLoading = mainView.findViewById(R.id.new_to_do_loading) as ProgressAyubo

        toDoWeeklyDateRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        pref = PrefManager(getContext());
        appToken = pref.getUserToken();
        IsEmptyData = false;

        calendarView =
            mainView.findViewById(R.id.calendarView) as com.prolificinteractive.materialcalendarview.MaterialCalendarView
        calendarViewLinearLayout =
            mainView.findViewById(R.id.calendarViewLinearLayout) as LinearLayout
        viewCalendarTopicText = mainView.findViewById(R.id.viewCalendarTopicText) as TextView
        viewCalendarShowLessTextView =
            mainView.findViewById(R.id.viewCalendarShowLessTextView) as TextView
        to_do_weekly_date_recycler_view_relative_layout =
            mainView.findViewById(R.id.to_do_weekly_date_recycler_view_relative_layout) as RelativeLayout
        to_do_list_empty_linear_layout =
            mainView.findViewById(R.id.to_do_list_empty_linear_layout) as RelativeLayout
        future_appointment = mainView.findViewById(R.id.future_appointment) as ImageView
        previous_appointment = mainView.findViewById(R.id.previous_appointment) as ImageView
        to_do_list_empty_linear_layout.visibility = View.GONE


        val unwrappedDrawable: Drawable? =
            AppCompatResources.getDrawable(requireContext(), R.drawable.calendar_circle_bg);
        val wrappedDrawable: Drawable? = unwrappedDrawable?.let { DrawableCompat.wrap(it) };
        if (wrappedDrawable != null) {
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#2CD889"))
        };




        future_appointment.setImageDrawable(requireContext().resources.getDrawable(R.drawable.calendar_circle_bg))


        val unwrappedDrawableNew: Drawable? =
            AppCompatResources.getDrawable(requireContext(), R.drawable.calendar_circle_bg);
        val wrappedDrawableNew: Drawable? = unwrappedDrawableNew?.let { DrawableCompat.wrap(it) };
        if (wrappedDrawableNew != null) {
            DrawableCompat.setTint(unwrappedDrawableNew, Color.parseColor("#F05B5B"))
        };





        previous_appointment.setImageDrawable(requireContext().resources.getDrawable(R.drawable.calendar_circle_bg))

        viewCalendarTopicText.setOnClickListener {
            selectedDateFromCalendar = ""
            isShowMoreOpened = true;
            calendarViewLinearLayout.visibility = View.VISIBLE
            viewCalendarTopicText.visibility = View.GONE
            to_do_weekly_date_recycler_view_relative_layout.visibility = View.GONE
            viewCalendarShowLessTextView.visibility = View.VISIBLE
            val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.BELOW, R.id.viewCalendarShowLessRelativeLayout);
            params.setMargins(8, 0, 8, 0)


            val paramsForEmptyView: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
            paramsForEmptyView.addRule(
                RelativeLayout.BELOW,
                R.id.viewCalendarShowLessRelativeLayout
            );
            paramsForEmptyView.setMargins(8, 16, 8, 0)

            to_do_item_recycler_view_relative_layout.layoutParams = params;
            if (IsEmptyData) {
                to_do_list_empty_linear_layout.layoutParams = paramsForEmptyView;
                to_do_list_empty_linear_layout.visibility = View.VISIBLE

            } else {
                to_do_list_empty_linear_layout.visibility = View.GONE
            }



            calendarView.clearSelection();


            val date = Date(selectedTime)
            val cal2: Calendar = toCalendar(date);
            calendarView.setSelectedDate(cal2);


            val calIns = Calendar.getInstance()
            if (calIns.time.day != cal2.time.day) {
                calendarView.addDecorator(object : DayViewDecorator {
                    override fun shouldDecorate(day: CalendarDay?): Boolean {
                        val cal1: Calendar = day!!.calendar;
                        val cal2: Calendar = Calendar.getInstance();
                        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                                && cal1.get(Calendar.DAY_OF_YEAR) ==
                                cal2.get(Calendar.DAY_OF_YEAR));
                    }

                    override fun decorate(view: DayViewFacade?) {
                        context?.let {
                            ContextCompat.getDrawable(
                                it,
                                R.drawable.selector_calendar_disable_date
                            )?.let { view!!.setBackgroundDrawable(it) }
                        };
                    }

                })
            }

            getToDoData(date.time, false);

        }

        viewCalendarShowLessTextView.setOnClickListener {
            isShowMoreOpened = false;
            selectedDateFromCalendar = ""
            viewCalendarShowLessTextView.visibility = View.GONE
            viewCalendarTopicText.visibility = View.VISIBLE
            to_do_weekly_date_recycler_view_relative_layout.visibility = View.VISIBLE
            calendarViewLinearLayout.visibility = View.GONE
            val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.BELOW, R.id.viewCalendarTopic);
            params.setMargins(8, 20, 8, 0)

            to_do_item_recycler_view_relative_layout.layoutParams = params

            val paramsForEmptyView: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
            paramsForEmptyView.addRule(RelativeLayout.BELOW, R.id.viewCalendarTopic);
            paramsForEmptyView.setMargins(8, 150, 8, 0)
            paramsForEmptyView.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);


            if (IsEmptyData) {
                to_do_list_empty_linear_layout.layoutParams = paramsForEmptyView;
                to_do_list_empty_linear_layout.visibility = View.VISIBLE
            } else {
                to_do_list_empty_linear_layout.visibility = View.GONE
            }

            val cal2: CalendarDay = calendarView.selectedDate;

            var month = ""
            if ((cal2.month + 1) > 9) {
                month = (cal2.month + 1).toString()
            } else {
                month = "0" + (cal2.month + 1).toString()
            }


            selectedDateFromCalendar =
                month + "/" + cal2.day.toString() + "/" + cal2.year.toString()

            getToDoData(convertDateToLong(selectedDateFromCalendar), false);


        }

        calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(
                widget: MaterialCalendarView,
                calendarDay: CalendarDay,
                selected: Boolean
            ) {

                val calIns = Calendar.getInstance()
                val selectedDate = calendarDay!!.calendar

                if (calIns.time.day == selectedDate.time.day) {
                    calendarView.addDecorator(object : DayViewDecorator {
                        override fun shouldDecorate(day: CalendarDay?): Boolean {
                            val cal1: Calendar = day!!.calendar;
                            val cal2: Calendar = Calendar.getInstance();


                            return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                                    && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                                    && cal1.get(Calendar.DAY_OF_YEAR) ==
                                    cal2.get(Calendar.DAY_OF_YEAR));
                        }

                        override fun decorate(view: DayViewFacade?) {
                            context?.let {
                                ContextCompat.getDrawable(
                                    it,
                                    R.drawable.selector_calendar_date
                                )?.let { view!!.setBackgroundDrawable(it) }
                            };
                        }

                    })
                } else {

                    calendarView.addDecorator(object : DayViewDecorator {
                        override fun shouldDecorate(day: CalendarDay?): Boolean {
                            val cal1: Calendar = day!!.calendar;
                            val cal2: Calendar = Calendar.getInstance();
                            return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                                    && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                                    && cal1.get(Calendar.DAY_OF_YEAR) ==
                                    cal2.get(Calendar.DAY_OF_YEAR));
                        }

                        override fun decorate(view: DayViewFacade?) {
                            context?.let {
                                ContextCompat.getDrawable(
                                    it,
                                    R.drawable.selector_calendar_disable_date
                                )?.let { view!!.setBackgroundDrawable(it) }
                            };
                        }

                    })
                }


                var month = ""
                if ((calendarDay.month + 1) > 9) {
                    month = (calendarDay.month + 1).toString()
                } else {
                    month = "0" + (calendarDay.month + 1).toString()
                }


                val myDate: String =
                    calendarDay.year.toString() + "/" + month + "/" + calendarDay.day.toString()
                val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd");
                val date: Date = sdf.parse(myDate);
                val millis: Long = date.getTime();

                System.out.println(millis)


                getToDoData(millis, false);

                oldSelectedDate = calendarDay;

            }

        })


        return mainView;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewProfileToDo.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewProfileToDo().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClick(item: WeeklyItem, position: Int) {
        selectedDateFromCalendar = ""
        selectedTime = item.longDate;
        selectedDateFromCalendar = convertLongToTime(selectedTime)
        getToDoData(selectedTime, false);
    }

    fun getToDoData(milisecondDate: Long, firstTimeLoads: Boolean) {

        val getNewDashBoardData = pref.newDashBoardData;

        if (getNewDashBoardData.equals("")) {
            groupId = "user_dashboard"
        } else {
            groupId = getNewDashBoardData;
        }

        newToDoLoading.visibility = View.VISIBLE

        val timeZone: TimeZone = TimeZone()
        val milisecondDateForTimeZone: Long = timeZone.utcToLocal(Date(milisecondDate))

        val timeZoneNew: java.util.TimeZone = java.util.TimeZone.getDefault();
        val timeZoneId = timeZoneNew.getID();

        to_do_list_empty_linear_layout.visibility = View.GONE
        val apiService: ApiInterface =
            ApiClient.getAzureApiClientV1ForTodo().create(ApiInterface::class.java);
        val call: Call<ProfileDashboardResponseData> = apiService.getAllUserToDoLists(
            AppConfig.APP_BRANDING_ID,
            appToken,
            groupId,
            milisecondDateForTimeZone,
            timeZoneId
        );
        call.enqueue(object : Callback<ProfileDashboardResponseData> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<ProfileDashboardResponseData>,
                response: Response<ProfileDashboardResponseData>
            ) {
                newToDoLoading.visibility = View.GONE
                if (response.isSuccessful) {

                    val toDoMainData: JsonObject =
                        Gson().toJsonTree(response.body()!!.data).asJsonObject;


//                    if (firstTimeLoads) {
                    val dateFormat: DateFormat = SimpleDateFormat("MM/dd/yyyy");
                    val calendar: Calendar = Calendar.getInstance();
                    daysOfWeek = ArrayList<String>();
                    if (toDoMainData.get("start_of_the_week").asString.equals("monday")) {
                        calendar.setFirstDayOfWeek(Calendar.MONDAY);
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        daysOfWeek = ArrayList<String>();

                        for (i in 0..6) {
                            daysOfWeek.add(dateFormat.format(calendar.getTime()))
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                        }
                    } else {
                        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        daysOfWeek = ArrayList<String>();

                        for (i in 0..6) {
                            daysOfWeek.add(dateFormat.format(calendar.getTime()))
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                        }
                    }


                    weeklyItemsArrayList = ArrayList<WeeklyItem>()
                    for (day in daysOfWeek) {
                        System.out.println(day)
                        val simpleDateFormatForDate: SimpleDateFormat =
                            SimpleDateFormat("MM/dd/yyyy");
                        val simpleDateFormatForDayName: SimpleDateFormat =
                            SimpleDateFormat("MM/dd/yyyy");
                        val simpleDateFormatForCompare: SimpleDateFormat =
                            SimpleDateFormat("MM/dd/yyyy");

                        val dateForDay: Date = simpleDateFormatForDate.parse(day);
                        val dateForDayName: Date = simpleDateFormatForDate.parse(day);

                        simpleDateFormatForDate.applyPattern("dd");
                        simpleDateFormatForDayName.applyPattern("EE");

                        val newDateString = simpleDateFormatForDate.format(dateForDay);
                        val newDateStringForName =
                            simpleDateFormatForDayName.format(dateForDayName);


                        System.out.println(newDateString)
                        System.out.println(newDateStringForName)

                        val calobj: Calendar = Calendar.getInstance();
                        System.out.println(simpleDateFormatForCompare.format(calobj.getTime()));


                        val date1: Date = SimpleDateFormat("MM/dd/yyyy").parse(day);
                        System.out.println(date1);

                        var dateToCompare = ""

                        if (!selectedDateFromCalendar.equals("")) {
                            dateToCompare = selectedDateFromCalendar;
                        } else {
                            dateToCompare = simpleDateFormatForCompare.format(calobj.getTime());
                        }

                        if (dateToCompare.compareTo(day) == 0) {
                            weeklyItemsArrayList.add(
                                WeeklyItem(
                                    newDateStringForName,
                                    newDateString,
                                    true,
                                    date1.time
                                )
                            )
                            selectedTime = date1.time
                        } else {
                            weeklyItemsArrayList.add(
                                WeeklyItem(
                                    newDateStringForName,
                                    newDateString,
                                    false,
                                    date1.time
                                )
                            )
                        }


                    }

                    notificationsForWeek = toDoMainData.get("notifications_for_week").asJsonArray
                    val notificationsForMonth: JsonArray =
                        toDoMainData.get("notifications_for_month").asJsonArray

                    val notificationsForWeekItemArrayList: ArrayList<NotificationDateObject> =
                        ArrayList<NotificationDateObject>();
                    val notificationsForMonthItemArrayList: ArrayList<NotificationDateObject> =
                        ArrayList<NotificationDateObject>();


                    if (notificationsForWeek.size() > 0) {
                        for (i in 0 until notificationsForWeek.size()) {
                            val notificationsForWeekItem = notificationsForWeek.get(i)
                            notificationsForWeekItemArrayList.add(
                                NotificationDateObject(
                                    notificationsForWeekItem.asJsonObject.get("date").asInt,
                                    notificationsForWeekItem.asJsonObject.get("day").asString,
                                    notificationsForWeekItem.asJsonObject.get("color").asString,
                                    notificationsForWeekItem.asJsonObject.get("timestamp").asLong

                                )
                            )


                        }
                    }

                    if (notificationsForMonth.size() > 0) {
                        for (i in 0 until notificationsForMonth.size()) {
                            val notificationsForMonthItem = notificationsForMonth.get(i)
                            notificationsForMonthItemArrayList.add(
                                NotificationDateObject(
                                    notificationsForMonthItem.asJsonObject.get("date").asInt,
                                    notificationsForMonthItem.asJsonObject.get("day").asString,
                                    notificationsForMonthItem.asJsonObject.get("color").asString,
                                    notificationsForMonthItem.asJsonObject.get("timestamp").asLong

                                )
                            )


                        }
                    }




                    for (i in 0 until notificationsForMonthItemArrayList.size) {
                        holidayDates = HashSet<CalendarDay>();
                        val c: Calendar = Calendar.getInstance();
                        c.setTime(
                            sdf.parse(
                                convertLongToTimeNew(
                                    notificationsForMonthItemArrayList.get(
                                        i
                                    ).timestamp
                                )
                            )
                        );
                        val dayOfWeek: Int = c.get(Calendar.DAY_OF_WEEK);
                        val cd = CalendarDay.from(
                            c.get(Calendar.YEAR),
                            c.get(Calendar.MONTH),
                            c.get(Calendar.DAY_OF_MONTH)
                        )
                        holidayDates!!.add(cd);

                        val calendarViewDateObject = CalendarViewDateObject(
                            notificationsForMonthItemArrayList.get(i).color,
                            holidayDates!!
                        )

                        calendarView.addDecorator(
                            DateDecoratorNewForDay(
                                Color.parseColor(calendarViewDateObject.calendarDayColor),
                                calendarViewDateObject.calendarDay,
                                context!!
                            )
                        )

                    }

                    val adapter = activity?.windowManager?.let {
                        context?.let { it1 ->
                            WeeklyCalendarAdapter(
                                it1,
                                weeklyItemsArrayList,
                                it,
                                this@NewProfileToDo,
                                notificationsForWeekItemArrayList
                            )
                        }
                    }
                    toDoWeeklyDateRecyclerView.adapter = adapter
//                    }

                    val toDoItemRecyclerView =
                        mainView.findViewById(R.id.to_do_item_recycler_view) as RecyclerView
                    toDoItemRecyclerView.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    val toDoItemArrayList = ArrayList<ToDoItem>()
                    val tasksJsonArray: JsonArray = toDoMainData.get("tasks").asJsonArray
                    to_do_item_recycler_view_relative_layout =
                        mainView.findViewById(R.id.to_do_item_recycler_view_relative_layout) as RelativeLayout

                    get_action_text.visibility = View.GONE
                    to_do_item_recycler_view_relative_layout.visibility = View.GONE
                    if (tasksJsonArray.size() > 0) {
                        to_do_item_recycler_view_relative_layout.visibility = View.VISIBLE
                        for (i in 0 until tasksJsonArray.size()) {
                            val taskItem = tasksJsonArray.get(i)

                            toDoItemArrayList.add(
                                ToDoItem(
                                    if (taskItem.asJsonObject.get("id") == null) "" else taskItem.asJsonObject.get(
                                        "id"
                                    ).asString,
                                    if (taskItem.asJsonObject.get("app_id") == null) "" else taskItem.asJsonObject.get(
                                        "app_id"
                                    ).asString,
                                    if (taskItem.asJsonObject.get("user_id") == null) "" else taskItem.asJsonObject.get(
                                        "user_id"
                                    ).asString,
                                    if (taskItem.asJsonObject.get("title") == null) "" else taskItem.asJsonObject.get(
                                        "title"
                                    ).asString,
                                    if (taskItem.asJsonObject.get("description") == null) "" else taskItem.asJsonObject.get(
                                        "description"
                                    ).asString,
                                    if (taskItem.asJsonObject.get("task_icon") == null) "" else taskItem.asJsonObject.get(
                                        "task_icon"
                                    ).asString,
                                    if (taskItem.asJsonObject.get("icon") == null) "" else taskItem.asJsonObject.get(
                                        "icon"
                                    ).asString,
                                    if (taskItem.asJsonObject.get("status") == null) "" else taskItem.asJsonObject.get(
                                        "status"
                                    ).asString,
                                    if (taskItem.asJsonObject.get("created_datetime") == null) 0L else taskItem.asJsonObject.get(
                                        "created_datetime"
                                    ).asLong,
                                    if (taskItem.asJsonObject.get("updated_datetime") == null) 0L else taskItem.asJsonObject.get(
                                        "updated_datetime"
                                    ).asLong,
                                    if (taskItem.asJsonObject.get("task_datetime") == null) 0L else taskItem.asJsonObject.get(
                                        "task_datetime"
                                    ).asLong,
                                    if (taskItem.asJsonObject.get("action") == null) "" else taskItem.asJsonObject.get(
                                        "action"
                                    ).asString,
                                    if (taskItem.asJsonObject.get("meta") == null) "" else taskItem.asJsonObject.get(
                                        "meta"
                                    ).asString


                                )

                            )


                        }
                        val toDoItemAdapter = context?.let {
                            ToDoItemAdapter(
                                it,
                                toDoItemArrayList,
                                this@NewProfileToDo,
                                this@NewProfileToDo
                            )
                        }
                        toDoItemRecyclerView.adapter = toDoItemAdapter
                        IsEmptyData = false;
                        get_action_text.visibility = View.VISIBLE
                    } else {
                        toDoItemRecyclerView.removeAllViews()
                        to_do_list_empty_linear_layout.visibility = View.VISIBLE
                        IsEmptyData = true;
                        get_action_text.visibility = View.GONE

                        if (!isShowMoreOpened) {
                            val layoutParams: RelativeLayout.LayoutParams =
                                to_do_list_empty_linear_layout.layoutParams as (RelativeLayout.LayoutParams);
                            layoutParams.addRule(
                                RelativeLayout.CENTER_IN_PARENT,
                                RelativeLayout.TRUE
                            );
                            to_do_list_empty_linear_layout.setLayoutParams(layoutParams);
                        } else {
                            val layoutParams: RelativeLayout.LayoutParams =
                                to_do_list_empty_linear_layout.layoutParams as (RelativeLayout.LayoutParams);
                            layoutParams.addRule(
                                RelativeLayout.BELOW,
                                R.id.viewCalendarShowLessRelativeLayout
                            );
                            layoutParams.setMargins(8, 16, 8, 0)
                            to_do_list_empty_linear_layout.setLayoutParams(layoutParams);
                        }


                    }


                }
            }

            override fun onFailure(call: Call<ProfileDashboardResponseData>, t: Throwable) {
                newToDoLoading.visibility = View.GONE
                get_action_text.visibility = View.GONE
            }

        })
    }

    fun setAction(toDoItemId: String, toDoItemStatus: String) {
        newToDoLoading.visibility = View.VISIBLE
        val requestToDoAction = RequestToDoAction(toDoItemId, toDoItemStatus);
        val apiService: ApiInterface =
            ApiClient.getAzureApiClientV1ForToDoUpdate().create(ApiInterface::class.java);
        val call: Call<ProfileDashboardResponseData> =
            apiService.toDoUpdateStatus(AppConfig.APP_BRANDING_ID, appToken, requestToDoAction);
        call.enqueue(object : Callback<ProfileDashboardResponseData> {

            override fun onResponse(
                call: Call<ProfileDashboardResponseData>,
                response: Response<ProfileDashboardResponseData>
            ) {
                newToDoLoading.visibility = View.GONE
                getToDoData(selectedTime, false);
            }

            override fun onFailure(call: Call<ProfileDashboardResponseData>, t: Throwable) {
                newToDoLoading.visibility = View.GONE
            }

        })
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            getToDoData(System.currentTimeMillis(), true)
        }
    }


    override fun onItemClick(item: ToDoItem, position: Int) {
        val current = convertLongToTime(System.currentTimeMillis());
        val taskDateTime = convertLongToTime(item.task_datetime);
        if (taskDateTime.compareTo(current) > 0) {
            return;
        } else if (taskDateTime.compareTo(current) < 0) {

            if (!item.action.equals("dynamicquestion"))
                onProcessAction(item.action, item.meta);
        } else {
            onProcessAction(item.action, item.meta);
        }

    }

    override fun onItemLongPress(item: ToDoItem, position: Int) {
        val now: String = convertLongToTime(System.currentTimeMillis());
        val d: String = convertLongToTime(item.task_datetime);
        if (now.compareTo(d) > 0) {
            return
        } else if (now.compareTo(d) < 0) {
            return

        } else {
            openBottomSlider(item);
        }


    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val simpleDateFormatForDate = SimpleDateFormat("MM/dd/yyyy");
        return simpleDateFormatForDate.format(date)
    }

    fun convertLongToTimeNew(time: Long): String {
        val date = Date(time)
        val simpleDateFormatForDate = SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormatForDate.format(date)
    }


    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("MM/dd/yyyy")
        return df.parse(date).time
    }

    fun openBottomSlider(item: ToDoItem) {
        val builder: BottomSheet.Builder? = context?.let { BottomSheet.Builder(it) };
        if (builder != null) {
            builder.setTitle("Title")
                .setView(R.layout.to_do_bottom_sheet)
                .setFullWidth(false)
                .show();

            builder.view.findViewById<LinearLayout>(R.id.done_button_linear_layout)
                .setOnClickListener {
                    builder.dismiss()
                    setAction(item.id, "done");
                }

            builder.view.findViewById<LinearLayout>(R.id.cancel_button_linear_layout)
                .setOnClickListener {
                    builder.dismiss()
                    setAction(item.id, "canceled");
                }
        };
    }


    fun onProcessAction(action: String, meta: String) {
        processAction(action, meta)
    }

    fun processAction(action: String, meta: String) {

        Log.d("====action====", action)
        Log.d("====meta====", meta)

        if (action == "treasure") {
            onTreasureClick(meta)
        }

        if (action == "program_dash") {
            goToNewDashboard(meta)
        }

        if (action == "discover") {
            onDiscoverClick()
        }
        if (action == "prescription") {
            onClickPrescription(meta)
        }
        if (action == "ordermedicine") {
            openOrderMedicine(meta)
        }
        if (action == "echannel") {
            onClickEChannel(meta)
        }
        if (action == "store_group") {
            onClickStoreGroupView(meta)
        }
        if (action == "call") {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$meta")
            startActivity(intent)
        }

        if (action == "process_payment") {
            onClickPaymentConfirm(meta)
        }
        if (action == "other_payments") {
            onClickOtherPayment(meta)
        }
        if (action == "paynow") {
            onPayNowClick(meta)
        }
        if (action == "addtobill") {
            onClickAddToBill(meta)
        }

        if (action == "filtered_videocall") {
            onVideoCallClick(meta)
        }
        if (action == "videocall") {
            onVideoCallClick("")
        }
        if (action == "goal") {
            onGoalClick()
        }
        if (action == "filtered_reportreview") {
            onReportReviewFiltered(meta)
        }
        if (action == "reports") {
            onReportsClick()
        }
        if (action == "help") {
            onHelpClick()
        }
        if (action == "leaderboard") {
            onLeaderboardClick(meta)
        }
        if (action == "openbadgenative") {
            onOpenBadgersClick()
        }

        if (action == "web") {
            onClickWeb(meta)
        }
        if (action == "common") {
            onClickCommonWebView(meta)
        }

        if (action == "commonpay") {
            onClickCommonWebView(meta)
        }

        if (action == "webapy") {
            onClickWeb(meta)
        }

        if (action == "paypin") {
            openPayPin()
        }

        if (action == "paysummary") {
            openPaymentSummary()
        }

        if (action == "commonview") {
            onClickCommonWebView(meta)
        }

        if (action == "data_challenge") {
            goToWalkAndWin(meta);
        }

        if (action == "native_post") {
            onClickNativeView(meta)
        }

        if (action == "native_post_json") {
            onJSONNativePostClick(meta)
        } else if (action == "post") {
            onPostClick(meta)
        } else if (action == "challenge") {
            onMapChallangeClick(meta)
        } else if (action == "chat") {
            onChatClick(meta)
        } else if (action == "filtered_chat") {
            onChatClickFilter(meta)
        } else if (action == "program_timeline") {
            onProgramNewDesignClick(meta)
        } else if (action == "programtimeline") {
            onProgramNewDesignClick(meta)
        } else if (action == "program") {
            onProgramPostClick(meta)
        } else if (action == "channeling") {
            onButtonChannelingClick(meta)
        } else if (action == "janashakthiwelcome") {
            onJanashakthiWelcomeClick(meta)
        } else if (action == "dynamicquestion") {
            onDyanamicQuestionClick(meta)
        } else if (action == "janashakthireports") {
            onJanashakthiReportsClick(meta)
        }


    }

    fun onChatClickFilter(meta: String) {
        // AskActivity.startActivity(this,meta)
        val intent = Intent(context, AskActivity::class.java)
        intent.putExtra("filtereKey", meta)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    fun onJSONNativePostClick(meta: String) {
        val intent = Intent(context, NativePostJSONActivity::class.java)
        intent.putExtra("meta", meta)
        startActivity(intent)
    }

    private fun goToWalkAndWin(meta: String) {
        val intent = Intent(context, WalkWinMainActivity::class.java)
        intent.putExtra("challenge_id", meta)
        startActivity(intent);
    }

    private fun openPaymentSummary() {
        val intent = Intent(context, PaymentSummaryViewActivity::class.java)
        startActivity(intent);
    }

    private fun openPayPin() {
        val intent = Intent(context, PaymentPinSubmitActivity::class.java)
        startActivity(intent);
    }

    fun onReportReviewFiltered(meta: String) {
        val intent = Intent(context, GetAReviewActivity::class.java)
        intent.putExtra("filtereKey", meta)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun onClickStoreGroupView(meta: String) {
        if (meta.isNotEmpty()) {
            GroupViewActivity.startActivity(context as Activity, meta)
        }
    }

    fun onClickEChannel(meta: String) {
        startActivity(Intent(context, VisitDoctorActivity::class.java))
    }

    fun onClickPrescription(meta: String) {
        startActivity(Intent(context, Medicine_ViewActivity::class.java))
    }

    fun openOrderMedicine(meta: String) {
//        startActivity(Intent(this, Medicine_ViewActivity::class.java))
//        startActivity(Intent(this, OrderMedicineMain::class.java))
        val i = Intent(requireContext(), OMMainPage::class.java)
        val jsonObject: JsonObject =
            Gson().toJsonTree(OMCommon(requireContext()).retrieveFromDraftSingleton()).asJsonObject;
        OMCommon(requireContext()).saveToCommonSingletonAndRetrieve(jsonObject);
        startActivity(i)
    }

    fun onDiscoverClick() {
//        val intent = Intent(context, LifePlusProgramActivity::class.java)
//        val intent = Intent(context, NewDiscoverActivity::class.java)
        val intent = SetDiscoverPage().getDiscoverIntent(requireContext());
        intent.putExtra("isFromSearchResults", false)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun goToNewDashboard(meta: String) {
        val intent = Intent(context, DiscoverToNewDashboard::class.java)
        intent.putExtra("dashboard_meta", meta)
        startActivity(intent);
    }

    open fun onTreasureClick(meta: String?) {
        val intent = Intent(context, TreasureViewActivity::class.java)
        intent.putExtra(EXTRA_TREASURE_KEY, meta)
        startActivity(intent)
    }

    fun onOpenBadgersClick() {
        val intent = Intent(context, Badges_Activity::class.java)
        startActivity(intent)
    }

    fun onLeaderboardClick(meta: String) {
        if (meta.length > 0) {
            val intent = Intent(context, NewLeaderBoardActivity::class.java)
            intent.putExtra("challange_id", meta)
            startActivity(intent)
        }
    }

    fun onPayNowClick(meta: String) {
        val intent = Intent(context, PaymentActivity::class.java)
        intent.putExtra("paymentmeta", meta)
        startActivity(intent)
    }

    fun onHelpClick() {
        val intent = Intent(context, HelpFeedbackActivity::class.java)
        startActivity(intent)
    }

    fun onReportsClick() {
        val intent = Intent(context, ReportDetailsActivity::class.java)
        intent.putExtra("data", "all")
        Ram.setReportsType("fromHome")
        startActivity(intent)
    }

    fun onGoalClick() {

        val prefManager = PrefManager(context)
        val status = prefManager.myGoalData["my_goal_status"]

        if (status === "Pending") {
            val intent = Intent(context, AchivedGoal_Activity::class.java)
            startActivity(intent)
        } else if (status === "Pick") {
            val intent = Intent(context, PickAGoal_Activity::class.java)
            startActivity(intent)
        } else if (status === "Completed") {
            val serviceObj = HomePage_Utility(context)
            serviceObj.showAlert_Deleted(
                context,
                "This goal has been achieved for the day. Please pick another goal tomorrow"
            )
        }
    }

    fun onProgramNewDesignClick(meta: String) {
        if (meta.length > 0) {
            val intent = Intent(context, ProgramActivity::class.java)
            intent.putExtra("meta", meta)
            startActivity(intent)
        }
    }

    fun onClickNativeView(meta: String) {
        val intent = Intent(context, NativePostActivity::class.java)
        intent.putExtra("meta", meta)
        startActivity(intent)
    }

    fun onPaymentProcessed(obj: PriceList) {
        // PaymentConfirmActivity.startActivity(context,obj.item_price_master_id,obj.text,obj.service_payment_frequency_source_id,obj.related_id,obj.payment_source_id,obj.payment_frequency)
    }

    fun onClickOtherPayment(meta: String) {
        //OtherPaymentActivity.startActivity(context,meta)
    }

    fun onClickAddToBill(meta: String) {
        // EnterMobileNumberActivityPayment.startActivity(context,meta)
    }

    fun onClickPaymentConfirm(meta: String) {
        // PaymentConfirmActivity.startActivity(this,meta)
    }

    fun onClickCommonWebView(meta: String) {
        val intent = Intent(context, CommonWebViewActivity::class.java)
        intent.putExtra("URL", meta)
        startActivity(intent)
    }

    fun onClickWeb(meta: String) {

        if (meta.isNotEmpty()) {
            Log.d("......meta......1...", meta)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(meta)
            startActivity(i)
        } else {
            Log.d("......meta......0...", meta)
        }
    }

    fun onChatClick(meta: String) {
        //  AyuboChatActivity.startActivity(context,meta, false)
    }

    fun onChatQuesClick(meta: String) {

        var pref: PrefManager? = PrefManager(context)
        pref?.relateID = meta
        pref?.setIsJanashakthiDyanamic(true)
        //  IntroActivity.startActivity(context)
    }

    fun onJanashakthiWelcomeClick(meta: String) {
        val pref = PrefManager(context)
        pref.relateID = meta
        pref.setIsJanashakthiWelcomee(true)
        val intent = Intent(context, JanashakthiWelcomeActivity::class.java)
        startActivity(intent)
    }

    fun onDyanamicQuestionClick(meta: String) {
        var pref: PrefManager? = PrefManager(context)
        pref?.relateID = meta
        pref?.setIsJanashakthiDyanamic(true)
        val intent = Intent(context, IntroActivity::class.java)
        startActivity(intent)
    }

    fun onJanashakthiReportsClick(meta: String) {
        MedicalUpdateActivity.startActivity(requireContext())
    }

    fun onOpenVideoClick(meta: String) {
        val intent = Intent(context, OpenPostActivity::class.java)
        intent.putExtra("postID", meta)
        startActivity(intent)
    }

    fun onOpenImageClick(meta: String) {
        val intent = Intent(context, OpenPostActivity::class.java)
        intent.putExtra("postID", meta)
        startActivity(intent)
    }

    fun onOpenNativePost(meta: String) {
        val intent = Intent(context, NativePostActivity::class.java)
        intent.putExtra("meta", meta)
        startActivity(intent)
    }

    fun onPostClick(meta: String) {
        val intent = Intent(context, OpenPostActivity::class.java)
        intent.putExtra("postID", meta)
        startActivity(intent)
    }

    private fun startDoctorsActivity(doctorID: String) {
        val parameters = DocSearchParameters()
        parameters.doctorId = doctorID
        parameters.locationId = ""
        parameters.specializationId = ""
        parameters.date = ""
        val pref = PrefManager(context)
        val user_id = pref.loginUser["uid"]
        parameters.user_id = user_id

        val intent = Intent(context, SearchActivity::class.java)
        intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, SelectDoctorAction(parameters))
        intent.putExtra(SearchActivity.EXTRA_TO_DATE, "")
        startActivity(intent)

    }

    fun onButtonChannelingClick(meta: String) {
        if (meta.length > 0) {
            startDoctorsActivity(meta)
        } else {
            val intent = Intent(context, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    fun onVideoCallClick(meta: String) {
        if (meta.length > 0) {
            val activity = "my_doctor"
            val intent = Intent(context, MyDoctorLocations_Activity::class.java)
            intent.putExtra("doctor_id", meta)
            intent.putExtra("activityName", activity)
            startActivity(intent)
        } else {
            val intent = Intent(context, MyDoctor_Activity::class.java)
            intent.putExtra("activityName", "myExperts")
            startActivity(intent)
        }
    }

    fun onMapChallangeClick(meta: String) {

        if (meta.length > 0) {
//            val serviceObj = MapChallangesServices(this, meta)
//            serviceObj.Service_getChallengeMapData_ServiceCall()

            val intent = Intent(context, MapChallengeActivity::class.java)
            intent.putExtra("challenge_id", meta)
            startActivity(intent)

        } else {
            val intent = Intent(context, NewCHallengeActivity::class.java)
            startActivity(intent)
        }
    }

    fun onShowExperts(experts: ArrayList<Experts>) {
        // ViewExpertsActivity.startActivity(context,experts)
//        val intent = Intent(this,ViewExpertsActivity::class.java)
//        intent.putExtra("program_experts", experts)
//        startActivity(intent)
    }

    fun onAskClick(activityName: String, meta: String) {
        val intent = Intent(context, AskQuestion_Activity::class.java)
        startActivity(intent)
    }

    fun onProgramPostClick(meta: String) {
        if (meta.length > 0) {
            val intent = Intent(context, SingleTimeline_Activity::class.java)
            intent.putExtra("related_by_id", meta)
            intent.putExtra("type", "program")
            startActivity(intent)
        }
    }


}


class DateDecoratorNew : DayViewDecorator {
    var color: Int = 0;
    lateinit var dates: HashSet<CalendarDay>;
    lateinit var drawable: ColorDrawable;
    lateinit var context: Context;

    constructor(color: Int, dates: HashSet<CalendarDay>, context: Context) {
        this.color = color
        this.dates = dates
        this.context = context
    }


    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day);
    }

    override fun decorate(view: DayViewFacade?) {

        view!!.addSpan(DotSpan(10F, color));
    }

}

class DateDecoratorNewForDay : DayViewDecorator {
    var color: Int = 0;
    lateinit var dates: HashSet<CalendarDay>;
    lateinit var drawable: ColorDrawable;
    lateinit var context: Context;

    constructor(color: Int, dates: HashSet<CalendarDay>, context: Context) {
        this.color = color
        this.dates = dates
        this.context = context
    }


    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day);
    }

    override fun decorate(view: DayViewFacade?) {

        view!!.addSpan(DotSpan(8F, color));
    }

}