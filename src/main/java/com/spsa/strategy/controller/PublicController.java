package com.spsa.strategy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spsa.strategy.model.Users;
import com.spsa.strategy.service.EvidenceService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(path = "/api/public/")
public class PublicController {

	@Autowired
	private EvidenceService evidenceService;

	@RequestMapping(value = "/goal/evidence/download/file/{fileName}", method = RequestMethod.GET)
    public ResponseEntity<?> downloadfile(HttpServletRequest request, 
										@PathVariable String fileName) {

        Users user = (Users) request.getAttribute("user");
        return evidenceService.downloadfile(user, fileName);
    }

	@RequestMapping(value = "/goal/evidence/file/{fileName}", method = RequestMethod.GET)
    public ResponseEntity<?> returnbase64file(HttpServletRequest request, 
										@PathVariable String fileName) {

        Users user = (Users) request.getAttribute("user");
        return evidenceService.returnbase64file(user, fileName);
    }
}
