package org.challenge.controller;

import static org.challenge.controller.util.ResponseConstants.RESPONSE_DELETE_RECORD;
import static org.challenge.controller.util.ResponseConstants.RESPONSE_GET_RECORD;
import static org.challenge.util.Constants.ARITHMETIC_OPERATION_ERROR;
import static org.challenge.util.Constants.RECORD_OPERATION_NOT_SUPPORTED;
import static org.challenge.util.Constants.RECORD_OPERATION_PERFORMED;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.challenge.controller.util.Response;
import org.challenge.controller.util.ResponsePageable;
import org.challenge.service.RecordService;
import org.challenge.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/record")
public class RecordController {

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    /**
     * ex:http://localhost:8080/api/v1/record
     * Get all records
     *
     * @param header Authorization header
     * @param  pageNumber Page number
     * @param  pageSize Page size
     * @return <200>ResponseEntity<ResponsePageable><200/>
     * <400>Not Supported Operation<400/>
     * <500>Internal Error<500/>
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<ResponsePageable> getRecord(@RequestHeader(name = "Authorization", required = true) String header,
        @RequestParam(value = "pageNumber", required = true) Integer pageNumber,
        @RequestParam(value = "pageSize", required = true) Integer pageSize) {
        String token = header.replace("Bearer ", "");
        String username = TokenUtil.parseToken(token).get("username").toString();

        if (pageNumber < 0 || pageSize < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponsePageable(RESPONSE_GET_RECORD, RECORD_OPERATION_NOT_SUPPORTED,
                    null,
                    HttpStatus.BAD_REQUEST.value()));
        }
        try {
            Page recordPage = recordService.getUserRecord(username, pageNumber, pageSize);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String json;
            try {
                 json = objectMapper.writeValueAsString(recordPage);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponsePageable(RESPONSE_GET_RECORD, RECORD_OPERATION_PERFORMED,
                   recordPage,
                    HttpStatus.OK.value()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponsePageable(RESPONSE_GET_RECORD, ARITHMETIC_OPERATION_ERROR,
                    Page.empty(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    /**
     * ex:http://localhost:8080/api/v1/record
     * Delete a record
     *
     * @param header Authorization header
     * @param  recordId Record id
     * @return <200>esponseEntity<Response><200/>
     * <500>Internal Error<500/>
     */
    @DeleteMapping
    public ResponseEntity<Response> deleteRecord(@RequestHeader(name = "Authorization", required = true) String header,
        @RequestParam(value = "recordId", required = true) UUID recordId) {
        String token = header.replace("Bearer ", "");
        String username = TokenUtil.parseToken(token).get("username").toString();

        try {
            recordService.deleteRecord(username, recordId);
            return ResponseEntity.status(HttpStatus.OK).body(
                new Response(RESPONSE_DELETE_RECORD, RECORD_OPERATION_PERFORMED,
                    RECORD_OPERATION_PERFORMED,
                    HttpStatus.OK.value()));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new Response(RESPONSE_DELETE_RECORD, ARITHMETIC_OPERATION_ERROR,
                    ARITHMETIC_OPERATION_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

    }
}
