package com.rent.rentmanagement.renttest.DataModels;

/**
 * Created by imazjav0017 on 13-03-2018.
 */

public class StudentModel {
    String name;
    String phNo;
    String roomNo;
    String _id;
    String aadharNo;
    String roomId;

    public StudentModel(String name, String phNo, String roomNo, String _id, String roomId,String aadharNo) {
        this.name = name;
        this.phNo = phNo;
        this.roomNo = roomNo;
        this._id = _id;
        this.aadharNo = aadharNo;
        this.roomId = roomId;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public StudentModel(String name, String phNo, String roomNo, String _id,String aadharNo) {
        this.name = name;
        this.phNo = phNo;
        this.roomNo = roomNo;
        this._id = _id;
        this.aadharNo=aadharNo;
    }


    public String getName() {
        return name;
    }

    public String getPhNo() {
        return phNo;
    }

    public String get_id() {
        return _id;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        String result=String.valueOf(aadharNo);
        return result;
    }
}
