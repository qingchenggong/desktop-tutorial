package com.frame;

import com.dao.CustomerDAO;
import com.dao.RecordDAO;
import com.utils.SystemConstants;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RecordTablePanel extends JInternalFrame {
    private Object[] detailColumns=new Object[]{"货号","商品名","价格","数量"};
    public RecordTablePanel(String mobile){
        super("消费记录",true,true,true,true);
        this.setSize(SystemConstants.FRAME_WIDTH,SystemConstants.FRAME_HEIGHT);

        //顶部操作栏
        JPanel topPanel=new JPanel();
        //查询条件，要查哪一列，需要和DAO中的columnNames字段顺序对应
        JTextField nameField=new JTextField(10);
        nameField.setText(mobile);
        topPanel.add(new JLabel("会员号："));
        topPanel.add(nameField);
        JButton searchBtn=new JButton("查询");
        topPanel.add(searchBtn);

        JTable table=new JTable(){
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };
        //查询所有数据并显示在表格里
        TableModel tableModel=new DefaultTableModel(RecordDAO.search(1,mobile), RecordDAO.columnNames);
        table.setModel(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//单选

        JPanel recordPanel=new JPanel(new BorderLayout());
        recordPanel.add(table.getTableHeader(),BorderLayout.NORTH);
        //
        JScrollPane scrollPane=new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(table);
        recordPanel.add(scrollPane,BorderLayout.CENTER);

        JPanel panel=new JPanel(new GridLayout(2,1));
        panel.add(recordPanel);
        //底部操作栏
        JScrollPane buttomPanel=new JScrollPane();
        buttomPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JTable detailTable=new JTable(){
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };
        detailTable.setModel(new DefaultTableModel(null,detailColumns));
        buttomPanel.setViewportView(detailTable);
        //点击记录展示详情
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //被选中的行
                int rowNum=table.getSelectedRow();
                System.out.println("选中行是"+rowNum);
                if (rowNum<=-1){
                    return;
                }
                int id=(int) table.getValueAt(rowNum,0);
                detailTable.setModel(new DefaultTableModel(RecordDAO.toList(RecordDAO.getDetailList(id)),detailColumns));
            }
        });

        //点击查询按钮事件
        searchBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.setModel(new DefaultTableModel(RecordDAO.search(1,nameField.getText()),RecordDAO.columnNames));
            }
        });

        //把表头添加到容器顶部（使用普通的中间容器添加表格时，表头和内容需要分开添加）
        panel.add(buttomPanel);
        //把表格内容添加到容器中心
        this.add(topPanel,BorderLayout.NORTH);
        this.add(panel,BorderLayout.CENTER);
        //显示内部窗口
        this.setVisible(true);
    }
}

