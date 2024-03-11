package com.frame;

import com.dao.GoodsDAO;
import com.utils.SystemConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GoodsTablePanel extends JInternalFrame {
    public GoodsTablePanel(){
        super("商品列表",true,true,true,true);
        this.setSize(SystemConstants.FRAME_WIDTH,SystemConstants.FRAME_HEIGHT);
        JTable table=new JTable(){
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };
        //查询所有数据并显示在表格里
        TableModel tableModel=new DefaultTableModel(GoodsDAO.search(1,""), GoodsDAO.columnNames);
        table.setModel(tableModel);
        //顶部操作栏
        JPanel topPanel=new JPanel();
        //查询条件，要查哪一列，需要和DAO中的columnNames字段顺序对应
        JTextField nameField=new JTextField(20);
        topPanel.add(new JLabel("商品名："));
        topPanel.add(nameField);
        JButton searchBtn=new JButton("查询");
        topPanel.add(searchBtn);
        JButton addBtn=new JButton("添加");
        topPanel.add(addBtn);
        JButton editBtn=new JButton("编辑");
        topPanel.add(editBtn);
        JPanel panel=new JPanel(new BorderLayout());
        panel.setSize(SystemConstants.FRAME_WIDTH,SystemConstants.FRAME_HEIGHT);
        //点击查询按钮事件
        searchBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TableModel tableModel=new DefaultTableModel(GoodsDAO.search(1,nameField.getText()), GoodsDAO.columnNames);
                table.setModel(tableModel);
            }
        });
        //点击添加按钮
        addBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Adminpanel.setContent(new GoodsEditPanel(-1));
            }
        });
        //点击修改按钮
        editBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowNum=table.getSelectedRow();
                System.out.println("选中行是"+rowNum);
                if (rowNum<=-1){
                    return;
                }
                Adminpanel.setContent(new GoodsEditPanel(table.getValueAt(rowNum,0)));
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
