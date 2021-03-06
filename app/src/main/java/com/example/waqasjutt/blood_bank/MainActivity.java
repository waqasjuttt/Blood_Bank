package com.example.waqasjutt.blood_bank;

import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;
    private long back_pressed;
    public View view, headerView;
    public NavigationView navigationView = null;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle mToggle;
    public TextView tv_name, tv_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //How to change elements in the header programatically
        headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Login_Fragment(),
                            Utils.Login_Fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {

        CustomDialog_Class customDialog_class = new CustomDialog_Class(this);

//        i = (i + 1);
//        Toast.makeText(getApplicationContext(),
//                " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
//        if (i > 1) {
//            finishAffinity();
//        }

        // Find the tag of fragments
        Fragment Home_Fragment = fragmentManager
                .findFragmentByTag(Utils.Home_Fragment);
        Fragment Login_Fragment = fragmentManager
                .findFragmentByTag(Utils.Login_Fragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (Home_Fragment != null) {
            fragmentManager.popBackStack(null
                    , FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else if (Login_Fragment != null) {
            customDialog_class.show();
            customDialog_class.setCanceledOnTouchOutside(false);
        } else if (Home_Fragment != null) {
            customDialog_class.show();
            customDialog_class.setCanceledOnTouchOutside(false);
        }
//        else if (back_pressed + 1000 > System.currentTimeMillis()) {
//            super.onBackPressed();
//        }
        else {
            customDialog_class.show();
            customDialog_class.setCanceledOnTouchOutside(false);
//            Toast.makeText(getBaseContext(),
//                    "Press once again to exit!", Toast.LENGTH_SHORT)
//                    .show();
        }
//        back_pressed = System.currentTimeMillis();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //noinspection SimplifiableIfStatement
        int id = item.getItemId();
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
//        else if (id == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        resetAllMenuItemsTextColor(navigationView);
        int id = item.getItemId();

        if (id == R.id.home) {
//            Toast.makeText(MainActivity.this, "HOME", Toast.LENGTH_SHORT).show();
            setTextColorForMenuItem(item, R.color.colorAccent);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container
                            , new Home_Fragment())
                    .commit();
            fragmentManager.popBackStack(null
                    , FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (id == R.id.profile) {
//            Toast.makeText(MainActivity.this, "User Profile", Toast.LENGTH_SHORT).show();
            setTextColorForMenuItem(item, R.color.colorAccent);
            fragmentTransaction = fragmentManager
                    .beginTransaction()
                    .replace(R.id.container
                            , new Profile_Fragment());
            fragmentTransaction
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.search_blood) {
//            Toast.makeText(MainActivity.this, "Search Blood", Toast.LENGTH_SHORT).show();
            setTextColorForMenuItem(item, R.color.colorAccent);
            fragmentTransaction = fragmentManager
                    .beginTransaction()
                    .replace(R.id.container
                            , new Blood_Types_For_Donors());
            fragmentTransaction
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.blood_seeker) {
//            Toast.makeText(MainActivity.this, "Blood Seeker", Toast.LENGTH_SHORT).show();
            setTextColorForMenuItem(item, R.color.colorAccent);
            fragmentTransaction = fragmentManager
                    .beginTransaction()
                    .replace(R.id.container
                            , new Blood_Types_For_Seeker_Fragment());
            fragmentTransaction
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.add_blood_request) {
//            Toast.makeText(MainActivity.this, "Add Blood Request", Toast.LENGTH_SHORT).show();
            setTextColorForMenuItem(item, R.color.colorAccent);
            fragmentTransaction = fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Add_Blood_Request_Fragment());
            fragmentTransaction
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.blood_info) {
            fragmentTransaction = fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Blood_Types_Info());
            fragmentTransaction
                    .addToBackStack(null)
                    .commit();
//            Toast.makeText(MainActivity.this, "Blood Info", Toast.LENGTH_SHORT).show();
            setTextColorForMenuItem(item, R.color.colorAccent);
        } else if (id == R.id.blood_banks) {
            fragmentTransaction = fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Blood_Banks());
            fragmentTransaction
                    .addToBackStack(null)
                    .commit();
//            Toast.makeText(MainActivity.this, "Blood Banks", Toast.LENGTH_SHORT).show();
            setTextColorForMenuItem(item, R.color.colorAccent);
        } else if (id == R.id.about_us) {
//            Toast.makeText(MainActivity.this, "About Us", Toast.LENGTH_SHORT).show();
            setTextColorForMenuItem(item, R.color.colorAccent);
            Home_Fragment home_fragment = (Home_Fragment) getSupportFragmentManager().findFragmentByTag(Utils.Home_Fragment);
            home_fragment.MyCustomAlertDialog();
        } else if (id == R.id.logout) {
//            Toast.makeText(MainActivity.this, "You Logout", Toast.LENGTH_SHORT).show();
            SharedPrefManager.getInstance(MainActivity.this).Logout();
            fragmentManager.popBackStack(null
                    , FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container
                            , new Login_Fragment()
                            , Utils.Login_Fragment)
                    .commit();
        } else if (id == R.id.contact_us) {
            fragmentTransaction = fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Contact_Us_Fragment());
            fragmentTransaction
                    .addToBackStack(null)
                    .commit();
//            Toast.makeText(MainActivity.this, "Contact Us", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.share_app) {
            String shareBody = "https://play.google.com/store/apps/details?id=************************";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "APP NAME (Open it in Google Play Store to Download the Application)");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
//            Toast.makeText(MainActivity.this, "Share App", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setTextColorForMenuItem(MenuItem menuItem, @ColorRes int color) {
        SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, spanString.length(), 0);
        menuItem.setTitle(spanString);
    }

    private void resetAllMenuItemsTextColor(NavigationView navigationView) {
        for (int i = 0; i < navigationView.getMenu().size(); i++)
            setTextColorForMenuItem(navigationView.getMenu().getItem(i), R.color.colorPrimary);
    }
}