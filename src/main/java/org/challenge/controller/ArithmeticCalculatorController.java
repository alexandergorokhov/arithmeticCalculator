package org.challenge.controller;

import static org.challenge.util.Constants.ARITHMETIC_OPERATION_ERROR;
import static org.challenge.util.Constants.ARITHMETIC_OPERATION_NOT_SUPPORTED;
import org.challenge.controller.util.Request;
import org.challenge.controller.util.Response;
import org.challenge.dto.OperationDTO;
import org.challenge.exception.OperationNotSupportedException;
import org.challenge.service.ArithmeticCalculatorService;
import org.challenge.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/calculator")
public class ArithmeticCalculatorController {

    ArithmeticCalculatorService service;

    @Autowired
    public ArithmeticCalculatorController(ArithmeticCalculatorService service) {
        this.service = service;
    }


    /**
     * ex:http://localhost:8080/api/v1/calculator/arithmeticOperation
     * Performs the arithmetic operation on the operands
     *
     * @param header Authorization header
     * @return <200>Operation successful<200/>
     * <400>Not Supported Operation<400/>
     * <500>Internal Error<500/>
     */
    @PostMapping(value = "/arithmeticOperation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> arithmeticOperation(
        @RequestHeader(name = "Authorization", required = true) String header,
        @RequestBody Request request) {
        OperationDTO dto;
        String token = header.replace("Bearer ", "");
        Long operationId = request.getOperationId();
        Double operand1 = request.getOperand1();
        Double operand2 = request.getOperand2();
        String username = TokenUtil.parseToken(token).get("username").toString();
        try {
            dto = service.validateAndPerformOperation(username, operationId, operand1, operand2);
            return ResponseEntity.status(HttpStatus.OK).body(
                new Response(operationId, dto.getType(),
                    dto.getResponse(),
                    HttpStatus.OK.value()));
        } catch (OperationNotSupportedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new Response(operationId, ARITHMETIC_OPERATION_NOT_SUPPORTED,
                    ARITHMETIC_OPERATION_NOT_SUPPORTED,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new Response(operationId, ARITHMETIC_OPERATION_ERROR,
                    ARITHMETIC_OPERATION_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}
