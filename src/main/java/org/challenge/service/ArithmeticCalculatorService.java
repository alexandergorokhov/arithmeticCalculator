package org.challenge.service;

import org.challenge.client.ApiWebClient;
import org.challenge.domain.Operation;
import org.challenge.repository.ArithmeticCalculatorRepository;
import org.challenge.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ArithmeticCalculatorService {

    ArithmeticCalculatorRepository arithmeticCalculatorRepository;
    ApiWebClient apiWebClient;

    @Autowired
    public ArithmeticCalculatorService(ArithmeticCalculatorRepository repository, ApiWebClient apiWebClient) {
        this.arithmeticCalculatorRepository = repository;
        this.apiWebClient = apiWebClient;
    }

    public BigDecimal getOperationCost(int operationId) {
        return arithmeticCalculatorRepository.getOperationCost(operationId);
    }

    public String validateAndPerformOperation(long userId, long operationId, double... params) {
        Operation operation = arithmeticCalculatorRepository.getOperation(operationId);
        BigDecimal balance = arithmeticCalculatorRepository.getUserBalance(userId);
        if (isOperationPossible(operation, balance)) {
            performOperation(userId, operation);
            return "Operation performed";
        }
        return "Operation not performed";
    }

    private void performOperation(long userId, Operation operation, double... params) {
        String result = "";
        switch (operation.getType()) {
            case Constants.ADDITION:
                result = String.valueOf(addition(params[0], params[1]));
                // TODO: save result to DB
                break;
            case Constants.SUBTRACTION:
                result = String.valueOf(subtraction(params[0], params[1]));
                break;
            case Constants.MULTIPLICATION:
                result = String.valueOf(multiplication(params[0], params[1]));
                break;
            case Constants.DIVISION:
                result = String.valueOf(division(params[0], params[1]));
                break;
            case Constants.SQUARE_ROOT:
                result = String.valueOf(root(params[0], 2));
                break;
            case Constants.RANDOM_STRING:
                result = randomString();
                break;
            default:
                result = "Operation not supported";  // TODO add exception

        }

    }

    public String randomString() {
        // TODO move to constant
        return apiWebClient.getRandomString(2,10).get(0);
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
}
