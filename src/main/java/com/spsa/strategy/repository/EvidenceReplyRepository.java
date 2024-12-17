package com.spsa.strategy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.Evidence;

@Repository
public interface EvidenceReplyRepository extends JpaRepository<Evidence, String> {

}
