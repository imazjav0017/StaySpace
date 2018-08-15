package com.rent.rentmanagement.renttest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.DataModels.TenantRequestModel;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TenantRequestAdapter extends RecyclerView.Adapter<TenantRequestAdapter.ViewHolder>{

List<TenantRequestModel>tenantRequestModels;

    public TenantRequestAdapter(List<TenantRequestModel> tenantRequestModels) {
        this.tenantRequestModels = tenantRequestModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_tenant_request_list_item,parent,false);
       return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TenantRequestModel model=tenantRequestModels.get(position);
        holder.name.setText(model.getTenantname());
        holder.roomNo.setText("Room no "+model.getRoomNo());
        final JSONObject requestResponse=new JSONObject();
        try {
            requestResponse.put("auth", LoginActivity.sharedPreferences.getString("token",null));
            //requestResponse.put("roomId",model.getRoomId());
            requestResponse.put("tenantId",model.getTenantId());
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Calling","phno");
            }
        });
    }

    @Override
    public int getItemCount() {
        return tenantRequestModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        Context context;
        TextView name,roomNo;
        Button accept,reject,call,message;
        public ViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            name=(TextView)itemView.findViewById(R.id.studentRequestName);
            roomNo=(TextView)itemView.findViewById(R.id.requestingRoomNo);
            accept=(Button)itemView.findViewById(R.id.acceptRequestBtn);
            reject=(Button)itemView.findViewById(R.id.rejectRequestButton);
            call=(Button)itemView.findViewById(R.id.callRequestingButton);
            message=(Button)itemView.findViewById(R.id.messageRequestButton);
        }
    }
}
