package com.hh.ui;

import com.hh.db.BaseDAO;
import com.hh.vo.AccountVo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**注册ui
 * @author LiZechen
 * @create 2021-06-22 9:43
 */
public class Register extends JFrame {
    private JLabel lblQQCode, lblNickname, lblPassword, lblConfirm,//确认密码
            lblAge, lblSex, lblRemark, lblIpAddress, lblPort, lblHeadImage;
    private JTextField txtQQCode, txtNickname, txtAge, txtIpAddress, txtPort;
    private JPasswordField txtPassword, txtConfirm;
    private JComboBox cbHeadImage;
    private JRadioButton rbMale, rbFemale;
    private JTextArea taRemark;
    private ButtonGroup bgSex;
    private JLabel lblBG;
    private JButton btnSave;
    private String sHeadImage[] = {"head/0.png", "head/1.png", "head/2.png",
            "head/3.png", "head/4.png", "head/5.png", "head/6.png",
            "head/7.png", "head/8.png", "head/9.png", "head/10.png"
    };
    private ImageIcon[] headIcon = {new ImageIcon(sHeadImage[0]),
            new ImageIcon(sHeadImage[1]), new ImageIcon(sHeadImage[2]),
            new ImageIcon(sHeadImage[3]), new ImageIcon(sHeadImage[4]),
            new ImageIcon(sHeadImage[5]), new ImageIcon(sHeadImage[6]),
            new ImageIcon(sHeadImage[7]), new ImageIcon(sHeadImage[8]),
            new ImageIcon(sHeadImage[9]), new ImageIcon(sHeadImage[10]),
    };


    public Register() throws HeadlessException {
        super("MyQQ注册");
        lblBG = new JLabel(new ImageIcon("images/bgreg8.jpg"));
        add(lblBG);
        lblBG.setLayout(null);
        JLabel title = new JLabel("用户注册", JLabel.CENTER);
        title.setFont(new Font("黑体", Font.BOLD, 48));
        title.setBounds(215, 15, 200, 50);
        lblBG.add(title);
        lblQQCode = new JLabel("QQ号码：", JLabel.RIGHT);
        lblNickname = new JLabel("昵称：", JLabel.RIGHT);
        lblHeadImage = new JLabel("头像：", JLabel.RIGHT);
        lblPassword = new JLabel("登录密码：", JLabel.RIGHT);
        lblConfirm = new JLabel("确认密码：", JLabel.RIGHT);
        lblAge = new JLabel("年龄：", JLabel.RIGHT);
        lblSex = new JLabel("性别：", JLabel.RIGHT);
        lblIpAddress = new JLabel("Ip地址：", JLabel.RIGHT);
        lblPort = new JLabel("端口：", JLabel.RIGHT);
        lblRemark = new JLabel("备注：", JLabel.RIGHT);

        txtQQCode = new JTextField(10);
        txtQQCode.setText("登录账号，注册后系统自动生成");
        txtQQCode.setEditable(false);
        txtNickname = new JTextField(10);
        cbHeadImage = new JComboBox(headIcon);
        txtPassword = new JPasswordField(10);
        txtPassword.setEchoChar('*');
        txtConfirm = new JPasswordField(10);
        txtConfirm.setEchoChar('*');
        txtAge = new JTextField(5);
        rbMale = new JRadioButton("男", true);
        rbFemale = new JRadioButton("女");
        bgSex = new ButtonGroup();
        bgSex.add(rbFemale);
        bgSex.add(rbMale);
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        txtIpAddress = new JTextField(address.getHostAddress());
        txtPort = new JTextField(5);
        txtPort.setEditable(false);
        taRemark = new JTextArea(3, 80);
        btnSave = new JButton("注册(S)");
        btnSave.setMnemonic('s');
        //qq号码
        lblQQCode.setBounds(10, 120, 100, 20);
        lblBG.add(lblQQCode);
        txtQQCode.setBounds(120, 120, 200, 20);
        lblBG.add(txtQQCode);
        //昵称
        lblNickname.setBounds(10, 150, 100, 20);
        lblBG.add(lblNickname);
        txtNickname.setBounds(120, 150, 200, 20);
        lblBG.add(txtNickname);
        //密码
        lblPassword.setBounds(10, 180, 100, 20);
        lblBG.add(lblPassword);
        txtPassword.setBounds(120, 180, 200, 20);
        lblBG.add(txtPassword);
        //确认
        lblConfirm.setBounds(10, 210, 100, 20);
        lblBG.add(lblConfirm);
        txtConfirm.setBounds(120, 210, 200, 20);
        lblBG.add(txtConfirm);
        //年龄
        lblAge.setBounds(380, 185, 100, 20);
        lblBG.add(lblAge);
        txtAge.setBounds(480, 185, 50, 20);
        lblBG.add(txtAge);
        //性别
        lblSex.setBounds(380, 215, 100, 20);
        lblBG.add(lblSex);
        rbMale.setBounds(480, 215, 40, 20);
        rbMale.setOpaque(false);//设置隐藏
        lblBG.add(rbMale);
        rbFemale.setBounds(520, 215, 40, 20);
        rbFemale.setOpaque(false);//设置隐藏
        lblBG.add(rbFemale);
        //ip
        lblIpAddress.setBounds(10, 240, 100, 20);
        lblBG.add(lblIpAddress);
        txtIpAddress.setBounds(120, 240, 200, 20);
        lblBG.add(txtIpAddress);
        //端口
        lblPort.setBounds(280, 240, 100, 20);
        lblBG.add(lblPort);
        txtPort.setBounds(380, 240, 180, 20);
        lblBG.add(txtPort);
        //备注
        lblRemark.setBounds(10, 300, 100, 20);
        lblBG.add(lblRemark);
        taRemark.setBounds(120, 300, 400, 80);
        lblBG.add(taRemark);
        //头像
        lblHeadImage.setBounds(400, 110, 80, 60);
        lblBG.add(lblHeadImage);
        cbHeadImage.setBounds(480, 110, 80, 65);
        lblBG.add(cbHeadImage);
        //按钮
        btnSave.setBounds(250, 390, 100, 40);
        lblBG.add(btnSave);
        btnSave.addActionListener(this::actionPerformed);


        setSize(600, 500);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);//将窗口置于屏幕中央
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    //监听方法(不懂语法)
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            if ("".equals(txtNickname.getText().trim())) {//判断昵称是否为空
                JOptionPane.showMessageDialog(this, "请输入昵称");//不懂
                return;

            }
            if ("".equals(String.valueOf(txtPassword.getPassword()).trim())) {//判断密码是否为空
                JOptionPane.showMessageDialog(this, "请输入密码");//不懂
                return;

            }
            if (!String.valueOf(txtPassword.getPassword()).trim().equals
                    (String.valueOf(txtConfirm.getPassword()).trim())) {//判断两次密码是否相等
                JOptionPane.showMessageDialog(this, "登录密码与确认密码不一致");//不懂
                return;

            }
            int age = 0;//判断年龄是否输入正确
            String sage = txtAge.getText().trim();
            if (sage.equals("")) {
                JOptionPane.showMessageDialog(this, "请输入年龄");
                txtAge.setText("0");
                return;
            }
            try {
                age = Integer.parseInt(sage);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "请输入正确的年龄（0-150）");
                return;
            }
            if (age < 0 || age > 150) {
                JOptionPane.showMessageDialog(this, "请输入正确的年龄（0-150）");
                return;
            }

            AccountVo accountVo = new AccountVo();
            if (rbMale.isSelected()) {
                accountVo.setSex("男");
            } else {
                accountVo.setSex("女");
            }
            accountVo.setNickname(txtNickname.getText().trim());//去空格
            accountVo.setHeadImage(sHeadImage[cbHeadImage.getSelectedIndex()]);
            accountVo.setAge(age);
            accountVo.setPassword(String.valueOf(txtPassword.getPassword()).trim());//getText已过时
            accountVo.setIpAddress(txtIpAddress.getText().trim());
            accountVo.setRemark(taRemark.getText());
            BaseDAO baseDAO = new BaseDAO();
            //保存信息到数据库中的account表
            accountVo = baseDAO.saveAccount(accountVo);
            //返回文本框信息
            txtQQCode.setText(accountVo.getQqCode() + "");
            txtPort.setText("0");

            JOptionPane.showMessageDialog(this, "注册成功,您的qq号码：" + accountVo.getQqCode());
        }
    }
}


