package com.hh.vo;

/**离线消息JavaBean
 * @author LiZechen
 * @create 2021-06-22 8:47
 */
public class OffineMsgVo {

    private int msgId;
    private int myQQCode;
    private int friendQQcode;
    private int cmd;
    private String msg;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getMyQQCode() {
        return myQQCode;
    }

    public void setMyQQCode(int myQQCode) {
        this.myQQCode = myQQCode;
    }

    public int getFriendQQcode() {
        return friendQQcode;
    }

    public void setFriendQQcode(int friendQQcode) {
        this.friendQQcode = friendQQcode;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
