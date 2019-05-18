package com.sfan.hydro.service;

import com.sfan.hydro.domain.model.Article;
import com.sfan.hydro.domain.expand.PageModel;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface ArticleService {

	List<Integer> listArticle(PageModel<Article> param);
	List<Article> listArticle(Article param);
	Article getArticleById(int id);
	void saveArticle(Article article, String markdownContent, String htmlContent);
	int countTitle(String title, int id);
	void updateArticle(Article article, String markdownContent, String htmlContent);
	void deleteArticle(int id);
	List<Article> assemblyArticle(Collection<Integer> idList);
	int countArticle();
	void updateArticleViewCount(int articleId);
	List<Article> listArticleArchivesByPage(PageModel<Article> param);
}
