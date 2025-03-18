package com.spsa.strategy.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spsa.strategy.builder.request.EvidenceCommentSaveRq;
import com.spsa.strategy.builder.request.EvidenceSaveRq;
import com.spsa.strategy.builder.response.DatatableResponse;
import com.spsa.strategy.builder.response.IdRs;
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.enumeration.CustomAction;
import com.spsa.strategy.enumeration.EvidenceStatus;
import com.spsa.strategy.enumeration.GoalStatus;
import com.spsa.strategy.enumeration.Menuauthid;
import com.spsa.strategy.model.Authoritygoals;
import com.spsa.strategy.model.Departmentgoals;
import com.spsa.strategy.model.Evidence;
import com.spsa.strategy.model.EvidenceReply;
import com.spsa.strategy.model.FileEvidence;
import com.spsa.strategy.model.Sectiongoals;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.repository.AuthoritygoalsRepository;
import com.spsa.strategy.repository.DepartmentgoalsRepository;
import com.spsa.strategy.repository.EvidenceReplyRepository;
import com.spsa.strategy.repository.EvidenceRepository;
import com.spsa.strategy.repository.FileEvidenceRepository;
import com.spsa.strategy.repository.SectiongoalsRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@Transactional
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

	@Autowired
	DepartmentgoalsRepository departmentgoalsRepository;

	@Autowired
	AuthoritygoalsRepository authoritygoalsRepository;
	
	@Autowired
	SectiongoalsRepository sectiongoalsRepository;

	@Value("${spring.file.uploaddir}") 
    private String fileuploaddir;

	@Value("${spring.file.accessurl}") 
    private String fileaccessurl;

	@Value("${spring.file.trash.dir}") 
    private String filetrashdir;

	@Override
	public ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String goalid, Users user, boolean showcurrentuserlist) {
		try {

			String currentuser = showcurrentuserlist ? user.getUsername() : null;
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
	public ResponseEntity<?> uploadfiles(Locale locale, Users user, MultipartFile[] files, Long evidenceid, String goalid) {

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
                evidence.setGoalid(goalid);
                evidence = evidenceRepository.save(evidence);
            }

            List<FileEvidence> savedfiles = new ArrayList<FileEvidence>();
            for (MultipartFile file : files)
            	savedfiles.add(savefile(user, file, evidence));
            	
        	return ResponseEntity.ok(savedfiles);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
    }
	
	private FileEvidence savefile(Users user, MultipartFile file, Evidence evidence) {

		try {
	        
            String originalFilename = file.getOriginalFilename();
            String fileName = Utils.generateUniqueString(Constants.EVIDENCE_KEY) + "_" + originalFilename;
            
            // Save file metadata to the database
            FileEvidence metadata = new FileEvidence();
            metadata.setFilename(fileName);
            metadata.setFilepath(fileaccessurl + fileName);
            metadata.setFileimage(metadata.getFilepath());
            metadata.setUsername(user.getUsername()); // uploaded by username
            metadata.setDate_time(new Date());
            metadata.setEvidenceid(evidence.getId());
            metadata.setFilesize(file.getSize());
            metadata.setWidth(Constants.DEFAULT_WIDTH);
            metadata = fileEvidenceRepository.save(metadata);
            
            checkAndCreateDirectory(fileuploaddir);
            Path filePath = Paths.get(fileuploaddir, fileName);

            
            // Save file to the server
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
	
	        return metadata;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void checkAndCreateDirectory(String filePath){
		try {
	        Path path = Paths.get(filePath);
	
	        if (Files.notExists(path)) {
	            Files.createDirectories(path);
	            System.out.println("Directory created: " + path.toString());
	        }
		}catch (Exception e) {
			e.printStackTrace();
		}
    }
	 
	@Override
	public ResponseEntity<?> downloadfile(Users user, String fileName) {
		try {
	        Path filePath = Paths.get(fileuploaddir).resolve(fileName).normalize();
	        Resource resource = new UrlResource(filePath.toUri());
	
	        if (!resource.exists()) 
				return ResponseEntity.ok(new MessageResponse("resource_not_found", 111));
	
	        String contentType = getFileContentType(resource);
	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	                .body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse("exception_case", 111));
		}
	}
	private String getFileContentType(Resource resource) {
	    try {
	        // Use Java's built-in file type detection
	        return Files.probeContentType(resource.getFile().toPath());
	    } catch (Exception e) {
	        return "application/octet-stream";
	    }
	}
	 
	@Override
	public ResponseEntity<?> returnbase64file(Users user, String fileName) {
		try {
	        Path filePath = Paths.get(fileuploaddir).resolve(fileName).normalize();
	        Resource resource = new UrlResource(filePath.toUri());
	
	        if (!resource.exists()) 
				return ResponseEntity.ok(new MessageResponse("resource_not_found", 111));

	        byte[] fileBytes = Files.readAllBytes(filePath);

	        // Encode the byte array into Base64
	        String base64EncodedFile = Base64.getEncoder().encodeToString(fileBytes);

	        // Get MIME type for the file (e.g., image/jpeg, application/pdf)
	        String mimeType = Files.probeContentType(filePath);

//		        // Return the Base64 encoded string in the response body
//		        return ResponseEntity.ok()
//		                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
//		                .contentType(MediaType.parseMediaType(mimeType))
//		                .body("data:" + mimeType + ";base64," + base64EncodedFile);

//				return ResponseEntity.ok(new MessageResponse("data:" + mimeType + ";base64," + base64EncodedFile));
			return ResponseEntity.ok("data:" + mimeType + ";base64," + base64EncodedFile);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse("exception_case", 111));
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
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> commentremove(Locale locale, Long id, Users user) {
		try {
	        Optional<EvidenceReply> opt = evidenceReplyRepository.findById(id);
			if (opt.isPresent()) {
				evidenceReplyRepository.delete(opt.get());
			}
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("success_operation", locale)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> remove(Locale locale, Long id, Users user) {
		try {
	        Optional<Evidence> opt = evidenceRepository.findById(id);
			if (opt.isPresent()) {
				evidenceReplyRepository.deleteByEvidenceid(id);

				List<FileEvidence> fileevidencelist = fileEvidenceRepository.findByEvidenceid(id);
				if (fileevidencelist != null && fileevidencelist.size() > 0)
					for (FileEvidence fe : fileevidencelist)
						removefile(locale, fe.getId(), user);
				
				evidenceRepository.delete(opt.get());
			}
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("success_operation", locale)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> removefile(Locale locale, Long id, Users user) {
		try {
			
			Optional<FileEvidence> opt = fileEvidenceRepository.findById(id);
			
			if (opt.isPresent()) {
				FileEvidence fe = opt.get();
				
				movefiletotrash(fileuploaddir + fe.getFilename(), filetrashdir + fe.getFilename());
				
				long evidenceid = fe.getEvidenceid();
				fileEvidenceRepository.delete(fe);
				IdRs rs = new IdRs();
				rs.setId(evidenceid);
				return ResponseEntity.ok(rs);
			}

			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 222));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}
	
	private void movefiletotrash(String filepath, String trashpath) {

		try {
			Path sourcePath = Paths.get(filepath);

	        Path destinationPath = Paths.get(trashpath);
	        if (!Files.exists(destinationPath.getParent())) {
	            Files.createDirectories(destinationPath.getParent());
	        }

            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File moved successfully!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ResponseEntity<?> save(Locale locale, @Valid EvidenceSaveRq req, Users user) {
		try {
	        Evidence evidence = null;
            if (req.getId() != null) {

            	Optional<Evidence> opt = evidenceRepository.findById(req.getId());
            	if (!opt.isPresent())
        			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 111));
            		
        		evidence = opt.get();
            }
            if (evidence == null) {
                evidence = new Evidence();
                evidence.setDate_time(new Date());
                evidence.setUsername(user.getUsername());
                evidence.setGoalid(req.getGoalid());
            }
            evidence.setDescription(req.getDescription());

            evidence.setStatus(EvidenceStatus.EvidenceNew.name());
            String menuauthid = req.getGoalid().contains(Constants.SECTION_KEY) ? Menuauthid.managesectiongoals.name() : Menuauthid.managedepartmentgoals.name();
    		if (Utils.isapiauthorized(CustomAction.UpdateEvidenceStatus.name(), menuauthid, user.getAuthorizedapis())) {
                evidence.setComment(req.getComment());
                evidence.setStatus(req.getStatus());
                
            	// update parent status & parent percentages
                updateparentstatusespercentages(req);
    		}
             
            evidence = evidenceRepository.save(evidence);
        	return ResponseEntity.ok(new MessageResponse(messageService.getMessage("success_operation", locale)));
        	
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	private void updateparentstatusespercentages(@Valid EvidenceSaveRq req) {
		try {
            String parentstatus = req.getStatus().equals(EvidenceStatus.EvidenceApproved.name()) ? GoalStatus.EvidenceApproved.name() : GoalStatus.EvidenceInProgress.name();
        	if (req.getGoalid().contains(Constants.SECTION_KEY)) { // operation
        		Optional<Sectiongoals> parentgoalopt = sectiongoalsRepository.findById(req.getGoalid());
        		if (parentgoalopt.isPresent()) {
            		Sectiongoals parentgoal = parentgoalopt.get();
            		parentgoal.setYearlyweight(parentgoal.getYearlyexpectedweight());
                    parentgoal.setStatus(parentstatus);
                    sectiongoalsRepository.save(parentgoal);
                    
                    int sumofpercentage = sectiongoalsRepository.findSumOfPercentageGoalsByDepgoalid(parentgoal.getDepgoalid());
            		Optional<Departmentgoals> depgoalopt = departmentgoalsRepository.findById(parentgoal.getDepgoalid());
            		if (parentgoalopt.isPresent()) {
            			Departmentgoals depgoal = depgoalopt.get();
            			depgoal.setYearlyweight(sumofpercentage);
            			depgoal.setStatus(sumofpercentage == depgoal.getYearlyexpectedweight() ?  GoalStatus.EvidenceApproved.name() : GoalStatus.EvidenceInProgress.name());
                        departmentgoalsRepository.save(depgoal);

                        int sumofauthpercentage = departmentgoalsRepository.findSumOfPercentageGoalsByAuthgoalid(depgoal.getAuthgoalid());
                		Optional<Authoritygoals> authgoalopt = authoritygoalsRepository.findById(depgoal.getAuthgoalid());
                		if (parentgoalopt.isPresent()) {
                			Authoritygoals authgoal = authgoalopt.get();
                			authgoal.setYearlyweight(sumofauthpercentage);
                			authgoal.setStatus(sumofpercentage == authgoal.getYearlyexpectedweight() ?  GoalStatus.EvidenceApproved.name() : GoalStatus.EvidenceInProgress.name());
                			authoritygoalsRepository.save(authgoal);
                		}
            		}
        		}
        	} else {
        		Optional<Departmentgoals> parentgoalopt = departmentgoalsRepository.findById(req.getGoalid());
        		if (parentgoalopt.isPresent()) {
        			Departmentgoals parentgoal = parentgoalopt.get();
            		parentgoal.setYearlyweight(parentgoal.getYearlyexpectedweight());
                    parentgoal.setStatus(parentstatus);
                    departmentgoalsRepository.save(parentgoal);
                    
                    int sumofpercentage = departmentgoalsRepository.findSumOfPercentageGoalsByAuthgoalid(parentgoal.getAuthgoalid());
            		Optional<Authoritygoals> authgoalopt = authoritygoalsRepository.findById(parentgoal.getAuthgoalid());
            		if (parentgoalopt.isPresent()) {
            			Authoritygoals authgoal = authgoalopt.get();
            			authgoal.setYearlyweight(sumofpercentage);
            			authgoal.setStatus(sumofpercentage == authgoal.getYearlyexpectedweight() ?  GoalStatus.EvidenceApproved.name() : GoalStatus.EvidenceInProgress.name());
            			authoritygoalsRepository.save(authgoal);
            		}
        		}
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	

	@Override
	public String removebygoalid(Locale locale, Users user, String goalid) {
		List<Evidence> evidencelist = evidenceRepository.findByGoalid(goalid);
		if (evidencelist != null)
			for (Evidence evidence : evidencelist)
				remove(locale, evidence.getId(), user);
		
		return "success";
	}

}
