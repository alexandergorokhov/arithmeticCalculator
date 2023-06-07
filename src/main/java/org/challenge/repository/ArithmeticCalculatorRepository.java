package org.challenge.repository;

import org.challenge.domain.Operation;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;

@Repository
public class ArithmeticCalculatorRepository{
    public BigDecimal getOperationCost(long operationId) {
        return BigDecimal.valueOf(0);
    }

    public Operation getOperation(long operationId) {
        return Operation.builder().build();
    }

    public BigDecimal getUserBalance(long userId) {
        return BigDecimal.valueOf(0);
    }

    public Map<Integer,Operation> getSupportedOperations() {
        return null;
    }
}
