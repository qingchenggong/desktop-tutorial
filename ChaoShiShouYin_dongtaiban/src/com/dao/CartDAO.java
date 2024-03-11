package com.dao;

import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    public static final Object[] columnNames = {"商品编号", "商品名", "单价", "数量"};
    public static final List<Object[]> data = new ArrayList<>();

    public static Object[][] toList(List<Object[]> list){
        Object[][] result=new Object[list.size()][];
        for (int i=0;i<list.size();i++){
            result[i]=list.get(i);
        }
        return result;
    }

    public static Object[] findById(int id){
        for (Object[] d:data){
            if ((int) d[0]==id){
                return d;
            }
        }
//        return new Object[columnNames.length];
        return null;
    }

    //添加数据
    public static void add(Object[] obj){
        data.add(obj);
    }

    //修改数据
    public static void update(int id,Object[] obj) {
        for (int i = 0; i < data.size(); i++) {
            if ((int) data.get(i)[0] == id) {
                if ((int) obj[3] > 0) {     //商品库存数量要大于0，不然就从购物车里直接移除
                    data.set(i, obj);
                } else {
                    data.remove(i);
                }
                break;
            }
        }
    }

    public static void remove(int id) {
        for (int i=0;i<data.size();i++){
            if ((int)data.get(i)[0]==id){
                data.remove(i);
                break;
            }
        }
    }
}
