package com.rent.rentmanagement.renttest.Tenants.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.Tenants.TenantFragments.TenantProfileFragment;

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
public class GetTenantHomeService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.rent.rentmanagement.renttest.Tenants.Services.action.FOO";
    private static final String ACTION_BAZ = "com.rent.rentmanagement.renttest.Tenants.Services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.rent.rentmanagement.renttest.Tenants.Services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.rent.rentmanagement.renttest.Tenants.Services.extra.PARAM2";

    public GetTenantHomeService() {
        super("GetTenantHomeService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            GetTenantHomeTask task=new GetTenantHomeTask();
            JSONObject data=new JSONObject();
            try {
                data.put("auth",LoginActivity.sharedPreferences.getString("token",null));
                data.put("roomId",LoginActivity.sharedPreferences.getString("tenantRoomId",null));
                data.put("tenantId", LoginActivity.sharedPreferences.getString("tenantId",null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            task.execute("https://sleepy-atoll-65823.herokuapp.com/students/tenantHome",data.toString());
        }
    }
     void save(String s) {
         JSONObject mainObject = null;
         JSONObject tenantDetail =null;
         try {
             mainObject = new JSONObject(s);
             tenantDetail= mainObject.getJSONObject("tenantDetail");

         } catch (JSONException e) {
             e.printStackTrace();
         }
         finally {
             LoginActivity.sharedPreferences.edit().putString("tenantDetail", tenantDetail.toString()).apply();
             try {
                 TenantProfileFragment.setData();
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
         try {
            JSONObject roomDetails = mainObject.getJSONObject("roomDetail");
            JSONObject paymentDetail = mainObject.getJSONObject("payment");
            JSONArray sentRoomRequestList = mainObject.getJSONArray("sentRoomRequest");
            JSONObject roomateObject = mainObject.getJSONObject("roommate");
            LoginActivity.sharedPreferences.edit().putString("tenantRoomDetails", roomDetails.toString()).apply();
            LoginActivity.sharedPreferences.edit().putString("tenantPaymentDetail", paymentDetail.toString()).apply();
            LoginActivity.sharedPreferences.edit().putString("sentRoomRequestList", sentRoomRequestList.toString()).apply();
            LoginActivity.sharedPreferences.edit().putString("roomateObject", roomateObject.toString()).apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }

     }

    class GetTenantHomeTask extends AsyncTask<String,Void,String> {

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
                Log.i("GetTenantHomeData", params[1]);
                int resp = connection.getResponseCode();
                Log.i("GetTenantHomeResp", String.valueOf(resp));
                if (resp == 200) {
                    String response = getResponse(connection);
                    return response;
                } else {
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
                Log.i("GetTenantHomeResp",s);
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
