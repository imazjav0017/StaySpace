package com.mansa.StaySpace.AsyncTasks;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mansa.StaySpace.LoginActivity;

import org.json.JSONArray;
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

public class LogoutTask extends AsyncTask<String,Void,String> {
    Context context;
    public interface LogoutResp
    {
        void processFinish(Boolean output,Boolean isSuccess);
    }

    LogoutResp logoutResp=null;
    public LogoutTask(Context context,LogoutResp resp) {
        this.context = context;
        this.logoutResp=resp;
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
            Log.i("LOGOUTDATA", params[1]);
            int resp = connection.getResponseCode();
            Log.i("LOGOUTRESP",String.valueOf(resp));
            if(resp==200)
            {
                String response=getResponse(connection);
                return "200";
            }
            else
            {
                return ""+resp;
            }

        }catch(MalformedURLException e)
        {
            e.printStackTrace();
            return null;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s!=null)
        {
            if(s.equals("200"))
            {
                Log.i("LogoutResp", s);
                JSONObject mainObject = null;
                logoutResp.processFinish(true,true);
                Toast.makeText(context, "Logged Out Successfully!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Unable To Logout", Toast.LENGTH_SHORT).show();
                logoutResp.processFinish(true,false);
            }
        }
        else
        {
            Log.i("errrrrr","internet");
            logoutResp.processFinish(true,false);
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
