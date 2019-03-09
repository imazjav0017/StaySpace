package com.mansa.StaySpace.Tenants.TenantFragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Tenants.Async.GetAvailableRoomsTask;
import com.mansa.StaySpace.Tenants.DataModels.AvailableRoomModel;
import com.mansa.StaySpace.Tenants.Adapters.AvailableRoomsAdapter;
import com.mansa.StaySpace.Tenants.Services.GetAvailableRoomsService;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AvailableRoomsFragment extends Fragment implements SearchView.OnQueryTextListener {
    View v;
    RecyclerView availableRoomsList;
    Context context;
    public static ArrayList<AvailableRoomModel>availableRooms;
    public static List<String>buildings; //ND non Distinct;
    public static Set<String> buildingsD; //D  Distinct;
    static AvailableRoomsAdapter adapter;
    public static SearchView searchView;
    boolean filterState=false;
    static TextView emptyText;
    public static ProgressBar progressBar;
    List<AvailableRoomModel>filteredList;
    public AvailableRoomsFragment() {
    }

    @SuppressLint("ValidFragment")
    public AvailableRoomsFragment(Context context) {
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.tenant_available_rooms_fragment,container,false);
        availableRoomsList=(RecyclerView)v.findViewById(R.id.availableRoomsList);
        progressBar=(ProgressBar)v.findViewById(R.id.availableRoomsProgress);
        emptyText=(TextView)v.findViewById(R.id.noAvailableRoomstext);
        availableRooms=new ArrayList<>();
        buildings=new ArrayList<>();
        filteredList=new ArrayList<>();
        adapter=new AvailableRoomsAdapter(availableRooms);
        LinearLayoutManager lm=new LinearLayoutManager(context);
        availableRoomsList.setLayoutManager(lm);
        availableRoomsList.setHasFixedSize(true);
        availableRoomsList.setAdapter(adapter);
        return  v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item=menu.findItem(R.id.searchMenuTenant);
        item.setVisible(true);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        ImageView icon= (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        icon.setColorFilter(Color.WHITE);
        searchView.setOnQueryTextListener(this);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Enter Building name or room No or ownerNo");
        MenuItem filterItem=menu.findItem(R.id.filterMenuTenant);
        filterItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.filterMenuTenant:

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View v = LayoutInflater.from(getContext()).inflate(R.layout.tenant_dialog_filter_search, null, false);
                    final AutoCompleteTextView buildingNameInput = (AutoCompleteTextView) v.findViewById(R.id.filterBuildingNameInput);
                    final EditText roomNoInput = (EditText) v.findViewById(R.id.filterRoomNoInput);
                    Button searchBtn = (Button) v.findViewById(R.id.searchFilterBtn);
                    TextView removeFilters=(TextView)v.findViewById(R.id.removefilterOption);
                    builder.setView(v);
                    final AlertDialog filterDialog = builder.create();
                    filterDialog.show();
                    setBuildings();
                    ArrayAdapter<String> Aadapter = new ArrayAdapter(getContext(), android.R.layout.select_dialog_item, buildings);
                    buildingNameInput.setThreshold(1);
                    buildingNameInput.setAdapter(Aadapter);
                    buildingNameInput.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            buildingNameInput.showDropDown();
                        }
                    });
                    searchBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String bName = buildingNameInput.getText().toString();
                            String rNo = roomNoInput.getText().toString();
                            filter(bName, rNo);
                            filterDialog.dismiss();
                        }
                    });
                    removeFilters.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            filterState=false;
                            Toast.makeText(getContext(), "Filter Cleared!", Toast.LENGTH_SHORT).show();
                            if(availableRooms!=null)
                                adapter.removeFilter(availableRooms);
                            filterDialog.dismiss();
                        }
                    });
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        newText=newText.toLowerCase();
        List<AvailableRoomModel> filteredList1=new ArrayList<>();

        //if the list is in filter state
        if(filterState)
        {
            filteredList1.clear();
            //if text is empty
            if(newText.isEmpty())
            {
                filteredList1.addAll(filteredList);
            }
            else
            {
                //if text is not empty
                if(filteredList!=null)
                {
                    for(AvailableRoomModel model : filteredList)
                    {
                        if(model.getRoomNo().toLowerCase().contains(newText))
                        {
                            filteredList1.add(model);
                        }
                        if(model.getBuildingName().toLowerCase().contains(newText))
                            filteredList1.add(model);
                        if (model.getOwnerName().toLowerCase().equals(newText))
                            filteredList1.add(model);
                        if(model.getPhoneNo().equals(newText))
                            filteredList1.add(model);
                    }

                }
            }

            //common for both empty and non empty
            if(adapter!=null)
            {
                adapter.setFilter(filteredList1);
            }

        }

        //if not in filter state
        else
        {
            filteredList1.clear();
        if(newText.isEmpty())
        {
           // Log.i("empty","called");
            filteredList1.addAll(availableRooms);
        }
        else {
            //text non empty
            if (availableRooms != null) {
                for (AvailableRoomModel model : availableRooms) {
                    if (model.getRoomNo().toLowerCase().contains(newText)) {
                        filteredList1.add(model);
                    }
                    if (model.getBuildingName().toLowerCase().contains(newText))
                        filteredList1.add(model);
                    if (model.getOwnerName().toLowerCase().equals(newText))
                        filteredList1.add(model);
                    if (model.getPhoneNo().equals(newText))
                        filteredList1.add(model);
                }
               // Log.i("emptyElse",String.valueOf(filteredList1.size()));
            }
        }

        //common to both empty and non empty
            if (adapter != null) {
                adapter.setFilter(filteredList1);
            }
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("in 2","frag");
        progressBar.setVisibility(View.VISIBLE);
        setView();

    }
    //called by tenantActivity when loading complete
    public static void updateNow()
    {
        //if updateView is called by activity before opening fragment
        // will lead to null pointer
        if(availableRooms!=null)
        {
            setView();
        }
    }
    public static void setView() {
        if (GetAvailableRoomsService.availableRooms != null) {
            availableRooms.clear();
            availableRooms.addAll(GetAvailableRoomsService.availableRooms);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
            if(availableRooms.isEmpty())
            {
                emptyText.setVisibility(View.VISIBLE);
            }
            else
            {
                emptyText.setVisibility(View.INVISIBLE);
            }
        }
        if(GetAvailableRoomsTask.failed)
            progressBar.setVisibility(View.INVISIBLE);
    }
    public static void setBuildings()
    {
        if(GetAvailableRoomsService.buildings!=null)
        {
            buildingsD=new LinkedHashSet<>(GetAvailableRoomsService.buildings);
            buildings.clear();
            buildings.addAll(buildingsD);
            Log.i("buidl",buildings.toString());
        }
    }
    public void filter(String bName,String rNo)
    {
        filterState=true;
        Log.i("xyz",bName);
        rNo.toLowerCase();
        filteredList.clear();
        if(bName.isEmpty() && rNo.isEmpty()) {
            filteredList.addAll(availableRooms);
            Log.i("xyz","empty");
            filterState=false;
        }
        else if(rNo.equals("") && !bName.equals(""))
        {
            Log.i("xyz","bName");
            if(availableRooms!=null) {
                for (AvailableRoomModel model : availableRooms) {
                    if (model.getBuildingName().equals(bName)) {
                        filteredList.add(model);
                    }
                }
            }
        }
        else if(bName.equals("") && !rNo.equals(""))
        {
            Log.i("xyz","rNo");
            if(availableRooms!=null) {
                for (AvailableRoomModel model : availableRooms) {
                    if (model.getRoomNo().toLowerCase().equals(rNo)) {
                        filteredList.add(model);
                    }
                }
            }
        }
        else {
            if (availableRooms != null) {
                for (AvailableRoomModel model : availableRooms) {
                    if (model.getRoomNo().toLowerCase().equals(rNo) &&
                            model.getBuildingName().equals(bName)) {
                        filteredList.add(model);
                    }
                }
            }
        }

        if(adapter!=null)
        {
            adapter.setFilter(filteredList);
        }
    }



}
