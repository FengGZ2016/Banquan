package wjx.classmanager.ui.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wjx.classmanager.R;
import wjx.classmanager.adapter.ImageAdapter;
import wjx.classmanager.model.FolderBean;
import wjx.classmanager.widget.ImageDirPopupWindow;

import static wjx.classmanager.model.Constant.MyClass.PHOTO_SELECT;

public class ImageLoaderActivity extends BaseActivity {

    private GridView mGridView;
    private ImageAdapter mImageAdapter;
    private RelativeLayout mBottomLayout;

    private List<String> mImages;
    private List<FolderBean> mFolderBeans = new ArrayList<>();

    private TextView mDirName;
    private TextView mDirCount;

    private File mCurrentDir;
    private int mMaxCount;

    private ProgressDialog mProgressDialog;
    private ImageDirPopupWindow mImageDirPopupWindow;

    private TextView mTextTitle;
    private ImageView mImageBack;
    private ImageView mImageAdd;

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mGridView = (GridView) findViewById(R.id.grid_view);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mDirName = (TextView) findViewById(R.id.dir_name);
        mDirCount = (TextView) findViewById(R.id.dir_count);
        mTextTitle = (TextView) findViewById(R.id.back_title);
        mImageBack = (ImageView) findViewById(R.id.back_image);
        mImageAdd = (ImageView) findViewById(R.id.back_add);
        mTextTitle.setText("图片选择器");
    }

    @Override
    public void initListener() {
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mImageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strings =mImageAdapter.getSelectedImage().toArray(new String[mImageAdapter.getSelectedImage().size()]);
                setResult(RESULT_OK,new Intent().putExtra(PHOTO_SELECT,strings));
                finish();
            }
        });

        mBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageDirPopupWindow.setAnimationStyle(R.style.PopupWindowAnim);
                mImageDirPopupWindow.showAsDropDown(mBottomLayout, 0, 0);
                lightOff();
            }
        });
    }

    @Override
    public void initData() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            showToast("当前存储卡不能用");
            return;
        }
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = ImageLoaderActivity.this.getContentResolver();
                Cursor cursor = contentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                Set<String> mDirPaths = new HashSet<String>();

                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;

                    String dirPath = parentFile.getAbsolutePath();
                    FolderBean folderBean = null;

                    //避免重复扫描
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstPath(path);
                    }

                    if (parentFile.list() == null)
                        continue;

                    //得到图片的数量
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"))
                                return true;
                            return false;
                        }
                    }).length;

                    folderBean.setCount(picSize);
                    mFolderBeans.add(folderBean);

                    if (picSize > mMaxCount) {
                        mMaxCount = picSize;
                        mCurrentDir = parentFile;
                    }
                }
                cursor.close();

                //图片扫描完成，发送通知
                mMyHandler.sendEmptyMessage(1001);
            }
        }).start();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_image_loader;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }

    private Handler mMyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
            //绑定数据到View中
            date2View();
            //有数据后可以初始化
            initDirPopupWindow();
        }
    };

    private void date2View() {
        if (mCurrentDir == null) {
            showToast("未扫描到任何图片");
            return;
        }
        mImages = Arrays.asList(mCurrentDir.list());
        mImageAdapter = new ImageAdapter(this, mImages, mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(mImageAdapter);

        mDirCount.setText(mMaxCount + "");
        mDirName.setText(mCurrentDir.getName());
    }

    private void initDirPopupWindow() {
        mImageDirPopupWindow = new ImageDirPopupWindow(this, mFolderBeans);
        mImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        mImageDirPopupWindow.setOnDirSelectedListener(new ImageDirPopupWindow.DirSelectedListener() {
            @Override
            public void onSelected(FolderBean folderBean) {
                mCurrentDir = new File(folderBean.getDir());

                mImages = Arrays.asList(mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"))
                            return true;
                        return false;
                    }
                }));

                mImageAdapter = new ImageAdapter(ImageLoaderActivity.this,mImages,mCurrentDir.getAbsolutePath());
                mGridView.setAdapter(mImageAdapter);

                mDirCount.setText(mImages.size()+"");
                mDirName.setText(folderBean.getName());

                mImageDirPopupWindow.dismiss();
            }
        });
    }

    private void lightOn() {    //内容区域变亮
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    private void lightOff() {    //内容区域变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.8f;
        getWindow().setAttributes(lp);
    }
}
