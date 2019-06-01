package com.sfan.hydro.controller.admin;

import com.sfan.hydro.attach.MessagesResource;
import com.sfan.hydro.domain.DTO.UserDTO;
import com.sfan.hydro.domain.enumerate.FileType;
import com.sfan.hydro.domain.enumerate.MessageLocale;
import com.sfan.hydro.domain.enumerate.SystemConst;
import com.sfan.hydro.domain.expand.ResponseModel;
import com.sfan.hydro.domain.DTO.SettingDTO;
import com.sfan.hydro.domain.model.Setting;
import com.sfan.hydro.domain.model.User;
import com.sfan.hydro.service.SettingService;
import com.sfan.hydro.service.UserService;
import com.sfan.hydro.support.CustomLocaleResolver;
import com.sfan.hydro.util.FileUtil;
import com.sfan.hydro.util.MessageDigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Controller
@RequestMapping("/admin/setting")
public class SettingController {

    private final String PREFIX_PATH = "/admin/setting/";

    @Autowired
    private SettingService settingService;
    @Autowired
    private UserService userService;

    @Autowired
    private MessagesResource messagesResource;
    @Autowired
    private CustomLocaleResolver localeResolver;

    @Value("${hydro.version}")
    private String currentVersion;

    @RequestMapping
    public String settingMainView(){
        return PREFIX_PATH + "settingMain";
    }

    @RequestMapping("/about")
    public String systemAboutView(Model model) {
        model.addAttribute("currentVersion", currentVersion);

        File file = FileUtil.filepathResolver(FileType.Update.getPath() + "hydro");
        if(file.exists() && file.isDirectory() && file.list().length > 0){
            model.addAttribute("hasDecompressed", true);
        }
        return PREFIX_PATH + "systemInfo";
    }

    @RequestMapping("/defaultSetting")
    @ResponseBody
    public ResponseModel listDefaultSetting(HttpServletRequest request){
        HttpSession session = request.getSession();
        Object instance = session.getAttribute(SystemConst.User.getVal());
        UserDTO userDTO = new UserDTO(instance instanceof User ? (User)instance : null);

        List<Setting> settings = settingService.listIsSystemVariable(true);

        Map<String, Object> result = new HashMap<>();
        result.put("ownerInfo", userDTO);
        result.put("settings", toKeyValuePaire(settings));

        return new ResponseModel(true, null, result);
    }

    @RequestMapping(value = "/customSetting", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel listCustomSetting(){
        List<Setting> settings = settingService.listIsSystemVariable(false);
        return new ResponseModel(true, null, toKeyValuePaire(settings));
    }

    @RequestMapping(value = "/custom", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseModel addSetting(Setting setting){
        if(settingService.checkSetNameExists(setting)){
            return new ResponseModel(false, messagesResource.getMessage("Setting.key_exists"), null);
        }
        settingService.addSetting(setting);
        return new ResponseModel(true, messagesResource.getMessage("Setting.custom_save_success"), setting);
    }

    @RequestMapping(value = "/updateMainSetting", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel updateSetting(HttpServletRequest request, User user, SettingDTO settingDTO){
        Optional.ofNullable(settingDTO.getSettings()).filter(s -> s.size() > 0).ifPresent(s -> {
            settingService.updateSetting(s);
            for(Setting set : s){
                if(SystemConst.Language.getVal().equals(set.getItemName())){
                    localeResolver.setLocale(MessageLocale.valueOfStr(set.getItemValue()));
                }
            }
        });
        if(!user.getPassword().isEmpty()){
            user.setPassword(MessageDigestUtil.getEncryptionCharset(user.getPassword()));
        }
        userService.updateUser(user);
        HttpSession session = request.getSession();
        session.setAttribute(SystemConst.User.getVal(), user);
        return new ResponseModel(true, messagesResource.getMessage("Setting.setting_update_success"), null);
    }

    @RequestMapping(value = "/custom", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel updateSetting(Setting setting){
        settingService.updateSetting(Arrays.asList(setting));
        return new ResponseModel(true, messagesResource.getMessage("Setting.custom_update_success"), setting);
    }

    @RequestMapping(value = "/custom", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseModel deleteSetting(int settingId){
        Setting setting = settingService.getSettingById(settingId);
        if(setting != null){
            settingService.deleteSetting(settingId);
        }
        return new ResponseModel(true, messagesResource.getMessage("Setting.custom_delete_success"), setting);
    }

    private Map<String, Object> toKeyValuePaire(List<Setting> settings){
        Map<String, Object> settingMap = new HashMap<>();
        settings.forEach(s -> {
            settingMap.put(s.getItemName(), s);
        });
        return settingMap;
    }
}
