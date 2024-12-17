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
import com.spsa.strategy.builder.response.DatatableResponse;
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.enumeration.UserLevelEnum;
import com.spsa.strategy.enumeration.UserRoleEnum;
import com.spsa.strategy.model.Departmentgoals;
import com.spsa.strategy.model.Userlevel;
import com.spsa.strategy.repository.DepartmentgoalsRepository;
import com.spsa.strategy.repository.UserlevelRepository;

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
	UserlevelRepository userlevelRepository;

	@Override
	public ResponseEntity<?> goalsave(Locale locale, @Valid DepartmentgoalSaveRq req, String username, String strategylevelid) {

        Optional<Userlevel> userlevel = userlevelRepository.findById(strategylevelid);
        if (!userlevel.isPresent() || 
        		userlevel.get().getLevel() == null || 
        		userlevel.get().getLevel().equals(UserLevelEnum.SECTION.name()) ||
        		!userlevel.get().getRole().equals(UserRoleEnum.MANAGER.name()))
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("not_authorized_msg", locale), 604));
        
		if (req.getId() != null && !req.getId().trim().equals("")) {
			Optional<Departmentgoals> opt = goalsRepository.findById(req.getId());
			if (opt.isPresent())
				req.setId(opt.get().getId());
			else 
				req.setId(generateUniqueId());
		}
		else
			req.setId(generateUniqueId());
		
		Departmentgoals obj = req.returnDepartmentgoals(username);
		obj = goalsRepository.save(obj);
		return ResponseEntity.ok(obj);
	}

	@Override
	public ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String goalid, String strategylevelid) {
		try {

	        Optional<Userlevel> userlevel = userlevelRepository.findById(strategylevelid);
	        if (!userlevel.isPresent() || 
	        		userlevel.get().getLevel() == null || 
	        		userlevel.get().getLevel().equals(UserLevelEnum.SECTION.name()) ||
	        		!userlevel.get().getRole().equals(UserRoleEnum.MANAGER.name()))
				return ResponseEntity.ok(new MessageResponse(messageService.getMessage("not_authorized_msg", locale), 604));
	        
			Page<Departmentgoals> pages = null;
			if (sortcolumn == null) sortcolumn = "date_time";
			Specification<Departmentgoals> spec = JPASpecification.returnDepartmentgoalSpecification(search, sortcolumn, descending, goalid);
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
}
