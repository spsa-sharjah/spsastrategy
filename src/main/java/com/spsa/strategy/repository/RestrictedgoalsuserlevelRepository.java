package com.spsa.strategy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.Restrictedgoalsuserlevel;

@Repository
public interface RestrictedgoalsuserlevelRepository extends JpaRepository<Restrictedgoalsuserlevel, String> {

}
