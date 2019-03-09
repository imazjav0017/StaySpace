 package com.mansa.StaySpace.Owner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mansa.StaySpace.Fragments.EditProfileFragment;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Services.ChangeOwnerPasswordService;
import com.mansa.StaySpace.Services.EditProfileOwnerService;
import com.mansa.StaySpace.Tenants.EditProfileActivity;
import com.mansa.StaySpace.Tenants.SendRequestActivity;

import org.json.JSONException;
import org.json.JSONObject;

 public class UpdateOwnerProfileActivity extends AppCompatActivity {
     EditText firstName,lastName,email,mobileNo;
     Button save;
     ProgressBar progressBar;
     String id;
     public static AlertDialog resetDialog;
     public static ProgressDialog progressDialog,EProgressDialog;
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
                 email.setEnabled(false);
                 mobileNo.setText(mobNo);
                 firstName.setSelection(fName.length());
                 lastName.setSelection(lName.length());
                 email.setSelection(emailId.length());
                 mobileNo.setSelection(mobNo.length());
             }
     }
     public void changePassword(View view)
     {

         AlertDialog.Builder builder = new AlertDialog.Builder(UpdateOwnerProfileActivity.this);
         View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.reset_password_dialog, null, false);
         final EditText oldPasswd=(EditText)v.findViewById(R.id.oldPasswordInput);
         final EditText newPasswd=(EditText)v.findViewById(R.id.newPasswordInput);
         final EditText newPasswdAgain=(EditText)v.findViewById(R.id.newPasswordAgainInput);
         Button reset=(Button)v.findViewById(R.id.resetPasswordBtn);
         builder.setView(v);
         resetDialog = builder.create();
         resetDialog.show();
         reset.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String op=oldPasswd.getText().toString();
                 String np=newPasswd.getText().toString();
                 String npa=newPasswdAgain.getText().toString();
                 if(npa.equals(np))
                 {
                     progressDialog=new ProgressDialog(UpdateOwnerProfileActivity.this);
                     progressDialog.setTitle("Changing Password");
                     progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                     progressDialog.setMax(100);
                     progressDialog.setMessage("Changing...");
                     Intent i=new Intent(getApplicationContext(), ChangeOwnerPasswordService.class);
                     i.putExtra("op",op);
                     i.putExtra("np",np);
                     progressDialog.show();
                     startService(i);
                 }
                 else
                     Toast.makeText(UpdateOwnerProfileActivity.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
             }
         });
     }
     void saveChanges()
     {
         Log.i("saving","changes");
             String fName = firstName.getText().toString();
             String lName = lastName.getText().toString();
             String emailId = email.getText().toString();
             String mobNo = mobileNo.getText().toString();
             if (fName.equals("") || lName.equals("") || emailId.equals("") || mobNo.equals("")) {
                 Toast.makeText(getApplicationContext(), "Missing Fields!", Toast.LENGTH_SHORT).show();
             }
             else
             {
                 EProgressDialog=new ProgressDialog(UpdateOwnerProfileActivity.this);
                 EProgressDialog.setTitle("Edit Profile");
                 EProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                 EProgressDialog.setMax(100);
                 EProgressDialog.setMessage("Saving...");
                 Intent i=new Intent(getApplicationContext(), EditProfileOwnerService.class);
                 Bundle b=new Bundle();
                 b.putString("fName",fName);
                 b.putString("lName",lName);
                 b.putString("mobNo",mobNo);
                 b.putString("email",emailId);
                 i.putExtras(b);
                 startService(i);
                 EProgressDialog.show();
                 //onBackPressed();
             }
         }

}
