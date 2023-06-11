package org.challenge.controller;

import org.challenge.controller.util.Response;
import org.challenge.service.UserService;
import org.challenge.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        // Perform authentication
        Authentication authentication = null;

        // passwordEncoder.encode(loginRequest.getPassword()
        //     "password":"$2a$10$RMvB0po4bpJGwpcyX20c1u1NmIgl0uUd0t9UWPXwerd2W/nPuruxe"
        authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

// TODO pwd provided plain
        // store pwd encrypted

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate session token
        UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());

        // TDDO - Generate session token
        String token = TokenUtil.generateToken(userDetails);

        // Return the session token
        // TODO move to constants
        return ResponseEntity.ok(new Response(1, "Login", token, HttpStatus.OK.value()));
    }
}
