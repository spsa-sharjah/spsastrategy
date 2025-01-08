package com.spsa.strategy.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spsa.strategy.config.Constants;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "evidence_files", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
public class FileEvidence {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long evidenceid;
	
	private String filename;
	
	private String username;
	
	private long filesize;
	
	private String filepath;
	
	private String filetype;

	private String fileimage;
	
	private String width;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private Date date_time;
	
	public FileEvidence() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEvidenceid() {
		return evidenceid;
	}

	public void setEvidenceid(Long evidenceid) {
		this.evidenceid = evidenceid;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getFileimage() {
		return fileimage;
	}

	public void setFileimage(String fileimage) {
		this.fileimage = fileimage;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
}
