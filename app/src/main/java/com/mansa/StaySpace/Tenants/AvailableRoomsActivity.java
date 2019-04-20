package com.mansa.StaySpace.Tenants;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Tenants.Adapters.AvailableRoomsAdapter;
import com.mansa.StaySpace.Tenants.Async.GetAvailableRoomsTask;
import com.mansa.StaySpace.Tenants.DataModels.AvailableRoomModel;
import com.mansa.StaySpace.Tenants.Services.GetAvailableRoomsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AvailableRoomsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView availableRoomsList;
    Context context;
    public static ArrayList<AvailableRoomModel>availableRooms;
    public static List<String> buildings; //ND non Distinct;
    public static Set<String> buildingsD; //D  Distinct;
    static AvailableRoomsAdapter adapter;
    public static SearchView searchView;
    boolean filterState=false;
    static TextView emptyText;
    public static ProgressBar progressBar;
    List<AvailableRoomModel>filteredList;
    String bId,bName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_rooms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bId=getIntent().getStringExtra("buildingId");
        bName=getIntent().getStringExtra("buildingName");
        setTitle(bName);
        availableRoomsList=(RecyclerView)findViewById(R.id.availableRoomsList);
        progressBar=(ProgressBar)findViewById(R.id.availableRoomsProgress);
        emptyText=(TextView)findViewById(R.id.noAvailableRoomstext);
        availableRooms=new ArrayList<>();
        buildings=new ArrayList<>();
        filteredList=new ArrayList<>();
        adapter=new AvailableRoomsAdapter(availableRooms);
        LinearLayoutManager lm=new LinearLayoutManager(context);
        availableRoomsList.setLayoutManager(lm);
        availableRoomsList.setHasFixedSize(true);
        availableRoomsList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        if(bId!=null)
        startService(new Intent(getApplicationContext(),GetAvailableRoomsService.class).putExtra("buildingId",bId));
        else
            Log.i("bID","null");
        if(!isMyServiceRunning())
        updateNow();
    }
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.mansa.StaySpace.Tenants.Services.GetAvailableRoomsService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //called by tenantActivity when loading complete
    public static void updateNow()
    {
        //if updateView is called by activity before opening fragment
        // will lead to null pointer
        if(availableRooms!=null)
        {
            availableRooms.clear();
            setView();
        }
    }
    public static void setView() {
        progressBar.setVisibility(View.INVISIBLE);
        if (GetAvailableRoomsService.availableRooms != null) {
            availableRooms.clear();
            availableRooms.addAll(GetAvailableRoomsService.availableRooms);
            adapter.notifyDataSetChanged();
            if(availableRooms.isEmpty())
            {
                emptyText.setVisibility(View.VISIBLE);
            }
            else
            {
                emptyText.setVisibility(View.INVISIBLE);
            }
        }
        if(GetAvailableRoomsTask.failed)
            progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_search_room, menu);
        searchView = (SearchView) menu.findItem(R.id.searchRoomTenant).getActionView();
        ImageView icon=searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        icon.setColorFilter(Color.WHITE);
        searchView.setOnQueryTextListener(AvailableRoomsActivity.this);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Enter Room No or Owner Ph No.");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.searchRoomTenant)
        {
            return true;
        }
        return false;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        newText=newText.toLowerCase();
        List<AvailableRoomModel> filteredList1=new ArrayList<>();
            filteredList1.clear();
            if(newText.isEmpty())
            {
                // Log.i("empty","called");
                filteredList1.addAll(availableRooms);
            }
            else {
                //text non empty
                if (availableRooms != null) {
                    for (AvailableRoomModel model : availableRooms) {
                        if (model.getRoomNo().toLowerCase().contains(newText)) {
                            filteredList1.add(model);
                        }
                        if (model.getOwnerName().toLowerCase().equals(newText))
                            filteredList1.add(model);
                        if (model.getPhoneNo().equals(newText))
                            filteredList1.add(model);
                    }
                    // Log.i("emptyElse",String.valueOf(filteredList1.size()));
                }
            }

            //common to both empty and non empty
            if (adapter != null) {
                adapter.setFilter(filteredList1);
            }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified())
        {
            searchView.setIconified(true);
        }
        else {
            availableRooms.clear();
            adapter.notifyDataSetChanged();
            if (GetAvailableRoomsService.availableRooms != null)
                GetAvailableRoomsService.availableRooms.clear();
            super.onBackPressed();
            finish();
        }
    }

}
