package com.spsa.strategy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.spsa.strategy.builder.request.EmailDetailsRq;
import com.spsa.strategy.builder.response.DatatableResponse;
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.model.NotificationToken;
import com.spsa.strategy.model.Settings;
import com.spsa.strategy.model.SystemNotification;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.repository.NotificationTokenRepository;
import com.spsa.strategy.repository.SystemNotificationRepository;
import com.spsa.strategy.rest.call.TeamRoleUsersList;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private MessageService messageService;
	
	@Autowired
    private NotificationTokenRepository repository;

	@Autowired
    private SystemNotificationRepository systemNotificationRepository;
	
	@Autowired
    private SettingsService settingsService;

	@Autowired
    private EmailService emailService;

	@Value("${spring.auth.endpoint.api}") 
	private String authendpointapi;

	@Value("${spring.spsa.teamroleuserslist.api}") 
	private String teamroleuserslistapi;
	

    public void registertoken(HttpServletRequest request, Users user, Map<String, String> payload) {
    	if (!payload.containsKey(Constants.TOKEN_PARAM))
    		return;
    	String token = payload.get(Constants.TOKEN_PARAM);
        if (repository.findByUsername(user.getUsername()).stream().noneMatch(t -> t.getToken().equals(token))) {
            String id = generateuniqueid(Constants.TOKEN_ID);
            NotificationToken notificationToken = new NotificationToken(id, token, user.getUsername(), request.getRemoteAddr(), payload);
            repository.save(notificationToken);
        }
    }

    private String generateuniqueid(String tokenId) {
    	String newid = Utils.generateUniqueString(tokenId);
    	Optional<NotificationToken> notifopt = repository.findById(newid);
    	if (notifopt.isPresent())
    		generateuniqueid(tokenId);
		return newid;
	}

	public boolean sendnotification(Locale locale, String fromusername, Map<String, String> payload) {
		try {
			if (!payload.containsKey(Constants.TITLE_PARAM) ||
					!payload.containsKey(Constants.MESSAGE_PARAM) ||
					!payload.containsKey(Constants.TOUSER_PARAM))
				return false;
			
			String title = payload.get(Constants.TITLE_PARAM);
			String message = payload.get(Constants.MESSAGE_PARAM);
			String touser = payload.get(Constants.TOUSER_PARAM);
			String link = payload.containsKey(Constants.LINK_PARAM) ? payload.get(Constants.LINK_PARAM) : null;
			String toemail = payload.containsKey(Constants.TOEMAIL_PARAM) ? payload.get(Constants.TOEMAIL_PARAM) : null;
	        List<NotificationToken> notiftokens = repository.findByUsername(touser);
	        List<String> tokens = new ArrayList<String>();
	        if (notiftokens != null && notiftokens.size() > 0)
		        for (NotificationToken token : notiftokens) {
		            tokens.add(token.getToken());
		        	new Thread(() -> {
			        	try {
				            Notification notification = Notification.builder()
				                    .setTitle(title)
				                    .setBody(message)
				                    .build();
				
				            Message firebaseMessage = Message.builder()
				                    .setToken(token.getToken())
				                    .setNotification(notification)
				                    .build();
				
				            FirebaseMessaging.getInstance().send(firebaseMessage);
			    		} catch (Exception e) {
			    			e.printStackTrace();
			    			repository.deleteByToken(token.getToken()); // revoked by Firebase
			    		}
		        	}).start();
		        }
	        String tokenid = tokens.size() > 0 ? String.join(", ", tokens) : null;
	        SystemNotification sysnotif = new SystemNotification(tokenid, fromusername, touser, title, message, link);
	        systemNotificationRepository.save(sysnotif);

	        Settings settings = settingsService.returndefaultSettings(); 
	        if (settings != null &&
	        		toemail != null &&
	        		settings.isSendemailnotif()) {
	        	new Thread(() -> {
	        		try {
							String subject = messageService.getMessage("notification", locale);
							String msgBody = "You received a new SPSA notification in the Strategy service, from the '" + fromusername + "', " + message;
							EmailDetailsRq rq = new EmailDetailsRq(toemail, msgBody, subject + title);
							emailService.sendSimpleMail(rq);
		    		} catch (Exception e) {
		    			e.printStackTrace();
		    		}
	        	});
	        }
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
    }

	@Override
	public ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, Users user, boolean isadmin, Boolean assender) {
		try {
			Page<SystemNotification> pages = null;
			if (sortcolumn == null) sortcolumn = "datetime";
			Specification<SystemNotification> spec = JPASpecification.returnSystemNotificationSpecification(search, sortcolumn, descending, user, isadmin, assender);
			
			Pageable pageable = PageRequest.of(page, size);
		    pages = systemNotificationRepository.findAll(spec, pageable);

			List<SystemNotification> list = isadmin ? systemNotificationRepository.findBySenderusername(user.getUsername()) : 
													  systemNotificationRepository.findAll();
			long totalrows = list.size();
			long recordsFiltered = totalrows;
	
	        DatatableResponse<SystemNotification> datatableresponse = new DatatableResponse<SystemNotification>(draw, totalrows, recordsFiltered, pages.getContent());
		       
			return ResponseEntity.ok(datatableresponse);
	        
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> newnotifications(Locale locale, Users user) {
		try {
			List<SystemNotification> list = systemNotificationRepository.findnewusernotifications(user.getUsername());
			return ResponseEntity.ok(list);
	        
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> notificationdetails(Locale locale, Users user, Long id, boolean isadmin) {
		try {
			Optional<SystemNotification> notifopt = isadmin ? systemNotificationRepository.findById(id) :
					systemNotificationRepository.findByIdAndUsername(id, user.getUsername());
			if (notifopt.isPresent()) {
				return ResponseEntity.ok(notifopt.get());
			}
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("resource_not_found", locale), 200));
	        
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public ResponseEntity<?> notificationseen(Locale locale, Users user, Long id) {
		try {
			Optional<SystemNotification> notifopt = systemNotificationRepository.findByIdAndReceiverusername(id, user.getUsername());
			if (notifopt.isPresent()) {
				SystemNotification notif = notifopt.get();
				if (notif.isSeen())
					return ResponseEntity.ok(notif);
				notif.setSeen(true);
				notif = systemNotificationRepository.save(notif);
				return ResponseEntity.ok(notif);
			}
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("resource_not_found", locale), 200));
	        
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new MessageResponse(messageService.getMessage("exception_case", locale), 111));
		}
	}

	@Override
	public String sendnotifications(HttpServletRequest request, Locale locale, String fromusername, String team, String role, Map<String, String> payload) {

		try {
			String apikey = request.getHeader("apikey");
			String apisecret = request.getHeader("apisecret");
			TeamRoleUsersList teamRoleUsersList = new TeamRoleUsersList(authendpointapi + teamroleuserslistapi, apikey, apisecret, settingsService.returnServerkey(), settingsService.returnServerpass(), locale.getLanguage(), team, role);
			String teamRoleUsersListRes = teamRoleUsersList.callAsPost();
			if (teamRoleUsersListRes == null)
				return "ERROR_RETRIEVE_USERS_LIST";
	
			JSONArray teamRoleUsersListResonse = new JSONArray(teamRoleUsersListRes);
			if (teamRoleUsersListResonse == null || teamRoleUsersListResonse.length() == 0)
				return "EMPTY_RESPONSE";

			List<Users> users = new ArrayList<Users>();
			for (int i = 0; i < teamRoleUsersListResonse.length(); i++) {
			    JSONObject jsonObject = teamRoleUsersListResonse.getJSONObject(i);
				Users user = new Users(jsonObject);
				users.add(user);
			}
			
			for (Users usr : users) {
				payload.put(Constants.TOUSER_PARAM, usr.getUsername());
				payload.put(Constants.TOEMAIL_PARAM, usr.getEmail());
				sendnotification(locale, fromusername, payload);
			}
			
			return "SUCCESS";
			
		} catch (Exception e) {
			e.printStackTrace();
			return "EXCEPTION";
		}
		
	}
}