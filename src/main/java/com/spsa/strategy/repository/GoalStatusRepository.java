package com.spsa.strategy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.GoalStatus;

@Repository
public interface GoalStatusRepository extends JpaRepository<GoalStatus, String> {

    @Query("SELECT g FROM GoalStatus g WHERE g.menuauthid = :menuauthid ORDER BY g.order ASC ")
	List<GoalStatus> findByMenuauthid(@Param("menuauthid") String menuauthid);

    @Query("SELECT g FROM GoalStatus g WHERE g.menuauthid IS NULL ORDER BY g.order ASC ")
    List<GoalStatus> findEmptyMenuauthid();
    
}
