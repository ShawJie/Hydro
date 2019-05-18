package com.sfan.hydro.dao;

import java.util.Collection;
import java.util.List;

import com.sfan.hydro.domain.model.Article;
import com.sfan.hydro.domain.expand.PageModel;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleDao {
	List<Integer> listArticleByPage(PageModel param);
	List<Article> listArticle(Article param);
	Article getArticleById(int id);
	void save(Article article);
	int countTitle(Article title);
	void updateArticle(Article article);
	void deleteArticle(int id);
	List<Article> assemblyArticle(Collection<Integer> idList);
	int countArticle();
	void updateArticleViewCount(int articleId);
	List<Article> listArticleArchivesByPage(PageModel param);
}
