package com.rent.rentmanagement.renttest.Owner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rent.rentmanagement.renttest.AsyncTasks.LogoutTask;
import com.rent.rentmanagement.renttest.Fragments.AllTenantsFragment;
import com.rent.rentmanagement.renttest.Fragments.OwnerProfileFragment;
import com.rent.rentmanagement.renttest.Fragments.ProfileFragment;
import com.rent.rentmanagement.renttest.Fragments.RoomsFragment;
import com.rent.rentmanagement.renttest.Fragments.TenantsFragment;
import com.rent.rentmanagement.renttest.MyFirebaseInstanceIdService;
import com.rent.rentmanagement.renttest.Services.DeleteTokenService;
import com.rent.rentmanagement.renttest.Services.GetAllTenantsService;
import com.rent.rentmanagement.renttest.Services.GetRoomRequestsService;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Services.GetRoomsService;
import com.rent.rentmanagement.renttest.Services.SendTokenOwnerService;
import com.rent.rentmanagement.renttest.Services.getOwnerDetailsService;
import com.rent.rentmanagement.renttest.Tenants.SendRequestActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
   public static BottomNavigationView bottomNavigationView;
    public static FloatingActionButton fab;
    boolean showSv=false;
    String Buildings;
    private BroadcastReceiver tokenReciever;
    int requestNotification=0;
    public static int paymentNotification=0;
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.logoutMenuOption :
                new AlertDialog.Builder(this)
                        .setTitle("Logout!").setMessage("Are You Sure You Wish To Logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("status","logout");
                                final ProgressDialog progressDialog;
                                progressDialog=new ProgressDialog(MainActivity.this);
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
                                        progressDialog.show();
                                        LogoutTask task=new LogoutTask(getApplicationContext(), new LogoutTask.LogoutResp() {
                                            @Override
                                            public void processFinish(Boolean output,Boolean isSuccess) {
                                                if(output) {
                                                    progressDialog.dismiss();
                                                    if (isSuccess) {
                                                        LoginActivity.sharedPreferences.edit().clear().apply();
                                                        if (GetRoomsService.tRooms != null) {
                                                            GetRoomsService.tRooms.clear();
                                                            GetRoomsService.oRooms.clear();
                                                            GetRoomsService.eRooms.clear();
                                                        }
                                                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                                        startActivity(i);
                                                        startService(new Intent(getApplicationContext(), DeleteTokenService.class));
                                                    }
                                                }
                                            }
                                        });
                                        task.execute(LoginActivity.URL+"/users/logout",data.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                    Log.i("LogoutError","nTokenMissing");
                            }
                        }).setNegativeButton("No",null).show();
                return true;
            case R.id.switchBuildingOption :
                Log.i("switching","building");
                Intent i=new Intent(getApplicationContext(),SwitchBuildingActivity.class);
                startActivity(i);
                return true;
        }

        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        fab = (FloatingActionButton) findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryToAddRooms();
            }
        });
        fab.setVisibility(View.INVISIBLE);
        requestNotification=getIntent().getIntExtra("fromN",-1);
        paymentNotification=getIntent().getIntExtra("fromPayment",-1);
        Log.i("Notification","recieved"+requestNotification);
        if(requestNotification==StudentActivity.FROM_NOTIFICATION) {
            bottomNavigationView.setSelectedItemId(R.id.tenantsViewiTem);
        }
        else if(paymentNotification==StudentActivity.FROM_NOTIFICATION)
        {
            bottomNavigationView.setSelectedItemId(R.id.roomViewItem);
        }
        else
            loadFragment(new ProfileFragment(MainActivity.this));
        //getRoomRequestsList

        if(getIntent().getBooleanExtra("deletedStudent",false)==true)
        {
            loadFragment(new RoomsFragment(MainActivity.this,paymentNotification));
        }
    }
    void sendToken()
    {
        Intent i=new Intent(getApplicationContext(), SendTokenOwnerService.class);
        startService(i);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        int id=item.getItemId();
        switch (id)
        {
            case R.id.profileViewItem:
                fab.setVisibility(View.INVISIBLE);
                fragment=new ProfileFragment(MainActivity.this);
                break;
            case R.id.roomViewItem:
                fragment=new RoomsFragment(MainActivity.this,paymentNotification);
                break;
            case R.id.tenantsViewiTem:
                fab.setVisibility(View.INVISIBLE);
                fragment=new AllTenantsFragment(MainActivity.this,requestNotification);
                Log.i("current","tenants");
                break;
            case R.id.myProfileViewTab:
                fab.setVisibility(View.INVISIBLE);
                fragment=new OwnerProfileFragment(MainActivity.this);
                break;
        }
        loadFragment(fragment);
        return true;
    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentsContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {

        if (bottomNavigationView.getSelectedItemId()!=R.id.profileViewItem)
        {
            if(bottomNavigationView.getSelectedItemId()==R.id.roomViewItem)
            {
                if(!(RoomsFragment.searchView.isIconified()))
                {
                    RoomsFragment.searchView.setIconified(true);
                }
                else
                {
                    bottomNavigationView.setSelectedItemId(R.id.profileViewItem);
                }
            }
            if(bottomNavigationView.getSelectedItemId()==R.id.tenantsViewiTem)
            {
                if(!(TenantsFragment.searchView.isIconified()))
                {
                    TenantsFragment.searchView.setIconified(true);
                }
                else
                {
                    bottomNavigationView.setSelectedItemId(R.id.profileViewItem);
                }
            }
            else
                bottomNavigationView.setSelectedItemId(R.id.profileViewItem);


        }
        else
        {
            new AlertDialog.Builder(this).setTitle("Exit!").setMessage("Are You Sure You Wish To Exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           moveTaskToBack(true);
                        }
                    }).setNegativeButton("No",null).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
      // unregisterReceiver(tokenReciever);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*tokenReciever=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("token","recieved");
                if(LoginActivity.sharedPreferences.getBoolean("isOwner",false)==true)
                    sendToken();
            }
        };
        registerReceiver(tokenReciever,new IntentFilter(MyFirebaseInstanceIdService.TOKEN_BROADCAST));*/
        Buildings=LoginActivity.sharedPreferences.getString("buildings",null);
        if(Buildings!=null)
        {
            int  buildingIndex= LoginActivity.sharedPreferences.getInt("buildingIndex",0);
            try {
                JSONArray buildingArray = new JSONArray(Buildings);
                if (buildingArray.length() > 0) {
                    JSONObject buildingObject = buildingArray.getJSONObject(buildingIndex);
                    String buildName = buildingObject.getString("name");
                    setTitle(buildName);
                }
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        Log.i("onResume",ACTIVITY_SERVICE);
        startService(new Intent(getApplicationContext(),getOwnerDetailsService.class));
        startService(new Intent(getApplicationContext(), GetRoomRequestsService.class));
        startService(new Intent(getApplicationContext(), GetRoomsService.class));
        startService(new Intent(getApplicationContext(), GetAllTenantsService.class));
        RoomsFragment.showProgress(true);
    }
    public void tryToAddRooms()
         {
             Buildings=LoginActivity.sharedPreferences.getString("buildings",null);
             if(Buildings!=null)
             {
                 try {
                     JSONArray buildingArray = new JSONArray(Buildings);
                     if (buildingArray.length() > 0) {
                        Intent i=new Intent(getApplicationContext(),SelectBuildingActivity.class);
                        startActivity(i);
                     } else
                     {
                         Intent i=new Intent(getApplicationContext(),UpdateOwnerExtraActivity.class);
                         startActivity(i);
                     }
                 }catch (JSONException e)
                 {
                     e.printStackTrace();
                 }
             }
         }


}

