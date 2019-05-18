package com.sfan.hydro.service;

import com.sfan.hydro.domain.DTO.CustomPageDTO;
import com.sfan.hydro.domain.model.CustomPage;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface CustomPageService {
    void addCustomPage(CustomPage page);
    void updatePage(CustomPage page);
    void deleteCustomPage(int id);
    List<CustomPage> getByRoutePath(String routePath);
    CustomPageDTO getReleasedPageByRoute(String route, Date releaseDate, boolean publish);
    List<CustomPage> listCustomPage();
    CustomPage getById(int id);
}
