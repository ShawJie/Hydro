package com.sfan.hydro.domain.DTO;

import com.sfan.hydro.domain.model.User;

public class UserDTO {
    private Integer id;
    private String userName;
    private String account;
    private String password;
    private String email;
    private String github;
    private String avator;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
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
    public String getAvator() {
        return avator;
    }
    public void setAvator(String avator) {
        this.avator = avator;
    }

    public UserDTO(){}

    public UserDTO(User user){
        this.id = user.getId();
        this.userName = user.getUserName();
        this.account = user.getAccount();
        this.email = user.getEmail();
        this.github = user.getGithub();
        this.avator = user.getAvator();
    }
}
