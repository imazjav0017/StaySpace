package com.mansa.StaySpace.Owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.mansa.StaySpace.Adapters.BuildingListAdapter;
import com.mansa.StaySpace.DataModels.BuildingListModel;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectBuildingActivity extends AppCompatActivity {
    RecyclerView buildingsList;
    static List<BuildingListModel> buildingListModels;
    static BuildingListAdapter adapter;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_select_building);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("My Buildings");
        buildingsList = (RecyclerView) findViewById(R.id.buildingNamesRv);
        buildingListModels = new ArrayList<>();
        adapter = new BuildingListAdapter(buildingListModels);
        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        buildingsList.setLayoutManager(manager);
        buildingsList.setHasFixedSize(true);
        buildingsList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }
    static public void update()
    {
        if(buildingListModels!=null)
            setData();
    }
    public void addBuilding(View v)
    {
        Log.i("addBuilding","clicked");
        Intent i=new Intent(getApplicationContext(),UpdateOwnerExtraActivity.class);
        startActivity(i);
    }

    static void setData() {
        buildingListModels.clear();
        String Buildings = LoginActivity.sharedPreferences.getString("buildings", null);
        if (Buildings != null) {
            try {
                JSONArray buildingArray = new JSONArray(Buildings);
                if (buildingArray.length() > 0) {
                    for(int i=0;i<buildingArray.length();i++)
                    {
                        JSONObject buildingObject = buildingArray.getJSONObject(i);
                        String buildId = buildingObject.getString("_id");
                        String buildName=buildingObject.getString("name");
                        buildingListModels.add(
                                new BuildingListModel(R.drawable.building, buildName,buildId));
                    }
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
}
