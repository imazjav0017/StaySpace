package com.mansa.StaySpace.Tenants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Tenants.Adapters.SentRequestAdapter;
import com.mansa.StaySpace.Tenants.DataModels.AvailableRoomModel;
import com.mansa.StaySpace.Tenants.Services.SentRequestsService;

import java.util.ArrayList;

public class SentRoomRequestActivity extends AppCompatActivity {
    RecyclerView sentRequestsRv;
    public static ProgressBar progressBar;
    public static ArrayList<AvailableRoomModel>sentRequestsList;
    static SentRequestAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenant_activity_sent_room_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Stay Space");
        sentRequestsRv=(RecyclerView) findViewById(R.id.sentRequestsRv);
        progressBar=(ProgressBar)findViewById(R.id.sentRoomRequestsProgress);
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
        progressBar.setVisibility(View.VISIBLE);
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
            progressBar.setVisibility(View.INVISIBLE);
        }
        if(SentRequestsService.failed)
        {
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}
