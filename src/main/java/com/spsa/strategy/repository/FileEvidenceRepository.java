package com.spsa.strategy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.FileEvidence;

@Repository
public interface FileEvidenceRepository extends JpaRepository<FileEvidence, Long> {

	List<FileEvidence> findByEvidenceid(Long evidenceid);
}
