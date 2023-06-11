package org.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RecordDto {

    private UUID id;
    private Long operationId;
    private Long userId;
    private BigDecimal amount;
    private BigDecimal userBalance;
    private String operationResponse;
    private LocalDateTime operationDate;
    private LocalDateTime createdAt;
}
