package com.spsa.strategy.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spsa.strategy.model.EvidenceReply;

@Repository
public interface EvidenceReplyRepository extends JpaRepository<EvidenceReply, Long> {

	List<EvidenceReply> findByEvidenceid(Long evidenceid);

	Page<EvidenceReply> findAll(Specification<EvidenceReply> spec, Pageable pageable);

}
