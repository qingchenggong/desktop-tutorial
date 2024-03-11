package com.frame;

import com.utils.SystemConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Adminpanel extends JPanel {
    private static JDesktopPane contentpanel=new JDesktopPane();//主面板
    public Adminpanel(){
        this.setBounds(0,0, SystemConstants.FRAME_WIDTH,SystemConstants.FRAME_HEIGHT);
        contentpanel.setBounds(0,20,SystemConstants.FRAME_WIDTH,SystemConstants.FRAME_HEIGHT);
        this.add(contentpanel, BorderLayout.CENTER);//中间

        JMenuBar menuBar=new JMenuBar();//菜单栏的容器
        JMenu paymentMenu=new JMenu("结账");
        JMenu customerMenu=new JMenu("会员管理");
        JMenu goodsMenu=new JMenu("商品管理");
        JMenu recordMenu=new JMenu("消费记录");
        JMenu systemMenu=new JMenu("退出登录");
        menuBar.add(paymentMenu);
        menuBar.add(customerMenu);
        menuBar.add(goodsMenu);
        menuBar.add(recordMenu);
        menuBar.add(systemMenu);

        paymentMenu.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                setContent(new PaymentPanel());
            }
        });

        customerMenu.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                setContent(new CustomerTablePanel());
            }
        });

        goodsMenu.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                setContent(new GoodsTablePanel());
            }
        });

        recordMenu.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                setContent(new RecordTablePanel(null));//在首页点击菜单是没有查询条件的所以这里填null
            }
        });

        systemMenu.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                //setContent(new DataTablePanel());
                contentpanel.removeAll();
                contentpanel.repaint();
                MainFrame.setContent(new LoginPanel());
            }
        });
        this.setLayout(new BorderLayout());
        menuBar.setBounds(0,0,SystemConstants.FRAME_WIDTH,50);//菜单栏大小
        this.add(menuBar,BorderLayout.NORTH);//菜单栏上部
    }
    public static void setContent(JInternalFrame internalFrame){
        internalFrame.setSize(SystemConstants.FRAME_WIDTH-100,SystemConstants.FRAME_HEIGHT-100);
        //显示内部窗口
        internalFrame.setVisible(true);
        contentpanel.removeAll();//重新绘制窗口
        contentpanel.repaint();
        contentpanel.add(internalFrame);
    }
}
