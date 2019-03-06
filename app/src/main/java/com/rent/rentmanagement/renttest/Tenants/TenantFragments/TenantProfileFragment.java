package com.rent.rentmanagement.renttest.Tenants.TenantFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Tenants.EditProfileActivity;
import com.rent.rentmanagement.renttest.Tenants.UpdateProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TenantProfileFragment extends android.support.v4.app.Fragment {
    static TextView name,email,phNo;
    Button updateProfile;
    View v;
    Context context;
    ImageView ad;
    public TenantProfileFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            setData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public TenantProfileFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v=inflater.inflate(R.layout.tenant_profile_fragment,container,false);
        name=(TextView)v.findViewById(R.id.tenantNameTextView);
        email=(TextView)v.findViewById(R.id.tenantEmailTextView);
        phNo=(TextView)v.findViewById(R.id.tenantNumberTextView);
        ad=(ImageView)v.findViewById(R.id.tenantSideAd);
        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAd();
            }
        });
        updateProfile=(Button)v.findViewById(R.id.tenantUpdateProfileBtn);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, EditProfileActivity.class);
                startActivity(i);
            }
        });
        return v;
    }
    public static void setData() throws JSONException {
        String tName,tEmail,tPhoneNO;
        String data= LoginActivity.sharedPreferences.getString("tenantDetail",null);
        if(data!=null && name!=null)
        {
            JSONObject object=new JSONObject(data);
            tEmail=object.getString("email");
            tPhoneNO=object.getString("mobileNo");
            JSONObject nameObject=object.getJSONObject("name");
            tName=nameObject.getString("firstName")+" "+nameObject.getString("lastName");
            name.setText(tName);
            email.setText(tEmail);
            phNo.setText(tPhoneNO);
        }
    }
    void showAd()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_show_ad, null, false);
        builder.setView(v);
        final AlertDialog resetDialog = builder.create();
        resetDialog.show();
    }
}
