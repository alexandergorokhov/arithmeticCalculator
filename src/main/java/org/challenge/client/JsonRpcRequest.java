package org.challenge.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.challenge.client.util.ApiWebClientConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonRpcRequest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(JsonRpcRequest.class);

    @JsonProperty("jsonrpc")
    private String jsonRpcVersion = ApiWebClientConstants.JSON_RPC_VERSION;
    @JsonProperty("method")
    private String method;
    @JsonProperty("params")
    private Map<String, Object> params = new HashMap<>();
    @JsonProperty("id")
    private int id;

    public JsonRpcRequest(String method, int id) {
        this.method = method;
        this.id = id;
    }

    public void addParam(String key, Object value) {
        params.put(key, value);
    }

    public String toJsonString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            logger.error("Error while converting to json string", e);
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> retrieveArrayField(String jsonString, List<String> path) {
        ArrayList<String> result = new ArrayList<>();
        JsonNode fieldNode = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            for (String p : path) {
                fieldNode = jsonNode.get(p);
                if (fieldNode != null) {
                    jsonNode = fieldNode;
                }
            }
            if (jsonNode != null && jsonNode.isArray()) {
                for(JsonNode node : jsonNode) {
                    result.add(node.toString());
                }
            }
        } catch (JsonProcessingException e) {
            logger.error("Error while deserializing json string", e);
            throw new RuntimeException(e);
        }
        return result;
    }

}
