package com.rent.rentmanagement.renttest.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.rent.rentmanagement.renttest.Adapters.OccupiedRoomsAdapter;
import com.rent.rentmanagement.renttest.Adapters.RecyclerAdapter;
import com.rent.rentmanagement.renttest.Adapters.TotalRoomsAdapter;
import com.rent.rentmanagement.renttest.DataModels.RoomModel;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.Owner.MainActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Owner.ViewPagerAdapter;
import com.rent.rentmanagement.renttest.Services.GetRoomsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by imazjav0017 on 24-03-2018.
 */

public class RoomsFragment extends Fragment implements SearchView.OnQueryTextListener {
    Context context;
    TabLayout tabLayout;
    ViewPager viewPager;
    public static SearchView searchView;
    public static ProgressBar progressBar;
    ViewPagerAdapter viewPagerAdapter;
    public static RecyclerAdapter adapter;
    public static TotalRoomsAdapter adapter3;
    public static OccupiedRoomsAdapter adapter2;
    public static ArrayList<RoomModel> erooms;
    public static ArrayList<RoomModel> oRooms;
    public static ArrayList<RoomModel> tRooms;
    public static int currentTab;
    //android.support.v4.app.FragmentManager fragmentManager;
    public RoomsFragment() {
    }

    public RoomsFragment(Context context) {
        this.context = context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  v=inflater.inflate(R.layout.owner_fragment_room_tabs,container,false);
        erooms=new ArrayList<>();
        oRooms=new ArrayList<>();
        tRooms=new ArrayList<>();
        currentTab=-1;
        adapter=new RecyclerAdapter(erooms,context);
        adapter2=new OccupiedRoomsAdapter(oRooms,context);
        adapter3=new TotalRoomsAdapter(tRooms,context);
        tabLayout=(TabLayout)v.findViewById(R.id.tabLayout);
        viewPager=(ViewPager)v.findViewById(R.id.viewPager);
        viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager(),context);
        viewPager.setOffscreenPageLimit(2);
        viewPagerAdapter.addFragment(new TotalRoomsFragment(context),"All Rooms");
        viewPagerAdapter.addFragment(new EmptyRoomsFragment(context),"Empty Rooms");
        viewPagerAdapter.addFragment(new RentDueFragment(context),"Rent Due");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(100);
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
        MenuItem item=menu.findItem(R.id.searchMenu);
        item.setVisible(true);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        searchView.setMaxWidth(Integer.MAX_VALUE);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<RoomModel> filteredList = new ArrayList<>();
        ArrayList<RoomModel> filteredOcList = new ArrayList<>();
        ArrayList<RoomModel> filteredTotalList = new ArrayList<>();
        filteredList.clear();
        filteredOcList.clear();
        filteredTotalList.clear();
        if(newText.isEmpty())
        {
            filteredTotalList.addAll(RoomsFragment.tRooms);
        }
        if(RoomsFragment.erooms!=null) {
            for (RoomModel model : RoomsFragment.erooms) {
                if (model.getRoomNo().toLowerCase().contains(newText)) {
                    filteredList.add(model);
                }
            }
            for (RoomModel model : RoomsFragment.oRooms) {
                if (model.getRoomNo().toLowerCase().contains(newText)) {
                    filteredOcList.add(model);
                }
            }
            for (RoomModel model : RoomsFragment.tRooms) {
                if (model.getRoomNo().toLowerCase().contains(newText)) {
                    filteredTotalList.add(model);
                }
            }
        }
        if(RoomsFragment.adapter3!=null) {

            RoomsFragment.adapter.setFilter(filteredList);
            RoomsFragment.adapter2.setFilter(filteredOcList);
            RoomsFragment.adapter3.setFilter(filteredTotalList);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.fab.setVisibility(View.VISIBLE);
        try {
            setStaticData(LoginActivity.sharedPreferences.getString("getRoomsResp",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateView();
        if(currentTab!=-1)
            if(viewPager!=null)
            viewPager.setCurrentItem(currentTab,true);
    }
    public static void showProgress(boolean flag)
    {
        if(progressBar!=null) {
            if (flag)
                progressBar.setVisibility(View.VISIBLE);
            else {
                RoomsFragment.progressBar.setProgress(100);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
    public static void updateView()
    {
        if(adapter!=null && GetRoomsService.tRooms!=null) {
            tRooms.clear();
            erooms.clear();
            oRooms.clear();
            tRooms.addAll(GetRoomsService.tRooms);
            erooms.addAll(GetRoomsService.eRooms);
            oRooms.addAll(GetRoomsService.oRooms);
            adapter.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();
            adapter3.notifyDataSetChanged();
            TotalRoomsFragment.empty();
            RentDueFragment.empty();
            EmptyRoomsFragment.empty();
        }
    }

    public void setStaticData(String s) throws JSONException {
        if (s != null) {
            erooms.clear();
            oRooms.clear();
            tRooms.clear();
            JSONArray mainArray = new JSONArray(s);
            for (int i = 0; i < mainArray.length(); i++) {
                JSONObject mainObject = mainArray.getJSONObject(i);
                String roomId = mainObject.getString("_id");
                String roomType = mainObject.getString("roomType");
                String roomRent = mainObject.getString("roomRent");
                String roomNo = mainObject.getString("roomNo");
                boolean isEmpty = mainObject.getBoolean("isEmpty");
                if (isEmpty == true) {
                    //empty rooms
                    String emptyDays = mainObject.getString("emptyDays");
                    erooms.add(new RoomModel(roomType, roomNo, roomRent, roomId, "10 days", isEmpty, emptyDays));
                    tRooms.add(new RoomModel(roomType, roomNo, roomRent, roomId, "10 days", isEmpty, emptyDays));
                } else {
                    boolean isRentDue = mainObject.getBoolean("isRentDue");
                    String dueAmount = String.valueOf(mainObject.getInt("dueAmount"));
                    String dueDays = mainObject.getString("dueDays");
                    String dueDate = mainObject.getString("dueDate");
                    tRooms.add(new RoomModel(roomType, roomNo, roomRent, dueAmount, roomId,
                            dueDate, isEmpty, isRentDue, dueDays));
                    if (isRentDue = true) {
                        oRooms.add(new RoomModel(roomType, roomNo, roomRent, dueAmount, roomId,
                                dueDate, isEmpty, isRentDue, dueDays));
                    }
                }
            }
            adapter.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();
            adapter3.notifyDataSetChanged();
            }
    }


}