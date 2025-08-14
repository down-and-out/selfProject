package util;

import com.alibaba.druid.util.JdbcUtils;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConnect {
    private static String driver;
    private static String url;
    private static String user;
    private static String password;

    static{
        try{
            // 1.新建属性集对象
            Properties properties = new Properties();
            // 2通过反射，新建字符输入流，读取db.properties文件
            InputStream inputStream = JdbcUtils.class.getClassLoader().getResourceAsStream("db.properties");
            // 3.将输入流中读取到的属性，加载到properties属性集对象中
            properties.load(inputStream);
            // 根据键，获取 properties 中对应的值 --> 这里需要使用使用括号
            driver = properties.getProperty("jdbc.driver");
            url = properties.getProperty("jdbc.url");
            user = properties.getProperty("jdbc.user");
            password = properties.getProperty("jdbc.password");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Connection getDBConnection(){
        try{
            // 注册数据库驱动
            Class.forName(driver);
            // 获取数据库连接
            Connection connection = DriverManager.getConnection(url,user,password);
            // 返回数据库连接
            return connection;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean closeDBConnection(Connection connection) throws SQLException{
        connection.close();
        return true;
    }


//    public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        //1.注册数据库的驱动
////        Class.forName("com.mysql.cj.jdbc.Driver");
////        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user","root","123456");
//        Connection connection = getDBConnection();
//
//        // 这是需要执行的sql 语句
//        String sql = "insert into try(userName,password) values(?,?)";
//        // 获取预处理对象，并且依次给出参数赋值
//        PreparedStatement preparedStatement = connection.prepareCall(sql);
//        // 表中有三个字段：id,userName,password
//        preparedStatement.setString(1,"try1");
//        preparedStatement.setString(2,"a12345");
//
//        int i = preparedStatement.executeUpdate();
//        System.out.println(i);
//        preparedStatement.close();
//        connection.close();
//    }
}
