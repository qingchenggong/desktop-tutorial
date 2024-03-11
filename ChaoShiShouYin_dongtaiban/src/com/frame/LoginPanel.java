package com.frame;

import com.dao.UserDAO;
import com.pojo.User;
import com.utils.SystemConstants;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPanel extends JPanel {
    public LoginPanel(){
        this.setBounds(0,0, SystemConstants.FRAME_WIDTH,SystemConstants.FRAME_HEIGHT);

        this.add(new JLabel("用户名"));
        JTextField nameField=new JTextField(10);
        this.add(nameField);

        this.add(new JLabel("密码"));
        JPasswordField pwdField=new JPasswordField(10);
        this.add(pwdField);

        JButton loginBtn=new JButton("登录");
        this.add(loginBtn);
        loginBtn.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                String username=nameField.getText();
                String password=new String(pwdField.getPassword());
                User user= UserDAO.getUserByName(username);
                JPanel panel=new Adminpanel();

                if (user==null||!user.getPassword().equals(password)){
                    JOptionPane.showMessageDialog(loginBtn.getParent(),"用户名或密码错误","系统提示",JOptionPane.WARNING_MESSAGE);
                }
                else {
                    MainFrame.setContent(panel);
                    Adminpanel.setContent(new PaymentPanel());//默认显示结账界面
                    MainFrame.user=user;
                }
            }
        });
    }
}
