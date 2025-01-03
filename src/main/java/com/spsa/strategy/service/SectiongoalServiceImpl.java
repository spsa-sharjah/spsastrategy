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
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.enumeration.PositionEnum;
import com.spsa.strategy.enumeration.LevelEnum;
import com.spsa.strategy.model.Sectiongoals;
import com.spsa.strategy.model.Users;
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

	@Override
	public ResponseEntity<?> goalsave(Locale locale, @Valid SectiongoalSaveRq req, String username, Users user) {
        
		if (req.getId() != null && !req.getId().trim().equals("")) {
			Optional<Sectiongoals> opt = goalsRepository.findById(req.getId());
			if (opt.isPresent())
				req.setId(opt.get().getId());
			else 
				req.setId(generateUniqueId());
		}
		else
			req.setId(generateUniqueId());
		
		Sectiongoals obj = req.returnSectiongoals(username, user.getUser_role());
		obj = goalsRepository.save(obj);
		return ResponseEntity.ok(obj);
	}

	@Override
	public ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String goalid, Users user) {
		try {

			String parentrole = null;
			if (user.getLevel() != null) {
				if (user.getLevel().equalsIgnoreCase(LevelEnum.SECTION.name().toLowerCase()) &&
						user.getPosition().equalsIgnoreCase(PositionEnum.EMPLOYEE.name().toLowerCase()))
					parentrole = user.getParentrole();
			}
			
			Page<Sectiongoals> pages = null;
			if (sortcolumn == null) sortcolumn = "date_time";
			Specification<Sectiongoals> spec = JPASpecification.returnSectiongoalSpecification(search, sortcolumn, descending, goalid, parentrole);
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

        Optional<Sectiongoals> opt = goalsRepository.findById(goalid);
		if (opt.isPresent()) {
			goalsRepository.delete(opt.get());
		}
		// TODO remove all related child rows
		return ResponseEntity.ok(new MessageResponse(messageService.getMessage("success_operation", locale)));
	}

	@Override
	public ResponseEntity<?> details(Locale locale, String goalid, String username, Users user) {

        Optional<Sectiongoals> opt = goalsRepository.findById(goalid);
		if (opt.isPresent()) {
			return ResponseEntity.ok(opt.get());
		}
		return ResponseEntity.ok(new MessageResponse(messageService.getMessage("invalid_params", locale), 111));
	}
}
