package com.rent.rentmanagement.renttest.Tenants.TenantFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rent.rentmanagement.renttest.R;

public class MainPageFragment extends Fragment {
    View v;

    public MainPageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.tenant_main_page,container,false);
        return v;
    }
}
