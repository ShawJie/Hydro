package com.sfan.hydro.dao;

import com.sfan.hydro.domain.model.VisitInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface VisitInfoDao {

    void saveVisitInfo(VisitInfo visitInfo);
    List<VisitInfo> listVisitInfo(Map<String, Date> timeInterval);
    int countVisit(Map<String, Date> timeInterval);
}
