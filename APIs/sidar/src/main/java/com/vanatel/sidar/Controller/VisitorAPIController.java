package com.vanatel.sidar.Controller;

import com.vanatel.sidar.Model.VisitorDetails;
import com.vanatel.sidar.Service.VisitorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("api/v1/visitor")
public class VisitorAPIController {

    @Autowired
    VisitorService visitorService;

    @PostMapping("/register")
    public String registerVisitor(@RequestBody VisitorDetails visitorDetails) {
        visitorService.registerVisitor(visitorDetails);
        return "Visitor registered successfully";
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signOutVisitor(@RequestBody SignOutRequest signOutRequest) {
        String response = visitorService.signOutVisitor(signOutRequest.getDocumentNo(), signOutRequest.getSignedOutBy());
        return ResponseEntity.ok(response);
    }

    static class SignOutRequest {
        private String documentNo;
        private String signedOutBy;

        // Getters and setters
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

//    List Visitors
    @GetMapping("/show-list")
    public List<VisitorDetails> listVisitors() {
        return visitorService.findAllVisitors();
    }
}
