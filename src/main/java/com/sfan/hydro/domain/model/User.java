package com.sfan.hydro.domain.model;

import java.util.Date;

public class User {
	
	private Integer id; 
	private String userName;
	private String account;
	private String password;
	private String email;
	private String github;
	private String avator;
	private boolean activated = true;
	private String groupSet;
	private Date lastLoginDate;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAvator() {
		return avator;
	}
	public void setAvator(String avator) {
		this.avator = avator;
	}
	public String getGroupSet() {
		return groupSet;
	}
	public void setGroupSet(String groupSet) {
		this.groupSet = groupSet;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public boolean isActivated() {
		return activated;
	}
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGithub() {
		return github;
	}
	public void setGithub(String github) {
		this.github = github;
	}
}
