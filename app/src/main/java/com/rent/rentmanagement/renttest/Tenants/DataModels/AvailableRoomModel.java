package com.rent.rentmanagement.renttest.Tenants.DataModels;

public class AvailableRoomModel {
    String roomType,roomNo,roomRent,_id,owner_id,ownerName,phoneNo,buildingId,
            buildingName,floors;

    public AvailableRoomModel(String roomType, String roomNo, String roomRent,
                              String _id, String owner_id, String ownerName,
                              String phoneNo, String buildingId, String buildingName,
                              String floors) {
        this.roomType = roomType;
        this.roomNo = roomNo;
        this.roomRent = roomRent;
        this._id = _id;
        this.owner_id = owner_id;
        this.ownerName = ownerName;
        this.phoneNo = phoneNo;
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.floors = floors;
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

    public String getOwner_id() {
        return owner_id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getFloors() {
        return floors;
    }
}
