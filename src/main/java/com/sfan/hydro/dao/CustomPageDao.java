package com.sfan.hydro.dao;

import com.sfan.hydro.domain.DTO.CustomPageDTO;
import com.sfan.hydro.domain.model.CustomPage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomPageDao {
    void addCustomPage(CustomPage page);
    void updatePage(CustomPage page);
    void deleteCustomPage(int id);
    List<CustomPage> getByRoutePath(String routePath);
    CustomPage getReleasedPageByRoute(CustomPageDTO page);
    List<CustomPage> listCustomPage();
    CustomPage getById(int id);
}
