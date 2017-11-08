package wjx.classmanager.presenter.impl;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;
import wjx.classmanager.model.ClassPhoto;
import wjx.classmanager.model.ClassUser;
import wjx.classmanager.presenter.ClassPhotoPresenter;
import wjx.classmanager.utils.FileUtil;
import wjx.classmanager.view.ClassPhotoView;

/**
 * Created by wjx on 2017/10/18.
 */

public class ClassPhotoPresenterImpl implements ClassPhotoPresenter {

    private ClassPhotoView mClassPhotoView;
    private ClassUser mClassUser=BmobUser.getCurrentUser(ClassUser.class);

    public ClassPhotoPresenterImpl(ClassPhotoView classPhotoView){
        mClassPhotoView=classPhotoView;
    }

    /**
     * 上传单一图片
     * @param path
     */
    private void postPicToBmob(String path) {

        if (!TextUtils.isEmpty(path) ){
            final BmobFile bmobFile = new BmobFile(new File(path));

            bmobFile.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e==null){
                        //上传成功
                        ClassPhoto photo=new ClassPhoto();
                        photo.setPhotoPath(bmobFile.getFileUrl());
                        photo.setClassId(mClassUser.getGroupId());
                        photo.setSourse(BmobUser.getCurrentUser().getUsername());
                        photo.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null){
                                    Log.i("bmob","保存成功");
                                    mClassPhotoView.onPicPostSuccess();
                                }else{
                                    Log.i("bmob","保存失败："+e.getMessage());
                                    mClassPhotoView.onPicPostFailed();
                                }

                            }
                        });

                    }   else {
                        //上传失败
                        mClassPhotoView.onPicPostFailed();
                    }
                }
            });
        }

//        final BmobFile bmobFile = new BmobFile(new File(path));
//        bmobFile.uploadblock(new UploadFileListener() {
//
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    saveInBmobPhoto(bmobFile.getUrl());
//                }else{
//                    mClassPhotoView.onPicPostFailed(e.getMessage());
//                }
//            }
//
//            @Override
//            public void onProgress(Integer value) {
//                // 返回的上传进度（百分比）
//            }
//        });


    }



    /**
     * 批量上传图片
     * @param filePaths
     */
    private void postPicToBmob(String[] filePaths){

        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(final List<BmobFile> files, List<String> urls) {


                List<BmobObject> classPhotos = new ArrayList<BmobObject>();
                for (int i = 0; i < files.size(); i++) {
                  ClassPhoto classPhoto=new ClassPhoto();
                    classPhoto.setPhotoPath(files.get(i).getFileUrl());
                    classPhoto.setClassId(mClassUser.getGroupId());
                    classPhoto.setSourse(BmobUser.getCurrentUser().getUsername());
                    classPhotos.add(classPhoto);

                }
//第一种方式:v3.5.0之前的版本
               new BmobBatch().insertBatch(classPhotos).doBatch(new QueryListListener<BatchResult>() {
                   @Override
                   public void done(List<BatchResult> list, BmobException e) {
                       if(e==null){
                           for(int i=0;i<list.size();i++){
                               BatchResult result = list.get(i);
                               BmobException ex =result.getError();
                               if(ex==null){
                                   Log.d("批量添加","第"+i+"个数据批量添加成功："+result.getCreatedAt()+","+result.getObjectId()+","+result.getUpdatedAt());
                                  //log("第"+i+"个数据批量添加成功："+result.getCreatedAt()+","+result.getObjectId()+","+result.getUpdatedAt());
                               }else{
                                   Log.d("批量添加","第"+i+"个数据批量添加失败："+ex.getMessage()+","+ex.getErrorCode());
                                   //log("第"+i+"个数据批量添加失败："+ex.getMessage()+","+ex.getErrorCode());
                               }
                           }
                           mClassPhotoView.onPicPostSuccess();
                       }else{
                           Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                           mClassPhotoView.onPicPostFailed();
                       }
                   }
               });
//
                
                Log.e( "onSuccess: ","图片上传成功" +files.size()+"_+_+"+urls.size());
                mClassPhotoView.onPicPostSuccess();
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                Log.e( "onProgress: ",totalPercent+"" );
            }

            @Override
            public void onError(int stateCode, String errormsg) {
                Log.e( "onError: ", errormsg);
                mClassPhotoView.onPicPostFailed();
            }
        });
    }



    /**
     * 处理图库返回的图片
     * @param activity
     * @param data
     */
    @Override
    public void handleAlbumImage(Activity activity, Intent data) {
        if (data != null) {
            mClassPhotoView.onStartLoadPic();
            Uri uri = data.getData();
           String imageUrl = FileUtil.getPath(activity, uri);
           postPicToBmob(imageUrl);


//            if (!TextUtils.isEmpty(imageUrl) ){
//                final BmobFile bmobFile = new BmobFile(new File(imageUrl));
//
//                bmobFile.upload(new UploadFileListener() {
//                    @Override
//                    public void done(BmobException e) {
//                        if (e==null){
//                            //上传成功
//                            ClassPhoto photo=new ClassPhoto();
//                            photo.setPhotoPath(bmobFile.getFileUrl());
//                            photo.setClassId(mClassUser.getGroupId());
//                            photo.setSourse(BmobUser.getCurrentUser().getUsername());
//                            photo.save(new SaveListener<String>() {
//                                @Override
//                                public void done(String s, BmobException e) {
//                                    if(e==null){
//                                        Log.i("bmob","保存成功");
//                                        mClassPhotoView.onPicPostSuccess();
//                                    }else{
//                                        Log.i("bmob","保存失败："+e.getMessage());
//                                        mClassPhotoView.onPicPostFailed(e.getMessage());
//                                    }
//
//                                }
//                            });
//
//                        }   else {
//                            //上传失败
//                            mClassPhotoView.onPicPostFailed(e.getMessage());
//                        }
//                    }
//                });
//            }
        }

    }

    @Override
    public void handleCameraImage(Activity activity, Uri uri) {
        mClassPhotoView.onStartLoadPic();
        String imageUrl = FileUtil.getPath(activity, uri);
        postPicToBmob(imageUrl);
        //postPicToBmob(getCameraImagePath(activity,imageUri));

    }


    /**
     * 处理选择器返回的图片
     * @param stringExtra
     */
    @Override
    public void handleSelectorImage(String[] stringExtra) {
        mClassPhotoView.onStartLoadPic();
        postPicToBmob(stringExtra);
    }

    @Override
    public void getBmobData() {
        BmobQuery<ClassPhoto> bmobQuery=new BmobQuery<>();
        bmobQuery.setLimit(500);
        bmobQuery.addWhereEqualTo("classId",mClassUser.getGroupId());
        bmobQuery.findObjects(new FindListener<ClassPhoto>() {
            @Override
            public void done(List<ClassPhoto> list, BmobException e) {
                if (e==null){
                    mClassPhotoView.getBmobDataSuccess(list);
                }else {
                    mClassPhotoView.getBmobDataFail();
                }
            }
        });

    }

//    @Override
//    public void updatePhotoList(String obejctId) {
//        BmobQuery<BmobPhoto> query = new BmobQuery<BmobPhoto>();
//        query.getObject(obejctId, new QueryListener<BmobPhoto>() {
//
//            @Override
//            public void done(BmobPhoto object, BmobException e) {
//                if(e==null){
//                    BmobPhoto photo = new BmobPhoto();
//                    photo.setUser(object.getUser());
//                    photo.setUrl(object.getUrl());
//                    Log.e( "done: ", object.getUrl());
//                    photo.setDate(object.getCreatedAt());
//                    mClassPhotoView.onUpdatePhotoSuccess(photo);
//                }else{
//                    mClassPhotoView.onUpdatePhotoFailed(e.getMessage());
//                }
//            }
//        });
//    }

    /**
     * 4.4以上系统使用这个方法处理图片
     * @param activity
     * @param data
     */
//    private String handleImageOnKitKat(Activity activity,Intent data) {
//        String imagepath = null;
//        Uri uri = data.getData();
//        if (DocumentsContract.isDocumentUri(activity, uri)) {
//            //如果是documents类型的Uri,则通过Document id 处理
//            String docId = DocumentsContract.getDocumentId(uri);
//            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
//                String id = docId.split(":")[1];//解析出数字格式的ID
//                String selection = MediaStore.Images.Media._ID + "=" + id;
//                imagepath = getAlbumImagePath(activity,MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
//            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
//                imagepath = getAlbumImagePath(activity,contentUri, null);
//            }
//        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            //如果是content类型的Uri,则使用普通方式处理
//            imagepath = getAlbumImagePath(activity,uri, null);
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            //如果是file类型的Uri,直接获取图片路径即可
//            imagepath = uri.getPath();
//        }
//        return imagepath;
//    }

    /**
     * 4.4以下系统使用这个方法处理图片
     * @param data
     */
//    private String handleImageBeforeKitKat(Activity activity,Intent data) {
//        Uri uri = data.getData();
//        String imagePath = getAlbumImagePath(activity,uri, null);
//        return imagePath;
//    }

    /**
     * 获取相册的图片路径
     * @param activity
     * @param uri
     * @param selection
     * @return
     */
    private String getAlbumImagePath(Activity activity,Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = activity.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 获取相机拍照的图片路径
     * @param context
     * @param uri
     * @return
     */
    private String getCameraImagePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
