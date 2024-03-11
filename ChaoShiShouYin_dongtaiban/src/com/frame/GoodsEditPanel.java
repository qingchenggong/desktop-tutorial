package com.frame;

import com.dao.GoodsDAO;
import com.utils.SystemConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GoodsEditPanel extends JInternalFrame {
    public GoodsEditPanel(Object selectedId) {
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
        boxLeft.add(new JLabel("商品名"));
        boxLeft.add(Box.createVerticalStrut(8));
        boxLeft.add(new JLabel("价格"));
        boxLeft.add(Box.createVerticalStrut(8));
        boxLeft.add(new JLabel("库存"));
        boxLeft.add(Box.createVerticalStrut(30));
        Box boxRight = Box.createVerticalBox();
        JTextField field1 = new JTextField(10);
        boxRight.add(field1);
        boxRight.add(Box.createVerticalStrut(5));
        JTextField field2 = new JTextField(10);
        boxRight.add(field2);
        boxRight.add(Box.createVerticalStrut(5));
        JTextField field3 = new JTextField(10);
        boxRight.add(field3);
        boxRight.add(Box.createVerticalStrut(5));
        int id=(int)selectedId;
        if (id>-1){
            Object[] data= GoodsDAO.findById(id);
            field1.setText(data[1].toString());
            field2.setText(data[2].toString());
            field3.setText(data[3].toString());
        }
        JButton btn = new JButton("提交");
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object[] data=new Object[]{id,field1.getText(),Double.parseDouble(field2.getText()),Integer.parseInt(field3.getText())};

                if (id>-1){
                    GoodsDAO.updata(id,data);
                }
                else {
                    GoodsDAO.add(data);
                }
                //回到列表页
                Adminpanel.setContent(new GoodsTablePanel());
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









