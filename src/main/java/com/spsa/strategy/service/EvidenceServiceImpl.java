package com.spsa.strategy.service;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spsa.strategy.builder.response.DatatableResponse;
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.enumeration.PositionEnum;
import com.spsa.strategy.model.Evidence;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.repository.EvidenceRepository;



@Service
public class EvidenceServiceImpl implements EvidenceService {

	@Autowired
	MessageService messageService;
	
	@Autowired
	EvidenceRepository evidenceRepository;

	@Override
	public ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String goalid, Users user) {
		try {

			String currentuser = null;
			if (user.getPosition() != null) {
				if (user.getPosition().equalsIgnoreCase(PositionEnum.EMPLOYEE.name().toLowerCase()))
					currentuser = user.getUsername();
			}
			
			Page<Evidence> pages = null;
			if (sortcolumn == null) sortcolumn = "date_time";
			Specification<Evidence> spec = JPASpecification.returnGoalevidenceSpecification(search, sortcolumn, descending, goalid, user.getUser_role(), currentuser);
			
			Pageable pageable = PageRequest.of(page, size);
		    pages = evidenceRepository.findAll(spec, pageable);

			List<Evidence> allusersbysearch = evidenceRepository.findAll(spec);
			long totalrows = allusersbysearch.size();
			long recordsFiltered = totalrows;
	
	        DatatableResponse<Evidence> datatableresponse = new DatatableResponse<Evidence>(draw, totalrows, recordsFiltered, pages.getContent());
		       
			return ResponseEntity.ok(datatableresponse);
	        
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}
	
	

}
