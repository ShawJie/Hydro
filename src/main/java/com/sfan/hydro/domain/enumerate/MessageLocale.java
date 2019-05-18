package com.sfan.hydro.domain.enumerate;

public enum MessageLocale {
    DEFAULT(""),
    ZH_CN("zh-CN"),
    EN_US("en-US");

    MessageLocale(String val){
        this.val = val;
    }

    public String getVal(){
        return this.val;
    }

    public static MessageLocale valueOfStr(String val){
        for(MessageLocale m : MessageLocale.values()){
            if(val.equals(m.val)){
                return m;
            }
        }
        return null;
    }

    String val;
}
