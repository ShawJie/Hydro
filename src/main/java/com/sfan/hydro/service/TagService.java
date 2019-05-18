package com.sfan.hydro.service;

import com.sfan.hydro.domain.model.Article;
import com.sfan.hydro.domain.model.Tag;
import com.sfan.hydro.domain.expand.PageModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    void saveTag(Tag tag);
    void updateTag(Tag tag);
    void remove(int id);
    Tag getTagById(int id);
    List<Tag> listTag(Tag tag);
    List<Tag> listTagByPage(PageModel<Tag> pageModel);
    int countTagName(Tag tag);
    List<Tag> getArticleTagRelation(int articleId);
    void saveArticleTagRelation(Article article);
}
