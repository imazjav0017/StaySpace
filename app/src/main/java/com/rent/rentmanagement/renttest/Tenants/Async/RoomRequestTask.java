package com.rent.rentmanagement.renttest.Tenants.Async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rent.rentmanagement.renttest.Tenants.UpdateProfileActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RoomRequestTask extends AsyncTask<String,Integer,String> {
    Context context;

    public RoomRequestTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(params[1]);
            Log.i("data", params[1]);
            int resp = connection.getResponseCode();
            Log.i("RoomRequestSentResp",String.valueOf(resp));
            if(resp==200)
            {
                String response=getResponse(connection);
                return response;
            }
            else if(resp==422)
            {
                return "adhaar";
            }
            else
            {
                return null;
            }

        }catch(MalformedURLException e)
        {
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
        if (s != null) {
            if(s.equals("adhaar"))
            {
                Toast.makeText(context, "Please Update Your adhaar to send request!", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(context, UpdateProfileActivity.class);
                i.putExtra("sendingRequest",true);
                (context).startActivity(i);
            }
            else {
                Toast.makeText(context, "Request Sent!", Toast.LENGTH_SHORT).show();
                Log.i("RoomRequestTask", s);
            }
        }
        else
        {
            Toast.makeText(context, "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
        }
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
}
