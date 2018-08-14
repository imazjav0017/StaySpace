package com.rent.rentmanagement.renttest.Tenants.TenantFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rent.rentmanagement.renttest.DataModels.AvailableRoomModel;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Tenants.Adapters.AvailableRoomsAdapter;
import com.rent.rentmanagement.renttest.Tenants.Async.GetAvailableRoomsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AvailableRoomsFragment extends Fragment {
    View v;
    RecyclerView availableRoomsList;
    Context context;
    public static ArrayList<AvailableRoomModel>availableRooms;
    static AvailableRoomsAdapter adapter;

    public AvailableRoomsFragment() {
    }

    @SuppressLint("ValidFragment")
    public AvailableRoomsFragment(Context context) {
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.available_rooms_fragment,container,false);
        availableRoomsList=(RecyclerView)v.findViewById(R.id.availableRoomsList);
        availableRooms=new ArrayList<>();
        adapter=new AvailableRoomsAdapter(availableRooms);
        LinearLayoutManager lm=new LinearLayoutManager(context);
        availableRoomsList.setLayoutManager(lm);
        availableRoomsList.setHasFixedSize(true);
        availableRoomsList.setAdapter(adapter);
        return  v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("in 2","frag");
        try {
            getData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData() throws JSONException {
        JSONObject obj=new JSONObject();
        String auth= LoginActivity.sharedPreferences.getString("token",null);
        if(auth!=null) {
            obj.put("auth", auth);
            GetAvailableRoomsTask task = new GetAvailableRoomsTask(context);
            task.execute("https://sleepy-atoll-65823.herokuapp.com/students/getEmptyRooms", obj.toString());
        }
        }
    public static void setData(String s,Context c) throws JSONException
    {
        if(s!=null)
        {
            JSONObject response=new JSONObject(s);
            availableRooms.clear();
            JSONArray room=response.getJSONArray("room");
            if(room.length()>0)
            {
             for(int i=0;i<room.length();i++)
             {
                 JSONObject object=room.getJSONObject(i);
                 JSONObject userDetails=object.getJSONObject("user");
                 String userId=userDetails.getString("_id");
                 String ownerName=userDetails.getString("name");
                 availableRooms.add(new AvailableRoomModel(object.getString("roomType"),
                         object.getString("roomNo"),String.valueOf(object.getInt("roomRent")),
                         object.getString("_id"),userId,ownerName));
             }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
