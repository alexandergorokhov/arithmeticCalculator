package service;

import org.challenge.domain.Record;
import org.challenge.domain.User;
import org.challenge.repository.RecordRepository;
import org.challenge.service.RecordService;
import org.challenge.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RecordServiceTest {

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RecordService recordService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        // Arrange
        Record record = new Record();

        // Act
        recordService.save(record);

        // Assert
        verify(recordRepository, times(1)).save(record);
    }

    @Test
    public void testGetUserRecord_UserExists_ReturnsPage() {
        // Arrange
        User user = new User();
        user.setId(1L);
        when(userService.findUserByUserName(anyString())).thenReturn(Optional.of(user));
        when(recordRepository.findLatestRecordsByUserId(anyLong(), any(Pageable.class))).thenReturn(mock(Page.class));

        // Act
        Page result = recordService.getUserRecord("username", 0, 10);

        // Assert
        Assertions.assertNotNull(result);
        verify(userService, times(1)).findUserByUserName(anyString());
        verify(recordRepository, times(1)).findLatestRecordsByUserId(anyLong(), any(Pageable.class));
    }

    @Test
    public void testGetUserRecord_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Arrange
        when(userService.findUserByUserName(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(UsernameNotFoundException.class, () ->
            recordService.getUserRecord("username", 0, 10)
        );
        verify(userService, times(1)).findUserByUserName(anyString());
        verifyNoInteractions(recordRepository);
    }


    @Test
    public void testDeleteRecord_RecordDoesNotExist_ThrowsRuntimeException() {
        // Arrange
        when(recordRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () ->
            recordService.deleteRecord("username", UUID.randomUUID())
        );
        verify(recordRepository, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(recordRepository);
        verifyNoInteractions(userService);
    }

    @Test
    public void testDeleteRecord_RecordAlreadyDeleted_ThrowsRuntimeException() {
        // Arrange
        Record record = new Record(UUID.randomUUID(),
           1L,
          1L,
            BigDecimal.TEN,
            BigDecimal.ONE,
            "9",
            LocalDateTime.now(),
            LocalDateTime.now(),
            false,
            null);
        record.deleteRecord();
        when(recordRepository.findById(any(UUID.class))).thenReturn(Optional.of(record));

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () ->
            recordService.deleteRecord("username", UUID.randomUUID())
        );
        verify(recordRepository, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(recordRepository);
        verifyNoInteractions(userService);
    }

    @Test
    public void testDeleteRecord_UserDoesNotExist_ThrowsUsernameNotFoundException() {

        Record record = new Record(UUID.randomUUID(),
            1L,
            1L,
            BigDecimal.TEN,
            BigDecimal.ONE,
            "9",
            LocalDateTime.now(),
            LocalDateTime.now(),
            false,
            null);
        // Arrange
        when(userService.findUserByUserName(anyString())).thenReturn(Optional.empty());
        when(recordRepository.findById(any(UUID.class))).thenReturn(Optional.of(record));



        // Act & Assert
        Assertions.assertThrows(UsernameNotFoundException.class, () ->
            recordService.deleteRecord("username", UUID.randomUUID())
        );
        verify(userService, times(1)).findUserByUserName(anyString());
    }

}
