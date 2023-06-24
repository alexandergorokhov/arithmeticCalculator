package controller;

import static org.challenge.controller.util.ResponseConstants.RESPONSE_LOGIN_ID;

import org.challenge.controller.LoginRequest;
import org.challenge.controller.UserController;
import org.challenge.controller.util.Response;
import org.challenge.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
public class UserControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserController userController;

    @BeforeEach
    public void setup() {
        userController = new UserController(authenticationManager, userService, passwordEncoder);
    }

    @Test
    public void testLogin_ValidCredentials_ReturnsToken() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testuser", "password");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userService.loadUserByUsername(Mockito.anyString())).thenReturn(userDetails);

        String expectedToken = "token";

        Response expectedResponse = new Response(RESPONSE_LOGIN_ID, "Login", expectedToken, HttpStatus.OK.value());

        // Act
        ResponseEntity<Response> responseEntity = userController.login(loginRequest);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(expectedResponse.getId(), responseEntity.getBody().getId());
        Assertions.assertEquals(expectedResponse.getType(), responseEntity.getBody().getType());


    }

    @Test
    public void testLogin_InvalidCredentials_ThrowsUsernameNotFoundException() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new UsernameNotFoundException("Invalid credentials"));

        // Act & Assert
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userController.login(loginRequest);
        });
    }
}

