package com.rent.rentmanagement.renttest.Tenants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Tenants.Adapters.SentRequestAdapter;
import com.rent.rentmanagement.renttest.Tenants.DataModels.AvailableRoomModel;
import com.rent.rentmanagement.renttest.Tenants.Services.GetAvailableRoomsService;
import com.rent.rentmanagement.renttest.Tenants.Services.SentRequestsService;

import java.util.ArrayList;

public class SentRoomRequestActivity extends AppCompatActivity {
    RecyclerView sentRequestsRv;
    public static ArrayList<AvailableRoomModel>sentRequestsList;
    static SentRequestAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_room_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Stay Space");
        sentRequestsRv=(RecyclerView) findViewById(R.id.sentRequestsRv);
        sentRequestsList=new ArrayList<>();
        adapter=new SentRequestAdapter(sentRequestsList);
        LinearLayoutManager lm=new LinearLayoutManager(getApplicationContext());
        sentRequestsRv.setLayoutManager(lm);
        sentRequestsRv.setHasFixedSize(true);
        sentRequestsRv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(getApplicationContext(), SentRequestsService.class));
        setView();
    }
    public static void updateNow()
    {
        //if updateView is called by activity before opening fragment
        // will lead to null pointer
        if(sentRequestsList!=null)
        {
            setView();
        }
    }
    public static void setView() {
        if (SentRequestsService.requestList != null) {
            sentRequestsList.clear();
            sentRequestsList.addAll(SentRequestsService.requestList);
            adapter.notifyDataSetChanged();
        }
    }
}
