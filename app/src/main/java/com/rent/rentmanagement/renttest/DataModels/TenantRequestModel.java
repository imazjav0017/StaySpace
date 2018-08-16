package com.rent.rentmanagement.renttest.DataModels;

public class TenantRequestModel {
    String tenantname,tenantId,roomNo;

    public TenantRequestModel(String tenantname, String tenantId, String roomNo) {
        this.tenantname = tenantname;
        this.tenantId = tenantId;
        this.roomNo = roomNo;
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

    @Override
    public String toString() {
        String x="name "+this.tenantname+" id "+this.tenantId+" roomNo "+this.roomNo;
        return x;
    }
}
