package com.mansa.StaySpace.Tenants;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.mansa.StaySpace.AsyncTasks.LogoutTask;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.MyFirebaseInstanceIdService;
import com.mansa.StaySpace.Owner.BottomNavigationViewHelper;
import com.mansa.StaySpace.Owner.MainActivity;
import com.mansa.StaySpace.Services.DeleteTokenService;
import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Services.GetRoomsService;
import com.mansa.StaySpace.Tenants.Services.GetAvailableBuildingsService;
import com.mansa.StaySpace.Tenants.Services.GetAvailableRoomsService;
import com.mansa.StaySpace.Tenants.Services.GetTenantHomeService;
import com.mansa.StaySpace.Tenants.Services.GetTenantPeersService;
import com.mansa.StaySpace.Tenants.Services.SendTokenTenantService;
import com.mansa.StaySpace.Tenants.TenantFragments.AvailableBuildingsFragment;
import com.mansa.StaySpace.Tenants.TenantFragments.AvailableRoomsFragment;
import com.mansa.StaySpace.Tenants.TenantFragments.ComplaintsFragmentTenant;
import com.mansa.StaySpace.Tenants.TenantFragments.MainPageFragment;
import com.mansa.StaySpace.Tenants.TenantFragments.TenantProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class TenantActivity extends AppCompatActivity {

    BottomNavigationView navigation;
    BroadcastReceiver tokenReciever;
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
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new MainPageFragment();
                     break;
                case R.id.navigation_dashboard:
                    startService(new Intent(getApplicationContext(), GetAvailableBuildingsService.class));
                    fragment = new AvailableBuildingsFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment = new TenantProfileFragment(getApplicationContext());
                    break;
                case R.id.complaintsTenant:
                    fragment=new ComplaintsFragmentTenant();
                    break;
            }
            loadFragment(fragment);
            return true;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenant_main_activity);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Stay Space");
        loadFragment(new MainPageFragment());
    }
    void sendToken()
    {
        Intent i=new Intent(getApplicationContext(), SendTokenTenantService.class);
        startService(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(tokenReciever);
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* tokenReciever=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("token","recieved");
                if(LoginActivity.sharedPreferences.getBoolean("isOwner",true)==false)
                    sendToken();
            }
        };
        registerReceiver(tokenReciever,new IntentFilter(MyFirebaseInstanceIdService.TOKEN_BROADCAST));*/
        startService(new Intent(getApplicationContext(), GetTenantHomeService.class));
        startService(new Intent(getApplicationContext(), GetTenantPeersService.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_tenants, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logoutTenantOption) {
            new AlertDialog.Builder(this)
                    .setTitle("Logout!").setMessage("Are You Sure You Wish To Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("logout", "rec");
                            final ProgressDialog progressDialog;
                            progressDialog=new ProgressDialog(TenantActivity.this);
                            progressDialog.setTitle("Logout");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.setMax(100);
                            progressDialog.setMessage("Logging out...");
                            String auth=LoginActivity.sharedPreferences.getString("token",null);
                            String nToken=LoginActivity.sharedPreferences.getString("nToken",null);
                            if(auth!=null && nToken!=null) {
                                JSONObject data = new JSONObject();
                                try {
                                    data.put("auth", auth);
                                    data.put("nToken", nToken);
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();
                                    LogoutTask task=new LogoutTask(getApplicationContext(), new LogoutTask.LogoutResp() {
                                        @Override
                                        public void processFinish(Boolean output,Boolean isSuccess) {
                                            if(output) {
                                                progressDialog.dismiss();
                                                if (isSuccess) {
                                                    LoginActivity.sharedPreferences.edit().clear().apply();
                                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                                    startActivity(i);
                                                    startService(new Intent(getApplicationContext(), DeleteTokenService.class));
                                                }
                                            }
                                        }
                                    });
                                    task.execute(LoginActivity.MAINURL+"/users/logout",data.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                                Log.i("LogoutError","nTokenMissing");
                        }
                    }).setNegativeButton("No", null).show();
            return true;

        }
        else if(item.getItemId()==R.id.sentRoomRequestsOption)
        {
            startActivity(new Intent(getApplicationContext(),SentRoomRequestActivity.class));
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        if (navigation.getSelectedItemId() != R.id.navigation_home) {
            if (navigation.getSelectedItemId() == R.id.navigation_dashboard) {
                if(AvailableBuildingsFragment.searchView.isIconified())
                    navigation.setSelectedItemId(R.id.navigation_home);
                else
                    AvailableBuildingsFragment.searchView.setIconified(true);
                }
          /* else if (navigation.getSelectedItemId() == R.id.navigation_notifications) {
                navigation.setSelectedItemId(R.id.navigation_home);

            }*/
          else
                navigation.setSelectedItemId(R.id.navigation_home);
            }
        else {
                new AlertDialog.Builder(this).setTitle("Exit!").setMessage("Are You Sure You Wish To Exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                moveTaskToBack(true);
                            }
                        }).setNegativeButton("No", null).show();
            }

    }
}

