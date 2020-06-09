package com.eccsm.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthConfig {


    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
}
