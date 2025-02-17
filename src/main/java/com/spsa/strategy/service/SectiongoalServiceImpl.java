package com.spsa.strategy.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spsa.strategy.builder.request.SectiongoalSaveRq;
import com.spsa.strategy.builder.response.DatatableResponse;
import com.spsa.strategy.builder.response.IntRs;
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.model.Departmentgoals;
import com.spsa.strategy.model.Sectiongoals;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.repository.DepartmentgoalsRepository;
import com.spsa.strategy.repository.SectiongoalsRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Transactional
@Service
public class SectiongoalServiceImpl implements SectiongoalService {

	@Autowired
	MessageService messageService;
	
	@Autowired
	SectiongoalsRepository goalsRepository;

	@Autowired
	DepartmentgoalsRepository departmentgoalsRepository;

	@Autowired
	EvidenceService evidenceService;

	@Override
	public ResponseEntity<?> goalsave(Locale locale, @Valid SectiongoalSaveRq req, String username, Users user) {

		try {
			Integer remainingweight = calculateweight(req.getDepgoalid(), req.getId());
			if (req.getId() != null && !req.getId().trim().equals("")) {
				Optional<Sectiongoals> opt = goalsRepository.findById(req.getId());
				if (opt.isPresent())
					req.setId(opt.get().getId());
				else 
					req.setId(generateUniqueId());
			}
			else
				req.setId(generateUniqueId());
	
			int yearlyweight = Utils.concertStringtoInteger(req.getYearlyweight());
			int yearlyexpectedweight = Utils.concertStringtoInteger(req.getYearlyexpectedweight());
			if(yearlyweight < 0 || yearlyexpectedweight < 0)
				return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 112));

			if(yearlyweight > remainingweight || yearlyexpectedweight > remainingweight)
				return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 113));

			if(yearlyweight > yearlyexpectedweight)
				return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 114));
			
			Sectiongoals obj = req.returnSectiongoals(username, user);
			obj = goalsRepository.save(obj);
			return ResponseEntity.ok(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String goalid, Users user) {
		try {
			Page<Sectiongoals> pages = null;
			if (sortcolumn == null) sortcolumn = "date_time";
			Specification<Sectiongoals> spec = JPASpecification.returnSectiongoalSpecification(search, sortcolumn, descending, goalid, user.getParentrole());
		    Pageable pageable = PageRequest.of(page, size);
		    pages = goalsRepository.findAll(spec, pageable);

			List<Sectiongoals> allusersbysearch = goalsRepository.findAll(spec);
			long totalrows = allusersbysearch.size();
			long recordsFiltered = totalrows;
	
	        DatatableResponse<Sectiongoals> datatableresponse = new DatatableResponse<Sectiongoals>(draw, totalrows, recordsFiltered, pages.getContent());
		       
			return ResponseEntity.ok(datatableresponse);
	        
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

    private String generateUniqueId() {
        String id = Utils.generateId(Constants.SECTION_KEY);
        Optional<Sectiongoals> opt = goalsRepository.findById(id);
		if (opt.isPresent())
			generateUniqueId();
        return id;
    }

	@Override
	public ResponseEntity<?> goalremove(Locale locale, String goalid, String username, Users user) {
		try {
			
	        Optional<Sectiongoals> opt = goalsRepository.findById(goalid);
			if (opt.isPresent()) {

				evidenceService.removebygoalid(locale, user, goalid);
				goalsRepository.delete(opt.get());
			}
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("success_operation", locale)));   
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> details(Locale locale, String goalid, String username, Users user) {
		try {
	        Optional<Sectiongoals> opt = goalsRepository.findById(goalid);
			if (opt.isPresent()) {
				Sectiongoals goal = opt.get();
				return ResponseEntity.ok(goal);
			}
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 111));   
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> sectiongoalweight(Locale locale, String username, Users user, String depgoalid,
			String goalid) {
		try {

			IntRs val = new IntRs(calculateweight(depgoalid, goalid));
			return ResponseEntity.ok(val);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	private Integer calculateweight (String depgoalid, String goalid) throws Exception{
		Integer sum = 0;
		if (goalid != null) {
			Optional<Sectiongoals> opt = goalsRepository.findById(goalid);
			if (opt.isPresent()) 
				sum = goalsRepository.findSumOfGoalsByDepgoalidNotMatchingGoalid(depgoalid, goalid);
			else
				sum = goalsRepository.findSumOfGoalsByDepgoalid(depgoalid);
		}
		else
			sum = goalsRepository.findSumOfGoalsByDepgoalid(depgoalid);
		
		Optional<Departmentgoals> departmentgoalsopt = departmentgoalsRepository.findById(depgoalid);
		Integer parentgoalweight = departmentgoalsopt.get().getYearlyexpectedweight();
		if (sum == null) sum = 0;
		return parentgoalweight - sum;
	}

	@Override
	public String deletebydepgoalid(Locale locale, Users user, String depgoalid) {
		List<Sectiongoals> list = goalsRepository.findByDepgoalid(depgoalid);
		for (Sectiongoals secgoal : list) 
			goalremove(locale, secgoal.getId(), user.getUsername(), user);
		return "success";
	}
}
