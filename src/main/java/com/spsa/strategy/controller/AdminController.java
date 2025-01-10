package com.spsa.strategy.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spsa.strategy.builder.request.AuthoritygoalSaveRq;
import com.spsa.strategy.builder.request.DepartmentgoalSaveRq;
import com.spsa.strategy.builder.request.EvidenceCommentSaveRq;
import com.spsa.strategy.builder.request.EvidenceSaveRq;
import com.spsa.strategy.builder.request.SectiongoalSaveRq;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.service.AuthService;
import com.spsa.strategy.service.AuthoritygoalService;
import com.spsa.strategy.service.DepartmentgoalService;
import com.spsa.strategy.service.EvidenceService;
import com.spsa.strategy.service.SectiongoalService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AuthoritygoalService authoritygoalService;

	@Autowired
	private DepartmentgoalService departmentgoalService;

	@Autowired
	private SectiongoalService sectiongoalService;

	@Autowired
	private AuthService authService;

	@Autowired
	private EvidenceService evidenceService;

	@RequestMapping(value = "/authority/goal/list", method = RequestMethod.POST)
	public ResponseEntity<?> authoritygoallist(HttpServletRequest request,
								  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
								  @RequestHeader(name = "page", required = false, defaultValue = "0") Integer page,
								  @RequestHeader(name = "size", required = false, defaultValue = "0") Integer size,
								  @RequestHeader(name = "search", required = false) String search,
								  @RequestHeader(name = "sortcolumn", required = false) String sortcolumn,
								  @RequestHeader(name = "descending", required = false, defaultValue = "false") Boolean descending,
						          @RequestHeader(name = "draw", required = false, defaultValue = "1") Integer draw,
								  @RequestHeader(name = "all", required = false, defaultValue = "false") Boolean all) {

        Users user = (Users) request.getAttribute("user");
		return authoritygoalService.list(locale, page, size, search, sortcolumn, descending, draw, null, user, all);
	}
	
	@RequestMapping(value = "/authority/goal/save", method = RequestMethod.POST)
	public ResponseEntity<?> authoritygoalsave(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestHeader(name = "username", required = true) String username,
									  		  @Valid @RequestBody AuthoritygoalSaveRq req) {

        Users user = (Users) request.getAttribute("user");
		return authoritygoalService.goalsave(locale, req, username, user);
	}
	
	@RequestMapping(value = "/authority/goal/remove", method = RequestMethod.POST)
	public ResponseEntity<?> authoritygoalremove(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestHeader(name = "username", required = true) String username,
											  @RequestHeader(name = "goalid", required = true) String goalid) {

        Users user = (Users) request.getAttribute("user");
		return authoritygoalService.goalremove(locale, goalid, username, user);
	}
	
	@RequestMapping(value = "/authority/goal/details", method = RequestMethod.POST)
	public ResponseEntity<?> authoritygoaldetails(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestHeader(name = "username", required = true) String username,
											  @RequestHeader(name = "goalid", required = true) String goalid,
											  @RequestHeader(name = "weightcalculation", required = false) Boolean weightcalculation) {

        Users user = (Users) request.getAttribute("user");
		return authoritygoalService.details(locale, goalid, username, user, weightcalculation);
	}


	@RequestMapping(value = "/department/goal/list", method = RequestMethod.POST)
	public ResponseEntity<?> departmentgoallist(HttpServletRequest request,
								  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
								  @RequestHeader(name = "page", required = false, defaultValue = "0") Integer page,
								  @RequestHeader(name = "size", required = false, defaultValue = "0") Integer size,
								  @RequestHeader(name = "search", required = false) String search,
								  @RequestHeader(name = "sortcolumn", required = false) String sortcolumn,
								  @RequestHeader(name = "descending", required = false, defaultValue = "false") Boolean descending,
						          @RequestHeader(name = "draw", required = false, defaultValue = "1") Integer draw,
						          @RequestHeader(name = "goalid", required = false) String goalid,
								  @RequestHeader(name = "all", required = false, defaultValue = "false") Boolean all) {

        Users user = (Users) request.getAttribute("user");
		return departmentgoalService.list(locale, page, size, search, sortcolumn, descending, draw, goalid, user, all);
	}
	
	@RequestMapping(value = "/department/goal/save", method = RequestMethod.POST)
	public ResponseEntity<?> departmentgoalsave(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestHeader(name = "username", required = true) String username,
									  		  @Valid @RequestBody DepartmentgoalSaveRq req) {

        Users user = (Users) request.getAttribute("user");
		return departmentgoalService.goalsave(locale, req, username, user);
	}
	
	@RequestMapping(value = "/department/goal/remove", method = RequestMethod.POST)
	public ResponseEntity<?> departmentgoalremove(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestHeader(name = "username", required = true) String username,
											  @RequestHeader(name = "goalid", required = true) String goalid) {

        Users user = (Users) request.getAttribute("user");
		return departmentgoalService.goalremove(locale, goalid, username, user);
	}
	
	@RequestMapping(value = "/department/goal/details", method = RequestMethod.POST)
	public ResponseEntity<?> departmentgoaldetails(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestHeader(name = "username", required = true) String username,
											  @RequestHeader(name = "goalid", required = true) String goalid,
											  @RequestHeader(name = "weightcalculation", required = false) Boolean weightcalculation) {

        Users user = (Users) request.getAttribute("user");
		return departmentgoalService.details(locale, goalid, username, user, weightcalculation);
	}
	
	@RequestMapping(value = "/section/goal/list", method = RequestMethod.POST)
	public ResponseEntity<?> sectionparentgoallist(HttpServletRequest request,
								  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
								  @RequestHeader(name = "page", required = false, defaultValue = "0") Integer page,
								  @RequestHeader(name = "size", required = false, defaultValue = "0") Integer size,
								  @RequestHeader(name = "search", required = false) String search,
								  @RequestHeader(name = "sortcolumn", required = false) String sortcolumn,
								  @RequestHeader(name = "descending", required = false, defaultValue = "false") Boolean descending,
						          @RequestHeader(name = "draw", required = false, defaultValue = "1") Integer draw,
						          @RequestHeader(name = "goalid", required = false) String goalid) {

        Users user = (Users) request.getAttribute("user");
		return sectiongoalService.list(locale, page, size, search, sortcolumn, descending, draw, goalid, user);
	}
	
	@RequestMapping(value = "/section/goal/save", method = RequestMethod.POST)
	public ResponseEntity<?> sectiongoalsave(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestHeader(name = "username", required = true) String username,
									  		  @Valid @RequestBody SectiongoalSaveRq req) {

        Users user = (Users) request.getAttribute("user");
		return sectiongoalService.goalsave(locale, req, username, user);
	}
	
	@RequestMapping(value = "/section/goal/remove", method = RequestMethod.POST)
	public ResponseEntity<?> sectiongoalremove(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestHeader(name = "username", required = true) String username,
											  @RequestHeader(name = "goalid", required = true) String goalid) {

        Users user = (Users) request.getAttribute("user");
		return sectiongoalService.goalremove(locale, goalid, username, user);
	}
	
	@RequestMapping(value = "/section/goal/details", method = RequestMethod.POST)
	public ResponseEntity<?> sectiongoaldetails(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestHeader(name = "username", required = true) String username,
											  @RequestHeader(name = "goalid", required = true) String goalid,
											  @RequestHeader(name = "weightcalculation", required = false) Boolean weightcalculation) {

        Users user = (Users) request.getAttribute("user");
		return sectiongoalService.details(locale, goalid, username, user, weightcalculation);
	}

	
	@RequestMapping(value = "/role/goal/access/list", method = RequestMethod.POST)
	public ResponseEntity<?> rolegoalaccesslist(HttpServletRequest request,
												 @RequestHeader(name = "Accept-Language", required = false) Locale locale,
												 @RequestHeader(name = "username", required = true) String username,
												 @RequestHeader(name = "goalid", required = true) String goalid) {

        Users user = (Users) request.getAttribute("user");
		return authService.rolegoalaccesslist(locale, user, goalid);
	}

	
	@RequestMapping(value = "/goal/evidence/list", method = RequestMethod.POST)
	public ResponseEntity<?> goalevidencelist(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestHeader(name = "page", required = false, defaultValue = "0") Integer page,
											  @RequestHeader(name = "size", required = false, defaultValue = "0") Integer size,
											  @RequestHeader(name = "search", required = false) String search,
											  @RequestHeader(name = "sortcolumn", required = false) String sortcolumn,
											  @RequestHeader(name = "descending", required = false, defaultValue = "false") Boolean descending,
									          @RequestHeader(name = "draw", required = false, defaultValue = "1") Integer draw,
									          @RequestHeader(name = "goalid", required = false) String goalid) {

        Users user = (Users) request.getAttribute("user");
		return evidenceService.list(locale, page, size, search, sortcolumn, descending, draw, goalid, user);
	}

	@RequestMapping(value = "/goal/evidence/save", method = RequestMethod.POST)
	public ResponseEntity<?> goalevidencesave(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @Valid @RequestBody EvidenceSaveRq req) {

        Users user = (Users) request.getAttribute("user");
		return evidenceService.save(locale, req, user);
	}
	
	@RequestMapping(value = "/goal/evidence/remove", method = RequestMethod.POST)
	public ResponseEntity<?> goalevidenceremove(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
									          @RequestHeader(name = "id", required = true) Long id) {

        Users user = (Users) request.getAttribute("user");
		return evidenceService.remove(locale, id, user);
	}

	@RequestMapping(value = "/goal/evidence/files/list", method = RequestMethod.POST)
	public ResponseEntity<?> goalevidencefileslist(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
									          @RequestHeader(name = "evidenceid", required = true) Long evidenceid) {

        Users user = (Users) request.getAttribute("user");
		return evidenceService.fileslist(locale, evidenceid, user);
	}
	

	@RequestMapping(value = "/goal/evidence/files/upload", method = RequestMethod.POST)
    public ResponseEntity<?> uploadfiles(HttpServletRequest request, 
			  							@RequestHeader(name = "Accept-Language", required = false) Locale locale,
										@RequestHeader(name = "evidenceid", required = false) Long evidenceid,
										@RequestHeader(name = "goalid", required = false) String goalid,
										@RequestParam("file") MultipartFile[] files) {
 
        Users user = (Users) request.getAttribute("user");
        return evidenceService.uploadfiles(locale, user, files, evidenceid, goalid);
    }

	@RequestMapping(value = "/goal/evidence/file/remove/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> goalevidencefileremove(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @PathVariable(name = "id", required = true) Long id) {

        Users user = (Users) request.getAttribute("user");
		return evidenceService.removefile(locale, id, user);
	}
	
	@RequestMapping(value = "/goal/evidence/comment/list", method = RequestMethod.POST)
	public ResponseEntity<?> goalevidencecommentlist(HttpServletRequest request,
													  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
													  @RequestHeader(name = "page", required = false, defaultValue = "0") Integer page,
													  @RequestHeader(name = "size", required = false, defaultValue = "0") Integer size,
													  @RequestHeader(name = "search", required = false) String search,
													  @RequestHeader(name = "sortcolumn", required = false) String sortcolumn,
													  @RequestHeader(name = "descending", required = false, defaultValue = "false") Boolean descending,
											          @RequestHeader(name = "draw", required = false, defaultValue = "1") Integer draw,
											          @RequestHeader(name = "id", required = true, defaultValue = "1") Long evidenceid) {

        Users user = (Users) request.getAttribute("user");
		return evidenceService.commentlist(locale, page, size, search, sortcolumn, descending, draw, evidenceid, user);
	}

	@RequestMapping(value = "/goal/evidence/comment/save", method = RequestMethod.POST)
	public ResponseEntity<?> goalevidencecommentsave(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
									  		  @Valid @RequestBody EvidenceCommentSaveRq req) {

        Users user = (Users) request.getAttribute("user");
		return evidenceService.commentsave(locale, req, user);
	}

	@RequestMapping(value = "/goal/evidence/comment/remove", method = RequestMethod.POST)
	public ResponseEntity<?> goalevidencecommentremove(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
									          @RequestHeader(name = "id", required = true) Long id) {

        Users user = (Users) request.getAttribute("user");
		return evidenceService.commentremove(locale, id, user);
	}
}
