package com.rent.rentmanagement.renttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends AppCompatActivity {
    EditText emailInput;
    Button sendEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Forgot Password");
        emailInput=(EditText) findViewById(R.id.forgotPasswordEmail);
        sendEmail=(Button)findViewById(R.id.sendForgotPasswordRequest);
        String email=getIntent().getStringExtra("email");
        if(email!=null)
            emailInput.setText(email);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
    }
    void sendRequest()
    {
        Toast.makeText(this, "Email Sent!", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}
