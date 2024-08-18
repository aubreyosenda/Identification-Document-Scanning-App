package com.android.example.cameraxapp.Model;

public class Organization {
    private int organizationId;
    private String organizationName;
    private int floorNumber;
    private String buildingId;

    public Organization() {
    }

    public Organization(int organizationId, String organizationName, int floorNumber, String buildingId) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.floorNumber = floorNumber;
        this.buildingId = buildingId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

}
