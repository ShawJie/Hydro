package com.sfan.hydro.domain.model;

import java.util.Date;

public class VisitInfo {
    private Integer id;
    private String ip;
    private String requestPath;
    private Date datetime;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getRequestPath() {
        return requestPath;
    }
    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }
    public Date getDatetime() {
        return datetime;
    }
    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public VisitInfo(String ip, String requestPath, Date datetime) {
        this.ip = ip;
        this.requestPath = requestPath;
        this.datetime = datetime;
    }

    public VisitInfo() {
    }
}
