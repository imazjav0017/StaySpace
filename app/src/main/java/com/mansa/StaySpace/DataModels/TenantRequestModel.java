package com.mansa.StaySpace.DataModels;

public class TenantRequestModel {
    String tenantname,tenantId,roomNo,roomId,_id,phoneNo,email,checkinDate;

    public TenantRequestModel(String tenantname, String tenantId, String roomNo, String roomId, String _id, String phoneNo, String email, String checkinDate) {
        this.tenantname = tenantname;
        this.tenantId = tenantId;
        this.roomNo = roomNo;
        this.roomId = roomId;
        this._id = _id;
        this.phoneNo = phoneNo;
        this.email = email;
        this.checkinDate = checkinDate;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getTenantname() {
        return tenantname;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String get_id() {
        return _id;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        String x="name "+this.tenantname+" id "+this.tenantId+" roomNo "+this.roomNo;
        return x;
    }
}
