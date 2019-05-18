package com.sfan.hydro.service;

import com.sfan.hydro.domain.model.VisitInfo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface VisitInfoService {
    void saveVisitInfo(VisitInfo visitInfo);
    List<VisitInfo> listVisitInfo(Date startTime, Date endTime);
    int countVisit( Date startTime, Date endTime);
}
