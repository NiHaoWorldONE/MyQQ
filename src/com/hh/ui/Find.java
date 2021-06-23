package com.hh.ui;

import com.hh.base.Cmd;
import com.hh.base.SendCmd;
import com.hh.base.SendMsg;
import com.hh.db.BaseDAO;
import com.hh.vo.AccountVo;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author LiZechen
 * @create 2021-06-23 11:28
 */
public class Find extends JFrame implements ActionListener {

    JLabel lblQQCode ,lblNickname;
    JTextField txtQQCode,txtNickname;
    JTable dataTable;
    JButton btnFind,btnAdd;
    AccountVo myInfo;
    ArrayList<String> aHead;
    ArrayList aData;
    BaseDAO baseDAO;


    public Find(){
        init();
    }
    public  Find (AccountVo accountVo){
        this.myInfo = accountVo;
        init();
    }
   //初始化
    public void init(){
        JPanel topPanel = new JPanel(new FlowLayout((FlowLayout.LEFT)));
        lblQQCode = new JLabel("QQ号码");
        lblNickname = new JLabel("昵称");
        txtQQCode = new JTextField(8);
        txtNickname = new JTextField(5);
        btnFind = new JButton("查找");
        btnFind = new JButton("查找");
        topPanel.add(lblQQCode);
        topPanel.add(txtQQCode);
        topPanel.add(lblNickname);
        topPanel.add(txtNickname);
        topPanel.add(btnFind);
        add(topPanel,BorderLayout.NORTH);
        String[] columnNames = {"头像", "QQ号码", "昵称", "年龄", "性别","状态"};
        aHead = new ArrayList<>();
        for(int i=0;i<columnNames.length;i++){
            aHead.add(columnNames[i]);
        }

        int myQQ = myInfo.getQqCode();
        String sql = "select headimage,qqcode,nickname,age,sex,onlinestatus from account  where qqcode != " + myQQ;
        baseDAO = new BaseDAO();
        aData = baseDAO.findFriend(sql);
        dataTable = new JTable(new MyTableModel(aHead,aData));
        dataTable.setRowHeight(60);
        add(new JScrollPane(dataTable));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("添加好友");
        bottomPanel.add(btnAdd);

        add(bottomPanel,BorderLayout.SOUTH);
        btnFind.addActionListener(this);
        btnAdd.addActionListener(this);
        setSize(800, 500);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    //内部类实现抽象的TableModel
    class MyTableModel extends AbstractTableModel {
        ArrayList<String> vHead;
        ArrayList data;
        public MyTableModel(ArrayList<String> vHead,ArrayList data) {
            this.vHead = vHead;
            this.data = data;
        }
        //得到表格列长度
        public int getColumnCount() {
            return vHead.size();
        }
        //得到表格行数
        public int getRowCount() {
            return data.size();
        }
        //得到表格列名
        public String getColumnName(int col) {
//            System.out.println(" 该列的列名为：" + vHead.get(col));
            return vHead.get(col);
        }
        //得到指定单元的值
        public Object getValueAt(int row, int col) {
            ArrayList rowData = (ArrayList)aData.get(row);
            if(col==0){//表示头像
                return new ImageIcon(rowData.get(col).toString());
            }else{
                return rowData.get(col);
            }
        }
        //返回指定列的数据类型
        @Override
        public Class<?> getColumnClass(int c) {
            if(c==0){//表示头像
                return ImageIcon.class;
            }else{
                return super.getColumnClass(c);
            }
        }
        //设置单元格是否可修改
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        //设置单元格的值
        public void setValueAt(Object value, int row, int col) {
            ArrayList rowData = (ArrayList)aData.get(row);
            rowData.set(col, value);
            fireTableCellUpdated(row, col);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnFind){//添加按钮
            String sql = "select headimage,qqcode,nickname,age,sex,onlinestatus from account " +
                    "where 1 = 1";
            String qqCode = txtQQCode.getText().trim();
            String nickName = txtNickname.getText().trim();
            if(!qqCode.equals("")){
                sql += " and qqCode =" + qqCode;
            }
            if(!nickName.equals("")){
                sql += " and nickname like '%" + nickName + "%'";//找包含nickname字段的
            }
            aData = baseDAO.findFriend(sql);
            dataTable.setModel(new MyTableModel(aHead,aData));
        } else if(e.getSource() == btnAdd){
            int index = dataTable.getSelectedRow();
            if(index >= 0){
                ArrayList row = (ArrayList) aData.get(index);
                int qqCode = Integer.parseInt(row.get(1).toString());
                AccountVo friendInfo = baseDAO.getSelectedFriend(qqCode);
                //判断是否是好友
                if(baseDAO.isFriend(myInfo.getQqCode(),qqCode)){
                    JOptionPane.showMessageDialog(this,"你们已经是好友了!");
                    return;
                }else{
                    String msg = "是否添加【" + friendInfo.getNickname() + "】为好友？" ;
                    int confirm = JOptionPane.showConfirmDialog(this,msg,"添加好友",JOptionPane.OK_CANCEL_OPTION);
                    if(confirm == JOptionPane.OK_OPTION) {
                        SendMsg message = new SendMsg();
                        message.cmd = Cmd.CMD_ADD;
                        message.myInfo = myInfo;
                        message.friendInfo = friendInfo;
                        SendCmd.send(message);
                    }
                }
            }
        }
    }


}




