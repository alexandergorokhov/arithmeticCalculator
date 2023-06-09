package org.challenge.repository;

import org.challenge.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RecordRepository extends JpaRepository<Record, UUID> {
    @Query("SELECT r FROM Record r WHERE r.userId = :userId ORDER BY r.operationDate DESC")
    Optional<Record> findLatestRecordByUserId(@Param("userId") Long userId);
}
