package wjx.classmanager.ui.fragment;

import android.support.v4.app.Fragment;

import static wjx.classmanager.model.Constant.FragmentType.CREATE_MANAGE;
import static wjx.classmanager.model.Constant.FragmentType.CREATE_MESSAGE;
import static wjx.classmanager.model.Constant.FragmentType.CREATE_NOTIFY;

/**
 * Created by wjx on 2017/10/3.
 */

public class FragmentFactory {
    /**
     * 获取一个碎片
     * @return
     */
    public static Fragment getFragment(String key){
        switch (key){
            case CREATE_MANAGE:
                return new ManageFragment();
            case CREATE_MESSAGE:
                return new MessageFragment();
            case CREATE_NOTIFY:
                return new MemberFragment();
        }
        return null;
    }
}
