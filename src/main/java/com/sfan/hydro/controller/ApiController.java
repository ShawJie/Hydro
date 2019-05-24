package com.sfan.hydro.controller;

import com.sfan.hydro.domain.DTO.ArticleDTO;
import com.sfan.hydro.domain.DTO.UserDTO;
import com.sfan.hydro.domain.enumerate.FileType;
import com.sfan.hydro.domain.enumerate.UserGroup;
import com.sfan.hydro.domain.expand.PageModel;
import com.sfan.hydro.domain.expand.ResponseModel;
import com.sfan.hydro.domain.model.*;
import com.sfan.hydro.service.*;
import com.sfan.hydro.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@RequestMapping("/api")
@RestController
public class ApiController {

    private UserService userService;
    private ArticleService articleService;
    private CategoryService categoryService;
    private TagService tagService;
    private SettingService settingService;

    @Lazy
    @Autowired
    public ApiController(UserService userService, ArticleService articleService,
                         CategoryService categoryService, TagService tagService, SettingService settingService) {
        this.userService = userService;
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.settingService = settingService;
    }

    private final String OK_MSG = "OK";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<Integer, Set<String>> articleViewRecorder;

    @RequestMapping(value = "/owner_info", method = RequestMethod.GET)
    public ResponseModel getOwnerInfo(){
        ResponseModel responseModel = new ResponseModel(true, OK_MSG, null);

        Optional<User> userOptional = Optional.ofNullable(userService.getUserByGroup(UserGroup.Admin));
        responseModel.setData(new UserDTO(userOptional.orElse(new User())));
        return responseModel;
    }

    @RequestMapping(value = "/article_list", method = RequestMethod.GET)
    public ResponseModel listArticle(int pageIndex, int pageSize,
                                             @RequestParam(required = false) String title, @RequestParam(required = false) Integer tid,
                                             @RequestParam(required = false) Integer cid, @RequestParam(required = false) String cdate){
        Article param = new Article();
        param.setTitle(title);
        param.setTag(tid);
        param.setCategoryId(cid);
        if (cdate != null && Pattern.matches("\\d{4}-\\d{2}-\\d{2}", cdate)){
            Calendar calendar = Calendar.getInstance();
            String[] split = cdate.split("-");
            calendar.set(Integer.valueOf(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]));
            param.setCreateDate(calendar.getTime());
        }

        PageModel<Article> pageModel = new PageModel<>(pageIndex, pageSize, param);
        PageModel<ArticleDTO> packageArticle = new PageModel<>(pageIndex, pageSize, null);

        List<Integer> articleIds = articleService.listArticle(pageModel);
        // 二次查询获取详细数据
        Optional.of(articleIds).filter(ids -> ids.size() > 0).ifPresentOrElse(ids -> {
            List<ArticleDTO> articles = new ArrayList<>();
            articleService.assemblyArticle(ids).forEach(a -> articles.add(new ArticleDTO(a)));
            packageArticle.setData(articles);
            packageArticle.setCount(pageModel.getCount());
        }, () -> packageArticle.setData(new ArrayList()));

        return new ResponseModel(true, OK_MSG, packageArticle);
    }

    @RequestMapping(value = "/category_list", method = RequestMethod.GET)
    public ResponseModel listCategory(){
        List<Category> categories = categoryService.listCategory(new Category());
        return new ResponseModel(true, OK_MSG, categories);
    }

    @RequestMapping(value = "/tag_list", method = RequestMethod.GET)
    public ResponseModel listTag(){
        List<Tag> tags = tagService.listTag(new Tag());
        return new ResponseModel(true, OK_MSG, tags);
    }

    @RequestMapping(value = "/setting_info", method = RequestMethod.GET)
    public ResponseModel listSettingFromKey(@RequestParam(required = true) String... keys){
        ResponseModel responseModel = new ResponseModel();
        Optional.ofNullable(keys).filter(ks -> ks.length > 0).ifPresentOrElse(ks -> {
            responseModel.setSuccess(true);
            responseModel.setMsg(OK_MSG);
            responseModel.setData(settingService.listSetting(Arrays.asList(keys)));
        }, () -> {
            responseModel.setSuccess(false);
            responseModel.setMsg("keys cannot be empty");
        });

        return responseModel;
    }

    @RequestMapping(value = "/article_archives", method = RequestMethod.GET)
    public ResponseModel getArticleArchives(int pageIndex, int pageSize){
        PageModel pageModel = new PageModel<>(pageIndex, pageSize, null);
        pageModel.setOrderRule("DESC");
        articleService.listArticleArchivesByPage(pageModel);

        Optional.of(pageModel.getData()).filter(articles -> articles.size() > 0).ifPresent(articles -> {
            List<ArticleDTO> articleDTOS = new ArrayList<>();
            articles.forEach(a -> articleDTOS.add(new ArticleDTO((Article)a)));
            pageModel.setData(articleDTOS);
        });

        return new ResponseModel(true, OK_MSG, pageModel);
    }

    @RequestMapping(value = "/article/{articleId}", method = RequestMethod.GET)
    public ResponseModel getArticleContent(HttpServletRequest request, @PathVariable int articleId){
        ResponseModel responseModel = new ResponseModel();

        Optional.ofNullable(articleService.getArticleById(articleId)).ifPresentOrElse(article -> {
            List<Article> fullData = articleService.assemblyArticle(Arrays.asList(article.getId()));
            ArticleDTO articleDTO = new ArticleDTO(fullData.stream().findFirst().get());
            responseModel.setData(articleDTO);
            try {
                String articleContent = FileUtil.getFileContext(FileType.Article, article.getHtmlPath());
                articleDTO.setArticleContent(articleContent);
            }catch (IOException e){
                logger.error("read article content failed", e);
            }
            responseModel.setMsg(OK_MSG);
            responseModel.setSuccess(true);

            readerCount(articleId, request.getRemoteAddr());
        }, () -> {
            responseModel.setMsg(String.format("cannot find the article with id:%d", articleId));
            responseModel.setSuccess(false);
        });

        return responseModel;
    }

    private void readerCount(int articleId, String ipAddress){
        Optional.ofNullable(articleViewRecorder.get(articleId)).ifPresentOrElse(ips -> {
            if (!ips.contains(ipAddress)){
                articleService.updateArticleViewCount(articleId);
            }
        }, () -> {
            Set<String> ips = new HashSet<>();
            ips.add(ipAddress);

            articleService.updateArticleViewCount(articleId);

            articleViewRecorder.put(articleId, ips);
        });
    }

    @SuppressWarnings("all")
    @PostConstruct
    private void initial(){
        articleViewRecorder = new HashMap<>();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                articleViewRecorder.clear();
            }
        };

        Calendar dayEnd = Calendar.getInstance();
        dayEnd.set(Calendar.DATE, dayEnd.get(Calendar.DATE) + 1);
        dayEnd.set(Calendar.HOUR_OF_DAY, 0);
        dayEnd.set(Calendar.MINUTE, 0);
        dayEnd.set(Calendar.SECOND, 0);

        timer.schedule(timerTask, dayEnd.getTime(), (1000 * 60 * 60 * 24));
    }
}
