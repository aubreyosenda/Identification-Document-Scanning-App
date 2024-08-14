package com.vanatel.sidar.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EndpointsController {

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

    @GetMapping("/company/register")
    public String registerCompany() { return "./company/register-company.html"; }

    @GetMapping("/company/login")
    public String companyLogin() { return "./company/company-login.html"; }

    @GetMapping ("/company/profile")
    public String companyProfile() { return "./company/company-profile.html"; }

    @GetMapping("/company/buildings")
    public String buildings() { return "./company/buildings.html"; }

    @GetMapping("/company/buildings/organizations")
    public String organizations() { return "./company/organizations.html"; }

    @GetMapping("/company/buildings/personnel")
    public String buildingsPersonnel() { return "./company/personnel.html"; }



//    Visitor Section
    @GetMapping ("/visitor")
    public String visitors() {  return "./visitor/index.html"; }

    @GetMapping("/visitor/list")
    public String visitorsList() {
        return "./visitor/show-visitors.html";
    }

    @GetMapping("/visitor/register")
    public String registerVisitor() {
        return "./visitor/register-visitor.html";
    }

    @GetMapping("/visitor/signout")
    public String signOutVisitor() {
        return "./visitor/signout-visitor.html";
    }

}
