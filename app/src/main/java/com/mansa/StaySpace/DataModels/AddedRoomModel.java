package com.mansa.StaySpace.DataModels;

public class AddedRoomModel {
    String roomNo,roomType,roomRent,BuildingName;

    public AddedRoomModel(String roomNo, String roomType, String roomRent, String buildingName) {
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.roomRent = roomRent;
        BuildingName = buildingName;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomRent() {
        return roomRent;
    }

    public String getBuildingName() {
        return BuildingName;
    }
}
