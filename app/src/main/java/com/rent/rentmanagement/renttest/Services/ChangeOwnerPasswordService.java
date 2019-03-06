package com.rent.rentmanagement.renttest.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.Owner.UpdateOwnerProfileActivity;
import com.rent.rentmanagement.renttest.Tenants.EditProfileActivity;
import com.rent.rentmanagement.renttest.Tenants.Services.EditProfileTenantService;

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

public class ChangeOwnerPasswordService extends IntentService {

    public ChangeOwnerPasswordService() {
        super("ChangeOwnerPasswordService");
    }
    String np,op;
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            np=intent.getStringExtra("np");
            op=intent.getStringExtra("op");
            try {
                startTask();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    void startTask() throws JSONException {
        if(np!=null &&op!=null)
        {
            String auth= LoginActivity.sharedPreferences.getString("token",null);
            if(auth!=null)
            {
                JSONObject data=new JSONObject();
                data.put("oldPassword",op);
                data.put("newPassword",np);
                data.put("verifyPassword",np);
                data.put("auth",auth);
                ChangeTask task=new ChangeTask();
                task.execute(LoginActivity.URL+"/users/change_password",data.toString());
            }
        }
    }

    class ChangeTask extends AsyncTask<String,Void,String> {

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
                Log.i("CHANGEDATA", params[1]);
                int resp = connection.getResponseCode();
                Log.i("CHANGEDATA", String.valueOf(resp));
                if (resp == 200) {
                    String response = getResponse(connection);
                    return response;
                }
               else
                {
                    return "400";
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
            boolean isOwner=false;
            if(UpdateOwnerProfileActivity.progressDialog!=null) {
                UpdateOwnerProfileActivity.progressDialog.dismiss();
                isOwner=true;
            }
             if(EditProfileActivity.progressDialog!=null) {
                 EditProfileActivity.progressDialog.dismiss();
                 isOwner=false;
             }
            if (s != null) {
                if(s.equals("400"))
                {
                    Toast.makeText(ChangeOwnerPasswordService.this, "Old Password Is Invalid Or New And Old Passwords Are Same", Toast.LENGTH_LONG).show();
                }
                else {
                    if(isOwner) {
                        Log.i("isOWnee","y");
                        if (UpdateOwnerProfileActivity.resetDialog != null) {
                            UpdateOwnerProfileActivity.resetDialog.dismiss();
                        }
                    }
                    else
                    {
                        Log.i("isTen","y");
                        if (EditProfileActivity.resetDialog != null)
                        {
                            EditProfileActivity.resetDialog.dismiss();
                        }
                    }
                    Log.i("CHANGEDATA", s);
                    Toast.makeText(ChangeOwnerPasswordService.this, "Saved !", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ChangeOwnerPasswordService.this, "No Internet!", Toast.LENGTH_SHORT).show();
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


