package controller;

import static org.challenge.util.Constants.RECORD_OPERATION_PERFORMED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.challenge.controller.RecordController;
import org.challenge.controller.util.ResponsePageable;
import org.challenge.domain.User;
import org.challenge.service.RecordService;
import org.challenge.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
@SpringJUnitConfig
class RecordControllerTest {

    @Mock
    private RecordService recordService = mock(RecordService.class);
    private RecordController recordController = new RecordController(recordService);

    @Test
    void getRecord_ValidParameters_ReturnsResponseEntityWithOKStatus() {
        // Mocking dependencies
        Page recordPage = mock(Page.class);
        User user = new User();
        user.setId(1L);

        String header = "Bearer <token>";
        Claims claims= new DefaultClaims();
        claims.put("username", "user");
        try (MockedStatic<TokenUtil> mockedStatic = Mockito.mockStatic(TokenUtil.class)) {
            mockedStatic.when(() -> TokenUtil.parseToken(anyString())).thenReturn(claims);
            Integer pageNumber = 1;
            Integer pageSize = 10;

            // Execute the controller method
            ResponseEntity<ResponsePageable> responseEntity = recordController.getRecord(header, pageNumber, pageSize);

            // Assert the response status
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

            // Assert the response body
            ResponsePageable responseBody = responseEntity.getBody();
            assertEquals(RECORD_OPERATION_PERFORMED, responseBody.getType());
            assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        }catch (Exception e ){
            System.out.println(e.getMessage());
        }


    }

}
