package com.yogan.user_registration.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping({"/welcome", "/home", "/"})
    public String welcome(){
        return "welcome";
    }
}
