package com.android.example.cameraxapp.Model;

import java.math.BigInteger;

public class Visitors {
    private String documentType;
    private String visitorDocNo;
    private String visitorFullName;
    private String visitorPhone;
    private String floorNo;
    private String organization;
    private String vehicleNumberPlate;
    private String signedInBy;
    private String BuildingID;

    public Visitors(String documentType, String visitorDocNo, String visitorFullName,
                    String visitorPhone, String floorNo, String organization,
                    String vehicleNumberPlate, String signedInBy, String buildingID) {
        this.documentType = documentType;
        this.visitorDocNo = visitorDocNo;
        this.visitorFullName = visitorFullName;
        this.visitorPhone = visitorPhone;
        this.floorNo = floorNo;
        this.organization = organization;
        this.vehicleNumberPlate = vehicleNumberPlate;
        this.signedInBy = signedInBy;
        BuildingID = buildingID;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getVisitorDocNo() {
        return visitorDocNo;
    }

    public void setVisitorDocNo(String visitorDocNo) {
        this.visitorDocNo = visitorDocNo;
    }

    public String getVisitorFullName() {
        return visitorFullName;
    }

    public void setVisitorFullName(String visitorFullName) {
        this.visitorFullName = visitorFullName;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    public String getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(String floorNo) {
        this.floorNo = floorNo;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getVehicleNumberPlate() {
        return vehicleNumberPlate;
    }

    public void setVehicleNumberPlate(String vehicleNumberPlate) {
        this.vehicleNumberPlate = vehicleNumberPlate;
    }

    public String getSignedInBy() {
        return signedInBy;
    }

    public void setSignedInBy(String signedInBy) {
        this.signedInBy = signedInBy;
    }

    public String getBuildingID() {
        return BuildingID;
    }

    public void setBuildingID(String buildingID) {
        BuildingID = buildingID;
    }
}
