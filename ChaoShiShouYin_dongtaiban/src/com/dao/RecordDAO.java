package com.dao;

import com.utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//结账按钮，记录本次交易的时间和金额，会员信息，商品明细，减掉商品库存，增加会员积分
public class RecordDAO {
    public static final Object[] columnNames = {"ID", "手机号", "姓名", "日期","金额"};
    public static Object[][] toList(List<Object[]> list){
        Object[][] result=new Object[list.size()][];
        for (int i=0;i<list.size();i++){
            result[i]=list.get(i);
        }
        return result;
    }
    public static Object[][] search(int col,String text){
        List<Object[]> result=new ArrayList<>();
        String sql="select id,mobile,customer_name,created_time,amount from marker_record where 1=1 ";//当管理员没有输入任何查询条件，及全部内容，
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
                Object[] obj=new Object[]{rs.getInt("id"),rs.getString("mobile"),rs.getString("customer_name"),rs.getString("created_time"),rs.getDouble("amount")};
                result.add(obj);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DbUtils.closeAll(connection,ps,rs);
        }
        return toList(result);
    }


    //添加数据
    public static void add(Object[] obj){
        String sql="insert into marker_record(mobile,customer_name,created_time,amount) values(?,?,?,?)";
        List param=new ArrayList<>();
        param.add(obj[1]);
        param.add(obj[2]);
        param.add(obj[3]);
        param.add(obj[4]);
        Connection connection=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            connection=DbUtils.open();
            ps=connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (param!=null){
                for (int i=0;i<param.size();i++){
                    ps.setObject(i+1,param.get(i));
                }
            }
            ps.executeUpdate();
            rs=ps.getGeneratedKeys();
            if (rs.next()){
                String sql2="insert into marker_record_detail(record_id,goods_id,goods_name,price,num) values(?,?,?,?,?)";
                int recordId=rs.getInt(1);
                for (Object[] detail:CartDAO.data){
                    List param2=new ArrayList<>();
                    param2.add(recordId);
                    param2.add(detail[0]);
                    param2.add(detail[1]);
                    param2.add(detail[2]);
                    param2.add(detail[3]);
                    DbUtils.executeUpdate(sql2,param2);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DbUtils.closeAll(connection,ps,rs);
        }
    }

    //查询交易明细
    public static List<Object[]> getDetailList(int id){
        List<Object[]> result=new ArrayList<>();
        String sql="select goods_id,goods_name,price,num from marker_record_detail where record_id=? ";//
        List param=new ArrayList<>();
        param.add(id);

        Connection connection=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            connection=DbUtils.open();
            ps=DbUtils.preparedStatement(sql,param,connection);
            rs=ps.executeQuery();
            while (rs.next()){//如果输入空查询，那么遍历所有信息
                Object[] obj=new Object[]{rs.getInt("goods_id"),rs.getString("goods_name"),rs.getDouble("price"),rs.getInt("num")};
                result.add(obj);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DbUtils.closeAll(connection,ps,rs);
        }
        return result;
    }

}
