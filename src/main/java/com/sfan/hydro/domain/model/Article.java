package com.sfan.hydro.domain.model;

import java.util.Date;
import java.util.List;

public class Article {
	private Integer id;
	private Date createDate;
	private int authorId;
	private Integer viewCount;
	private String title;
	private String markdownPath;
	private String htmlPath;
	private Integer categoryId;
	private Boolean publicise;
	private String excerpt;
	private String categoryText;
	private List<Tag> tagList;
	private User author;
	private Integer tag;


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getAuthorId() {
		return authorId;
	}
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getViewCount() {
		return viewCount;
	}
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMarkdownPath() {
		return markdownPath;
	}
	public void setMarkdownPath(String markdownPath) {
		this.markdownPath = markdownPath;
	}
	public String getHtmlPath() {
		return htmlPath;
	}
	public void setHtmlPath(String htmlPath) {
		this.htmlPath = htmlPath;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public Boolean getPublicise() {
		return publicise;
	}
	public void setPublicise(Boolean publicise) {
		this.publicise = publicise;
	}
	public String getExcerpt() {
		return excerpt;
	}
	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public List<Tag> getTagList() {
		return tagList;
	}
	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}
	public String getCategoryText() {
		return categoryText;
	}
	public void setCategoryText(String categoryText) {
		this.categoryText = categoryText;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}

	public Integer getTag() {
		return tag;
	}
	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public Article(int id) {
		this.id = id;
	}

	public Article(){}
}
