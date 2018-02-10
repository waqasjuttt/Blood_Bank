package com.example.waqasjutt.blood_bank;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Forget_Password_Fragment extends Fragment implements View.OnClickListener {

    private View view;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    @Bind(R.id.et_Mobile_Search)
    EditText et_Mobile_Search;
    private Button btnMobile;
    private ProgressDialog progressDialog;

    public Forget_Password_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.forget_password_fragment, container, false);
        getActivity().setTitle("Forget Password");
        fragmentManager = getActivity().getSupportFragmentManager();
        ButterKnife.bind(getActivity());
        progressDialog = new ProgressDialog(getActivity());

        //For disable nav drawer
        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((MainActivity) getActivity()).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        et_Mobile_Search = (EditText) view.findViewById(R.id.et_Mobile_Search);
        btnMobile = (Button) view.findViewById(R.id.btnMobileSearch);

        btnMobile.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMobileSearch:
                CheckValidation();
                break;
        }
    }

    private void CheckValidation() {
        // Check if email id is valid or not
        if (et_Mobile_Search.getText().toString().trim().equals("")) {
            et_Mobile_Search.setError("Enter your Mobile Number.");
        } else if (et_Mobile_Search.getText().toString().contains(" ")) {
            et_Mobile_Search.setError("Space are not allowed.");
        } else if (!et_Mobile_Search.getText().toString().startsWith("03")) {
            et_Mobile_Search.setError("Mobile number should be start 03.");
        } else if (et_Mobile_Search.getText().toString().length() != 11) {
            et_Mobile_Search.setError("Length should be 11 digits.");
        } else if (!et_Mobile_Search.getText().toString().trim().equals("")
                && !et_Mobile_Search.getText().toString().contains(" ")
                && et_Mobile_Search.getText().toString().startsWith("03")
                && et_Mobile_Search.getText().toString().length() == 11) {
            checkInternet();
        } else {
            et_Mobile_Search.setError(null);
        }
    }

    public void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            StringRequest stringRequest = new StringRequest(
                    com.android.volley.Request.Method.POST,
                    Paths.MOBILE_EXISTS_URL,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("error") == "true") {
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Mobile_Number", Context.MODE_APPEND);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("mobile", et_Mobile_Search.getText().toString()).commit();
                                    Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                                    fragmentTransaction =
//                                            fragmentManager
//                                                    .beginTransaction()
//                                                    .replace(R.id.container,
//                                                            new ResetPassword_Fragment());
//                                    fragmentTransaction
//                                            .addToBackStack(null)
//                                            .commit();

                                    fragmentTransaction =
                                            fragmentManager
                                                    .beginTransaction()
                                                    .replace(R.id.container,
                                                            new Verification_Fragment_For_ForgetPassword());
                                    fragmentTransaction
                                            .addToBackStack(null)
                                            .commit();
                                } else if (obj.getString("error") == "false") {
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
                    new com.android.volley.Response.ErrorListener() {
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
                    param.put("mobile", et_Mobile_Search.getText().toString());
                    return param;
                }
            };
            RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_LONG).show();
        }
    }
}