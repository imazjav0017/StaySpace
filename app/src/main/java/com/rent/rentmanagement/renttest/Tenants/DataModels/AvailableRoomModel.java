package com.rent.rentmanagement.renttest.Tenants.DataModels;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AvailableRoomModel {
    String roomType,roomNo,roomRent,_id,owner_id,ownerName,phoneNo,buildingId,
            buildingName,floors,address,vacancy;

    public AvailableRoomModel(String roomType, String roomNo, String roomRent, String _id, String owner_id, String ownerName, String phoneNo, String buildingId, String buildingName, String floors, String address, String vacancy) {
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
        this.address = address;
        this.vacancy = vacancy;
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

    public String getAddress() {
        return address;
    }

    public String getVacancy() {
        return vacancy;
    }

    @Override
    public String toString() {
        try {
            JSONObject object = new JSONObject();
            object.put("roomNo", roomNo);
            object.put("roomType", roomType);
            object.put("roomRent", roomRent);
            object.put("roomId", _id);
            object.put("ownerId", owner_id);
            object.put("ownerName", ownerName);
            object.put("phNo", phoneNo);
            object.put("buildingId", buildingId);
            object.put("buildingName", buildingName);
            object.put("address",address);
            object.put("vacancy",vacancy);
            return object.toString();
        }catch (JSONException e)
        {
            Log.i("err",e.getMessage());
            return null;
        }



    }
}
