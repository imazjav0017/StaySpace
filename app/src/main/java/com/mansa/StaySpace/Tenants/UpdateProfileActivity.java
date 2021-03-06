package com.mansa.StaySpace.Tenants;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mansa.StaySpace.DataCallBack;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Tenants.Async.TenantUpdateTask;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateProfileActivity extends AppCompatActivity {
    EditText adhaarNoInput;
    boolean sendingRequest;
    ProgressDialog progressDialog;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenant_activity_update_profile);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Owner Name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        if(adhaarNo.equals(""))
        {
            Toast.makeText(this, "Field Cant Be Empty!", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog=new ProgressDialog(UpdateProfileActivity.this);
            progressDialog.setTitle("Updating..");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMax(100);
            progressDialog.setMessage("Updating Details");
            JSONObject data = new JSONObject();
            data.put("auth", LoginActivity.sharedPreferences.getString("token", null));
            data.put("tenantId", LoginActivity.sharedPreferences.getString("tenantId", null));
            data.put("adharNo", adhaarNo);
            progressDialog.show();
            TenantUpdateTask task = new TenantUpdateTask(getApplicationContext(), new DataCallBack() {
                @Override
                public void datacallBack(String result, boolean response) {
                    if (response) {
                        progressDialog.dismiss();
                        onBackPressed();
                    }
                }
            });
            task.execute(LoginActivity.MAINURL+"/students/editTenantProfile", data.toString());
        }
    }
}
