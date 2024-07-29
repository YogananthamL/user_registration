package com.yogan.user_registration.controllers;

import com.yogan.user_registration.dto.UserDTO;
import com.yogan.user_registration.services.RegistrationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticateController {

    private final RegistrationService service;

    public AuthenticateController(RegistrationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserDTO userDTO){
        return service.register(userDTO);

    }
    @GetMapping("/confirm")
    public String confirm(@RequestParam String token){
        return service.confirm(token);
    }

}
