package com.sfan.hydro.service;

import com.sfan.hydro.domain.model.Setting;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface SettingService {
    void addSetting(Setting setting);
    void updateSetting(Collection<Setting> settings);
    void deleteSetting(int id);
    List<Setting> listSetting(Collection<String> keyNames);
    Setting getSingleSetting(String keyName);
    List<Setting> listIsSystemVariable(boolean isSystemVariable);
    Setting getSettingById(int id);
    boolean checkSetNameExists(Setting setting);
}
