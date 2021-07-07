package com.hh.ui;

/**
 * @author LiZechen
 * @create 2021-06-22 17:39
 */

import com.hh.base.Cmd;
import com.hh.base.SendCmd;
import com.hh.base.SendMsg;
import com.hh.db.BaseDAO;
import com.hh.vo.AccountVo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;

public class Main extends JFrame implements ActionListener, MouseListener, WindowListener {
    JLabel bgImg, lblMyInfo;
    JTabbedPane tabPanel;
    JList lstFriend, lstMate, lstFamily, lstHmd;
    JButton btnFind, btnChange;
    JMenuItem miDel;
    //弹出菜单
    JPopupMenu popMenu;
    //更改状态
    JComboBox cbState;
    //保存登录成功后的个人信息
    AccountVo myInfo, friendInfo;
    //保存所有的好友信息
    ArrayList<AccountVo> vAllInfo, vFriend, vFamily, vMate, vHmd;
    BaseDAO baseDAO = new BaseDAO();
    Chat chat;//定义成员变量接受消息
    HashMap<Integer, Chat> chatWin = new HashMap<Integer, Chat>();//保存聊天内容，采用Hashmap存储

    public Main() {

    }

    public Main(AccountVo myInfo) {
        //传入账户数据
        this.myInfo = myInfo;
        //监听窗口事件
        addWindowListener(this);
        setIconImage(new ImageIcon(myInfo.getHeadImage()).getImage());
//		setUndecorated(true);
        setResizable(false);
        bgImg = new JLabel(new ImageIcon("images/bgmin.jpg"));
        bgImg.setLayout(new BorderLayout());
        //设置背景透明
        bgImg.setOpaque(false);
        add(bgImg);

        //设置TabbedPane为透明背景
        UIManager.put("TabbedPane.contentOpaque", false);
        tabPanel = new JTabbedPane();
        tabPanel.setOpaque(false);
        //传入好友信息
        vAllInfo = new ArrayList<AccountVo>();
        vFriend = new ArrayList<AccountVo>();
        vFamily = new ArrayList<AccountVo>();
        vMate = new ArrayList<AccountVo>();
        vHmd = new ArrayList<AccountVo>();
        lstFriend = new JList();
        lstMate = new JList();
        lstFamily = new JList();
        lstHmd = new JList();
        lstFriend.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstFriend.addMouseListener(this);
        //设置透明
        lstFriend.setOpaque(false);
        lstMate.setOpaque(false);
        lstFamily.setOpaque(false);
        lstHmd.setOpaque(false);
        //设置TabbedPane为透明背景
        UIManager.put("TabbedPane.contentOpaque", false);
        tabPanel = new JTabbedPane();
        tabPanel.setOpaque(false);
        //添加标签页
        tabPanel.addTab("好友", lstFriend);
        bgImg.add(tabPanel);
        refresh();

        //设置个人头像
        lblMyInfo = new JLabel(myInfo.getNickname() + "(" +
                myInfo.getQqCode() + ")【" + myInfo.getRemark() + "】",
                new ImageIcon(myInfo.getHeadImage()), JLabel.LEFT);
        bgImg.add(lblMyInfo, BorderLayout.NORTH);

        //在下面放按钮
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnFind = new JButton("查找");
        btnFind.addActionListener(this);
        //右键删除好友
        miDel = new JMenuItem("删除好友资料");
        miDel.addActionListener(this);
        popMenu = new JPopupMenu();
        popMenu.add(miDel);

        //启动线程
        new ReceiverThread().start();
        //发送登录广播
        SendCmd.sendAll(vAllInfo, myInfo, Cmd.CMD_ONLINE);

        bottomPanel.add(btnFind);
        bgImg.add(bottomPanel, BorderLayout.SOUTH);
        setSize(300, 700);
        setVisible(true);
        setLocation(1050, 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    //鼠标点击
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == lstFriend) {
            if (e.getClickCount() == 2 && lstFriend.getSelectedIndex() >= 0) {//双击且选中
                friendInfo = vFriend.get(lstFriend.getSelectedIndex());
                openChat();
            } else if (e.getButton() == 3 && lstFriend.getSelectedIndex() >= 0) {
                friendInfo = vFriend.get(lstFriend.getSelectedIndex());
                popMenu.show(lstFriend, e.getX(), e.getY());

            }
        }
    }


        @Override
        public void mousePressed (MouseEvent e){

        }

        @Override
        public void mouseReleased (MouseEvent e){

        }

        @Override
        public void mouseEntered (MouseEvent e){

        }

        @Override
        public void mouseExited (MouseEvent e){

        }

        //打开聊天窗口
        public Chat openChat () {
            //从Hashtable中获取根该朋友聊天的窗口信息
            Chat chat = chatWin.get(friendInfo.getQqCode());
            if (chat == null) {
                chat = new Chat(myInfo, friendInfo);
                chatWin.put(friendInfo.getQqCode(), chat);
            }
            //显示
            chat.setVisible(true);
            return chat;
        }

        @Override
        public void windowOpened (WindowEvent e){

        }

        @Override
        public void windowClosing (WindowEvent e){
            //更改为离线状态
            baseDAO.changeStatus(myInfo.getQqCode(), Cmd.STATUS[1]);
            SendCmd.sendAll(vAllInfo, myInfo, Cmd.CMD_OFFLINE);
        }

        @Override
        public void windowClosed (WindowEvent e){

        }

        @Override
        public void windowIconified (WindowEvent e){

        }

        @Override
        public void windowDeiconified (WindowEvent e){

        }

        @Override
        public void windowActivated (WindowEvent e){

        }

        @Override
        public void windowDeactivated (WindowEvent e){

        }

        //显示文本信息
        class DataModel extends AbstractListModel {
            ArrayList<AccountVo> data;

            public DataModel() {
            }

            public DataModel(ArrayList<AccountVo> data) {
                this.data = data;
            }

            //系统自动运行，下标从0开始计算
            public Object getElementAt(int index) {
                AccountVo info = data.get(index);
                return info.getNickname() + "(" + info.getQqCode() + ")【" + info.getRemark() + "]";
            }

            public int getSize() {
                return data.size();
            }

        }

        //接受内部类
        class ReceiverThread extends Thread {
            DatagramSocket socket;


            @Override
            public void run() {
                try {
                    socket = new DatagramSocket(myInfo.getPort());
                    while (true) {
                        byte b[] = new byte[1024 * 512];

                        DatagramPacket packet = new DatagramPacket(b, 0, b.length);
                        //接收信息
                        try {
                            socket.receive(packet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ByteArrayInputStream bais = new ByteArrayInputStream(b);
                        ObjectInputStream ois = null;
                        try {
                            ois = new ObjectInputStream(bais);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            SendMsg msg = (SendMsg) ois.readObject();
                            myInfo = msg.friendInfo;
                            friendInfo = msg.myInfo;
                            switch (msg.cmd) {
                                case Cmd.CMD_ONLINE:
                                case Cmd.CMD_OFFLINE:
                                    refresh();//更新数据库状态
                                    break;
                                case Cmd.CMD_SEND://接收聊天消息
                                    System.out.println("接收聊天消息....");
                                    Chat chat = openChat();
                                    try {
                                        chat.appendView(msg.myInfo.getNickname(), msg.doc);
                                    } catch (BadLocationException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case Cmd.CMD_ADD:
                                    String str = "你是否同意【" + friendInfo.getNickname() + "】添加您为好友？";
                                    SendMsg msg2 = new SendMsg();
                                    int confirm = JOptionPane.showConfirmDialog(null, str, "添加好友", JOptionPane.OK_CANCEL_OPTION);
                                    if (confirm == JOptionPane.OK_OPTION) {
                                        msg2.cmd = Cmd.CMD_AGREE;
                                        baseDAO.addFriend(myInfo.getQqCode(), friendInfo.getQqCode());//确认则进行
                                        refresh();
                                    } else {
                                        msg2.cmd = Cmd.CMD_REFUSE;
                                    }
                                    msg2.myInfo = myInfo;
                                    msg2.friendInfo = friendInfo;
                                    SendCmd.send(msg2);
                                    break;
                                case Cmd.CMD_AGREE:
                                    refresh();
                                    break;
                                case Cmd.CMD_REFUSE:
                                    refresh();
                                    break;
                                case Cmd.CMD_FILE:
                                    str = friendInfo.getNickname() + "发送了" + msg.filename + "文件，是否接收";
                                    confirm = JOptionPane.showConfirmDialog(null, str, "接收文件", JOptionPane.OK_CANCEL_OPTION);
                                    if (confirm == JOptionPane.OK_OPTION) {
                                        JFileChooser chooser = new JFileChooser(" ");
                                        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
                                        chooser.setDialogTitle("保存文件");
                                        if (chooser.showOpenDialog(null) == chooser.APPROVE_OPTION) {
                                            String ext = msg.filename.substring(msg.filename.indexOf('.'), msg.filename.length());//自己起名字并获得扩展名
                                            String filename = chooser.getSelectedFile().getAbsolutePath() + ext;
                                            FileOutputStream fos = new FileOutputStream(filename);
                                            fos.write(msg.b);
                                            fos.flush();
                                            fos.close();
                                        }
                                    }
                                case Cmd.CMD_DELETE:
                                    refresh();
                                    baseDAO.deleteFriend(myInfo.getQqCode(), friendInfo.getQqCode());
                                    refresh();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }


                    }

                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        }

        // 获取好友头像 参照他人实现
        class MyHeadImage extends DefaultListCellRenderer {
            ArrayList<AccountVo> datas;

            public MyHeadImage(ArrayList<AccountVo> datas) {
                this.datas = datas;
            }

            @Override
            public Component getListCellRendererComponent(JList list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value,
                        index, isSelected, cellHasFocus);
                if (index >= 0 && index < datas.size()) {
                    AccountVo user = datas.get(index);
                    setIcon(new ImageIcon(user.getHeadImage()));
                    //设置文本内容
                    setText(user.getNickname() + "(" + user.getQqCode() + ")【" + user.getOnlineStatus() + "】");
                    System.out.println(user.getHeadImage());
                }
                // 设置字体颜色
                if (isSelected) {
                    setBackground(list.getSelectionBackground());
                    setForeground(list.getSelectionForeground());
                } else {
                    setBackground(list.getBackground());
                    setForeground(list.getForeground());
                }
                //设置有效
                setEnabled(list.isEnabled());
                //设置默认字体
                setFont(list.getFont());
//			设置透明
                setOpaque(false);
                return this;
            }
        }


        //把好友信息分别放到相对应的ArrayList中
        public void refresh () {
            //获取所有的好友信息
            vAllInfo = baseDAO.getMyFriend(myInfo.getQqCode());
            //清空ArrayList的值
            vMate.clear();
            vFriend.clear();
            vFamily.clear();
            vHmd.clear();
            for (AccountVo acc : vAllInfo) {
                String groupName = acc.getGroupName();
                if (groupName.equals(Cmd.GROUP_NAME[1])) {//朋友
                    vFriend.add(acc);
                }
                //初始化List图形界面的显示
                lstFriend.setModel(new DataModel(vFriend));
                lstMate.setModel(new DataModel(vMate));
                lstFamily.setModel(new DataModel(vFamily));
                lstHmd.setModel(new DataModel(vHmd));
                lstFriend.setCellRenderer(new MyHeadImage(vFriend));
                lstMate.setCellRenderer(new MyHeadImage(vMate));
                lstFamily.setCellRenderer(new MyHeadImage(vFamily));
                lstHmd.setCellRenderer(new MyHeadImage(vHmd));


            }
        }

        @Override
        public void actionPerformed (ActionEvent e){
            if (e.getSource() == btnFind) {
                new Find(myInfo);
            } else if (e.getSource() == miDel) {//删除好友
                baseDAO.deleteFriend(myInfo.getQqCode(), friendInfo.getQqCode());
                refresh();
                SendMsg msg = new SendMsg();
                msg.cmd = Cmd.CMD_DELETE;
                msg.myInfo=myInfo;
                msg.friendInfo = friendInfo;
                SendCmd.send(msg);
            }
        }

}
