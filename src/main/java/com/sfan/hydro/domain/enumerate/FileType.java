package com.sfan.hydro.domain.enumerate;

public enum FileType {
	Article (1, "custom/article/"),
	Image(2, "custom/image/"),
	Theme(3, "classpath:templates/themes/"),
	CustomPage(4, "classpath:templates/customPage/");

	private int value;
	private String path;
	FileType(int value, String path){
		this.value = value;
		this.path = path;
	}
	
	public int getVal() {
		return this.value;
	}
	public String getPath(){
		return this.path;
	}

	public static FileType get(int value){
		for(FileType item : FileType.values()){
			if(item.getVal() == value){
				return item;
			}
		}
		return null;
	}
}
