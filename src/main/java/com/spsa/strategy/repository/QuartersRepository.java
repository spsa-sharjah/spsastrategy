package com.spsa.strategy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.Quarters;

@Repository
public interface QuartersRepository extends JpaRepository<Quarters, String> {

}
