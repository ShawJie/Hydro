package com.sfan.hydro.domain.DTO;

import com.sfan.hydro.domain.model.CustomPage;

import java.util.Date;

public class CustomPageDTO {
    private String routePath;
    private String pageName;
    private String pagePath;
    private Date releaseDate;
    private boolean publish;

    public String getRoutePath() {
        return routePath;
    }
    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }
    public String getPagePath() {
        return pagePath;
    }
    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }
    public Date getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
    public boolean isPublish() {
        return publish;
    }
    public void setPublish(boolean publish) {
        this.publish = publish;
    }
    public String getPageName() {
        return pageName;
    }
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public CustomPageDTO() {
    }

    public CustomPageDTO(String routePath, Date releaseDate, boolean publish) {
        this.routePath = routePath;
        this.releaseDate = releaseDate;
        this.publish = publish;
    }

    public CustomPageDTO(CustomPage customPage){
        this.routePath = customPage.getRoutePath();
        this.pageName = customPage.getPageName();
        this.pagePath = customPage.getPagePath();
        this.releaseDate = customPage.getReleaseDate();
        this.publish = customPage.getPublish();
    }
}
