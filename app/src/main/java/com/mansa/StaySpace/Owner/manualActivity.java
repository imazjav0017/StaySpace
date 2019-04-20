package com.mansa.StaySpace.Owner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mansa.StaySpace.Adapters.AddedRoomsAdapter;
import com.mansa.StaySpace.Adapters.TotalRoomsAdapter;
import com.mansa.StaySpace.DataModels.AddedRoomModel;
import com.mansa.StaySpace.DataModels.RoomModel;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class manualActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    EditText rentInput,roomNo;
    Button addRoomsbutton;
    String accessToken,rooms=null,rentAmount=null,roomType=null,buildingName;
    int roomCapacity=0;
    List<AddedRoomModel> addedRooms;
    RecyclerView addedRoomsList;
    AddedRoomsAdapter adapter;
    ProgressDialog progressDialog;
    boolean isFinished=false;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    }

    public void enable()
    {

        addRoomsbutton.setClickable(true);
    }
    public void saveRooms(View v) {
        addRoomsbutton.setClickable(false);
        rooms=roomNo.getText().toString();
        rentAmount=rentInput.getText().toString();
        if (accessToken == null || rentAmount.equals("") || rooms.equals("")||roomType==null)
        {
            Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show();
            addRoomsbutton.setClickable(true);
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Add a room").setMessage("Are You Sure You Wish To Add a  "+roomType+" room?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                setPostData();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enable();
                }
            }).show();


        }
    }
    public void finishAdding(View v)
    {
        isFinished=true;
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if(!isFinished) {
            new AlertDialog.Builder(this).setTitle("Are You Sure You Want To Go Back?").setMessage("Click Yes If You Have Added All Rooms ").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isFinished=true;
                    onBackPressed();
                }
            }).setNegativeButton("No",null).show();
        }
        else
        super.onBackPressed();
    }

    void setPostData() throws JSONException {
        Toast.makeText(getApplicationContext(), "Processing!", Toast.LENGTH_SHORT).show();
        Log.i("addRoomsProcess","sending");
        JSONObject roomsData=new JSONObject();
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        roomsData.put("roomType",roomType);
        roomsData.put("roomRent",Integer.parseInt(rentAmount));
        roomsData.put("roomNo",rooms);
        roomsData.put("date",dateFormat.format(new Date()).toString());
        if(roomCapacity!=0)
            roomsData.put("roomCapacity",roomCapacity);
        if(accessToken!=null)
            roomsData.put("auth",accessToken);
        roomsData.put("ownerId",LoginActivity.sharedPreferences.getString(
                "ownerId",null));
        String buildingId=getIntent().getStringExtra("buildingId");
        if(buildingId!=null)
        roomsData.put("buildingId",buildingId);
        manualActivity.SendToken task = new manualActivity.SendToken();
        task.execute(LoginActivity.MAINURL+"/rooms/addRooms",roomsData.toString());
        progressDialog=new ProgressDialog(manualActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Adding Room No"+rooms);
        progressDialog.setTitle("Adding Room");
        progressDialog.setMax(100);
        progressDialog.show();
    }
    public class SendToken extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Accept","application/json");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.connect();
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(params[1]);
                Log.i("manual rooms addData",params[1]);
                int tokenRecieved = connection.getResponseCode();
                Log.i("manual rooms addResp", String.valueOf(tokenRecieved));
                return String.valueOf(tokenRecieved);

            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            response(s);
            progressDialog.dismiss();
        }
    }
    public void response(String s) {
        if (s != null)
            if (s.equals("200")) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                //keyboard dissapears code
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rentInput.getWindowToken(), 0);
                //adding in recycler View
                addedRooms.add(new AddedRoomModel(rooms,roomType,rentAmount,buildingName));
                adapter.notifyDataSetChanged();
                enable();
            }
            else if(s.equals("422"))
            {
                enable();
                Toast.makeText(this, "Room With Same No. Already Exists", Toast.LENGTH_SHORT).show();
            }
            else {
                enable();
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        else {
            enable();
            Toast.makeText(this, "Please Check Your Internet Connection And Try Later!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_add_rooms_manual);
        addRoomsbutton= (Button) findViewById(R.id.addroomsButton);
        roomNo=(EditText) findViewById(R.id.roomdetailInput);
        rentInput=(EditText)findViewById(R.id.rentInput);
        buildingName=getIntent().getStringExtra("buildingName");
        addedRooms=new ArrayList<>();
        addedRoomsList=(RecyclerView)findViewById(R.id.addedRoomsRv);
        adapter=new AddedRoomsAdapter(addedRooms);
        addedRoomsList.setHasFixedSize(true);
        addedRoomsList.setAdapter(adapter);
        LinearLayoutManager lm=new LinearLayoutManager(getApplicationContext());
        addedRoomsList.setLayoutManager(lm);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add Rooms");
        accessToken= LoginActivity.sharedPreferences.getString("token",null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String[] items={"Room Type","Single","Double","Triple"};
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.single)
                {
                    roomType=items[1];
                    roomCapacity=1;
                }
                else if(checkedId==R.id.doubleBtn)
                {
                    roomType=items[2];
                    roomCapacity=2;
                }
                else if(checkedId==R.id.triple)
                {
                    roomType=items[3];
                    roomCapacity=3;
                }

            }
        });
    }
}
