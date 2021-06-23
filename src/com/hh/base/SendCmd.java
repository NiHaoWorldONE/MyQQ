package com.hh.base;

import com.hh.vo.AccountVo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

/**发送
 * 采用udp协议
 * @author LiZechen
 * @create 2021-06-22 22:49
 */
public class SendCmd {
    public  static void send(SendMsg msg) {
        //定义socket
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        //字节数组输出流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //对象输出流
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //把要发送的数据转换为字节数组
        byte b[] = bos.toByteArray();
        //获取好友的ip地址
        InetAddress inet = null;
        try {
            inet = InetAddress.getByName(msg.friendInfo.getIpAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //获取好友的接收端口
        int port = msg.friendInfo.getPort();
        //生成发送报
        DatagramPacket packet = new DatagramPacket(b,0,b.length,inet,port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }
    //发送广播
    public static void sendAll(ArrayList<AccountVo> allInfo, AccountVo myInfo, int cmd){
        //增强for循环
        for (AccountVo acc : allInfo) {
            //判断不是离线则发送且不发送给自己
            if(!acc.getOnlineStatus().equals(Cmd.STATUS[1]) &&
                acc.getQqCode()!=myInfo.getQqCode()){

                SendMsg msg = new SendMsg();
                msg.cmd = cmd;
                msg.myInfo = myInfo;
                msg.friendInfo = acc;
                send(msg);

            }
        }
    }
}
