package com.spsa.strategy.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.Departmentgoals;

@Repository
public interface DepartmentgoalsRepository extends JpaRepository<Departmentgoals, String> {

	Page<Departmentgoals> findAll(Specification<Departmentgoals> spec, Pageable pageable);

	List<Departmentgoals> findAll(Specification<Departmentgoals> spec);

}
