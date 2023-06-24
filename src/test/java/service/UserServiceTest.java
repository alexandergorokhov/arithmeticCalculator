package service;

import org.challenge.domain.User;
import org.challenge.repository.UserRepository;
import org.challenge.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIsUserExists_UserExists_ReturnsTrue() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        // Act
        boolean result = userService.isUserExists("username");

        // Assert
        Assertions.assertTrue(result);
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void testIsUserExists_UserDoesNotExist_ReturnsFalse() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act
        boolean result = userService.isUserExists("username");

        // Assert
        Assertions.assertFalse(result);
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void testLoadUserByUsername_UserExists_ReturnsUserDetails() {
        // Arrange
        User user = new User();
        user.setUsername("username");
        user.setPassword(new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'});
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userService.loadUserByUsername("username");

        // Assert
        Assertions.assertEquals("username", userDetails.getUsername());
        Assertions.assertEquals("password", userDetails.getPassword());
        Assertions.assertEquals(1, userDetails.getAuthorities().size());
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void testLoadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(UsernameNotFoundException.class, () ->
            userService.loadUserByUsername("username")
        );
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void testFindUserByUserName_UserExists_ReturnsOptionalUser() {
        // Arrange
        User user = new User();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findUserByUserName("username");

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user, result.get());
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void testFindUserByUserName_UserDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findUserByUserName("username");

        // Assert
        Assertions.assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void testUpdateUserBalance() {
        // Arrange
        User user = new User();
        user.setBalance(BigDecimal.ZERO);
        BigDecimal amount = new BigDecimal("100.00");

        // Act
        userService.updateUserBalance(user, amount);

        // Assert
        Assertions.assertEquals(new BigDecimal("100.00"), user.getBalance());

    }
}