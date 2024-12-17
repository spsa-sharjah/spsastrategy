package com.spsa.strategy.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.Authoritygoals;

@Repository
public interface AuthoritygoalsRepository extends JpaRepository<Authoritygoals, String> {

	Page<Authoritygoals> findAll(Specification<Authoritygoals> spec, Pageable pageable);

	List<Authoritygoals> findAll(Specification<Authoritygoals> spec);

}
