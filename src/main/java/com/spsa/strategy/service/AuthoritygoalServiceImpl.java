package com.spsa.strategy.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spsa.strategy.builder.request.AuthoritygoalSaveRq;
import com.spsa.strategy.builder.response.DatatableResponse;
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.enumeration.UserLevelEnum;
import com.spsa.strategy.enumeration.UserRoleEnum;
import com.spsa.strategy.model.Authoritygoals;
import com.spsa.strategy.model.Userlevel;
import com.spsa.strategy.repository.AuthoritygoalsRepository;
import com.spsa.strategy.repository.UserlevelRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Transactional
@Service
public class AuthoritygoalServiceImpl implements AuthoritygoalService {

	@Autowired
	MessageService messageService;
	
	@Autowired
	AuthoritygoalsRepository goalsRepository;
	
	@Autowired
	UserlevelRepository userlevelRepository;

	@Override
	public ResponseEntity<?> goalsave(Locale locale, @Valid AuthoritygoalSaveRq req, String username, String strategylevelid) {

        Optional<Userlevel> userlevel = userlevelRepository.findById(strategylevelid);
        if (!userlevel.isPresent() || 
        		userlevel.get().getLevel() == null || 
        		!userlevel.get().getLevel().equals(UserLevelEnum.AUTHORITY.name()) ||
        		!userlevel.get().getRole().equals(UserRoleEnum.MANAGER.name()))
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("not_authorized_msg", locale), 604));
        
		if (req.getId() != null && !req.getId().trim().equals("")) {
			Optional<Authoritygoals> opt = goalsRepository.findById(req.getId());
			if (opt.isPresent())
				req.setId(opt.get().getId());
			else 
				req.setId(generateUniqueId());
		}
		else
			req.setId(generateUniqueId());
		
		Authoritygoals obj = req.returnAuthoritygoals(username);
		obj = goalsRepository.save(obj);
		return ResponseEntity.ok(obj);
	}

	@Override
	public ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String username, String strategylevelid) {
		try {

			String getbyusername = null;
	        Optional<Userlevel> userlevel = userlevelRepository.findById(strategylevelid);
	        if (!userlevel.isPresent() || 
	        		userlevel.get().getLevel() == null || 
	        		!userlevel.get().getRole().equals(UserRoleEnum.MANAGER.name()))
				return ResponseEntity.ok(new MessageResponse(messageService.getMessage("not_authorized_msg", locale), 604));
	        
			Page<Authoritygoals> pages = null;
			if (sortcolumn == null) sortcolumn = "date_time";
			Specification<Authoritygoals> spec = JPASpecification.returnAuthoritygoalSpecification(search, sortcolumn, descending, getbyusername);
		    Pageable pageable = PageRequest.of(page, size);
		    

	        if (userlevel.isPresent() &&
		        		userlevel.get().getLevel() != null && 
		        		userlevel.get().getLevel().equals(UserLevelEnum.DEPARTMENT.name()) &&
		        		userlevel.get().getRole().equals(UserRoleEnum.MANAGER.name()))
        		pages = goalsRepository.findnonrestrictedgoals(strategylevelid, pageable);
	        else
	        	pages = goalsRepository.findAll(spec, pageable);

			List<Authoritygoals> allusersbysearch = goalsRepository.findAll(spec);
			long totalrows = allusersbysearch.size();
			long recordsFiltered = totalrows;
	
	        DatatableResponse<Authoritygoals> datatableresponse = new DatatableResponse<Authoritygoals>(draw, totalrows, recordsFiltered, pages.getContent());
		       
			return ResponseEntity.ok(datatableresponse);
	        
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

    private String generateUniqueId() {
        String id = Utils.generateId(Constants.AUTHORITY_KEY);
        Optional<Authoritygoals> opt = goalsRepository.findById(id);
		if (opt.isPresent())
			generateUniqueId();
        return id;
    }

	@Override
	public ResponseEntity<?> goalremove(Locale locale, String goalid, String username, String strategylevelid) {
        Optional<Authoritygoals> opt = goalsRepository.findById(goalid);
		if (opt.isPresent()) {
			goalsRepository.delete(opt.get());
		}
		// TODO remove all related child rows
		return ResponseEntity.ok(new MessageResponse(messageService.getMessage("success_operation", locale)));
	}
}
