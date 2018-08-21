package com.rent.rentmanagement.renttest.Tenants.DataModels;

public class AvailableRoomModel {
    String roomType,roomNo,roomRent,_id,owner_id,ownerName,phoneNo;

    public AvailableRoomModel(String roomType, String roomNo, String roomRent, String _id, String owner_id, String ownerName, String phoneNo) {
        this.roomType = roomType;
        this.roomNo = roomNo;
        this.roomRent = roomRent;
        this._id = _id;
        this.owner_id = owner_id;
        this.ownerName = ownerName;
        this.phoneNo = phoneNo;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public String getPhoneNo() {
        return phoneNo;
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

    public String getOwnerName() {
        return ownerName;
    }
}
