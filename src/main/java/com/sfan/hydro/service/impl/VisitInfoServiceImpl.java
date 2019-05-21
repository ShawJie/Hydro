package com.sfan.hydro.service.impl;

import com.sfan.hydro.dao.VisitInfoDao;
import com.sfan.hydro.domain.model.VisitInfo;
import com.sfan.hydro.service.VisitInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("visitInfoService")
public class VisitInfoServiceImpl implements VisitInfoService {

    @Autowired
    private VisitInfoDao visitInfoDao;

    @Override
    public void saveVisitInfo(VisitInfo visitInfo) {
        visitInfoDao.saveVisitInfo(visitInfo);
    }

    @Override
    public List<VisitInfo> listVisitInfo(Date startTime, Date endTime) {
        Map<String, Date> timeInterval = new HashMap<>();
        timeInterval.put("startTime", startTime);
        timeInterval.put("endTime", endTime);
        return visitInfoDao.listVisitInfo(timeInterval);
    }

    @Override
    public int countVisit(Date startTime, Date endTime) {
        Map<String, Date> timeInterval;
        if (startTime == null){
            timeInterval = null;
        }else if(startTime != null && endTime != null){
            timeInterval = new HashMap<>();
            timeInterval.put("startTime", startTime);
            timeInterval.put("endTime", endTime);
        }else {
            timeInterval = new HashMap<>();
            timeInterval.put("startTime", startTime);
            timeInterval.put("endTime", Calendar.getInstance().getTime());
        }
        return visitInfoDao.countVisit(timeInterval);
    }
}
