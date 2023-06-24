package org.challenge.controller;

import static org.challenge.controller.util.ResponseConstants.RESPONSE_LOGIN_ID;

import io.jsonwebtoken.security.InvalidKeyException;
import org.challenge.controller.util.Response;
import org.challenge.service.UserService;
import org.challenge.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

    /**
     * ex:http://localhost:8080/api/v1/user/login
     * Login to the system
     * body{
     *     username: "admin",
     *     password: "admin"
     * }
     *
     * @param  @LoginRequest
     * @return <200>ResponseEntity<Response><200/>
     * <403>Bad request<403/>
     * <400>Bad request<400/>
     */
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
       try {
           Authentication authentication = null;
           authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
           );

           SecurityContextHolder.getContext().setAuthentication(authentication);

           // Generate session token
           UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());

           String token = TokenUtil.generateToken(userDetails);

           // Return the session token
           return ResponseEntity.ok(new Response(RESPONSE_LOGIN_ID, "Login", token, HttpStatus.OK.value()));
       }
       catch (InvalidKeyException | BadCredentialsException e){
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(RESPONSE_LOGIN_ID, null, null, HttpStatus.FORBIDDEN.value()));
       }
       catch (RuntimeException e) {
           return ResponseEntity.badRequest().body(new Response(RESPONSE_LOGIN_ID, null, null, HttpStatus.BAD_REQUEST.value()));
       }

    }
}
