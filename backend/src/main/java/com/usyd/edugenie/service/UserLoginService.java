package com.usyd.edugenie.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.usyd.edugenie.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserLoginService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private final String CLIENT_ID = "18188062495-splm7ss0dp13pspgsd3fftl8eqse35je.apps.googleusercontent.com";

    public String verifyUser(String credential) {
        // Retrieve user payload from Google
        Payload payload = getPayloadFromGoogle(credential);
        System.out.println("user payload: " + payload);

        // Get or create user from payload
        Users user = findOrCreateUser(payload);
        return jwtTokenProvider.generateJwtToken(user.getEmail());
    }

    /**
     * Protected method to fetch Google payload for testing ease
     */
    protected Payload getPayloadFromGoogle(String credential) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
            GoogleIdToken idToken = verifier.verify(credential);
            if (idToken == null) {
                System.out.println("Invalid ID token.");
                throw new IllegalArgumentException("Invalid ID token.");
            }
            return idToken.getPayload();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Users findOrCreateUser(Payload payload) {
        String email = payload.getEmail();
        Optional<Users> user = usersService.getUserByEmail(email);
        if (user.isPresent()) {
            System.out.println("User already present: " + email);
            return user.get();
        }

        Users newUser = new Users(
            (String) payload.get("given_name"),
            (String) payload.get("family_name"),
            email,
            (String) payload.get("picture"),
            LocalDateTime.of(1970, 1, 1, 0, 0),
            LocalDateTime.now()
        );
        System.out.println("New user created: " + newUser);
        return usersService.createUser(newUser);
    }
}
