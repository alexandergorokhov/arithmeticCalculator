package org.challenge.controller;

import static org.challenge.util.Constants.RANDOM_STRING;
import static org.challenge.util.Constants.RANDOM_STRING_CREATED;
import static org.challenge.util.Constants.RANDOM_STRING_ERROR;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.challenge.service.ArithmeticCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
     * ex:http://localhost:8080/api/v1/calculator/randomString
     * Creates a random string of length 10 with a-z lowercase characters
     * @return <200>Operation successful<200/>
     * <500>Internal Error<500/>
     */
    @ApiOperation(value = "Creates a random string", notes = "Creates a random string of length 10 with a-z lowercase characters")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = RANDOM_STRING_CREATED),
        @ApiResponse(responseCode = "500", description = RANDOM_STRING_ERROR)})
    @GetMapping(value = "/randomString", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> randomString() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                new Response(1, RANDOM_STRING,
                    service.randomString(),
                    HttpStatus.OK.value()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new Response(1, RANDOM_STRING,
                    RANDOM_STRING_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}
