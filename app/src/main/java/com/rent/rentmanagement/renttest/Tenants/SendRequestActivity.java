package com.rent.rentmanagement.renttest.Tenants;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.DataCallBack;
import com.rent.rentmanagement.renttest.Fragments.DatePickerFragment;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Tenants.Async.RoomRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class SendRequestActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView buildingNameTv,addresstv,OwnerNameTv,OwnerPhNoTv,RoomNoTv,roomTypeTv,roomRentTv,
    vacancyTv,datePickerTv;
    Button sendRequest;
    ImageButton call;
    String data,buildingName,ownerName,phNo,roomNo,roomType,rent,roomId,ownerId,buildingId,address;
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
        setContentView(R.layout.tenant_activity_send_request);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        data=getIntent().getStringExtra("data");
        roomId=getIntent().getStringExtra("roomId");
        ownerId=getIntent().getStringExtra("ownerId");
        buildingId=getIntent().getStringExtra("buildingId");
        buildingNameTv=(TextView)findViewById(R.id.availableBuildingNameTextView);
        addresstv=(TextView)findViewById(R.id.availableAddressTv);
        OwnerNameTv=(TextView)findViewById(R.id.availableOwnerTv);
        OwnerPhNoTv=(TextView)findViewById(R.id.availableOwnerPhNoTv);
        RoomNoTv=(TextView)findViewById(R.id.availableRoomNoTv);
        roomTypeTv=(TextView)findViewById(R.id.availableRoomTypeTv);
        roomRentTv=(TextView)findViewById(R.id.availableRoomRentTv);
        vacancyTv=(TextView)findViewById(R.id.availableVacancyTv);
        datePickerTv=(TextView)findViewById(R.id.tenantDatePicker);
        sendRequest=(Button)findViewById(R.id.sendRequestButton);
        call=(ImageButton)findViewById(R.id.callRequestingOwnerBtn);
        try {
            extractInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar c=Calendar.getInstance();
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
        datePickerTv.setText(year+"-"+month+"-"+day);
        datePickerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment df=new DatePickerFragment();
                df.show(getSupportFragmentManager(),"Choose Date");

            }
        });
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
        datePickerTv.setText(year+"-"+month+"-"+day);

    }

    public void tryToSendRequest(View v)
    {
        JSONObject requestObject=new JSONObject();
        try {
            String auth=LoginActivity.sharedPreferences.getString("token",null);
            String _id= LoginActivity.sharedPreferences.getString("tenantId",null);
            if(auth!=null)
                requestObject.put("auth",auth);
            if(roomId!=null)
            requestObject.put("roomId",roomId);
            if(ownerId!=null)
            requestObject.put("ownerId",ownerId);
            if(buildingId!=null)
            requestObject.put("buildingId",buildingId);
            requestObject.put("tenantId",_id);
            requestObject.put("requestCheckInDate",datePickerTv.getText().toString());
            RoomRequestTask task=new RoomRequestTask(getApplicationContext(), new DataCallBack() {
                @Override
                public void datacallBack(String result, boolean response) {
                    if(response)
                    {
                        onBackPressed();
                    }
                }
            });
            task.execute("https://sleepy-atoll-65823.herokuapp.com/students/sendRoomRequest",requestObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void extractInfo() throws JSONException {
        if(data!=null)
        {
            JSONObject s=new JSONObject(data);
            buildingName=s.getString("buildingName");
            ownerName=s.getString("ownerName");
            phNo=s.getString("phNo");
            roomNo=s.getString("roomNo");
            roomType=s.getString("roomType");
            rent=s.getString("roomRent");
            address=s.getString("address");
            setTitle("Room No :\n"+roomNo);
            buildingNameTv.setText(buildingName);
            OwnerNameTv.setText(ownerName);
            OwnerPhNoTv.setText("Contact: "+phNo);
            RoomNoTv.setText(roomNo);
            roomTypeTv.setText("Room Type :"+roomType);
            roomRentTv.setText("Rent :\u20b9"+rent);
            addresstv.setText("Address: "+address);
        }
    }
    public void callOwner(View v)
    {
        if(phNo!=null) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + phNo));
            startActivity(i);
        }
    }


}
