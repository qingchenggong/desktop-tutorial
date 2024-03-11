package com.dao;

import com.pojo.User;
import com.utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public static final Object[] columnNames = {"ID", "手机号", "姓名", "积分"};

    public static Object[][] toList(List<Object[]> list){
        Object[][] result=new Object[list.size()][];
        for (int i=0;i<list.size();i++){
            result[i]=list.get(i);
        }
        return result;
    }
    public static Object[][] search(int col,String text){
        List<Object[]> result=new ArrayList<>();
        String sql="select id,mobile,name,score from marker_customer where 1=1 ";//当管理员没有输入任何查询条件，及全部内容，
        List param=new ArrayList<>();
        if (text!=null&&text.trim().length()>0){                                //如果有则在sql语句后面加上查询语句
            if (col==1){
                sql+=" and mobile=?";
                param.add(text);
            }
        }
        Connection connection=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            connection= DbUtils.open();
            ps=DbUtils.preparedStatement(sql,param,connection);
            rs=ps.executeQuery();
            while (rs.next()){//如果输入空查询，那么遍历所有信息
                Object[] obj=new Object[]{rs.getInt("id"),rs.getString("mobile"),rs.getString("name"),rs.getInt("score")};
                result.add(obj);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DbUtils.closeAll(connection,ps,rs);
        }
        return toList(result);
    }
    public static Object[] findById(int id){
        String sql="select id,mobile,name,score from marker_customer where id=? ";
        List param=new ArrayList<>();
        param.add(id);

        Connection connection=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            connection=DbUtils.open();
            ps=DbUtils.preparedStatement(sql,param,connection);
            rs=ps.executeQuery();
            if (rs.next()){
                Object[] obj=new Object[]{rs.getInt("id"),rs.getString("mobile"),rs.getString("name"),rs.getInt("score")};
                return obj;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DbUtils.closeAll(connection,ps,rs);
        }

        return new Object[columnNames.length];
    }

    //添加数据
    public static void add(Object[] obj){
        String sql="insert into marker_customer(mobile,name,score) values(?,?,0)";
        List param=new ArrayList<>();
        param.add(obj[1]);
        param.add(obj[2]);
        DbUtils.executeUpdate(sql,param);
    }

    //
    public static Object[] findByMobile(String mobile){
        String sql="select id,mobile,name,score from marker_customer where mobile=? ";
        List param=new ArrayList<>();
        param.add(mobile);

        Connection connection=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            connection=DbUtils.open();
            ps=DbUtils.preparedStatement(sql,param,connection);
            rs=ps.executeQuery();
            if (rs.next()){
                Object[] obj=new Object[]{rs.getInt("id"),rs.getString("mobile"),rs.getString("name"),rs.getInt("score")};
                return obj;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DbUtils.closeAll(connection,ps,rs);
        }
        return null;
    }
    //修改数据
    public static void updata(int id,Object[] obj){
        String sql="update marker_customer set score=? where id=? ";
        List param=new ArrayList<>();
        param.add(obj[3]);
        param.add(obj[0]);
        DbUtils.executeUpdate(sql,param);
    }
}

