package com.rent.rentmanagement.renttest.Tenants.TenantFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rent.rentmanagement.renttest.Tenants.DataModels.AvailableRoomModel;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Tenants.Adapters.AvailableRoomsAdapter;
import com.rent.rentmanagement.renttest.Tenants.Services.GetAvailableRoomsService;

import java.util.ArrayList;
import java.util.List;

public class AvailableRoomsFragment extends Fragment implements SearchView.OnQueryTextListener {
    View v;
    RecyclerView availableRoomsList;
    Context context;
    public static ArrayList<AvailableRoomModel>availableRooms;
    static AvailableRoomsAdapter adapter;
    public static SearchView searchView;

    public AvailableRoomsFragment() {
    }

    @SuppressLint("ValidFragment")
    public AvailableRoomsFragment(Context context) {
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.tenant_available_rooms_fragment,container,false);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item=menu.findItem(R.id.searchMenuTenant);
        item.setVisible(true);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        ImageView icon= (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        icon.setColorFilter(Color.WHITE);
        searchView.setOnQueryTextListener(this);
        searchView.setMaxWidth(Integer.MAX_VALUE);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText=newText.toLowerCase();
        List<AvailableRoomModel> filteredList=new ArrayList<>();
        filteredList.clear();
        if(newText.isEmpty())
        {
            filteredList.addAll(availableRooms);
        }
        if(availableRooms!=null)
        {
            for(AvailableRoomModel model : availableRooms)
            {
                if(model.getRoomNo().toLowerCase().contains(newText))
                {
                    filteredList.add(model);
                }
                if(model.getBuildingName().toLowerCase().contains(newText))
                    filteredList.add(model);
                if (model.getOwnerName().toLowerCase().contains(newText))
                    filteredList.add(model);
                if(model.getPhoneNo().equals(newText))
                    filteredList.add(model);
            }
            if(adapter!=null)
            {
                adapter.setFilter(filteredList);
            }
        }
        return true;
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
        if (GetAvailableRoomsService.availableRooms != null) {
            availableRooms.clear();
            availableRooms.addAll(GetAvailableRoomsService.availableRooms);
            adapter.notifyDataSetChanged();
        }
    }



}
