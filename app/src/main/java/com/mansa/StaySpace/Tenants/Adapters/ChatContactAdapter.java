package com.mansa.StaySpace.Tenants.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mansa.StaySpace.DataModels.ChatContact;
import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Tenants.Services.GetTenantPeerChatService;
import com.mansa.StaySpace.Tenants.TenantChatActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatContactAdapter extends RecyclerView.Adapter<ChatContactAdapter.ViewHolder>{
    List<ChatContact> chatContactList;

    public ChatContactAdapter(List<ChatContact> chatContactList) {
        this.chatContactList = chatContactList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_contact_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ChatContact contact=chatContactList.get(position);
        holder.name.setText(contact.getName());
        holder.recentMsg.setText(contact.getRecentMsg());
        if(contact.isGroup())
        {
            holder.dp.setImageResource(R.drawable.ic_group_icon);
        }
        else
        {
            holder.dp.setImageResource(R.drawable.ima);
        }
        holder.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(holder.context, TenantChatActivity.class);
                i.putExtra("name",contact.getName());
                i.putExtra("chatId",contact.getChatId());
                i.putExtra("isGroup",contact.isGroup());
                Intent getChat=new Intent(holder.context, GetTenantPeerChatService.class);
                getChat.putExtra("chatId",contact.getChatId());
                getChat.putExtra("isGroup",contact.isGroup());
                holder.context.startService(getChat);
               //.putExtra("recentMsg",contact.getRecentMsg());
                holder.context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatContactList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        Context context;
        TextView name,recentMsg;
        RelativeLayout bg;
        CircleImageView dp;
        public ViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            name=itemView.findViewById(R.id.chatContactName);
            recentMsg=itemView.findViewById(R.id.chatContactMsg);
            bg=itemView.findViewById(R.id.chatBgTen);
            dp=itemView.findViewById(R.id.chatItemDP);
        }
    }
}
