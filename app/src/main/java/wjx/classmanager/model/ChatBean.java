package wjx.classmanager.model;

import java.io.Serializable;

/**
 * 作者：国富小哥
 * 日期：2017/11/6
 * Created by Administrator
 */

public class ChatBean implements Serializable{

    /**
     * 用户名
     */
    private String name;
    /**
     * 用户头像
     */
    private byte[] imgByte;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImgByte() {
        return imgByte;
    }

    public void setImgByte(byte[] imgByte) {
        this.imgByte = imgByte;
    }
}
