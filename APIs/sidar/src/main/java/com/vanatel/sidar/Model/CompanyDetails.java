package com.vanatel.sidar.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table (name = "Company_Details")

public class CompanyDetails {

    @Id
    @Column (name = "company_id")
    private String companyId;

    @Column(name="company_name")
    private String companyName;

    @Column (name =  "company_email")
    private String companyEmailAddress;

    @Column (name = "phone_number")
    private long companyPhoneNumber;

    @Column (name = "company_hq_location")
    private String companyPhysicalAddress;

    @Column(name = "company_username")
    private String companyUsername;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(name = "password")
    private String companyPassword;

    public CompanyDetails() {
    }

    public CompanyDetails(String companyId, String companyName, String companyEmailAddress,
                          long companyPhoneNumber, String companyPhysicalAddress,
                          String companyUsername, Timestamp creationDate, String companyPassword) {

        this.companyId = companyId;
        this.companyName = companyName;
        this.companyEmailAddress = companyEmailAddress;
        this.companyPhoneNumber = companyPhoneNumber;
        this.companyPhysicalAddress = companyPhysicalAddress;
        this.companyUsername = companyUsername;
        this.creationDate = creationDate;
        this.companyPassword = companyPassword;
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

    public String getCompanyEmailAddress() {
        return companyEmailAddress;
    }

    public void setCompanyEmailAddress(String companyEmailAddress) {
        this.companyEmailAddress = companyEmailAddress;
    }

    public long getCompanyPhoneNumber() {
        return companyPhoneNumber;
    }

    public void setCompanyPhoneNumber(long companyPhoneNumber) {
        this.companyPhoneNumber = companyPhoneNumber;
    }

    public String getCompanyPhysicalAddress() {
        return companyPhysicalAddress;
    }

    public void setCompanyPhysicalAddress(String companyPhysicalAddress) {
        this.companyPhysicalAddress = companyPhysicalAddress;
    }

    public String getCompanyUsername() {
        return companyUsername;
    }

    public void setCompanyUsername(String companyUsername) {
        this.companyUsername = companyUsername;
    }

    public Timestamp getCreationDate() { return creationDate; }
    public void setCreationDate(Timestamp creationDate) { this.creationDate = creationDate; }

    public String getCompanyPassword() {
        return companyPassword;
    }

    public void setCompanyPassword(String companyPassword) {
        this.companyPassword = companyPassword;
    }
}
