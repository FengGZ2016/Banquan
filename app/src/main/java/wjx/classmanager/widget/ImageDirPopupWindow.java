package wjx.classmanager.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import wjx.classmanager.R;
import wjx.classmanager.model.FolderBean;
import wjx.classmanager.model.ImageLoader;

/**
 * Created by wjx on 2017/8/12.
 */

public class ImageDirPopupWindow extends PopupWindow {

    private int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;
    private List<FolderBean> mFolderBeen;

    public ImageDirPopupWindow(Context context, List<FolderBean> folderBeen) {
        calWidthAndHeight(context);
        mConvertView = LayoutInflater.from(context).inflate(R.layout.popup_main, null);
        mFolderBeen = folderBeen;

        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    //点击外面退出popupWindow
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        initView(context);
        initEvent();
    }

    private void initView(Context context) {
        mListView = (ListView) mConvertView.findViewById(R.id.popup_listview);
        mListView.setAdapter(new DirAdapter(context, mFolderBeen));
    }

    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mDirSelectedListener != null) {
                    mDirSelectedListener.onSelected(mFolderBeen.get(position));
                }
            }
        });
    }

    /**
     * 计算popupwindow的宽高
     *
     * @param context
     */
    private void calWidthAndHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mWidth = displayMetrics.widthPixels;
        mHeight = (int) (displayMetrics.heightPixels * 0.7f);
    }

    private class DirAdapter extends ArrayAdapter<FolderBean> {

        private LayoutInflater mInflater;
        private List<FolderBean> mDatas;

        public DirAdapter(@NonNull Context context, @NonNull List<FolderBean> objects) {
            super(context, 0, objects);
            mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.popup_item, parent, false);
                holder = new ViewHolder();
                holder.mImageView = (ImageView) convertView.findViewById(R.id.popup_image);
                holder.mDirName = (TextView) convertView.findViewById(R.id.popup_name);
                holder.mDirCount = (TextView) convertView.findViewById(R.id.popup_count);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            FolderBean folderBean = getItem(position);
            holder.mImageView.setImageResource(R.drawable.prepare_load); //重置
            ImageLoader.getInstance().loadImage(folderBean.getFirstPath(), holder.mImageView);
            holder.mDirName.setText(folderBean.getName());
            holder.mDirCount.setText(folderBean.getCount() + "");

            return convertView;
        }

        private class ViewHolder {
            ImageView mImageView;
            TextView mDirName;
            TextView mDirCount;
        }
    }

    public DirSelectedListener mDirSelectedListener;

    public interface DirSelectedListener {
        void onSelected(FolderBean folderBean);
    }

    public void setOnDirSelectedListener(DirSelectedListener listener) {
        mDirSelectedListener = listener;
    }
}
