package com.mansa.StaySpace.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mansa.StaySpace.DataModels.BuildingListModel;
import com.mansa.StaySpace.Owner.EditBuildingActivity;
import com.mansa.StaySpace.Owner.automanualActivity;
import com.mansa.StaySpace.Owner.manualActivity;
import com.mansa.StaySpace.R;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BuildingListModel model=buildingListModels.get(position);
        holder.buildingName.setText(model.getBuildingName());
        holder.buildingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(holder.context,manualActivity.class);
                i.putExtra("buildingId",model.get_id());
                i.putExtra("buildingName",model.getBuildingName());
                holder.context.startActivity(i);

            }
        });
        holder.buildingBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.i("Building On Long Click","yes");
                CharSequence[]items={"Edit","Delete"};
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.context);
                builder.setTitle("Select The Option:");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0)
                        {
                            Log.i("EditBuilding","y");
                            Intent intent=new Intent(holder.context, EditBuildingActivity.class);
                            intent.putExtra("index",position);
                            holder.context.startActivity(intent);
                        }
                        else if(i==1)
                        {
                            Log.i("DeleteBuilding","y");
                        }
                    }
                });
                builder.show();
                return true;
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
