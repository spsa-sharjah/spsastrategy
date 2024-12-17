package com.spsa.strategy.builder.response;

public class MessageResponse {
	private boolean success;
	private String status;
	private String message;
	private int code;

	public MessageResponse(String message, int code) { // Error case
		this.message = message;
		this.success = false;
		this.status = "Failed";
		this.code = code;
	}

	public MessageResponse(String message) { // Success case
		this.message = message;
		this.success = true;
		this.status = "Success";
		this.code = 200;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
