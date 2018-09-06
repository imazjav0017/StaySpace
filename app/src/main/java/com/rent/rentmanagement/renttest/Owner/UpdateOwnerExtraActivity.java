package com.rent.rentmanagement.renttest.Owner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.AsyncTasks.AddBuildingTask;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateOwnerExtraActivity extends AppCompatActivity {
EditText buildingName,address,floors;
    String buildingVal,addressVal,floorVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_update_owner_extra);
        buildingName=(EditText)findViewById(R.id.updateOwnerBuildingNameInput);
        address=(EditText)findViewById(R.id.updateOwnerAddressInput);
        floors=(EditText)findViewById(R.id.updateOwnerFloorsInput);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Update Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    }
    public void update(View v)
    {
        Log.i("update","1");
        buildingVal=buildingName.getText().toString();
        addressVal=address.getText().toString();
        floorVal=floors.getText().toString();
        if(buildingVal.matches("") || addressVal.matches("")||floorVal.matches(""))
        {
            Toast.makeText(getApplicationContext(), "Missing Fields!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            JSONObject data=new JSONObject();
            try {
                data.put("name",buildingVal);
                data.put("address",addressVal);
                data.put("floor",floorVal);
                data.put("ownerId", LoginActivity.sharedPreferences.getString("ownerId",null));
                data.put("auth",LoginActivity.sharedPreferences.getString("token",null));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AddBuildingTask task=new AddBuildingTask(getApplicationContext(), new AddBuildingTask.AddBuildingResp() {
                @Override
                public void processFinish(Boolean output) {
                    if(output==true)
                    {//will execute when the process completes
                        onBackPressed();
                    }
                }
            });
            task.execute("https://sleepy-atoll-65823.herokuapp.com/build/addBuild",data.toString());
        }
    }
}
