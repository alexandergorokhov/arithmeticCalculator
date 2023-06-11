package org.challenge.controller;

import static org.challenge.util.Constants.ARITHMETIC_OPERATION_ERROR;
import static org.challenge.util.Constants.ARITHMETIC_OPERATION_NOT_SUPPORTED;
import static org.challenge.util.Constants.RECORD_OPERATION_NOT_SUPPORTED;
import static org.challenge.util.Constants.RECORD_OPERATION_PERFORMED;

import org.challenge.controller.util.Response;
import org.challenge.domain.Record;
import org.challenge.dto.RecordDto;
import org.challenge.exception.OperationNotSupportedException;
import org.challenge.service.RecordService;
import org.challenge.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class RecordController {

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }


    @GetMapping("/record")
    public ResponseEntity<Response> getRecord(@RequestHeader(name = "Authorization", required = true) String header,
        @RequestParam(value = "pageNumber", required = true) Integer pageNumber,
        @RequestParam(value = "pageSize", required = true) Integer pageSize) {
        String token = header.replace("Bearer ", "");
        String username = TokenUtil.parseToken(token).get("username").toString();

        if (pageNumber < 0 || pageSize < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new Response(1, RECORD_OPERATION_NOT_SUPPORTED,
                    RECORD_OPERATION_NOT_SUPPORTED,
                    HttpStatus.BAD_REQUEST.value()));
        }
        try {
            List<Record> recordList = recordService.getUserRecord(username, pageNumber, pageSize);
            return ResponseEntity.status(HttpStatus.OK).body(
                new Response(1, RECORD_OPERATION_PERFORMED,
                    recordList.toString(),
                    HttpStatus.OK.value()));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new Response(1, ARITHMETIC_OPERATION_ERROR,
                    ARITHMETIC_OPERATION_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }


    }

    @DeleteMapping
    public ResponseEntity<Response> deleteRecord(@RequestHeader(name = "Authorization", required = true) String header,
        @RequestParam(value = "recordId", required = true) UUID recordId) {
        String token = header.replace("Bearer ", "");
        String username = TokenUtil.parseToken(token).get("username").toString();

        try {
            recordService.deleteRecord(username, recordId);
            return ResponseEntity.status(HttpStatus.OK).body(
                new Response(1, RECORD_OPERATION_PERFORMED,
                    RECORD_OPERATION_PERFORMED,
                    HttpStatus.OK.value()));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new Response(1, ARITHMETIC_OPERATION_ERROR,
                    ARITHMETIC_OPERATION_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }


    }
}
