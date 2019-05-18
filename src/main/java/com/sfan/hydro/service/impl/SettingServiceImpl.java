package com.sfan.hydro.service.impl;

import com.sfan.hydro.dao.SettingDao;
import com.sfan.hydro.domain.model.Setting;
import com.sfan.hydro.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service("settingService")
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingDao settingDao;

    @Override
    public void addSetting(Setting setting) {
        settingDao.addSetting(setting);
    }

    @Override
    public void updateSetting(Collection<Setting> settings) {
        settingDao.updateSetting(settings);
    }

    @Override
    public void deleteSetting(int id) {
        settingDao.deleteSetting(id);
    }

    @Override
    public List<Setting> listSetting(Collection<String> keyNames) {
        return settingDao.listSetting(keyNames);
    }

    @Override
    public Setting getSingleSetting(String keyName) {
        List<Setting> data = listSetting(Arrays.asList(new String[]{keyName}));
        return data.stream().findFirst().get();
    }

    @Override
    public List<Setting> listIsSystemVariable(boolean isSystemVariable) {
        return settingDao.listIsSystemVariable(isSystemVariable);
    }

    @Override
    public Setting getSettingById(int id) {
        return settingDao.getSettingById(id);
    }

    @Override
    public boolean checkSetNameExists(Setting setting) {
        int count = settingDao.checkSetNameExists(setting);
        return count > 0;
    }
}
