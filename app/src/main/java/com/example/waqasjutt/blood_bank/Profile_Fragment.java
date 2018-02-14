package com.example.waqasjutt.blood_bank;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sdsmdg.tastytoast.TastyToast;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Profile_Fragment extends Fragment implements View.OnClickListener {

    //For DOB
    private DatePickerFragment datePickerFragment;
    private static Calendar dateTime = Calendar.getInstance();
    protected static int mYear;
    protected static int mMonth;
    protected static int mDay;
    private static final int allowedDOB = 18;
    protected static Button btnDOB;
    protected static TextView tv_DOB;
    protected static String strDt;
////////////////////////////////////////////////////////////

    private ProgressDialog progressDialog;
    private View view;
    private static String strHeaderName;
    private static String strCity;
    private static String strBlood;
    private static EditText et_name, et_address, et_mobile;
    private Button btnSave, btnResetValue;
    private String[] str_City = {"Lahore", "Multan", "Karachi", "Islamabad", "Sialkot"};
    private String[] str_Blood_Group = {"A+", "A-", "AB+", "AB-", "B+", "B-", "O+", "O-"};
    private ArrayAdapter<String> CityAdapter, BloodAdapter;
    private static BetterSpinner CitySpinner;
    private static BetterSpinner BloodSpinner;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    public Profile_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.profile_fragment, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        progressDialog = new ProgressDialog(getActivity());

        //For Navigation Bar
        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((MainActivity) getActivity()).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        initComponents();
        setListners();

        et_mobile.setText(SharedPrefManager.getInstance(getActivity()).getMobile());
        et_mobile.setEnabled(false);
        if (SharedPrefManager.getInstance(getActivity()).getName().contains("New User")) {
            et_name.setText("");
        } else {
            et_name.setText(SharedPrefManager.getInstance(getActivity()).getName());
        }
        et_address.setText(SharedPrefManager.getInstance(getActivity()).getAddress());
        CitySpinner.setText(SharedPrefManager.getInstance(getActivity()).getCity());
        BloodSpinner.setText(SharedPrefManager.getInstance(getActivity()).getBloodGroup());
        tv_DOB.setText(SharedPrefManager.getInstance(getActivity()).getDOB());

        getActivity().setTitle("Profile");

        return view;
    }

    private void setListners() {
        btnSave.setOnClickListener(this);
        btnDOB.setOnClickListener(this);
        btnResetValue.setOnClickListener(this);
    }

    private void initComponents() {
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnDOB = (Button) view.findViewById(R.id.btn_DOB);
        tv_DOB = (TextView) view.findViewById(R.id.tv_DOB);
        et_name = (EditText) view.findViewById(R.id.et_Name);
        et_mobile = (EditText) view.findViewById(R.id.et_Phone);
        et_address = (EditText) view.findViewById(R.id.et_Address);
        btnResetValue = (Button) view.findViewById(R.id.btnResetValue);

        CitySpinner = (BetterSpinner) view.findViewById(R.id.City);
        CityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, str_City);
        CitySpinner.setAdapter(CityAdapter);

        BloodSpinner = (BetterSpinner) view.findViewById(R.id.BloodGroup);
        BloodAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, str_Blood_Group);
        BloodSpinner.setAdapter(BloodAdapter);

        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        et_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnResetValue:
                tv_DOB.setText("Pick your date of birth.");
                CitySpinner.setText("");
                BloodSpinner.setText("");
                et_name.setText("");
                et_address.setText("");
                break;

            case R.id.btnSave:
                CheckValidation();
                break;

            case R.id.btn_DOB:
                datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "Select Your Birthday");
                break;
        }
    }

    private void CheckValidation() {
        if (et_name.getText().toString().isEmpty()) {
            et_name.setText("New User");
        }

        strCity = CitySpinner.getText().toString();
        strBlood = BloodSpinner.getText().toString();
        strDt = tv_DOB.getText().toString();

        if (tv_DOB.getText().toString().contains("Your are not allowed to donate blood because you are")) {
            strDt = "Your are not allowed to donate blood because you are not 18 years old.";
            strCity = "";
            strBlood = "";
            et_name.setText("");
            et_address.setText("");
        } else if (!BloodSpinner.getText().toString().isEmpty()
                && (tv_DOB.getText().toString().contains("Pick your date of birth")
                || tv_DOB.getText().toString().contains("Your are not allowed to donate blood because you are"))) {
            et_name.setText("");
            Toast.makeText(getActivity(), "Select your date of birth.", Toast.LENGTH_SHORT).show();
        } else if (!BloodSpinner.getText().toString().isEmpty()
                && (!tv_DOB.getText().toString().contains("Pick your date of birth")
                || !tv_DOB.getText().toString().contains("Your are not allowed to donate blood because you are"))
                && !CitySpinner.getText().toString().isEmpty()
                && !et_name.getText().toString().isEmpty()) {
            checkInternet();
        } else if (BloodSpinner.getText().toString().isEmpty()
                && tv_DOB.getText().toString().contains("Pick your date of birth")
                && CitySpinner.getText().toString().isEmpty()
                && (et_name.getText().toString().isEmpty()
                || et_name.getText().toString().contains("New User"))
                && et_address.getText().toString().isEmpty()) {
            checkInternet();
        } else if (!BloodSpinner.getText().toString().isEmpty()
                && (!tv_DOB.getText().toString().contains("Pick your date of birth")
                || !tv_DOB.getText().toString().contains("Your are not allowed to donate blood because you are"))
                && (et_name.getText().toString().isEmpty()
                || et_name.getText().toString().contains("New User"))) {
            et_name.setText("");
            Toast.makeText(getActivity(), "All fields are require.", Toast.LENGTH_SHORT).show();
        } else if (!BloodSpinner.getText().toString().isEmpty()
                && (!tv_DOB.getText().toString().contains("Pick your date of birth")
                || !tv_DOB.getText().toString().contains("Your are not allowed to donate blood because you are"))
                && (!et_name.getText().toString().isEmpty()
                || !et_name.getText().toString().contains("New User"))
                && CitySpinner.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "All fields are require.", Toast.LENGTH_SHORT).show();
        } else if (!et_name.getText().toString().isEmpty()
                || !et_name.getText().toString().contains("New User")) {
            checkInternet();
        }
    }

    public void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            progressDialog.setMessage("Updating profile...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Paths.EDIT_PROFILE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("error") == "false") {
                                    TastyToast.makeText(getActivity(), jsonObject.getString("message")
                                            , Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
                                    SharedPrefManager.getInstance(getActivity()).getUserData(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("mobile"),
                                            jsonObject.getString("city"),
                                            jsonObject.getString("address"),
                                            jsonObject.getString("blood_group"),
                                            jsonObject.getString("dob")
                                    );
                                } else if (jsonObject.getString("error") == "true") {
                                    TastyToast.makeText(getActivity(), jsonObject.getString("message")
                                            , Toast.LENGTH_LONG, TastyToast.ERROR).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ((MainActivity) getActivity()).navigationView.getMenu().getItem(0).setChecked(true);
                            ((MainActivity) getActivity()).onNavigationItemSelected(((MainActivity) getActivity()).navigationView.getMenu().getItem(0));
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.container, new Home_Fragment(), Utils.Home_Fragment).commit();
                            fragmentManager.popBackStack(null
                                    , FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (getActivity() != null) {
                                Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", et_name.getText().toString());
                    params.put("mobile", SharedPrefManager.getInstance(getActivity()).getMobile());
                    params.put("blood_group", strBlood);
                    params.put("city", strCity);
                    params.put("address", et_address.getText().toString());
                    params.put("dob", strDt);
                    return params;
                }
            };
            RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    // For Date of Birth
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Using current date as start Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Get DatePicker Dialog
            return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            dateTime.set(mYear, monthOfYear, dayOfMonth);
            long selectDateInMilliSeconds = dateTime.getTimeInMillis();

            Calendar currentDate = Calendar.getInstance();
            long currentDateInMilliSeconds = currentDate.getTimeInMillis();

            SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            strDt = simpleDate.format(dateTime.getTime());

            if (selectDateInMilliSeconds > currentDateInMilliSeconds) {
                Toast.makeText(getActivity(), "Your birthday date must come before taday's date", Toast.LENGTH_LONG).show();
                return;
            }

            long diffDate = currentDateInMilliSeconds - selectDateInMilliSeconds;
            Calendar yourAge = Calendar.getInstance();
            yourAge.setTimeInMillis(diffDate);

            long returnedYear = yourAge.get(Calendar.YEAR) - 1970;

            if (returnedYear < allowedDOB) {
                Toast.makeText(getActivity(), "Sorry!!! You are not allowed.", Toast.LENGTH_LONG).show();
                if (returnedYear <= 0) {
                    tv_DOB.setText("Your are not allowed to donate blood because you are not 18 years old.");
                } else {
                    tv_DOB.setText("Your are not allowed to donate blood because you are " + returnedYear + " years old.");
                }
                strDt = "";
                CitySpinner.setText("");
                BloodSpinner.setText("");
                et_name.setText("");
                et_address.setText("");
                return;
            } else {
                // move to another activity page
//                Toast.makeText(getActivity(), "You are allowed to use this app \nYour Age is: " + returnedYear, Toast.LENGTH_LONG).show();
                tv_DOB.setText("Date of birth " + strDt);
            }
        }
    }
}