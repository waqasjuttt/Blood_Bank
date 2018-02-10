package com.example.waqasjutt.blood_bank;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;


public class SharedPrefManager {
    private static SharedPrefManager sharedPrefManager;
    private static Context context;

    private static final String SHARED_PREF_NAME = "Blood_Bank";

    private static final String KEY_FULL_NAME = "FullName";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_DOB = "dob";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CITY = "city";
    private static final String KEY_BLOOD = "blood_group";
    private static final String KEY_USER_ID = "userid";

    SharedPrefManager(Context context) {
        SharedPrefManager.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (sharedPrefManager == null) {
            sharedPrefManager = new SharedPrefManager(context);
        }
        return sharedPrefManager;
    }

    public boolean getUserData(String id,
                               String name,
                               String city,
                               String address,
                               String bloodGroup,
                               String dob) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_FULL_NAME, name);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_BLOOD, bloodGroup);
        editor.putString(KEY_DOB, dob);
        editor.commit();
        return true;
    }

    public boolean getUserID(String id,
                             String mobile, String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_FULL_NAME, name);
        editor.commit();
        return true;
    }

    public boolean Logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_MOBILE, null) != null) {
            return true;
        }
        return false;
    }

    public String getMobile() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_MOBILE, null);
    }

    public String getID() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public String getName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FULL_NAME, null);
    }

    public String getDOB() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DOB, null);
    }

    public String getBloodGroup() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_BLOOD, null);
    }

    public String getCity() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CITY, null);
    }

    public String getAddress() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ADDRESS, null);
    }
}