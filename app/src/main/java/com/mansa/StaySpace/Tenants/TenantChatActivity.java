package com.mansa.StaySpace.Tenants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mansa.StaySpace.DataModels.Chat;
import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Tenants.Services.GetTenantPeersService;
import com.mansa.StaySpace.Tenants.Services.SendChatTenantService;
import com.mansa.StaySpace.Tenants.Adapters.ChatAdapter;
import com.mansa.StaySpace.Tenants.Services.GetTenantPeerChatService;

import java.util.ArrayList;
import java.util.List;

public class TenantChatActivity extends AppCompatActivity {
    public static String name,chatId;
    static RecyclerView chatsRv;
    static List<Chat> chats;
    static ChatAdapter adapter;
    EditText input;
   public static boolean isGroup=false;
   public static boolean isRunning=false;
   static boolean isFetched=false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent getIntent=getIntent();
        name=getIntent.getStringExtra("name");
        chatId=getIntent.getStringExtra("chatId");
        isGroup=getIntent.getBooleanExtra("isGroup",false);
        if(name!=null)
            setTitle(name);
        else
            setTitle("Stay Space");
        input=(EditText)findViewById(R.id.tenantChatInput);
        chatsRv=(RecyclerView)findViewById(R.id.tenantChatMessages);
        chats=new ArrayList<>();
        adapter=new ChatAdapter(chats);
        LinearLayoutManager lm=new LinearLayoutManager(getApplicationContext());
        lm.setStackFromEnd(true);
        chatsRv.setLayoutManager(lm);
        chatsRv.setHasFixedSize(true);
        chatsRv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning=true;
        if(chatId!=null)
        {
            Intent refreshChatIntent=new Intent(getApplicationContext(), GetTenantPeerChatService.class);
            refreshChatIntent.putExtra("chatId",chatId);
            refreshChatIntent.putExtra("isGroup",isGroup);
            startService(refreshChatIntent);
        }
        setData(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning=false;
    }

    public static void updateNow(int which)
    {
        if(chats!=null)
            setData(which);
    }

    static void setData(int which)
    {
        //which is 0 from getPeersChats and 1 from sendChat
        if(which==0) {
            if (GetTenantPeerChatService.chats != null) {
                Log.i("working","u");
                chats.clear();
                chats.addAll(GetTenantPeerChatService.chats);
                adapter.notifyDataSetChanged();
                chatsRv.scrollToPosition(chats.size()-1);
                isFetched=true;
            }
        }
        else
        {
            /*   if(SendChatTenantService.chats!=null)
            {
                chats.clear();
                chats.addAll(SendChatTenantService.chats);
                adapter.notifyDataSetChanged();
                chatsRv.scrollToPosition(chats.size()-1);
            }*/
            if(SendChatTenantService.sentChat!=null)
            {
                chats.remove(chats.size()-1);
                chats.add(SendChatTenantService.sentChat);
                adapter.notifyDataSetChanged();
                chatsRv.scrollToPosition(chats.size()-1);
            }


        }
    }

    public void sendMessage(View view) {
        if(isFetched) {
            String message = input.getText().toString();
            if (!message.equals("")) {
                chats.add(new Chat(message, "name", chatId, "Sending...", "date", false, true, isGroup));
                adapter.notifyDataSetChanged();
                input.setText("");
                Intent i = new Intent(getApplicationContext(), SendChatTenantService.class);
                i.putExtra("chatId", chatId);
                i.putExtra("msg", message);
                i.putExtra("isGroup", isGroup);
                startService(i);
            }
        }
        else
            Toast.makeText(this, "Please Wait,Loading Messages.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        chats.clear();
        adapter.notifyDataSetChanged();
        if(GetTenantPeerChatService.chats!=null)
            GetTenantPeerChatService.chats.clear();
        super.onBackPressed();
        finish();
    }
}
