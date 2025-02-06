package com.spsa.strategy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.GoalStatus;

@Repository
public interface GoalStatusRepository extends JpaRepository<GoalStatus, String> {
    
}
