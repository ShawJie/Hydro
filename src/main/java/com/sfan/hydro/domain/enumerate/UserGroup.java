package com.sfan.hydro.domain.enumerate;

public enum UserGroup {

    Admin("0");

    private String groupType;
    UserGroup(String groupType){
        this.groupType = groupType;
    }

    public String GetVal() {
        return this.groupType;
    }
}
