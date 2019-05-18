package com.sfan.hydro.dao;

import com.sfan.hydro.domain.model.Setting;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SettingDao {
    void addSetting(Setting setting);
    void updateSetting(Collection<Setting> settings);
    void deleteSetting(int id);
    List<Setting> listSetting(Collection<String> keyNames);
    List<Setting> listIsSystemVariable(boolean isSystemVariable);
    Setting getSettingById(int id);
    int checkSetNameExists(Setting setting);
}
