package com.rent.rentmanagement.renttest.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.Owner.UpdateOwnerProfileActivity;
import com.rent.rentmanagement.renttest.Tenants.Services.GetTenantHomeService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class EditProfileOwnerService extends IntentService {

    public EditProfileOwnerService() {
        super("EditProfileOwnerService");
    }
    String fName,lName,email,mobNo;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Bundle b=intent.getExtras();
            fName=b.getString("fName");
            lName=b.getString("lName");
            email=b.getString("email");
            mobNo=b.getString("mobNo");
            try {
                startTask();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    void startTask() throws JSONException {

        if(fName!=null && lName!= null && email!=null && mobNo!=null)
        {
            String auth= LoginActivity.sharedPreferences.getString("token",null);
            if(auth!=null)
            {
                JSONObject data=new JSONObject();
                data.put("auth",auth);
                data.put("firstName",fName);
                data.put("lastName",lName);
                data.put("email",email);
                data.put("mobileNo",mobNo);
                EditTask task=new EditTask();
                task.execute(LoginActivity.URL+"/users/ownerProfileEdit",data.toString());
            }
            else
                Log.i("EDITPROFILE","AUTH is null");
        }
    }

    class EditTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("Accept", "application/json");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.connect();
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(params[1]);
                Log.i("EDITPROFILEDATA", params[1]);
                int resp = connection.getResponseCode();
                Log.i("EDITPROFILERESP", String.valueOf(resp));
                if (resp == 200) {
                    String response = getResponse(connection);
                    return response;
                }
                else {
                    return null;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(UpdateOwnerProfileActivity.EProgressDialog!=null)
            {
                UpdateOwnerProfileActivity.EProgressDialog.dismiss();
            }
            if (s != null) {
                Log.i("EDITPROFILERESP",s);
                Toast.makeText(EditProfileOwnerService.this, "Saved!", Toast.LENGTH_SHORT).show();
                startService(new Intent(getApplicationContext(), GetTenantHomeService.class));

            } else {
                Toast.makeText(EditProfileOwnerService.this, "No Internet!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String  getResponse(HttpURLConnection connection)
    {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = in.readLine()) != null) {

                sb.append(line);
                break;
            }

            in.close();
            return sb.toString();
        }catch(Exception e)
        {
            return e.getMessage();
        }
    }
}

