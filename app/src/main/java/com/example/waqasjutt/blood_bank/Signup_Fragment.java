package com.example.waqasjutt.blood_bank;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Signup_Fragment extends Fragment implements View.OnClickListener {

    private Dialog MyDialog;
    @Bind(R.id.et_Phone)
    EditText et_mobile;
    private TextView tv_Disclaimer_Detail, tv_AlreadyMember;
    private Button btnAgree, btnNotAgree, btnDisclaimer, btnSubmit;
    private ImageView btnClose;
    private View view;
    private ProgressDialog progressDialog;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private int countMobileNumber = 1;

    public Signup_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.signup_fragment, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();

        //For Navigation Bar
        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((MainActivity) getActivity()).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        getActivity().setTitle("Sign Up");
        progressDialog = new ProgressDialog(getActivity());
        initComponents();
        setListners();

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
        btnSubmit.setOnClickListener(this);
        btnDisclaimer.setOnClickListener(this);
        tv_AlreadyMember.setOnClickListener(this);
    }

    private void initComponents() {
        ButterKnife.bind(getActivity());

        btnSubmit = (Button) view.findViewById(R.id.btnSignUp);
        tv_AlreadyMember = (TextView) view.findViewById(R.id.tv_Already_Member);
        et_mobile = (EditText) view.findViewById(R.id.et_Phone);
        btnDisclaimer = (Button) view.findViewById(R.id.btn_Disclaimer);

        et_mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Disclaimer:
                MyCustomAlertDialog();
                break;
            case R.id.btnSignUp:
                CheckValidation();
                break;
            case R.id.tv_Already_Member:
                fragmentManager.popBackStackImmediate();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, new Login_Fragment(), Utils.Login_Fragment)
                        .commit();
                break;
        }
    }

    private void CheckValidation() {

        // Check if Mobile Number is valid or not
        if (et_mobile.getText().toString().isEmpty()) {
            et_mobile.setError("Enter Mobile Number.");
        } else if (et_mobile.getText().toString().contains(" ")) {
            et_mobile.setError("Space are not allowed.");
        } else if (!et_mobile.getText().toString().startsWith("03")) {
            et_mobile.setError("Enter a valid mobile number start with 03.");
        } else if (et_mobile.getText().toString().length() <= 10 || et_mobile.getText().toString().length() >= 12) {
            if ((countMobileNumber > 3) &&
                    (et_mobile.getText().toString().length() <= 10) || et_mobile.getText().toString().length() >= 12) {
                et_mobile.setError("Enter 11 digits phone number.");
            } else {
                et_mobile.setError("Enter valid phone number.");
            }
            countMobileNumber++;
        } else {
            et_mobile.setError(null);
            checkInternet();
        }
    }

    public void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (!et_mobile.getText().toString().isEmpty() &&
                    !et_mobile.getText().toString().contains(" ") &&
                    et_mobile.getText().toString().length() == 11 &&
                    et_mobile.getText().toString().startsWith("03")) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Mobile_Number", Context.MODE_APPEND);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("mobile", et_mobile.getText().toString()).commit();

                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Paths.MOBILE_EXISTS_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("error") == "false") {
                                        fragmentTransaction = fragmentManager
                                                .beginTransaction()
                                                .replace(R.id.container, new VerifyAccount_Fragment());
                                        fragmentTransaction.addToBackStack(null).commit();
                                    } else if (jsonObject.getString("error") == "true") {
                                        TastyToast.makeText(getActivity(), jsonObject.getString("message")
                                                , Toast.LENGTH_LONG, TastyToast.ERROR).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.hide();
                                if (getActivity() != null) {
                                    Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("mobile", et_mobile.getText().toString());
                        return params;
                    }
                };
                RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
            }
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void MyCustomAlertDialog() {
        MyDialog = new Dialog(getActivity());
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.custom_dialog);
        MyDialog.setCanceledOnTouchOutside(false);

        btnAgree = (Button) MyDialog.findViewById(R.id.btn_Agree);
        btnNotAgree = (Button) MyDialog.findViewById(R.id.btn_Not_Agree);
        btnClose = (ImageView) MyDialog.findViewById(R.id.btnClose);
        tv_Disclaimer_Detail = (TextView) MyDialog.findViewById(R.id.tv_Disclaimer);

        tv_Disclaimer_Detail.setText("*This Application is owned by Blood Bank Pakistan.\n" +
                "*We have no legal obligation related to any information or database in this application.\n" +
                "*Entire data and information is publicly exposed and developer/owner of this application is not responsible for any loss/damages.");
        btnAgree.setEnabled(true);
        btnNotAgree.setEnabled(true);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialog.dismiss();
            }
        });

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Welcome to Blood Bank", Toast.LENGTH_LONG).show();
                MyDialog.dismiss();
            }
        });
        btnNotAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Your can't use Blood Bank App", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });

        MyDialog.show();
    }
}