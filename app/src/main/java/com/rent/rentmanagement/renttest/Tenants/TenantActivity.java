package com.rent.rentmanagement.renttest.Tenants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Tenants.Services.GetAvailableRoomsService;
import com.rent.rentmanagement.renttest.Tenants.Services.GetTenantHomeService;
import com.rent.rentmanagement.renttest.Tenants.TenantFragments.AvailableRoomsFragment;
import com.rent.rentmanagement.renttest.Tenants.TenantFragments.MainPageFragment;
import com.rent.rentmanagement.renttest.Tenants.TenantFragments.TenantProfileFragment;

import org.json.JSONObject;

import java.net.URISyntaxException;

public class TenantActivity extends AppCompatActivity {



    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.tenantsFragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment=null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment=new MainPageFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_dashboard:
                    fragment=new AvailableRoomsFragment(getApplicationContext());
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_notifications:
                    fragment=new TenantProfileFragment(getApplicationContext());
                    loadFragment(fragment);
                    return true;
            }
            return true;
        }
    };
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://sleepy-atoll-65823.herokuapp.com/");
        } catch (URISyntaxException e) {
            Log.i("err",e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenant_main_activity);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Tenant Side");
        loadFragment(new MainPageFragment());
        mSocket.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(getApplicationContext(), GetTenantHomeService.class));
        startService(new Intent(getApplicationContext(),GetAvailableRoomsService.class));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_tenants,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.logoutTenantOption)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Logout!").setMessage("Are You Sure You Wish To Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("status","logout");
                            LoginActivity.sharedPreferences.edit().clear().apply();
                            mSocket.disconnect();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);

                        }
                    }).setNegativeButton("No",null).show();
            return true;

        }

        return false;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Exit!").setMessage("Are You Sure You Wish To Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       moveTaskToBack(true);
                    }
                }).setNegativeButton("No",null).show();
    }

}

