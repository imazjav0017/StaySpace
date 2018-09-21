package com.rent.rentmanagement.renttest.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.Adapters.ProfileDetailsAdapter;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.DataModels.ProfileDetailsModel;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Services.getOwnerDetailsService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imazjav0017 on 02-03-2018.
 */

public class ProfileFragment extends Fragment {
    View v;
    Context context;
    LinearLayout totalRooms;
    LinearLayout totalStudents;
    static SwipeRefreshLayout swipeRefreshLayout;
    static TextView name;
   // TextView noOfRooms;
   // TextView noOfTenants;
    static String oName,rooms,tenants;
    RecyclerView detailsRv;
    RecyclerView roomsRv;
    static ProfileDetailsAdapter adapter;
    static ProfileDetailsAdapter adapter2;
    static List<ProfileDetailsModel>pList,rList;
    public ProfileFragment() {

    }
    public ProfileFragment(Context context) {
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.owner_fragment_main_details_page,container,false);
        name=(TextView)v.findViewById(R.id.ownerNameTextView);
        detailsRv=(RecyclerView)v.findViewById(R.id.profileDetailsRv);
        roomsRv=(RecyclerView)v.findViewById(R.id.roomsdetailsRv) ;
        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.ownerMainDetailsSrl);
        pList=new ArrayList<>();
        rList=new ArrayList<>();
        adapter=new ProfileDetailsAdapter(pList);
        adapter2=new ProfileDetailsAdapter(rList);
        LinearLayoutManager lm1=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        detailsRv.setLayoutManager(lm1);
        detailsRv.setHasFixedSize(true);
        detailsRv.setAdapter(adapter);
        LinearLayoutManager lm2=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        roomsRv.setLayoutManager(lm2);
        roomsRv.setHasFixedSize(true);
        roomsRv.setAdapter(adapter2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });


        return v;
    }
    void refresh()
    {
        getContext().startService(new Intent(getContext(),getOwnerDetailsService.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    public static void setData(){
        if(pList!=null) {
            pList.clear();
            rList.clear();
            String s = LoginActivity.sharedPreferences.getString("ownerDetails", null);
            try {
                JSONObject object = new JSONObject(s);
                JSONObject ownerNameObject = object.getJSONObject("name");
                String ownerName = ownerNameObject.getString("firstName") + " " + ownerNameObject.getString("lastName");
                name.setText(ownerName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String totalRooms = LoginActivity.sharedPreferences.getString("totalRooms", null);
            String emptyCount = LoginActivity.sharedPreferences.getString("emptyCount", null);
            String tenantCount = LoginActivity.sharedPreferences.getString("tenantCount", null);
            String occupiedRoomsCount = LoginActivity.sharedPreferences.getString("occupiedRoomsCount", null);
            String totalCollectedAmount = LoginActivity.sharedPreferences.getString("totalCollectedAmount", null);
            String totalRent = LoginActivity.sharedPreferences.getString("totalRent", null);
            String totalDueAmount = LoginActivity.sharedPreferences.getString("totalDueAmount", null);


            if (totalRooms != null)
                rList.add(new ProfileDetailsModel("Total Rooms", totalRooms));
            if (occupiedRoomsCount != null)
                rList.add(new ProfileDetailsModel("Occupied Rooms", occupiedRoomsCount));
            if (emptyCount != null)
                rList.add(new ProfileDetailsModel("Empty Rooms", emptyCount));


            if (totalRent != null)
                pList.add(new ProfileDetailsModel("Total Rent", "₹" + totalRent));
            if (totalCollectedAmount != null)
                pList.add(new ProfileDetailsModel("Rent Collected", "₹" + totalCollectedAmount));
            if (totalDueAmount != null) {
                pList.add(new ProfileDetailsModel("Due Rent", "₹" + totalDueAmount));
            }
            if (tenantCount != null) {
                rList.add(new ProfileDetailsModel("Total Tenants", tenantCount));
            }
            adapter.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
