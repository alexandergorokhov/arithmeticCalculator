package service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.challenge.client.ApiWebClient;
import org.challenge.domain.Operation;
import org.challenge.domain.Record;
import org.challenge.domain.User;
import org.challenge.dto.OperationDTO;
import org.challenge.exception.OperationNotSupportedException;
import org.challenge.repository.OperationRepository;
import org.challenge.service.ArithmeticCalculatorService;
import org.challenge.service.RecordService;
import org.challenge.service.UserService;
import org.challenge.util.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

public class ArithmeticCalculatorServiceTest {

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private RecordService recordService;

    @Mock
    private UserService userService;

    @Mock
    private ApiWebClient apiWebClient;

    @Mock
    private Logger logger;

    @InjectMocks
    private ArithmeticCalculatorService calculatorService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateAndPerformOperation_UserExists_OperationSupported_ParamsProvided_ReturnsOperationDTO() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setBalance(BigDecimal.TEN);
        Operation operation = new Operation();
        operation.setId(1L);
        operation.setType(Constants.ADDITION);
        operation.setCost(BigDecimal.ONE);
        ArrayList<StringBuilder> random = new ArrayList<>();
        random.add(new StringBuilder("randomString"));
        when(userService.findUserByUserName(anyString())).thenReturn(Optional.of(user));
        when(operationRepository.findById(anyLong())).thenReturn(Optional.of(operation));
      //  when(calculatorService.isOperationPossible(any(Operation.class), any(BigDecimal.class))).thenReturn(true);
        //when(apiWebClient.getRandomString(anyInt(), anyInt())).thenReturn(random);

        // Act
        OperationDTO result = calculatorService.validateAndPerformOperation("username", 1L, 10.0, 10.0);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(operation.getId(), result.getId());
        Assertions.assertEquals(operation.getType(), result.getType());
        Assertions.assertEquals("20.0", result.getResponse());
        verify(userService, times(1)).findUserByUserName(anyString());
        verify(operationRepository, times(1)).findById(anyLong());
     //   verify(calculatorService, times(1)).isOperationPossible(any(Operation.class), any(BigDecimal.class));
        verify(recordService, times(1)).save(any(Record.class));
        verify(userService, times(1)).updateUserBalance(any(User.class), any(BigDecimal.class));
        verifyNoMoreInteractions(userService, operationRepository, calculatorService, recordService);
    }

    @Test
    public void testValidateAndPerformOperation_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Arrange
        when(userService.findUserByUserName(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(UsernameNotFoundException.class, () ->
            calculatorService.validateAndPerformOperation("username", 1L, 10.0, 10.0)
        );
        verify(userService, times(1)).findUserByUserName(anyString());
        verifyNoInteractions(operationRepository, calculatorService, recordService);
    }

    @Test
    public void testValidateAndPerformOperation_OperationNotFound_ThrowsIllegalArgumentException() {
        // Arrange
        User user = new User();
        user.setId(1L);
        when(userService.findUserByUserName(anyString())).thenReturn(Optional.of(user));
        when(operationRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            calculatorService.validateAndPerformOperation("username", 1L, 10.0, 10.0)
        );
        verify(userService, times(1)).findUserByUserName(anyString());
        verify(operationRepository, times(1)).findById(anyLong());
        verifyNoInteractions(calculatorService, recordService);
    }

    @Test
    public void testValidateAndPerformOperation_OperationNotSupported_ParamsNotProvided_ThrowsOperationNotSupportedException() {
        // Arrange
        User user = new User();
        user.setId(1L);
        Operation operation = new Operation();
        operation.setId(1L);
        operation.setType(Constants.ADDITION);
        operation.setCost(BigDecimal.TEN);
        when(userService.findUserByUserName(anyString())).thenReturn(Optional.of(user));
        when(operationRepository.findById(anyLong())).thenReturn(Optional.of(operation));
        when(calculatorService.isOperationPossible(any(Operation.class), any(BigDecimal.class))).thenReturn(true);

        // Act & Assert
        Assertions.assertThrows(OperationNotSupportedException.class, () ->
            calculatorService.validateAndPerformOperation("username", 1L)
        );
        verify(userService, times(1)).findUserByUserName(anyString());
        verify(operationRepository, times(1)).findById(anyLong());
        verify(calculatorService, times(1)).isOperationPossible(any(Operation.class), any(BigDecimal.class));
        verifyNoInteractions(recordService);
    }

    @Test
    public void testValidateAndPerformOperation_InsufficientFunds_ReturnsOperationDTOWithInsufficientFundsResponse() {
        // Arrange
        User user = new User();
        user.setId(1L);
        Operation operation = new Operation();
        operation.setId(1L);
        operation.setType(Constants.ADDITION);
        operation.setCost(BigDecimal.TEN);
        when(userService.findUserByUserName(anyString())).thenReturn(Optional.of(user));
        when(operationRepository.findById(anyLong())).thenReturn(Optional.of(operation));
        when(calculatorService.isOperationPossible(any(Operation.class), any(BigDecimal.class))).thenReturn(false);

        // Act
        OperationDTO result = calculatorService.validateAndPerformOperation("username", 1L, 10.0, 10.0);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(operation.getId(), result.getId());
        Assertions.assertEquals(operation.getType(), result.getType());
        Assertions.assertEquals(Constants.INSUFFICIENT_FUNDS, result.getResponse());
        verify(userService, times(1)).findUserByUserName(anyString());
        verify(operationRepository, times(1)).findById(anyLong());
        verify(calculatorService, times(1)).isOperationPossible(any(Operation.class), any(BigDecimal.class));
        verifyNoInteractions(recordService);
    }
}
