package com.spsa.strategy.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spsa.strategy.builder.request.AuthoritygoalSaveRq;
import com.spsa.strategy.builder.request.DepartmentgoalSaveRq;
import com.spsa.strategy.service.AuthoritygoalService;
import com.spsa.strategy.service.DepartmentgoalService;

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

//	@Autowired
//	private SectiongoalService sectiongoalService;

	@RequestMapping(value = "/authority/goal/list", method = RequestMethod.POST)
	public ResponseEntity<?> authoritygoallist(HttpServletRequest request,
								  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
								  @RequestHeader(name = "page", required = false, defaultValue = "0") Integer page,
								  @RequestHeader(name = "size", required = false, defaultValue = "0") Integer size,
								  @RequestHeader(name = "search", required = false) String search,
								  @RequestHeader(name = "sortcolumn", required = false) String sortcolumn,
								  @RequestHeader(name = "descending", required = false, defaultValue = "false") Boolean descending,
						          @RequestHeader(name = "draw", required = false, defaultValue = "1") Integer draw) {

        String strategylevelid = (String) request.getAttribute("strategylevelid");
		return authoritygoalService.list(locale, page, size, search, sortcolumn, descending, draw, null, strategylevelid);
	}
	
	@RequestMapping(value = "/authority/goal/save", method = RequestMethod.POST)
	public ResponseEntity<?> authoritygoalsave(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestHeader(name = "username", required = true) String username,
									  		  @Valid @RequestBody AuthoritygoalSaveRq req) {

        String strategylevelid = (String) request.getAttribute("strategylevelid");
		return authoritygoalService.goalsave(locale, req, username, strategylevelid);
	}
	
	@RequestMapping(value = "/authority/goal/remove", method = RequestMethod.POST)
	public ResponseEntity<?> authoritygoalremove(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestHeader(name = "username", required = true) String username,
											  @RequestHeader(name = "goalid", required = true) String goalid) {

        String strategylevelid = (String) request.getAttribute("strategylevelid");
		return authoritygoalService.goalremove(locale, goalid, username, strategylevelid);
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
						          @RequestHeader(name = "goalid", required = true) String goalid) {

        String strategylevelid = (String) request.getAttribute("strategylevelid");
		return departmentgoalService.list(locale, page, size, search, sortcolumn, descending, draw, goalid, strategylevelid);
	}
	
	@RequestMapping(value = "/department/goal/save", method = RequestMethod.POST)
	public ResponseEntity<?> departmentgoalsave(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestHeader(name = "username", required = true) String username,
									  		  @Valid @RequestBody DepartmentgoalSaveRq req) {

        String strategylevelid = (String) request.getAttribute("strategylevelid");
		return departmentgoalService.goalsave(locale, req, username, strategylevelid);
	}
	
	@RequestMapping(value = "/department/goal/remove", method = RequestMethod.POST)
	public ResponseEntity<?> departmentgoalremove(HttpServletRequest request,
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestHeader(name = "username", required = true) String username,
											  @RequestHeader(name = "goalid", required = true) String goalid) {

        String strategylevelid = (String) request.getAttribute("strategylevelid");
		return departmentgoalService.goalremove(locale, goalid, username, strategylevelid);
	}
}
