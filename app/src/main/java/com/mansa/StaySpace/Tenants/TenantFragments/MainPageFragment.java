package com.mansa.StaySpace.Tenants.TenantFragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.mansa.StaySpace.R;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.Tenants.Services.GetTenantHomeService;

import org.json.JSONException;
import org.json.JSONObject;

public class MainPageFragment extends Fragment {
    View v;
    static String phNo;
    static SwipeRefreshLayout swipeRefreshLayout;
    static TextView dueDays,dueAmount,roomNo,buildingName,ownerName,ownerPhNo,rentStatus;
    ImageButton call,sms;

    public MainPageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.tenant_main_page,container,false);
        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.tenantMainPageSrl);
        dueDays=(TextView)v.findViewById(R.id.dueDaysTenantTextView);
        dueAmount=(TextView)v.findViewById(R.id.dueAmountTenantTextView);
        roomNo=(TextView)v.findViewById(R.id.roomNoTenantTextView);
        buildingName=(TextView)v.findViewById(R.id.buildingNameTenantTextView);
        ownerName=(TextView)v.findViewById(R.id.ownerNameTenantTextView);
        ownerPhNo=(TextView)v.findViewById(R.id.ownerPhNoTenantTextView);
        rentStatus=(TextView)v.findViewById(R.id.rentStatusTenantTextView);
        call=(ImageButton) v.findViewById(R.id.callOwnerBtn);
        sms=(ImageButton)v.findViewById(R.id.messageOwnerBtn);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsOwner();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callOwner();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               refresh();
            }
        });
        return v;
    }
    void smsOwner()
    {
        if(phNo!=null)
        {
                    String number = phNo;
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
        }
    }
    void callOwner()
    {
        if(phNo!=null)
        {
            Intent i=new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:"+phNo));
            startActivity(i);
        }

    }
    void refresh()
    {
        getContext().startService(new Intent(getContext(), GetTenantHomeService.class));
    }

    @Override
    public void onPause() {
        super.onPause();
        if(swipeRefreshLayout!=null)
        {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            updateView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void updateView() throws JSONException {
        if(dueDays!=null)
        {
            String roomDetails= LoginActivity.sharedPreferences.getString("tenantRoomDetails",null);
            if(roomDetails!=null) {
            JSONObject tenantRoomDetails = new JSONObject(roomDetails);
            String roomNoValue = tenantRoomDetails.getString("roomNo");
            roomNo.setText(roomNoValue);
                 }
                 else
            {
                roomNo.setText("N/A");
            }
            String paymentDetail=LoginActivity.sharedPreferences.getString("tenantPaymentDetail",null);
            if(paymentDetail!=null)
            {
                JSONObject tenantPaymentDetail=new JSONObject(paymentDetail);
                String dueDaysValue=tenantPaymentDetail.getString("dueDate");
                int dueAmountValue=tenantPaymentDetail.getInt("dueAmount");
                boolean isRentDue=tenantPaymentDetail.getBoolean("isRentDue");
                    dueDays.setText("Due On: " + dueDaysValue);
                dueAmount.setText("\u20B9"+String.valueOf(dueAmountValue));
                if(dueAmountValue==0)
                    dueAmount.setTextColor(Color.parseColor("#3aa335"));
                if(isRentDue) {
                    rentStatus.setText("Not Paid");
                    rentStatus.setTextColor(Color.parseColor("#df0804"));
                }
                else {
                    rentStatus.setText("Paid");
                    rentStatus.setTextColor(Color.parseColor("#3aa335"));
                }

            }
            else
            {
                dueDays.setText("Due On:N/A");
                dueAmount.setText("N/A");
                rentStatus.setText("N/A");

            }
            String buildingInfo=LoginActivity.sharedPreferences.getString("tenantBuildingInfo",null);
            if(buildingInfo!=null)
            {
                JSONObject tenantBuildingInfo=new JSONObject(buildingInfo);
                String buildingNameValue=tenantBuildingInfo.getString("name");
                buildingName.setText(buildingNameValue);
            }
            else
            {
                buildingName.setText("N/A");
            }
            String ownerInfo=LoginActivity.sharedPreferences.getString("tenantOwnerInfo",null);
            if(ownerInfo!=null) {
                JSONObject tenantOwnerInfo = new JSONObject(ownerInfo);
                 phNo = tenantOwnerInfo.getString("mobileNo");
                JSONObject name = tenantOwnerInfo.getJSONObject("name");
                String nameValue = name.getString("firstName") + " " + name.getString("lastName");
                ownerName.setText("Mr." + nameValue);
                ownerPhNo.setText(phNo);
            }
            else
            {
                ownerName.setText("N/A");
                ownerPhNo.setText("N/A");
                phNo=null;
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
