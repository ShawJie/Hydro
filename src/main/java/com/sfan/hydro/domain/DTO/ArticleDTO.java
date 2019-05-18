package com.sfan.hydro.domain.DTO;

import com.sfan.hydro.domain.model.Article;
import com.sfan.hydro.domain.model.Category;
import com.sfan.hydro.domain.model.Tag;
import com.sfan.hydro.domain.model.User;

import java.util.Date;
import java.util.List;

public class ArticleDTO {
    private Integer id;
    private Date createDate;
    private String title;
    private Integer viewCount;
    private String articleContent;
    private String excerpt;
    private Category category;
    private List<Tag> tagList;
    private UserDTO author;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getViewCount() {
        return viewCount;
    }
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
    public String getArticleContent() {
        return articleContent;
    }
    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }
    public String getExcerpt() {
        return excerpt;
    }
    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public List<Tag> getTagList() {
        return tagList;
    }
    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }
    public UserDTO getAuthor() {
        return author;
    }
    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public ArticleDTO() {
    }

    public ArticleDTO(Article article) {
        this.id = article.getId();
        this.createDate = article.getCreateDate();
        this.title = article.getTitle();
        this.viewCount = article.getViewCount();
        this.excerpt = article.getExcerpt();
        Category category = new Category();
        category.setId(article.getCategoryId());
        category.setCategoryName(article.getCategoryText());
        this.category = category;
        this.tagList = article.getTagList();
        this.author = new UserDTO(article.getAuthor());
    }
}
