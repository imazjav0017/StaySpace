package com.rent.rentmanagement.renttest.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.rent.rentmanagement.renttest.Adapters.TotalTenantsAdapter;
import com.rent.rentmanagement.renttest.DataModels.StudentModel;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Services.GetAllTenantsService;
import com.rent.rentmanagement.renttest.Tenants.TenantActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitish on 27-03-2018.
 */

public class TenantsFragment extends Fragment implements SearchView.OnQueryTextListener {
    Context context;
    public static SearchView searchView;
    static TextView empty;
    CheckBox onlyRooms;
    boolean rooms;
    String checkRooms;
    static SwipeRefreshLayout swipeRefreshLayout;
    public TenantsFragment() {
    }

    public TenantsFragment(Context context) {
        this.context = context;
    }

    RecyclerView totalTenants;
    public static List<StudentModel> studentModelList;
    public static TotalTenantsAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    void setStaticData(String s) throws JSONException {
        if(s!=null) {
            studentModelList.clear();
            JSONObject mainObject = new JSONObject(s);
            JSONArray tenantsArray = mainObject.getJSONArray("tenants");
            JSONArray studentsArray = mainObject.getJSONArray("student");
            for (int i = 0; i < studentsArray.length(); i++) {
                JSONObject roomDetail = studentsArray.getJSONObject(i);
                String roomId = roomDetail.getString("_id");
                String roomNo = roomDetail.getString("roomNo");
                JSONArray studentsDetailArray = roomDetail.getJSONArray("students");
                for (int j = 0; j < studentsDetailArray.length(); j++) {
                    JSONObject studentDetails = studentsDetailArray.getJSONObject(j);
                    String studentId = studentDetails.getString("_id");
                    String name = studentDetails.getString("name");
                    String mobileNo = studentDetails.getString("mobileNo");
                    String adharNo = studentDetails.getString("adharNo");
                    studentModelList.add(new StudentModel(name, mobileNo, roomNo, studentId, adharNo, roomId,false));
                }
            }
            for (int i = 0; i < tenantsArray.length(); i++) {
                JSONObject tenantObject = tenantsArray.getJSONObject(i);
                String tenantId = tenantObject.getString("_id");
                String mobileNo = tenantObject.getString("mobileNo");
                JSONObject nameObject = tenantObject.getJSONObject("name");
                String name = nameObject.getString("firstName") + " " + nameObject.getString("lastName");
                JSONObject roomObject = tenantObject.getJSONObject("room");
                String roomId = roomObject.getString("_id");
                String roomNo = roomObject.getString("roomNo");
                String adharNo = tenantObject.getString("adharNo");
                studentModelList.add(new StudentModel(name, mobileNo, roomNo, tenantId, adharNo, roomId,true));
            }
            adapter.notifyDataSetChanged();
        }
    }

    public static void updateView()
    {
        if(adapter!=null && GetAllTenantsService.studentsList!=null)
        {
            studentModelList.clear();
            studentModelList.addAll(GetAllTenantsService.studentsList);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            if(studentModelList.isEmpty())
                empty.setVisibility(View.VISIBLE);
            else
                empty.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    //to check if string has only numbers
    boolean isNumber(String s)
    {
        int x=1;
        for(int i=0;i<s.length();i++)
        {
            if(Character.isDigit(s.charAt(i)))
            {
                x=0;
                continue;
            }
            else {
                x=1;
            }
        }
        if(x==0)
        return true;
        else
            return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item=menu.findItem(R.id.searchMenu);
        item.setVisible(true);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Enter Tenant name or RoomNo...");
        onlyRooms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    rooms = true;
                    onQueryTextChange(checkRooms);
                }
                else
                {
                    rooms=false;
                    onQueryTextChange(checkRooms);

                }


            }
        });
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<StudentModel>filteredTenants=new ArrayList<>();
        filteredTenants.clear();
        if(newText.isEmpty())
        {
            onlyRooms.setChecked(false);
        }

        if(TenantsFragment.studentModelList!=null)
        {
            if(isNumber(newText))
            {
                checkRooms=newText;
                onlyRooms.setVisibility(View.VISIBLE);
            }
            else
            {
                onlyRooms.setVisibility(View.INVISIBLE);
            }
            for(StudentModel model:TenantsFragment.studentModelList)
            {
                if(rooms)
                {
                    if(model.getRoomNo().equals(newText))
                    {
                        filteredTenants.add(model);
                    }
                }
                else {
                    if (model.getName().toLowerCase().contains(newText)) {
                        filteredTenants.add(model);
                    }
                    if(model.getRoomNo().equals(newText))
                    {
                        filteredTenants.add(model);
                    }
                }

            }

            if(TenantsFragment.adapter!=null)
            {
                TenantsFragment.adapter.setFilter(filteredTenants);
            }
        }
        return true;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.owner_tenant_all,container,false);
        totalTenants=(RecyclerView)v.findViewById(R.id.totalStudentsList);
        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.allTenantsSrl);
        empty=(TextView)v.findViewById(R.id.noTenantsText);
        onlyRooms=(CheckBox)v.findViewById(R.id.roomsOnlySearchFilter);
        studentModelList=new ArrayList<>();
        adapter=new TotalTenantsAdapter(studentModelList);
        LinearLayoutManager lm=new LinearLayoutManager(context);
        totalTenants.setLayoutManager(lm);
        totalTenants.setHasFixedSize(true);
        totalTenants.setAdapter(adapter);
        try {
            setStaticData(LoginActivity.sharedPreferences.getString("allTenants",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        getContext().startService(new Intent(getContext(),GetAllTenantsService.class));
    }
}
