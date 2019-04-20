package com.mansa.StaySpace.DataModels;

public class ChatContact {
    String name,recentMsg,chatId;
    boolean isGroup;

    public ChatContact(String name, String recentMsg, String chatId, boolean isGroup) {
        this.name = name;
        this.recentMsg = recentMsg;
        this.chatId = chatId;
        this.isGroup = isGroup;
    }

    public String getName() {
        return name;
    }

    public String getRecentMsg() {
        return recentMsg;
    }

    public String getChatId() {
        return chatId;
    }

    public boolean isGroup() {
        return isGroup;
    }
}
