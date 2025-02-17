package com.spsa.strategy.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.Departmentgoals;

@Repository
public interface DepartmentgoalsRepository extends JpaRepository<Departmentgoals, String> {

	Page<Departmentgoals> findAll(Specification<Departmentgoals> spec, Pageable pageable);

	List<Departmentgoals> findAll(Specification<Departmentgoals> spec);

    @Query("SELECT SUM(g.yearlyexpectedweight) FROM Departmentgoals g WHERE g.authgoalid = :authgoalid")
    Integer findSumOfGoalsByAuthgoalid(@Param("authgoalid") String authgoalid);

    @Query("SELECT SUM(g.yearlyexpectedweight) FROM Departmentgoals g WHERE g.authgoalid = :authgoalid AND g.id <> :goalid")
    Integer findSumOfGoalsByAuthgoalidYearNotMatchingGoalid(@Param("authgoalid") String authgoalid, @Param("goalid") String goalid);

	List<Departmentgoals> findByAuthgoalid(String authgoalid);
}
