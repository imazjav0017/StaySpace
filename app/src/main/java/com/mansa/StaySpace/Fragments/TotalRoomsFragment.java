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
 * Created by imazjav0017 on 24-03-2018.
 */

public class TotalRoomsFragment extends Fragment {
    Context context;
    public static TextView empty;
    RecyclerView totalRoomsList;
    public TotalRoomsFragment() {
    }

    public TotalRoomsFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        empty();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  v=inflater.inflate(R.layout.owner_activity_all_rooms,container,false);
        totalRoomsList=(RecyclerView)v.findViewById(R.id.totalRoomsList);
        empty=(TextView)v.findViewById(R.id.noRoomsText);
        LinearLayoutManager lm=new LinearLayoutManager(context);
        totalRoomsList.setLayoutManager(lm);
        totalRoomsList.setHasFixedSize(true);
        totalRoomsList.setAdapter(RoomsFragment.adapter3);
        return v;
    }
    public static void empty() {
        if (empty != null) {
            if (RoomsFragment.tRooms.isEmpty())
                RoomsFragment.adapter3.setEmptyView(empty);
            else
                empty.setVisibility(View.INVISIBLE);
        }
    }
}
