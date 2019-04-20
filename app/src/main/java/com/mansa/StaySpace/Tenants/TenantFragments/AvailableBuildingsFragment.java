package com.mansa.StaySpace.Tenants.TenantFragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Tenants.Adapters.BuildingAdapter;
import com.mansa.StaySpace.Tenants.Async.GetAvailableRoomsTask;
import com.mansa.StaySpace.Tenants.DataModels.AvailableRoomModel;
import com.mansa.StaySpace.Tenants.DataModels.BuildingModel;
import com.mansa.StaySpace.Tenants.Services.GetAvailableBuildingsService;
import com.mansa.StaySpace.Tenants.Services.GetAvailableRoomsService;

import java.util.ArrayList;
import java.util.List;

public class AvailableBuildingsFragment extends Fragment implements SearchView.OnQueryTextListener {
    View v;
    RecyclerView rv;
    static List<BuildingModel>buildingModels;
   static BuildingAdapter adapter;
    static ProgressBar progressBar;
    static TextView emptyText;
   static SwipeRefreshLayout swipeRefreshLayout;
    public static SearchView searchView;
    public AvailableBuildingsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.tenant_fragment_available_buildings,container,false);
        rv=v.findViewById(R.id.availableBuildingsRv);
        progressBar=v.findViewById(R.id.availableBuildingsProgress);
        swipeRefreshLayout=v.findViewById(R.id.availableBuildingSwipeRefresh);
        emptyText=v.findViewById(R.id.emptyBuildingsView);
        buildingModels=new ArrayList<>();
        adapter=new BuildingAdapter(buildingModels);
        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        return v;
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
        searchView.setQueryHint("Enter Building name...");

    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        newText=newText.toLowerCase();
        List<BuildingModel> filteredList1=new ArrayList<>();
            filteredList1.clear();
            if(newText.isEmpty())
            {
                // Log.i("empty","called");
                filteredList1.addAll(buildingModels);
            }
            else {
                //text non empty
                if (buildingModels != null) {
                    for (BuildingModel model : buildingModels) {
                       if(model.getName().toLowerCase().contains(newText))
                       {
                           filteredList1.add(model);
                       }
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
    void refresh()
    {
        getContext().startService(new Intent(getContext(),GetAvailableBuildingsService.class));
    }
    @Override
    public void onPause() {
        super.onPause();
        if(swipeRefreshLayout!=null)
        {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        setView();
    }

    public static void updateNow()
    {
        //if updateView is called by activity before opening fragment
        // will lead to null pointer
        if(buildingModels!=null)
        {
            setView();
        }
    }
    public static void setView() {
        if (GetAvailableBuildingsService.buildingModels != null) {
            buildingModels.clear();
            buildingModels.addAll(GetAvailableBuildingsService.buildingModels);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
            if(buildingModels.isEmpty())
            {
                emptyText.setVisibility(View.VISIBLE);
            }
            else
            {
                emptyText.setVisibility(View.INVISIBLE);
            }
        }
        if(GetAvailableBuildingsService.failed)
            progressBar.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }
}
