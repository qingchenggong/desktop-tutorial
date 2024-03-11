package com.utils;

import java.sql.*;
import java.util.List;

public class DbUtils {
    private static final String username="root";
    private static final String password="123456";
//    private static final String url="jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false";
    private static final String url="jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=false";
//    private String driver="com.mysql.jdbc.Driver";
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    //打开数据库的连接
    public static Connection open(){
        try {
            Connection connection= DriverManager.getConnection(url,username,password);
            return connection;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //预编译语句
    public static PreparedStatement preparedStatement(String sql, List param,Connection connection){
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            if (param!=null){
                for (int i=0;i<param.size();i++){
                    statement.setObject(i+1,param.get(i));
                }
            }
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //关闭所有资源
    public static void closeAll(Connection connection, PreparedStatement statement, ResultSet resultSet){
        try {
            if (resultSet!=null){
                resultSet.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if (statement!=null){
                statement.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if (connection!=null){
                connection.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static int executeUpdate(String sql,List param){
        PreparedStatement statement=null;
        Connection connection=null;
        try {
            connection=DbUtils.open();
            statement=DbUtils.preparedStatement(sql,param,connection);
            return statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DbUtils.closeAll(connection,statement,null);
        }
        return 0;
    }
}
