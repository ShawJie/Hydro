package com.sfan.hydro.service;

import com.sfan.hydro.domain.expand.Theme;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

@Service
public interface ThemeService {
    Theme getCurrentThemeConfig();
    List<Theme> getThemeList();
    void changeTheme(Theme theme);
    Theme getSingleThemeInfo(File themeFileDir);
    Theme getSingleThemeInfo(String themePath);
    String checkRequireField(InputStream archiveFileStream);
    void updateThemeOptions(Theme theme);
}
