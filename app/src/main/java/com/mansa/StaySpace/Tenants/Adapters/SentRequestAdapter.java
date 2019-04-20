package com.mansa.StaySpace.Tenants.Adapters;

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

import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Tenants.DataModels.AvailableRoomModel;
import com.mansa.StaySpace.Tenants.SendRequestActivity;

import java.util.List;

public class SentRequestAdapter extends RecyclerView.Adapter<SentRequestAdapter.ViewHolder> {
    List<AvailableRoomModel> availableRoomModelList;

    public SentRequestAdapter(List<AvailableRoomModel> availableRoomModelList) {
        this.availableRoomModelList = availableRoomModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.tenant_sent_request_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AvailableRoomModel model=availableRoomModelList.get(position);
        holder.roomNo.setText(model.getRoomNo());
        holder.buildingName.setText("Building:\n"+model.getBuildingName());
        holder.rent.setText("Rent: \u20B9"+model.getRoomRent());
        holder.ownerName.setText("Owner: Mr./Mrs."+model.getOwnerName());
        holder.bg.setOnClickListener(new View.OnClickListener() {
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
        /*holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("cancel ","request");
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return availableRoomModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        Context context;
        RelativeLayout bg;
        TextView roomNo,buildingName,rent,ownerName;
      //  Button cancel;

        public ViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            bg=(RelativeLayout)itemView.findViewById(R.id.requestRoomBg);
            roomNo=(TextView)itemView.findViewById(R.id.sentRRoomNo);
            buildingName=(TextView)itemView.findViewById(R.id.sentRBuildingName);
            rent=(TextView)itemView.findViewById(R.id.sentRRoomRent);
            ownerName=(TextView)itemView.findViewById(R.id.sentROwnerName);
            //cancel=(Button)itemView.findViewById(R.id.cancelRoomRequestBtn);
        }
    }
}
