package com.sfan.hydro.controller.admin;

import com.sfan.hydro.domain.enumerate.SystemConst;
import com.sfan.hydro.domain.expand.ResponseModel;
import com.sfan.hydro.domain.model.User;
import com.sfan.hydro.domain.model.VisitInfo;
import com.sfan.hydro.service.*;
import com.sfan.hydro.attach.MessagesResource;
import com.sfan.hydro.util.MessageDigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.JDBCType;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessagesResource messagesResource;

    @Autowired
    private ArticleService articleService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private VisitInfoService visitInfoService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(){
        return "admin/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel loginLogic(HttpServletRequest request, User userInfo){
        ResponseModel responseModel = new ResponseModel();

        userInfo.setPassword(MessageDigestUtil.getEncryptionCharset(userInfo.getPassword()));
        Optional<User> targetUser = userService.listUser(userInfo).stream().findFirst();

        targetUser.ifPresentOrElse(u -> {
            HttpSession session = request.getSession();
            session.setAttribute(SystemConst.User.getVal(), u);
            responseModel.setSuccess(true);
            responseModel.setMsg(messagesResource.getMessage("Login.login_success"));
        }, () -> {
            responseModel.setSuccess(false);
            responseModel.setMsg(messagesResource.getMessage("Login.login_failed"));
        });
        return responseModel;
    }

    @RequestMapping()
    public String mainPage(Model model){
        Map<String, Object> modelData = new HashMap<>();

        modelData.put("articleAmount", articleService.countArticle());
        modelData.put("mediaAmount", mediaService.countMedia(null));
        modelData.put("themeAmount", themeService.getThemeList().size());

        modelData.put("themeName", themeService.getCurrentThemeConfig().getThemeName());

        modelData.put("totalVisitCount", visitInfoService.countVisit(null, null));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        List<VisitInfo> visitInfos = visitInfoService.listVisitInfo(calendar.getTime(), null);

        modelData.put("todayVisitList", visitInfos);
        modelData.put("todayVisitCount", visitInfos.size());

        model.addAllAttributes(modelData);
        return "admin/dashboard";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logOut(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute(SystemConst.User.getVal());
        return new ModelAndView("redirect:/");
    }
}
