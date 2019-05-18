package com.sfan.hydro.domain.enumerate;

public enum SystemConst {
    Straight("Straight"),
    Language("Language"),
    User("author");

    String val;
    SystemConst(String val){
        this.val = val;
    }

    public String getVal(){
        return this.val;
    }
}
