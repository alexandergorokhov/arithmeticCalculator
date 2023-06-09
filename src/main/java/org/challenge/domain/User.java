package org.challenge.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User {
    @Id
    private Long id;
    private String username;
    private char[] password;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {
    }
}
