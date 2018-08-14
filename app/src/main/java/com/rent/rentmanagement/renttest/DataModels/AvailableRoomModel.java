package com.rent.rentmanagement.renttest.DataModels;

public class AvailableRoomModel {
    String roomType,roomNo,roomRent,_id,user_id,ownerName;

    public AvailableRoomModel(String roomType, String roomNo, String roomRent, String _id, String user_id, String ownerName) {
        this.roomType = roomType;
        this.roomNo = roomNo;
        this.roomRent = roomRent;
        this._id = _id;
        this.user_id = user_id;
        this.ownerName = ownerName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getRoomRent() {
        return roomRent;
    }

    public String get_id() {
        return _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getOwnerName() {
        return ownerName;
    }
}
