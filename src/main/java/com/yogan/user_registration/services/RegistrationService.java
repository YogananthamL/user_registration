package com.yogan.user_registration.services;

import com.yogan.user_registration.dto.UserDTO;
import com.yogan.user_registration.models.ApplicationUser;
import com.yogan.user_registration.models.Token;
import com.yogan.user_registration.models.UserRole;
import com.yogan.user_registration.repositories.TokenRepository;
import com.yogan.user_registration.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegistrationService {
    private static final String CONFIRMATION_URL="http://localhost:8080/api/v1/authentication/confirm?token=%s";

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final TokenRepository tokenRepository;
    private final EmailService service;
    public RegistrationService(UserRepository repository, PasswordEncoder encoder, TokenRepository tokenRepository, EmailService service) {
        this.repository = repository;
        this.encoder = encoder;
        this.tokenRepository = tokenRepository;
        this.service = service;
    }

    @Transactional
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

        ApplicationUser savedUser = repository.save(appUser);

        String generatedToken= UUID.randomUUID().toString();
        Token token= Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .user(appUser)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();
        tokenRepository.save(token);

        try {
            service.send(userDTO.getEmail(),null,userDTO.getFirstName(),String.format(CONFIRMATION_URL,generatedToken));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return generatedToken;
    }

    public String confirm(String token) {
        Token savedToken=tokenRepository.findByToken(token).orElseThrow(()-> new IllegalStateException("token not found"));
        if(LocalDateTime.now().isAfter(savedToken.getExpiredAt())){
            String generatedToken= UUID.randomUUID().toString();
            Token newToken= Token.builder()
                    .token(generatedToken)
                    .createdAt(LocalDateTime.now())
                    .user(savedToken.getUser())
                    .expiredAt(LocalDateTime.now().plusMinutes(10))
                    .build();
            tokenRepository.save(newToken);

            try {
                service.send(savedToken.getUser().getEmail(),null,savedToken.getUser().getFirstName(),String.format(CONFIRMATION_URL,generatedToken));
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return "Token is expired new token has been sent to your email";
        }
        ApplicationUser user=repository.findById(savedToken.getUser().getId()).orElseThrow(()-> new IllegalStateException("User not found"));
        user.setEnabled(true);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
        return "Your account has been successfully enabled";

    }
}
