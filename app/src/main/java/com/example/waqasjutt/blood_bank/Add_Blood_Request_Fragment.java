package com.example.waqasjutt.blood_bank;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Add_Blood_Request_Fragment extends Fragment implements View.OnClickListener {

    private ProgressDialog progressDialog;
    private View view;
    private static String strCity;
    private static String strBlood;
    private static EditText et_name, et_hospital, et_mobile;
    @Bind(R.id.et_Blood_Request)
    EditText et_blood_bags;
    private Button btnSaveBloodBags, btnResetValue;
    private String[] str_City = {"Lahore", "Multan", "Karachi", "Islamabad", "Sialkot"};
    private String[] str_Blood_Group = {"A+", "A-", "AB+", "AB-", "B+", "B-", "O+", "O-"};
    private ArrayAdapter<String> CityAdapter, BloodAdapter;
    private static BetterSpinner CitySpinner;
    private static BetterSpinner BloodSpinner;
    private FragmentManager fragmentManager;

    public Add_Blood_Request_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.add_blood_request_fragment, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        progressDialog = new ProgressDialog(getActivity());
        ButterKnife.bind(getActivity());

        //For Navigation Bar
        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((MainActivity) getActivity()).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        getActivity().setTitle("Add Blood Request");

        initComponents();
        setListners();

        et_mobile.setText(SharedPrefManager.getInstance(getActivity()).getMobile());
        et_mobile.setEnabled(false);
        et_name.setText(SharedPrefManager.getInstance(getActivity()).getName());
        et_name.setEnabled(false);
        et_hospital.setText(SharedPrefManager.getInstance(getActivity()).getHospital());
        et_blood_bags.setText(SharedPrefManager.getInstance(getActivity()).getBloodBags());
        CitySpinner.setText(SharedPrefManager.getInstance(getActivity()).getCityRequest());
        BloodSpinner.setText(SharedPrefManager.getInstance(getActivity()).getBloodTypeRequest());

//        Toast.makeText(getActivity(), "Name: " + SharedPrefManager.getInstance(getActivity()).getName()
//                        + "\nMobile: " + SharedPrefManager.getInstance(getActivity()).getMobile()
//                        + "\nHospital: " + SharedPrefManager.getInstance(getActivity()).getHospital()
//                        + "\nCity: " + SharedPrefManager.getInstance(getActivity()).getCityRequest()
//                        + "\nBlood Bags: " + SharedPrefManager.getInstance(getActivity()).getBloodBags()
//                        + "\nBlood Type: " + SharedPrefManager.getInstance(getActivity()).getBloodTypeRequest()
//                , Toast.LENGTH_LONG).show();

        return view;
    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void setListners() {
        btnSaveBloodBags.setOnClickListener(this);
        btnResetValue.setOnClickListener(this);
    }

    private void initComponents() {
        btnSaveBloodBags = (Button) view.findViewById(R.id.btnSaveBloodBags);
        et_name = (EditText) view.findViewById(R.id.et_Name);
        et_mobile = (EditText) view.findViewById(R.id.et_Phone);
        et_hospital = (EditText) view.findViewById(R.id.et_Hospital_Name);
        et_blood_bags = (EditText) view.findViewById(R.id.et_Blood_Request);
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
        et_hospital.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

//        et_blood_bags.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    hideKeyboard(v);
//                }
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnResetValue:
                CitySpinner.setText("");
                BloodSpinner.setText("");
                et_blood_bags.setText("");
                et_hospital.setText("");
                break;

            case R.id.btnSaveBloodBags:
                CheckFields();
                break;
        }
    }

    private void CheckFields() {
        if (et_blood_bags.getText().toString().isEmpty()
                && BloodSpinner.getText().toString().isEmpty()
                && CitySpinner.getText().toString().isEmpty()
                && et_hospital.getText().toString().isEmpty()) {
            CitySpinner.setText("");
            BloodSpinner.setText("");
            et_blood_bags.setText("");
            et_hospital.setText("");
            checkInternet();
        } else if (BloodSpinner.getText().toString().isEmpty()
                || CitySpinner.getText().toString().isEmpty()
                || et_hospital.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "All fields are require.", Toast.LENGTH_SHORT).show();
        } else if (!et_blood_bags.getText().toString().isEmpty()) {
            CheckValidation();
        }
    }

    private void CheckValidation() {
        int intBloodBags = Integer.parseInt(et_blood_bags.getText().toString());

        if (intBloodBags > 10) {
            et_blood_bags.setError("You can add max 10 blood bags.");
        } else if (et_hospital.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Enter hospital name.", Toast.LENGTH_SHORT).show();
        } else if (CitySpinner.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Select your city.", Toast.LENGTH_SHORT).show();
        } else if (BloodSpinner.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Select blood type.", Toast.LENGTH_SHORT).show();
        } else if (intBloodBags < 11
                && !BloodSpinner.getText().toString().isEmpty()
                && !CitySpinner.getText().toString().isEmpty()
                && !et_hospital.getText().toString().isEmpty()) {
            checkInternet();
        } else {
            et_blood_bags.setError(null);
        }
    }

    private void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            strCity = CitySpinner.getText().toString();
            strBlood = BloodSpinner.getText().toString();

//        Toast.makeText(getActivity(), "Name: " + et_name.getText().toString()
//                + "\nMobile: " + et_mobile.getText().toString()
//                + "\nAddress: " + et_address.getText().toString()
//                + "\nCity: " + strCity
//                + "\nBlood: " + strBlood
//                + "\nDOB: " + strDt, Toast.LENGTH_LONG).show();
            progressDialog.setMessage("Updating request...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Paths.BLOOD_BAGS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("error") == "false") {
                                    TastyToast.makeText(getActivity(), obj.getString("message")
                                            , Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
                                    SharedPrefManager.getInstance(getActivity()).getUserBloodRequest(
                                            obj.getString("mobile"),
                                            obj.getString("blood_group"),
                                            obj.getString("blood_bottle"),
                                            obj.getString("city"),
                                            obj.getString("hospital")
                                    );
                                } else if (obj.getString("error") == "true") {
                                    TastyToast.makeText(getActivity(), obj.getString("message")
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
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("mobile", SharedPrefManager.getInstance(getActivity()).getMobile());
                    params.put("blood_group", strBlood);
                    params.put("blood_bottle", et_blood_bags.getText().toString());
                    params.put("city", strCity);
                    params.put("hospital", et_hospital.getText().toString());
                    return params;
                }
            };
            Volley.newRequestQueue(getActivity()).add(stringRequest);
        } else {
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
        }
    }
}