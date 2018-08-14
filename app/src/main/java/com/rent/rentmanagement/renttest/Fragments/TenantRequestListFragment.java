package com.rent.rentmanagement.renttest.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.Adapters.TenantRequestAdapter;
import com.rent.rentmanagement.renttest.DataModels.TenantRequestModel;
import com.rent.rentmanagement.renttest.Owner.MainActivity;
import com.rent.rentmanagement.renttest.R;

import java.util.ArrayList;

public class TenantRequestListFragment extends Fragment{
    View v;
    RecyclerView requestList;
    TextView noRequest;
    Context context;
    public ArrayList<TenantRequestModel> tenantRequestModels;
    public TenantRequestAdapter adapter;

    public TenantRequestListFragment(Context context) {
        this.context = context;
    }

    public TenantRequestListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.owner_tenant_request_list,container,false);
        requestList=(RecyclerView)v.findViewById(R.id.totalRequestsList);
        noRequest=(TextView)v.findViewById(R.id.noRequestsText);
        tenantRequestModels=new ArrayList<>();
        adapter=new TenantRequestAdapter(tenantRequestModels);
        LinearLayoutManager lm=new LinearLayoutManager(context);
        requestList.setLayoutManager(lm);
        requestList.setHasFixedSize(true);
        requestList.setAdapter(adapter);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }
    public void setView()
    {
        tenantRequestModels.clear();
        if(MainActivity.REQUESTSSET==1)
        {
            tenantRequestModels.addAll(MainActivity.tenantRequestModels);
            adapter.notifyDataSetChanged();
        }
    }
}
