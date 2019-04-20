package com.mansa.StaySpace.Tenants.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mansa.StaySpace.DataModels.Chat;
import com.mansa.StaySpace.LoginActivity;
import com.mansa.StaySpace.Tenants.TenantChatActivity;

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

public class GetTenantPeerChatService extends IntentService {
    public static List<Chat>chats;
    String chatId;
    boolean isGroup;
    public GetTenantPeerChatService() {
        super("GetTenantPeerChatService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                chatId=intent.getStringExtra("chatId");
                isGroup=intent.getBooleanExtra("isGroup",false);
                Log.i("isGroup",String.valueOf(isGroup));
                if(chatId!=null)
                startTask();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    void setChats(String s) throws JSONException {
        if(s!=null)
        {
            chats=new ArrayList<>();
            chats.clear();
            JSONObject result=new JSONObject(s);
            JSONObject response=result.getJSONObject("response");
            JSONArray messages=response.getJSONArray("message");
            for(int i=0;i<messages.length();i++)
            {
                JSONObject message=messages.getJSONObject(i);
                String id=message.getString("_id");
                JSONObject authorObject=message.getJSONObject("author");
                String author=authorObject.getString("_id");
                JSONObject nameObj=authorObject.getJSONObject("name");
                String name=nameObj.getString("firstName")+" "+nameObj.getString("lastName");
                String msg=message.getString("msg");
                String time=message.getString("time");
                String date=message.getString("date");
                boolean isMe = false;
                String userId=LoginActivity.sharedPreferences.getString("userId",null);
                if(userId!=null) {
                    Log.i("userCheck",userId+","+author);
                    if (userId.equals(author)) {
                        isMe = true;
                    } else {
                        isMe = false;
                    }
                }
                boolean isOwner=LoginActivity.sharedPreferences.getBoolean("isOwner",false);
                chats.add(new Chat(msg,name,id,time,date,isOwner,isMe,isGroup));
            }
            TenantChatActivity.updateNow(0);

        }
    }
    void startTask() throws JSONException {
        JSONObject data=new JSONObject();
        String auth= LoginActivity.sharedPreferences.getString("token",null);
        if(auth!=null)
        {
            data.put("auth",auth);
            data.put("chatId",chatId);
            GetPeersChatTask task=new GetPeersChatTask();
            task.execute(LoginActivity.MAINURL+"/chat/getPeerChat",data.toString());
        }
        else
        {
            Log.i("GetChat","Auth is Null");
        }
    }
    public class GetPeersChatTask extends AsyncTask<String,Void,String> {
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
                Log.i("GetPeersChatData", params[1]);
                int resp = connection.getResponseCode();
                Log.i("GetPeersChatResp", String.valueOf(resp));
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
                Log.i("GetPeersChatResponse",s);
                try {
                    setChats(s);
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
