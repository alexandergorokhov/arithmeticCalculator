package org.challenge.service;

import org.challenge.client.ApiWebClient;
import org.challenge.domain.Operation;
import org.challenge.dto.OperationDTO;
import org.challenge.exception.OperationNotSupported;
import org.challenge.repository.ArithmeticCalculatorRepository;
import org.challenge.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArithmeticCalculatorService {

    ArithmeticCalculatorRepository arithmeticCalculatorRepository;
    ApiWebClient apiWebClient;

    private static HashMap<Integer, Operation> operations = new HashMap<>();

    @Autowired
    public ArithmeticCalculatorService(ArithmeticCalculatorRepository repository, ApiWebClient apiWebClient) {
        this.arithmeticCalculatorRepository = repository;
        this.apiWebClient = apiWebClient;
        operations.putAll(arithmeticCalculatorRepository.getSupportedOperations());
    }

    public BigDecimal getOperationCost(int operationId) {
        return arithmeticCalculatorRepository.getOperationCost(operationId);
    }

    public OperationDTO validateAndPerformOperation(long userId, long operationId, double... params) {
        Operation operation = arithmeticCalculatorRepository.getOperation(operationId);
        BigDecimal balance = arithmeticCalculatorRepository.getUserBalance(userId);
        OperationDTO dto = new OperationDTO();
        dto.setId(operation.getId());
        dto.setType(operation.getType());
        if (isOperationPossible(operation, balance)) {
            dto.setResponse(performOperation(userId, operation).toString());
        } else {
            dto.setResponse(Constants.INSUFFICIENT_FUNDS);
        }
        return dto;
    }

    private StringBuilder performOperation(long userId, Operation operation, double... params) {
        StringBuilder result = new StringBuilder();
        switch (operation.getType()) {
            case Constants.ADDITION:
                result = new StringBuilder().append(addition(params[0], params[1]));
                // TODO: save result to DB
                break;
            case Constants.SUBTRACTION:
                result = result.append(subtraction(params[0], params[1]));
                break;
            case Constants.MULTIPLICATION:
                result = result.append(multiplication(params[0], params[1]));
                break;
            case Constants.DIVISION:
                result = result.append(division(params[0], params[1]));
                break;
            case Constants.SQUARE_ROOT:
                result = result.append(root(params[0], 2));
                break;
            case Constants.RANDOM_STRING:
                result = result.append(randomString());
                break;
            default:
                throw new OperationNotSupported(Constants.ARITHMETIC_OPERATION_NOT_SUPPORTED);
        }
        return result;
    }

    public String randomString() {
        // TODO move to constant
        return apiWebClient.getRandomString(2, 10).get(0).toString();
    }

    private Double root(double param, int i) {
        return Math.pow(param, 1.0 / i);
    }


    public boolean isOperationPossible(Operation operation, BigDecimal balance) {
        return operation.getCost().compareTo(balance) >= 0;
    }

    public Double addition(double param, double param1) {
        return param + param1;
    }

    private Double subtraction(double param, double param1) {
        return param - param1;
    }

    private Double multiplication(double param, double param1) {
        return param * param1;
    }

    private Double division(double param, double param1) {
        return param / param1;
    }

    public Map<Integer, Operation> getOperationSupportedOperations() {
        return operations;
    }
}
