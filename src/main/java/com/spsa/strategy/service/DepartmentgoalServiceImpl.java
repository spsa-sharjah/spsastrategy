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

import com.spsa.strategy.builder.request.DepartmentgoalSaveRq;
import com.spsa.strategy.builder.request.ResrictedGoalRolesRq;
import com.spsa.strategy.builder.response.DatatableResponse;
import com.spsa.strategy.builder.response.IntRs;
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.enumeration.CustomAction;
import com.spsa.strategy.enumeration.Menuauthid;
import com.spsa.strategy.enumeration.YearlyGoalStatus;
import com.spsa.strategy.model.Authoritygoals;
import com.spsa.strategy.model.Departmentgoals;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.model.YearlyGoalsSettings;
import com.spsa.strategy.repository.AuthoritygoalsRepository;
import com.spsa.strategy.repository.DepartmentgoalsRepository;
import com.spsa.strategy.repository.YearlyGoalsSettingsRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Transactional
@Service
public class DepartmentgoalServiceImpl implements DepartmentgoalService {

	@Autowired
	MessageService messageService;
	
	@Autowired
	DepartmentgoalsRepository goalsRepository;

	@Autowired
	private AuthService authService;
	
	@Autowired
	AuthoritygoalsRepository authoritygoalsRepository;
	
	@Autowired
	SectiongoalService sectiongoalService;

	@Autowired
	YearlyGoalsSettingsRepository yearlyGoalsSettingsRepository;

	@Override
	public ResponseEntity<?> goalsave(Locale locale, @Valid DepartmentgoalSaveRq req, String username, Users user) {

		try {
			Integer remainingweight = calculateweight(req.getAuthgoalid(), req.getId());
			Departmentgoals existingdeptgoal = null;
			if (req.getId() != null && !req.getId().trim().equals("")) {
				Optional<Departmentgoals> opt = goalsRepository.findById(req.getId());
				if (opt.isPresent()) {
					existingdeptgoal = opt.get();
					req.setId(existingdeptgoal.getId());
				} else 
					req.setId(generateUniqueId());
			}
			else
				req.setId(generateUniqueId());
	
			int yearlyweight = Utils.concertStringtoInteger(req.getYearlyweight());
			int yearlyexpectedweight = Utils.concertStringtoInteger(req.getYearlyexpectedweight());
			if(yearlyweight < 0 || yearlyexpectedweight < 0)
				return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 112));

			if (Utils.isapiauthorized(CustomAction.VerifyPercentage.name(), Menuauthid.managedepartmentgoals.name(), user.getAuthorizedapis())) 
				if(yearlyweight > remainingweight || yearlyexpectedweight > remainingweight)
					return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 113));

			if(yearlyweight > yearlyexpectedweight)
				return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 114));
			
			Departmentgoals obj = req.returnDepartmentgoals(username, user, existingdeptgoal);
			obj = goalsRepository.save(obj);
			
			updatestatus(req.getAuthgoalid());
			
			ResrictedGoalRolesRq rq = new ResrictedGoalRolesRq(req.getId(), req.getRoles());
			authService.rolegoalsaccesssave(locale, user, rq);
			
			return ResponseEntity.ok(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}
	private void updatestatus(String authgoalid) {
		Optional<Authoritygoals> opt = authoritygoalsRepository.findById(authgoalid);
		if (opt.isPresent()) {
			Authoritygoals authoritygoals = opt.get();

			Optional<YearlyGoalsSettings> optyg = yearlyGoalsSettingsRepository.findByYear(authoritygoals.getYear());
			if (optyg.isPresent()) {
				YearlyGoalsSettings yearlyGoalsSettings = optyg.get();

				String status = yearlyGoalsSettings.isSkipendorsement() ? YearlyGoalStatus.EndorsementSkipped.name(): YearlyGoalStatus.New.name();
				
				yearlyGoalsSettings.setStatus(status);
				yearlyGoalsSettingsRepository.save(yearlyGoalsSettings);
				
				goalsRepository.updategoalendorsementstatus(authgoalid, status);
			}
		}
	}
	
	@Override
	public ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String goalid, Users user, Boolean all) {
		try {
			Page<Departmentgoals> pages = null;
			if (sortcolumn == null) sortcolumn = "date_time";

			boolean showApprovedOnly = false;
			if (Utils.isapiauthorized(CustomAction.ShowApprovedOnly.name(), Menuauthid.managedepartmentgoals.name(), user.getAuthorizedapis()))
				showApprovedOnly = true;
			
			Specification<Departmentgoals> spec = JPASpecification.returnDepartmentgoalSpecification(search, sortcolumn, descending, goalid, user.getUser_role(), user.getParentrole(), showApprovedOnly);

			if (all != null && all == true) {
				List<Departmentgoals> allbysearch = goalsRepository.findAll(spec);
				return ResponseEntity.ok(allbysearch);
			}
			
			Pageable pageable = PageRequest.of(page, size);
		    pages = goalsRepository.findAll(spec, pageable);

			List<Departmentgoals> allusersbysearch = goalsRepository.findAll(spec);
			long totalrows = allusersbysearch.size();
			long recordsFiltered = totalrows;
	
	        DatatableResponse<Departmentgoals> datatableresponse = new DatatableResponse<Departmentgoals>(draw, totalrows, recordsFiltered, pages.getContent());
		       
			return ResponseEntity.ok(datatableresponse);
	        
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

    private String generateUniqueId() {
        String id = Utils.generateId(Constants.DEPARTMENT_KEY);
        Optional<Departmentgoals> opt = goalsRepository.findById(id);
		if (opt.isPresent())
			generateUniqueId();
        return id;
    }

	@Override
	public ResponseEntity<?> goalremove(Locale locale, String goalid, String username, Users user) {
		try {
	        Optional<Departmentgoals> opt = goalsRepository.findById(goalid);
			if (opt.isPresent()) {
				sectiongoalService.deletebydepgoalid(locale, user, goalid);
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
	        Optional<Departmentgoals> opt = goalsRepository.findById(goalid);
			if (opt.isPresent()) {
				Departmentgoals goal = opt.get();
				return ResponseEntity.ok(goal);
			}
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 111));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> departmentgoalweight(Locale locale, String username, Users user, String authgoalid,
			String goalid) {
		try {
			IntRs val = new IntRs(calculateweight(authgoalid, goalid));
			return ResponseEntity.ok(val);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}
	
	private Integer calculateweight (String authgoalid, String goalid) throws Exception{
		Integer sum = 0;
		if (goalid != null) {
			Optional<Departmentgoals> opt = goalsRepository.findById(goalid);
			if (opt.isPresent()) 
				sum = goalsRepository.findSumOfGoalsByAuthgoalidYearNotMatchingGoalid(authgoalid, goalid);
			else
				sum = goalsRepository.findSumOfGoalsByAuthgoalid(authgoalid);
		}
		else
			sum = goalsRepository.findSumOfGoalsByAuthgoalid(authgoalid);
		Optional<Authoritygoals> authoritygoalsopt = authoritygoalsRepository.findById(authgoalid);
		Integer parentgoalweight = authoritygoalsopt.get().getYearlyexpectedweight();
		if (sum == null) sum = 0;
		
		return parentgoalweight - sum;
	}

	@Override
	public String deletebyauthgoalid(Locale locale, Users user, String authgoalid) {
		List<Departmentgoals> list = goalsRepository.findByAuthgoalid(authgoalid);
		for (Departmentgoals depgoal : list) 
			goalremove(locale, depgoal.getId(), user.getUsername(), user);
		return "success";
	}
}
