package com.sfan.hydro.config;

import com.sfan.hydro.domain.enumerate.FileType;
import com.sfan.hydro.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    private final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private final String CONNECTION_STR_TEMPLATE = "jdbc:mysql://%s:%s/%s?characterEncoding=utf8&useSSL=false&serverTimezone=Hongkong&allowMultiQueries=true";

    private Properties properties;

    private String host;
    private String port;
    private String userName;
    private String password;
    private String databaseName;

    public void setHost(String host) {
        this.host = host;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @PostConstruct
    public void loadJDBCInfo(){
        Yaml yaml = new Yaml();
        Resource resource = new ClassPathResource("hydro-config.yaml");
        if(!resource.exists()){
            logger.error("Hydro-config.yaml Not found");
            throw new RuntimeException();
        }
        Map<String, Map<String, String>> config = null;
        try(InputStream inputStream = resource.getInputStream()) {
            config = yaml.loadAs(inputStream, HashMap.class);
        }catch (IOException e){
            logger.error("can't load hydro-config.yaml");
            throw new RuntimeException();
        }
        Optional.ofNullable(config.get("database")).ifPresent(m -> {
            setHost(m.get("host"));
            setPort(String.valueOf(m.get("port")));
            setUserName(m.get("userName"));
            setPassword(String.valueOf(m.get("password")));
            setDatabaseName(m.get("name"));
        });

        initialCheck();
    }

    @Bean
    public DataSource getDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER_CLASS);
        dataSource.setUrl(String.format(CONNECTION_STR_TEMPLATE, host, port, databaseName));
        dataSource.setUsername(userName);
        dataSource.setPassword(password);

        return dataSource;
    }

    private void initialCheck(){
        properties = new Properties();
        Resource resource =  new ClassPathResource("configuration/webSiteConfig.properties");
        try (InputStream inputStream = resource.getInputStream()){
            properties.load(inputStream);
        }catch (IOException e){
            logger.error("can't load webSiteConfig.properties");
            throw new RuntimeException();
        }

        Connection conn = null;
        Statement stmt = null;
        if (Boolean.valueOf(properties.getProperty("FirstSetup.database"))){
            try {
                Class.forName(DRIVER_CLASS);
                conn = DriverManager.getConnection(String.format(CONNECTION_STR_TEMPLATE, host, port, databaseName), userName, password);
                stmt = conn.createStatement();

                String sql = FileUtil.getFileContext(FileType.ClassPathOrigin, "configuration/hydro.sql");

                stmt.execute(sql);
                logger.info("database initial success");

                try (FileOutputStream fop = new FileOutputStream(resource.getFile())){
                    properties.setProperty("FirstSetup.database", Boolean.FALSE.toString());
                    properties.store(fop, null);
                }
            } catch (ClassNotFoundException e){
                logger.error("cannot load the connection driver", e);
                return;
            } catch (IOException e){
                logger.error("cannot initial database");
                return;
            } catch (SQLException e){
                logger.error("cannot create database connection", e);
                return;
            } finally {
                try {
                    if(conn != null){
                        conn.close();
                    }
                    if(stmt != null){
                        stmt.close();
                    }
                } catch (SQLException e){
                    logger.error("cannot close connection", e);
                }
            }
        }
    }
}
