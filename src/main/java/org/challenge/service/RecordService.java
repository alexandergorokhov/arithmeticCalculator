package org.challenge.service;

import org.challenge.domain.Record;
import org.challenge.domain.User;
import org.challenge.repository.RecordRepository;
import org.challenge.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecordService {
    private RecordRepository recordRepository;
    private UserService userService;

    @Autowired
    public RecordService(RecordRepository recordRepository, UserService userService) {
        this.recordRepository = recordRepository;
        this.userService = userService;
    }

    public void save(Record record) {
        recordRepository.save(record);
    }


    public List<Record> getUserRecord(String username, Integer pageNumber, Integer pageSize) {
        Optional<User> user = userService.findUserByUserName(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(Constants.USER_NOT_FOUND);
        }
        Long userId = user.get().getId();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return recordRepository.findLatestRecordsByUserId(userId, pageable);
    }

    @Transactional
    public void deleteRecord(String username, UUID recordId) {
        Optional<Record> recordOptional = recordRepository.findById(recordId);
        if (!recordOptional.isPresent()) {
            throw new RuntimeException("Record not found");
        }
        Record record = recordOptional.get();
        if (record.getDeleted()) {
            throw new RuntimeException("Record already deleted");
        }
        Optional<User> user = userService.findUserByUserName(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(Constants.USER_NOT_FOUND);
        }
        record.deleteRecord();
        recordRepository.save(record);
        userService.updateUserBalance(user.get(), record.getAmount());
    }
}
