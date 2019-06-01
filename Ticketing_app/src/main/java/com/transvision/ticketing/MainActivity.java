package com.transvision.ticketing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.transvision.ticketing.extra.GetSetValues;
import com.transvision.ticketing.fragment.AdminTicketing;
import com.transvision.ticketing.posting.SendingData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static com.transvision.ticketing.extra.Constants.GETSET;
import static com.transvision.ticketing.extra.Constants.TICKETS_VIEWFAILURE;
import static com.transvision.ticketing.extra.Constants.TICKETS_VIEWSUCCESS;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Fragment fragment;
    GetSetValues getSetValues;
    private Toolbar toolbar;
    Intent intent;
    ArrayList<String> main_tabs_list;
    SendingData sendingData;
    String user_role = "", user_id = "", user_password = "", subdiv_code = "";
    ArrayList<GetSetValues> tickets_list;

    //******************************************* Handler *************************************************************************
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case TICKETS_VIEWSUCCESS:
                    Intent intent = new Intent(MainActivity.this, ViewallTickets.class);
                    intent.putExtra("list", tickets_list);
                    intent.putExtra(GETSET, getSetValues);// passing whole object
                    startActivity(intent);
                    break;

                case TICKETS_VIEWFAILURE:
                    Toast.makeText(MainActivity.this, "Ticket Not Found", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    //*********************************************************************************************************************************
    public enum Steps {
        FORM0(AdminTicketing.class);

        private Class clazz;

        Steps(Class clazz) {
            this.clazz = clazz;
        }

        public Class getFragClass() {
            return clazz;
        }
    }

    //*********************************************************************************************************************************
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();
        getSetValues = new GetSetValues();
        sendingData = new SendingData(this);
        tickets_list = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        main_tabs_list = new ArrayList<>();
        main_tabs_list.addAll(Arrays.asList(getResources().getStringArray(R.array.tabs_list)));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView mrcode = header.findViewById(R.id.textView);
        mrcode.setText("Ticketing Tool");

        //*****************************set app version to drawer******************************************************************************
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String main_curr_version = null;
        if (pInfo != null) {
            main_curr_version = pInfo.versionName;
        }

        NavigationView logout_navigationView = findViewById(R.id.nav_drawer_bottom);
        logout_navigationView.setNavigationItemSelectedListener(this);
        logout_navigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#FF4081")));
        Menu menu = logout_navigationView.getMenu();
        MenuItem nav_login = menu.findItem(R.id.nav_version);
        nav_login.setTitle("Version" + " : " + main_curr_version);

//*******************************************************************************************************************************************
        user_id = Objects.requireNonNull(intent.getExtras()).getString("UserId");
        user_password = intent.getExtras().getString("UserPassword");
        user_role = intent.getExtras().getString("ROLE");

        getSetValues.setUserId(user_id);
        getSetValues.setPassword(user_password);
        getSetValues.setUser_role(user_role);

        if (Objects.equals(intent.getExtras().getString("ROLE"), "MR")) {
            user_role = "MR";
            subdiv_code = user_id.substring(0, 6);
        } else {
            user_role = "";
            subdiv_code = intent.getExtras().getString("SUBDIVCODE");
        }
        switchContent(Steps.FORM0, getResources().getString(R.string.app_name));
    }

    public String getSubdiv_code() {
        return this.subdiv_code;
    }

    public GetSetValues getSetValues() {
        return this.getSetValues;
    }

    //******************************************************************************************************************************************
    public void switchContent(Steps currentForm, String title) {
        try {
            fragment = (Fragment) currentForm.getFragClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        toolbar.setTitle(title);
        ft.replace(R.id.container_main, fragment, currentForm.name());
        ft.commit();
    }

//    public Fragment getFragment(Steps currentForm) {
//        try {
//            fragment = (Fragment) currentForm.getFragClass().newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return fragment;
//    }

    public Intent getintent() {
        return this.intent;
    }

    //******************************************************************************************************************************************
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //******************************************************************************************************************************************
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.generate) {
            fragment = new AdminTicketing();

        } else if (id == R.id.update) {
            tickets_list.clear();
            SendingData.View_All_Tickets viewAllTickets = sendingData.new View_All_Tickets(getSetValues, handler, tickets_list);
            viewAllTickets.execute(user_id, user_password, user_role);

        } else if (id == R.id.view_ticket) {
            tickets_list.clear();
            SendingData.View_All_Tickets viewAllTickets = sendingData.new View_All_Tickets(getSetValues, handler, tickets_list);
            viewAllTickets.execute(user_id, user_password, user_role);

        } else if (id == R.id.setting) {
//            Intent intent = new Intent(MainActivity.this, Settings.class);
//            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            Intent logout = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logout);
            SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            finish();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
