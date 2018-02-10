package com.example.waqasjutt.blood_bank;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.waqasjutt.blood_bank.Add_Donors_Details.Add_Donors_Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home_Fragment extends Fragment {

    private View view;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private GridView gridView;
    private String[] values = {
            "Blood Donors"
            , "Add Blood Request"
            , "Search by City"
            , "Blood Banks"
            , "Share App"
            , "Contact Us"
            , "About Us"};
    private int[] images = {
            R.drawable.blood
            , R.drawable.request
            , R.drawable.city
            , R.drawable.blood_bank
            , R.drawable.share
            , R.drawable.contact_us
            , R.drawable.about_us};
    private LinearLayout Home_Linearlaoyout;

    public Home_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.home_fragment, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();

        //For Navigation Bar
        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((MainActivity) getActivity()).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        getActivity().setTitle("Home");

//////////////////User Profile updation////////////////////////////////////////////////////////////////////////////////////////////////////////
        ((MainActivity) getActivity()).tv_name = (TextView) ((MainActivity) getActivity()).headerView.findViewById(R.id.tv_UserNameOfHeader);
        ((MainActivity) getActivity()).tv_mobile = (TextView) ((MainActivity) getActivity()).headerView.findViewById(R.id.tv_UserMobileOfHeader);
        ((MainActivity) getActivity()).tv_name.setText(SharedPrefManager.getInstance(getActivity()).getName());
        ((MainActivity) getActivity()).tv_mobile.setText(SharedPrefManager.getInstance(getActivity()).getMobile());
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        checkInternet();

//        Toast.makeText(getActivity(), "Name: " + SharedPrefManager.getInstance(getActivity()).getName()
//                + "\nMobile: " + SharedPrefManager.getInstance(getActivity()).getMobile()
//                + "\nAddress:  " + SharedPrefManager.getInstance(getActivity()).getAddress()
//                + "\nCity: " + SharedPrefManager.getInstance(getActivity()).getCity()
//                + "\nBlood: " + SharedPrefManager.getInstance(getActivity()).getBloodGroup()
//                + "\nDOB: " + SharedPrefManager.getInstance(getActivity()).getDOB(), Toast.LENGTH_LONG).show();

        gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(getActivity(), values, images));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        ((TextView) v.findViewById(R.id.grid_item_label))
                                .getText(), Toast.LENGTH_SHORT).show();

                if (position == 0) {
                    fragmentTransaction = fragmentManager
                            .beginTransaction()
                            .replace(R.id.container, new Add_Donors_Fragment());
                    fragmentTransaction
                            .addToBackStack(null)
                            .commit();
                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            ((TextView) v.findViewById(R.id.grid_item_label))
                                    .getText(), Toast.LENGTH_SHORT).show();
                } else if (position == 1) {
                    fragmentTransaction = fragmentManager
                            .beginTransaction()
                            .replace(R.id.container, new Add_Donors_Fragment());
                    fragmentTransaction
                            .addToBackStack(null)
                            .commit();
                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            ((TextView) v.findViewById(R.id.grid_item_label))
                                    .getText(), Toast.LENGTH_SHORT).show();
                } else if (position == 2) {
                    fragmentTransaction = fragmentManager
                            .beginTransaction()
                            .replace(R.id.container, new Add_Donors_Fragment());
                    fragmentTransaction
                            .addToBackStack(null)
                            .commit();
                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            ((TextView) v.findViewById(R.id.grid_item_label))
                                    .getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Paths.USER_PROFILE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                SharedPrefManager.getInstance(getActivity())
                                        .getUserData(
                                                obj.getString("id"),
                                                obj.getString("name"),
                                                obj.getString("city"),
                                                obj.getString("address"),
                                                obj.getString("blood_group"),
                                                obj.getString("dob")
                                        );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("mobile", SharedPrefManager.getInstance(getActivity()).getMobile());
                    return param;
                }
            };
            RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(getActivity(), "Internet connection is required to use this App", Toast.LENGTH_LONG).show();
        }
    }
}

class ImageAdapter extends BaseAdapter {
    private Context context;
    private final String[] strValues;
    private final int[] imageValue;

    public ImageAdapter(Context context, String[] strValues, int[] imageValue) {
        this.context = context;
        this.strValues = strValues;
        this.imageValue = imageValue;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;

        if (convertView == null) {

            view = new View(context);

            // get layout from mobile.xml
            view = inflater.inflate(R.layout.gridview_item, null);

            // set value into textview
            TextView textView = (TextView) view
                    .findViewById(R.id.grid_item_label);
            textView.setText(strValues[position]);

            // set image based on selected text
            ImageView imageView = (ImageView) view
                    .findViewById(R.id.grid_item_image);
            imageView.setImageResource(imageValue[position]);

        } else {
            view = (View) convertView;
        }

        return view;
    }

    @Override
    public int getCount() {
        return strValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}