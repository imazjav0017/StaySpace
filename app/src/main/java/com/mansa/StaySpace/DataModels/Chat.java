package com.mansa.StaySpace.DataModels;

public class Chat {
    String Message,chatId,time,date,authorName;
    boolean isOwner,isMe,isGroup;

    public Chat(String message,String name, String chatId, String time, String date, boolean isOwner, boolean isMe,boolean isGroup) {
        Message = message;
        this.authorName=name;
        this.chatId = chatId;
        this.time = time;
        this.date = date;
        this.isOwner = isOwner;
        this.isMe = isMe;
        this.isGroup=isGroup;
    }

    public String getMessage() {
        return Message;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public boolean isMe() {
        return isMe;
    }

    public String getChatId() {
        return chatId;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public String getAuthorName() {
        return authorName;
    }
}
