package com.sfan.hydro.domain.expand;

import java.util.Map;

public class Theme{

    private String themePath;
    private String creator;
    private String description;
    private String themeName;
    private String rendering;
    private boolean current;
    private Map<String, String> route;
    private Map<String, String> option;

    public String getThemePath() {
        return themePath;
    }
    public void setThemePath(String themePath) {
        this.themePath = themePath;
    }
    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getThemeName() {
        return themeName;
    }
    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
    public String getRendering() {
        return rendering;
    }
    public void setRendering(String rendering) {
        this.rendering = rendering;
    }
    public boolean isCurrent() {
        return current;
    }
    public void setCurrent(boolean current) {
        this.current = current;
    }
    public Map<String, String> getRoute() {
        return route;
    }
    public void setRoute(Map<String, String> route) {
        this.route = route;
    }
    public Map<String, String> getOption() {
        return option;
    }
    public void setOption(Map<String, String> option) {
        this.option = option;
    }

    public Theme(){}

    public Theme(String themeName, String themePath) {
        this.themeName = themeName;
        this.themePath = themePath;
    }

    public String getRouteVal(String key){
        if(route == null){
            return null;
        }
        return route.get(key);
    }
}
