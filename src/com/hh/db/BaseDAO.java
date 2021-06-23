package com.hh.db;

import com.hh.base.Cmd;
import com.hh.util.Jdbc;
import com.hh.vo.AccountVo;

import javax.security.auth.login.AccountException;
import java.net.InetAddress;
import java.sql.*;
import java.util.Random;
import java.util.ArrayList;

/**
 * @author LiZechen
 * @create 2021-06-22 13:35
 */
public class BaseDAO {

    //生成qq号码

    public int getQQCode(){
        boolean isExist=false;
        Connection conn = null;
        try {
            conn = Jdbc.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "select qqcode from account where qqcode=?";
        int qqcode = 0;
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            while(!isExist){
                Random rand = new Random();
                qqcode = rand.nextInt(8999)+1000;//生成四位qq号码
                pstmt.setInt(1, qqcode);
                ResultSet rs = pstmt.executeQuery();
                if(!rs.next()){
                    isExist=true;
                }
            }
            pstmt.close();//不懂
        }catch(Exception e){
            e.printStackTrace();
        }
        return qqcode;

    }
    //随机生成端口号（每次登录都重新产生端口）
    public int getPort(){
        boolean isExist=false;
        Connection conn = null;
        try {
            conn = Jdbc.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "select qqcode from account where onlinestatus != ? and qqcode=?";
        int port = 0;
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            while(!isExist){
                Random rand = new Random();
                port = rand.nextInt(64000)+1000;//生成四位qq号码
                pstmt.setString(1, Cmd.STATUS[1]);
                pstmt.setInt(2, port);//不懂
                ResultSet rs = pstmt.executeQuery();
                if(!rs.next()){
                    isExist=true;
                }
            }
            pstmt.close();//不懂
        }catch(Exception e){
            e.printStackTrace();
        }
        return port;

    }
    //保存注册用户信息
    public AccountVo saveAccount (AccountVo account){//不懂
        //获取qq号码
        int qqCode = getQQCode();
        account.setQqCode(qqCode);
        account.setPort(0);
        account.setOnlineStatus(Cmd.STATUS[1]);//默认离线

        String sql = "insert into account values(?,?,?,?,?,?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = Jdbc.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            int i=1;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(i++, account.getQqCode());
            pstmt.setString(i++, account.getNickname());
            pstmt.setString(i++, account.getPassword());
            pstmt.setString(i++, account.getHeadImage());
            pstmt.setInt(i++, account.getAge());
            pstmt.setString(i++, account.getSex());
            pstmt.setString(i++, account.getRemark());
            pstmt.setString(i++, account.getIpAddress());
            pstmt.setInt(i++, account.getPort());
            pstmt.setString(i++, account.getOnlineStatus());

            i=pstmt.executeUpdate();
            pstmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return account;
    }
    //登录
    /*1.获取一个合法的端口
     *2.登录成功后把所有字段的值设置到account对象中，返回给登录界面
     *3.更新端口和状态
     *
     * */
    public AccountVo login(AccountVo account){
        Connection conn = null;
        try {
            conn = Jdbc.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "select * from account where qqcode=? and password=?";
        int qqcode = 0;
        try{
            int port =getPort();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, account.getQqCode());
            pstmt.setString(2, account.getPassword());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                account.setNickname(rs.getString("nickName").trim());
                account.setHeadImage(rs.getString("headImage").trim());
                account.setAge(rs.getInt("age"));
                account.setSex(rs.getString("sex").trim());


                InetAddress address = InetAddress.getLocalHost();//每次登录重新获取本地ip地址
                String ip = address.getHostAddress();
                account.setIpAddress(ip);
                account.setPort(port);
                account.setOnlineStatus(Cmd.STATUS[0]);//在线
                account.setRemark(rs.getString("remark"));
                //更改端口和状态
                sql = "update account set ipaddr='"+ip+"' ,port="+port +",onlinestatus='" + Cmd.STATUS[0] + "' where qqcode="+account.getQqCode();
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                pstmt.close();
                stmt.close();
            }else{
                //登录失败
                account=null;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return account;
    }
    //更改状态
    public void changeStatus(int qqcode,String status){
        String sql ="update account set onlinestatus=? where qqCode=?";
        Connection conn = null;
        try {
            conn = Jdbc.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            int i=1;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(i++, status);
            pstmt.setInt(i++, qqcode);
            i=pstmt.executeUpdate();
            pstmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //查找好友信息
    public ArrayList<AccountVo> getMyFriend(int myQQCode){
        Connection conn = null;
        try {
            conn = Jdbc.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "select a.*,f.groupName from account a join Friends f on a.qqcode=f.friendQQcode where myqqcode=?";
        int qqcode = 0;
        ArrayList<AccountVo> vmyFriend = new ArrayList<AccountVo>();
        try{
            int port =getPort();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, myQQCode);
            ResultSet rs = pstmt.executeQuery();
            //循环语句取出好友信息
            while(rs.next()){
                AccountVo account = new AccountVo();
                account.setQqCode(rs.getInt("qqcode"));
                account.setNickname(rs.getString("nickName").trim());
                account.setHeadImage(rs.getString("headImage").trim());
                account.setAge(rs.getInt("age"));
                account.setSex(rs.getString("sex").trim());
                account.setIpAddress(rs.getString("ipAddr"));
                account.setPort(rs.getInt("port"));
                account.setOnlineStatus(rs.getString("onlinestatus"));//在线
                account.setRemark(rs.getString("remark"));
                account.setGroupName(rs.getString("groupName").trim());
                vmyFriend.add(account);
            }
            pstmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return vmyFriend;

    }
    //查找功能
    public ArrayList<ArrayList> findFriend(final String sql){
        ArrayList<ArrayList> vData = new ArrayList<ArrayList>();
        Connection conn = null;
        try {
            conn = Jdbc.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            int port =getPort();
            Statement pstmt = conn.createStatement();
            ResultSet rs = pstmt.executeQuery(sql);
            while(rs.next()){
                ArrayList rowData = new ArrayList();
                rowData.add(rs.getString("headImage").trim());
                rowData.add(rs.getInt("qqcode"));
                rowData.add(rs.getString("nickName").trim());
                rowData.add(rs.getInt("age"));
                rowData.add(rs.getString("sex").trim());
                rowData.add(rs.getString("onlinestatus").trim());
                vData.add(rowData);
            }
            pstmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return vData;
    }
    //判断是否是好友
    public boolean isFriend(int myQQcode,int friendqqcode){
        String sql = "select * from friends where myqqcode=? and friendqqcode=?";
        boolean isFriend=false;
        AccountVo account = new AccountVo();
        Connection conn = null;
        try {
            conn = Jdbc.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, myQQcode);
            pstmt.setInt(2, friendqqcode);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                isFriend=true;
            }
            pstmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return isFriend;
    }
    //添加好友信息到数据库
    public void addFriend(int myQQCode ,int friendQQCode){
        try {
            Connection conn = Jdbc.getConnection();
            String sql ="insert into friends values(null,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, myQQCode);
            pstmt.setInt(2, friendQQCode);
            pstmt.setString(3, Cmd.GROUP_NAME[1]);
            pstmt.executeUpdate();
            //添加两次，是双向都添加到数据库
            pstmt.setInt(1, friendQQCode);
            pstmt.setInt(2, myQQCode);
            pstmt.setString(3, Cmd.GROUP_NAME[1]);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //从数据库删除好友
    public void deleteFriend(int myQQCode ,int friendQQCode){
        try {
            Connection conn = Jdbc.getConnection();
            String sql ="delete from friends where myqqcode = ? and friendqqcode = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, myQQCode);
            pstmt.setInt(2, friendQQCode);
            pstmt.executeUpdate();
            //删除两次
            pstmt.setInt(1, friendQQCode);
            pstmt.setInt(2, myQQCode);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //获取选中需添加的好友信息
    public AccountVo getSelectedFriend(int myQQCode) {
        Connection conn = null;
        try {
            conn = Jdbc.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "select * from account where qqCode = ?";
        int qqcode = 0;
        AccountVo account = new AccountVo();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, myQQCode);
            ResultSet rs = pstmt.executeQuery();

            //循环语句取出好友信息
            while (rs.next()) {

                account.setQqCode(rs.getInt("qqcode"));
                account.setNickname(rs.getString("nickName").trim());
                account.setHeadImage(rs.getString("headImage").trim());
                account.setAge(rs.getInt("age"));
                account.setSex(rs.getString("sex").trim());
                account.setIpAddress(rs.getString("ipAddr"));
                account.setPort(rs.getInt("port"));
                account.setOnlineStatus(rs.getString("onlinestatus"));//在线
                account.setRemark(rs.getString("remark"));
            }
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }
}
