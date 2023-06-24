package org.challenge.repository;

import org.challenge.domain.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface RecordRepository extends JpaRepository<Record, UUID> {
    @Query("SELECT r FROM Record r WHERE r.userId = :userId AND r.deleted=false ORDER BY r.operationDate DESC")
    Page<Record> findLatestRecordsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Record r SET r.deleted = true, r.deletedAt = :deletedAt WHERE r.id = :recordId")
    void deleteRecord(@Param("recordId") UUID recordId, @Param("deletedAt") LocalDateTime deletedAt);
}
