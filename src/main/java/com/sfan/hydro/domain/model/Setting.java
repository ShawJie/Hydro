package com.sfan.hydro.domain.model;

public class Setting {
    private Integer id;
    private String itemName;
    private String itemValue;
    private boolean systemVariable;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getItemValue() {
        return itemValue;
    }
    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
    public boolean isSystemVariable() {
        return systemVariable;
    }
    public void setSystemVariable(boolean systemVariable) {
        this.systemVariable = systemVariable;
    }
}
