package com.vanatel.sidar.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class landingPageController {

    @GetMapping("/")
    public String landingPage() {
        return "index";
    }

    @GetMapping("/register")
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
