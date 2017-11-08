package wjx.classmanager.utils;

/**
 * 作者：国富小哥
 * 日期：2017/10/22
 * Created by Administrator
 */

public class DownUtil {

    /**
     * 截取字符串，获取图片名
     * @param imageHeaderUri
     * @return
     */
    public static String getImageNameToDate(String imageHeaderUri) {
        String str=imageHeaderUri.substring(imageHeaderUri.lastIndexOf("/")+1,imageHeaderUri.length());
        return str;
    }
}
