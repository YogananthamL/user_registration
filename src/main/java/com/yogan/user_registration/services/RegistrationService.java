package com.yogan.user_registration.services;

import com.yogan.user_registration.dto.UserDTO;
import com.yogan.user_registration.models.ApplicationUser;
import com.yogan.user_registration.models.UserRole;
import com.yogan.user_registration.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public RegistrationService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public String register(UserDTO userDTO){
         boolean isUserExist=repository.findByEmail(userDTO.getEmail()).isPresent();
         if(isUserExist){
             throw new IllegalStateException("User Already exist with the same email");
         }
         String encodedPassword=encoder.encode(userDTO.getPassword());
        ApplicationUser appUser= ApplicationUser.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(encodedPassword)
                .role(UserRole.ROLE_USER)
                .build();

         repository.save(appUser);
        return "User has been registered successfully";
    }
}
