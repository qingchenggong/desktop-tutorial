package com.frame;

import com.pojo.User;
import com.utils.SystemConstants;

import javax.swing.*;

public class MainFrame {
    public static final JFrame frame=new JFrame("超市收银系统");
    public static User user;//当前登录用户

    public static void main(String[] args) {
        frame.setSize(SystemConstants.FRAME_WIDTH,SystemConstants.FRAME_HEIGHT);//窗体大小
        frame.setLayout(null);//清空窗体布局
        frame.setLocationRelativeTo(null);//窗体置中
        frame.setVisible(true);//窗体可见
        frame.setContentPane(new LoginPanel());//窗体默认界面是登录界面
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//主窗体退出时退出主程序
    }
    //调用，更换页面（窗口中含有不同的模块）
    public static void setContent(JPanel panel){
        frame.setContentPane(panel);
    }
}
