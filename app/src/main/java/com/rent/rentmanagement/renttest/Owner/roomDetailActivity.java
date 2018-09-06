package com.rent.rentmanagement.renttest.Owner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.rent.rentmanagement.renttest.Adapters.PaymentHistoryAdapter;
import com.rent.rentmanagement.renttest.Adapters.StudentAdapter;
import com.rent.rentmanagement.renttest.AsyncTasks.CheckoutTask;
import com.rent.rentmanagement.renttest.AsyncTasks.PaymentTask;
import com.rent.rentmanagement.renttest.DataModels.PaymentHistoryModel;
import com.rent.rentmanagement.renttest.DataModels.StudentModel;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class roomDetailActivity extends AppCompatActivity {
    TextView rn,rt,rr,studentsExpandingView,paymentsExpandLayout;
    RecyclerView studentsRV,paymentsHistoryList;
    StudentAdapter adapter;
    List<StudentModel> studentsList;
    List<PaymentHistoryModel>paymentList;
    Button checkOut;
    PaymentHistoryAdapter pAdapter;
    ExpandableRelativeLayout expandableRelativeLayout,expandablePayments;
    String roomNo,roomType,roomRent,_id,response,dueAmnt;
    boolean fromTotal;
    JSONObject rentdetails;
    void enable(Button btn)
    {
        btn.setClickable(true);
    }
    public void makeJson(String _id,EditText payee,EditText rentCollectedInput,String mode,String reason)
    {
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

        rentdetails=new JSONObject();
        try {

            if(LoginActivity.sharedPreferences.getString("token",null)==null)
            {
                throw new Exception("invalid token");
            }
            else {
                rentdetails.put("roomId",_id);
                rentdetails.put("auth", LoginActivity.sharedPreferences.getString("token", null));
                if(mode.equals("c")) {
                    rentdetails.put("payee", payee.getText().toString());
                    rentdetails.put("amount", Integer.parseInt(rentCollectedInput.getText().toString()));
                    rentdetails.put("date",dateFormat.format(new Date()).toString());
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
    public void setStaticData(String s,EditText payee,String _id) {
        if(s!=null) {
            if (s.equals("0")) {
                Toast.makeText(getApplicationContext(), "Fetching!", Toast.LENGTH_SHORT).show();

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
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        this.finish();
    }

    public void setTokenJson(String mode)
    {    DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if(LoginActivity.sharedPreferences.getString("token",null)!=null) {
                JSONObject token = new JSONObject();
                token.put("auth",LoginActivity.sharedPreferences.getString("token", null));
                token.put("roomId",_id);
                token.put("date" ,dateFormat.format(new Date()).toString());
                if(mode.equals("delete")) {
                    DeleteRoomsTask task = new DeleteRoomsTask();
                    task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/deleteRooms", token.toString());
                }
                else if(mode.equals("ch"))
                {
                    CheckoutTask task = new CheckoutTask();
                    String s=task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/vacateRooms", token.toString()).get();
                    if (s != null) {
                        Toast.makeText(roomDetailActivity.this,s,Toast.LENGTH_SHORT).show();
                        if(s.equals("checked out from Room"))
                        {
                            onBackPressed();
                        }
                        else if(s.equals("First clear Dues!"))
                        {
                            AlertDialog.Builder builder=new AlertDialog.Builder(roomDetailActivity.this);
                            View view= LayoutInflater.from(roomDetailActivity.this).inflate(R.layout.owner_dialog_collect,null,false);
                            final EditText rentCollectedInput=(EditText)view.findViewById(R.id.rentcollectedinput);
                            final EditText payee=(EditText)view.findViewById(R.id.payee);
                            rentCollectedInput.setText(dueAmnt);
                            rentCollectedInput.setSelection(rentCollectedInput.getText().toString().length());
                            final Button collectedButton=(Button)view.findViewById(R.id.collectedbutton);
                            DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
                            Date dateObj=new Date();
                            String date=df.format(dateObj).toString();
                            setStaticData(LoginActivity.sharedPreferences.getString("roomsDetails",null),payee,_id);
                            TextView dateCollected=(TextView)view.findViewById(R.id.datecollectedinput);
                            dateCollected.setText(date);
                            builder.setView(view);
                            final AlertDialog dialog=builder.create();
                            dialog.show();
                            collectedButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    collectedButton.setClickable(false);
                                    makeJson(_id,payee,rentCollectedInput,"c",null);
                                    PaymentTask ptask=new PaymentTask(roomDetailActivity.this,dialog,collectedButton);
                                    ptask.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/paymentDetail",rentdetails.toString());

                                }
                            });

                        }
                    }
                    else
                    {
                        Toast.makeText(roomDetailActivity.this, "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public String  getResponse(HttpURLConnection connection)
    {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = in.readLine()) != null) {

                sb.append(line);
                break;
            }

            in.close();
            return sb.toString();
        }catch(Exception e)
        {
            return e.getMessage();
        }
    }
    public class DeleteRoomsTask extends AsyncTask<String,Void,String>
    {
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
                Log.i("data", params[1]);
                int resp = connection.getResponseCode();
                Log.i("deletRoomResp",String.valueOf(resp));
                if(resp==200)
                {
                    response=getResponse(connection);
                    return response;
                }
                else
                {
                    return null;
                }

            }catch(MalformedURLException e)
            {
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
            if (s != null) {
                Toast.makeText(roomDetailActivity.this, "Room Deleted!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            else
            {
                Toast.makeText(roomDetailActivity.this, "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
                super.onPostExecute(s);
            }
        }
    }
    public void deleteRoom(View v)
    {
        new AlertDialog.Builder(this)
                .setTitle("Delete!").setMessage("Are You Sure You Wish To Delete Room No "+roomNo)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setTokenJson("delete");
                    }
                })
               .setNegativeButton("No",null).show();
    }
    public void checkOut(View v)
    {
        if(checkOut.getText().toString().equals("Checkout")) {
            new AlertDialog.Builder(this)
                    .setTitle("Checkout!").setMessage("Are You Sure You Wish To Checkout from Room No " + roomNo + "?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setTokenJson("ch");
                        }
                    })
                    .setNegativeButton("No", null).show();
        }
        else
        {
            Intent i=new Intent(getApplicationContext(),StudentActivity.class);
            i.putExtra("id",_id);
            i.putExtra("roomNo",roomNo);
            startActivity(i);
            finish();
        }

    }
    public void editRoom(View v)
    {
        Intent i=new Intent(getApplicationContext(),edit_rooms.class);
        i.putExtra("roomNo",roomNo);
        i.putExtra("roomRent",roomRent);
        i.putExtra("roomType",roomType);
        i.putExtra("id",_id);
        i.putExtra("fromTotal",fromTotal);
        startActivity(i);
        finish();
    }
    public  void addStudent(View v)
    {
        Intent i=new Intent(getApplicationContext(),StudentActivity.class);
        i.putExtra("id",_id);
        i.putExtra("roomNo",roomNo);
        i.putExtra("fromDetails",true);
        startActivity(i);
        finish();
    }
    public void setPaymentHistory(String s) {
        paymentList.clear();
        if(s!=null) {
            if (s.equals("0")) {
                Toast.makeText(this, "Fetching!", Toast.LENGTH_SHORT).show();

            } else {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("room");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject detail = array.getJSONObject(i);
                        if(detail.getString("_id").equals(_id))
                        {
                            JSONObject paymentobj=detail.getJSONObject("paymentDetail");
                            Log.i("payments",paymentobj.toString());
                            JSONArray payments=paymentobj.getJSONArray("payment");
                            if(payments.length()>0)
                            {

                                for(int k=0;k<payments.length();k++) {
                                    JSONObject paymentDetails = payments.getJSONObject(k);
                                    if(paymentDetails.getBoolean("payStatus")==true)
                                    paymentList.add(new PaymentHistoryModel(paymentDetails.getString("payee"),
                                            paymentDetails.getString("amount"),paymentDetails.getString("date")
                                            ,paymentDetails.getBoolean("payStatus")));
                                    else
                                        paymentList.add(new PaymentHistoryModel(paymentDetails.getString("reason"),paymentDetails.getString("date")
                                                ,paymentDetails.getBoolean("payStatus")));


                                }
                                pAdapter.notifyDataSetChanged();

                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void setStudentsData(String s) throws JSONException {
        studentsList.clear();
       Log.i("setStudentsData",s);
        if(s!=null) {
            JSONObject jsonObject=new JSONObject(s);
            //tenants checked in through requests
            JSONArray tenantArray=jsonObject.getJSONArray("tenant");

            JSONArray array=jsonObject.getJSONArray("student");
            JSONArray roomNo=jsonObject.getJSONArray("roomNo");
            studentsList.clear();
            //adding the "students"
            for(int k=0;k<array.length();k++)
            {
                String rNo=roomNo.getString(k);
                JSONArray array1=array.getJSONArray(k);
                for (int i = 0; i < array1.length(); i++) {
                    JSONObject detail = array1.getJSONObject(i);
                    studentsList.add(new StudentModel(detail.getString("name"),detail.getString("mobileNo"),rNo
                            ,detail.getString("_id"),detail.getString("adharNo")));
                }
            }
            for(int i=0;i<tenantArray.length();i++)
            {
                String rNo=roomNo.getString(i);
                JSONArray array2=tenantArray.getJSONArray(i);
                for (int i1 = 0; i1 < array2.length(); i1++) {
                    JSONObject detail = array2.getJSONObject(i1);
                    studentsList.add(new StudentModel(detail.getString("name"),detail.getString("mobileNo"),rNo
                            ,detail.getString("_id"),detail.getString("adharNo")));
                }

            }
         if(studentsList.size()==0)
                    {

                            checkOut.setText("Checkin");

                    }
                    adapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_room_detail);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i=getIntent();
        _id=i.getStringExtra("id");
        roomNo=i.getStringExtra("roomNo");
        roomType=i.getStringExtra("roomType");
        fromTotal=i.getBooleanExtra("fromTotal",false);
        roomRent=i.getStringExtra("roomRent");
        dueAmnt=i.getStringExtra("due");
        setTitle("RoomNo: "+i.getStringExtra("roomNo"));
        checkOut=(Button)findViewById(R.id.empt_checkin);
        rn = (TextView) findViewById(R.id.roomno);
        rt = (TextView) findViewById(R.id.roomtype);
        rr = (TextView) findViewById(R.id.roomrent);
        studentsExpandingView = (TextView) findViewById(R.id.studentsExpandingView);
        paymentsExpandLayout = (TextView) findViewById(R.id.paymentsExpandLayout);
        expandableRelativeLayout=(ExpandableRelativeLayout)findViewById(R.id.studentsLayout);
        expandablePayments=(ExpandableRelativeLayout)findViewById(R.id.paymentsLayout);
        rn.setText(roomNo);
        rt.setText(roomType);
        rr.setText("\u20B9"+roomRent);
        studentsRV=(RecyclerView)findViewById(R.id.studentsRecyclerView);
        studentsList=new ArrayList<>();
        adapter=new StudentAdapter(studentsList,getApplicationContext());
        LinearLayoutManager lm=new LinearLayoutManager(getApplicationContext());
        studentsRV.setLayoutManager(lm);
        studentsRV.setHasFixedSize(true);
        studentsRV.setAdapter(adapter);
        paymentList=new ArrayList<>();
        paymentsHistoryList=(RecyclerView)findViewById(R.id.paymentsHistoryList);
        pAdapter=new PaymentHistoryAdapter(paymentList);
        LinearLayoutManager lm2=new LinearLayoutManager(getApplicationContext());
        paymentsHistoryList.setLayoutManager(lm2);
        paymentsHistoryList.setHasFixedSize(true);
        paymentsHistoryList.setAdapter(pAdapter);
        setPaymentHistory(LoginActivity.sharedPreferences.getString("roomsDetails",null));
        try {
            setStudentsData(LoginActivity.sharedPreferences.getString("allTenantsinfo",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void expandStudents(View v)
    {
        if(expandableRelativeLayout.isExpanded())
        {
            studentsExpandingView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_down_arrow,0);
            expandableRelativeLayout.collapse();
        }
        else {
            studentsExpandingView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_up_arrow,0);
            expandableRelativeLayout.toggle();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void expandPayments(View v)
    {
        if(expandablePayments.isExpanded())
        {
            paymentsExpandLayout.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_down_arrow,0);
            expandablePayments.collapse();
        }
        else {
            paymentsExpandLayout.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_up_arrow,0);
            expandablePayments.toggle();
        }
    }
}
