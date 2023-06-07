package org.challenge.controller;

import static org.challenge.util.Constants.ARITHMETIC_OPERATION_ERROR;
import static org.challenge.util.Constants.ARITHMETIC_OPERATION_NOT_SUPPORTED;
import static org.challenge.util.Constants.ARITHMETIC_OPERATION_PERFORMED;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.challenge.dto.OperationDTO;
import org.challenge.exception.OperationNotSupported;
import org.challenge.service.ArithmeticCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * ex:http://localhost:8080/api/v1/calculator/operation
     * Performs the arithmetic operation on the operands
     *
     * @param operationId
     * @param operand1
     * @param operand2
     * @return <200>Operation successful<200/>
     * <400>Not Supported Operation<400/>
     * <500>Internal Error<500/>
     */
    @ApiOperation(value = "Perform arithmetic operation", notes = "Performs an arithmetic operation specified by the operationId on the operands")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = ARITHMETIC_OPERATION_PERFORMED),
        @ApiResponse(responseCode = "400", description = ARITHMETIC_OPERATION_NOT_SUPPORTED),
        @ApiResponse(responseCode = "500", description = ARITHMETIC_OPERATION_ERROR)})
    @PostMapping(value = "/arithmeticOperation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> arithmeticOperation(@RequestParam(name = "operationId", required = true) int operationId,
        @RequestParam(name = "operand1") double operand1,
        @RequestParam(name = "operand2") double operand2) {
        String result;
        long userId = 1;
        OperationDTO dto;
        try {
            dto = service.validateAndPerformOperation(userId, operationId, operand1, operand2);

            return ResponseEntity.status(HttpStatus.OK).body(
                new Response(operationId, dto.getType(),
                    dto.getResponse(),
                    HttpStatus.OK.value()));
        } catch (OperationNotSupported e) {
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
