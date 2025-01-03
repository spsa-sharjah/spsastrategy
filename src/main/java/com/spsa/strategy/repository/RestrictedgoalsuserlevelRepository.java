package com.spsa.strategy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.Restrictedgoalsuserlevel;

import jakarta.transaction.Transactional;

@Repository
public interface RestrictedgoalsuserlevelRepository extends JpaRepository<Restrictedgoalsuserlevel, String> {

    @Modifying
    @Transactional
	void deleteByGoalid(String goalid);

	List<Restrictedgoalsuserlevel> findByGoalid(String goalid);

}
