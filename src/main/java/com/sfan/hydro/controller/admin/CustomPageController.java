package com.sfan.hydro.controller.admin;

import com.sfan.hydro.attach.MessagesResource;
import com.sfan.hydro.domain.DTO.CustomPageDTO;
import com.sfan.hydro.domain.enumerate.FileType;
import com.sfan.hydro.domain.expand.ResponseModel;
import com.sfan.hydro.domain.model.CustomPage;
import com.sfan.hydro.service.CustomPageService;
import com.sfan.hydro.util.FileUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/page")
public class CustomPageController {

    private final String PREFIX_PATH = "/admin/page/";
    private final String PAGE_FILE_SUFFIX = ".html";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomPageService customPageService;

    @Autowired
    private MessagesResource messagesResource;

    @RequestMapping(method = RequestMethod.GET)
    public String pageMainView(){
        return PREFIX_PATH + "pageMain";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel listCustomPage(){
        return new ResponseModel(true, null, customPageService.listCustomPage());
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String pageAddView(){
        return PREFIX_PATH + "pageAdd";
    }

    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseModel addCustomPage(CustomPage customPage, String customPageContent){
        if(customPage.getId() == null){
            String randomFileName = RandomStringUtils.randomAlphabetic(8) + PAGE_FILE_SUFFIX;
            try {
                FileUtil.writeInFile(customPageContent.getBytes(), FileType.CustomPage.getPath(), randomFileName);
            }catch (IOException e){
                logger.error("Write down page file failed", e);
                return new ResponseModel(false, messagesResource.getMessage("Pages.write_file_failed"), null);
            }
            customPage.setPagePath(randomFileName);

            customPageService.addCustomPage(customPage);
        }else {
            CustomPage origin = customPageService.getById(customPage.getId());
            try {
                FileUtil.writeInFile(customPageContent.getBytes(), FileType.CustomPage.getPath(), origin.getPagePath());
            }catch (IOException e){
                logger.error("Write down page file failed", e);
                return new ResponseModel(false, messagesResource.getMessage("Pages.write_file_failed"), null);
            }
            customPageService.updatePage(customPage);
        }
        return new ResponseModel(true, messagesResource.getMessage("Pages.save_success"), customPage);
    }

    @RequestMapping(value = "/edit/{pageId}", method = RequestMethod.GET)
    public String pageEditView(Model model, @PathVariable int pageId){
        CustomPage origin = customPageService.getById(pageId);

        String pageContent = null;
        try {
            pageContent = FileUtil.getFileContext(FileType.CustomPage, origin.getPagePath());
        }catch (IOException e){
            logger.error("cannot load page content", e);
        }
        model.addAttribute("pageContent", pageContent);
        model.addAttribute("customPage", origin);
        return PREFIX_PATH + "pageAdd";
    }

    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @ResponseBody
    public boolean routeExists(String routePath, Integer id){
        return customPageService.getByRoutePath(routePath).stream().filter(p -> p.getId() != id).count() > 0;
    }

    @RequestMapping(value="/review", method = RequestMethod.GET)
    public String reviewPageView(){
        return PREFIX_PATH + "pageWrapper";
    }

    @RequestMapping(value = "/review", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel reviewPage(CustomPage customPage, String customPageContent){
        Map<String, Object> requireFields = new HashMap<>();
        requireFields.put("pageName", customPage.getPageName());
        requireFields.put("pageContent", customPageContent);
        return new ResponseModel(true, null, requireFields);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseModel removeCustomPage(int pageId){
        CustomPage origin = customPageService.getById(pageId);
        if(origin != null){
            try {
                FileUtil.deleteFile(FileType.CustomPage, origin.getPagePath());
            } catch (IOException e){
                logger.error("Delete custom page file failed", e);
            }
            customPageService.deleteCustomPage(origin.getId());
        }
        return new ResponseModel(true, messagesResource.getMessage("Pages.delete_success"), origin);
    }
}
