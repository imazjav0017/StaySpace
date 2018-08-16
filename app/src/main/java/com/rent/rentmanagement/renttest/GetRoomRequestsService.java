package com.rent.rentmanagement.renttest;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.rent.rentmanagement.renttest.AsyncTasks.GetRoomRequestsTask;
import com.rent.rentmanagement.renttest.DataModels.TenantRequestModel;
import com.rent.rentmanagement.renttest.Fragments.TenantRequestListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, GetRoomRequestsService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            JSONObject object=new JSONObject();
            if(LoginActivity.sharedPreferences.getString("token",null)!=null)
            {
                try {
                    object.put("auth",LoginActivity.sharedPreferences.getString("token",null));
                    GetRoomRequestsTask task=new GetRoomRequestsTask(getApplicationContext());
                    task.execute("https://sleepy-atoll-65823.herokuapp.com/students/getRoomRequest",object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Handle action Foo in the provided background thread with the provided
         * parameters.
         */
    }
    public static ArrayList<TenantRequestModel> tenantRequestModels;
    //store RoomRequests in a static arrayList
    public static void setRequestsData(String s) throws JSONException {
        tenantRequestModels=new ArrayList<>();
        JSONObject object=new JSONObject(s);
        String _id,name,roomNo;
        JSONArray tenants=object.getJSONArray("tenant");
        JSONArray roomNoArray=object.getJSONArray("roomNo");
        tenantRequestModels.clear();
        for(int i=0;i<tenants.length();i++)
        {
            JSONObject tenantDetails=tenants.getJSONObject(i);
            roomNo=roomNoArray.getString(i);
            _id=tenantDetails.getString("_id");
            name=tenantDetails.getString("name");
            tenantRequestModels.add(new TenantRequestModel(name,_id,roomNo));
        }
        TenantRequestListFragment.upateView();

    }
}
