package com.frame;

import com.dao.CustomerDAO;
import com.dao.GoodsDAO;
import com.utils.SystemConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomerTablePanel extends JInternalFrame {
    public CustomerTablePanel(){
        super("会员列表",true,true,true,true);
        this.setSize(SystemConstants.FRAME_WIDTH,SystemConstants.FRAME_HEIGHT);
        JTable table=new JTable(){
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };
        //查询所有数据并显示在表格里
        TableModel tableModel=new DefaultTableModel(CustomerDAO.search(1,""), CustomerDAO.columnNames);
        table.setModel(tableModel);
        //顶部操作栏
        JPanel topPanel=new JPanel();
        //查询条件，要查哪一列，需要和DAO中的columnNames字段顺序对应
        JTextField nameField=new JTextField(20);
        topPanel.add(new JLabel("手机号："));
        topPanel.add(nameField);
        JButton searchBtn=new JButton("查询");
        topPanel.add(searchBtn);
        JButton addBtn=new JButton("添加会员");
        topPanel.add(addBtn);
        JButton recordBtn=new JButton("消费记录");
        topPanel.add(recordBtn);
        JPanel panel=new JPanel(new BorderLayout());
        panel.setSize(SystemConstants.FRAME_WIDTH,SystemConstants.FRAME_HEIGHT);
        //点击查询按钮事件
        searchBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TableModel tableModel=new DefaultTableModel(CustomerDAO.search(1,nameField.getText()), CustomerDAO.columnNames);
                table.setModel(tableModel);
            }
        });
        //点击添加按钮
        addBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Adminpanel.setContent(new CustomerEditPanel(-1));
            }
        });
        //点击消费记录按钮
        recordBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowNum=table.getSelectedRow();
                System.out.println("选中行是"+rowNum);
                if (rowNum<=-1){
                    return;
                }
                Adminpanel.setContent(new RecordTablePanel(table.getValueAt(rowNum,1).toString()));
            }
        });
        //把表头添加到容器顶部（使用普通的中间容器添加表格时，表头和内容需要分开添加）
        panel.add(table.getTableHeader(),BorderLayout.NORTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//单选
        //把表格内容添加到容器中心
        this.add(topPanel,BorderLayout.NORTH);
        panel.add(table,BorderLayout.CENTER);
        this.add(panel,BorderLayout.CENTER);
        //显示内部窗口
        this.setVisible(true);
    }
}

