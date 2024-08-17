package com.android.example.cameraxapp.Model;

public class Vistors {

    String documentType;
    String visitorDocNo, visitorFullName, visitorPhone, floorNo, organization, vehicleNumberPlate;

    public Vistors() {
    }

    public Vistors(String documentType, String visitorDocNo, String visitorFullName, String visitorPhone, String floorNo, String organization, String vehicleNumberPlate) {
        this.documentType = documentType;
        this.visitorDocNo = visitorDocNo;
        this.visitorFullName = visitorFullName;
        this.visitorPhone = visitorPhone;
        this.floorNo = floorNo;
        this.organization = organization;
        this.vehicleNumberPlate = vehicleNumberPlate;
    }

    public Vistors(String selectedDocumentView, String textDocNoView, String textNameView, String textPhoneNoView, String selectedFloor, String selectedOrganization, String workId, String vehicle, String buildingId) {
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
}
