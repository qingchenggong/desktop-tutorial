package com.frame;

import com.dao.CustomerDAO;
import com.dao.GoodsDAO;
import com.utils.SystemConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerEditPanel extends JInternalFrame {
    public CustomerEditPanel(Object selectedId) {
        super("编辑数据", true, true, true, true);
        this.setSize(SystemConstants.FRAME_WIDTH - 20, SystemConstants.FRAME_HEIGHT - 50);
        //创建内容面板
        JPanel panel = new JPanel();
        panel.setSize(100, 50);
        panel.setLayout(new FlowLayout());
        //设置内部窗口的内容面板.
        this.setContentPane(panel);
        Box boxBase = Box.createHorizontalBox();
        Box boxLeft = Box.createVerticalBox();


        boxLeft.add(new JLabel("手机号"));
        boxLeft.add(Box.createVerticalStrut(8));
        boxLeft.add(new JLabel("姓名"));
        boxLeft.add(Box.createVerticalStrut(30));
        Box boxRight = Box.createVerticalBox();
        JTextField field1 = new JTextField(10);
        boxRight.add(field1);
        boxRight.add(Box.createVerticalStrut(5));
        JTextField field2 = new JTextField(10);
        boxRight.add(field2);
        boxRight.add(Box.createVerticalStrut(5));
        int id=(int)selectedId;
        JButton btn = new JButton("提交");
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (CustomerDAO.findByMobile(field1.getText())!=null){
                    JOptionPane.showMessageDialog(btn.getParent(),"手机号已存在","系统提示",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Object[] data=new Object[]{id,field1.getText(),field2.getText(),0};
                String str="^1[3456789]\\d{9}$";
                Boolean number= new  String((String) data[1]).matches(str);
                if (number){
                    if (data[2].toString().length()>0){
                        CustomerDAO.add(data);
                    }else{
                        JOptionPane.showMessageDialog(btn.getParent(),"请输入会员的姓名","系统提示",JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                else {
                    JOptionPane.showMessageDialog(btn.getParent(),"请输入正确的手机号码","系统提示",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                //回到列表页
                Adminpanel.setContent(new CustomerTablePanel());
            }
        });
        boxRight.add(btn);
        boxBase.add(boxLeft);
        boxBase.add(Box.createHorizontalStrut(8));
        boxBase.add(boxRight);
        panel.add(boxBase);
        //显示内部窗口
        this.setVisible(true);
    }
}










