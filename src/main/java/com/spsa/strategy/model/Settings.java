package com.spsa.strategy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "settings", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
public class Settings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String adminkey;
	
	private boolean isdefault;

	private boolean sendemailnotif;
	
	public Settings() {
	}
	
	public Settings(@NotBlank String adminkey, boolean isdefault) {
		super();
		this.adminkey = adminkey;
		this.isdefault = isdefault;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAdminkey() {
		return adminkey;
	}

	public void setAdminkey(String adminkey) {
		this.adminkey = adminkey;
	}

	public boolean isIsdefault() {
		return isdefault;
	}

	public void setIsdefault(boolean isdefault) {
		this.isdefault = isdefault;
	}

	public boolean isSendemailnotif() {
		return sendemailnotif;
	}

	public void setSendemailnotif(boolean sendemailnotif) {
		this.sendemailnotif = sendemailnotif;
	}
}
