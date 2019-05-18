package com.sfan.hydro.support;

import com.sfan.hydro.domain.expand.PageModel;
import com.sfan.hydro.util.pagination.SqlDialect;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Component
@Intercepts({@Signature(type= StatementHandler.class,method="prepare",args={Connection.class,Integer.class})})
public class PaginationInterceptor implements Interceptor {

    @Autowired
    SqlDialect sqlDialect;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler stmt = (StatementHandler)invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(stmt, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
        Connection connection = (Connection) invocation.getArgs()[0];
        if(mappedStatement.getParameterMap().getType() == PageModel.class) {
            PageModel model = (PageModel)stmt.getBoundSql().getParameterObject();
            this.fillCount(stmt.getBoundSql(), mappedStatement, connection, model);
            if(model.getCount() == 0){
                return invocation.proceed();
            }
            String pagingSql = sqlDialect.getSelectPagingSql(stmt.getBoundSql().getSql(), model);

            metaObject.setValue("delegate.boundSql.sql", pagingSql);
            metaObject.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
            metaObject.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {
        // noting to init
    }

    public void fillCount(BoundSql boundSql, MappedStatement mappedStatement, Connection connection, PageModel pageModel) throws SQLException {
        String countSql = sqlDialect.getSelectTotalSql(boundSql.getSql());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Object parameterObject = boundSql.getParameterObject();
        BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings,parameterObject);
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject,countBoundSql);
        PreparedStatement prepareStmt = null;
        ResultSet rs = null;
        try {
            prepareStmt = connection.prepareStatement(countSql);
            parameterHandler.setParameters(prepareStmt);
            rs = prepareStmt.executeQuery();
            if(rs.next()) {
                int count = rs.getInt(1);
                pageModel.setCount(count);
            }
        }finally {
            if(rs!=null)
                rs.close();
            if(prepareStmt!=null)
                prepareStmt.close();
        }
    }
}
