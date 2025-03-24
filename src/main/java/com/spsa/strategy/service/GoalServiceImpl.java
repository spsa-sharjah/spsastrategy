package com.spsa.strategy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spsa.strategy.builder.request.EndorseGoalsRq;
import com.spsa.strategy.builder.request.EndorseRq;
import com.spsa.strategy.builder.request.YearlySettingsRq;
import com.spsa.strategy.builder.response.GoalsTree;
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.enumeration.CustomAction;
import com.spsa.strategy.enumeration.GoalStatus;
import com.spsa.strategy.enumeration.Menuauthid;
import com.spsa.strategy.enumeration.YearlyGoalStatus;
import com.spsa.strategy.model.Authoritygoals;
import com.spsa.strategy.model.Departmentgoals;
import com.spsa.strategy.model.Evidence;
import com.spsa.strategy.model.Sectiongoals;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.model.YearlyGoalsSettings;
import com.spsa.strategy.repository.AuthoritygoalsRepository;
import com.spsa.strategy.repository.DepartmentgoalsRepository;
import com.spsa.strategy.repository.EvidenceRepository;
import com.spsa.strategy.repository.GoalStatusRepository;
import com.spsa.strategy.repository.SectiongoalsRepository;
import com.spsa.strategy.repository.YearlyGoalsSettingsRepository;
import com.spsa.strategy.rest.call.ChildRolesUsersList;

import jakarta.servlet.http.HttpServletRequest;
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

	@Autowired
	private YearlyGoalsSettingsRepository yearlyGoalsSettingsRepository;
	
	@Autowired
	private NotificationService notificationService;

	@Value("${spring.auth.endpoint.api}") 
	private String authendpointapi;

	@Value("${spring.spsa.childrolesuserslist.api}") 
	private String childrolesuserslistapi;

	@Autowired
	private SettingsService settingsService;
	
	@Override
	public ResponseEntity<?> list(Locale locale, Users user, String menuauthid) {
		return menuauthid != null ? ResponseEntity.ok(menuauthid.equalsIgnoreCase(Constants.ALL) ? goalStatusRepository.findAll() : goalStatusRepository.findByMenuauthid(menuauthid))
								  : ResponseEntity.ok(goalStatusRepository.findEmptyMenuauthid());
	}

	@Override
	public ResponseEntity<?> authoritygoaltree(Users user, Locale locale, String year, String team, boolean showdepartment, boolean showsection, boolean showevidence) {
		
		if (team != null && team.equalsIgnoreCase("empty")) team = null;
		List<Authoritygoals> authoritygoalslist = team != null ? authoritygoalsRepository.findByYearAndTean(year, team) : authoritygoalsRepository.findByYear(year);

		List<GoalsTree> goalstree = new ArrayList<GoalsTree>();
		
		for (Authoritygoals authoritygoals : authoritygoalslist) {

			List<Departmentgoals> departmentgoalslist = showdepartment ? departmentgoalsRepository.findByAuthgoalid(authoritygoals.getId()) : new ArrayList<Departmentgoals>();

			if (departmentgoalslist != null && departmentgoalslist.size() > 0) {
				for (Departmentgoals departmentgoals : departmentgoalslist) {
	
					List<Evidence> departmentevidencelist = showevidence ? evidenceRepository.findByGoalid(departmentgoals.getId()) : new ArrayList<Evidence>();
					departmentgoals.setEvidencelist(departmentevidencelist);
	
					List<Sectiongoals> sectiongoalslist = showsection ? sectiongoalsRepository.findByDepgoalid(departmentgoals.getId()) : new ArrayList<Sectiongoals>();
					departmentgoals.setSectiongoalslist(sectiongoalslist);
	
					if (sectiongoalslist != null && sectiongoalslist.size() > 0) 
						for (Sectiongoals section : sectiongoalslist) {
							List<Evidence> sectionevidencelist = showevidence ? evidenceRepository.findByGoalid(section.getId()) : new ArrayList<Evidence>();
							section.setEvidencelist(sectionevidencelist);
						}
				}
					
				GoalsTree goalsTree = new GoalsTree(authoritygoals, departmentgoalslist);
				goalstree.add(goalsTree);
			}
		}
		return ResponseEntity.ok(goalstree);
	}

	@Override
	public ResponseEntity<?> authoritygoalendorse(HttpServletRequest request, Users user, Locale locale, @Valid EndorseGoalsRq req) {

		Optional<Authoritygoals> authoritygoalopt = authoritygoalsRepository.findById(req.getAuthgoalid());
		
		if (authoritygoalopt.isPresent()) {
			
			List<Departmentgoals> selecteddepartmentgoals = new ArrayList<Departmentgoals>();
			Authoritygoals authoritygoals = authoritygoalopt.get();
			int totalpercentage = authoritygoals.getYearlyexpectedweight();
			int sumdeppercentage = 0;
			for (EndorseRq depgoal : req.getDepgoallist()) {

				Optional<Departmentgoals> departmentgoalopt = departmentgoalsRepository.findById(depgoal.getDepgoalid());
				if (departmentgoalopt.isPresent()) {

					Departmentgoals departmentgoals = departmentgoalopt.get();

					departmentgoals.setStatus(depgoal.isApproved() ? GoalStatus.EndorsedApproved.name() : GoalStatus.EndorsedUnapproved.name());
					if (depgoal.getReason() != null)
						departmentgoals.setEndorsementreason(depgoal.getReason());
					selecteddepartmentgoals.add(departmentgoals);
					
					if (depgoal.isApproved()) {
						int deppercentage = departmentgoals.getYearlyexpectedweight();
						sumdeppercentage += deppercentage;
					}
				}
			}
			
			if (totalpercentage < sumdeppercentage)
				return new ResponseEntity<MessageResponse>(new MessageResponse(messageService.getMessage("match_total_percentage", locale), 413), HttpStatus.OK);

			authoritygoals.setStatus(totalpercentage > sumdeppercentage ? GoalStatus.EndorsedPartially.name() : GoalStatus.EndorsementCompleted.name());
			
			for (Departmentgoals depgoal : selecteddepartmentgoals) {
				departmentgoalsRepository.save(depgoal);
			}
			
			authoritygoalsRepository.save(authoritygoals);

			List<Authoritygoals> authorities = authoritygoalsRepository.findByYearAndStatus(authoritygoals.getYear(), GoalStatus.New.name());
			if (authorities != null && authorities.size() == 0) {
				Optional<YearlyGoalsSettings> ygsopt = yearlyGoalsSettingsRepository.findByYear(authoritygoals.getYear());
				if (ygsopt.isPresent()) {
					YearlyGoalsSettings ygs = ygsopt.get();
					ygs.setStatus(YearlyGoalStatus.EndorsementCompleted.name());
					yearlyGoalsSettingsRepository.save(ygs);
				}
			}
			
			sendnotificationtoauthorityusers(request, locale, user, authoritygoals.getId());
			
			return new ResponseEntity<MessageResponse>(new MessageResponse(messageService.getMessage("success_operation", locale)), HttpStatus.OK);
		}

		return new ResponseEntity<MessageResponse>(new MessageResponse(messageService.getMessage("server_error", locale), 413), HttpStatus.OK);
	}

	private String sendnotificationtoauthorityusers(HttpServletRequest request, Locale locale, Users user, String reference) {

		try {
			String parentrole = user.getUser_role();
			String apikey = request.getHeader("apikey");
			String apisecret = request.getHeader("apisecret");
			ChildRolesUsersList childRolesUsersList = new ChildRolesUsersList(authendpointapi + childrolesuserslistapi, apikey, apisecret, settingsService.returnServerkey(), settingsService.returnServerpass(), locale.getLanguage(), parentrole);
			String childRolesUsersListRes = childRolesUsersList.callAsPost();
			if (childRolesUsersListRes == null)
				return "ERROR_RETRIEVE_USERS_LIST";
	
			JSONArray childRolesUsersListResponse = new JSONArray(childRolesUsersListRes);
			if (childRolesUsersListResponse == null || childRolesUsersListResponse.length() == 0)
				return "EMPTY_RESPONSE";

			List<Users> users = new ArrayList<Users>();
			for (int i = 0; i < childRolesUsersListResponse.length(); i++) {
			    JSONObject jsonObject = childRolesUsersListResponse.getJSONObject(i);
				Users usr = new Users(jsonObject);
				users.add(usr);
			}

			Map<String, String> payload = new HashMap<String, String>();
	        payload.put(Constants.TITLE_PARAM, "Authority goal endorsed");
	        payload.put(Constants.MESSAGE_PARAM, "The Chairman has endorsed the Authority goal reference" + reference);
	        payload.put(Constants.LINK_PARAM, reference);
			for (Users usr : users) {
				payload.put(Constants.TOUSER_PARAM, usr.getUsername());
				payload.put(Constants.TOEMAIL_PARAM, usr.getEmail());
				notificationService.sendnotification(locale, user.getUsername(), payload);
			}
			
			return "SUCCESS";
			
		} catch (Exception e) {
			e.printStackTrace();
			return "EXCEPTION";
		}
		
	}

	@Override
	public ResponseEntity<?> yearlysettings(Users user, Locale locale, String year) {
		Optional<YearlyGoalsSettings> optional = yearlyGoalsSettingsRepository.findByYear(year);
		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}
		
		YearlyGoalsSettings yearlysGoalsSettings = new YearlyGoalsSettings(year);
		yearlysGoalsSettings = yearlyGoalsSettingsRepository.save(yearlysGoalsSettings); // save default settings
		return ResponseEntity.ok(yearlysGoalsSettings);
	}

	@Override
	public ResponseEntity<?> yearlysettingsave(Users user, Locale locale, @Valid YearlySettingsRq req) {
		
		boolean authorizedtoskipendorsement = Utils.isapiauthorized(CustomAction.SkipEndorsement.name(), Menuauthid.manageauthoritygoals.name(), user.getAuthorizedapis());
		
		YearlyGoalsSettings yearlysGoalsSettings = new YearlyGoalsSettings(req, authorizedtoskipendorsement);
		
		Optional<YearlyGoalsSettings> optional = yearlyGoalsSettingsRepository.findByYear(req.getYear());
		if (optional.isPresent()) {
			yearlysGoalsSettings = optional.get();
			Long id = yearlysGoalsSettings.getId();
			String status = yearlysGoalsSettings.getStatus();
			yearlysGoalsSettings = new YearlyGoalsSettings(req, authorizedtoskipendorsement);
			yearlysGoalsSettings.setId(id);
			yearlysGoalsSettings.setStatus(status);
		}
		
		if (req.isChangegoalsstatus() && authorizedtoskipendorsement &&
				Utils.isapiauthorized(CustomAction.UpdateEndorsementStatuses.name(), Menuauthid.manageauthoritygoals.name(), user.getAuthorizedapis())) {
			String status = yearlysGoalsSettings.isSkipendorsement() ? YearlyGoalStatus.EndorsementSkipped.name() : YearlyGoalStatus.New.name();
			authoritygoalsRepository.updateGoalsStatusByYear(req.getYear(), status);
			String depstatus = yearlysGoalsSettings.isSkipendorsement() ? GoalStatus.EndorsedApproved.name() : GoalStatus.New.name();
			departmentgoalsRepository.updateGoalsStatusByYear(req.getYear(), depstatus);
			yearlysGoalsSettings.setStatus(status);
		}

		yearlysGoalsSettings = yearlyGoalsSettingsRepository.save(yearlysGoalsSettings);
		
		return ResponseEntity.ok(yearlysGoalsSettings);
	}

	@Override
	public ResponseEntity<?> yearlysettingendorsementready(HttpServletRequest request, Users user, Locale locale, String year) {

		Optional<YearlyGoalsSettings> optional = yearlyGoalsSettingsRepository.findByYear(year);
		if (!optional.isPresent())
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
			
		YearlyGoalsSettings yearlysGoalsSettings = optional.get();
		yearlysGoalsSettings.setStatus(YearlyGoalStatus.ReadyForEndorsement.name());
		yearlysGoalsSettings = yearlyGoalsSettingsRepository.save(yearlysGoalsSettings); 

		if (user.getParentrole() != null) {
	        Map<String, String> payload = new HashMap<String, String>();
	        payload.put(Constants.TITLE_PARAM, year + " Is ready for Endorsement");
	        payload.put(Constants.MESSAGE_PARAM, "The team has announced that the goals are ready for Endorsement process, you can check. ");
			notificationService.sendnotifications(request, locale, user.getUsername(), null, user.getParentrole(), payload);
		}
		return ResponseEntity.ok(yearlysGoalsSettings);
	}

}
