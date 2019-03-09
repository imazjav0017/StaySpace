package com.mansa.StaySpace.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mansa.StaySpace.DataModels.AddedRoomModel;
import com.mansa.StaySpace.R;

import java.util.List;

public class AddedRoomsAdapter extends RecyclerView.Adapter<AddedRoomsAdapter.ViewHolder>  {
    List<AddedRoomModel>roomModels;

    public AddedRoomsAdapter(List<AddedRoomModel> roomModels) {
        this.roomModels = roomModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_added_rooms_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AddedRoomModel model=roomModels.get(position);
        holder.roomNo.setText(model.getRoomNo());
        holder.rent.setText("\u20B9 "+model.getRoomRent());
        holder.buildingName.setText(model.getBuildingName());


        String roomType=model.getRoomType();
        switch(roomType)
        {
            case "Single":
                roomType="x1";
                break;
            case "Double":
                roomType="x2";
                break;
            case "Triple":
                roomType="x3";
                break;
            default:
                roomType=model.getRoomType();
        }
        holder.roomType.setText(roomType);
    }

    @Override
    public int getItemCount() {
        return roomModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView roomNo,roomType,rent,buildingName;
        Context context;
        public ViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            roomNo=(TextView)itemView.findViewById(R.id.addedRoomNo);
            roomType=(TextView)itemView.findViewById(R.id.addedRoomType);
            rent=(TextView)itemView.findViewById(R.id.addedRoomRent);
            buildingName=(TextView)itemView.findViewById(R.id.addedRoomBuilding);
        }
    }
}
