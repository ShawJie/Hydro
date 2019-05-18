package com.sfan.hydro.service.impl;

import com.sfan.hydro.dao.TagDao;
import com.sfan.hydro.domain.model.Article;
import com.sfan.hydro.domain.model.Tag;
import com.sfan.hydro.service.TagService;
import com.sfan.hydro.domain.expand.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("TagService")
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;

    @Override
    public void saveTag(Tag tag) {
        tagDao.saveTag(tag);
    }

    @Override
    public void updateTag(Tag tag) {
        tagDao.UpdateTag(tag);
    }

    @Override
    public void remove(int id) {
        tagDao.remove(id);
    }

    @Override
    public Tag getTagById(int id) {
        return tagDao.getTagById(id);
    }

    @Override
    public List<Tag> listTag(Tag tag) {
        return tagDao.listTag(tag);
    }

    @Override
    public List<Tag> listTagByPage(PageModel<Tag> pageModel) {
        List<Tag> data = tagDao.listTagByPage(pageModel);
        pageModel.setData(data);
        return data;
    }

    @Override
    public int countTagName(Tag tag) {
        return tagDao.countTagName(tag);
    }

    @Override
    public List<Tag> getArticleTagRelation(int articleId){
        return tagDao.getArticleTagRelation(articleId);
    }

    @Override
    public void saveArticleTagRelation(Article article) {
        tagDao.saveArticleTagRelation(article);
    }
}
