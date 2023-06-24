package org.challenge.repository;

import org.challenge.domain.User;
import org.challenge.util.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.status = '" + Constants.STATUS_ACTIVE + "'")
    Optional<User> findByUsername( @Param("username") String username);

    }
