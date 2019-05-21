package com.sfan.hydro.controller;

import com.sfan.hydro.attach.HydroNotFoundException;
import com.sfan.hydro.domain.DTO.CustomPageDTO;
import com.sfan.hydro.domain.enumerate.FileType;
import com.sfan.hydro.domain.enumerate.SystemConst;
import com.sfan.hydro.domain.expand.Theme;
import com.sfan.hydro.service.CustomPageService;
import com.sfan.hydro.service.ThemeService;
import com.sfan.hydro.service.UserService;
import com.sfan.hydro.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class IndexController {

    private final String CUSTOM_WAPPER_PAGE = "customPage/pageWrapper";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;
    @Autowired
    ThemeService themeService;
    @Autowired
    CustomPageService customPageService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(){
        Theme currentTheme = themeService.getCurrentThemeConfig();
        Map<String, String> routeMap = currentTheme.getRoute();
        return routeMap.get("index");
    }

    @RequestMapping(value = "/post/{articleId}", method = RequestMethod.GET)
    public String articlePage(@PathVariable int articleId){
        Theme currentTheme = themeService.getCurrentThemeConfig();
        return currentTheme.getRoute().get("post.detail");
    }

    @RequestMapping(value = "/{route}", method = RequestMethod.GET)
    public String customRoute(Model model, @PathVariable String route){
        Optional<CustomPageDTO> optional = Optional.ofNullable(customPageService.getReleasedPageByRoute(route, Calendar.getInstance().getTime(), true));
        if(optional.isPresent()){
            CustomPageDTO customPage = optional.get();
            String pageContent = null;
            try {
                pageContent = FileUtil.getFileContext(FileType.CustomPage, customPage.getPagePath());
            }catch (IOException e){
                logger.error("Get Custom Page Content failed", e);
            }
            model.addAttribute("pageName", customPage.getPageName());
            model.addAttribute("pageContent", pageContent);
            model.addAttribute("routeState", SystemConst.Straight);
            return CUSTOM_WAPPER_PAGE;
        }else{
            Theme currentTheme = themeService.getCurrentThemeConfig();
            Map<String, String> routeMap = currentTheme.getRoute();
            String target;
            if((target = routeMap.get(route)) != null){
                return target;
            }
        }
        throw new HydroNotFoundException();
    }
}
