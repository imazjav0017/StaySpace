package com.mansa.StaySpace.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mansa.StaySpace.Fragments.ProfileFragment;
import com.mansa.StaySpace.LoginActivity;

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

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class getOwnerDetailsService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.rent.rentmanagement.renttest.Services.action.FOO";
    private static final String ACTION_BAZ = "com.rent.rentmanagement.renttest.Services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.rent.rentmanagement.renttest.Services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.rent.rentmanagement.renttest.Services.extra.PARAM2";

    public getOwnerDetailsService() {
        super("getOwnerDetailsService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String auth,buildingId=null,ownerId;
            auth= LoginActivity.sharedPreferences.getString("token",null);
            ownerId=LoginActivity.sharedPreferences.getString("ownerId",null);
            String builidngArray=LoginActivity.sharedPreferences.getString("buildings",null);
            if(builidngArray!=null)
            {
                try {
                    JSONArray buildings=new JSONArray(builidngArray);
                    if (buildings.length()>0) {
                        /*JSONObject bObject = buildings.getJSONObject(0);
                        buildingId=bObject.getString("_id");*/
                        JSONObject bObject = buildings.getJSONObject(LoginActivity.sharedPreferences.getInt("buildingIndex",0));
                        buildingId=bObject.getString("_id");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONObject data=new JSONObject();
            try {
                data.put("auth", auth);
                data.put("ownerId", ownerId);
                data.put("buildingId", buildingId);
            }catch (JSONException e)
            {
                e.printStackTrace();
            }


            GetOwnerDetailsTask task=new GetOwnerDetailsTask();
            task.execute(LoginActivity.MAINURL+"/users/ownerHome",data.toString());
        }
    }
    void addToSharedPref(String tag,String value)
    {
        if(tag!=null && value!=null)
        {
            LoginActivity.sharedPreferences.edit().putString(tag,value).apply();
        }
    }
    void save(String s)
    {
        try {
            JSONObject mainObject=new JSONObject(s);
            String totalRooms=String.valueOf(mainObject.getInt("totalRooms"));
            String tenantCount=String.valueOf(mainObject.getInt("tenantCount"));
            String emptyCount=String.valueOf(mainObject.getInt("emptyCount"));
            String occupiedRoomsCount=String.valueOf(mainObject.getInt("occupiedRoomsCount"));
            String totalCollectedAmount=String.valueOf(mainObject.getInt("totalCollectedAmount"));
            String totalRent=String.valueOf(mainObject.getInt("totalRent"));
            String totalDueAmount=String.valueOf(mainObject.getInt("totalDueAmount"));
            addToSharedPref("totalRooms",totalRooms);
            addToSharedPref("emptyCount",emptyCount);
            addToSharedPref("tenantCount",tenantCount);
            addToSharedPref("occupiedRoomsCount",occupiedRoomsCount);
            addToSharedPref("totalCollectedAmount",totalCollectedAmount);
            addToSharedPref("totalRent",totalRent);
            addToSharedPref("totalDueAmount",totalDueAmount);
            ProfileFragment.setData();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
   class GetOwnerDetailsTask extends AsyncTask<String,Void,String> {

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
               Log.i("GETOWNERDETAILSDATA", params[1]);
               int resp = connection.getResponseCode();
               Log.i("GETOWNERDETAILSRESP", String.valueOf(resp));
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
               Log.i("GETOWNERDETAILSRESP",s);
               save(s);
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
