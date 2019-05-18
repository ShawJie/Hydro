package com.sfan.hydro.attach;

import com.sfan.hydro.domain.enumerate.MessageLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessagesResource {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String code){
        return getMessage(code, null);
    }

    public String getMessage(String code, String defaultMessage){
        return getMessage(code, null, defaultMessage);
    }

    private String getMessage(String code, Object[] args, String defaultMessage){
        return messageSource.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
    }
}
