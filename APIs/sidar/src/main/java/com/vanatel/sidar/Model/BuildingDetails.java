package com.vanatel.sidar.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "buildings")

public class BuildingDetails {

    @Id
    @Column(name = "building_id")
    private String buildingId;

    @Column(name = "building_name", nullable = false)
    private String buildingName;

    @Column(name = "number_of_floors", nullable = false)
    private int numberOfFloors;

    @Column(name = "company_id", nullable = false)
    private String companyID;


    public BuildingDetails() {
    }

    public BuildingDetails(String buildingId, String buildingName,
                           int numberOfFloors, String companyID) {
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.numberOfFloors = numberOfFloors;
        this.companyID = companyID;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }
}
