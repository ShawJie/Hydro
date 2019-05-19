

package com.sfan.hydro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfan.hydro.attach.MessagesResource;
import com.sfan.hydro.domain.expand.Theme;
import com.sfan.hydro.service.ThemeService;
import com.sfan.hydro.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service("ThemeService")
public class ThemeServiceImpl implements ThemeService {

    private static Theme currentTheme;
    private final String THEME_DIR = "classpath:templates/themes/";
    private final String ROUTE_PREFIX = "Route.";
    private final String OPTION_PREFIX = "Option.";
    private final String THEME_FILE_NAME = "theme.properties";
    private final String[] THEME_FILE_REQUIRE_FIELD = {"Name", "Creator", "Description", "Rendering", "Route.Index", "Route.post.detail"};

    private Properties config = new Properties();
    private Resource configResource = new ClassPathResource("webSiteConfig.properties");

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ThymeleafViewResolver viewResolver;
    @Autowired
    private MessagesResource messagesResource;


    @Override
    public Theme getCurrentThemeConfig() {
        return currentTheme;
    }

    @Override
    public List<Theme> getThemeList() {
        File themeDirFile = FileUtil.filepathResolver(THEME_DIR);
        List<Theme> themeList = new ArrayList<>();
        File[] files = themeDirFile.listFiles();
        for(File item : files){
            themeList.add(getSingleThemeInfo(item));
        }
        return themeList;
    }

    @Override
    public void changeTheme(Theme theme){
        try {
            theme = getSingleThemeInfo(theme.getThemePath());
            updateTemplateFragmentPath("themes/" + theme.getThemePath());
            updatePropertiesVal(theme);
        }catch (IOException e){
            logger.error("change theme failed", e);
            throw new RuntimeException("change theme failed");
        }
        currentTheme = theme;
    }

    @Override
    public Theme getSingleThemeInfo(File themeFileDir) {
        if(themeFileDir == null){
            return null;
        }
        Theme theme = null;
        if(themeFileDir.isDirectory()){
            theme = new Theme();
            String themePath = themeFileDir.getName() + "/";
            theme.setThemePath(themePath);
            theme.setCurrent(themePath.equals(currentTheme.getThemePath()));
            File target = themeFileDir.listFiles((dir, name) -> name.equals(THEME_FILE_NAME))[0];
            Properties themeConf = new Properties();
            Map<String, String> themeProperties = null;
            try (InputStreamReader isr = new InputStreamReader(new FileInputStream(target), "UTF-8")){
                themeConf.load(isr);
            }catch (IOException e){
                e.printStackTrace();
            }

            themeProperties = new HashMap(themeConf);
            theme.setCreator(themeProperties.get("Creator"));
            theme.setDescription(themeProperties.get("Description"));
            theme.setThemeName(themeProperties.get("Name"));
            theme.setRendering(themeProperties.get("Rendering"));

            Map<String, String> routeMap = new HashMap();
            Map<String, String> optionMap = new HashMap<>();

            themeProperties.forEach((k ,v) -> {
                if(k.startsWith("Route.")){
                    routeMap.put(k.replace(ROUTE_PREFIX, ""), v);
                }else if(k.startsWith("Option.")){
                    optionMap.put(k.replace(OPTION_PREFIX, ""), v);
                }
            });
            theme.setRoute(routeMap);
            theme.setOption(optionMap.size() > 0 ? optionMap : null);
        }
        return theme;
    }

    @Override
    public Theme getSingleThemeInfo(String themePath) {
        File resource = FileUtil.filepathResolver(THEME_DIR + themePath);
        Theme theme = null;
        if(resource.exists()){
            theme = getSingleThemeInfo(resource);
        }
        return theme;
    }

    @Override
    public String checkRequireField(InputStream archiveFileStream){
        StringBuilder alertMsg = new StringBuilder();

        boolean requireFile = false;

        Properties prop = new Properties();
        Map<String, String> themeProperties = null;
        try(ZipInputStream zin = new ZipInputStream(archiveFileStream)){
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null){
                if(Pattern.matches(".*"+ THEME_FILE_NAME +"$", entry.getName())){
                    requireFile = true;
                    prop.load(zin);
                    break;
                }
            }
        }catch (IOException e){
            logger.error("", e);
            e.printStackTrace();
        }

       if(requireFile){
           Arrays.asList(THEME_FILE_REQUIRE_FIELD).forEach(f -> {
               if(!themeProperties.containsKey(f)){
                   alertMsg.append(messagesResource.getMessage(String.format("Theme.require.%s", f)));
                   alertMsg.append("\r\n");
               }
           });
       }else{
           alertMsg.append(messagesResource.getMessage("Theme.require.main_file"));
       }

        return alertMsg.toString();
    }

    @Override
    public void updateThemeOptions(Theme theme) {
        Properties themeProp = new Properties();
        File resource = FileUtil.filepathResolver(THEME_DIR + theme.getThemePath());
        if(resource.exists()){
            File propertiesFile = resource.listFiles((dir, name) -> name.equals(THEME_FILE_NAME))[0];

            try (InputStreamReader isr = new InputStreamReader(new FileInputStream(propertiesFile), "UTF-8")){
                themeProp.load(isr);

                theme.getOption().forEach((k, v) -> themeProp.setProperty(OPTION_PREFIX + k, v));

                FileOutputStream fos = new FileOutputStream(propertiesFile);
                themeProp.store(fos, null);
                fos.close();

                currentTheme.setOption(theme.getOption());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @PostConstruct
    private void initial(){
        try {
            config.load(configResource.getInputStream());
            Theme theme = new Theme(config.getProperty("Theme.Name"), config.getProperty("Theme.Path"));
            currentTheme = theme;
            changeTheme(theme);
        }catch (IOException e){
            throw new RuntimeException("Theme initial failed", e);
        }
    }

    private void updateTemplateFragmentPath(String path){
        viewResolver.addStaticVariable("FPath", path);
    }

    private void updatePropertiesVal(Theme theme) throws IOException{
        config.setProperty("Theme.Name", theme.getThemeName());
        config.setProperty("Theme.Path", theme.getThemePath());

        FileOutputStream fos = new FileOutputStream(FileUtil.filepathResolver("classpath:" + configResource.getFilename()));
        config.store(fos, null);
        fos.close();
    }
}
