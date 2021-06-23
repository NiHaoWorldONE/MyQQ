package com.hh.ui;

import com.hh.db.BaseDAO;
import com.hh.vo.AccountVo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author LiZechen
 * @create 2021-06-22 15:25
 */
public class Login extends JFrame implements MouseListener,ActionListener {
    private JLabel lblQQCode,lblPassword, lblReg,lblBG;
    JButton btnLogin;

    private JTextField txtQQCode;
    private JPasswordField txtPassword;

    public Login() throws HeadlessException {
        super("MyQQ登录");
        lblBG = new JLabel(new ImageIcon("images/bglogin.jpg"));
        add(lblBG);
        lblBG.setLayout(null);
        //监听窗口事件



        JLabel title = new JLabel("用户登录",JLabel.CENTER);
        title.setFont(new Font("黑体",Font.BOLD,48));
        title.setBounds(215,15,200,50);
        lblBG.add(title);
        lblQQCode = new JLabel("QQ号码：",JLabel.RIGHT);
        lblPassword = new JLabel("登录密码：",JLabel.RIGHT);


        txtQQCode = new JTextField(10);
        txtPassword = new JPasswordField(10);
        txtPassword.setEchoChar('*');
        lblReg = new JLabel("注册账号");
        lblReg.setForeground(new Color(68, 35, 127));
        lblReg.setFont(new Font("黑体",Font.BOLD,15));
        lblReg.addMouseListener(this);
        btnLogin = new JButton("登录");
        btnLogin.addActionListener(this);

        //qq号码
        lblQQCode.setBounds(100,120,100,20);
        lblBG.add(lblQQCode);
        txtQQCode.setBounds(200,120,200,20);
        lblBG.add(txtQQCode);
        //密码
        lblPassword.setBounds(100,180,100,20);
        lblBG.add(lblPassword);
        txtPassword.setBounds(200,180,200,20);
        lblBG.add(txtPassword);
        lblReg.setBounds(460, 120, 80, 30);
        lblBG.add(lblReg);
        btnLogin.setBounds(220, 250, 160, 30);
        lblBG.add(btnLogin);

        setSize(600,400);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);//将窗口置于屏幕中央
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == lblReg){
            new Register();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            String pwd = String.valueOf(txtPassword.getPassword()).trim();
            if (txtQQCode.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "请输入QQ号码");
                return;
            }
            if (pwd.equals("")) {
                JOptionPane.showMessageDialog(this, "请输入登录密码");
                return;
            }
            String qqcode = txtQQCode.getText().trim();

            BaseDAO baseDAO = new BaseDAO();
            AccountVo account = new AccountVo();
            account.setQqCode(Integer.parseInt(qqcode));
            account.setPassword(pwd);
            account = baseDAO.login(account);

            if(account==null){
                JOptionPane.showMessageDialog(this, "登录失败，用户名或密码错误!");
                return;
            }else{
				JOptionPane.showMessageDialog(this, "登录成功");


                //登录成功后，关闭当前窗口，
                dispose();
                //显示主窗口,同时把登录成功的个人信息account对象，传递到主窗口
                new Main(account);
            }


        }
    }
}

