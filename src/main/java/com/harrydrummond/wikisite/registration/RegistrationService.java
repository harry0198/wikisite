package com.harrydrummond.wikisite.registration;

import com.harrydrummond.wikisite.appuser.AppUser;
import com.harrydrummond.wikisite.appuser.AppUserRole;
import com.harrydrummond.wikisite.appuser.AppUserService;
import com.harrydrummond.wikisite.email.EmailSender;
import com.harrydrummond.wikisite.registration.token.ConfirmationToken;
import com.harrydrummond.wikisite.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }
        String token = appUserService.signUpUser(
                new AppUser(
                        request.getUsername(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
        //todo email registration - just ignored
        String link = "http://localhost:8080/api/v1/registration/confirm?token="+token;
        emailSender.send(request.getEmail(), "<h1>Click below to confirm</h1><p>"+link+"</p>");
        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Token already confirmed");
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(confirmationToken);
        appUserService.enableAppUser(confirmationToken.getAppUser());
        return "confirmed";
    }
}