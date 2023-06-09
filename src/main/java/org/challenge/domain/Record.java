package org.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@Entity
public final class Record {
    @Id
    private final UUID id;
    private final Long operationId;
    private final Long userId;
    private final BigDecimal amount;
    private final BigDecimal userBalance;
    private final String operationResponse;
    private final LocalDateTime operationDate;
    private final LocalDateTime createdAt;

    public Record() {
           this.id = null;
            this.operationId = null;
            this.userId = null;
            this.amount = null;
            this.userBalance = null;
            this.operationResponse = null;
            this.operationDate = null;
            this.createdAt = null;
    }
}
