package com.spsa.strategy.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.Authoritygoals;

import jakarta.transaction.Transactional;

@Repository
public interface AuthoritygoalsRepository extends JpaRepository<Authoritygoals, String> {

	Page<Authoritygoals> findAll(Specification<Authoritygoals> spec, Pageable pageable);

	List<Authoritygoals> findAll(Specification<Authoritygoals> spec);

    @Query(value = "SELECT * FROM authoritygoals a WHERE a.id NOT IN (SELECT goalid FROM restrictedgoalsuserlevel WHERE userlevelid = :userlevelid)", nativeQuery = true)
    Page<Authoritygoals> findnonrestrictedgoals(@Param("userlevelid") String userlevelid, Pageable pageable);
    
    @Query("SELECT SUM(g.yearlyexpectedweight) FROM Authoritygoals g WHERE g.year = :year")
    Integer findSumOfGoalsByYear(@Param("year") String year);

    @Query("SELECT SUM(g.yearlyexpectedweight) FROM Authoritygoals g WHERE g.year = :year AND g.id <> :goalid")
    Integer findSumOfGoalsByYearNotMatchingGoalid(@Param("year") String year, @Param("goalid") String goalid);

    @Query("SELECT g FROM Authoritygoals g WHERE g.year = :year ORDER BY date_time DESC")
    List<Authoritygoals> findByYear(@Param("year") String year);

    @Query("SELECT g FROM Authoritygoals g WHERE g.year = :year AND g.team = :team ORDER BY date_time DESC")
    List<Authoritygoals> findByYearAndTean(@Param("year") String year, @Param("team") String team);

    @Query("SELECT g FROM Authoritygoals g WHERE g.year = :year AND g.status = :status")
	List<Authoritygoals> findByYearAndStatus(@Param("year") String year, @Param("status") String status);

    @Modifying
    @Transactional
    @Query("UPDATE Authoritygoals g SET g.status = :status WHERE g.year = :year ")
	void updateGoalsStatusByYear(@Param("year") String year, @Param("status") String status);

    @Query("SELECT COUNT(g) FROM Authoritygoals g WHERE g.year = :year ")
	int countyearlygoals(@Param("year") String year);
}
