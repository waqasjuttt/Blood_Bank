package com.example.waqasjutt.blood_bank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Contact_Us_Fragment extends Fragment implements View.OnClickListener {

    private View view;
    @Bind(R.id.ed_Name)
    EditText ed_Name;
    @Bind(R.id.ed_Subject)
    EditText ed_Subject;
    @Bind(R.id.ed_Message)
    EditText ed_Message;
    private Button btnSend;

    public Contact_Us_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.contact_us_fragment, container, false);
        getActivity().setTitle("Contact Us");

        ButterKnife.bind(getActivity());
        //For disable nav drawer
        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((MainActivity) getActivity()).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        initComponents();
        setListner();

        return view;
    }

    private void initComponents() {
        ed_Name = (EditText) view.findViewById(R.id.ed_Name);
        ed_Subject = (EditText) view.findViewById(R.id.ed_Subject);
        ed_Message = (EditText) view.findViewById(R.id.ed_Message);
        btnSend = (Button) view.findViewById(R.id.btnSend);
    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void setListner() {
        btnSend.setOnClickListener(this);
        ed_Message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        ed_Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        ed_Subject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
            case R.id.btnSend:
                CheckValidation();
                break;
        }
    }

    private void CheckValidation() {
        String getName = ed_Name.getText().toString();
        String getEmail = "contact@BloodBankPakistan.com";
        String getSubject = ed_Subject.getText().toString();
        String getMessage = ed_Message.getText().toString();

        // Check for both field is empty or not
        if ((getName.isEmpty() || getName.equals("") || getName.length() == 0)
                && (getSubject.isEmpty() || getSubject.equals("") || getSubject.length() == 0)
                && (getMessage.isEmpty() || getMessage.equals("") || getMessage.length() == 0)) {
            TastyToast.makeText(getActivity(), "Try again, Required fields are missing.",
                    TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
        }

        // Check Name
        if (getName.isEmpty()) {
            ed_Name.setError("Enter your name.");
        } else if (getName.length() >= 20) {
            ed_Name.setError("Only 20 characters allowed.");
        } else {
            ed_Name.setError(null);
        }

        // Check Subject
        if (getSubject.isEmpty()) {
            ed_Subject.setError("Enter Subject.");
        } else if (getSubject.length() >= 40) {
            ed_Subject.setError("Only 40 characters allowed.");
        } else {
            ed_Subject.setError(null);
        }

        // Check Message
        if (getMessage.isEmpty()) {
            ed_Message.setError("Write your message.");
        } else {
            ed_Message.setError(null);
        }

        //Send message
        if ((!getName.isEmpty() && getName.length() <= 20)
                && (!getSubject.isEmpty() && getSubject.length() <= 40)
                && (!getMessage.isEmpty())) {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.setType("plain/text");

            email.putExtra(Intent.EXTRA_EMAIL, new String[]{getEmail});
            email.putExtra(Intent.EXTRA_SUBJECT, getSubject);
            email.putExtra(Intent.EXTRA_TEXT, getName + ":\n             " + getMessage);

            //This will show prompts of email intent
            startActivity(Intent.createChooser(email, "Select Your Default Email"));
        }
    }
}