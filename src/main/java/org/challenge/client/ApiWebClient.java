package org.challenge.client;

import org.challenge.client.util.ApiWebClientConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApiWebClient {

    private WebClient client;
    // TODO move to porperties
    private static final Logger logger = LoggerFactory.getLogger(ApiWebClient.class);
    private final String API_KEY;

    private final String BASE_URL = "";

    @Autowired
    public ApiWebClient(@Value("${random.api.key}") String API_KEY, @Value("${random.api.url}") String BASE_URL
    ) {
        // TODO add to properties
        this.client = WebClient.builder()
            //   .baseUrl(BASE_URL)
            .baseUrl("https://api.random.org/json-rpc/4/invoke")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
        // this.API_KEY = API_KEY;
        // TODO disable APi key. Use hashed or as VM argument
        this.API_KEY = "262c872e-a168-4af0-83db-210d264a4415";
    }

    public ArrayList<StringBuilder> getRandomString(int numberOfStrings, int lengthOfStrings) {
        logger.info("Getting random string");
        JsonRpcRequest jsonRpcRequest = new JsonRpcRequest( ApiWebClientConstants.JSON_RPC_METHOD, ApiWebClientConstants.JSON_RPC_PARAM_ID);
        jsonRpcRequest.addParam(ApiWebClientConstants.JSON_RPC_PARAM_API_KEY, API_KEY);
        jsonRpcRequest.addParam(ApiWebClientConstants.JSON_RPC_PARAM_N, numberOfStrings);
        jsonRpcRequest.addParam(ApiWebClientConstants.JSON_RPC_PARAM_LENGTH, lengthOfStrings);
        jsonRpcRequest.addParam(ApiWebClientConstants.JSON_RPC_PARAM_CHARACTERS, ApiWebClientConstants.JSON_RPC_PARAM_CHARACTERS_VALUE);

        String result = client.post()
            .body(BodyInserters.fromValue(jsonRpcRequest.toJsonString()))
            .retrieve()
            .bodyToMono(String.class)
            .block();
        logger.info("Random string: {}", result);
        return jsonRpcRequest.retrieveArrayField(result, List.of("result", "random", "data"));
    }

}
