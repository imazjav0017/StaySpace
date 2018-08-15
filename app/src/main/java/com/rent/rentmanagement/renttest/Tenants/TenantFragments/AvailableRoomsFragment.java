package com.rent.rentmanagement.renttest.Tenants.TenantFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rent.rentmanagement.renttest.DataModels.AvailableRoomModel;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.Owner.MainActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Tenants.Adapters.AvailableRoomsAdapter;
import com.rent.rentmanagement.renttest.Tenants.Async.GetAvailableRoomsTask;
import com.rent.rentmanagement.renttest.Tenants.GetAvailableRoomsService;
import com.rent.rentmanagement.renttest.Tenants.TenantActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AvailableRoomsFragment extends Fragment {
    View v;
    RecyclerView availableRoomsList;
    Context context;
    public static ArrayList<AvailableRoomModel>availableRooms;
    static AvailableRoomsAdapter adapter;

    public AvailableRoomsFragment() {
    }

    @SuppressLint("ValidFragment")
    public AvailableRoomsFragment(Context context) {
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.available_rooms_fragment,container,false);
        availableRoomsList=(RecyclerView)v.findViewById(R.id.availableRoomsList);
        availableRooms=new ArrayList<>();
        adapter=new AvailableRoomsAdapter(availableRooms);
        LinearLayoutManager lm=new LinearLayoutManager(context);
        availableRoomsList.setLayoutManager(lm);
        availableRoomsList.setHasFixedSize(true);
        availableRoomsList.setAdapter(adapter);
        return  v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("in 2","frag");
        setView();

    }
    //called by tenantActivity when loading complete
    public static void updateNow()
    {
        //if updateView is called by activity before opening fragment
        // will lead to null pointer
        if(availableRooms!=null)
        {
            setView();
        }
    }
    public static void setView() {
        if (TenantActivity.availableRooms != null) {
            availableRooms.clear();
            availableRooms.addAll(TenantActivity.availableRooms);
            adapter.notifyDataSetChanged();
        }
    }



}
