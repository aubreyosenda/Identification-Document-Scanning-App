package com.vanatel.sidar.Service.Impl;

import com.vanatel.sidar.DataBaseRepository.VisitorRepository;
import com.vanatel.sidar.Model.VisitorDetails;
import com.vanatel.sidar.Service.VisitorService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class VisitorServiceImpl implements VisitorService {
    VisitorRepository visitorRepository;

    public VisitorServiceImpl(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    @Override
    public String registerVisitor(VisitorDetails visitorDetails) {
//        Set the current time before saving
        visitorDetails.setSignInTime(new Timestamp(System.currentTimeMillis()));

//        Set The Constant details Here (Bulding Name and Signed In account Name)
        visitorDetails.setBuildingName("BID0001");
        visitorDetails.setSignedInBy("GS9854");
        visitorRepository.save(visitorDetails);
        return "Visitor Registered Successfully";
    }

    //    Update Visitor Operation
//    @Override
//    public String signOutVisitor(VisitorDetails visitorDetails){
//        visitorDetails.setSignOutTime(new Timestamp(System.currentTimeMillis()));
//        visitorDetails.setSignedOutBy("Anthony Loner");
//        visitorRepository.save(visitorDetails);
//        return "Visitor Signed Out Successfully";
//    }

    @Override
    public String signOutVisitor(String documentNo, String signedOutBy) {
        Optional<VisitorDetails> optionalVisitorDetails = visitorRepository.findByVisitorDocNo(documentNo);
        if (optionalVisitorDetails.isPresent()) {
            VisitorDetails visitorDetails = optionalVisitorDetails.get();
            visitorDetails.setSignOutTime(new Timestamp(System.currentTimeMillis()));
            visitorDetails.setSignedOutBy(signedOutBy);
            visitorRepository.save(visitorDetails);
            return "Visitor signed out successfully";
        } else {
            return "Visitor not found";
        }
    }


    @Override
    public VisitorDetails findVisitorByDocNo(String documentNo) {
        return visitorRepository.findByVisitorDocNo(documentNo).get();
    }

    @Override
    public List<VisitorDetails> findVisitorsByName(String visitorName) {
        return visitorRepository.findByVisitorFullName(visitorName);
    }

    @Override
    public List<VisitorDetails> findVisitorsByPhone(long phoneNumber) {
        return visitorRepository.findByVisitorPhone(phoneNumber);
    }
}
