package com.mansa.StaySpace.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mansa.StaySpace.DataModels.ChatContact;
import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Services.GetOwnerPeersService;
import com.mansa.StaySpace.Tenants.Adapters.ChatContactAdapter;
import com.mansa.StaySpace.Tenants.Services.GetTenantPeersService;

import java.util.ArrayList;
import java.util.List;

public class ComplaintsFragmentOwner extends android.support.v4.app.Fragment{
    View v;
    static SwipeRefreshLayout swipeRefreshLayout;
    ImageButton newChatBtn;
    RecyclerView chatsRv;
    static List<ChatContact> chatContactList;
    static ChatContactAdapter adapter;

    public ComplaintsFragmentOwner() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.tenant_complaints_fragment,container,false);
        swipeRefreshLayout=v.findViewById(R.id.tenantComplaintsSrl);
        newChatBtn=v.findViewById(R.id.newChatTenant);
        chatsRv=v.findViewById(R.id.tenantChatsRv);
        chatContactList=new ArrayList<>();
        adapter=new ChatContactAdapter(chatContactList);
        LinearLayoutManager lm=new LinearLayoutManager(getActivity());
        chatsRv.setLayoutManager(lm);
        chatsRv.setHasFixedSize(true);
        chatsRv.setAdapter(adapter);
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
        getContext().startService(new Intent(getContext(),GetOwnerPeersService.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(swipeRefreshLayout!=null)
        {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    static void setView()
    {
        if(GetOwnerPeersService.chatContacts!=null) {
            chatContactList.clear();
            chatContactList.addAll(GetOwnerPeersService.chatContacts);
            adapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(false);
    }
    public static void updateNow()
    {
        if(chatContactList!=null)
        {
            setView();
        }
    }
}
