package org.challenge.controller;

import static org.challenge.util.Constants.RANDOM_STRING;
import static org.challenge.util.Constants.RANDOM_STRING_ERROR;

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
