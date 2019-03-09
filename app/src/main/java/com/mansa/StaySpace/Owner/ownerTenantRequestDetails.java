package com.mansa.StaySpace.Owner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mansa.StaySpace.Fragments.DatePickerFragment;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Services.ResponseToRoomRequestService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class ownerTenantRequestDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
TextView roomnoTextView,name,phNo,datePickerTv;
Button accept,reject,call,message;
String tenantName,roomNo,tenantId,mobileNo,checkinDate,requestId,roomId;
    JSONObject requestResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_tenant_request_details);
        roomnoTextView=(TextView)findViewById(R.id.requestingRoomNoInfo);
        name=(TextView)findViewById(R.id.requestingTenantNameInfo);
        phNo=(TextView)findViewById(R.id.requestingTenantPhoneInfo);
        datePickerTv=(TextView)findViewById(R.id.checkinDatePicker);
        requestResponse=new JSONObject();
        Intent i=getIntent();
        tenantName=i.getStringExtra("name");
        roomNo =i.getStringExtra("roomNo");
        tenantId=i.getStringExtra("tenantId");
        mobileNo=i.getStringExtra("mobileNo");
        checkinDate=i.getStringExtra("checkInDate");
        requestId=i.getStringExtra("requestId");
        roomId=i.getStringExtra("roomId");
        if(checkinDate!=null)
            datePickerTv.setText(checkinDate);
        name.setText(tenantName);
        roomnoTextView.setText(roomNo);
        phNo.setText(mobileNo);


        datePickerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment df=new DatePickerFragment();
                df.show(getSupportFragmentManager(),"Choose Date");

            }
        });

    }
    public void call(View v)
    {
        Intent i=new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:"+mobileNo));
        startActivity(i);
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
    void setJson()
    {
        requestResponse=new JSONObject();
        try {
            int buildingIndex= LoginActivity.sharedPreferences.getInt("buildingIndex",0);
            String buildId=getBuildingId(buildingIndex);
            String ownerId=LoginActivity.sharedPreferences.getString("ownerId",null);
            requestResponse.put("auth", LoginActivity.sharedPreferences.getString("token",null));
            requestResponse.put("ownerId",ownerId);
            requestResponse.put("requestId",requestId);
            requestResponse.put("tenantId",tenantId);
            requestResponse.put("roomId",roomId);
            requestResponse.put("buildingId",buildId);
            requestResponse.put("checkInDate",checkinDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void AcceptRequest(View v)
    {
        Log.i("AcceptRequest","clicked");
        setJson();
        if(requestResponse!=null) {
            try {
                requestResponse.put("response", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent i=new Intent(getApplicationContext(),ResponseToRoomRequestService.class);
            i.putExtra("data",requestResponse.toString());
            i.putExtra("response",true);
            i.putExtra("tenantName",tenantName);
            startService(i);
            onBackPressed();
        }
        else
        Toast.makeText(this, "Try Later", Toast.LENGTH_SHORT).show();



    }
    public void rejectRequest(View v)
    {
        Log.i("rejectRequest","clicked");
        setJson();
        if(requestResponse!=null) {
            try {
                requestResponse.put("response", false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent i=new Intent(getApplicationContext(),ResponseToRoomRequestService.class);
            i.putExtra("data",requestResponse.toString());
            i.putExtra("response",false);
            i.putExtra("tenantName",tenantName);
            startService(i);
            onBackPressed();
        }
        else
            Toast.makeText(this, "Try Later", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,i);
        c.set(Calendar.MONTH,i1);
        c.set(Calendar.DAY_OF_MONTH,i2);
        //String date=DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        String year= String.valueOf(c.get(Calendar.YEAR));
        String month= String.valueOf(c.get(Calendar.MONTH)+1);
        if(month.length()==1)
        {
            month="0"+month;
        }
        String day= String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        if(day.length()==1)
        {
            day="0"+day;
        }
        checkinDate=year+"-"+month+"-"+day;
        datePickerTv.setText(checkinDate);
    }
}
