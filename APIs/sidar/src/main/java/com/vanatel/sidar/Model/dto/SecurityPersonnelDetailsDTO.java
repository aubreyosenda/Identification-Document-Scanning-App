package com.vanatel.sidar.Model.dto;

public class SecurityPersonnelDetailsDTO {
    private int id;
    private String fullName;
    private Long nationalIdNumber;
    private String workIdNumber;
    private Long phoneNumber;
    private String buildingId;
    private String buildingName;
    private String companyId;
    private String companyName;

    // Constructors
    public SecurityPersonnelDetailsDTO() {
    }

    public SecurityPersonnelDetailsDTO(int id, String fullName, Long nationalIdNumber,
                                       String workIdNumber, Long phoneNumber, String buildingId,
                                       String buildingName, String companyId, String companyName) {
        this.id = id;
        this.fullName = fullName;
        this.nationalIdNumber = nationalIdNumber;
        this.workIdNumber = workIdNumber;
        this.phoneNumber = phoneNumber;
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.companyId = companyId;
        this.companyName = companyName;
    }

    // Getters and Setters
    public int getId() {
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

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
