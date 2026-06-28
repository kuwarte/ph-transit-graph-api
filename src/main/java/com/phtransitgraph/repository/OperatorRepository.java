package com.phtransitgraph.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.phtransitgraph.entity.Operator;

public interface OperatorRepository extends JpaRepository<Operator, String> {
    Page<Operator> findByVerifiedTrue(Pageable pageable);

    List<Operator> findByVerifiedFalse();

    Optional<Operator> findByUserId(String userId);

    boolean existsByFranchiseNo(String franchiseNo);
}
