package com.sfan.hydro.controller.admin;

import com.sfan.hydro.domain.enumerate.FileType;
import com.sfan.hydro.attach.MessagesResource;
import com.sfan.hydro.domain.enumerate.SystemConst;
import com.sfan.hydro.domain.expand.ResponseModel;
import com.sfan.hydro.domain.model.Article;
import com.sfan.hydro.domain.model.Category;
import com.sfan.hydro.domain.model.Tag;
import com.sfan.hydro.domain.model.User;
import com.sfan.hydro.service.ArticleService;
import com.sfan.hydro.service.CategoryService;
import com.sfan.hydro.service.TagService;
import com.sfan.hydro.util.FileUtil;
import com.sfan.hydro.domain.expand.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/admin/article")
public class ArticleController {

    private final String PREFIX_PATH = "/admin/article/";

    @Autowired
    ArticleService articleService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    TagService tagService;
    @Autowired
    MessagesResource messagesResource;

    @RequestMapping(method = RequestMethod.GET)
    public String articleListView(Model model){
        model.addAttribute("tags", tagService.listTag(new Tag()));
        model.addAttribute("categories", categoryService.listCategory(new Category()));
        return PREFIX_PATH + "articleList";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String articleAddView(Model model){
        model.addAttribute("tags", tagService.listTag(new Tag()));
        model.addAttribute("categories", categoryService.listCategory(new Category()));
        return PREFIX_PATH + "articleAdd";
    }

    @RequestMapping(value = "/edit/{articleId}", method = RequestMethod.GET)
    public String articleEdit(Model model, @PathVariable Integer articleId){
        Article article = articleService.getArticleById(articleId);
        if(article != null) {
            try {
                model.addAttribute("articleContext" , FileUtil.getFileContext(FileType.Article, article.getMarkdownPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            article.setTagList(tagService.getArticleTagRelation(articleId));
            model.addAttribute("article", article);
        }

        model.addAttribute("tags", tagService.listTag(new Tag()));
        model.addAttribute("categories", categoryService.listCategory(new Category()));

        return PREFIX_PATH + "articleAdd";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel articleList(Article article, int pageIndex, int pageSize){
        PageModel<Article> pageModel = new PageModel<Article>(pageIndex, pageSize, article);

        List<Integer> articleIds = articleService.listArticle(pageModel);

        // 二次查询获取详细数据
        Optional.of(articleIds).filter(ids -> ids.size() > 0).ifPresentOrElse(ids -> pageModel.setData(articleService.assemblyArticle(articleIds)),
                () -> pageModel.setData(new ArrayList()));
        return new ResponseModel(true, null, pageModel);
    }

    @RequestMapping(value="/publish", method=RequestMethod.PUT)
    @ResponseBody
    public ResponseModel articleSave(HttpServletRequest request, String markdownContent, String htmlContent, Article article) {
        User user = (User)request.getSession().getAttribute(SystemConst.User.getVal());
        Optional.ofNullable(article.getId()).ifPresentOrElse(id -> {
            article.setAuthorId(user.getId());
            articleService.saveArticle(article, markdownContent, htmlContent);
        }, () -> articleService.updateArticle(article, markdownContent, htmlContent));
        tagService.saveArticleTagRelation(article);

        return new ResponseModel(true, messagesResource.getMessage("Article.add.publish_success"), article);
    }

    @RequestMapping(value = "/review/{id}", method = RequestMethod.GET)
    public String articleReviewView(Model model, @PathVariable int id){
        Article article = articleService.getArticleById(id);
        if(article != null) {
            String htmlContent = null;
            try {
                htmlContent = FileUtil.getFileContext(FileType.Article, article.getHtmlPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            article = articleService.assemblyArticle(Arrays.asList(id)).stream().findFirst().get();
            model.addAttribute("articleContent", htmlContent);
        }


        model.addAttribute("article", article);
        return PREFIX_PATH + "articleReview";
    }

    @RequestMapping(value="/review", method=RequestMethod.POST)
    @ResponseBody
    public ResponseModel articleReviewView(HttpServletRequest request, String htmlContent, Article article) {
        User user = (User)request.getSession().getAttribute(SystemConst.User.getVal());

        article.setAuthorId(user.getId());
        article.setAuthor(user);
        article.setCreateDate(Calendar.getInstance().getTime());

        Map<String, Object> articleInfo = new HashMap<>();
        articleInfo.put("article", article);
        articleInfo.put("articleContent", htmlContent);
        return new ResponseModel(true, "Data initial success", articleInfo);
    }

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public String articleCategoryView(){
        return PREFIX_PATH + "categoryList";
    }

    @RequestMapping(value = "/category/save", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseModel saveCategory(Category category){
        if(categoryService.countCategoryName(category) > 0){
            return new ResponseModel(false, messagesResource.getMessage("Article.categories.exists"), null);
        }

        boolean newItem = (category.getId() == null);
        if(newItem){
            categoryService.saveCategory(category);
        }else{
            categoryService.updateCategory(category);
        }
        return new ResponseModel(true,
                messagesResource.getMessage(newItem ? "Article.categories.save_success" : "Article.categories.edit_success"),
                category);
    }

    @RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel getCategoryById(@PathVariable int categoryId){
        Category category = categoryService.getCategoryById(categoryId);
        if(category == null){
            return new ResponseModel(false, "target not found", null);
        }else{
            return new ResponseModel(true, null, category);
        }
    }

    @RequestMapping(value = "/category/{categoryId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseModel deleteCategoryById(@PathVariable int categoryId){
        categoryService.remove(categoryId);
        return new ResponseModel(true, messagesResource.getMessage("Article.categories.delete_success"), null);
    }

    @RequestMapping(value = "/category/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel listCategory(int pageIndex, int pageSize, Category category){
        PageModel<Category> pageModel = new PageModel<>(pageIndex, pageSize, category);
        categoryService.listCategory(pageModel);
        return new ResponseModel(true, null, pageModel);
    }

    @RequestMapping(value = "/tag", method = RequestMethod.GET)
    public String articleTag(){
        return PREFIX_PATH + "tagList";
    }

    @RequestMapping(value = "/tag/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel listTag(int pageIndex, int pageSize, Tag tag){
        PageModel<Tag> pageModel = new PageModel<>(pageIndex, pageSize, tag);
        tagService.listTagByPage(pageModel);
        return new ResponseModel(true, null, pageModel);
    }

    @RequestMapping(value = "/tag/save", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseModel saveTag(Tag tag){
        if(tagService.countTagName(tag) > 0){
            return new ResponseModel(false, messagesResource.getMessage("Article.tag.exists"), null);
        }

        boolean newItem = (tag.getId() == null);
        if(newItem){
            tagService.saveTag(tag);
        }else{
            tagService.updateTag(tag);
        }
        return new ResponseModel(true,
                messagesResource.getMessage(newItem ? "Article.tag.save_success" : "Article.tag.edit_success"), tag);
    }

    @RequestMapping(value = "/tag/{tagId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel getTagById(@PathVariable int tagId){
        Tag tag = tagService.getTagById(tagId);
        if(tag == null){
            return new ResponseModel(false, "target not found", null);
        }else{
            return new ResponseModel(true, null, tag);
        }
    }

    @RequestMapping(value = "/tag/{tagId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseModel deleteTagById(@PathVariable int tagId){
        tagService.remove(tagId);
        return new ResponseModel(true, messagesResource.getMessage("Article.tag.delete_success"), null);
    }
}
