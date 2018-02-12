package com.example.waqasjutt.blood_bank;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home_Fragment extends Fragment {

    private Dialog MyDialog;
    private TextView tv_Disclaimer_Detail, tv_main_heading;
    private Button btnAgree, btnNotAgree;
    private ImageView btnClose;

    private View view;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private GridView gridView;
    private String[] values = {
            "Blood Donors"
            , "Add Blood Request"
            , "Blood Seeker"
            , "Blood Compatibility"
            , "Blood Banks"
            , "Share App"
            , "Contact Us"
            , "About Us"};
    private int[] images = {
            R.drawable.blood
            , R.drawable.request
            , R.drawable.blood_seeker
            , R.drawable.info
            , R.drawable.blood_bank
            , R.drawable.share
            , R.drawable.contact_us
            , R.drawable.about_us};
    private LinearLayout Home_Linearlaoyout;
    private SwipeRefreshLayout swipeRefreshLayout;

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
                            .replace(R.id.container, new Blood_Types_For_Donors());
                    fragmentTransaction.addToBackStack(null).commit();
//                    Toast.makeText(
//                            getActivity().getApplicationContext(),
//                            ((TextView) v.findViewById(R.id.grid_item_label))
//                                    .getText(), Toast.LENGTH_SHORT).show();
                } else if (position == 1) {
                    fragmentTransaction = fragmentManager
                            .beginTransaction()
                            .replace(R.id.container, new Add_Blood_Request_Fragment());
                    fragmentTransaction
                            .addToBackStack(null)
                            .commit();
                } else if (position == 2) {
                    fragmentTransaction = fragmentManager
                            .beginTransaction()
                            .replace(R.id.container, new Blood_Types_For_Seeker_Fragment());
                    fragmentTransaction
                            .addToBackStack(null)
                            .commit();
                } else if (position == 3) {
                    fragmentTransaction = fragmentManager
                            .beginTransaction()
                            .replace(R.id.container, new Blood_Types_Info());
                    fragmentTransaction
                            .addToBackStack(null)
                            .commit();
                } else if (position == 4) {
                    fragmentTransaction = fragmentManager
                            .beginTransaction()
                            .replace(R.id.container, new Blood_Banks());
                    fragmentTransaction
                            .addToBackStack(null)
                            .commit();
                } else if (position == 5) {
                    String shareBody = "https://play.google.com/store/apps/details?id=************************";
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "APP NAME (Open it in Google Play Store to Download the Application)");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                } else if (position == 6) {
                    fragmentTransaction = fragmentManager
                            .beginTransaction()
                            .replace(R.id.container, new Contact_Us_Fragment());
                    fragmentTransaction
                            .addToBackStack(null)
                            .commit();
                } else if (position == 7) {
                    MyCustomAlertDialog();
                }
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

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (gridView == null || gridView.getChildCount() == 0) ?
                                0 : gridView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
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
                                                obj.getString("mobile"),
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
                            if (getActivity() != null) {
                                Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_SHORT).show();
                            }
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

    public void MyCustomAlertDialog() {
        MyDialog = new Dialog(getActivity());
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.custom_dialog);

        btnAgree = (Button) MyDialog.findViewById(R.id.btn_Agree);
        btnNotAgree = (Button) MyDialog.findViewById(R.id.btn_Not_Agree);
        btnClose = (ImageView) MyDialog.findViewById(R.id.btnClose);
        tv_Disclaimer_Detail = (TextView) MyDialog.findViewById(R.id.tv_Disclaimer);
        tv_main_heading = (TextView) MyDialog.findViewById(R.id.tv_main_heading);
        tv_main_heading.setVisibility(View.GONE);

        tv_Disclaimer_Detail.setText("*This Application is owned by Blood Bank Pakistan.\n" +
                "*We have no legal obligation related to any information or database in this application.\n" +
                "*Entire data and information is publicly exposed and developer/owner of this application is not responsible for any loss/damages.");
        btnAgree.setEnabled(true);
        btnAgree.setText("OK");

        btnNotAgree.setEnabled(false);
        btnNotAgree.setVisibility(View.GONE);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialog.dismiss();
            }
        });

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
            }
        });

        MyDialog.show();
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