package com.rent.rentmanagement.renttest.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.Adapters.OccupiedRoomsAdapter;
import com.rent.rentmanagement.renttest.Adapters.RecyclerAdapter;
import com.rent.rentmanagement.renttest.Adapters.TotalRoomsAdapter;
import com.rent.rentmanagement.renttest.AsyncTasks.GetRoomsTask;
import com.rent.rentmanagement.renttest.DataModels.RoomModel;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.Owner.MainActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Owner.ViewPagerAdapter;

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
    public void setTokenJson()
    {
        try {
            if(LoginActivity.sharedPreferences.getString("token",null)!=null
                    &&(LoginActivity.sharedPreferences.getString("ownerId",null)!=null)) {
                progressBar.setVisibility(View.VISIBLE);
                JSONObject token = new JSONObject();
                token.put("auth",LoginActivity.sharedPreferences.getString("token", null));
                token.put("ownerId"
                        ,LoginActivity.sharedPreferences.getString("ownerId",null));
                GetRoomsTask task = new GetRoomsTask(context);
                task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/getRooms", token.toString());
                Log.i("status","fini");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public static void setData(String s, Context context) throws JSONException {
        if(s!=null) {
            erooms.clear();
            oRooms.clear();
            tRooms.clear();
            JSONObject jsonObject = new JSONObject(s);
            Log.i("reponse",s);
            LoginActivity.sharedPreferences.edit().putInt("totalTenants", jsonObject.getInt("totalStudents")).apply();
            JSONArray array = jsonObject.getJSONArray("room");
            Log.i("array", array.toString());
            LoginActivity.sharedPreferences.edit().putInt("occupiedRoomsCount", jsonObject.getInt("occupiedRoomsCount")).apply();
            LoginActivity.sharedPreferences.edit().putInt("emptyRoomsCount", jsonObject.getInt("emptyRoomsCount")).apply();
            LoginActivity.sharedPreferences.edit().putInt("notCollected", jsonObject.getInt("notCollected")).apply();
            LoginActivity.sharedPreferences.edit().putInt("totalRooms", array.length()).apply();
            LoginActivity.sharedPreferences.edit().putString("totalIncome", String.valueOf(jsonObject.getInt("totalIncome"))).apply();
            LoginActivity.sharedPreferences.edit().putString("todayIncome", String.valueOf(jsonObject.getInt("todayIncome"))).apply();
            LoginActivity.sharedPreferences.edit().putString("collected", String.valueOf(jsonObject.getInt("collected"))).apply();
            ProfileFragment.setData();
            LoginActivity.sharedPreferences.edit().putString("roomsDetails", s).apply();
            if (array.length() == 0) {
                Log.i("array","empty");
            } else {
                Toast.makeText(context, "Refreshed!", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject detail = array.getJSONObject(i);
                    if (detail.getBoolean("isEmpty") == true) {
                        //empty rooms
                        erooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                detail.getString("roomRent"), detail.getString("_id"), detail.getString("checkOutDate"), detail.getBoolean("isEmpty"), detail.getString("emptyDays")));
                        tRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                detail.getString("roomRent"), detail.getString("_id"),
                                detail.getString("checkOutDate"), detail.getBoolean("isEmpty"), detail.getString("emptyDays")));

                    } else {
                        tRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                detail.getString("roomRent"), detail.getString("dueAmount"), detail.getString("_id"), detail.getString("dueDate")
                                , detail.getBoolean("isEmpty"), detail.getBoolean("isRentDue"), detail.getString("emptyDays")));
                        if (detail.getBoolean("isRentDue") == true) {
                            oRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                    detail.getString("roomRent"), detail.getString("dueAmount"),
                                    detail.getString("_id"), detail.getString("dueDate"), detail.getBoolean("isEmpty"), detail.getBoolean("isRentDue")
                                    , detail.getString("dueDays")));
                        }


                    }
                }
            }
            adapter.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();
            adapter3.notifyDataSetChanged();
            TotalRoomsFragment.empty();
            RentDueFragment.empty();
            EmptyRoomsFragment.empty();

        }

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
        //viewPager.setCurrentItem(2,true);
        viewPager.setOffscreenPageLimit(2);
        viewPagerAdapter.addFragment(new TotalRoomsFragment(context),"All Rooms");
        viewPagerAdapter.addFragment(new EmptyRoomsFragment(context),"Empty Rooms");
        viewPagerAdapter.addFragment(new RentDueFragment(context),"Rent Due");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        setStaticData(LoginActivity.sharedPreferences.getString("roomsDetails",null));
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
        setTokenJson();
        if(currentTab!=-1)
            if(viewPager!=null)
            viewPager.setCurrentItem(currentTab,true);
    }

    public void setStaticData(String s) {

        if(s!=null) {
            if (s.equals("0")) {
                Toast.makeText(context, "Fetching!", Toast.LENGTH_SHORT).show();
                // setTokenJson();

            } else {
                erooms.clear();
                oRooms.clear();
                tRooms.clear();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    JSONArray array = jsonObject.getJSONArray("room");


                    //Log.i("arrayStatic", array.toString());
                    if (array.length() == 0) {

                    } else {

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject detail = array.getJSONObject(i);
                            if (detail.getBoolean("isEmpty") == true) {
                                //empty rooms
                                erooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                        detail.getString("roomRent"), detail.getString("_id"),
                                        detail.getString("checkOutDate"), detail.getBoolean("isEmpty"), detail.getString("emptyDays")));
                                tRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                        detail.getString("roomRent"), detail.getString("_id"),
                                        detail.getString("checkOutDate"), detail.getBoolean("isEmpty"), detail.getString("emptyDays")));

                            } else {
                                tRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                        detail.getString("roomRent"), detail.getString("dueAmount"), detail.getString("_id"), detail.getString("dueDate")
                                        , detail.getBoolean("isEmpty"), detail.getBoolean("isRentDue"), detail.getString("emptyDays")));
                                if (detail.getBoolean("isRentDue") == true) {
                                    oRooms.add(new RoomModel(detail.getString("roomType"), detail.getString("roomNo"),
                                            detail.getString("roomRent"), detail.getString("dueAmount"),
                                            detail.getString("_id"), detail.getString("dueDate"), detail.getBoolean("isEmpty"), detail.getBoolean("isRentDue")
                                            , detail.getString("dueDays")));
                                }


                            }
                        }


                    /*if(erooms.size()==0)
                    {
                        EmptyRoomsFragment.emptyList.setVisibility(View.VISIBLE);
                        EmptyRoomsFragment.emptyList.setClickable(true);
                    }
                    else {
                        EmptyRoomsFragment.emptyList.setVisibility(View.INVISIBLE);
                        EmptyRoomsFragment.emptyList.setClickable(false);
                    }*/
                    }
                } catch (Exception e) {
                    Log.i("err", "err");
                    e.printStackTrace();
                }
            }
        }
        else
        {
            //setTokenJson();
        }
        adapter.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        adapter3.notifyDataSetChanged();



    }


}