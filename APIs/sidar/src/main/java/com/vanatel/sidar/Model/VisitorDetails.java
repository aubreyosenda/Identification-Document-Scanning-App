package com.vanatel.sidar.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "Visitor_Details")
public class VisitorDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "signin_time")
    private Timestamp signInTime;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_no")
    private String visitorDocNo;

    @Column(name = "full_name")
    private String visitorFullName;

    @Column(name = "phone_number")
    private long visitorPhone;

    @Column(name = "floor_number")
    private String floorNo;

    @Column(name = "organization_name")
    private String organization;

    @Column(name = "vehicle_number_plate")
    private String vehicleNumberPlate;

    @Column(name = "signed_in_by")
    private String signedInBy;

    @Column(name = "signout_time")
    private Timestamp signOutTime;

    @Column(name = "signed_out_by")
    private String signedOutBy;

    @Column(name = "building_id")
    private String buildingId;

    // Getters and Setters

    public VisitorDetails() {
    }

    public VisitorDetails(Timestamp signInTime, String documentType, String visitorDocNo,
                          String visitorFullName, long visitorPhone, String floorNo,
                          String organization, String vehicleNumberPlate, String signedInBy,
                          Timestamp signOutTime, String signedOutBy, String buildingId) {

        this.signInTime = signInTime;
        this.documentType = documentType;
        this.visitorDocNo = visitorDocNo;
        this.visitorFullName = visitorFullName;
        this.visitorPhone = visitorPhone;
        this.floorNo = floorNo;
        this.organization = organization;
        this.vehicleNumberPlate = vehicleNumberPlate;
        this.signedInBy = signedInBy;
        this.signOutTime = signOutTime;
        this.signedOutBy = signedOutBy;
        this.buildingId = buildingId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(Timestamp signInTime) {
        this.signInTime = signInTime;
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

    public long getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(long visitorPhone) {
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

    public Timestamp getSignOutTime() {
        return signOutTime;
    }

    public void setSignOutTime(Timestamp signOutTime) {
        this.signOutTime = signOutTime;
    }

    public String getSignedOutBy() {
        return signedOutBy;
    }

    public void setSignedOutBy(String signedOutBy) {
        this.signedOutBy = signedOutBy;
    }

    public String getBuildingName() {
        return buildingId;
    }

    public void setBuildingName(String buildingId) {
        this.buildingId = buildingId;
    }
}
