package com.sfan.hydro.dao;

import com.sfan.hydro.domain.model.Article;
import com.sfan.hydro.domain.model.Tag;
import com.sfan.hydro.domain.expand.PageModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagDao {
    void saveTag(Tag tag);
    void UpdateTag(Tag tag);
    void remove(int id);
    Tag getTagById(int id);
    List<Tag> listTag(Tag param);
    List<Tag> listTagByPage(PageModel<Tag> pageModel);
    int countTagName(Tag tag);
    List<Tag> getArticleTagRelation(int articleId);
    void saveArticleTagRelation(Article article);
}
