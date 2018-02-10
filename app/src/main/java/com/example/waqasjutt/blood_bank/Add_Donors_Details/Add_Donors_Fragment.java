package com.example.waqasjutt.blood_bank.Add_Donors_Details;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.waqasjutt.blood_bank.MainActivity;
import com.example.waqasjutt.blood_bank.Paths;
import com.example.waqasjutt.blood_bank.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Add_Donors_Fragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    private ArrayAdapter<String> adapter;
    private ListView recyclerView;

    public Add_Donors_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.add_donors_fragment, container, false);

        //For Navigation Bar
        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((MainActivity) getActivity()).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        getActivity().setTitle("Blood Donors");

        recyclerView = (ListView) view.findViewById(R.id.recyclerView);
        checkInternet();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
                        checkInternet();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 4000);
            }
        });

        return view;
    }

    private void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Paths.DONORS_LIST_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                ArrayList<Blood_Items> blood_itemsArrayList = new ArrayList<>();
                                //converting the string to json array object
                                JSONArray array = new JSONArray(response);

                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject jsonObject = array.getJSONObject(i);

                                    Blood_Items blood_items = new Blood_Items();
                                    blood_items.setName(jsonObject.getString("name"));
                                    blood_items.setMobile(jsonObject.getString("mobile"));
                                    blood_items.setBlood_group(jsonObject.getString("blood_group"));
                                    blood_items.setCity(jsonObject.getString("city"));
                                    blood_items.setAddress(jsonObject.getString("address"));
                                    blood_itemsArrayList.add(blood_items);
                                }
                                Blood_Adapter adapter = new Blood_Adapter(getActivity(), R.layout.blood_item_details, blood_itemsArrayList);
                                recyclerView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_SHORT).show();
                        }
                    });

            //adding our stringrequest to queue
            Volley.newRequestQueue(getActivity()).add(stringRequest);
        } else {
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_LONG).show();
        }
    }
}