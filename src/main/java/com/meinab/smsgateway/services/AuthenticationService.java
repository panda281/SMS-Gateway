package com.meinab.smsgateway.services;

import com.meinab.smsgateway.models.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Base64;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;

    public String login(String basicAuthString){
        String[] credentials = decodeBasicAuth(basicAuthString);
        String username = credentials[0];
        String password = credentials[1];
        Users users = usersService.loadUserByUsername(username);
        if(passwordEncoder.matches(password,users.getPassword()))
            return username;
        else throw new BadCredentialsException("Incorrect username or password");
    }

    private static String[] decodeBasicAuth(String basicAuthString) {
            if (basicAuthString == null || !basicAuthString.startsWith("Basic ")) {
                throw new IllegalArgumentException("Either token mission or Invalid format");
            }
            basicAuthString = basicAuthString.substring(6);
            String decodedCredentials = new String(Base64.getDecoder().decode(basicAuthString));

            String[] credentials = decodedCredentials.split(":");
            if (credentials.length != 2) {
                throw new IllegalArgumentException("Invalid Basic Auth string");
            }

            return credentials;
    }
}
