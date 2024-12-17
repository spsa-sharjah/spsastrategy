package com.spsa.strategy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.Userlevel;

@Repository
public interface UserlevelRepository extends JpaRepository<Userlevel, String> {

}
