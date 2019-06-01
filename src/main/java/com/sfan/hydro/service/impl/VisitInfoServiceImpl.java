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
        Map<String, Date> timeInterval = timeSpace(startTime, endTime);
        return visitInfoDao.listVisitInfo(timeInterval);
    }

    @Override
    public int countVisit(Date startTime, Date endTime) {
        Map<String, Date> timeInterval = timeSpace(startTime, endTime);
        return visitInfoDao.countVisit(timeInterval);
    }

    private HashMap<String, Date> timeSpace(Date startTime, Date endTime){
        HashMap<String, Date> wrapper;
        if (startTime == null){
            wrapper = null;
        }else if(startTime != null && endTime != null){
            wrapper = new HashMap<>();
            wrapper.put("startTime", startTime);
            wrapper.put("endTime", endTime);
        }else {
            wrapper = new HashMap<>();
            wrapper.put("startTime", startTime);
            wrapper.put("endTime", Calendar.getInstance().getTime());
        }
        return wrapper;
    }
}
