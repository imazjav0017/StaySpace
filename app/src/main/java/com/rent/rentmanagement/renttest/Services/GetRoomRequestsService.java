package com.rent.rentmanagement.renttest.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.rent.rentmanagement.renttest.DataModels.TenantRequestModel;
import com.rent.rentmanagement.renttest.Fragments.TenantRequestListFragment;
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
public class GetRoomRequestsService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.rent.rentmanagement.renttest.action.FOO";
    private static final String ACTION_BAZ = "com.rent.rentmanagement.renttest.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.rent.rentmanagement.renttest.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.rent.rentmanagement.renttest.extra.PARAM2";

    public GetRoomRequestsService() {
        super("GetRoomRequestsService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, GetRoomRequestsService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                setJsonData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * Handle action Foo in the provided background thread with the provided
         * parameters.
         */
    }
    void setJsonData() throws JSONException {
        JSONObject object=new JSONObject();
        String auth=LoginActivity.sharedPreferences.getString("token",null);
        String ownerId=LoginActivity.sharedPreferences.getString("ownerId",null);
        object.put("auth",auth);
        object.put("ownerId",ownerId);
        GetRoomRequestsTask task=new GetRoomRequestsTask();
        task.execute("https://sleepy-atoll-65823.herokuapp.com/users/getRoomRequest",object.toString());


    }
    public static ArrayList<TenantRequestModel> tenantRequestModels;
    //store RoomRequests in a static arrayList
    public static void setRequestsData(String s) throws JSONException {
        tenantRequestModels=new ArrayList<>();
        JSONObject object=new JSONObject(s);
        String _id,name,roomNo,studentId,phoneNo,emailId;
        JSONArray roomRequest=object.getJSONArray("roomRequest");
        tenantRequestModels.clear();
        for(int i=0;i<roomRequest.length();i++)
        {
            JSONObject mainObject=roomRequest.getJSONObject(i);
            _id=mainObject.getString("_id");
            JSONObject tenantDetails=mainObject.getJSONObject("tenantDetail");
            studentId=tenantDetails.getString("_id");
            phoneNo=tenantDetails.getString("mobileNo");
            emailId=tenantDetails.getString("email");

            JSONObject nameObject=tenantDetails.getJSONObject("name");
            name=nameObject.getString("firstName")+" "+nameObject.getString("lastName");
            JSONObject roomDetails=mainObject.getJSONObject("roomDetail");
            roomNo=roomDetails.getString("roomNo");
            tenantRequestModels.add(new TenantRequestModel(name,studentId,roomNo,_id,phoneNo,emailId));
        }
        TenantRequestListFragment.upateView();

    }

    public static void removeElement(int i)
    {
        tenantRequestModels.remove(i);
        TenantRequestListFragment.upateView();
    }
    class GetRoomRequestsTask extends AsyncTask<String,Integer,String> {

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
                Log.i("GETROOMREQUESTSDATA", params[1]);
                int resp = connection.getResponseCode();
                Log.i("GETROOMREQUESTSRESP",String.valueOf(resp));
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
                Log.i("GETROOMREQUESTSRESP", s);
                try {
                    GetRoomRequestsService.setRequestsData(s);
                } catch (JSONException e) {
                    Log.i("JSONException",e.toString());
                }
            }
            else
            {
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
