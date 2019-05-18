package com.sfan.hydro.util.pagination.dialects;

import com.sfan.hydro.domain.expand.PageModel;
import com.sfan.hydro.util.pagination.SqlDialect;
import org.springframework.stereotype.Component;

@Component("mysqlDialect")
public class MysqlDialect implements SqlDialect {
	@Override
	public boolean supportsPaging() {
		return true;
	}

	@Override
	public String getSelectTotalSql(String targetSql) {
		String sql = targetSql.toLowerCase();
		StringBuilder sqlBuilder = new StringBuilder(sql);
		sqlBuilder.insert(0, "select count(1) as count from ( ").append(" ) as t");
		return sqlBuilder.toString();
	}

	@Override
	public String getSelectPagingSql(String targetSql, PageModel pageModel) {
		StringBuilder sqlBuilder = new StringBuilder(targetSql);
        String orderRule = "";
        if(pageModel.getOrderKey()!=null)
        	orderRule = String.format("order by %s %s", pageModel.getOrderKey(), pageModel.getOrderRule() != null ? pageModel.getOrderRule():DEFAULT_SORT_RULE);
        sqlBuilder.insert(0, "select * from (");
        sqlBuilder.append(") tmp" + orderRule);
        sqlBuilder.append(" limit " + ((pageModel.getPageIndex() - 1) * pageModel.getPageSize())
                + "," + pageModel.getPageSize());
        return sqlBuilder.toString();
	}
}
