package org.challenge.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private String userName;
    private char[] password;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {
    }
}
