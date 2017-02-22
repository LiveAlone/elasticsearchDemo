package org.yqj.es.demo.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ElasticSearchDruidDataSourceFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Created by yaoqijun.
 * Date:2016-11-08
 * Email:yaoqijunmail@gmail.io
 * Descirbe:
 */
public class MainDatasourceTest {
    public static void main(String[] args) throws Exception{
        Properties properties = new Properties();
        properties.put("url", "jdbc:elasticsearch://192.168.1.11:9300/school");
        DruidDataSource dds = (DruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
        dds.setInitialSize(1);
        Connection connection = dds.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from school");
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString("nt"));
        }

        ps.close();
        connection.close();
        dds.close();
    }
}
