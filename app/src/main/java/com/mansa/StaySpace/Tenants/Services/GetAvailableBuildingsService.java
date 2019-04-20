package com.mansa.StaySpace.Tenants.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.Tenants.DataModels.BuildingModel;
import com.mansa.StaySpace.Tenants.EditProfileActivity;
import com.mansa.StaySpace.Tenants.TenantFragments.AvailableBuildingsFragment;

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
import java.net.URL;
import java.util.ArrayList;

public class GetAvailableBuildingsService extends IntentService {

    public GetAvailableBuildingsService() {
        super("GetAvailableBuildingsService");
    }
   public static ArrayList<BuildingModel>buildingModels;
    public static boolean failed=false;
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                startTask();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    void startTask() throws JSONException {

            String auth= LoginActivity.sharedPreferences.getString("token",null);
            if(auth!=null)
            {
                JSONObject data=new JSONObject();
                data.put("auth",auth);
                BuildingTask task=new BuildingTask();
                task.execute(LoginActivity.MAINURL+"/students/getEmptyBuildings",data.toString());
            }
        }

        void setData(String s) throws JSONException {
            if(s!=null) {
                buildingModels = new ArrayList<>();
                JSONObject buildings=new JSONObject(s);
                JSONArray mainArray=buildings.getJSONArray("buildings");
                for(int i=0;i<mainArray.length();i++)
                {
                    JSONObject mainObject=mainArray.getJSONObject(i);
                    String name=mainObject.getString("name");
                    String id=mainObject.getString("_id");
                    buildingModels.add(new BuildingModel(id,name));
                }
                AvailableBuildingsFragment.updateNow();
            }
        }


    class BuildingTask extends AsyncTask<String,Void,String> {

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
                Log.i("BUILDINGDATA", params[1]);
                int resp = connection.getResponseCode();
                Log.i("BUILDINGRESP", String.valueOf(resp));
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
            if(EditProfileActivity.EProgressDialog!=null)
                EditProfileActivity.EProgressDialog.dismiss();
            if (s != null) {
                failed=false;
                Log.i("BUILDINGRESP",s);
                try {
                    setData(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                failed=true;
                Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
                AvailableBuildingsFragment.updateNow();
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
