package com.android.example.cameraxapp.Model;

public class SignOutRequest {
    private String documentNo;
    private String signedOutBy;

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getSignedOutBy() {
        return signedOutBy;
    }

    public void setSignedOutBy(String signedOutBy) {
        this.signedOutBy = signedOutBy;
    }
}
