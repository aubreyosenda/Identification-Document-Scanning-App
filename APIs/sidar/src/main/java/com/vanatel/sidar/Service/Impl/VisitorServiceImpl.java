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
        visitorRepository.save(visitorDetails);
        return "Visitor Registered Successfully";
    }


    @Override
    public String signOutVisitor(String documentNo, String signedOutBy) {
        Optional<VisitorDetails> optionalVisitorDetails = visitorRepository.findByVisitorDocNoAndSignOutTimeIsNull(documentNo);
        if (optionalVisitorDetails.isPresent()) {
            VisitorDetails visitorDetails = optionalVisitorDetails.get();
            visitorDetails.setSignOutTime(new Timestamp(System.currentTimeMillis()));
            visitorDetails.setSignedOutBy(signedOutBy);
            visitorRepository.save(visitorDetails);
            return "Visitor signed out successfully";
        } else {
            return "Visitor not found or already signed out";
        }
    }


    @Override
    public List<VisitorDetails> findAllVisitors() {
        return visitorRepository.findAll();
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
