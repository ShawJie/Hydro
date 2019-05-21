package com.sfan.hydro.controller.admin;

import com.sfan.hydro.attach.MessagesResource;
import com.sfan.hydro.domain.enumerate.FileType;
import com.sfan.hydro.domain.expand.ResponseModel;
import com.sfan.hydro.domain.expand.Theme;
import com.sfan.hydro.service.ThemeService;
import com.sfan.hydro.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/theme")
public class ThemeController {

    private final String PREFIX_PATH = "/admin/theme/";

    @Autowired
    ThemeService themeService;
    @Autowired
    MessagesResource messagesResource;

    @RequestMapping
    public String themeView(){
        return PREFIX_PATH + "themeMain";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel listTheme(){
        List<Theme> themeList = themeService.getThemeList();
        return new ResponseModel(true, null, themeList);
    }

    @RequestMapping(value = "/install", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel installTheme(MultipartFile themeArchive){
        if(themeArchive.isEmpty()){
            return new ResponseModel(false, messagesResource.getMessage("Theme.archive_required"), null);
        }

        try {
            String alertMes = themeService.checkRequireField(themeArchive.getInputStream());
            if(!alertMes.isEmpty()){
                return new ResponseModel(false, alertMes, null);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        String originFileName = themeArchive.getOriginalFilename();
        String fileName = originFileName.substring(0, originFileName.lastIndexOf('.'));

        File themeFileDir = null;
        try {
            themeFileDir = FileUtil.deCompressRecursion(themeArchive.getInputStream(), FileType.Theme, fileName);
        }catch (IOException e){
            e.printStackTrace();
        }
        Theme theme = themeService.getSingleThemeInfo(themeFileDir);
        return new ResponseModel(true, messagesResource.getMessage("Theme.install_success"), theme);
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel applyTheme(Theme theme){
        themeService.changeTheme(theme);
        return new ResponseModel(true, messagesResource.getMessage("Theme.apply_success"), null);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseModel deleteTheme(Theme theme){
        theme = themeService.getSingleThemeInfo(theme.getThemePath());
        if(theme != null){
            try {
                FileUtil.deleteFile(FileType.Theme, theme.getThemePath());
            }catch (IOException e){
                return new ResponseModel(false,  messagesResource.getMessage("Theme.delete_failed"), null);
            }
        }else{
            return new ResponseModel(false,  messagesResource.getMessage("Theme.can_not_found"), null);
        }
        return new ResponseModel(true,  messagesResource.getMessage("Theme.delete_success"), null);
    }

    @RequestMapping(value = "/saveOption", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel updateThemeOption(Theme theme){
        Theme origin = themeService.getSingleThemeInfo(theme.getThemePath());
        if(origin != null){
            themeService.updateThemeOptions(theme);
        }
        return new ResponseModel(true, messagesResource.getMessage("Theme.custom_option_save_success"), null);
    }
}
