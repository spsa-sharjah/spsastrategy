package com.spsa.strategy.config;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.Enumeration;
import java.util.Locale;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.model.Settings;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.service.AuthService;
import com.spsa.strategy.service.MessageService;
import com.spsa.strategy.service.SettingsService;

@Component
@Order(1)
public class RequestResponseLoggingFilter implements Filter {

	@Autowired
	AuthService authService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SettingsService settingsService;

    private static final Pattern DANGEROUS_PATTERN = Pattern.compile("(<script.*?>.*?</script>|javascript:|eval\\(|alert\\()");

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
        String requestURI = req.getRequestURI();

        res.setHeader("X-Frame-Options", "DENY");
        res.setHeader("X-Content-Type-Options", "nosniff");
        
		System.out.println("Logging Request  " + req.getMethod() + " ; " + req.getRequestURI());

		res.setCharacterEncoding("UTF-8");
		Enumeration<String> headerNames = req.getHeaderNames();
		if (headerNames != null) {

	        String lang = req.getHeader("Accept-Language"); 
	        lang = lang == null ? Constants.DEFAULT_LANG : lang;
			Locale locale = new Locale(lang == null ? Constants.DEFAULT_LANG : lang);

	        while (headerNames.hasMoreElements()) {
	            String headerName = headerNames.nextElement();
	            String headerValue = req.getHeader(headerName);
	            
	            // Check for dangerous patterns in the header values
	            if (headerValue != null && DANGEROUS_PATTERN.matcher(headerValue).find()) {

	       			MessageResponse errorResponse = new MessageResponse(messageService.getMessage("invalid_request_xss", locale), 101);

	                res.setContentType("application/json");
	                res.setStatus(HttpStatus.OK.value());
	                res.getWriter().write(Utils.convertObjectToJson(errorResponse));
	                return;  
	            }
	        }

            for (String excludedPath : Constants.EXCLUDED_PATHS) {
                if (requestURI.contains(excludedPath)) {
                    chain.doFilter(request, response);
                    return;
                }
            }
            
			String token = req.getHeader("token");
			String apisecret = req.getHeader("apisecret");
			String apikey = req.getHeader("apikey");
			String username = req.getHeader("username");
            String adminkeyh = req.getHeader("admin"); 
			
			String url = req.getRequestURL().toString();

       	    Settings settings = settingsService.returndefaultSettings();
       		String adminkey = settings.getAdminkey();

       		if (requestURI.contains(Constants.ADMIN_PATH))
	            if (adminkeyh == null || !adminkeyh.equals(adminkey)) {
	       			MessageResponse errorResponse = new MessageResponse(messageService.getMessage("not_authorized_msg", locale), 139);
	
	                res.setContentType("application/json");
	                res.setStatus(HttpStatus.OK.value());
	                res.getWriter().write(Utils.convertObjectToJson(errorResponse));
	       	        return;
	            	
	            }

			ResponseEntity<?> spsaauth = authService.callAuth(apikey, apisecret, username, token, url, lang);

			if (spsaauth != null && spsaauth.getBody() instanceof Users) { // success

				Users user = (Users) spsaauth.getBody();
		        req.setAttribute("user", user);
		        
				chain.doFilter(request, response);
				System.out.println("Logging Response : " + res.getContentType());
				return;
			}

			res.setContentType("application/json");
			res.setStatus(HttpStatus.OK.value());
			res.getWriter().write(Utils.convertObjectToJson(spsaauth));
			return;
		}

	}

}