package com.mansa.StaySpace.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mansa.StaySpace.DataModels.RoomModel;
import com.mansa.StaySpace.Fragments.RoomsFragment;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.Owner.MainActivity;

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
public class GetRoomsService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.rent.rentmanagement.renttest.Services.action.FOO";
    private static final String ACTION_BAZ = "com.rent.rentmanagement.renttest.Services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.rent.rentmanagement.renttest.Services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.rent.rentmanagement.renttest.Services.extra.PARAM2";

    public GetRoomsService() {
        super("GetRoomsService");
    }
     int buildingIndex;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                buildingIndex= LoginActivity.sharedPreferences.getInt("buildingIndex",0);
                startTask();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    void startTask() throws JSONException {
        String buildId=null;
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
            data.put("auth",auth);
            data.put("buildingId",buildId);
            data.put("ownerId",ownerId);
            GetRoomsTask task = new GetRoomsTask();
            task.execute(LoginActivity.MAINURL+"/rooms/getRooms",data.toString());
        }
    }
    public static ArrayList<RoomModel> tRooms;
    public static ArrayList<RoomModel> eRooms;
    public static ArrayList<RoomModel> oRooms;
    void setData(String s) throws JSONException {
        LoginActivity.sharedPreferences.edit().putString("getRoomsResp",s).apply();
        tRooms=new ArrayList<>();
        eRooms=new ArrayList<>();
        oRooms=new ArrayList<>();
        tRooms.clear();
        eRooms.clear();
        oRooms.clear();
        JSONArray mainArray=new JSONArray(s);
        for(int i=0;i<mainArray.length();i++)
        {
            JSONObject mainObject=mainArray.getJSONObject(i);
            String roomId=mainObject.getString("_id");
            String roomType=mainObject.getString("roomType");
            String roomRent=mainObject.getString("roomRent");
            String roomNo=mainObject.getString("roomNo");
            boolean isEmpty=mainObject.getBoolean("isEmpty");
            int roomCapacity=mainObject.getInt("roomCapacity");
            int totalRoomsCapacity=mainObject.getInt("totalRoomCapacity");
            if(isEmpty==true)
            {
                //empty rooms
                String emptyDays=mainObject.getString("emptyDays");
                String checkOutDate=mainObject.getString("checkOutDate");
                eRooms.add(new RoomModel(roomType,roomNo,roomRent,roomId,checkOutDate,isEmpty,emptyDays,roomCapacity,totalRoomsCapacity));
                tRooms.add(new RoomModel(roomType,roomNo,roomRent,roomId,checkOutDate,isEmpty,emptyDays,roomCapacity,totalRoomsCapacity));
            }
            else
            {
                boolean isRentDue=mainObject.getBoolean("isRentDue");
                String dueAmount=String.valueOf(mainObject.getInt("dueAmount"));
                String dueDays=mainObject.getString("dueDays");
                String dueDate=mainObject.getString("dueDate");
                String checkInDate=mainObject.getString("checkInDate");
                tRooms.add(new RoomModel(roomType,roomNo,roomRent,dueAmount,roomId,checkInDate,
                        dueDate,isEmpty,isRentDue,dueDays,roomCapacity,totalRoomsCapacity));
                if(isRentDue==true)
                {
                    oRooms.add(new RoomModel(roomType,roomNo,roomRent,dueAmount,roomId,checkInDate, dueDate,isEmpty,isRentDue,dueDays,roomCapacity,totalRoomsCapacity));
                }
            }
        }
        RoomsFragment.updateView();
        RoomsFragment.showProgress(false);



    }
    public class GetRoomsTask extends AsyncTask<String,Integer,String>
    {
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            RoomsFragment.progressBar.setProgress(values[0]);
        }
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
                Log.i("GETROOMSDATA", params[1]);
                int resp = connection.getResponseCode();
                Log.i("GETROOMSRESP",String.valueOf(resp));
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
            super.onPostExecute(s);
            if (s != null) {
                Log.i("GETROOMSRESP", s);
                try {

                    setData(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                RoomsFragment.showProgress(false);
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
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



}
