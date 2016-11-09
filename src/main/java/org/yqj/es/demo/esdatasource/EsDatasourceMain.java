package org.yqj.es.demo.esdatasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ElasticSearchDruidDataSourceFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Created by yaoqijun on 2016/11/8.
 */
public class EsDatasourceMain {
    public static void main(String[] args) throws Exception {
        System.out.println("main result");
        Properties properties = new Properties();
        properties.put("url", "jdbc:elasticsearch://localhost:9300/teacher");
        DruidDataSource dds = (DruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
        dds.setInitialSize(1);
        Connection connection = dds.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from student");
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            System.out.println("-------------------------------");
            System.out.print(" id : "+resultSet.getInt("id"));
            System.out.print(" name : "+resultSet.getString("name"));
            System.out.print(" age : "+resultSet.getInt("age"));
            System.out.println(" grade : "+resultSet.getDouble("grade"));
            System.out.println("-------------------------------");
        }

        ps.close();
        connection.close();
        dds.close();
    }
}
