 package com.rent.rentmanagement.renttest.Owner;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.Fragments.EditProfileFragment;
import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.R;

import org.json.JSONException;
import org.json.JSONObject;

 public class UpdateOwnerProfileActivity extends AppCompatActivity {
     EditText firstName,lastName,email,mobileNo;
     Button save;
     ProgressBar progressBar;
     RadioGroup gendergroup;
     String gender=null,id;
     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         if(item.getItemId()==android.R.id.home)
         {
             onBackPressed();
             return true;
         }
         return false;
     }

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_update_owner_profile);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         firstName=(EditText)findViewById(R.id.EditfirstNameInput);
         lastName=(EditText)findViewById(R.id.EditLastNameInput);
         progressBar=(ProgressBar)findViewById(R.id.editProgress);
         email=(EditText)findViewById(R.id.EditemailInput);
         save=(Button)findViewById(R.id.editSave);
         mobileNo=(EditText)findViewById(R.id.editMobileNo);
         gendergroup=(RadioGroup)findViewById(R.id.editGenderRadioGroup);
         gendergroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup radioGroup, int i) {
                 if(i==R.id.editMaleRadioBtn)
                 {
                     gender="m";
                 }
                 else
                 {
                     gender="f";
                 }
             }
         });
         save.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 saveChanges();
             }
         });
         try {
             setData();
         } catch (JSONException e) {
             e.printStackTrace();
         }
    }
     void setData() throws JSONException {
         String ownerDetails= LoginActivity.sharedPreferences.getString("ownerDetails",null);
             if(ownerDetails!=null)
             {
                 JSONObject ownerObject=new JSONObject(ownerDetails);
                 id=ownerObject.getString("_id");
                 JSONObject name=ownerObject.getJSONObject("name");
                 String fName=name.getString("firstName");
                 String lName=name.getString("lastName");
                 String emailId=ownerObject.getString("email");
                 String mobNo=ownerObject.getString("mobileNo");
                 firstName.setText(fName);
                 lastName.setText(lName);
                 email.setText(emailId);
                 mobileNo.setText(mobNo);
             }
     }
     void saveChanges()
     {
         Log.i("saving","changes");
         if(gender==null)
         {
             Toast.makeText(getApplicationContext(), "Select Your Gender", Toast.LENGTH_SHORT).show();
         }
         else {
             String fName = firstName.getText().toString();
             String lName = lastName.getText().toString();
             String emailId = email.getText().toString();
             String mobNo = mobileNo.getText().toString();
             if (fName.equals("") || lName.equals("") || emailId.equals("") || mobNo.equals("")) {
                 Toast.makeText(getApplicationContext(), "Missing Fields!", Toast.LENGTH_SHORT).show();
             }
             else
             {
                 onBackPressed();
             }
         }

     }
}
