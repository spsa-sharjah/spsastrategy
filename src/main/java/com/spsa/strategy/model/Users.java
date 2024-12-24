package com.spsa.strategy.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Users {
	private Long user_id;

	private String first_name;

	private String last_name;

	private String username;

	private String user_role;

	private String email;

	private String mobile_no;

	private String hire_date;

	private Double salary;

	private String first_name_ar;

	private String last_name_ar;

	private String date_time;
	
	private String otp;
	
	private String position;
	private String level;
	private String parentrole;
	
    private List<String> authorizedapis;
    
	public Users() {
	}

	public Users(JSONObject verifyAuthResponse) {
		this.user_id = verifyAuthResponse.getLong("user_id");
		this.first_name = verifyAuthResponse.has("first_name") && !verifyAuthResponse.get("first_name").equals(null) ? verifyAuthResponse.getString("first_name") : null;
		this.last_name = verifyAuthResponse.has("last_name") && !verifyAuthResponse.get("last_name").equals(null) ? verifyAuthResponse.getString("last_name") : null;
		this.username = verifyAuthResponse.has("username") && !verifyAuthResponse.get("username").equals(null) ? verifyAuthResponse.getString("username") : null;
		this.user_role = verifyAuthResponse.has("user_role") && !verifyAuthResponse.get("user_role").equals(null) ? verifyAuthResponse.getString("user_role") : null;
		this.email = verifyAuthResponse.has("email") && !verifyAuthResponse.get("email").equals(null) ? verifyAuthResponse.getString("email") : null;
		this.mobile_no = verifyAuthResponse.has("mobile_no") && !verifyAuthResponse.get("mobile_no").equals(null) ? verifyAuthResponse.getString("mobile_no") : null;
		this.hire_date = verifyAuthResponse.has("hire_date") && !verifyAuthResponse.get("hire_date").equals(null) ? verifyAuthResponse.getString("hire_date") : null;
		this.salary = verifyAuthResponse.has("salary") && !verifyAuthResponse.get("salary").equals(null) ? verifyAuthResponse.getDouble("salary") : 0;
		this.first_name_ar = verifyAuthResponse.has("first_name_ar") && !verifyAuthResponse.get("first_name_ar").equals(null) ? verifyAuthResponse.getString("first_name_ar") : null;
		this.last_name_ar = verifyAuthResponse.has("last_name_ar") && !verifyAuthResponse.get("last_name_ar").equals(null) ? verifyAuthResponse.getString("last_name_ar") : null;
		this.date_time = verifyAuthResponse.has("date_time") && !verifyAuthResponse.get("date_time").equals(null) ? verifyAuthResponse.getString("date_time") : null;
		
		this.position = verifyAuthResponse.has("position") && !verifyAuthResponse.get("position").equals(null) ? verifyAuthResponse.getString("position") : null;
		this.level = verifyAuthResponse.has("level") && !verifyAuthResponse.get("level").equals(null) ? verifyAuthResponse.getString("level") : null;
		this.parentrole = verifyAuthResponse.has("parentrole") && !verifyAuthResponse.get("parentrole").equals(null) ? verifyAuthResponse.getString("parentrole") : null;
		
		JSONArray authorizedapisarray = verifyAuthResponse.has("authorizedapis") && !verifyAuthResponse.get("authorizedapis").equals(null) ? verifyAuthResponse.getJSONArray("authorizedapis") : new JSONArray();
	
		this.authorizedapis = new ArrayList<String>();
		for (Object obj : authorizedapisarray) {
			this.authorizedapis.add(obj.toString());
		}
	}

	public Long getUser_id() {
		return user_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getUsername() {
		return username;
	}

	public String getUser_role() {
		return user_role;
	}

	public String getEmail() {
		return email;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public String getHire_date() {
		return hire_date;
	}

	public Double getSalary() {
		return salary;
	}

	public String getFirst_name_ar() {
		return first_name_ar;
	}

	public String getLast_name_ar() {
		return last_name_ar;
	}

	public String getDate_time() {
		return date_time;
	}

	public String getOtp() {
		return otp;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public void setHire_date(String hire_date) {
		this.hire_date = hire_date;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public void setFirst_name_ar(String first_name_ar) {
		this.first_name_ar = first_name_ar;
	}

	public void setLast_name_ar(String last_name_ar) {
		this.last_name_ar = last_name_ar;
	}

	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public List<String> getAuthorizedapis() {
		return authorizedapis;
	}

	public void setAuthorizedapis(List<String> authorizedapis) {
		this.authorizedapis = authorizedapis;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getParentrole() {
		return parentrole;
	}

	public void setParentrole(String parentrole) {
		this.parentrole = parentrole;
	}
	
	
}
