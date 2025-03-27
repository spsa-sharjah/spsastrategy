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

import com.spsa.strategy.builder.request.AuthoritygoalSaveRq;
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
import com.spsa.strategy.model.Users;
import com.spsa.strategy.model.YearlyGoalsSettings;
import com.spsa.strategy.repository.AuthoritygoalsRepository;
import com.spsa.strategy.repository.YearlyGoalsSettingsRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Transactional
@Service
public class AuthoritygoalServiceImpl implements AuthoritygoalService {

	@Autowired
	MessageService messageService;
	
	@Autowired
	AuthoritygoalsRepository goalsRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	DepartmentgoalService departmentgoalService;
	
	@Autowired
	YearlyGoalsSettingsRepository yearlyGoalsSettingsRepository;
	
	@Override
	public ResponseEntity<?> goalsave(Locale locale, @Valid AuthoritygoalSaveRq req, String username, Users user) {

		try {
			Authoritygoals existingauthgoal = null;
			Integer remainingweight = calculateweight(req.getYear(), req.getId());
			if (req.getId() != null && !req.getId().trim().equals("")) {
				Optional<Authoritygoals> opt = goalsRepository.findById(req.getId());
				if (opt.isPresent()) {
					existingauthgoal = opt.get();
					req.setId(existingauthgoal.getId());
				}
				else 
					req.setId(generateUniqueId());
			}
			else
				req.setId(generateUniqueId());

			int yearlyweight = Utils.concertStringtoInteger(req.getYearlyweight());
			int yearlyexpectedweight = Utils.concertStringtoInteger(req.getYearlyexpectedweight());
			if(yearlyweight < 0 || yearlyexpectedweight < 0)
				return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 112));

			if (Utils.isapiauthorized(CustomAction.VerifyPercentage.name(), Menuauthid.manageauthoritygoals.name(), user.getAuthorizedapis()))
				if(yearlyweight > remainingweight || yearlyexpectedweight > remainingweight)
					return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 113));

			if(yearlyweight > yearlyexpectedweight)
				return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 114));

			Optional<YearlyGoalsSettings> optyg = yearlyGoalsSettingsRepository.findByYear(req.getYear());
			boolean skipendorsement = false;
			if (optyg.isPresent()) {
				YearlyGoalsSettings yearlyGoalsSettings = optyg.get();
				skipendorsement = yearlyGoalsSettings.isSkipendorsement();
				String status = YearlyGoalStatus.New.name();
				if (skipendorsement) status = YearlyGoalStatus.EndorsementSkipped.name();
				yearlyGoalsSettings.setStatus(status);
				yearlyGoalsSettingsRepository.save(yearlyGoalsSettings);
			}
			
			String reference = existingauthgoal == null ? returnuniquereference(req.getYear()) : existingauthgoal.getRef();
			Authoritygoals obj = req.returnAuthoritygoals(username, user, Menuauthid.manageauthoritygoals.name(), skipendorsement, reference, existingauthgoal);
			obj = goalsRepository.save(obj);
			
			ResrictedGoalRolesRq rq = new ResrictedGoalRolesRq(req.getId(), req.getRoles());
			authService.rolegoalsaccesssave(locale, user, rq);
			
			return ResponseEntity.ok(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	private String returnuniquereference(String year) {
		int yearlycount = goalsRepository.countyearlygoals(year);
		yearlycount++;
		return Constants.SA.replace("*YEAR*", year) + yearlycount;
	}

	@Override
	public ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String username, Users user, Boolean all, String year, String team) {
		try {
			
			String getbyusername = null;
			Page<Authoritygoals> pages = null;
			if (sortcolumn == null) sortcolumn = "date_time";

			boolean showApprovedOnly = false;
			if (Utils.isapiauthorized(CustomAction.ShowApprovedOnly.name(), Menuauthid.manageauthoritygoals.name(), user.getAuthorizedapis()))
				showApprovedOnly = true; // Show all not new = already approved and more
			Specification<Authoritygoals> spec = JPASpecification.returnAuthoritygoalSpecification(search, sortcolumn, descending, getbyusername, user.getUser_role(), year, team, showApprovedOnly);

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
		try {
	        Optional<Authoritygoals> opt = goalsRepository.findById(goalid);
			if (opt.isPresent()) {
				departmentgoalService.goalremove(locale, goalid, username, user);
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
			Optional<Authoritygoals> opt = goalsRepository.findById(goalid);
			if (opt.isPresent()) {
				Authoritygoals goal = opt.get();
				
				return ResponseEntity.ok(goal);
			}
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 111));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> authoritygoalweight(Locale locale, String username, Users user, String year, String goalid) {
		try {
			IntRs val = new IntRs(calculateweight(year, goalid));
			return ResponseEntity.ok(val);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	private Integer calculateweight (String year, String goalid) throws Exception{

		Integer sum = 0;
		if (goalid != null) {
			Optional<Authoritygoals> opt = goalsRepository.findById(goalid);
			if (opt.isPresent()) 
				sum = goalsRepository.findSumOfGoalsByYearNotMatchingGoalid(year, goalid);
			else
				sum = goalsRepository.findSumOfGoalsByYear(year);
		}
		else
			sum = goalsRepository.findSumOfGoalsByYear(year);
		if (sum == null) sum = 0;
		
		return 100 - sum;
	}
}
