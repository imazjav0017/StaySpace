package com.rent.rentmanagement.renttest.Owner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.R;

public class ownerTenantRequestDetails extends AppCompatActivity {
TextView roomnoTextView,name,phNo;
Button accept,reject,call,message;
String tenantName,roomNo,_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_tenant_request_details);
        roomnoTextView=(TextView)findViewById(R.id.requestingRoomNoInfo);
        name=(TextView)findViewById(R.id.requestingTenantNameInfo);
        phNo=(TextView)findViewById(R.id.requestingTenantPhoneInfo);
        Intent i=getIntent();
        tenantName=i.getStringExtra("name");
        roomNo =i.getStringExtra("roomNo");
        _id=i.getStringExtra("tenantId");
        name.setText(tenantName);
        roomnoTextView.setText(roomNo);



    }
    public void AcceptRequest(View v)
    {
        Log.i("request","accepted");
    }
    public void rejectRequest(View v)
    {
        Log.i("request","accepted");
    }
}
