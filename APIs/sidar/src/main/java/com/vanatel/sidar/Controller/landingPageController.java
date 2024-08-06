package com.vanatel.sidar.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class landingPageController {

    @GetMapping("/")
    public String landingPage() {
        return "index";
    }

    @GetMapping ("/about")
    public String about() {  return "about"; }

//    Company section
    @GetMapping("/register-company")
    public String registerCompany() { return "register-company"; }

    @GetMapping("/company-login")
    public String companyLogin() { return "company-login"; }


//    Visitor Section
    @GetMapping("/register-visitor")
    public String registerVisitor() {
        return "register-visitor";
    }

    @GetMapping("/signout")
    public String signOutVisitor() {
        return "signout-visitor";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }
}
