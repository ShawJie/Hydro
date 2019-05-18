package com.sfan.hydro.service.impl;

import com.sfan.hydro.dao.CategoryDao;
import com.sfan.hydro.domain.model.Category;
import com.sfan.hydro.service.CategoryService;
import com.sfan.hydro.domain.expand.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("CategoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public void saveCategory(Category category) {
        categoryDao.saveCategory(category);
    }

    @Override
    public void updateCategory(Category category) {
        categoryDao.updateCategory(category);
    }

    @Override
    public void remove(int id) {
        categoryDao.remove(id);
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryDao.getCategoryById(id);
    }

    @Override
    public List<Category> listCategory(Category param) {
        return categoryDao.listCategory(param);
    }

    @Override
    public List<Category> listCategory(PageModel<Category> param) {
        List<Category> data = categoryDao.listCategoryByPage(param);
        param.setData(data);
        return data;
    }

    @Override
    public int countCategoryName(Category category) {
        return categoryDao.countCategoryName(category);
    }
}
