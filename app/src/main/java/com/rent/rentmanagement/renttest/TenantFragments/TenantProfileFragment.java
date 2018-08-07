package com.rent.rentmanagement.renttest.TenantFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.R;

public class TenantProfileFragment extends android.support.v4.app.Fragment {
    TextView name,email,phNo;
    Button updateProfile;
    View v;
    public TenantProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v=inflater.inflate(R.layout.tenant_fragment,container,false);
        name=(TextView)v.findViewById(R.id.tenantNameTextView);
        email=(TextView)v.findViewById(R.id.tenantEmailTextView);
        phNo=(TextView)v.findViewById(R.id.tenantNumberTextView);
        updateProfile=(Button)v.findViewById(R.id.tenantUpdateProfileBtn);
        return v;
    }
}
