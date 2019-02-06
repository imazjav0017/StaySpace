package com.rent.rentmanagement.renttest.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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


import com.rent.rentmanagement.renttest.AsyncTasks.CheckoutTask;
import com.rent.rentmanagement.renttest.DataModels.StudentModel;
import com.rent.rentmanagement.renttest.Fragments.RoomsFragment;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.AsyncTasks.PaymentTask;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.DataModels.RoomModel;
import com.rent.rentmanagement.renttest.Owner.StudentActivity;
import com.rent.rentmanagement.renttest.Owner.roomDetailActivity;
import com.rent.rentmanagement.renttest.Services.GetRoomsService;
import com.rent.rentmanagement.renttest.Services.PaymentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by imazjav0017 on 18-03-2018.
 */

public class TotalRoomsAdapter extends RecyclerView.Adapter<TotalRoomsAdapter.TotalRoomsHolder> {
    List<RoomModel> roomList;
    JSONObject rentdetails;
    Context context;
    public static ProgressDialog progressDialog;

    public TotalRoomsAdapter(List<RoomModel> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    @Override
    public TotalRoomsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.test,parent,false);
        return new TotalRoomsHolder(v);
    }

    @Override
    public void onBindViewHolder(final TotalRoomsHolder holder, int position) {
        final RoomModel model=roomList.get(position);

        if(model.isEmpty==false)
        {
            int noOfTenants=model.getTotalRoomCapacity()-model.getRoomCapacity();
            holder.roomNo.setText(model.getRoomNo());
            holder.amount.setText("\u20B9"+model.getDueAmount());
            holder.noOfTenants.setText("x"+String.valueOf(noOfTenants));
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
            if(model.isRentDue==false)
            {
                holder.amountName.setText("Due Amount:");
                holder.checkIn.setText("Vacate");
                holder.status.setText("Paid");
                holder.statusBar.setBackgroundColor(Color.parseColor("#0ed747"));
            }
            else
            {
                holder.amountName.setText("Due Amount");
                holder.checkIn.setText("Collect");
                holder.status.setText("Rent Due");
                if(model.getDueAmount().equals(model.getRoomRent()))
                holder.statusBar.setBackgroundColor(Color.parseColor("#D32F2F"));
                else
                    holder.statusBar.setBackgroundColor(Color.parseColor("#b2df3e"));
            }
        }
        else
        {
            holder.noOfTenants.setText("x0");
            holder.roomNo.setText(model.getRoomNo());
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
            holder.amount.setText(" \u20B9"+model.getRoomRent());
            holder.checkIn.setText("CheckIn");
            holder.amountName.setText("Room Rent:");
            holder.status.setText("Vacant");
           holder.statusBar.setBackgroundColor(Color.parseColor("#FF3EC3EF"));
        }
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(holder.context,roomDetailActivity.class);
                i.putExtra("id",model.get_id());
                i.putExtra("roomNo",model.getRoomNo());
                i.putExtra("roomType",model.getRoomType());
                i.putExtra("roomRent",model.getRoomRent());
                i.putExtra("fromTotal",true);
                i.putExtra("due",model.getDueAmount());
                i.putExtra("roomCapacity",model.getRoomCapacity());
                i.putExtra("totalRoomCapacity",model.getTotalRoomCapacity());
                holder.context.startActivity(i);
            }
        });
        holder.checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(holder.checkIn.getText().toString())
                {
                    case "CheckIn":
                        Intent i=new Intent(holder.context,StudentActivity.class);
                        i.putExtra("id",model.get_id());
                        i.putExtra("roomNo",model.getRoomNo());
                        i.putExtra("fromTotal",true);
                        holder.context.startActivity(i);
                        break;
                    case "Collect":
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
                        ArrayAdapter<String>adapter=null;
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
                                    //progress dialog
                                    progressDialog=new ProgressDialog(holder.context);
                                    progressDialog.setMax(100);
                                    progressDialog.setMessage("Collecting "+am+" from "+payeeeName);
                                    progressDialog.setTitle("Collecting");
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
                        break;
                    case "Vacate":
                        new AlertDialog.Builder(context)
                                .setTitle("Vacate!").setMessage("Are You Sure You Wish To Checkout from Room No "+model.getRoomNo()+"?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            startCheckout(model.get_id(),holder.context);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setNegativeButton("No",null).show();
                        break;

                    
                }
            }
        });
    }
    void startCheckout(String roomId,Context context) throws JSONException {
        JSONObject data=new JSONObject();
        String auth = LoginActivity.sharedPreferences.getString("token", null);
        data.put("auth",auth);
        data.put("roomId",roomId);
        CheckoutTask task = new CheckoutTask(context);
        task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/vacateRooms", data.toString());
        progressDialog=new ProgressDialog(context);
        progressDialog.setMax(100);
        progressDialog.setMessage("Vacating");
        progressDialog.setTitle("Vacating Room");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

    }
    class CheckoutTask extends AsyncTask<String,Void,String> {
        Context context;

        public CheckoutTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("Accept", "application/json");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.connect();
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(params[1]);
                Log.i("VACATEDATA", params[1]);
                int resp = connection.getResponseCode();
                Log.i("VACATERESP", String.valueOf(resp));
                if (resp == 422) {
                    return "First clear Dues!";
                } else if (resp == 200) {
                    return "checked out from Room";
                } else {
                    return null;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if (s != null) {
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                if (s.equals("checked out from Room")) {
                    context.startService(new Intent(context, GetRoomsService.class));
                }
                else {
                    Toast.makeText(context, "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
  public void setEmptyView(TextView tv)
  {
      tv.setVisibility(View.VISIBLE);
  }
    @Override
    public int getItemCount()
    {
        return roomList.size();
    }
    public void setStaticData(String s, EditText payee, String _id) {
        if(s!=null) {
            if (s.equals("0")) {
                Toast.makeText(context, "Fetching!", Toast.LENGTH_SHORT).show();

            } else {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("room");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject detail = array.getJSONObject(i);
                        if(detail.getBoolean("isEmpty")==false && detail.getString("_id").equals(_id))
                        {
                            JSONArray students=detail.getJSONArray("students");
                            if(students.length()>0)
                            {
                                JSONObject studentDetails=students.getJSONObject(0);
                                payee.setText(studentDetails.getString("name"));
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void makeJson(String _id,EditText payee,EditText rentCollectedInput,String mode,String reason)
    {
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        rentdetails=new JSONObject();
        try {

            if(LoginActivity.sharedPreferences.getString("token",null)==null)
            {
                throw new Exception("invalid token");
            }
            else {
                rentdetails.put("roomId",_id);
                rentdetails.put("auth", LoginActivity.sharedPreferences.getString("token", null));
                dateFormat.format(new Date()).toString();
                if(mode.equals("c")) {
                    rentdetails.put("payee", payee.getText().toString());
                    rentdetails.put("amount", Integer.parseInt(rentCollectedInput.getText().toString()));
                    dateFormat.format(new Date()).toString();
                }else if(mode.equals("r"))
                {
                    Log.i("reason",reason);
                    rentdetails.put("reason",reason);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void setFilter(List<RoomModel> filteredList)
    {
        roomList=new ArrayList<>();
        roomList.addAll(filteredList);
        notifyDataSetChanged();
    }
    void goBack(Context context)
    {
       // RoomsFragment ob=new RoomsFragment();
        //ob.refresh();
        new RoomsFragment(context).onResume();
    }
    void enable(Button btn)
    {
        btn.setClickable(true);
    }
    public void setTokenJson(String mode,String _id)
    {
        try {
            if(LoginActivity.sharedPreferences.getString("token",null)!=null) {
                JSONObject token = new JSONObject();
                token.put("auth",LoginActivity.sharedPreferences.getString("token", null));
                token.put("roomId",_id);
               /* if(mode.equals("delete")) {
                    roomDetailActivity.DeleteRoomsTask task = new roomDetailActivity.DeleteRoomsTask();
                    task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/deleteRooms", token.toString());
                }*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Created by imazjav0017 on 18-03-2018.
     */

    public static class TotalRoomsHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        LinearLayout statusBar;
        TextView roomNo;
        TextView roomType;
        TextView amountName;
        TextView amount;
        TextView status;
        TextView noOfTenants;
        Context context;
        Button checkIn;
        public TotalRoomsHolder(View itemView) {
            super(itemView);
            ll=(LinearLayout)itemView.findViewById(R.id.TotalLl);
            statusBar=(LinearLayout)itemView.findViewById(R.id.totalStatusBar);
            context=itemView.getContext();
            roomNo=(TextView)itemView.findViewById(R.id.totalRoomNo);
            status=(TextView)itemView.findViewById(R.id.TotalStatus);
            roomType=(TextView)itemView.findViewById(R.id.totalRoomType);
            amount=(TextView)itemView.findViewById(R.id.TotalRentToBeCollected);
            amountName=(TextView)itemView.findViewById(R.id.amountNametextView);
            noOfTenants=(TextView)itemView.findViewById(R.id.noOfTenantsTextView);
            checkIn=(Button)itemView.findViewById(R.id.totalCheckinButton);
        }
    }
}
