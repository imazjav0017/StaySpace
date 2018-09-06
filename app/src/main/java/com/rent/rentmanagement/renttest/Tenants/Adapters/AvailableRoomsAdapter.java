package com.rent.rentmanagement.renttest.Tenants.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.Tenants.DataModels.AvailableRoomModel;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Tenants.Async.RoomRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AvailableRoomsAdapter extends RecyclerView.Adapter<AvailableRoomsAdapter.ViewHolder>{
    List<AvailableRoomModel> roomModelList;

    public AvailableRoomsAdapter(List<AvailableRoomModel> roomModelList) {
        this.roomModelList = roomModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.tenant_available_rooms_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AvailableRoomModel model=roomModelList.get(position);
        holder.roomNo.setText(model.getBuildingName()+" ,"+
                model.getFloors()+" floors ,roomNo. "+model.getRoomNo());
        holder.roomType.setText(", "+model.getRoomType());
        holder.rent.setText(" \u20B9"+model.getRoomRent());
        holder.ownerName.setText("Mr./Mrs."+model.getOwnerName());
        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("sending ","request");
                //making JSON with auth, tenantName,roomId
                JSONObject requestObject=new JSONObject();
                try {
                    String auth=LoginActivity.sharedPreferences.getString("token",null);
                    String _id=LoginActivity.sharedPreferences.getString("tenantId",null);
                    if(auth!=null)
                    requestObject.put("auth",auth);
                    requestObject.put("roomId",model.get_id());
                    requestObject.put("ownerId",model.getOwner_id());
                    requestObject.put("buildingId",model.getBuildingId());
                    requestObject.put("tenantId",_id);
                    RoomRequestTask task=new RoomRequestTask(holder.context);
                    task.execute("https://sleepy-atoll-65823.herokuapp.com/students/sendRoomRequest",requestObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView roomNo,roomType,rent,ownerName;
        Button request;
        Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            roomNo=(TextView)itemView.findViewById(R.id.availableRoomNo);
            roomType=(TextView)itemView.findViewById(R.id.availableRoomType);
            rent=(TextView)itemView.findViewById(R.id.availableRoomRent);
            ownerName=(TextView)itemView.findViewById(R.id.ownerNameAvailableRooms);
            request=(Button)itemView.findViewById(R.id.availableRoomRequestBtn);
        }
    }
}
