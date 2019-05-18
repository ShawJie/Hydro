package com.sfan.hydro.util.pagination;

import com.sfan.hydro.domain.expand.PageModel;
import org.springframework.stereotype.Service;

@Service
public interface SqlDialect {
	
	String DEFAULT_SORT_RULE = "ASC";
	
	boolean supportsPaging();
	String getSelectTotalSql(String targetSql);
	String getSelectPagingSql(String targetSql, PageModel pageModel);
}
