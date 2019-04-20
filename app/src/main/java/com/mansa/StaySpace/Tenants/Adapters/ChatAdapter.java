package com.mansa.StaySpace.Tenants.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mansa.StaySpace.DataModels.Chat;
import com.mansa.StaySpace.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{
    List<Chat>chats;

    public ChatAdapter(List<Chat> chats) {
        this.chats = chats;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chat chat=chats.get(position);
        if (chat.isMe())
        {
            holder.my.setVisibility(View.VISIBLE);
            holder.other.setVisibility(View.INVISIBLE);
            holder.group.setVisibility(View.INVISIBLE);
            holder.myChat.setText(chat.getMessage());
            holder.myTime.setText(chat.getTime());
        }
        else
        {
            if(chat.isGroup()) {
                holder.group.setVisibility(View.VISIBLE);
                holder.my.setVisibility(View.INVISIBLE);
                holder.other.setVisibility(View.INVISIBLE);
                holder.groupContact.setText(chat.getAuthorName());
                holder.groupChat.setText(chat.getMessage());
                holder.groupTime.setText(chat.getTime());
            }
            else {
                holder.other.setVisibility(View.VISIBLE);
                holder.my.setVisibility(View.INVISIBLE);
                holder.group.setVisibility(View.INVISIBLE);
                holder.otherChat.setText(chat.getMessage());
                holder.otherTime.setText(chat.getTime());
            }
        }
    }


    @Override
    public int getItemCount() {
        return chats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        Context context;
        LinearLayout my,other,group;
        TextView myChat,otherChat,myTime,otherTime,groupChat,groupTime,groupContact;
        ImageView myStatus;
        public ViewHolder(View v) {
            super(v);
            context=v.getContext();
            myChat=v.findViewById(R.id.myChatTv);
            otherChat=v.findViewById(R.id.otherChatTv);
            myTime=v.findViewById(R.id.myChatTimeTv);
            otherTime=v.findViewById(R.id.otherTimeTv);
            myStatus=v.findViewById(R.id.myChatStatusImage);
            my=v.findViewById(R.id.myChatLl);
            other=v.findViewById(R.id.otherChatLl);
            group=v.findViewById(R.id.groupChatLl);
            groupChat=v.findViewById(R.id.groupChatTv);
            groupTime=v.findViewById(R.id.groupTimeTv);
            groupContact=v.findViewById(R.id.groupContactNameTv);
        }
    }
}
