package com.spsa.strategy.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spsa.strategy.builder.request.EvidenceCommentSaveRq;
import com.spsa.strategy.builder.response.DatatableResponse;
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.enumeration.PositionEnum;
import com.spsa.strategy.model.Evidence;
import com.spsa.strategy.model.EvidenceReply;
import com.spsa.strategy.model.FileEvidence;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.repository.EvidenceReplyRepository;
import com.spsa.strategy.repository.EvidenceRepository;
import com.spsa.strategy.repository.FileEvidenceRepository;

import jakarta.validation.Valid;



@Service
public class EvidenceServiceImpl implements EvidenceService {

	@Autowired
	MessageService messageService;
	
	@Autowired
	EvidenceRepository evidenceRepository;
	
	@Autowired
	FileEvidenceRepository fileEvidenceRepository;
	
	@Autowired
	EvidenceReplyRepository evidenceReplyRepository;

	@Value("${spring.file.uploaddir}") 
    private String uploaddir;

	@Override
	public ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String goalid, Users user) {
		try {

			String currentuser = null;
			if (user.getPosition() != null) {
				if (user.getPosition().equalsIgnoreCase(PositionEnum.EMPLOYEE.name().toLowerCase()))
					currentuser = user.getUsername();
			}
			
			Page<Evidence> pages = null;
			if (sortcolumn == null) sortcolumn = "date_time";
			Specification<Evidence> spec = JPASpecification.returnGoalevidenceSpecification(search, sortcolumn, descending, goalid, user.getUser_role(), currentuser);
			
			Pageable pageable = PageRequest.of(page, size);
		    pages = evidenceRepository.findAll(spec, pageable);

			List<Evidence> allusersbysearch = evidenceRepository.findAll(spec);
			long totalrows = allusersbysearch.size();
			long recordsFiltered = totalrows;
			
			if (pages != null && pages.getSize() > 0)
				for (Evidence evidence : pages) {
		            evidence.setFilesnbr(0);
					List<FileEvidence> fileevidencelist = fileEvidenceRepository.findByEvidenceid(evidence.getId());
					if (fileevidencelist != null && fileevidencelist.size() > 0)
						evidence.setFilesnbr(fileevidencelist.size());
		        }
	
	        DatatableResponse<Evidence> datatableresponse = new DatatableResponse<Evidence>(draw, totalrows, recordsFiltered, pages.getContent());
		       
			return ResponseEntity.ok(datatableresponse);
	        
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> fileslist(Locale locale, Long evidenceid, Users user) {
		try {
			List<FileEvidence> fileevidencelist = fileEvidenceRepository.findByEvidenceid(evidenceid);
			if (fileevidencelist == null)
				return ResponseEntity.ok(new ArrayList<FileEvidence>());
				
			return ResponseEntity.ok(fileevidencelist);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> uploadfile(Locale locale, Users user, MultipartFile file, Long evidenceid, String comment) {

		try {
	        Evidence evidence = null;
            if (evidenceid != null) {

            	Optional<Evidence> opt = evidenceRepository.findById(evidenceid);
            	if (!opt.isPresent())
        			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 111));
            		
        		evidence = opt.get();
            }
            if (evidence == null) {
                evidence = new Evidence();
                evidence.setDate_time(new Date());
                evidence.setUsername(user.getUsername());
            }
            evidence.setComment(comment);
            evidence = evidenceRepository.save(evidence);
	        
            String originalFilename = file.getOriginalFilename();
            String fileName = Utils.generateUniqueString(Constants.EVIDENCE_KEY) + originalFilename;
            
            // Save file metadata to the database
            FileEvidence metadata = new FileEvidence();
            metadata.setFilename(originalFilename);
            metadata.setFilepath(uploaddir + fileName);
            metadata.setUsername(user.getUsername()); // uploaded by username
            metadata.setDate_time(new Date());
            metadata.setEvidenceid(evidence.getId());
            metadata = fileEvidenceRepository.save(metadata);
            
            Path filePath = Paths.get(uploaddir, fileName);

            // Save file to the server
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
	
	        return ResponseEntity.ok(metadata);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
    }
 
	@Override
	public ResponseEntity<?> downloadfile(Locale locale, Users user, String fileName) {
		try {
	        Path filePath = Paths.get("/opt/uploads").resolve(fileName).normalize();
	        Resource resource = new UrlResource(filePath.toUri());
	
	        if (!resource.exists()) 
				return ResponseEntity.ok(new MessageResponse(messageService.getMessage("resource_not_found", locale), 111));
	
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	                .body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> commentlist(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, Long evidenceid, Users user) {
		try {
			
			Page<EvidenceReply> pages = null;
			if (sortcolumn == null) sortcolumn = "date_time";
			Specification<EvidenceReply> spec = JPASpecification.returnEvidenceReplySpecification(search, sortcolumn, descending, evidenceid);
			
			Pageable pageable = PageRequest.of(page, size);
		    pages = evidenceReplyRepository.findAll(spec, pageable);

			List<EvidenceReply> list = evidenceReplyRepository.findByEvidenceid(evidenceid);
			long totalrows = list.size();
			long recordsFiltered = totalrows;
	
	        DatatableResponse<EvidenceReply> datatableresponse = new DatatableResponse<EvidenceReply>(draw, totalrows, recordsFiltered, pages.getContent());
		       
			return ResponseEntity.ok(datatableresponse);
	        
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> commentsave(Locale locale, @Valid EvidenceCommentSaveRq req, Users user) {

		EvidenceReply obj = null;
		if (req.getId() != null) {
			Optional<EvidenceReply> opt = evidenceReplyRepository.findById(req.getId());
			if (opt.isPresent())
				obj = opt.get();
		}
		if (obj == null) {
			obj = new EvidenceReply();
			obj.setDate_time(new Date());
			obj.setEvidenceid(req.getEvidenceid());
			obj.setUsername(user.getUsername());
		}
		obj.setComment(req.getComment());
		
		obj = evidenceReplyRepository.save(obj);
		
		return ResponseEntity.ok(obj);
	}

	@Override
	public ResponseEntity<?> commentremove(Locale locale, Long id, Users user) {

        Optional<EvidenceReply> opt = evidenceReplyRepository.findById(id);
		if (opt.isPresent()) {
			evidenceReplyRepository.delete(opt.get());
		}
		return ResponseEntity.ok(new MessageResponse(messageService.getMessage("success_operation", locale)));
	}

	@Override
	public ResponseEntity<?> remove(Locale locale, Long id, Users user) {

        Optional<Evidence> opt = evidenceRepository.findById(id);
		if (opt.isPresent()) {
			evidenceReplyRepository.deleteByEvidenceid(id);
			fileEvidenceRepository.deleteByEvidenceid(id);
			evidenceRepository.delete(opt.get());
		}
		return ResponseEntity.ok(new MessageResponse(messageService.getMessage("success_operation", locale)));
	}

}
