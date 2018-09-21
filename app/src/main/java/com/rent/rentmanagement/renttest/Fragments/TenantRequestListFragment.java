package com.rent.rentmanagement.renttest.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rent.rentmanagement.renttest.Adapters.TenantRequestAdapter;
import com.rent.rentmanagement.renttest.DataModels.TenantRequestModel;
import com.rent.rentmanagement.renttest.Services.GetRoomRequestsService;
import com.rent.rentmanagement.renttest.R;

import java.util.ArrayList;

public class TenantRequestListFragment extends Fragment{
    View v;
    RecyclerView requestList;
    TextView noRequest;
    Context context;
    static SwipeRefreshLayout swipeRefreshLayout;
    public static ArrayList<TenantRequestModel> tenantRequestModels;
    public static TenantRequestAdapter adapter;

    public TenantRequestListFragment(Context context) {
        this.context = context;
    }

    public TenantRequestListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.owner_tenant_request_list,container,false);
        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.tenantRequestListSrl);
        requestList=(RecyclerView)v.findViewById(R.id.totalRequestsList);
        noRequest=(TextView)v.findViewById(R.id.noRequestsText);
        tenantRequestModels=new ArrayList<>();
        adapter=new TenantRequestAdapter(tenantRequestModels);
        LinearLayoutManager lm=new LinearLayoutManager(context);
        requestList.setLayoutManager(lm);
        requestList.setHasFixedSize(true);
        requestList.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        return v;
    }
    void refresh()
    {
        getContext().startService(new Intent(getContext(),GetRoomRequestsService.class));
    }
    @Override
    public void onResume() {
        super.onResume();
        setView();
    }
    public static void upateView()
    {
        if(tenantRequestModels!=null)
        setView();
    }
    public static void setView()
    {
        if(GetRoomRequestsService.tenantRequestModels!=null) {
            tenantRequestModels.clear();
            tenantRequestModels.addAll(GetRoomRequestsService.tenantRequestModels);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }

    }
}
