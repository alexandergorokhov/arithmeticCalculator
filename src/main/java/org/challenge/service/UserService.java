package org.challenge.service;

import org.challenge.domain.User;
import org.challenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUserExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user =  userRepository.findByUsername(username);
        if (user.isPresent()) {

            return org.springframework.security.core.userdetails.User.
                        builder().
                        username(username).
                        password(String.valueOf(user.get().getPassword()))
                .authorities("ROLE_USER")
                .build();
        }else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public Optional<User> findUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateUserBalance(User user, BigDecimal amount) {
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }
}
