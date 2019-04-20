package com.mansa.StaySpace.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.Owner.SelectBuildingActivity;
import com.mansa.StaySpace.Owner.SwitchBuildingActivity;

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

public class GetBuilidngsService extends IntentService {

    public GetBuilidngsService() {
        super("GetBuilidngsService");
    }
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
        JSONObject data=new JSONObject();
        data.put("ownerId", LoginActivity.sharedPreferences.getString("ownerId", null));
        data.put("auth", LoginActivity.sharedPreferences.getString("token", null));
        GetAllBuildingsTask task=new GetAllBuildingsTask();
        task.execute(LoginActivity.MAINURL+"/build/getAllBuilding",data.toString());
    }
    class GetAllBuildingsTask extends AsyncTask<String,Void,String> {

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
                Log.i("GetAllBuildingsTaskData", params[1]);
                int resp = connection.getResponseCode();
                Log.i("GetAllBuildingsTaskResp", String.valueOf(resp));
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
            if (s != null) {
                Log.i("GetAllBuildingsTaskResp",s);
                try {
                    JSONObject mo=new JSONObject(s);
                    JSONArray bu=mo.getJSONArray("building");
                    LoginActivity.sharedPreferences.edit().putString("buildings",bu.toString()).apply();
                    SwitchBuildingActivity.update();
                    SelectBuildingActivity.update();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
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
