package com.mansa.StaySpace.Tenants.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Tenants.AvailableRoomsActivity;
import com.mansa.StaySpace.Tenants.DataModels.AvailableRoomModel;
import com.mansa.StaySpace.Tenants.DataModels.BuildingModel;

import java.util.ArrayList;
import java.util.List;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.ViewHolder> {
    List<BuildingModel>buildingModels;

    public BuildingAdapter(List<BuildingModel> buildingModels) {
        this.buildingModels = buildingModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.tenant_avaialble_building_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BuildingModel buildingModel=buildingModels.get(position);
        holder.name.setText(buildingModel.getName());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(holder.context, AvailableRoomsActivity.class);
                i.putExtra("buildingId",buildingModel.getId());
                i.putExtra("buildingName",buildingModel.getName());
                holder.context.startActivity(i);
            }
        });

    }
    public void setFilter(List<BuildingModel>filteredList)
    {
        buildingModels=new ArrayList<>();
        buildingModels.addAll(filteredList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return buildingModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        Context context;
        TextView name;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            name=itemView.findViewById(R.id.availableBuildingNameTV);
            img=itemView.findViewById(R.id.availableBuildingImg);
        }
    }
}
