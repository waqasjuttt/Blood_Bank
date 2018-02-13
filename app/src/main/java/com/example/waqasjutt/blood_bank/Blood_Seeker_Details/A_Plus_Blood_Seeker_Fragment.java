package com.example.waqasjutt.blood_bank.Blood_Seeker_Details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.waqasjutt.blood_bank.R;

public class A_Plus_Blood_Seeker_Fragment extends Fragment {

    private View view;

    public A_Plus_Blood_Seeker_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.add_donors_fragment, container, false);
        getActivity().setTitle("Blood Seeker");
        return view;
    }
}