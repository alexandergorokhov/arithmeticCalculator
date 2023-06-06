package org.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public final class Record {
    private final UUID id;
    private final Long operationId;
    private final Long userId;
    private final BigDecimal amount;
    private final BigDecimal userBalance;
    private final String operationResponse;
    private final LocalDateTime operationDate;
}
