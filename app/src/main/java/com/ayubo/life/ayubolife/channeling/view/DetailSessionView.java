package com.ayubo.life.ayubolife.channeling.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.channeling.activity.ResultActivity;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.webrtc.App;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.activity.DetailActivity;
import com.ayubo.life.ayubolife.channeling.activity.PayActivity;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.channeling.model.Appointment;
import com.ayubo.life.ayubolife.channeling.model.Booking;
import com.ayubo.life.ayubolife.channeling.model.DetailRow;
import com.ayubo.life.ayubolife.channeling.model.DownloadDataBuilder;
import com.ayubo.life.ayubolife.channeling.model.Session;
import com.ayubo.life.ayubolife.channeling.model.SessionParent;
import com.ayubo.life.ayubolife.channeling.model.SoapBasicParams;
import com.ayubo.life.ayubolife.channeling.model.User;
import com.ayubo.life.ayubolife.channeling.util.AppHandler;
import com.ayubo.life.ayubolife.channeling.util.DownloadManager;
import com.ayubo.life.ayubolife.channeling.util.TimeFormatter;

public class DetailSessionView implements DetailActivity.DetailAction, Serializable {

    //constants
    private static final String TITLE_TYPE = "Type";
    private static final String TITLE_DATE = "Date";
    private static final String TITLE_TIME = "Time";
    private static final String TITLE_LOCATION = "Location";
    private static final String TITLE_APPOINTMENT = "Appointment Number";
    private static final String TITLE_DOCTOR_FEE = "Doctor Fee";
    private static final String TITLE_HOSPITAL_FEE = "Hospital Fee";
    private static final String TITLE_PLATFORM_FEE = "Platform Fee";
    private static final String TITLE_VAT_FEE = "VAT";
    private static final String TITLE_BOOKING_FEE = "Booking Fee";

    //instances
    private Appointment appointment;
    private Booking booking;
    private Context context;
    public DetailSessionView(Appointment appointment) {
        this.appointment = appointment;
    }

    @Override
    public String getDocName() {
        return appointment.getSessionParent().getDoctor_name();
    }

    @Override
    public String getSpecialisation() {
        return appointment.getSessionParent().getSpecialization_name();
    }

    @Override
    public Double getTotalPrice() {
        return booking.getPrice().getTotal() * 1.0;
    }

    @Override
    public DownloadDataBuilder getAddDownloadBuilder() {
        return new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_ADD_APPOINTMENT, new BookingParams(appointment)
                        .getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT);
    }

    @Override
    public DownloadDataBuilder getRemoveDownloadBuilder(Activity activity) {
        return new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_REMOVE_BOOKING, new
                        RemoveParams(activity)
                        .getSearchParams())).setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT);
    }

    @Override
    public boolean readData(Activity activity, String response) {
        Log.d(DetailActivity.class.getSimpleName(), "Server Respond = " + response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            booking = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), Booking.class);

            ((App) activity.getApplication()).setBooking(booking);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return booking != null;
    }

    @Override
    public void onFinish(Activity activity) {
        if (booking != null) {

           if(booking.getPrice().getTotal()==0){
               Intent intent = new Intent(activity, ResultActivity.class);
               intent.putExtra(PayActivity.EXTRA_BOOKING_OBJECT, booking);
               activity.startActivity(intent);
           }else{
               Intent intent = new Intent(activity, PayActivity.class);
               intent.putExtra(PayActivity.EXTRA_BOOKING_OBJECT, booking);
               activity.startActivity(intent);
           }

        } else
            Toast.makeText(activity, "Please wait until retrieve Data...", Toast.LENGTH_LONG).show();
    }

    @Override
    public String getDoctorNote() {
        return appointment.getSessionParent().getSpecial_notes();
    }

    @Override
    public boolean hasData(Activity activity) {
        return ((App) activity.getApplication()).getBooking() != null;
    }

    @Override
    public List<Object> getPreList() {
        List<Object> objects = new ArrayList<>();

        objects.add(new DetailRow(TITLE_TYPE, appointment.getSource(), R.drawable.channeling));
        Date sessionDate = TimeFormatter.stringToDate(appointment.getSession().getShow_date(), TimeFormatter.DATE_FORMAT_VIDEO);;
        objects.add(new DetailRow(TITLE_DATE, TimeFormatter.millisecondsToString(sessionDate.getTime(),
                TimeFormatter.TIME_FORMAT_APPOINTMENT_DATE), R.drawable.date));
        objects.add(new DetailRow(TITLE_TIME, appointment.getSession().getTime(), R.drawable.time));
        objects.add(new DetailRow(TITLE_LOCATION, appointment.getSessionParent().getHospital_name(), R.drawable.hospital));
        objects.add(new DetailRow(TITLE_APPOINTMENT,  String.valueOf(appointment.getSession().getNext_appointment_no()), R.drawable.chnelling_appointment));

        return objects;
    }

    @Override
    public List<Object> getPostList() {
        List<Object> objects = new ArrayList<>();

        objects.add(new DetailRow(TITLE_DOCTOR_FEE, String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW,
                booking.getPrice().getDoctor_fee()), R.drawable.doctor_fee));
        objects.add(new DetailRow(TITLE_HOSPITAL_FEE, String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW,
                booking.getPrice().getHospital_fee()), R.drawable.hospital_fee));
        objects.add(new DetailRow(TITLE_PLATFORM_FEE, String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW,
                booking.getPrice().getPlatform_fee()), R.drawable.platform_fee));
        objects.add(new DetailRow(TITLE_BOOKING_FEE, String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW,
                booking.getPrice().getBooking_charge()), R.drawable.dc_booking_fee_icon));
        objects.add(new DetailRow(TITLE_VAT_FEE, String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW,
                booking.getPrice().getVat()), R.drawable.vat));

        return objects;
    }

    class BookingParams extends SoapBasicParams {
        Patient patient;
        ParamSession session;

        BookingParams(Appointment appointment) {

            patient = new Patient(appointment.getUser());
            session = new ParamSession(appointment.getSessionParent(), appointment.getSession(),
                    appointment.getSource());
            Log.d("BookingParams","================================");
        }

        public String getSearchParams() {

            Context c=null;
            c=  Ram.getMycontext();
            if(c!=null){

                PrefManager pref = new PrefManager(c);
                user_id = pref.getLoginUser().get("uid");
                Log.d("getSearchParams","=================================Context not null");
            }else{
                Log.d("getSearchParams","==============================Context  null");
            }


            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("b", user_id);
//                jsonObject.put("patient", patient.getMap().toString().replaceAll("=", "\"=\""));
//                jsonObject.put("session", session.getMap().toString().replaceAll("=", "\"=\""));
                jsonObject.put("c", new JSONObject(new Gson().toJson(patient)));
                jsonObject.put("d", new JSONObject(new Gson().toJson(session)));
                jsonObject.put("a", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            return jsonObject.toString().replace("\\", "");
            return jsonObject.toString();
        }
    }

    class RemoveParams extends SoapBasicParams {
        String trId;

        RemoveParams(Activity activity) {

            this.trId = String.valueOf(((App) activity.getApplication()).getBooking().getAppointment().getRef());
        }

        public String getSearchParams() {

            Context c=null;
            c=  Ram.getMycontext();
            if(c!=null){

                PrefManager pref = new PrefManager(c);
                user_id = pref.getLoginUser().get("uid");
                Log.d("getSearchParams","=================================Context not null");
            }else{
                Log.d("getSearchParams","==============================Context  null");
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", user_id);
                jsonObject.put("trId", trId);
                jsonObject.put("token_key", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }

    class Patient {
        private String member;
        private String needSMS;
        private String nsr;
        private String foreign;
        private String teleNo;
        private String title;
        private String patientName;
        private String nid;
        private String passport;
        private String email;
        private String clientRefNumber;

        Patient(User user) {
            patientName = String.format("%s %s", user.getfName(), user.getlName());
            teleNo = user.getPhone();
            if (AppHandler.isNotValidateNIC(user.getIdentification()))
                passport = user.getIdentification();
            else
                nid = user.getIdentification();
            title = "Mr";
            email = user.getEmail();
            clientRefNumber = "";
            member = "";
            needSMS = "";
            nsr = "";
            foreign = "";
        }

//        JSONArray getMap() {
//            JSONArray patientArray = new JSONArray();
//            HashMap<String, String> map = new HashMap<>();
//            map.put("patientName", patientName);
//            map.put("teleNo", teleNo);
//            map.put("passport", passport);
//            map.put("nid", nid);
//            map.put("title", title);
//            map.put("email", email);
//            map.put("clientRefNumber", clientRefNumber);
//            map.put("member", member);
//            map.put("needSMS", needSMS);
//            map.put("nsr", nsr);
//            map.put("foreign", foreign);
//
//            for (Object o : map.entrySet()) {
//                patientArray.put(o);
//            }
//
//            return patientArray;
//        }
    }

    class ParamSession {
        private String id;
        private String hosId;
        private String docId;
        private String theDay;
        private String startTime;
        private String theDate;
        private String from;

        ParamSession(SessionParent parent, Session session, String source) {

            Session.Info[] lis=session.getBooking_info();
            for (Session.Info s: lis) {

              if(s.getFrom().equals(source))  {
                  id=  s.getId();
              }
                //Do your stuff here

            }
            System.out.println("========================="+id);
           // id = session.getBooking_info()[0].getId();
            hosId = String.valueOf(parent.getHospital_id());
            docId = String.valueOf(parent.getDoctor_code());
            theDay = session.getDay();
            startTime = session.getTime();
            theDate = session.getShow_date();
            from = source;
        }

//        JSONArray getMap() {
//            JSONArray sessionArray = new JSONArray();
//            HashMap<String, String> map = new HashMap<>();
//            map.put("id", id);
//            map.put("hosId", hosId);
//            map.put("docId", docId);
//            map.put("theDay", theDay);
//            map.put("startTime", startTime);
//            map.put("theDate", theDate);
//            map.put("from", from);
//
//            for (Object o : map.entrySet()) {
//                sessionArray.put(o);
//            }
//
//            return sessionArray;
//        }
    }
}
