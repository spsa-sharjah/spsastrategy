package com.spsa.strategy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.YearlyGoalsSettings;

@Repository
public interface YearlyGoalsSettingsRepository extends JpaRepository<YearlyGoalsSettings, Long> {

	Optional<YearlyGoalsSettings> findByYear(String year);
    
}
