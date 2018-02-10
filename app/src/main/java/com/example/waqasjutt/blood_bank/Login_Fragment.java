package com.example.waqasjutt.blood_bank;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class Login_Fragment extends Fragment implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private EditText et_phone, et_password;
    private View view;
    private TextView tv_CreateAccount, tv_ForgetPassword;
    private Button btnLogin;
    private int countMobileNumber = 1;
    private ProgressDialog progressDialog;

    public Login_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.login_fragment, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();

        checkInternet();

        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            //////////////////User Profile updation////////////////////////////////////////////////////////////////////////////////////////////////////////
            ((MainActivity) getActivity()).tv_name = (TextView) ((MainActivity) getActivity()).headerView.findViewById(R.id.tv_UserNameOfHeader);
            ((MainActivity) getActivity()).tv_mobile = (TextView) ((MainActivity) getActivity()).headerView.findViewById(R.id.tv_UserMobileOfHeader);
            ((MainActivity) getActivity()).tv_name.setText(SharedPrefManager.getInstance(getActivity()).getName());
            ((MainActivity) getActivity()).tv_mobile.setText(SharedPrefManager.getInstance(getActivity()).getMobile());
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            new MainActivity().finishAffinity();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Home_Fragment(),
                            Utils.Home_Fragment).commit();
        }

        //For Go Back to previous fragment
        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((MainActivity) getActivity()).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        getActivity().setTitle("Login");

        initComponents();
        setListners();

        return view;
    }

    private void setListners() {
        tv_CreateAccount.setOnClickListener(this);
        tv_ForgetPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        progressDialog = new ProgressDialog(getActivity());
    }

    private void initComponents() {
        et_phone = (EditText) view.findViewById(R.id.et_Phone);
        et_password = (EditText) view.findViewById(R.id.et_password);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        tv_ForgetPassword = (TextView) view.findViewById(R.id.tv_ForgetPassword);
        tv_CreateAccount = (TextView) view.findViewById(R.id.tv_CreateAccount);

        et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
            case R.id.tv_CreateAccount:
                fragmentTransaction = fragmentManager
                        .beginTransaction()
                        .replace(R.id.container,
                                new Signup_Fragment(),
                                Utils.Signup_Fragment);
                fragmentTransaction
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.tv_ForgetPassword:
                fragmentTransaction = fragmentManager
                        .beginTransaction()
                        .replace(R.id.container,
                                new Forget_Password_Fragment());
                fragmentTransaction
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.btnLogin:
                CheckValidation();
                break;
        }
    }

    private void CheckValidation() {
        String getMobileNumber = et_phone.getText().toString();
        String getPassword = et_password.getText().toString();

        // Check if Mobile Number is valid or not
        if (getMobileNumber.isEmpty()) {
            et_phone.setError("Enter Mobile Number.");
        } else if (getMobileNumber.contains(" ")) {
            et_phone.setError("Space are not allowed.");
        } else if (!getMobileNumber.startsWith("03")) {
            et_phone.setError("Enter a valid mobile number start with 03.");
        } else if (getMobileNumber.length() <= 10 || getMobileNumber.length() >= 12) {
            if ((countMobileNumber > 3) &&
                    (getMobileNumber.length() <= 10) || getMobileNumber.length() >= 12) {
                et_phone.setError("Enter 11 digits phone number.");
            } else {
                et_phone.setError("Enter valid phone number.");
            }
            countMobileNumber++;
        } else {
            et_phone.setError(null);
        }

        // Check if password length
        if (getPassword.isEmpty()) {
            et_password.setError("Enter your password");
        } else if (getPassword.length() <= 4) {
            et_password.setError("Password length should be greater than 4.");
        } else {
            et_password.setError(null);
        }

        // Check if email id or password is valid
        if (!getMobileNumber.isEmpty() &&
                getMobileNumber.length() == 11 &&
                !getMobileNumber.contains(" ") &&
                getMobileNumber.startsWith("03") &&
                !getPassword.isEmpty() &&
                getPassword.length() > 4) {
            isNetworkAvailable();
        }
    }

    private void isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Paths.LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("error") == "false") {
                                    SharedPrefManager.getInstance(getActivity())
                                            .getUserID(
                                                    obj.getString("id"),
                                                    obj.getString("mobile"),
                                                    obj.getString("name")
                                            );

                                    TastyToast.makeText(getActivity(), "Login Successful.",
                                            TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                    fragmentManager
                                            .beginTransaction()
                                            .replace(R.id.container, new Home_Fragment(),
                                                    Utils.Home_Fragment).commit();
                                    new MainActivity().finish();
                                } else if (obj.getString("error") == "true") {
                                    TastyToast.makeText(getActivity(),
                                            obj.getString("message"),
                                            Toast.LENGTH_SHORT,
                                            TastyToast.ERROR).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),
                                    "Server is not responding...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("mobile", et_phone.getText().toString());
                    param.put("password", et_password.getText().toString());
                    return param;
                }
            };
            RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(getActivity(), "Internet Connection Is Required", Toast.LENGTH_LONG).show();
        }
    }

    private void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
        } else {
            Toast.makeText(getActivity(), "Internet connection is required to use this App", Toast.LENGTH_LONG).show();
        }
    }
}