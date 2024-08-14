package com.vanatel.sidar.Model;


import jakarta.persistence.*;

@Entity
@Table(name="Organizations")
public class OrganizationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private int organizationId;

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Column(name = "floor_number", nullable = false)
    private int floorNumber;

    @Column(name = "building_id", nullable = false)
    private String buildingId;

    public OrganizationDetails() {
    }

    public OrganizationDetails(int organizationId, String organizationName, int floorNumber, String buildingId) {
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
