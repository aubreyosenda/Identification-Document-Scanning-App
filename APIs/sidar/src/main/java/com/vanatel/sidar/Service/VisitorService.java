package com.vanatel.sidar.Service;

import com.vanatel.sidar.Model.VisitorDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public interface VisitorService {
    String registerVisitor(VisitorDetails visitorDetails);
    String signOutVisitor(String documentNo, String signedOutBy);
    List<VisitorDetails> findAllVisitors();
//    TODO
    VisitorDetails findVisitorByDocNo(String documentNo);
    List<VisitorDetails> findVisitorsByName(String visitorName);
    List<VisitorDetails> findVisitorsByPhone(long phoneNumber);
}
