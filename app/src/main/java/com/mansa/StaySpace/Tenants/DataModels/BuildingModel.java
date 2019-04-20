package com.mansa.StaySpace.Tenants.DataModels;

public class BuildingModel {
    String id,name;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BuildingModel(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
