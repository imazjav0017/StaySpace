package com.rent.rentmanagement.renttest.DataModels;

public class TenantRequestModel {
    String tenantname,tenantId,roomNo,_id,phoneNo,email;

    public TenantRequestModel(String tenantname, String tenantId, String roomNo, String _id, String phoneNo, String email) {
        this.tenantname = tenantname;
        this.tenantId = tenantId;
        this.roomNo = roomNo;
        this._id = _id;
        this.phoneNo = phoneNo;
        this.email = email;
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

    @Override
    public String toString() {
        String x="name "+this.tenantname+" id "+this.tenantId+" roomNo "+this.roomNo;
        return x;
    }
}
