package com.frame;

import com.dao.CartDAO;
import com.dao.CustomerDAO;
import com.dao.GoodsDAO;
import com.dao.RecordDAO;
import com.utils.SystemConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentPanel extends JInternalFrame {
    //因为商品修改商品，修改数量都需要重新结算，所以做成全局
    private JLabel totalLabel=new JLabel("合计：0元");
    private JLabel categoryLabel=new JLabel("种类：0");
    private JLabel countLabel=new JLabel("件数：0");
    private JTextField goodsField=new JTextField(5);
    private JTextField numField=new JTextField(5);
    private JTable table;
    private Double totalAmount=0.0;
    public PaymentPanel() {
        super("收银台", true, true, true, true);
        this.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_HEIGHT);
        //右边操作栏
        JPanel rightPanel=new JPanel(new BorderLayout());
        //垂直box（盒子模型）
        Box customerBox=Box.createVerticalBox();
        customerBox.setBorder(BorderFactory.createTitledBorder("会员信息"));
        rightPanel.add(customerBox);
        customerBox.add(Box.createVerticalStrut(5));

        //水平box
        Box box1=Box.createHorizontalBox();
        box1.add(Box.createHorizontalStrut(5));
        //查询条件，但要和DAO中columnNames字段对应
        JTextField customerField=new JTextField(10);
        box1.add(new JLabel("会员卡号"));
        box1.add(Box.createHorizontalStrut(5));
        box1.add(customerField);
        box1.add(Box.createHorizontalStrut(5));
        JButton searchCustomerBtn=new JButton("查询");
        box1.add(Box.createHorizontalStrut(5));
        box1.add(searchCustomerBtn);
        box1.add(Box.createHorizontalStrut(5));

        customerBox.add(box1);
        //空组件，美化页面占位用
        customerBox.add(Box.createVerticalStrut(5));

        JLabel customerLabel=new JLabel("会员");
        JPanel wrapper=new JPanel(new FlowLayout(FlowLayout.LEADING,0,0));
        wrapper.add(Box.createHorizontalStrut(5));
        wrapper.add(customerLabel);

        customerBox.add(wrapper);
        //空组件，美化页面占位用
        customerBox.add(Box.createVerticalStrut(5));

        //货品查询
        Box goodsBox=Box.createVerticalBox();
        goodsBox.setBorder(BorderFactory.createTitledBorder("商品信息"));
        goodsBox.add(Box.createVerticalStrut(5));

        //水平box
        Box box2=Box.createHorizontalBox();
        box2.add(Box.createHorizontalStrut(5));

        box2.add(new JLabel("货号"));
        box2.add(Box.createHorizontalStrut(5));
        box2.add(goodsField);
        box2.add(Box.createHorizontalStrut(5));
        JButton searchGoodsBtn=new JButton("查询");
        box2.add(searchGoodsBtn);
        box2.add(Box.createHorizontalStrut(5));

        box2.add(new JLabel("数量"));
        box2.add(Box.createHorizontalStrut(5));
        box2.add(numField);
        box2.add(Box.createHorizontalStrut(5));
        JButton numBtn=new JButton("加购");
        box2.add(numBtn);
        box2.add(Box.createHorizontalStrut(5));

        goodsBox.add(box2);

        Box box3=Box.createHorizontalBox();
        goodsBox.add(Box.createVerticalStrut(5));
        JLabel goodsLabel=new JLabel("--");
        box3.add(goodsLabel);
        goodsBox.add(box3);
        goodsBox.add(Box.createVerticalStrut(10));

        Box paymentBox=Box.createVerticalBox();
        paymentBox.add(Box.createVerticalStrut(10));

        Box box4=Box.createHorizontalBox();
        box4.add(Box.createHorizontalStrut(2));
        JButton plusBtn=new JButton("数量+");
        box4.add(plusBtn);
        box4.add(Box.createHorizontalStrut(5));
        JButton minusBtn=new JButton("数量-");
        box4.add(minusBtn);
        box4.add(Box.createHorizontalStrut(5));
        JButton removeBtn=new JButton("移除商品");
        box4.add(removeBtn);
        box4.add(Box.createHorizontalStrut(5));
        JButton clearBtn=new JButton("清空购物车");
        box4.add(clearBtn);
        box4.add(Box.createHorizontalStrut(2));
        paymentBox.add(box4);
        paymentBox.add(Box.createVerticalStrut(10));

        countLabel.setFont(new java.awt.Font("DIALOG",0,20));
        paymentBox.add(countLabel);

        categoryLabel.setFont(new java.awt.Font("DIALOG",0,20));
        paymentBox.add(categoryLabel);
        //设置字体
        totalLabel.setFont(new java.awt.Font("DIALOG",1,20));
        totalLabel.setForeground(Color.RED);
        paymentBox.add(totalLabel);
        paymentBox.add(Box.createVerticalStrut(10));

        Box box5=Box.createHorizontalBox();
        box5.add(Box.createHorizontalStrut(258));
        JButton payBtn=new JButton("结账");
        box5.add(payBtn);
        paymentBox.add(box5);

        paymentBox.add(Box.createVerticalStrut(140));

        rightPanel.add(customerBox,BorderLayout.NORTH);//上
        rightPanel.add(goodsBox,BorderLayout.CENTER);//中
        rightPanel.add(paymentBox,BorderLayout.SOUTH);//下

        JPanel panel=new JPanel(new BorderLayout());
        panel.setSize(SystemConstants.FRAME_WIDTH,SystemConstants.FRAME_HEIGHT);
        table=new JTable(){
            public boolean isCellEditable(int row,int column){
                if (column==3){           //将第4列（数量）可编辑。
                    return true;
                }
                return false;
            }

            public void setValueAt(Object newValue,int row,int column){
                Object oldValue=getValueAt(row,column);
                if (column==3){
                    try {
                        Integer num=Integer.parseInt(newValue.toString());
                        super.setValueAt(num,row,column);//更新商品信息
                        int id=(int) getValueAt(row,0);
                        if (num<=0){//如果用户输入小于等于0的数代表用户想删除这件商品
                            CartDAO.remove(id);
                            this.setModel(new DefaultTableModel(CartDAO.toList(CartDAO.data),CartDAO.columnNames));
                        }else {
                            Object[] goods=GoodsDAO.findById(id);
                            if (num>(int)goods[3]){
                                JOptionPane.showMessageDialog(numBtn.getParent(),"达到库存上限"+goods[3],"系统提示",JOptionPane.WARNING_MESSAGE);
                                super.setValueAt(oldValue,row,column);
                                return;
                            }
                            Object[] cart=CartDAO.findById(id);
                            cart[3]=num;
                        }
                        calculate();
                    }catch (Exception e){
                        JOptionPane.showMessageDialog(numBtn.getParent(),"商品数量必须是整数","系统提示",JOptionPane.WARNING_MESSAGE);
                        super.setValueAt(oldValue,row,column);
                    }
                }else {
                    super.setValueAt(newValue,row,column);
                }
            }
        };

        TableModel tableModel=new DefaultTableModel(CartDAO.toList(CartDAO.data),CartDAO.columnNames);
        table.setModel(tableModel);
        //点击会员查询按钮事件
        searchCustomerBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object[] customer= CustomerDAO.findByMobile(customerField.getText());
                if (customer!=null){
                    customerLabel.setText("会员："+customer[2]+"，积分："+customer[3]);
                }
                else {
                    customerLabel.setText("会员信息不存在！");
                }
            }
        });

        //点击商品查询按钮
        searchGoodsBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String id=goodsField.getText();
                if (id!=null&&id.trim().length()>0){//将从键盘输入的信息跟货号比对
                    try {
                        Object[] goods=GoodsDAO.findById(Integer.parseInt(id));
                        if (goods==null||goods[0]==null){
                            goodsLabel.setText("未能查到商品信息");
                        }
                        else {
                            goodsLabel.setText(goods[1]+"，售价："+goods[2]+"元，库存："+goods[3]);
                            numField.setText("1");//查询到了就将购物车里的默认数量写成1
                        }
                    }catch (Exception ex){
                        goodsLabel.setText("未查到商品信息");
                    }
                }
            }
        });

        //点击加购按钮
        numBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String idstr=goodsField.getText();
                if (idstr!=null&&idstr.trim().length()>0){
                    Integer id=null;
                    try {
                        id=Integer.parseInt(idstr);
                    }catch (Exception ex){
                        goodsLabel.setText("未查到商品信息");
                        return;
                    }
                    Object[] goods=GoodsDAO.findById(id);
                    if (goods==null||goods[0]==null){
                        goodsLabel.setText("未查到商品信息");
                        return;
                    } else {
                        goodsLabel.setText(goods[1]+",售价："+goods[2]+"元，库存："+goods[3]);
                        Integer num=1;
                        try {
                            num = Integer.parseInt(numField.getText());
                        }catch (Exception e2){//这里将会有一个异常，如果加购商品不是一个整数，那就将num重置为1
                            numField.setText("1");
                            num=1;
                        }
                        if (num>(int)goods[3]){//商品加购数量大于了库存
                            JOptionPane.showMessageDialog(numBtn.getParent(),"达到库存上限"+goods[3],"系统提示",JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        Object[] cart= CartDAO.findById(id);//!!!!!!!!!!!!!!!!
                        if (cart!=null){
                            cart[3]=(int)cart[3]+num;
                            if ((int)cart[3]>(int) goods[3]){
                                JOptionPane.showMessageDialog(numBtn.getParent(),"达到库存上限"+goods[3],"系统提示",JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            CartDAO.update(id,cart);
                        } else {
                            cart=new Object[]{id,goods[1],goods[2],num};
                            CartDAO.add(cart);
                        }
                        calculate();
                    }
                }
            }
        });

        //数量加按钮
        plusBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowNum=table.getSelectedRow();
                System.out.println("选中行是"+rowNum);
                if (rowNum<=-1){
                    return;
                }
                int id=(int) table.getValueAt(rowNum,0);
                Object[] goods=GoodsDAO.findById(id);
                Object[] cart=CartDAO.findById(id);
                if ((int)cart[3]+1<=(int) goods[3]){
                    cart[3]=(int)cart[3]+1;
                    CartDAO.update(id,cart);
                }else {
                    JOptionPane.showMessageDialog(numBtn.getParent(),"达到库存上限"+goods[3],"系统提示",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                calculate();
                table.setRowSelectionInterval(rowNum,rowNum);//继续选择商品按钮
            }
        });

        //数量-
        minusBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowNum=table.getSelectedRow();
                System.out.println("选中行是"+rowNum);
                if (rowNum<=-1){
                    return;
                }
                int id=(int) table.getValueAt(rowNum,0);
                Object[] cart=CartDAO.findById(id);
                if ((int)cart[3]-1>0){
                    cart[3]=(int)cart[3]-1;
                    CartDAO.update(id,cart);
                    calculate();
                    table.setRowSelectionInterval(rowNum,rowNum);//继续选择商品按钮
                }else {
                    CartDAO.remove(id);
                    calculate();
                }
            }
        });

        //移除商品
        removeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowNum=table.getSelectedRow();
                System.out.println("选中行是"+rowNum);
                if (rowNum<=-1){
                    return;
                }
                int id=(int) table.getValueAt(rowNum,0);
                CartDAO.remove(id);
                calculate();
            }
        });

        //清空购物车
        clearBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CartDAO.data.clear();
                calculate();
            }
        });

        //结账按钮
        payBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Object[] customer=CustomerDAO.findByMobile(customerField.getText());
                String phone=null;
                String name=null;
                if (customer!=null){//校验是否是会员
                    phone=customer[1].toString();
                    name=customer[2].toString();
                }
                Object[] record=new Object[]{0,phone,name,format.format(new Date()),totalAmount};
                RecordDAO.add(record);
                if (customer!=null){
                    //积分
                    customer[3]=(int)customer[3]+totalAmount.intValue();//满一元加一分，不满不加，读取整数部分即可
                    CustomerDAO.updata((int) customer[0],customer);
                }
                for (Object[] obj:CartDAO.data){
                    Object[] goods=GoodsDAO.findById((int) obj[0]);
                    //库存
                    goods[3]=(int)goods[3]-(int) obj[3];
                    GoodsDAO.updata((int) goods[0],goods);
                }
                CartDAO.data.clear();
                calculate();
            }
        });

        //把表头添加到容器顶部（使用普通的中间容器添加表格时，表头和内容需要分开添加）
        panel.add(table.getTableHeader(),BorderLayout.NORTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//单选
        //把表格内容添加到容器中心
        this.add(rightPanel,BorderLayout.EAST);
        panel.add(table,BorderLayout.CENTER);
        this.add(panel,BorderLayout.CENTER);
        //显示内部窗口
        this.setVisible(true);
    }

    //购物车的4个按钮都会影响总价的变化，所以将此部分单独列出封装
    public void calculate(){
        int cat=CartDAO.data.size();//商品的种类数量（加购同一种商品只增添数量）
        totalAmount=0.0;
        int count=0;
        for (Object[] d:CartDAO.data){
            count+=(int) d[3];//索引为3是商品数量
            totalAmount+=(double)d[2]*(int) d[3];//单价*数量
        }
        table.setModel(new DefaultTableModel(CartDAO.toList(CartDAO.data),CartDAO.columnNames));//按下数量操作内容会更新
        totalLabel.setText("合计："+totalAmount+"元");
        countLabel.setText("件数："+count);
        categoryLabel.setText("种类："+cat);
        goodsField.setText("");//加购成功会清空商品编号的那一个输入框，并将此改写成1
        numField.setText("1");
    }

}
