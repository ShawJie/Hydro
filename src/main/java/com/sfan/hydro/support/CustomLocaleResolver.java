package com.sfan.hydro.support;

import com.sfan.hydro.domain.enumerate.MessageLocale;
import com.sfan.hydro.domain.enumerate.SystemConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

@Component
public class CustomLocaleResolver implements LocaleResolver {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Locale locale;
    private Properties prop;
    private Resource resource;

    @PostConstruct
    public void loadProp(){
        resource = new ClassPathResource("configuration/webSiteConfig.properties");
        prop = new Properties();
        try {
            prop.load(resource.getInputStream());
        }catch (IOException e){
            logger.error("load system properties failed", e);
        }
        String language = prop.getProperty(SystemConst.Language.getVal());
        setLocale(MessageLocale.valueOfStr(language));
    }

    public void setLocale(MessageLocale locale){
        if(locale == null){
            this.locale = Locale.getDefault();
        }else{
            switch (locale.getVal()){
                case "zh-CN":
                    this.locale = Locale.SIMPLIFIED_CHINESE;
                    break;
                case "en-US":
                    this.locale = Locale.US;
                    break;
                default:
                    this.locale = Locale.getDefault();
            }
        }
        prop.setProperty(SystemConst.Language.getVal(), locale == null ? "" : locale.getVal());
        try (FileWriter writer = new FileWriter(resource.getFile())){
            prop.store(writer, null);
        } catch (IOException e){
            logger.error("failed store language to system config", e);
        }
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        if (locale == null){
            locale = Locale.getDefault();
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
