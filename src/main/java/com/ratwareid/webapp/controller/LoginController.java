package com.ratwareid.webapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController extends BaseController{

    // Login form
    @RequestMapping("/login")
    public String login() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName().equalsIgnoreCase("anonymousUser")){
            return "login";
        }else{
            return "redirect:/";
        }
    }

    // Login form with error
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName().equalsIgnoreCase("anonymousUser")) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }
}
