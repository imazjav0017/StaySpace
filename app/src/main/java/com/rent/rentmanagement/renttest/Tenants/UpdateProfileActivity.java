package com.rent.rentmanagement.renttest.Tenants;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Tenants.Async.TenantUpdateTask;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateProfileActivity extends AppCompatActivity {
    EditText adhaarNoInput;
    boolean sendingRequest;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenant_activity_update_profile);
        Intent i=getIntent();

        //if the user has been made to come to this activity by sending request
        sendingRequest=i.getBooleanExtra("sendingRequest",false);
        adhaarNoInput=(EditText)findViewById(R.id.tenantAdhaarNoUpdate);
        String tenantId= LoginActivity.sharedPreferences.getString("tenantId",null);
        if(tenantId!=null)
        {
            Log.i("id",tenantId);
        }
    }
    public void submit(View v) throws JSONException {
        Log.i("Submit","clicked");
        String adhaarNo=adhaarNoInput.getText().toString();
        JSONObject data=new JSONObject();
        data.put("auth",LoginActivity.sharedPreferences.getString("token",null));
        data.put("tenantId",LoginActivity.sharedPreferences.getString("tenantId",null));
        data.put("adharNo",adhaarNo);
        TenantUpdateTask task=new TenantUpdateTask(getApplicationContext());
        task.execute("https://sleepy-atoll-65823.herokuapp.com/students/editTenantProfile",data.toString());

    }
}
