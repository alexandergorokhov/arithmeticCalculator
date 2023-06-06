package org.challenge.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class User {
    private Long id;
    private String userName;
    private char[] password;
    private boolean status;

}
