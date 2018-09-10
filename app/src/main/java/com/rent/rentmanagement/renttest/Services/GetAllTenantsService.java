package com.rent.rentmanagement.renttest.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.DataModels.StudentModel;
import com.rent.rentmanagement.renttest.Fragments.TenantsFragment;
import com.rent.rentmanagement.renttest.LoginActivity;

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

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetAllTenantsService extends IntentService {


    public GetAllTenantsService() {
        super("GetAllTenantsService");
    }
    int buildingIndex;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            buildingIndex= LoginActivity.sharedPreferences.getInt("buildingIndex",0);
        }
        try {
            startTask();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void startTask() throws JSONException {
        String buildId = null;
        String auth = LoginActivity.sharedPreferences.getString("token", null);
        String ownerId = LoginActivity.sharedPreferences.getString("ownerId", null);
        String Buildings = LoginActivity.sharedPreferences.getString("buildings", null);
        if (Buildings != null) {
            try {
                JSONArray buildingArray = new JSONArray(Buildings);
                if (buildingArray.length() > 0) {
                    JSONObject buildingObject = buildingArray.getJSONObject(buildingIndex);
                    buildId = buildingObject.getString("_id");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject data = new JSONObject();
            data.put("auth", auth);
            data.put("buildingId", buildId);
            data.put("ownerId", ownerId);
            GetTentantsTask task = new GetTentantsTask();
            task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/getAllStudents", data.toString());
        }
    }
    public static ArrayList<StudentModel>studentsList;
    void setData(String s) throws JSONException {
        LoginActivity.sharedPreferences.edit().putString("allTenants",s).apply();
        studentsList = new ArrayList<>();
        studentsList.clear();
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
                studentsList.add(new StudentModel(name, mobileNo, roomNo, studentId, roomId, adharNo));
            }
        }
        for (int i = 0; i < tenantsArray.length(); i++)
        {
            JSONObject tenantObject=tenantsArray.getJSONObject(i);
            String tenantId=tenantObject.getString("_id");
            String mobileNo=tenantObject.getString("mobileNo");
            JSONObject nameObject=tenantObject.getJSONObject("name");
            String name=nameObject.getString("firstName")+" "+nameObject.getString("lastName");
            JSONObject roomObject=tenantObject.getJSONObject("room");
            String roomId=roomObject.getString("_id");
            String roomNo=roomObject.getString("roomNo");
            String adharNo=tenantObject.getString("adharNo");
            studentsList.add(new StudentModel(name, mobileNo, roomNo, tenantId, roomId, adharNo));

        }
        TenantsFragment.updateView();

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
    public class GetTentantsTask extends AsyncTask<String,Void,String>
    {
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
                Log.i("GETALLTENANTSDATA", params[1]);
                int resp = connection.getResponseCode();
                Log.i("GETALLTENANTSRESP",String.valueOf(resp));
                if(resp==200)
                {
                    String response=getResponse(connection);
                    return response;
                }
                else
                {
                    return null;
                }

            }catch(MalformedURLException e)
            {
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
            if (s != null) {
                Log.i("GETALLTENANTSRESP",s);
                try {
                    setData(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
                super.onPostExecute(s);
            }
        }
    }


}
