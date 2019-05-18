package com.sfan.hydro.domain.model;

import java.util.Date;

public class CustomPage {
    private Integer id;
    private String pageName;
    private String pagePath;
    private String routePath;
    private Boolean publish;
    private Date releaseDate;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getPageName() {
        return pageName;
    }
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
    public String getPagePath() {
        return pagePath;
    }
    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }
    public String getRoutePath() {
        return routePath;
    }
    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }
    public Boolean getPublish() {
        return publish;
    }
    public void setPublish(Boolean publish) {
        this.publish = publish;
    }
    public Date getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
