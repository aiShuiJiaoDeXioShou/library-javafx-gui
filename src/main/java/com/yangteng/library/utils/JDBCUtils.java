package com.yangteng.library.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtils {
    private static final Properties property;
    private static final DruidDataSource dataSource;
    private static final SqlSessionFactory sqlSessionFactory;

    static {
        try {
            //实例化Logger对象
            //加载配置文件
            property = new Properties();
            property.load(ClassLoaderUtils.load("static/config/druid.properties"));
            //实例化数据源对象
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(property);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // myBatis 进行实例化操作
    static {
        DataSource dataSource = getDruidDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMappers("com.yangteng.library.views.main.dao");
        configuration.addMappers("com.yangteng.library.views.notebook.dao");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    /**
     * 获取数据源DataSource对象
     *
     * @return
     */
    public static DruidDataSource getDruidDataSource() {
        return dataSource;
    }

    public static SqlSession getSqlSessionFactory() {
        return sqlSessionFactory.openSession();
    }

    /**
     * 获取数据库连接Connection对象
     *
     * @return Connection对象
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return connection;
        }
        return connection;
    }

    /**
     * 将数据库连接对象归还到数据库连接池中
     *
     * @param connection Connection-数据库连接对象
     */
    public static void returnResources(Connection connection) {
        returnResources(null, null, connection);
    }

    /**
     * 将数据库连接对象归还到数据库连接池中
     *
     * @param statement  Statement-SQL语句执行器
     * @param connection Connection-数据库连接对象
     */
    public static void returnResources(Statement statement, Connection connection) {
        returnResources(null, statement, connection);
    }

    /**
     * 将数据库连接对象归还到数据库连接池中
     *
     * @param resultSet  ResultSet-结果集
     * @param statement  Statement-SQL语句执行器
     * @param connection Connection-数据库连接对象
     */
    public static void returnResources(ResultSet resultSet, Statement statement, Connection connection) {
        if (null != resultSet) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (null != statement) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
