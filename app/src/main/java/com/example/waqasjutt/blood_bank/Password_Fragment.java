package com.example.waqasjutt.blood_bank;


import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class Password_Fragment extends Fragment implements View.OnClickListener {

    private Button btnPassword;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.et_ConfirmPassword)
    EditText et_ConfirmPassword;
    private View view;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private String strMobile;

    public Password_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.password_fragment, container, false);

        ButterKnife.bind(getActivity());

        fragmentManager = getActivity().getSupportFragmentManager();

        //For Go Back to previous fragment
        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((MainActivity) getActivity()).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        initComponents();
        setListners();

        return view;
    }

    private void setListners() {
        btnPassword.setOnClickListener(this);
        sharedPreferences = getActivity().getSharedPreferences("Mobile_Number", Context.MODE_APPEND);
        strMobile = sharedPreferences.getString("mobile", null);
    }

    private void initComponents() {
        btnPassword = (Button) view.findViewById(R.id.btnPassword);
        et_password = (EditText) view.findViewById(R.id.et_password);
        et_ConfirmPassword = (EditText) view.findViewById(R.id.et_ConfirmPassword);
        progressDialog = new ProgressDialog(getActivity());

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        et_ConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
            case R.id.btnPassword:
                CheckValidation();
                break;
        }
    }

    private void CheckValidation() {
        // Check if password should be greater than 3
        if (et_password.getText().toString().isEmpty()) {
            et_password.setError("Enter your password.");
        } else if (et_password.getText().toString().length() <= 4) {
            et_password.setError("Password length should be greater than 4.");
        } else {
            et_password.setError(null);
        }

        // Check if both password should be equal
        if (!et_ConfirmPassword.getText().toString().equals(et_password.getText().toString())) {
            et_ConfirmPassword.setError("Both paassword does not match");
        } else {
            et_ConfirmPassword.setError(null);
            checkInternet();
        }
    }

    public void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            // Else do signup or do your stuff
            if ((!et_password.getText().toString().isEmpty()
                    && et_password.getText().toString().length() >= 4) &&
                    (!et_ConfirmPassword.getText().toString().isEmpty()
                            && et_ConfirmPassword.getText().toString().equals(et_password.getText().toString())
                            && et_ConfirmPassword.getText().toString().length() >= 4)) {
//            TastyToast.makeText(getActivity(), "SignUp Successful.",
//                    TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                progressDialog.setMessage("Registering user...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Paths.SIGNUP_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("error") == "false") {
                                        TastyToast.makeText(getActivity(), jsonObject.getString("message")
                                                , Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
                                        SharedPrefManager.getInstance(getActivity())
                                                .getUserID(
                                                        jsonObject.getString("id"),
                                                        jsonObject.getString("mobile"),
                                                        jsonObject.getString("name")
                                                );
                                        fragmentTransaction = fragmentManager
                                                .beginTransaction()
                                                .replace(R.id.container, new Home_Fragment()
                                                        , Utils.Home_Fragment);
                                        fragmentTransaction.addToBackStack(null)
                                                .commit();
                                        sharedPreferences = getActivity().getSharedPreferences("Mobile_Number", Context.MODE_APPEND);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.apply();
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
                        params.put("mobile", strMobile);
                        params.put("name", "New User");
                        params.put("password", et_password.getText().toString());
                        return params;
                    }
                };
                RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
            }
        } else {
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_LONG).show();
        }
    }
}