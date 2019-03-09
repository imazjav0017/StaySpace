package com.mansa.StaySpace.Services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.mansa.StaySpace.Adapters.OccupiedRoomsAdapter;
import com.mansa.StaySpace.Adapters.TotalRoomsAdapter;
import com.mansa.StaySpace.DataCallBack;
import com.mansa.StaySpace.Fragments.RoomsFragment;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.Owner.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class PaymentService extends IntentService {

    public PaymentService() {
        super("PaymentService");
    }
    String roomId,date,payee,from,reason;
    int amount;
    boolean isPayment;
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            roomId=intent.getStringExtra("roomId");
            date=intent.getStringExtra("date");
            payee=intent.getStringExtra("payee");
            String am=intent.getStringExtra("amount");
            if(am!=null)
            amount=Integer.parseInt(am);
            from=intent.getStringExtra("from");//occ- occupied, tot-total ,else default
            isPayment=intent.getBooleanExtra("isPayment",false);
            try {
                if(isPayment)
                startPayment();
                else {
                    reason=intent.getStringExtra("reason");
                    startReason();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    void startPayment() throws JSONException {
        String auth = LoginActivity.sharedPreferences.getString("token", null);
        JSONObject data=new JSONObject();
        data.put("auth",auth);
        data.put("roomId",roomId);
        data.put("date",date);
        if(payee!=null) {
            data.put("payee", payee);
            data.put("amount", amount);
        }else
            Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show();
        PaymentTask task=new PaymentTask();
        task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/paymentDetail",data.toString());
    }
    void startReason() throws JSONException {
        String auth = LoginActivity.sharedPreferences.getString("token", null);
        JSONObject data=new JSONObject();
        data.put("auth",auth);
        data.put("roomId",roomId);
        data.put("date",date);
        if(reason!=null)
        {
            data.put("reason",reason);
        }
        else
            Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show();


        PaymentTask task=new PaymentTask();
        task.execute("https://sleepy-atoll-65823.herokuapp.com/rooms/paymentDetail",data.toString());

    }
        class PaymentTask extends AsyncTask<String, Void, String> {

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
                Log.i("COLLECTDATA", params[1]);
                int resp = connection.getResponseCode();
                Log.i("COLLECTRESP", String.valueOf(resp));
                if (resp == 200) {
                    return "success";
                } else
                    return "Some Error,check if fields are missings!";

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
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if(TotalRoomsAdapter.progressDialog!=null) {
                TotalRoomsAdapter.progressDialog.dismiss();
            }
            if(OccupiedRoomsAdapter.progressDialog!=null)
            {
                OccupiedRoomsAdapter.progressDialog.dismiss();
            }
            if (response != null) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                if(response.equals("Some Error,check if fields are missings!"))
                {
                    //enable(btn);
                }
                else {
                     startService(new Intent(getApplicationContext(),GetRoomsService.class));
                     //dialog.dismiss
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
            }
        }


    }


}
