package com.rent.rentmanagement.renttest.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rent.rentmanagement.renttest.Owner.ViewPagerAdapter;
import com.rent.rentmanagement.renttest.R;

public class AllTenantsFragment extends Fragment {
    View v;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Context context;
    int n;
    public AllTenantsFragment() {
    }

    @SuppressLint("ValidFragment")
    public AllTenantsFragment(Context context, int n) {
        this.context = context;
        this.n=n;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.owner_activity_total_tenantsctivity,container,false);
        tabLayout=(TabLayout)v.findViewById(R.id.tenantsListTabLayout);
        viewPager=(ViewPager)v.findViewById(R.id.tenantsViewPager);
        viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager(),context);
        //viewPager.setCurrentItem(2,true);
        viewPager.setOffscreenPageLimit(2);
        viewPagerAdapter.addFragment(new TenantsFragment(context),"Tenants");
        viewPagerAdapter.addFragment(new TenantRequestListFragment(context),"Room Requests");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        Log.i("nnnnnnnnn",""+n);
        if(n==1)
            viewPager.setCurrentItem(1,true);
        else
        viewPager.setCurrentItem(0,true);
        return v;
    }
}
