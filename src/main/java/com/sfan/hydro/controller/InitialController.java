package com.sfan.hydro.controller;

import com.sfan.hydro.domain.enumerate.UserGroup;
import com.sfan.hydro.domain.expand.ResponseModel;
import com.sfan.hydro.domain.model.User;
import com.sfan.hydro.service.UserService;
import com.sfan.hydro.util.MessageDigestUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;

@RequestMapping("/initial")
@Controller
public class InitialController {

    @Autowired
    private UserService userService;

    private final String DEFAULT_AVATOR = "/media/head.jpg";

    @RequestMapping
    public String initialView(){
        return "initial/initialProcess";
    }

    @RequestMapping("/submit")
    @ResponseBody
    public ResponseModel initialBlog(User user){
        user.setPassword(MessageDigestUtil.getEncryptionCharset(user.getPassword()));
        user.setLastLoginDate(Calendar.getInstance().getTime());
        user.setActivated(true);
        user.setGroupSet(UserGroup.Admin.GetVal());
        user.setAvator(DEFAULT_AVATOR);

        userService.saveUser(user);

        Resource resource = new ClassPathResource("configuration/webSiteConfig.properties");
        Assert.notNull(resource, "cannot find webSiteConfig.properties");

        Properties prop = new Properties();
        try (InputStream inputStream = resource.getInputStream()){
            prop.load(inputStream);
        } catch (IOException e){

        }

        try (FileOutputStream fop = new FileOutputStream(resource.getFile())){
            prop.setProperty("FirstSetUp.account", Boolean.FALSE.toString());
            prop.store(fop, null);
        } catch (IOException e){

        }

        return new ResponseModel(true, null, null);
    }
}
