package com.sfan.hydro.domain.model;

import java.util.Date;

public class Media{
	private Integer id;
	private String fileName;
	private String filePath;
	private Date uploadDate;
	private Integer fileType;
	private String description;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public Integer getFileType() {
		return fileType;
	}
	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}