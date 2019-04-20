package com.mansa.StaySpace.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mansa.StaySpace.DataModels.ChatContact;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.Fragments.ComplaintsFragmentOwner;

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
import java.util.ArrayList;
import java.util.List;

public class GetOwnerPeersService extends IntentService {
    public static List<ChatContact> chatContacts;
    public GetOwnerPeersService() {
        super("GetTenantPeersService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                startTask();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    void startTask() throws JSONException {
        JSONObject data=new JSONObject();
        String auth= LoginActivity.sharedPreferences.getString("token",null);
        if(auth!=null)
        {
            data.put("auth",auth);
            data.put("isOwner",true);
            GetPeersTask task=new GetPeersTask();
            task.execute(LoginActivity.MAINURL+"/chat/getPeers",data.toString());
        }
        else
        {
            Log.i("GetOwnerPeersService","Auth is Null");
        }
    }
    void setPeers(String s) throws JSONException {
        if(s!=null)
        {
            chatContacts=new ArrayList<>();
            chatContacts.clear();
            JSONObject result=new JSONObject(s);
            JSONArray responses=result.getJSONArray("response");
            for(int i=0;i<responses.length();i++)
            {
                JSONObject peer=responses.getJSONObject(i);
                JSONObject nameObject=peer.getJSONObject("name");
                String name=nameObject.getString("firstName")+" "+nameObject.getString("lastName");
                String chatId=peer.getString("chatId");
                boolean isGroup=peer.getBoolean("isGroup");
                chatContacts.add(new ChatContact(name,"Tap To Send A Message",chatId,isGroup));
            }
            ComplaintsFragmentOwner.updateNow();
        }
    }
    public class GetPeersTask extends AsyncTask<String,Void,String> {
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
                Log.i("GetPeersData", params[1]);
                int resp = connection.getResponseCode();
                Log.i("GetPeersResp", String.valueOf(resp));
                if (resp == 200) {
                    String response = getResponse(connection);
                    return response;
                } else {
                    return null;
                }

            } catch (MalformedURLException e) {
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
            if (s != null) {
                Log.i("GetPeersResponse",s);
                try {
                    setPeers(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
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
