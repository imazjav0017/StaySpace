package com.rent.rentmanagement.renttest.Tenants.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.Tenants.DataModels.AvailableRoomModel;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Tenants.Async.RoomRequestTask;
import com.rent.rentmanagement.renttest.Tenants.SendRequestActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
        holder.roomNo.setText(model.getRoomNo());
        holder.buildingName.setText(model.getBuildingName());
        holder.rent.setText("Rent: \u20B9"+model.getRoomRent());
        holder.ownerName.setText("Owner: Mr./Mrs."+model.getOwnerName());
        holder.availableRoomsBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("sending ","request");
                Intent i=new Intent(holder.context, SendRequestActivity.class);
                i.putExtra("roomId",model.get_id());
                i.putExtra("ownerId",model.getOwner_id());
                i.putExtra("buildingId",model.getBuildingId());
                i.putExtra("data",model.toString());
                holder.context.startActivity(i);
            }
        });
        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("sending ","request");
                Intent i=new Intent(holder.context, SendRequestActivity.class);
                i.putExtra("roomId",model.get_id());
                i.putExtra("ownerId",model.getOwner_id());
                i.putExtra("buildingId",model.getBuildingId());
                i.putExtra("data",model.toString());
                holder.context.startActivity(i);
            }
        });
    }
    public void setFilter(List<AvailableRoomModel>filteredList)
    {
        roomModelList=new ArrayList<>();
        roomModelList.addAll(filteredList);
        notifyDataSetChanged();
    }
    public void removeFilter(List<AvailableRoomModel> fullList)
    {
        roomModelList=new ArrayList<>();
        roomModelList.addAll(fullList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return roomModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout availableRoomsBg;
        TextView roomNo,buildingName,rent,ownerName,vacancy;
        Button request;
        Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            availableRoomsBg=(RelativeLayout)itemView.findViewById(R.id.availableRoomsBg);
            roomNo=(TextView)itemView.findViewById(R.id.availableRoomNo);
            buildingName=(TextView)itemView.findViewById(R.id.availableBuildingName);
            rent=(TextView)itemView.findViewById(R.id.availableRoomRent);
            ownerName=(TextView)itemView.findViewById(R.id.ownerNameAvailableRooms);
            request=(Button)itemView.findViewById(R.id.availableRoomRequestBtn);
        }
    }
}
