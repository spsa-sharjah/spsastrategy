package com.spsa.strategy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.EvidenceReply;

@Repository
public interface EvidenceRepository extends JpaRepository<EvidenceReply, String> {

}