package com.vanatel.sidar.Service;

import com.vanatel.sidar.Model.VisitorDetails;

import java.sql.Timestamp;
import java.util.List;

public interface VisitorService {
    String registerVisitor(VisitorDetails visitorDetails);
    String signOutVisitor(String documentNo, String signedOutBy);
//    String signOutVisitor(VisitorDetails visitorDetails);
    VisitorDetails findVisitorByDocNo(String documentNo);
    List<VisitorDetails> findVisitorsByName(String visitorName);
    List<VisitorDetails> findVisitorsByPhone(long phoneNumber);
}
