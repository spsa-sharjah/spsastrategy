package com.spsa.strategy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.Goaluserlevel;

@Repository
public interface GoaluserlevelRepository extends JpaRepository<Goaluserlevel, String> {

}
