package com.mansa.StaySpace.Owner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mansa.StaySpace.AsyncTasks.AddBuildingTask;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UpdateOwnerExtraActivity extends AppCompatActivity {
    EditText buildingName, address, floors, locality, pincode, city;
    Spinner stateSpinner, typeSpinner;
    String buildingVal, addressVal, floorVal, localVal, pinVal, cityVal, type = null, state = null;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_update_owner_extra);
        buildingName = (EditText) findViewById(R.id.updateOwnerBuildingNameInput);
        address = (EditText) findViewById(R.id.updateOwnerAddressInput);
        floors = (EditText) findViewById(R.id.updateOwnerFloorsInput);
        locality = (EditText) findViewById(R.id.buildingLocalityInput);
        pincode = (EditText) findViewById(R.id.buildingPinCodeInput);
        city = (EditText) findViewById(R.id.buildingCityInput);
        stateSpinner = (Spinner) findViewById(R.id.stateSpinner);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Selected", adapterView.getItemAtPosition(i).toString());
                String selected = adapterView.getItemAtPosition(i).toString();
                if (selected.equals("Choose A State"))
                    state = null;
                else
                    state = selected;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        typeSpinner = (Spinner) findViewById(R.id.buildingTypeSpinner);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Selected", adapterView.getItemAtPosition(i).toString());
                String selected = adapterView.getItemAtPosition(i).toString();
                if (selected.equals("Choose A Type"))
                    type = null;
                else
                    type = selected;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Update Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    public void update(View v) {
        Log.i("update", "1");
        buildingVal = buildingName.getText().toString();
        addressVal = address.getText().toString();
        floorVal = floors.getText().toString();
        localVal = locality.getText().toString();
        pinVal = pincode.getText().toString();
        cityVal = city.getText().toString();
        if (buildingVal.matches("") || addressVal.matches("") || floorVal.matches("")
                || localVal.matches("") || pinVal.matches("") || cityVal.matches("")) {
            Toast.makeText(getApplicationContext(), "Missing Fields!", Toast.LENGTH_SHORT).show();
            return;
        } else if (type == null) {
            Toast.makeText(this, "Please Select A Building Type", Toast.LENGTH_SHORT).show();
        } else if (state == null) {
            Toast.makeText(this, "Please Select A State", Toast.LENGTH_SHORT).show();
            return;
        } else {
            JSONObject data = new JSONObject();
            try {
                data.put("name", buildingVal);
                data.put("addressLine1", addressVal);
                data.put("addressLine2", localVal);
                data.put("state", state);
                data.put("city", cityVal);
                data.put("pincode", pinVal);
                data.put("floor", floorVal);
                data.put("type", type);
                data.put("ownerId", LoginActivity.sharedPreferences.getString("ownerId", null));
                data.put("auth", LoginActivity.sharedPreferences.getString("token", null));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AddBuildingTask task = new AddBuildingTask(getApplicationContext(), new AddBuildingTask.AddBuildingResp() {
                @Override
                public void processFinish(Boolean output) {
                    if (output == true) {
                        //will execute when the process completes
                        onBackPressed();
                    }
                }
            },0);
            task.execute(LoginActivity.MAINURL + "/build/addBuild", data.toString());
        }
    }

    public void getCurrentLocation(View v) {

        Log.i("getCurrentLocation", "clicked");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(UpdateOwnerExtraActivity.this);
        if(Build.VERSION.SDK_INT<23)
        {
            getLocation();
        }
        else
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else
            {
               getLocation();
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                getLocation();
            }
        }
        else
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("MissingPermission")
    void getLocation()
    {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.i("Location",""+location.getLatitude()+","+location.getLongitude());
                            getAddress(location);
                        }
                    }
                });
    }
    private class LocationTask extends AsyncTask<Location,Void,List<Address>>
    {

        @Override
        protected List<Address> doInBackground(Location... locations) {
            String result="";
            Geocoder geocoder;
            List<Address> addresses;
            geocoder=new Geocoder(UpdateOwnerExtraActivity.this,Locale.getDefault());
            Location location=locations[0];
            try {
                Double lat=location.getLatitude();
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                //addresses = geocoder.getFromLocation(25.614508,85.177546, 1);
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                return addresses;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            super.onPostExecute(addresses);
            if (addresses != null) {
                Log.i("address", addresses.get(0).toString());
                String addressF = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String cityF = addresses.get(0).getLocality();
                String stateF = addresses.get(0).getAdminArea();
                String countryF = addresses.get(0).getCountryName();
                String postalCodeF = addresses.get(0).getPostalCode();
                String knownNameF = addresses.get(0).getFeatureName();
                if (cityF != null) {
                    city.setText(cityF);
                    city.setSelection(cityF.length());
                }
                if (postalCodeF != null) {
                    pincode.setText(postalCodeF);
                    pincode.setSelection(postalCodeF.length());
                }
                if (stateF != null) {
                    String[] states = getResources().getStringArray(R.array.states);
                    int i = 0;
                    for (String s : states) {
                        if (s.matches(stateF)) {
                            stateSpinner.setSelection(i);
                            break;
                        }
                        i++;
                    }
                }
                if (addressF != null) {
                    String vals[] = addressF.split(",");
                    if(vals.length>4) {
                        String localF = vals[vals.length - 4];
                        localF = localF.trim();
                        locality.setText(localF);
                       /* String addval = "";
                        for (int i = 0; i < vals.length - 4; i++) {
                            String v = vals[i];
                            v = v.trim();
                            addval += v + ",";
                        }
                        address.setText(addval.substring(0, addval.length() - 1));*/
                    }
                    else if(vals.length>3)
                    {
                        String localF = vals[vals.length - 3];
                        localF = localF.trim();
                        locality.setText(localF);
                      /*  String addval = "";
                        for (int i = 0; i < vals.length - 4; i++) {
                            String v = vals[i];
                            v = v.trim();
                            addval += v + ",";
                        }
                        address.setText(addval.substring(0, addval.length() - 1));*/
                    }
                }
            }
            else
            {
                Toast.makeText(UpdateOwnerExtraActivity.this, "Unable To Fetch Address Info!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    void getAddress(Location location)
    {
        LocationTask task=new LocationTask();
        task.execute(location);
    }

}
