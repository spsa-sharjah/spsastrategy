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
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.enumeration.LevelEnum;
import com.spsa.strategy.enumeration.PositionEnum;
import com.spsa.strategy.model.Authoritygoals;
import com.spsa.strategy.model.Departmentgoals;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.repository.AuthoritygoalsRepository;
import com.spsa.strategy.repository.DepartmentgoalsRepository;

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

	@Override
	public ResponseEntity<?> goalsave(Locale locale, @Valid DepartmentgoalSaveRq req, String username, Users user) {
        
		if (req.getId() != null && !req.getId().trim().equals("")) {
			Optional<Departmentgoals> opt = goalsRepository.findById(req.getId());
			if (opt.isPresent())
				req.setId(opt.get().getId());
			else 
				req.setId(generateUniqueId());
		}
		else
			req.setId(generateUniqueId());
		
		Departmentgoals obj = req.returnDepartmentgoals(username, user.getUser_role());
		obj = goalsRepository.save(obj);
		
		ResrictedGoalRolesRq rq = new ResrictedGoalRolesRq(req.getId(), req.getRoles());
		authService.rolegoalsaccesssave(locale, user, rq);
		
		return ResponseEntity.ok(obj);
	}

	@Override
	public ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String goalid, Users user, Boolean all) {
		try {

			String parentrole = null;
			if (user.getLevel() != null && user.getPosition() != null) {
				if (user.getLevel().equalsIgnoreCase(LevelEnum.SECTION.name().toLowerCase()) &&
						user.getPosition().equalsIgnoreCase(PositionEnum.MANAGER.name().toLowerCase()))
					parentrole = user.getParentrole();
	
				else if (user.getLevel().equalsIgnoreCase(LevelEnum.DEPARTMENT.name().toLowerCase()) &&
						user.getPosition().equalsIgnoreCase(PositionEnum.EMPLOYEE.name().toLowerCase()))
					parentrole = user.getParentrole();
			}
			
			Page<Departmentgoals> pages = null;
			if (sortcolumn == null) sortcolumn = "date_time";
			Specification<Departmentgoals> spec = JPASpecification.returnDepartmentgoalSpecification(search, sortcolumn, descending, goalid, user.getUser_role(), parentrole);

			if (all != null && all == true) {
				List<Departmentgoals> allusersbysearch = goalsRepository.findAll(spec);
				return ResponseEntity.ok(allusersbysearch);
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

        Optional<Departmentgoals> opt = goalsRepository.findById(goalid);
		if (opt.isPresent()) {
			goalsRepository.delete(opt.get());
		}
		// TODO remove all related child rows
		return ResponseEntity.ok(new MessageResponse(messageService.getMessage("success_operation", locale)));
	}

	@Override
	public ResponseEntity<?> details(Locale locale, String goalid, String username, Users user, Boolean wheightcalculation) {

        Optional<Departmentgoals> opt = goalsRepository.findById(goalid);
		if (opt.isPresent()) {
			Departmentgoals goal = opt.get();
			if (wheightcalculation != null && wheightcalculation == true) {
				Optional<Authoritygoals> authoritygoalsopt = authoritygoalsRepository.findById(goal.getAuthgoalid());
				Integer parentgoalweight = authoritygoalsopt.get().getYearlyweight();
				Integer sum = goalsRepository.findSumOfGoalsByAuthgoalid(goal.getAuthgoalid());
				if (sum == null) sum = 0;
				goal.setRemainingwheight(parentgoalweight - sum);
			}
			return ResponseEntity.ok(goal);
		}
		return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 111));
	}
}
