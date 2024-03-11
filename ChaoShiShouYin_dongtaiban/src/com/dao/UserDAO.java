package com.dao;

import com.pojo.User;
import com.utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//登录的用户
public class UserDAO {
    public static User getUserByName(String username){
        String sql="select id,username,password from marker_admin where username=?";
        List param=new ArrayList<>();
        param.add(username);
        Connection connection=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            connection= DbUtils.open();
            ps=DbUtils.preparedStatement(sql,param,connection);
            rs=ps.executeQuery();
            if (rs.next()){
                User user=new User();
                user.setPassword(rs.getString("password"));
                user.setUsername(rs.getString("username"));
                return user;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DbUtils.closeAll(connection,ps,rs);
        }
        return null;
    }
}
