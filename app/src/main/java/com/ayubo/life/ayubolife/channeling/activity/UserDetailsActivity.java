package com.ayubo.life.ayubolife.channeling.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.channeling.config.Constants;
import com.ayubo.life.ayubolife.channeling.model.Appointment;
import com.ayubo.life.ayubolife.channeling.model.User;
import com.ayubo.life.ayubolife.channeling.util.AppHandler;
import com.ayubo.life.ayubolife.channeling.view.DetailSessionView;
import com.google.gson.Gson;

public class UserDetailsActivity extends AppCompatActivity {

    //constants
    public static final String EXTRA_APPOINTMENT_OBJECT = "appointment_object";
    public static final String EXTRA_DOCTOR_DETAILS = "doctor_details";
    PrefManager pref;
    //instances
    private User user;
    private Appointment appointment;

    android.app.AlertDialog dialogView;
//    TextView edtDOB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        pref = new PrefManager(UserDetailsActivity.this);


        final ViewHolder holder = new ViewHolder();

        readData(getIntent(), holder);

        Spinner spinner = (Spinner) findViewById(R.id.edt_title_user);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(UserDetailsActivity.this,
                R.array.title_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());


        findViewById(R.id.txt_next_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (holder.validate()) {
                    if (appointment == null)
                        appointment = new Appointment();
                    appointment.setUser(user);
                    Intent intent = new Intent(UserDetailsActivity.this, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_DETAIL_ACTION_OBJECT, new DetailSessionView(appointment));
                    startActivity(intent);
                }


            }
        });

        setUserDate(holder);

        LinearLayout btn_backImgBtn_layout = (LinearLayout) findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton btn_back_Button = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

//    public void showAlert_Add(Context c) {
//
//        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
//        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layoutView = inflater.inflate(R.layout.alert_date_picker, null, false);
//
//        builder.setView(layoutView);
//        // alert_ask_go_back_to_map
//        dialogView = builder.create();
//        dialogView.setCancelable(false);
//        final DatePicker picker = (DatePicker) layoutView.findViewById(R.id.datePicker1);
//
//
//        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_no);
//        btn_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogView.cancel();
//
//            }
//        });
//        TextView btn_yes = (TextView) layoutView.findViewById(R.id.btn_yes);
//        btn_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialogView.cancel();
//                System.out.println("Selected Date: " + picker.getDayOfMonth() + "/" + (picker.getMonth() + 1) + "/" + picker.getYear());
//                // String DateStr= picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear();
//                String DateStr = picker.getYear() + "/" + (picker.getMonth() + 1) + "/" + picker.getDayOfMonth();
//                Date date1 = null;
//                try {
//                    date1 = new SimpleDateFormat("yyyy/MM/dd").parse(DateStr);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                user.setDob(date1);
//                edtDOB.setText(DateStr);
//
//            }
//        });
//        dialogView.show();
//    }


    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            user.setTitle(parent.getItemAtPosition(pos).toString());

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }


    private void readData(Intent intent, ViewHolder holder) {
        if (intent.getExtras() != null)
            appointment = (Appointment) intent.getExtras().getSerializable(UserDetailsActivity.EXTRA_APPOINTMENT_OBJECT);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE);
        user = new Gson().fromJson(sharedPreferences.getString(Constants.PREFERENCE_USER_OBJECT, ""), User.class);
        if (user != null)
            holder.setUser(user);
        else
            user = new User();


    }

    void setUserDate(ViewHolder holder) {
        String fullName = pref.getLoginUser().get("name");

        holder.setUserDateToView(fullName, pref.getLoginUser().get("mobile"), pref.getLoginUser().get("email"));
    }


    class ViewHolder {
        EditText edtFName, edtLName, edtPhone;

//        edtEmail, edtIdentity;
        // LazyDatePicker lazyDatePicker;

        ViewHolder() {
            //  edtTitle = findViewById(R.id.edt_title_user);
            edtFName = findViewById(R.id.edt_name_user);
            edtLName = findViewById(R.id.edt_l_name_user);
            edtPhone = findViewById(R.id.edt_phone_user);
//            edtEmail = findViewById(R.id.edt_email_user);
//            edtIdentity = findViewById(R.id.edt_identification_user);
//             edtDOB = findViewById(R.id.edt_dob_user);
            //    lazyDatePicker = findViewById(R.id.edt_dob_user);


            //   lazyDatePicker.setDateFormat(LazyDatePicker.DateFormat.DD_MM_YYYY);
            //  lazyDatePicker.setMinDate(minDate);
            //  lazyDatePicker.setMaxDate(maxDate);

//            lazyDatePicker.setOnDatePickListener(new LazyDatePicker.OnDatePickListener() {
//                @Override
//                public void onDatePick(Date dateSelected) {
//
//                    user.setDob(dateSelected);
//
//                }
//            });
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
//                edtDOB.setShowSoftInputOnFocus(false);
//            } else { // API 11-20
//                edtDOB.setTextIsSelectable(true);
//            }

//            edtDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View view, boolean b) {
//                    if (b) {
//                        hideKeyboard();
//                        showDateSelector();
//                    }
//                }
//            });
//
//            edtDOB.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    showAlert_Add(UserDetailsActivity.this);
////                    hideKeyboard();
////                    showDateSelector();
//                }
//            });
        }

//        private void showDateSelector() {
//            DatePickerFragment newFragment = new DatePickerFragment();
//            newFragment.show(getSupportFragmentManager(), getString(R.string.appointment_date));
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 20);
//            newFragment.setDefaultDate(calendar);
//            newFragment.setOnDateSelectedListener(new DatePickerFragment.OnDateSelected() {
//                @Override
//                public void onSelected(Calendar calendar) {
////                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
//                    user.setDob(calendar.getTime());
//                    // edtDOB.setText(TimeFormatter.millisecondsToString(calendar.getTimeInMillis(), TimeFormatter.DATE_FORMAT_DOB));
//                }
//            });
//        }

//        private void hideKeyboard() {
//            View view = UserDetailsActivity.this.getCurrentFocus();
//            if (view != null) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null)
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//            }
//        }

        void setUserDateToView(String name, String mob, String email) {

            String fullName = pref.getLoginUser().get("name");
            String[] splited = fullName.split("\\s+");

            String fn = splited[0];
            String ln = splited[1];

            edtFName.setText(fn);
            edtLName.setText(ln);
            edtPhone.setText(pref.getLoginUser().get("mobile"));
//            edtEmail.setText(pref.getLoginUser().get("email"));
            //   edtDOB.setText(pref.getLoginUser().get(""));

        }


        boolean validate() {
            boolean isValid = true;

            if (edtFName.getText().toString().equals("")) {
                isValid = false;
                edtFName.setError(getString(R.string.enter_name));
            } else {
                user.setfName(edtFName.getText().toString());
                edtFName.setError(null);
            }

            if (edtLName.getText().toString().equals("")) {
                isValid = false;
                edtLName.setError(getString(R.string.enter_name));
            } else {
                user.setlName(edtLName.getText().toString());
                edtLName.setError(null);
            }

            if (edtPhone.getText().toString().equals("")) {
                isValid = false;
                edtPhone.setError(getString(R.string.enter_phone));
            }
            if (AppHandler.isNotValidatePhone(edtPhone.getText().toString())) {
                isValid = false;
                edtPhone.setError(getString(R.string.invalid_phone));
            } else {

                String sPhone = edtPhone.getText().toString();
                if (sPhone.length() == 9) {
                    sPhone = "0" + sPhone;
                }
                user.setPhone(sPhone);
                edtPhone.setError(null);
            }

//            if (!edtEmail.getText().toString().equals("") && AppHandler.isNotValidateEmail(edtEmail.getText().toString())) {
//                isValid = false;
//                edtEmail.setError(getString(R.string.invalid_email));
//            } else {
//                user.setEmail(edtEmail.getText().toString());
//                edtEmail.setError(null);
//            }


            //  if (com.flavors.changes.Constants.type == com.flavors.changes.Constants.Type.HEMAS) {

//            String strIdenty = edtIdentity.getText().toString();
//
//            if ((strIdenty != null) && (strIdenty.length() > 5)) {
//
//
//                //  edtIdentity.setError(getString(R.string.enter_identification));
//
//
//                if (edtIdentity.getText().length() > 5) {
//                    if (AppHandler.isNotValidateNIC(edtIdentity.getText().toString())) {
//                        edtIdentity.setError(getString(R.string.enter_valid_identification));
//                        isValid = false;
//                    } else {
//                        user.setIdentification(edtIdentity.getText().toString());
//                        edtIdentity.setError(null);
//                        isValid = true;
//                    }
//                }
//
//
//                if (user.getDob() == null) {
//                    String birthYear = null;
//                    if (edtIdentity.getText().toString() != null) {
//                        birthYear = AppHandler.getBirthYearFromID(edtIdentity.getText().toString());
//                    }
//                    birthYear = AppHandler.getBirthYearFromID(edtIdentity.getText().toString());
//                    if (birthYear != null) {
//                        Date date1 = null;
//                        try {
//                            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(birthYear);
//                            // lazyDatePicker.setDate(date1);
//                            user.setDob(date1);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//
//                        isValid = false;
//                    }
//                }
//
//
//            } else {
//                // isValid = false;
//            }

            if (user.getIdentification() == null) {
                user.setIdentification("000000000v");
            }

            if (isValid) {
                getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE).edit().putString(Constants.
                        PREFERENCE_USER_OBJECT, new Gson().toJson(user)).apply();
            }

            return isValid;
        }

        private void setUser(User user) {
            //edtTitle.setText(user.getTitle());
            edtFName.setText(user.getfName());
            edtLName.setText(user.getlName());
            edtPhone.setText(user.getPhone());
//            edtEmail.setText(user.getEmail());
//            if (user.getIdentification().equals("000000000v")) {
//                edtIdentity.setText("");
//            } else {
//                edtIdentity.setText(user.getIdentification());
//            }

            //   edtDOB.setText(TimeFormatter.millisecondsToString(user.getDob().getTime(), TimeFormatter.DATE_FORMAT_DOB));
        }
    }

}
