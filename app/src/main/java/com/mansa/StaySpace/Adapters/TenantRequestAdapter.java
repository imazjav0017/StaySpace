package com.mansa.StaySpace.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mansa.StaySpace.AsyncTasks.SendRoomRequestResponseTask;
import com.mansa.StaySpace.DataModels.TenantRequestModel;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.Owner.HomePageActivity;
import com.mansa.StaySpace.Owner.ownerTenantRequestDetails;
import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Services.GetRoomRequestsService;
import com.mansa.StaySpace.Services.ResponseToRoomRequestService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TenantRequestAdapter extends RecyclerView.Adapter<TenantRequestAdapter.ViewHolder>{

List<TenantRequestModel>tenantRequestModels;
   public static ProgressDialog progressDialog;

    public TenantRequestAdapter(List<TenantRequestModel> tenantRequestModels) {
        this.tenantRequestModels = tenantRequestModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_tenant_request_list_item,parent,false);
       return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TenantRequestModel model=tenantRequestModels.get(position);
        holder.name.setText(model.getTenantname());
        holder.roomNo.setText("Room no "+model.getRoomNo());
        holder.checkinDate.setText("Check In Date: "+model.getCheckinDate());

        //clicking the tenant shuld open info
        holder.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(holder.context,ownerTenantRequestDetails.class);
                i.putExtra("name",model.getTenantname());
                i.putExtra("roomNo",model.getRoomNo());
                i.putExtra("tenantId",model.getTenantId());
                i.putExtra("mobileNo",model.getPhoneNo());
                i.putExtra("checkInDate",model.getCheckinDate());
                i.putExtra("requestId",model.get_id());
                i.putExtra("roomId",model.getRoomId());
                holder.context.startActivity(i);

            }
        });


        //building json to send in response
        final JSONObject requestResponse=new JSONObject();
        try {
            int buildingIndex= LoginActivity.sharedPreferences.getInt("buildingIndex",0);
            String buildId=getBuildingId(buildingIndex);
            String ownerId=LoginActivity.sharedPreferences.getString("ownerId",null);
            requestResponse.put("auth", LoginActivity.sharedPreferences.getString("token",null));
            requestResponse.put("ownerId",ownerId);
            requestResponse.put("requestId",model.get_id());
            requestResponse.put("tenantId",model.getTenantId());
            requestResponse.put("roomId",model.getRoomId());
            requestResponse.put("buildingId",buildId);
            requestResponse.put("checkInDate",model.getCheckinDate());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //accept request
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Accept","request");
                try {
                    requestResponse.put("response",true);
                    Intent i=new Intent(holder.context,ResponseToRoomRequestService.class);
                    i.putExtra("data",requestResponse.toString());
                    i.putExtra("response",true);
                    i.putExtra("tenantName",model.getTenantname());
                    progressDialog=new ProgressDialog(holder.context);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMax(100);
                    progressDialog.setMessage("Adding "+model.getTenantname());
                    progressDialog.setTitle("Adding Tenant");
                    progressDialog.show();
                    holder.context.startService(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("rejected","request");
                try {
                    requestResponse.put("response",false);
                    Intent i=new Intent(holder.context,ResponseToRoomRequestService.class);
                    i.putExtra("data",requestResponse.toString());
                    i.putExtra("response",false);
                    i.putExtra("tenantName",model.getTenantname());
                    progressDialog=new ProgressDialog(holder.context);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMax(100);
                    progressDialog.setMessage("Rejecting "+model.getTenantname());
                    progressDialog.setTitle("Rejecting Tenant");
                    progressDialog.show();
                    holder.context.startService(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Calling","phno");
                Intent i=new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:"+model.getPhoneNo()));
                holder.context.startActivity(i);
            }
        });
    }
    String getBuildingId(int index) {
        String buildId=null;
        String Buildings = LoginActivity.sharedPreferences.getString("buildings", null);
        if (Buildings != null) {
            try {
                JSONArray buildingArray = new JSONArray(Buildings);
                if (buildingArray.length() > 0) {
                    JSONObject buildingObject = buildingArray.getJSONObject(index);
                     buildId = buildingObject.getString("_id");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return buildId;
    }

    @Override
    public int getItemCount() {
        return tenantRequestModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        Context context;
        TextView name,roomNo,checkinDate;
        Button accept,reject,call,message;
        RelativeLayout bg;
        public ViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            bg=(RelativeLayout)itemView.findViewById(R.id.tenantRequestListBg);
            name=(TextView)itemView.findViewById(R.id.studentRequestName);
            roomNo=(TextView)itemView.findViewById(R.id.requestingRoomNo);
            checkinDate=(TextView)itemView.findViewById(R.id.requestingRoomDate);
            accept=(Button)itemView.findViewById(R.id.acceptRequestBtn);
            reject=(Button)itemView.findViewById(R.id.rejectRequestButton);
            call=(Button)itemView.findViewById(R.id.callRequestingButton);
            message=(Button)itemView.findViewById(R.id.messageRequestButton);
        }
    }
}
