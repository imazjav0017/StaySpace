package com.mansa.StaySpace.Tenants.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.Tenants.DataModels.AvailableRoomModel;
import com.mansa.StaySpace.Tenants.SentRoomRequestActivity;

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

public class SentRequestsService extends IntentService {

   public static ArrayList<AvailableRoomModel>requestList;
   public static boolean failed=false;
    public SentRequestsService() {
        super("SentRequestsService");
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
        String auth= LoginActivity.sharedPreferences.getString("token",null);
        String tenantId=LoginActivity.sharedPreferences.getString("tenantId",null);
        data.put("auth",auth);
        data.put("tenantId",tenantId);
        SentRequestsTask task=new SentRequestsTask();
        task.execute(LoginActivity.URL+"/students/getSentRoomRequest",data.toString());
    }
    void setRequestList(String s) throws JSONException {
        if(s!=null)
        {
            requestList=new ArrayList<>();
            JSONObject mainObject=new JSONObject(s);
            JSONArray mainArray=mainObject.getJSONArray("request");
            for(int i=0;i<mainArray.length();i++)
            {
                JSONObject object=mainArray.getJSONObject(i);
                JSONObject room=object.getJSONObject("roomDetail");
                String roomType=room.getString("roomType");
                String roomRent=room.getString("roomRent");
                String roomNo=room.getString("roomNo");
                String roomId=room.getString("_id");
                String roomCapacity=room.getString("roomCapacity");
                JSONObject building=object.getJSONObject("buildingDetail");
                String buildingId=building.getString("_id");
                String buildingName=building.getString("name");
                String address=building.getString("address");
                String floors=String.valueOf(building.getInt("floor"));
                JSONObject ownerObject=object.getJSONObject("ownerDetail");
                String ownerId=ownerObject.getString("_id");
                String ownerPhoneNo=ownerObject.getString("mobileNo");
                JSONObject ownerNameObject=ownerObject.getJSONObject("name");
                String Ownername=ownerNameObject.getString("firstName")
                        +" "+ownerNameObject.getString("lastName");
                requestList.add(new AvailableRoomModel(roomType,roomNo,roomRent,roomId,ownerId,Ownername,
                        ownerPhoneNo,buildingId,buildingName,floors,address,roomCapacity ));
            }
            SentRoomRequestActivity.updateNow();
        }
    }
   public class SentRequestsTask extends AsyncTask<String,Void,String> {
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
               Log.i("SentRequestsData", params[1]);
               int resp = connection.getResponseCode();
               Log.i("SentRequestsResp", String.valueOf(resp));
               if (resp == 200) {
                   String response = getResponse(connection);
                   return response;
               } else {
                   return null;
               }

           } catch (MalformedURLException e) {
               e.printStackTrace();
               return null;
           } catch (ProtocolException e) {
               e.printStackTrace();
               return null;
           } catch (IOException e) {
               e.printStackTrace();
               return null;
           }

       }

       @Override
       protected void onPostExecute(String s) {
           super.onPostExecute(s);
           if (s != null) {
               failed=false;
               Log.i("SentRequestsResp",s);
               try {
                   setRequestList(s);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           } else {
               failed=true;
               Toast.makeText(SentRequestsService.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
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
