package com.hh.vo;

import java.io.Serializable;

/**朋友JavaBean
 * @author LiZechen
 * @create 2021-06-22 8:42
 */
public class FriendVo implements Serializable {
    private int friendId;
    private int myQQCode;
    private int friendQQCode;
    private String groupName;

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public int getMyQQCode() {
        return myQQCode;
    }

    public void setMyQQCode(int myQQCode) {
        this.myQQCode = myQQCode;
    }

    public int getFriendQQCode() {
        return friendQQCode;
    }

    public void setFriendQQCode(int friendQQCode) {
        this.friendQQCode = friendQQCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
