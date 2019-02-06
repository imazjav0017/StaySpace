package com.rent.rentmanagement.renttest.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.Fragments.RoomsFragment;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.AsyncTasks.PaymentTask;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.DataModels.RoomModel;
import com.rent.rentmanagement.renttest.Owner.roomDetailActivity;
import com.rent.rentmanagement.renttest.Services.PaymentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by imazjav0017 on 12-02-2018.
 */
class ViewHolder2 extends RecyclerView.ViewHolder {
    LinearLayout ll;
    LinearLayout status;
    TextView roomNo;
    TextView roomType;
    TextView amount;
    TextView date;
    Context context;
    Button reason;
    Button collect;
    TextView dueDays;
    public ViewHolder2(View itemView) {
        super(itemView);
        ll=(LinearLayout)itemView.findViewById(R.id.ocRoomLl);
        status=(LinearLayout)itemView.findViewById(R.id.statusRoom);
        context=itemView.getContext();
        date=(TextView)itemView.findViewById(R.id.checkInDate);
        dueDays=(TextView)itemView.findViewById(R.id.dueDays);
        roomNo=(TextView)itemView.findViewById(R.id.roomNoOccupiedop);
        roomType=(TextView)itemView.findViewById(R.id.roomTypeOcc);
        amount=(TextView)itemView.findViewById(R.id.rentToBeCollected);
        reason=(Button)itemView.findViewById(R.id.reason);
        collect=(Button)itemView.findViewById(R.id.collectingButton);
    }
}

public class OccupiedRoomsAdapter extends RecyclerView.Adapter<ViewHolder2> {
    List<RoomModel> roomList;
    Context context;
    JSONObject rentdetails;
    public static  ProgressDialog progressDialog;
    public OccupiedRoomsAdapter(List<RoomModel> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    @Override
    public ViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_occupied_rooms_item,parent,false);
        return new ViewHolder2(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder2 holder, int position) {
        final RoomModel model=roomList.get(position);
        holder.roomNo.setText("Room No. "+model.getRoomNo());
        holder.amount.setText("Due Amount: \u20B9"+model.getDueAmount());
        holder.date.setText(model.getCheckInDate());
        holder.dueDays.setText(model.getDays());
        holder.roomType.setText(", "+model.getRoomType()+",");

        holder.roomType.setText(", "+model.getRoomType()+",");
        if(model.isEmpty==false)
        {
            if(model.getDueAmount().equals(model.getRoomRent()))
            {
                holder.status.setBackgroundColor(Color.parseColor("#D32F2F"));
            }
            else
            {
                holder.status.setBackgroundColor(Color.parseColor("#b2df3e"));
            }
        }
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(holder.context,roomDetailActivity.class);
                i.putExtra("id",model.get_id());
                i.putExtra("roomNo",model.getRoomNo());
                i.putExtra("roomType",model.getRoomType());
                i.putExtra("roomRent",model.getRoomRent());
                i.putExtra("due",model.getDueAmount());
                i.putExtra("roomCapacity",model.getRoomCapacity());
                i.putExtra("totalRoomCapacity",model.getTotalRoomCapacity());
                holder.context.startActivity(i);
            }
        });
        holder.reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                View view=LayoutInflater.from(context).inflate(R.layout.owner_reason_dialog,null,false);
                final EditText input=(EditText)view.findViewById(R.id.reasonInputText);
                final Button btn=(Button)view.findViewById(R.id.reasonButtonDialog);
                builder.setView(view);
                final AlertDialog dialog=builder.create();
                dialog.show();
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn.setClickable(false);
                        String reason=input.getText().toString();
                        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
                        Date dateObj=new Date();
                        final String date=dateFormat.format(dateObj).toString();
                        if(reason.isEmpty()) {
                            enable(btn);
                            Toast.makeText(context, "Please Give A Reason..", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //progress dialog
                            progressDialog=new ProgressDialog(holder.context);
                            progressDialog.setMax(100);
                            progressDialog.setMessage("Saving");
                            progressDialog.setTitle("Reason");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            Intent i=new Intent(holder.context, PaymentService.class);
                            i.putExtra("roomId",model.get_id());
                            i.putExtra("date",date);
                            i.putExtra("reason",reason);
                            i.putExtra("from","occ");
                            i.putExtra("isPayment",false);//indicates that collect pressed from occ
                            holder.context.startService(i);
                            dialog.dismiss();
                            progressDialog.show();
                        }
                    }
                });

            }
        });
        holder.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String>tenants=null;
                String s=LoginActivity.sharedPreferences.getString("allTenants",null);
                if(s!=null)
                {
                    tenants=new ArrayList<>();
                    try {
                        JSONObject tObject=new JSONObject(s);
                        JSONArray tArray=tObject.getJSONArray("tenants");
                        for(int i1=0;i1<tArray.length();i1++)
                        {
                            JSONObject mainObj=tArray.getJSONObject(i1);
                            JSONObject nameObject=mainObj.getJSONObject("name");
                            JSONObject roomObject=mainObj.getJSONObject("room");
                            String roomNo=roomObject.getString("roomNo");
                            if(roomNo.equals(model.getRoomNo()))
                            {
                                tenants.add(nameObject.getString("firstName"));
                            }
                        }
                        JSONArray tStuds=tObject.getJSONArray("student");
                        for(int j=0;j<tStuds.length();j++)
                        {
                            JSONObject mainObj=tStuds.getJSONObject(j);
                            String roomNo=mainObj.getString("roomNo");
                            if(roomNo.equals(model.getRoomNo())) {
                                JSONArray studentsDetailArray = mainObj.getJSONArray("students");
                                for (int j1 = 0; j1 < studentsDetailArray.length(); j1++) {
                                    JSONObject studentDetails = studentsDetailArray.getJSONObject(j1);
                                    String name = studentDetails.getString("name");
                                    tenants.add(name);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                View view=LayoutInflater.from(context).inflate(R.layout.owner_dialog_collect,null,false);
                final Button collectedButton=(Button)view.findViewById(R.id.collectedbutton);
                final EditText rentCollectedInput=(EditText)view.findViewById(R.id.rentcollectedinput);
                final AutoCompleteTextView payee=(AutoCompleteTextView) view.findViewById(R.id.payee);
                ArrayAdapter<String> adapter=null;
                if(tenants!=null) {
                    payee.setText(tenants.get(0));
                    adapter = new ArrayAdapter(holder.context, android.R.layout.select_dialog_item, tenants);
                    payee.setThreshold(1);
                    payee.setAdapter(adapter);
                    payee.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            payee.showDropDown();
                        }
                    });
                }



                rentCollectedInput.setText(model.getDueAmount());
                rentCollectedInput.setSelection(rentCollectedInput.getText().toString().length());
                DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
                Date dateObj=new Date();
               final String date=dateFormat.format(dateObj).toString();
                TextView dateCollected=(TextView)view.findViewById(R.id.datecollectedinput);
                dateCollected.setText(date);
                builder.setView(view);
                final AlertDialog dialog=builder.create();
                dialog.show();
                               collectedButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         String payeeeName=payee.getText().toString();
                         String am=rentCollectedInput.getText().toString();
                        if (payeeeName.equals("")) {
                            Toast.makeText(holder.context, "Payee name Cannot be empty!", Toast.LENGTH_SHORT).show();
                        }
                        else if(am.equals("")) {
                            Toast.makeText(holder.context, "Amount cannot be 0!", Toast.LENGTH_SHORT).show();
                        }
                        else
                         {
                             progressDialog=new ProgressDialog(holder.context);
                             progressDialog.setMax(100);
                             progressDialog.setMessage("Collecting");
                             progressDialog.setTitle("Collecting "+am+" from "+payeeeName);
                             progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);



                            collectedButton.setClickable(false);
                            Intent i = new Intent(holder.context, PaymentService.class);
                            i.putExtra("roomId", model.get_id());
                            i.putExtra("date", date);
                            i.putExtra("payee", payeeeName);
                            i.putExtra("amount", am);
                            i.putExtra("from", "occ");
                            i.putExtra("isPayment", true);//indicates that collect pressed from occ
                            holder.context.startService(i);
                            dialog.dismiss();
                            progressDialog.show();
                        }
                    }
                });

            }
        });
    }
    public void setEmptyView(TextView tv)
    {
        tv.setVisibility(View.VISIBLE);
    }
    @Override
    public int getItemCount() {
        return roomList.size();
    }
    public void setFilter(List<RoomModel> filteredList)
    {
        roomList=new ArrayList<>();
        roomList.addAll(filteredList);
        notifyDataSetChanged();
    }
    public static void enable(Button btn)
    {
        btn.setClickable(true);
    }
}
