package com.spsa.strategy.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spsa.strategy.model.SystemNotification;

public interface SystemNotificationRepository extends JpaRepository<SystemNotification, Long> {

	List<SystemNotification> findBySenderusername(String senderusername);


	Page<SystemNotification> findAll(Specification<SystemNotification> spec, Pageable pageable);

    @Query("SELECT g FROM SystemNotification g WHERE g.receiverusername = :receiverusername AND seen = false ORDER BY g.datetime DESC ")
	List<SystemNotification> findnewusernotifications(@Param("receiverusername") String username);

    @Query("SELECT g FROM SystemNotification g WHERE g.id = :id AND (g.senderusername = :username OR g.receiverusername = :username) ")
	Optional<SystemNotification> findByIdAndUsername(@Param("id") Long id, @Param("username") String username);

    @Query("SELECT g FROM SystemNotification g WHERE g.id = :id AND g.receiverusername = :receiverusername ")
	Optional<SystemNotification> findByIdAndReceiverusername(@Param("id") Long id, @Param("receiverusername") String receiverusername);

}