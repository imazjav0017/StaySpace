package com.rent.rentmanagement.renttest.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.DataModels.BuildingListModel;
import com.rent.rentmanagement.renttest.Owner.automanualActivity;
import com.rent.rentmanagement.renttest.R;

import java.util.List;

public class BuildingListAdapter extends RecyclerView.Adapter<BuildingListAdapter.ViewHolder>
{
    List<BuildingListModel>buildingListModels;

    public BuildingListAdapter(List<BuildingListModel> buildingListModels) {
        this.buildingListModels = buildingListModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_building_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BuildingListModel model=buildingListModels.get(position);
        holder.buildingName.setText(model.getBuildingName());
        holder.buildingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(holder.context,automanualActivity.class);
                i.putExtra("buildingId",model.get_id());
                holder.context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return buildingListModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        Context context;
        ImageButton buildingBtn;
        TextView buildingName;
        public ViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            buildingBtn=(ImageButton)itemView.findViewById(R.id.buildingAvatar);
            buildingName=(TextView)itemView.findViewById(R.id.buildingNameTextView);
        }
    }
}
