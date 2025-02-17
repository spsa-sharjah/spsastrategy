package com.spsa.strategy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spsa.strategy.builder.request.EndorseGoalsRq;
import com.spsa.strategy.builder.request.EndorseRq;
import com.spsa.strategy.builder.response.GoalsTree;
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.enumeration.GoalStatus;
import com.spsa.strategy.model.Authoritygoals;
import com.spsa.strategy.model.Departmentgoals;
import com.spsa.strategy.model.Evidence;
import com.spsa.strategy.model.Sectiongoals;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.repository.AuthoritygoalsRepository;
import com.spsa.strategy.repository.DepartmentgoalsRepository;
import com.spsa.strategy.repository.EvidenceRepository;
import com.spsa.strategy.repository.GoalStatusRepository;
import com.spsa.strategy.repository.SectiongoalsRepository;

import jakarta.validation.Valid;

@Service
public class GoalServiceImpl implements GoalService {
	
	@Autowired
	private GoalStatusRepository goalStatusRepository;
	
	@Autowired
	private AuthoritygoalsRepository authoritygoalsRepository;
	
	@Autowired
	private DepartmentgoalsRepository departmentgoalsRepository;
	
	@Autowired
	private SectiongoalsRepository sectiongoalsRepository;
	
	@Autowired
	private EvidenceRepository evidenceRepository;
	
	@Autowired
	private MessageService messageService;

	@Override
	public ResponseEntity<?> list(Locale locale, Users user) {
		return ResponseEntity.ok(goalStatusRepository.findAll());
	}

	@Override
	public ResponseEntity<?> authoritygoaltree(Users user, Locale locale, String year, String team, boolean showdepartment, boolean showsection, boolean showevidence) {
		
		if (team != null && team.equalsIgnoreCase("empty")) team = null;
		List<Authoritygoals> authoritygoalslist = team != null ? authoritygoalsRepository.findByYearAndTean(year, team) : authoritygoalsRepository.findByYear(year);

		List<GoalsTree> goalstree = new ArrayList<GoalsTree>();
		
		for (Authoritygoals authoritygoals : authoritygoalslist) {

			List<Departmentgoals> departmentgoalslist = showdepartment ? departmentgoalsRepository.findByAuthgoalid(authoritygoals.getId()) : new ArrayList<Departmentgoals>();

			for (Departmentgoals departmentgoals : departmentgoalslist) {

				List<Evidence> departmentevidencelist = showevidence ? evidenceRepository.findByGoalid(departmentgoals.getId()) : new ArrayList<Evidence>();
				departmentgoals.setEvidencelist(departmentevidencelist);

				List<Sectiongoals> sectiongoalslist = showsection ? sectiongoalsRepository.findByDepgoalid(departmentgoals.getId()) : new ArrayList<Sectiongoals>();
				departmentgoals.setSectiongoalslist(sectiongoalslist);

				List<Evidence> sectionevidencelist = showevidence ? evidenceRepository.findByGoalid(authoritygoals.getId()) : new ArrayList<Evidence>();
				for (Sectiongoals section : sectiongoalslist) {
					section.setEvidencelist(sectionevidencelist);
				}
			}
				
			GoalsTree goalsTree = new GoalsTree(authoritygoals, departmentgoalslist);
			goalstree.add(goalsTree);
		}
		return ResponseEntity.ok(goalstree);
	}

	@Override
	public ResponseEntity<?> authoritygoalendorse(Users user, Locale locale, @Valid EndorseGoalsRq req) {

		Optional<Authoritygoals> authoritygoalopt = authoritygoalsRepository.findById(req.getAuthgoalid());
		
		if (authoritygoalopt.isPresent()) {
			
			List<Departmentgoals> selecteddepartmentgoals = new ArrayList<Departmentgoals>();
			Authoritygoals authoritygoals = authoritygoalopt.get();
			int totalpercentage = authoritygoals.getYearlyexpectedweight();
			int sumdeppercentage = 0;
			for (EndorseRq depgoal : req.getChoosendepgoallist()) {

				Optional<Departmentgoals> departmentgoalopt = departmentgoalsRepository.findById(depgoal.getDepgoalid());
				if (departmentgoalopt.isPresent()) {

					Departmentgoals departmentgoals = departmentgoalopt.get();

					departmentgoals.setStatus(depgoal.isApproved() ? GoalStatus.Approved.name() : GoalStatus.Unapproved.name());
					if (depgoal.getReason() != null)
						departmentgoals.setReason(depgoal.getReason());
					selecteddepartmentgoals.add(departmentgoals);
					
					int deppercentage = departmentgoals.getYearlyexpectedweight();
					sumdeppercentage += deppercentage;
				}
			}
			
			if (totalpercentage != sumdeppercentage)
				return new ResponseEntity<MessageResponse>(new MessageResponse(messageService.getMessage("match_total_percentage", locale), 413), HttpStatus.OK);
				
			
			for (Departmentgoals depgoal : selecteddepartmentgoals) {
				departmentgoalsRepository.save(depgoal);
			}

			return new ResponseEntity<MessageResponse>(new MessageResponse(messageService.getMessage("success_operation", locale)), HttpStatus.OK);
		}

		return new ResponseEntity<MessageResponse>(new MessageResponse(messageService.getMessage("server_error", locale), 413), HttpStatus.OK);
	}

}
