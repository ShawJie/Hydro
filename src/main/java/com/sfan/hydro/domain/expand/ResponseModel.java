package com.sfan.hydro.domain.expand;

public class ResponseModel {
	private Boolean success;
	private String msg;
	private Object data;

	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public ResponseModel(Boolean success, String msg, Object data) {
		this.success = success;
		this.msg = msg;
		this.data = data;
	}
	public ResponseModel() {}
}
