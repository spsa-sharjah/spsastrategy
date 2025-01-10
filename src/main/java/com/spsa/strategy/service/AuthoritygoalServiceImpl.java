package com.spsa.strategy.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spsa.strategy.builder.request.AuthoritygoalSaveRq;
import com.spsa.strategy.builder.request.ResrictedGoalRolesRq;
import com.spsa.strategy.builder.response.DatatableResponse;
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.model.Authoritygoals;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.repository.AuthoritygoalsRepository;

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
	private AuthService authService;
	
	@Override
	public ResponseEntity<?> goalsave(Locale locale, @Valid AuthoritygoalSaveRq req, String username, Users user) {
        
		if (req.getId() != null && !req.getId().trim().equals("")) {
			Optional<Authoritygoals> opt = goalsRepository.findById(req.getId());
			if (opt.isPresent())
				req.setId(opt.get().getId());
			else 
				req.setId(generateUniqueId());
		}
		else
			req.setId(generateUniqueId());
		
		Authoritygoals obj = req.returnAuthoritygoals(username, user.getUser_role());
		obj = goalsRepository.save(obj);
		
		ResrictedGoalRolesRq rq = new ResrictedGoalRolesRq(req.getId(), req.getRoles());
		authService.rolegoalsaccesssave(locale, user, rq);
		
		return ResponseEntity.ok(obj);
	}

	@Override
	public ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String username, Users user, Boolean all) {
		try {
			
			String getbyusername = null;
			Page<Authoritygoals> pages = null;
			if (sortcolumn == null) sortcolumn = "date_time";
			Specification<Authoritygoals> spec = JPASpecification.returnAuthoritygoalSpecification(search, sortcolumn, descending, getbyusername, user.getUser_role());

			if (all != null && all == true) {
				List<Authoritygoals> allusersbysearch = goalsRepository.findAll(spec);
				return ResponseEntity.ok(allusersbysearch);
			}
			
			Pageable pageable = PageRequest.of(page, size);

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
	public ResponseEntity<?> goalremove(Locale locale, String goalid, String username, Users user) {
        Optional<Authoritygoals> opt = goalsRepository.findById(goalid);
		if (opt.isPresent()) {
			goalsRepository.delete(opt.get());
		}
		// TODO remove all related child rows
		return ResponseEntity.ok(new MessageResponse(messageService.getMessage("success_operation", locale)));
	}

	@Override
	public ResponseEntity<?> details(Locale locale, String goalid, String username, Users user, Boolean wheightcalculation) {
		Optional<Authoritygoals> opt = goalsRepository.findById(goalid);
		if (opt.isPresent()) {
			Authoritygoals goal = opt.get();
			if (wheightcalculation != null && wheightcalculation == true) {
				Integer sum = goalsRepository.findSumOfGoalsByYear(goal.getYear());
				if (sum == null) sum = 0;
				goal.setRemainingwheight(100 - sum);
			}
			
			return ResponseEntity.ok(goal);
		}
		return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 111));
	}
}
