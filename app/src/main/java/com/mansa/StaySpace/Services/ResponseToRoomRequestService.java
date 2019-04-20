package com.mansa.StaySpace.Services;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mansa.StaySpace.Adapters.TenantRequestAdapter;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.Owner.MainActivity;

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

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ResponseToRoomRequestService extends IntentService {

    public ResponseToRoomRequestService() {
        super("ResponseToRoomRequestService");
    }



    String data,tenantname;
    boolean response;
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            data=intent.getStringExtra("data");
            tenantname=intent.getStringExtra("tenantName");
            response=intent.getBooleanExtra("response",false);
            try {
                startTask();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    void startTask() throws JSONException {
        JSONObject jsonData=new JSONObject(data);
        SendRoomRequestResponseTask task=new SendRoomRequestResponseTask(tenantname,response);
        task.execute(LoginActivity.MAINURL+"/users/responseToRoomRequest",jsonData.toString());
    }
     class SendRoomRequestResponseTask extends AsyncTask<String,Integer,String> {
        String name;
        boolean response;

        public SendRoomRequestResponseTask(String name, boolean response) {
            this.name = name;
            this.response = response;
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
                Log.i("REQUESTRESPONSEDATA", params[1]);
                int resp = connection.getResponseCode();
                Log.i("REQUESTRESPONSERESP",String.valueOf(resp));
                if(resp==200)
                {
                    String response=getResponse(connection);
                    return response;
                }
                else if(resp==422)
                {
                    return "422";
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
                Log.i("REQUESTRESPONSERESP", s);
                if(s.equals("422"))
                {
                    Toast.makeText(getApplicationContext(), "Tenant Already Checked in with some other room/owner", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (response)
                        Toast.makeText(getApplicationContext(), "Added " + name + " Successfully!", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(getApplicationContext(), "Rejected " + name + " !", Toast.LENGTH_SHORT).show();
                    }

                }
                startService(new Intent(getApplicationContext(), GetRoomRequestsService.class));
                startService(new Intent(getApplicationContext(), GetAllTenantsService.class));
                startService(new Intent(getApplicationContext(), GetRoomsService.class));
                if(TenantRequestAdapter.progressDialog!=null)
                {
                    TenantRequestAdapter.progressDialog.dismiss();
                }

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection and try later!", Toast.LENGTH_SHORT).show();
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



}
