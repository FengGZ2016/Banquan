package wjx.classmanager.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import wjx.classmanager.R;
import wjx.classmanager.adapter.ClassPhotoAdapter;
import wjx.classmanager.model.ClassPhoto;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.presenter.impl.ClassPhotoPresenterImpl;
import wjx.classmanager.view.ClassPhotoView;

import static wjx.classmanager.model.Constant.MyClass.CLASS_ALBUM;
import static wjx.classmanager.model.Constant.MyClass.CLASS_OPEN_CAMERA;
import static wjx.classmanager.model.Constant.MyClass.PHOTO_SELECT;
import static wjx.classmanager.model.Constant.MyClass.PHOTO_SELECTER;


public class ClassPhotoActivity extends BaseActivity implements View.OnClickListener,ClassPhotoView{

    private Button mTakePhoto;
    private Button mAlbumPhoto;
    private Button mPostPhoto;
    private ImageView mImageBack;
    private ImageView mImageViewAdd;
    private TextView mTextTitle;
    private RecyclerView mRecyclerView;
    private ClassPhotoAdapter mClassPhotoAdapter;
    private ClassUser mClassUser=BmobUser.getCurrentUser(ClassUser.class);
    private List<ClassPhoto> mClassPhotos;
    private ArrayList<String> photoStrs=new ArrayList<>();
    private ProgressDialog downPd;
    private ProgressDialog uploadPd;
    private Handler mHandler;
    private ClassPhotoPresenterImpl mClassPhotoPresenter;
    private File mOutputImage;
    private Uri imageUri;

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        initHandler();
        mClassPhotos=new ArrayList<>();
        mClassPhotoPresenter = new ClassPhotoPresenterImpl(this);
        mImageBack = (ImageView) findViewById(R.id.back_image);
        mImageViewAdd= (ImageView) findViewById(R.id.back_add);
        mImageViewAdd.setVisibility(View.GONE);
        mTextTitle = (TextView) findViewById(R.id.back_title);
        mTextTitle.setText("班级相册");
        mTakePhoto = (Button) findViewById(R.id.take_photo);
        mAlbumPhoto = (Button) findViewById(R.id.album_photo);
        mPostPhoto = (Button) findViewById(R.id.post_photo);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_photo);
        GridLayoutManager manager = new GridLayoutManager(mContext,3);
        mRecyclerView.setLayoutManager(manager);
        mClassPhotoAdapter = new ClassPhotoAdapter(mClassPhotos,photoStrs);
        mRecyclerView.setAdapter(mClassPhotoAdapter);
        
    }

    private void initHandler() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1001) {
                    //数据发生变化
                    for (int i=0;i<mClassPhotos.size();i++){
                        photoStrs.add(mClassPhotos.get(i).getPhotoPath());
                    }
                    mClassPhotoAdapter.refresh(mClassPhotos,photoStrs);
                }
                return false;
            }
        });
    }

    @Override
    public void initListener() {
        mTakePhoto.setOnClickListener(this);
        mAlbumPhoto.setOnClickListener(this);
        mPostPhoto.setOnClickListener(this);
        mImageBack.setOnClickListener(this);

    }

    @Override
    public void initData() {
        getBmobData();
    }

    /**
     * 获取比目上的网络图片
     */
    private void getBmobData() {
        downPd = new ProgressDialog(this);
        downPd.setMessage("loading...");
        downPd.setCanceledOnTouchOutside(false);
        downPd.show();

        mClassPhotoPresenter.getBmobData();

//
//        BmobQuery<ClassPhoto> bmobQuery=new BmobQuery<>();
//        bmobQuery.setLimit(500);
//        bmobQuery.addWhereEqualTo("classId",mClassUser.getGroupId());
//        bmobQuery.findObjects(new FindListener<ClassPhoto>() {
//            @Override
//            public void done(List<ClassPhoto> list, BmobException e) {
//                if (e==null){
//                    downPd.dismiss();
//                    mClassPhotos=list;
//                    //刷新
//                    mHandler.sendEmptyMessage(1001);
//                }else {
//                    downPd.dismiss();
//                    Toast.makeText(mContext, "获取图片数据失败！", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

    @Override
    public int getLayout() {
        return R.layout.activity_class_photo;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.take_photo:
                //拍照
                openCamera();
                break;
            case R.id.album_photo:
                //图库
                openAlbum();
                break;

            case R.id.post_photo:
                //选择器
                Intent intent = new Intent(ClassPhotoActivity.this,ImageLoaderActivity.class);
                startActivityForResult(intent,PHOTO_SELECTER);
                break;
            case R.id.back_image:
                finish();
                break;

        }

    }

    /**
     * 打开相机
     */
    private void openCamera() {

        //创建File对象，用于存储拍照后的图片
        mOutputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (mOutputImage.exists()) {
                mOutputImage.delete();
            }
            mOutputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(ClassPhotoActivity.this, "com.example.cameraalbumtest.fileprovider", mOutputImage);
        } else {
            imageUri = Uri.fromFile(mOutputImage);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CLASS_OPEN_CAMERA);
    }

    /**
     * 打开图库
     */
    private void openAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CLASS_ALBUM);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CLASS_OPEN_CAMERA:
                    //相机
                    mClassPhotoPresenter.handleCameraImage(this,imageUri);
                    break;
                case CLASS_ALBUM:
                    //图库
                    mClassPhotoPresenter.handleAlbumImage(ClassPhotoActivity.this,data);
                    break;
                case PHOTO_SELECTER:
                    //选择器
                    mClassPhotoPresenter.handleSelectorImage(data.getStringArrayExtra(PHOTO_SELECT));
                    break;
            }
        }

    }



    /**
     * 上传失败
     */
    @Override
    public void onPicPostFailed() {
        uploadPd.dismiss();
        Toast.makeText(mContext, "上传失败！", Toast.LENGTH_SHORT).show();
    }

    /**
     *上传单张图片成功
     */
    @Override
    public void onPicPostSuccess() {
        uploadPd.dismiss();
        Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
        getBmobData();
    }

    /**
     * 正在上传图片
     */
    @Override
    public void onStartLoadPic() {
        uploadPd=new ProgressDialog(this);
        uploadPd.setMessage("uploading...");
        uploadPd.setCanceledOnTouchOutside(false);
        uploadPd.show();

    }

    @Override
    public void getBmobDataFail() {
        downPd.dismiss();
        Toast.makeText(mContext, "获取图片数据失败！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getBmobDataSuccess(List<ClassPhoto> list) {
        downPd.dismiss();
        mClassPhotos=list;
        //刷新
        mHandler.sendEmptyMessage(1001);
    }


}
