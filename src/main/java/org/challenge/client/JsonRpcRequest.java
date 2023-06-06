package org.challenge.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

public class JsonRpcRequest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @JsonProperty("jsonrpc")
    private String jsonRpcVersion = "2.0";

    private String method;
    private Map<String, Object> params = new HashMap<>();
    private int id;

    public JsonRpcRequest(String method, int id) {
        this.method = method;
        this.id = id;
    }

    public void addParam(String key, Object value) {
        params.put(key, value);
    }

    public String toJsonString() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }

}
