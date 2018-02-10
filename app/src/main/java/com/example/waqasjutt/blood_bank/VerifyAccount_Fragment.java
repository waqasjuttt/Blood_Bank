package com.example.waqasjutt.blood_bank;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VerifyAccount_Fragment extends Fragment implements View.OnClickListener {

    private View view;
    private Dialog dialog;
    private TextView tv_dialog;
    private Button btn_dialog_ok;
    private SharedPreferences sharedPreferences;
    private String strMobile, strVerificationId;
    @Bind(R.id.et_Code)
    EditText et_Code;
    private Button btnCode, btnResend;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressDialog progressDialog;

    public VerifyAccount_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.verify_account_fragment, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        ButterKnife.bind(getActivity());
        progressDialog = new ProgressDialog(getActivity());

        sharedPreferences = getActivity().getSharedPreferences("Mobile_Number", Context.MODE_APPEND);
        strMobile = sharedPreferences.getString("mobile", null);

        //For Go Back to previous fragment
        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((MainActivity)getActivity()).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        getActivity().setTitle("Verify Account");
        checkInternet();

        MyDialog();

        initComponents();
        setListners();

        return view;
    }

    private void setListners() {
        btnCode.setOnClickListener(this);
        btnResend.setOnClickListener(this);
    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void initComponents() {
        btnCode = (Button) view.findViewById(R.id.btnCode);
        btnResend = (Button) view.findViewById(R.id.btn_Resend_Code);
        et_Code = (EditText) view.findViewById(R.id.et_Code);

        et_Code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };
    }

    public void MyDialog() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custome_dialog_for_resend_code);
        dialog.setCanceledOnTouchOutside(false);

        tv_dialog = (TextView) dialog.findViewById(R.id.tv_codeinfo);
        btn_dialog_ok = (Button) dialog.findViewById(R.id.btn_ok);

        tv_dialog.setText("Please wait, you will receive message or you can use Resend button after 20 seconds");
        btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCode:
                CheckValidation();
                break;

            case R.id.btn_Resend_Code:
                checkInternetForResendButton();
                break;
        }
    }

    private void CheckValidation() {
        String getCode = et_Code.getText().toString();

        if (getCode.isEmpty()) {
            et_Code.setError("Enter verification code");
        } else if (!getCode.isEmpty()) {
            signInWithCredential(PhoneAuthProvider.getCredential(strVerificationId, getCode));
            progressDialog.dismiss();
        } else {
            et_Code.setError(null);
        }
    }

    public void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {

        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            fragmentTransaction = fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.container, new Password_Fragment()
                                            , Utils.Password_Fragment);
                            fragmentTransaction
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            Toast.makeText(getActivity(), "Failed: " + task.getException().getMessage()
                                    , Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    strMobile, 60, TimeUnit.SECONDS, getActivity(), new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {
                            Toast.makeText(getActivity(), "OnVerification Failed" +
                                    e.getMessage(), Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            strVerificationId = s;
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Code Sent on this number " + strMobile, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeAutoRetrievalTimeOut(String s) {
                             super.onCodeAutoRetrievalTimeOut(s);
                            Toast.makeText(getActivity(), "On Code AutoRetrival Time out "
                                    , Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
            );
        } else {
            Toast.makeText(getActivity(), "Internet connection is required to use this App", Toast.LENGTH_LONG).show();
        }
    }

    public void checkInternetForResendButton() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    strMobile, 60, TimeUnit.SECONDS, getActivity(), new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {
                            Toast.makeText(getActivity(), "OnVerification Failed" +
                                    e.getMessage(), Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            strVerificationId = s;
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Code Resend", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeAutoRetrievalTimeOut(String s) {
                            // super.onCodeAutoRetrievalTimeOut(s);
                            Toast.makeText(getActivity(), "On Code AutoRetrival Time out "
                                    , Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
            );
        } else {
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_LONG).show();
        }
    }
}