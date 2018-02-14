package com.example.waqasjutt.blood_bank.Add_Donors_Details;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
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

public class A_Plus_Blood_Fragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    private Blood_Donors_Adapter blood_adapter;
    private ListView listView;
    private EditText et_search;

    public A_Plus_Blood_Fragment() {
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

        listView = (ListView) view.findViewById(R.id.listView);
        et_search = (EditText) view.findViewById(R.id.et_Search_City);
        et_search.setVisibility(View.GONE);

        listView.setTextFilterEnabled(true);

        checkInternet();
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                blood_adapter.getFilter().filter(charSequence);
                blood_adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (listView == null || listView.getChildCount() == 0) ?
                                0 : listView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        return view;
    }

    private void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            et_search.setEnabled(false);
            et_search.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Paths.A_PLUS_DONORS_LIST_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                listView.setVisibility(View.VISIBLE);
                                et_search.setEnabled(true);
                                et_search.setVisibility(View.VISIBLE);
                                ArrayList<Blood_Donors_Items> blood_itemsArrayList = new ArrayList<>();
                                //converting the string to json array object
                                JSONArray array = new JSONArray(response);

                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject jsonObject = array.getJSONObject(i);

                                    Blood_Donors_Items blood_items = new Blood_Donors_Items();
                                    blood_items.setName(jsonObject.getString("name"));
                                    blood_items.setMobile(jsonObject.getString("mobile"));
                                    blood_items.setBlood_group(jsonObject.getString("blood_group"));
                                    blood_items.setCity(jsonObject.getString("city"));
                                    blood_items.setAddress(jsonObject.getString("address"));
                                    blood_itemsArrayList.add(blood_items);
                                }
                                blood_adapter = new Blood_Donors_Adapter(getActivity(), R.layout.blood_item_details, blood_itemsArrayList);
                                listView.setAdapter(blood_adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (getActivity() != null) {
                                listView.setVisibility(View.GONE);
                                et_search.setEnabled(false);
                                et_search.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            //adding our stringrequest to queue
            Volley.newRequestQueue(getActivity()).add(stringRequest);
        } else {
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_LONG).show();
            et_search.setEnabled(false);
            et_search.setVisibility(View.GONE);
        }
    }
}