package com.spsa.strategy.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spsa.strategy.model.NotificationToken;

public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long> {
}