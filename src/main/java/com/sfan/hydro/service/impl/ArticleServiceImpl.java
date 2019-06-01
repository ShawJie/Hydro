package com.sfan.hydro.service.impl;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import com.sfan.hydro.dao.ArticleDao;
import com.sfan.hydro.domain.model.Article;
import com.sfan.hydro.domain.enumerate.FileType;
import com.sfan.hydro.domain.expand.PageModel;
import com.sfan.hydro.service.ArticleService;
import com.sfan.hydro.util.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	ArticleDao articleDao;

	Logger logger = LoggerFactory.getLogger(ArticleService.class);

	void CreateArticleFile(Article article,String markdownContent, String htmlContent) {
		try {
			FileUtil.writeInFile(markdownContent.getBytes(StandardCharsets.UTF_8), FileType.Article.getPath() + article.getTitle(), article.getTitle() + ".md");
			FileUtil.writeInFile(htmlContent.getBytes(StandardCharsets.UTF_8), FileType.Article.getPath() + article.getTitle(), article.getTitle() + ".html");
			article.setMarkdownPath(String.format("%s/%s",article.getTitle(), article.getTitle()+".md"));
			article.setHtmlPath(String.format("%s/%s",article.getTitle(), article.getTitle()+".html"));
		} catch (IOException e) {
			logger.error("Create article file failed", e);
			e.printStackTrace();
		}
	}
	
	String GetExcerpt(String html) {
		Document document = Jsoup.parse(html);
		Element element = document.selectFirst("p");
		return element != null ? element.toString() : "";
	}

	@Override
	public List<Integer> listArticle(PageModel<Article> param) {
		return  articleDao.listArticleByPage(param);
	}

	@Override
	public List<Article> listArticle(Article param) {
			return articleDao.listArticle(param);
	}

	@Override
	public Article getArticleById(int id) {
		return articleDao.getArticleById(id);
	}

	@Override
	public void saveArticle(Article article, String markdownContent, String htmlContent) {
		CreateArticleFile(article,markdownContent,htmlContent);
		article.setExcerpt(GetExcerpt(htmlContent));
		articleDao.save(article);
	}

	@Override
	public int countTitle(String title, int id) {
		Article param = new Article();
		param.setId(id);
		param.setTitle(title);
		return articleDao.countTitle(param);
	}

	@Override
	public void updateArticle(Article article, String markdownContent, String htmlContent) {
		Article oldArt = getArticleById(article.getId());
		try {
			FileUtil.deleteFile(FileType.Article, oldArt.getTitle());
		} catch (IOException e) {
			e.printStackTrace();
		}
		CreateArticleFile(article, markdownContent, htmlContent);
		article.setExcerpt(GetExcerpt(htmlContent));
		articleDao.updateArticle(article);
	}

	@Override
	public void deleteArticle(int id) {
		Article oldArt = getArticleById(id);
		try {
			FileUtil.deleteFile(FileType.Article, oldArt.getTitle());
		} catch (IOException e) {
			e.printStackTrace();
		}
		articleDao.deleteArticle(id);
	}

	@Override
	public List<Article> assemblyArticle(Collection<Integer> idList) {
		return articleDao.assemblyArticle(idList);
	}

	@Override
	public int countArticle() {
		return articleDao.countArticle();
	}

	@Override
	public void updateArticleViewCount(int articleId) {
		articleDao.updateArticleViewCount(articleId);
	}

	@Override
	public List<Article> listArticleArchivesByPage(PageModel<Article> param) {
		List<Article> data = articleDao.listArticleArchivesByPage(param);
		param.setData(data);
		return data;
	}
}
