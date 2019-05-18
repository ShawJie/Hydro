package com.sfan.hydro.service;

import com.sfan.hydro.domain.model.Category;
import com.sfan.hydro.domain.expand.PageModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    void saveCategory(Category category);
    void updateCategory(Category category);
    void remove(int id);
    Category getCategoryById(int id);
    List<Category> listCategory(Category param);
    List<Category> listCategory(PageModel<Category> param);
    int countCategoryName(Category category);
}
