package com.spsa.strategy.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.spsa.strategy.builder.response.MessageResponse;

@ControllerAdvice
public class RestErrorHandler {


	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public Object missingServletRequestParameterException(MissingServletRequestParameterException ex) {
		return new MessageResponse("MissingServletRequestParameterException - " + ex.getMessage(), 11);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public Object httpMessageNotReadableException(HttpMessageNotReadableException ex) {
		return new MessageResponse("HttpMessageNotReadableException - " + ex.getMessage(), 12);
	}  
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public Object methodArgumentNotValidException(MethodArgumentNotValidException ex) {
		return new MessageResponse("MethodArgumentNotValidException - " + ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage(), 13);
	} 
	 
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public Object methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		return new MessageResponse("MethodArgumentTypeMismatchException - " + ex.getMessage(), 14);
	}
    
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public Object methodArgumentTypeMismatchException(NoHandlerFoundException ex) {
		return new MessageResponse("NoHandlerFoundException - " + ex.getMessage(), 15);
	}
    
}