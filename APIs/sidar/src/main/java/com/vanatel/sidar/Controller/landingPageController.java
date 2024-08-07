package com.vanatel.sidar.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class landingPageController {

    @GetMapping("/")
    public String landingPage() {
        return "index.html";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error.html";
    }


//    Company pages
    @GetMapping("/company")
    public String company() {return "./company/index.html";}

    @GetMapping("/about")
    public String about() {return "./company/about.html";}

    @GetMapping ("contact-us")
    public String contactUs() {  return "./company/contact.html"; }

    @GetMapping("/register-company")
    public String registerCompany() { return "./company/register-company.html"; }

    @GetMapping("/company-login")
    public String companyLogin() { return "./company/company-login.html"; }

    @GetMapping ("/company-profile")
    public String companyProfile() { return "./company/company-profile.html"; }


//    Visitor Section
    @GetMapping ("/visitors")
    public String visitors() {  return "./visitors/index.html"; }

    @GetMapping("/register-visitor")
    public String registerVisitor() {
        return "./visitors/register-visitor.html";
    }

    @GetMapping("/signout")
    public String signOutVisitor() {
        return "./visitors/signout-visitor.html";
    }


}
