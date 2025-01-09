package com.spsa.strategy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.FileEvidence;

import jakarta.transaction.Transactional;

@Repository
public interface FileEvidenceRepository extends JpaRepository<FileEvidence, Long> {

	List<FileEvidence> findByEvidenceid(Long evidenceid);

    @Modifying
    @Transactional
	void deleteByEvidenceid(Long id);
}
