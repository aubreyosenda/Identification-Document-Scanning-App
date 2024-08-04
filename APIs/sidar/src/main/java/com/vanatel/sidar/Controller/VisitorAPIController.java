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

//    @PutMapping("/signout")
//    public String signOutVisitor(@RequestBody VisitorDetails visitorDetails) {
//        visitorService.signOutVisitor(visitorDetails);
//        return "Visitor signed out successfully";
//    }

    @GetMapping("/findbydocno")
    public ResponseEntity<VisitorDetails> findVisitorByDocNo(@RequestParam String documentNo) {
        VisitorDetails visitorDetails = visitorService.findVisitorByDocNo(documentNo);
        if (visitorDetails != null) {
            return ResponseEntity.ok(visitorDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findbyname")
    public ResponseEntity<List<VisitorDetails>> findVisitorsByName(@RequestParam String visitorName) {
        List<VisitorDetails> visitorDetailsList = visitorService.findVisitorsByName(visitorName);
        if (visitorDetailsList != null && !visitorDetailsList.isEmpty()) {
            return ResponseEntity.ok(visitorDetailsList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findbyphone")
    public ResponseEntity<List<VisitorDetails>> findVisitorsByPhone(@RequestParam long phoneNumber) {
        List<VisitorDetails> visitorDetailsList = visitorService.findVisitorsByPhone(phoneNumber);
        if (visitorDetailsList != null && !visitorDetailsList.isEmpty()) {
            return ResponseEntity.ok(visitorDetailsList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
