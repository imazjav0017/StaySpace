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
import android.widget.TextView;

import com.rent.rentmanagement.renttest.R;

/**
 * Created by imazjav0017 on 01-03-2018.
 */

public class RentDueFragment extends Fragment {
    View v;
    Context context;
   public static TextView empty;

    RecyclerView occupiedRoomsListView;

    public RentDueFragment() {
    }

    public RentDueFragment(Context context) {
        this.context=context;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.rent_due_tab,container,false);
        occupiedRoomsListView=(RecyclerView)v.findViewById(R.id.occupiedRoomsList);
        empty=(TextView)v.findViewById(R.id.noRentDueText);
        LinearLayoutManager lm1=new LinearLayoutManager(context);
        occupiedRoomsListView.setLayoutManager(lm1);
        occupiedRoomsListView.setHasFixedSize(true);
        occupiedRoomsListView.setAdapter(RoomsFragment.adapter2);

        /*if(RoomsFragment.oRooms.isEmpty())
        {
            if(empty!=null) {
                occupiedRoomsListView.setVisibility(View.INVISIBLE);
                empty.setVisibility(View.VISIBLE);
            }
        }
        else {
            if(empty!=null) {
                occupiedRoomsListView.setVisibility(View.VISIBLE);
                empty.setVisibility(View.INVISIBLE);
            }
        }*/
        return v;
    }
    public static void empty()
    {
        if(empty!=null) {
            if (RoomsFragment.oRooms.isEmpty())
                RoomsFragment.adapter2.setEmptyView(empty);
            else
                empty.setVisibility(View.INVISIBLE);
        }
    }
}
