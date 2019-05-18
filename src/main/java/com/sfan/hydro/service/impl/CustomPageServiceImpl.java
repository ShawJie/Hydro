package com.sfan.hydro.service.impl;

import com.sfan.hydro.dao.CustomPageDao;
import com.sfan.hydro.domain.DTO.CustomPageDTO;
import com.sfan.hydro.domain.model.CustomPage;
import com.sfan.hydro.service.CustomPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("customPageService")
public class CustomPageServiceImpl implements CustomPageService {

    @Autowired
    private CustomPageDao customPageDao;

    @Override
    public void addCustomPage(CustomPage page) {
        customPageDao.addCustomPage(page);
    }

    @Override
    public void updatePage(CustomPage page) {
        customPageDao.updatePage(page);
    }

    @Override
    public void deleteCustomPage(int id) {
        customPageDao.deleteCustomPage(id);
    }

    @Override
    public List<CustomPage> getByRoutePath(String routePath) {
        return customPageDao.getByRoutePath(routePath);
    }

    @Override
    public CustomPageDTO getReleasedPageByRoute(String route, Date releaseDate, boolean publish) {
        CustomPage customPage = customPageDao.getReleasedPageByRoute(new CustomPageDTO(route, releaseDate, publish));
        return customPage == null ? null : new CustomPageDTO(customPage);
    }

    @Override
    public List<CustomPage> listCustomPage() {
        return customPageDao.listCustomPage();
    }

    @Override
    public CustomPage getById(int id) {
        return customPageDao.getById(id);
    }
}
