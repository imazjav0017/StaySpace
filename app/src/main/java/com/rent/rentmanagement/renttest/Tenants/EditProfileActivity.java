package com.rent.rentmanagement.renttest.Tenants;

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

import com.rent.rentmanagement.renttest.LoginActivity;
import com.rent.rentmanagement.renttest.Owner.UpdateOwnerProfileActivity;
import com.rent.rentmanagement.renttest.R;
import com.rent.rentmanagement.renttest.Services.ChangeOwnerPasswordService;
import com.rent.rentmanagement.renttest.Tenants.Services.EditProfileTenantService;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileActivity extends AppCompatActivity {
    EditText firstName,lastName,email,mobileNo,adhaarNo;
    Button save;
    ProgressBar progressBar;
    RadioGroup gendergroup;
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
        setContentView(R.layout.tenant_activity_edit_profile);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firstName=(EditText)findViewById(R.id.EditfirstNameInputT);
        lastName=(EditText)findViewById(R.id.EditLastNameInputT);
        progressBar=(ProgressBar)findViewById(R.id.editProgressT);
        email=(EditText)findViewById(R.id.EditemailInputT);
        email.setEnabled(false);
        adhaarNo=(EditText)findViewById(R.id.editAdhaarNoT);
        save=(Button)findViewById(R.id.editSaveT);
        mobileNo=(EditText)findViewById(R.id.editMobileNoT);
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
        String s=LoginActivity.sharedPreferences.getString("tenantDetail",null);
        JSONObject tenantObject=new JSONObject(s);
        JSONObject name=tenantObject.getJSONObject("name");
        String fName=name.getString("firstName");
        String lName=name.getString("lastName");
        String emailId=tenantObject.getString("email");
        String mobNo=tenantObject.getString("mobileNo");
        firstName.setText(fName);
        lastName.setText(lName);
        email.setText(emailId);
        mobileNo.setText(mobNo);
        firstName.setSelection(fName.length());
        lastName.setSelection(lName.length());
        email.setSelection(emailId.length());
        mobileNo.setSelection(mobNo.length());
        try {
            String adNo = tenantObject.getString("adharNo");
            adhaarNo.setText(adNo);
            adhaarNo.setSelection(adNo.length());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public void changePassword(View view)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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
                    progressDialog=new ProgressDialog(EditProfileActivity.this);
                    progressDialog.setTitle("Changing Password");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMax(100);
                    progressDialog.setMessage("Changing...");
                    Intent i=new Intent(getApplicationContext(), ChangeOwnerPasswordService.class);
                    i.putExtra("op",op);
                    i.putExtra("np",np);
                    startService(i);
                    progressDialog.show();
                }
                else
                    Toast.makeText(EditProfileActivity.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
               // resetDialog.dismiss();
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
            String adNo=adhaarNo.getText().toString();
            if (fName.equals("") || lName.equals("") || emailId.equals("") || mobNo.equals("") ||adNo.equals("")) {
                Toast.makeText(getApplicationContext(), "Missing Fields!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                EProgressDialog=new ProgressDialog(EditProfileActivity.this);
                EProgressDialog.setTitle("Edit Profile");
                EProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                EProgressDialog.setMax(100);
                EProgressDialog.setMessage("Saving...");
                Intent i=new Intent(getApplicationContext(), EditProfileTenantService.class);
                Bundle b=new Bundle();
                b.putString("fName",fName);
                b.putString("lName",lName);
                b.putString("mobNo",mobNo);
                b.putString("adharNo",adNo);
                i.putExtras(b);
                startService(i);
                EProgressDialog.show();
            }
        }


}
