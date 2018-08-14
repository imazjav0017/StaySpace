package com.rent.rentmanagement.renttest.Tenants.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.DataModels.AvailableRoomModel;
import com.rent.rentmanagement.renttest.DataModels.RoomModel;
import com.rent.rentmanagement.renttest.R;

import java.util.List;

public class AvailableRoomsAdapter extends RecyclerView.Adapter<AvailableRoomsAdapter.ViewHolder>{
    List<AvailableRoomModel> roomModelList;

    public AvailableRoomsAdapter(List<AvailableRoomModel> roomModelList) {
        this.roomModelList = roomModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.available_rooms_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AvailableRoomModel model=roomModelList.get(position);
        holder.roomNo.setText("RoomNo. "+model.getRoomNo());
        holder.roomType.setText(", "+model.getRoomType());
        holder.rent.setText(" \u20B9"+model.getRoomRent());
        holder.ownerName.setText("Mr./Mrs."+model.getOwnerName());
        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("sending ","request");
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
