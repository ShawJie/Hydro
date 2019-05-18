package com.sfan.hydro.domain.DTO;

import com.sfan.hydro.domain.model.Setting;

import java.util.List;

public class SettingDTO {
    private List<Setting> settings;

    public List<Setting> getSettings() {
        return settings;
    }
    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }
}
