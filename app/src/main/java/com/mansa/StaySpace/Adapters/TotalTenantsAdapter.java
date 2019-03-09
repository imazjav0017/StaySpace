package com.mansa.StaySpace.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mansa.StaySpace.R;
import com.mansa.StaySpace.DataModels.StudentModel;
import com.mansa.StaySpace.Owner.studentProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imazjav0017 on 17-03-2018.
 */

public class TotalTenantsAdapter extends RecyclerView.Adapter<TotalTenantsAdapter.TotalTenantsHolder> {
    List<StudentModel> studentModels;

    public TotalTenantsAdapter(List<StudentModel> studentModels) {
        this.studentModels = studentModels;
    }

    @Override
    public TotalTenantsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_all_tenants_item,parent,false);
        return new TotalTenantsHolder(v);
    }

    @Override
    public void onBindViewHolder(final TotalTenantsHolder holder, int position) {
        final StudentModel model=studentModels.get(position);
        holder.studentName.setText(model.getName());
        holder.phNo.setText("Room No "+model.getRoomNo());
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:"+model.getPhNo()));
                holder.context.startActivity(i);

            }
        });
        holder.sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = model.getPhNo();
                String msg="Hi,"+model.getName()+" Your Rent Is Due, Please Pay As Soon As Possible From "+"Your Room Owner";                   // The number on which you want to send SMS
                holder.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)).putExtra("sms_body",msg));
            }
        });
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(holder.context,studentProfile.class);
                i.putExtra("name",model.getName());
                i.putExtra("id",model.get_id());
                i.putExtra("roomNo",model.getRoomNo());
                i.putExtra("roomId",model.getRoomId());
                i.putExtra("isTenant",model.isTenant());
                i.putExtra("aadharNo",model.getAadharNo());
                i.putExtra("phNo",model.getPhNo());
                i.putExtra("total",true);
                holder.context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return studentModels.size();
    }
    public void setFilter(List<StudentModel> filteredList)
    {
        studentModels=new ArrayList<>();
        studentModels.addAll(filteredList);
        notifyDataSetChanged();
    }

    /**
     * Created by imazjav0017 on 17-03-2018.
     */

    public static class TotalTenantsHolder extends RecyclerView.ViewHolder {
        TextView studentName,phNo;
        Button call,sms;
        Context context;
        RelativeLayout rl;
        public TotalTenantsHolder(View itemView) {
            super(itemView);
            studentName=(TextView)itemView.findViewById(R.id.studentNameTextView2);
            phNo=(TextView)itemView.findViewById(R.id.studentPhoneNumber2);
            call=(Button)itemView.findViewById(R.id.callButton1);
            sms=(Button)itemView.findViewById(R.id.sendTenantSmsButton);
            rl=(RelativeLayout)itemView.findViewById(R.id.viewDetailsRl);
            context=itemView.getContext();

        }
    }
}
