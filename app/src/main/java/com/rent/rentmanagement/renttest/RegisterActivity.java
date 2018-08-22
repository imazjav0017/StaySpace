package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {
    EditText username,email,password,mobileNo;
    Button register;
    ProgressBar progressBar;
    RadioGroup rg;
    boolean isOwner=false;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
    }

    public class RegisterTask extends AsyncTask<String,Void,String>
    {
        public void enableButton()
        {
            register.setClickable(true);
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url=new URL(params[0]);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                connection.addRequestProperty("Accept","application/json");
                connection.addRequestProperty("Content-Type","application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.connect();

                DataOutputStream outputStream=new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(params[1]);
                int resp=connection.getResponseCode();
                Log.i("resp",String.valueOf(resp));
                if(resp==200) {
                    onBackPressed();
                    return String.valueOf(resp);
                }
                else {
                    enableButton();
                    return String.valueOf(resp);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            if(s!=null)
            {
                if(s.equals("200"))
                {
                    Toast.makeText(getApplicationContext(), "Registered...", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Invalid! Maybe Some Detail is Already In Use", Toast.LENGTH_SHORT).show();

            }
            else Toast.makeText(RegisterActivity.this, "Please Check Your Internet and Try Again!", Toast.LENGTH_SHORT).show();

        }
    }
    public void Register(View v)  {
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        register.setClickable(false);
        try {
            progressBar.setVisibility(View.VISIBLE);
            JSONObject userDetails = new JSONObject();
         userDetails.put("name", username.getText().toString());
            userDetails.put("email", email.getText().toString());
            userDetails.put("password", password.getText().toString());
            userDetails.put("mobileNo",mobileNo.getText().toString());
            userDetails.put("date",dateFormat.format(new Date()).toString());
            userDetails.put("isOwner",isOwner);
            RegisterTask task=new RegisterTask();
            Log.i("data:",userDetails.toString());
            task.execute("https://sleepy-atoll-65823.herokuapp.com/users/signup",userDetails.toString());
        }catch(Exception e)
        {
            progressBar.setVisibility(View.INVISIBLE);
            Log.i("err",e.getMessage());
            register.setClickable(true);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username=(EditText)findViewById(R.id.newUsernameInput);
        progressBar=(ProgressBar)findViewById(R.id.registerProgress);
        email=(EditText)findViewById(R.id.emailInput);
        password=(EditText)findViewById(R.id.newPasswordInput);
        register=(Button)findViewById(R.id.submitRegister);
        mobileNo=(EditText)findViewById(R.id.mobileNo);
        rg=(RadioGroup)findViewById(R.id.registerRadioGroup);
        RadioButton defaultButton=(RadioButton)findViewById(R.id.tenantRadioRegister);
        defaultButton.setChecked(true);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.tenantRadioRegister) {
                    isOwner=false;
                    Log.i("radioClick","T");
                }
               else if(i==R.id.ownerRadioRegister) {
                   isOwner=true;
                    Log.i("radioClick","O");
                }
            }
        });
    }
}
