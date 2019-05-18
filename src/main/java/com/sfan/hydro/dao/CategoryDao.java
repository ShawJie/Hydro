package com.sfan.hydro.dao;

import com.sfan.hydro.domain.model.Category;
import com.sfan.hydro.domain.expand.PageModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao {
    void saveCategory(Category category);
    void updateCategory(Category category);
    void remove(int id);
    Category getCategoryById(int id);
    List<Category> listCategory(Category param);
    List<Category> listCategoryByPage(PageModel<Category> param);
    int countCategoryName(Category category);
}
