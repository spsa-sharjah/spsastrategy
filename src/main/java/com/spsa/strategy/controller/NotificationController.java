package com.spsa.strategy.controller;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.service.MessageService;
import com.spsa.strategy.service.NotificationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
	
	@Autowired
    private NotificationService service;

	@Autowired
	MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<?> registertoken(HttpServletRequest request, 
										  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
										  @RequestBody Map<String, String> payload) {
        Users user = (Users) request.getAttribute("user");
        service.registertoken(request, user, payload);
		return ResponseEntity.ok(new MessageResponse(messageService.getMessage("success_operation", locale)));
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendnotification(HttpServletRequest request, 
											  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
											  @RequestBody Map<String, String> payload) {
        Users user = (Users) request.getAttribute("user");
        boolean sent = service.sendnotification(user.getUsername(), payload);
		return sent ? ResponseEntity.ok(new MessageResponse(messageService.getMessage("success_operation", locale))) : 
					  ResponseEntity.ok(new MessageResponse(messageService.getMessage("server_error", locale), 504));
    }
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ResponseEntity<?> list(HttpServletRequest request,
													  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
													  @RequestHeader(name = "page", required = false, defaultValue = "0") Integer page,
													  @RequestHeader(name = "size", required = false, defaultValue = "0") Integer size,
													  @RequestHeader(name = "search", required = false) String search,
													  @RequestHeader(name = "sortcolumn", required = false) String sortcolumn,
													  @RequestHeader(name = "descending", required = false, defaultValue = "false") Boolean descending,
											          @RequestHeader(name = "draw", required = false, defaultValue = "1") Integer draw,
											          @RequestHeader(name = "assender", required = false) Boolean assender) {

        Users user = (Users) request.getAttribute("user");
		return service.list(locale, page, size, search, sortcolumn, descending, draw, user, false, assender);
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public ResponseEntity<?> newnotifications(HttpServletRequest request,
								  @RequestHeader(name = "Accept-Language", required = false) Locale locale) {

        Users user = (Users) request.getAttribute("user");
		return service.newnotifications(locale, user);
	}

	@RequestMapping(value = "/details", method = RequestMethod.POST)
	public ResponseEntity<?> notificationdetails(HttpServletRequest request,
								  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
								  @RequestHeader(name = "id", required = true) Long id) {

        Users user = (Users) request.getAttribute("user");
		return service.notificationdetails(locale, user, id, false);
	}
	
	@RequestMapping(value = "/seen", method = RequestMethod.POST)
	public ResponseEntity<?> notificationseen(HttpServletRequest request,
								  @RequestHeader(name = "Accept-Language", required = false) Locale locale,
								  @RequestHeader(name = "id", required = true) Long id) {

        Users user = (Users) request.getAttribute("user");
		return service.notificationseen(locale, user, id);
	}
}
