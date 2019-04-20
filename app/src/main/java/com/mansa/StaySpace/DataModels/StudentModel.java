package com.mansa.StaySpace.DataModels;

import java.util.List;

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
    boolean isTenant;
    List<String> idProofs;
    public StudentModel(String name, String phNo, String roomNo, String _id, String aadharNo, String roomId, boolean isTenant) {
        this.name = name;
        this.phNo = phNo;
        this.roomNo = roomNo;
        this._id = _id;
        this.aadharNo = aadharNo;
        this.roomId = roomId;
        this.isTenant = isTenant;
    }

    public StudentModel(String name, String phNo, String roomNo, String _id, String aadharNo, String roomId, boolean isTenant, List<String> idProofs) {
        this.name = name;
        this.phNo = phNo;
        this.roomNo = roomNo;
        this._id = _id;
        this.aadharNo = aadharNo;
        this.roomId = roomId;
        this.isTenant = isTenant;
        this.idProofs = idProofs;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public boolean isTenant() {
        return isTenant;
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

    public List<String> getIdProofs() {
        return idProofs;
    }
    @Override
    public String toString() {
        String result=String.valueOf(aadharNo);
        return result;
    }
}
