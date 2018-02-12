package com.example.waqasjutt.blood_bank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.waqasjutt.blood_bank.Add_Donors_Details.A_Plus_Blood_Fragment;

public class Blood_Types extends Fragment {

    private View view;
    private TextView tv_Ap, tv_An;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    public Blood_Types() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.blood_types, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();

        //For Navigation Bar
        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((MainActivity) getActivity()).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        getActivity().setTitle("Blood Types");

        initComponents();
        setListners();

        return view;
    }

    private void setListners() {
        tv_Ap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, new A_Plus_Blood_Fragment());
                fragmentTransaction.addToBackStack(null).commit();
            }
        });
    }

    private void initComponents() {
        tv_Ap = (TextView) view.findViewById(R.id.tv_Ap);
        tv_An = (TextView) view.findViewById(R.id.tv_An);
    }
}
