package com.rent.rentmanagement.renttest.Services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.ForgotPassword;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
public class ForgotPasswordService extends IntentService {

    public ForgotPasswordService() {
        super("ForgotPasswordService");
    }
    String email;
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            email=intent.getStringExtra("email");
            if(email!=null) {
                try {
                    startTask();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    void startTask() throws JSONException {
        JSONObject data=new JSONObject();
        data.put("email",email);
        ForgotPasswordTask task=new ForgotPasswordTask();
        task.execute("https://sleepy-atoll-65823.herokuapp.com/users/forgot_password",data.toString());
    }
    public String  getResponse(HttpURLConnection connection)
    {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = in.readLine()) != null) {

                sb.append(line);
                break;
            }

            in.close();
            return sb.toString();
        }catch(Exception e)
        {
            return e.getMessage();
        }
    }
    class ForgotPasswordTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url=new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("Accept", "application/json");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.connect();
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(strings[1]);
                Log.i("FORGOTPASSWORDDATA", strings[1]);
                int resp = connection.getResponseCode();
                Log.i("FORGOTPASSWORDRESP",String.valueOf(resp));
                if(resp==200)
                {
                    String response=getResponse(connection);
                    return response;
                }
                else if(resp==400)
                {
                    return "UNF";//user not found
                }
                else
                {
                    String response=getResponse(connection);
                    Log.i("ERRRR",response);
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null)
            {
                if(!s.equals("UNF")) {
                    Log.i("FORGOTPASSWORDRESP", s);
                    Toast.makeText(getApplicationContext(), "Email Sent!", Toast.LENGTH_SHORT).show();
                    ForgotPassword.switchView(1);
                }
                else
                {
                    Toast.makeText(ForgotPasswordService.this, "User Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
