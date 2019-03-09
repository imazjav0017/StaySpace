package com.mansa.StaySpace.DataModels;

public class BuildingListModel {
    int imageId;
    String  buildingName,_id;

    public BuildingListModel(int imageId, String buildingName, String _id) {
        this.imageId = imageId;
        this.buildingName = buildingName;
        this._id = _id;
    }

    public int getImageId() {
        return imageId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String get_id() {
        return _id;
    }
}
