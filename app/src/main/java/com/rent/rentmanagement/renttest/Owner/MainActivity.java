package com.rent.rentmanagement.renttest.Owner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.rent.rentmanagement.renttest.Fragments.AllTenantsFragment;
import com.rent.rentmanagement.renttest.Fragments.OwnerProfileFragment;
import com.rent.rentmanagement.renttest.Fragments.ProfileFragment;
import com.rent.rentmanagement.renttest.Fragments.RoomsFragment;
import com.rent.rentmanagement.renttest.Fragments.TenantsFragment;
import com.rent.rentmanagement.renttest.Services.GetRoomRequestsService;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
   public static BottomNavigationView bottomNavigationView;
    public static FloatingActionButton fab;
    public static String roomInfo;
    boolean showSv=false;
    public static boolean completedTasks=true;
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.logoutMenuOption)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Logout!").setMessage("Are You Sure You Wish To Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                                Log.i("status","logout");
                                LoginActivity.sharedPreferences.edit().clear().apply();
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);

                        }
                    }).setNegativeButton("No",null).show();
            return true;

        }

        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        fab = (FloatingActionButton) findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    JSONArray buildings=new JSONArray(LoginActivity.sharedPreferences.getString("buildingName",null));
                    if(buildings.length()==0)
                    {
                      Intent i=new Intent(getApplicationContext(),UpdateOwnerExtraActivity.class);
                      startActivity(i);
                    }
                    else
                    {
                        Intent i=new Intent(getApplicationContext(),automanualActivity.class);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        fab.setVisibility(View.INVISIBLE);
        if(getIntent().getIntExtra("fromN",-1)!= StudentActivity.FROM_NOTIFICATION)
        loadFragment(new ProfileFragment(MainActivity.this));
        else
            loadFragment(new RoomsFragment(MainActivity.this));
        //getRoomRequestsList

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
                fragment=new RoomsFragment(MainActivity.this);

                break;
            case R.id.tenantsViewiTem:
                fab.setVisibility(View.INVISIBLE);

                fragment=new AllTenantsFragment(MainActivity.this);
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

        }
        else
        {
            new AlertDialog.Builder(this).setTitle("Exit!").setMessage("Are You Sure You Wish To Exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.super.onBackPressed();
                        }
                    }).setNegativeButton("No",null).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume",ACTIVITY_SERVICE);
        startService(new Intent(getApplicationContext(), GetRoomRequestsService.class));
    }



}

