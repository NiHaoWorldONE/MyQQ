package com.hh.base;

/**命令字接口
 * @author LiZechen
 * @create 2021-06-22 9:05
 */
public interface Cmd {
    int CMD_ONLINE = 1000;//在线
    int CMD_OFFLINE = 1001;//离线

    int CMD_SEND = 1004;//发送消息
    int CMD_FILE = 1005;//发送文件
    int CMD_ADD = 1006;//添加好友

    int CMD_AGREE = 1008;//同意
    int CMD_REFUSE = 1009;//拒绝

    int CMD_DELETE = 1010;//删除好友

    String[] STATUS = {"在线","离线","忙碌","隐身"};//在线状态 现在只有两种状态，其他后续添加
    String[] GROUP_NAME = {"同学","好友","家人","黑名单"};

}
