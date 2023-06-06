package org.challenge.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ApiWebClient {

    private WebClient client;
    // TODO move to porperties
    private static final Logger logger = LoggerFactory.getLogger(ApiWebClient.class);
    private final String API_KEY;

   private final String BASE_URL="";

    @Autowired
    public ApiWebClient(  @Value("${random.api.key}") String API_KEY,  @Value("${random.api.url}") String BASE_URL
    ) {
        // TODO add to properties
        this.client = WebClient.builder()
         //   .baseUrl(BASE_URL)
            .baseUrl("https://api.random.org/json-rpc/4/invoke")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
       // this.API_KEY = API_KEY;
        this.API_KEY="262c872e-a168-4af0-83db-210d264a4415";
    }

    public String getRandomString(int numberOfStrings, int lengthOfStrings){
        logger.info("Getting random string");
        // Create a body for post request

        JsonRpcRequest jsonRpcRequest = new JsonRpcRequest("generateStrings", 1);
        jsonRpcRequest.addParam("apiKey",API_KEY);
        jsonRpcRequest.addParam("n", numberOfStrings);
        jsonRpcRequest.addParam("length", lengthOfStrings);
        jsonRpcRequest.addParam("characters", "abcdefghijklmnopqrstuvwxyz");

        // Check this
        String result = client.post().
            bodyValue(jsonRpcRequest)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        logger.info("Random string: {}", result);
        return result;
    }

}
