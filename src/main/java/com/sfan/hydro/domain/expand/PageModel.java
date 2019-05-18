package com.sfan.hydro.domain.expand;

import java.util.List;

public class PageModel<T> {
	private int pageIndex;
	private int pageSize;
	private int count;
	private String orderKey;
	private String orderRule;
	private T param;
	private List<T> data;
	
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public T getParam() {
		return param;
	}
	public void setParam(T param) {
		this.param = param;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public String getOrderKey() {
		return orderKey;
	}
	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}
	public String getOrderRule() {
		return orderRule;
	}
	public void setOrderRule(String orderRule) {
		this.orderRule = orderRule;
	}
	
	public PageModel(int pageIndex, int pageSize, T param) {
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.param = param;
	}
	public PageModel() {
	}
}
