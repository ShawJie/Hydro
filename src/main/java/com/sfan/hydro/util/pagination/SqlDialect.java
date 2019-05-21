package com.sfan.hydro.util.pagination;

import com.sfan.hydro.domain.expand.PageModel;

public interface SqlDialect {
	
	String DEFAULT_SORT_RULE = "ASC";
	
	boolean supportsPaging();
	String getSelectTotalSql(String targetSql);
	String getSelectPagingSql(String targetSql, PageModel pageModel);
}
