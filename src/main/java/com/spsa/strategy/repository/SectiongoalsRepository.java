package com.spsa.strategy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.Sectiongoals;

@Repository
public interface SectiongoalsRepository extends JpaRepository<Sectiongoals, String> {

}
