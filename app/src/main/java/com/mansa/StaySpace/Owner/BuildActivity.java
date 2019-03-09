package com.mansa.StaySpace.Owner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import java.util.Date;

public class BuildActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    EditText rentInput,roomNo;
    Button addRoomsbutton;
    String accessToken,rooms=null,rentAmount=null,roomType=null;
    int roomCapacity=0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

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
                    .setTitle("Add "+rooms+" rooms!").setMessage("Are You Sure You Wish To Add "+rooms+" "+roomType+" rooms?")
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

    void setPostData() throws JSONException {
        Toast.makeText(getApplicationContext(), "Processing!", Toast.LENGTH_SHORT).show();
        Log.i("addRoomsProcessAuto","sending");
        JSONObject roomsData=new JSONObject();
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        roomsData.put("roomType",roomType);
        roomsData.put("roomRent",Integer.parseInt(rentAmount));
        roomsData.put("noOfRooms",Integer.parseInt(rooms));
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
        SendToken task = new SendToken();
        task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/addRooms",roomsData.toString());
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
                Log.i("json data autoRooms",params[1]);
                int tokenRecieved = connection.getResponseCode();
                Log.i("auto rooms add resp", String.valueOf(tokenRecieved));
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
        }
    }
    public void response(String s) {
        if (s != null)
            if (s.equals("200")) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
              onBackPressed();
            } else {
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
        setContentView(R.layout.owner_activity_add_automatic_rooms);
        addRoomsbutton= (Button) findViewById(R.id.addroomsButton);
        roomNo=(EditText) findViewById(R.id.roomdetailInput);
        rentInput=(EditText)findViewById(R.id.rentInput);
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
