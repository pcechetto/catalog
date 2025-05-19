package com.example.catalog.services;

import com.example.catalog.dto.EmailDTO;
import com.example.catalog.entities.PasswordRecover;
import com.example.catalog.entities.User;
import com.example.catalog.repositories.PasswordRecoverRepository;
import com.example.catalog.repositories.UserRepository;
import com.example.catalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String passwordRecoverUri;

    private final UserRepository userRepository;
    private final PasswordRecoverRepository passwordRecoverRepository;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, PasswordRecoverRepository passwordRecoverRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordRecoverRepository = passwordRecoverRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void createRecoverToken(EmailDTO body) {
        User user = userRepository.findByEmail(body.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        String token = UUID.randomUUID().toString();
        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(body.getEmail());
        entity.setToken(UUID.randomUUID().toString());
        entity.setToken(token);
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60));
        passwordRecoverRepository.save(entity);

        String text = "Access link to reset password:\n\n" + passwordRecoverUri + token
                + "\n\nExpires in " + tokenMinutes + " minutes";

        emailService.sendEmail(body.getEmail(), "Password Recover", text);
    }
}
