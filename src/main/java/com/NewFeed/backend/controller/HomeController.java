package com.NewFeed.backend.controller;

import com.auth.configuration.security.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/home")
public class HomeController {
    @GetMapping({"", "/signUp", "/register", "/SignIn", "/oauth2/redirect",
            "/register/verifyEmail", "/requestResetLink", "/resetPassword",
            "/feed", "/messaging"})
    public ModelAndView getUser(Model model) {
        return new ModelAndView("../static/home/index");
    }
}
