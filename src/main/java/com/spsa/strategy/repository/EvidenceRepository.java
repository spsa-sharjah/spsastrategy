package com.spsa.strategy.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.Evidence;

@Repository
public interface EvidenceRepository extends JpaRepository<Evidence, Long> {

	Page<Evidence> findAll(Specification<Evidence> spec, Pageable pageable);

	List<Evidence> findAll(Specification<Evidence> spec);

}
