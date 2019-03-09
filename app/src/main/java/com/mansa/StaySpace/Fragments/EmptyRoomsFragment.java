package com.mansa.StaySpace.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mansa.StaySpace.R;

/**
 * Created by imazjav0017 on 01-03-2018.
 */

public class EmptyRoomsFragment extends Fragment {
    View v;
    static TextView empty;
    Context context;
    RecyclerView emptyRoomsListView;



    public EmptyRoomsFragment() {
    }

    public EmptyRoomsFragment(Context context) {
               this.context=context;
    }

    @Override
    public void onResume() {
        super.onResume();
        empty();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.owner_empty_rooms_tab,container,false);
        emptyRoomsListView=(RecyclerView)v. findViewById(R.id.emptyRoomsList);
        empty=(TextView)v.findViewById(R.id.noEmptyRoomsText);
        LinearLayoutManager lm=new LinearLayoutManager(context);
        emptyRoomsListView.setLayoutManager(lm);
        emptyRoomsListView.setHasFixedSize(true);
        emptyRoomsListView.setAdapter(RoomsFragment.adapter);


        return v;
    }
    public static void empty()
    {
        if(empty!=null) {
            if (RoomsFragment.erooms.isEmpty())
                RoomsFragment.adapter.setEmptyView(empty);
            else
                empty.setVisibility(View.INVISIBLE);
        }
    }

}
