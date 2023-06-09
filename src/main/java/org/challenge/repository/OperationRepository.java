package org.challenge.repository;

import org.challenge.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    @Override
    Optional<Operation> findById(Long aLong);
}
