package com.rent.rentmanagement.renttest.Tenants.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.rent.rentmanagement.renttest.Tenants.DataModels.AvailableRoomModel;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.Tenants.Async.GetAvailableRoomsTask;
import com.rent.rentmanagement.renttest.Tenants.TenantFragments.AvailableRoomsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetAvailableRoomsService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.rent.rentmanagement.renttest.Tenants.action.FOO";
    private static final String ACTION_BAZ = "com.rent.rentmanagement.renttest.Tenants.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.rent.rentmanagement.renttest.Tenants.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.rent.rentmanagement.renttest.Tenants.extra.PARAM2";

    public GetAvailableRoomsService() {
        super("GetAvailableRoomsService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, GetAvailableRoomsService.class);
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
        Intent intent = new Intent(context, GetAvailableRoomsService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            startTask();
        }

    }

    void startTask() {
        JSONObject obj = new JSONObject();
        String auth = LoginActivity.sharedPreferences.getString("token", null);
        if (auth != null) {
            try {
                obj.put("auth", auth);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GetAvailableRoomsTask task = new GetAvailableRoomsTask(getApplicationContext());
            task.execute("https://sleepy-atoll-65823.herokuapp.com/students/getEmptyRooms", obj.toString());
        }
    }
    //store the available rooms in static array list
    public static List<AvailableRoomModel> availableRooms;
    public static void setAvailableroomsData(String s,Context c) throws JSONException
    {
        if(s!=null)
        {
            availableRooms=new ArrayList<>();
            JSONObject response=new JSONObject(s);
            availableRooms.clear();
            JSONArray room=response.getJSONArray("room");
            if(room.length()>0)
            {
                for(int i=0;i<room.length();i++)
                {
                    JSONObject object=room.getJSONObject(i);
                    String roomId=object.getString("_id");
                    String roomType=object.getString("roomType");
                    String roomRent=object.getString("roomRent");
                    String roomNo=object.getString("roomNo");
                    JSONObject buildingObject=object.getJSONObject("building");
                    String buildingId=buildingObject.getString("_id");
                    String buildingName=buildingObject.getString("name");
                    String floors=String.valueOf(buildingObject.getInt("floor"));
                    JSONObject ownerObject=object.getJSONObject("owner");
                    String ownerId=ownerObject.getString("_id");
                    String ownerPhoneNo=ownerObject.getString("mobileNo");
                    JSONObject ownerNameObject=ownerObject.getJSONObject("name");
                    String Ownername=ownerNameObject.getString("firstName")
                            +" "+ownerNameObject.getString("lastName");
                    availableRooms.add(new AvailableRoomModel(roomType,roomNo,roomRent,roomId,ownerId,Ownername,
                            ownerPhoneNo,buildingId,buildingName,floors ));
                }
                AvailableRoomsFragment.updateNow();
            }
        }
    }
}

