package com.mansa.StaySpace.DataModels;

/**
 * Created by imazjav0017 on 11-02-2018.
 */

public class RoomModel {
    String roomType,roomNo,roomRent,_id,checkInDate,dueAmount,days,dueDate,checkOutDate;
    int roomCapacity,totalRoomCapacity;
    public boolean isEmpty;
    public boolean isRentDue;

//for vacant rooms
    public RoomModel(String roomType, String roomNo, String roomRent, String _id,String checkOutDate,boolean isEmpty,String days,
                     int roomCapacity,int totalRoomCapacity) {
        this.roomType = roomType;
        this.roomNo = roomNo;
        this.roomRent = roomRent;
        this._id = _id;
        this.isEmpty=isEmpty;
        this.checkOutDate=checkOutDate;
        this.days=days;
        this.roomCapacity=roomCapacity;
        this.totalRoomCapacity=totalRoomCapacity;
    }


    //for occupied Rooms

    public RoomModel(String roomType, String roomNo, String roomRent, String dueAmount, String _id, String checkInDate,String dueDate,boolean isEmpty,boolean isRentDue,String days,
                     int roomCapacity,int totalRoomCapacity) {
        this.roomType = roomType;
        this.roomNo = roomNo;
        this.roomRent = roomRent;
        this._id = _id;
        this.checkInDate = checkInDate;
        this.dueDate=dueDate;
        this.dueAmount = dueAmount;
        this.isEmpty=isEmpty;
        this.isRentDue=isRentDue;
        this.days=days;
        this.roomCapacity=roomCapacity;
        this.totalRoomCapacity=totalRoomCapacity;
    }

    public String getDays() {
        return days;
    }

    public String getDueAmount() {
        return dueAmount;
    }

    public String getCheckInDate() {
        return checkInDate;
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

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isRentDue() {
        return isRentDue;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public int getTotalRoomCapacity() {
        return totalRoomCapacity;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }
}
