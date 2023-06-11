package org.challenge.service;

import org.challenge.client.ApiWebClient;
import org.challenge.domain.Operation;
import org.challenge.domain.User;
import org.challenge.dto.OperationDTO;
import org.challenge.exception.OperationNotSupportedException;
import org.challenge.exception.SaveRecordException;
import org.challenge.repository.OperationRepository;
import org.challenge.repository.RecordRepository;
import org.challenge.util.Constants;
import org.challenge.domain.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArithmeticCalculatorService {

    private static final Logger logger = LoggerFactory.getLogger(ArithmeticCalculatorService.class);

    UserService userService;
    OperationRepository operationRepository;
    RecordService recordService;

    ApiWebClient apiWebClient;

    private static HashMap<Long, Operation> operations = new HashMap<>();

    @Autowired
    public ArithmeticCalculatorService(OperationRepository operationRepository, RecordService recordService, ApiWebClient apiWebClient, UserService userService) {
        this.operationRepository = operationRepository;
        this.recordService = recordService;
        this.userService = userService;
        this.apiWebClient = apiWebClient;
        operationRepository.findAll().forEach(operation -> {
            this.operations.put(operation.getId(), operation);
        });
    }


    public OperationDTO validateAndPerformOperation(String username, Long operationId, Double... params) {
        Optional<User> userOpt = userService.findUserByUserName(username);
        if (!userOpt.isPresent()) {
            throw new UsernameNotFoundException(Constants.USER_NOT_FOUND);
        }
        User user = userOpt.get();
        Operation operation = operationRepository.findById(operationId).get();

        if (!operation.getType().equals(Constants.RANDOM_STRING) && params == null) {
            throw new OperationNotSupportedException(Constants.ARITHMETIC_OPERATION_NOT_SUPPORTED);
        }
        BigDecimal balance = user.getBalance();
        OperationDTO dto = new OperationDTO();
        dto.setId(operation.getId());
        dto.setType(operation.getType());

        if (isOperationPossible(operation, balance)) {
            dto.setResponse(performOperation( operation, params).toString());
            saveRecord(user, operation, balance, dto);
        } else {
            dto.setResponse(Constants.INSUFFICIENT_FUNDS);
        }
        return dto;
    }

    @Transactional
    private void saveRecord(User user, Operation operation, BigDecimal balance, OperationDTO dto) {
        Record record = new Record(UUID.randomUUID(),
            operation.getId(),
            user.getId(),
            operation.getCost(),
            balance.subtract(operation.getCost()),
            dto.getResponse(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            false,
            null);
        try {
            recordService.save(record);
            userService.updateUserBalance(user,operation.getCost().multiply(new BigDecimal(-1)));
        } catch (Exception e) {
            logger.error("Error saving record: " + record.toString());
            throw new SaveRecordException(Constants.SAVE_RECORD_ERROR);
        }
    }

    private StringBuilder performOperation( Operation operation, Double... params) {
        StringBuilder result = new StringBuilder();
        switch (operation.getType()) {
            case Constants.ADDITION:
                result = new StringBuilder().append(addition(params[0], params[1]));
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
                throw new OperationNotSupportedException(Constants.ARITHMETIC_OPERATION_NOT_SUPPORTED);
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
        return balance.compareTo(operation.getCost()) >= 0;
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

    public Map<Long, Operation> getOperationSupportedOperations() {
        return operations;
    }
}
