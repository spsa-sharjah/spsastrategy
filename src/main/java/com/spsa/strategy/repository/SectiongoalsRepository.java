package com.spsa.strategy.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.Sectiongoals;

@Repository
public interface SectiongoalsRepository extends JpaRepository<Sectiongoals, String> {

	Page<Sectiongoals> findAll(Specification<Sectiongoals> spec, Pageable pageable);

	List<Sectiongoals> findAll(Specification<Sectiongoals> spec);

}
