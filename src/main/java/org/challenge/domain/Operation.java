package org.challenge.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Operation {
    @Id
    private Long id;
    private String type;
    private BigDecimal cost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Operation() {
    }
}
