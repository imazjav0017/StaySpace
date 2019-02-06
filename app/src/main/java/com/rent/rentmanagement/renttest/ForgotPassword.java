package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.Services.ForgotPasswordService;

public class ForgotPassword extends AppCompatActivity {
    EditText emailInput,otpInput;
    Button sendEmail,verifyOtp,sendEmailAgain;
    static RelativeLayout otpLayout,emailLayout;
    static int currentLayout=0;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Forgot Password");
        emailInput=(EditText) findViewById(R.id.forgotPasswordEmail);
        sendEmail=(Button)findViewById(R.id.sendForgotPasswordRequest);
        sendEmailAgain=(Button)findViewById(R.id.sendEmailAgainButton);
        otpInput=(EditText)findViewById(R.id.otpEditText);
        verifyOtp=(Button)findViewById(R.id.checkOtpButton);
        otpLayout=(RelativeLayout)findViewById(R.id.otpLayout);
        emailLayout=(RelativeLayout)findViewById(R.id.sendEmailLayout);
        final String[] email = {getIntent().getStringExtra("email")};
        if(email[0] !=null)
            emailInput.setText(email[0]);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email[0] =emailInput.getText().toString();
                sendRequest(email[0]);
            }
        });
        sendEmailAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(email[0]);
            }
        });

    }
    void sendRequest(String email)
    {

        if(!email.isEmpty()) {
            Intent i = new Intent(getApplicationContext(), ForgotPasswordService.class);
            i.putExtra("email",email);
            startService(i);
        }
        else
            Toast.makeText(this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
    }

    public static void switchView(int i)
    {
        if(i==1)
        {
            otpLayout.setVisibility(View.VISIBLE);
            emailLayout.setVisibility(View.INVISIBLE);
            currentLayout=1;
        }
        else
        {
            otpLayout.setVisibility(View.INVISIBLE);
            emailLayout.setVisibility(View.VISIBLE);
            currentLayout=0;
        }
    }

    @Override
    public void onBackPressed() {
        if(currentLayout==0)
        super.onBackPressed();
        else
            switchView(0);
    }
}
