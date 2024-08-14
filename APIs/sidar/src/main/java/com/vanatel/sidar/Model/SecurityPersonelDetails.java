package com.vanatel.sidar.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "securitypersonnel")
public class SecurityPersonelDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private int id;

    @Column(name="full_name", nullable = false, length = 50)
    private String fullName;

    @Column(name = "national_id_number", nullable = false)
    private Long nationalIdNumber;

    @Column(name="work_id_number", nullable = false, length = 20)
    private String workIdNumber;

    @Column(name="phone_number", nullable = false)
    private Long phoneNumber;

    @Column(name = "building_id", nullable = false)
    private String buildingId;

    public SecurityPersonelDetails() {
    }

    public SecurityPersonelDetails(int id, String fullName, Long nationalIdNumber, String workIdNumber, Long phoneNumber, String buildingId) {
        this.id = id;
        this.fullName = fullName;
        this.nationalIdNumber = nationalIdNumber;
        this.workIdNumber = workIdNumber;
        this.phoneNumber = phoneNumber;
        this.buildingId = buildingId;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getNationalIdNumber() {
        return nationalIdNumber;
    }

    public void setNationalIdNumber(Long nationalIdNumber) {
        this.nationalIdNumber = nationalIdNumber;
    }

    public String getWorkIdNumber() {
        return workIdNumber;
    }

    public void setWorkIdNumber(String workIdNumber) {
        this.workIdNumber = workIdNumber;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }
}
