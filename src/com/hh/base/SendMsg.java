package com.hh.base;

import com.hh.vo.AccountVo;

import javax.swing.text.StyledDocument;
import java.io.Serializable;

/**发送数据包
 * @author LiZechen
 * @create 2021-06-22 21:49
 */
public class SendMsg implements Serializable {

    //命令字
    public int cmd;
    //发送人信息
    public AccountVo myInfo;
    //接收人信息
    public AccountVo friendInfo;
    //发送的内容
    public StyledDocument doc;
    //发送文件内容采用udp大小在64k以下
    public byte b[];
    //发送文件名
    public String filename;

}
