package com.spsa.strategy.service;

import java.util.Locale;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.spsa.strategy.builder.request.EvidenceCommentSaveRq;
import com.spsa.strategy.builder.request.EvidenceSaveRq;
import com.spsa.strategy.model.Users;

import jakarta.validation.Valid;

public interface EvidenceService {

	ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String goalid, Users user);

	ResponseEntity<?> fileslist(Locale locale, Long evidenceid, Users user);

	ResponseEntity<?> uploadfiles(Locale locale, Users user, MultipartFile[] files, Long evidenceid, String goalid);

	ResponseEntity<?> downloadfile(Users user, String fileName);
	
	ResponseEntity<?> returnbase64file(Users user, String fileName);

	ResponseEntity<?> commentlist(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, Long evidenceid, Users user);

	ResponseEntity<?> commentsave(Locale locale, @Valid EvidenceCommentSaveRq req, Users user);

	ResponseEntity<?> commentremove(Locale locale, Long id, Users user);

	ResponseEntity<?> remove(Locale locale, Long id, Users user);

	ResponseEntity<?> removefile(Locale locale, Long id, Users user);

	ResponseEntity<?> save(Locale locale, @Valid EvidenceSaveRq req, Users user);
}
