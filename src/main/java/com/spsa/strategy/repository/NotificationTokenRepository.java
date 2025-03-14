package com.spsa.strategy.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spsa.strategy.model.NotificationToken;

import jakarta.transaction.Transactional;

public interface NotificationTokenRepository extends JpaRepository<NotificationToken, String> {

    @Query("SELECT g FROM NotificationToken g WHERE g.username = :username ORDER BY g.datetime DESC ")
	List<NotificationToken> findByUsername(@Param("username") String username);

    @Modifying
    @Transactional
	void deleteByToken(String token);
}