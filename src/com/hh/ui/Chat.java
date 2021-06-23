package com.hh.ui;

import com.hh.base.Cmd;
import com.hh.base.SendCmd;
import com.hh.base.SendMsg;
import com.hh.vo.AccountVo;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author LiZechen
 * @create 2021-06-22 21:06
 */
public class Chat extends JFrame implements ActionListener {
    JLabel title;
    JTextPane txtReceive,txtSend;
    JButton btnSend,btnClose,
            btnFile;
    AccountVo myInfo,friendInfo;
    public Chat(AccountVo myInfo,AccountVo friendInfo){
        String str =myInfo.getNickname()+"("+myInfo.getQqCode() +")和";
        str += friendInfo.getNickname()+"("+friendInfo.getQqCode()+")正在聊天...";
        setTitle(str);
        this.myInfo = myInfo;
        this.friendInfo = friendInfo;
        setIconImage(new ImageIcon(friendInfo.getHeadImage()).getImage());
        title = new JLabel(str,new ImageIcon(friendInfo.getHeadImage()),JLabel.LEFT);
        title.setForeground(Color.WHITE);
        title.setOpaque(false);
        JLabel titlebg = new JLabel(new ImageIcon("images/2.jpg"));
        titlebg.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlebg.add(title);
        add(titlebg,BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridLayout(2,1,1,1));
        txtReceive = new JTextPane();
        centerPanel.add(new JScrollPane(txtReceive));
        txtReceive.setEditable(false);
        
        JPanel sendPanel = new JPanel(new BorderLayout());
        JLabel btnPanel = new JLabel(new ImageIcon("images/11.jpg"));
        btnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        btnFile = new JButton("文件");
        btnFile.addActionListener(this);
        btnPanel.add(btnFile);
        sendPanel.add(btnPanel,BorderLayout.NORTH);
        txtSend = new JTextPane();
        sendPanel.add(txtSend,BorderLayout.CENTER);
        btnSend = new JButton("发送");
        btnSend.addActionListener(this);
        btnClose = new JButton("关闭");
        btnClose.addActionListener(this);
        JLabel bottombg = new JLabel(new ImageIcon("images/11.jpg"));
        bottombg.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottombg.add(btnSend);
        bottombg.add(btnClose);
        sendPanel.add(bottombg,BorderLayout.SOUTH);
        centerPanel.add(new JScrollPane(sendPanel));
        add(centerPanel);
        JLabel lblboy = new JLabel(new ImageIcon("images/6.jpg"));
        add(lblboy,BorderLayout.EAST);
        setSize(700, 500);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



    }
    //把发送框的内容提交到接收框，同时清除发送框的内容,不懂
    public void appendView(String name,StyledDocument xx) throws BadLocationException {
        //获取接收框的文档（内容）
        StyledDocument vdoc = txtReceive.getStyledDocument();//获取接收框的内容

        // 格式化时间
        java.util.Date date = new Date();//获取系统当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //设置显示时分秒的格式
        String time = sdf.format(date);//获取时分秒
        //创建一个属性集合
        SimpleAttributeSet as = new SimpleAttributeSet();
        // 显示谁说
        //vdoc.getLength()获取接收框中的原来内容的长度
        String s = name + "    " + time + "\n";
//		saveRecord(name,s);//保存聊天记录
        vdoc.insertString(vdoc.getLength(), s, as);
        int end = 0;
        //处理显示的内容
        while (end < xx.getLength()) {
            // 获得一个元素
            Element e0 = xx.getCharacterElement(end);
            //获取对应的风格，
            SimpleAttributeSet as1 = new SimpleAttributeSet();
            StyleConstants.setForeground(as1, StyleConstants.getForeground(e0.getAttributes()));
            StyleConstants.setFontSize(as1, StyleConstants.getFontSize(e0.getAttributes()));
            StyleConstants.setFontFamily(as1, StyleConstants.getFontFamily(e0.getAttributes()));
            //获取该元素的内容
            s = e0.getDocument().getText(end, e0.getEndOffset() - end);
            // 将元素加到浏览窗中
            if ("icon".equals(e0.getName())) {
                vdoc.insertString(vdoc.getLength(), s, e0.getAttributes());
            } else {
                vdoc.insertString(vdoc.getLength(), s, as1);
//				saveRecord(name,s+"\n");//保存聊天记录
            }
            //getEndOffset（）函数就是获取当前元素的长度
            end = e0.getEndOffset();
            // 输入一个换行
            vdoc.insertString(vdoc.getLength(), "\n", as);

            // 设置显示视图加字符的位置与文档结尾，以便视图滚动
            txtReceive.setCaretPosition(vdoc.getLength());
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSend) {
            if(txtSend.getText().equals("")){
                JOptionPane.showMessageDialog(this, "请输入你要发送的内容。");
                return;
            }else{
                try {
                    appendView(myInfo.getNickname(), txtSend.getStyledDocument());
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }

                SendMsg msg= new SendMsg();
                msg.cmd = Cmd.CMD_SEND;
                msg.myInfo = myInfo;
                msg.friendInfo = friendInfo;
                msg.doc = txtSend.getStyledDocument();
                SendCmd.send(msg);
                }
                txtSend.setText("");
        } else if (e.getSource() == btnClose) {
            dispose();

        } else if (e.getSource() == btnFile) {
            FileDialog dlg = new FileDialog(this,"请选择要发送的文件（64kb以下）",FileDialog.LOAD);
            dlg.setVisible(true);
            String filename = dlg.getDirectory()+dlg.getFile();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(filename);
                int size = fis.available();
                byte[] buffer = new byte[size];
                fis.read(buffer);
                SendMsg msg = new SendMsg();
                msg.cmd = Cmd.CMD_FILE;
                msg.myInfo = myInfo;
                msg.friendInfo = friendInfo;
                msg.b = buffer;
                msg.filename = dlg.getFile();
                SendCmd.send(msg);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }finally {
                try {
                    fis.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
