package wjx.classmanager.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;

/**
 * 作者：国富小哥
 * 日期：2017/10/16
 * Created by Administrator
 */

public class FileUtil {


    /**
     * 获取文件的路径
     * @param uri
     * @param mContext
     * @return
     */
    public static String getFilePath(Uri uri,Context mContext) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;

            try {
                cursor = mContext.getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        if (filePath == null) {
            return null;
        }
        return filePath;
    }

    /**
     * android获取一个用于打开Word文件的intent  
     * @param localFile
     * @return
     */
    public static Intent getWordFileIntent(File localFile){
        Intent intent=new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri=Uri.fromFile(localFile);
        intent.setDataAndType(uri,"application/msword");
        return intent;
    }

    /**
     * android获取一个用于打开Excel文件的intent  
     * @param localFile
     * @return
     */
    public static Intent getExcelFileIntent(File localFile){
        Intent intent=new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri=Uri.fromFile(localFile);
        intent.setDataAndType(uri,"application/vnd.ms-excel");
        return intent;
    }


    /**
     * android获取一个用于打开PPT文件的intent  
     * @param localFile
     * @return
     */
    public static Intent getPPTFileIntent(File localFile){
        Intent intent=new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri=Uri.fromFile(localFile);
        intent.setDataAndType(uri,"application/vnd.ms-powerpoint");
        return intent;
    }


    /**
     * android获取一个用于打开apk文件的intent  
     * @param localFile
     * @return
     */
    public static Intent getApkFileIntent(File localFile){
        Intent intent=new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri=Uri.fromFile(localFile);
        intent.setDataAndType(uri,"application/vnd.android.packge-archive");
        return intent;
    }


    /**
     * android获取一个用于打开CHM文件的intent  
     * @param localFile
     * @return
     */
    public static Intent getChmFileIntent(File localFile){
        Intent intent=new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri=Uri.fromFile(localFile);
        intent.setDataAndType(uri,"application/x-chm");
        return intent;
    }


    /**
     * android获取一个用于打开视频文件的intent  
     * @param localFile
     * @return
     */
    public static Intent getVideoFileIntent(File localFile){
        Intent intent=new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot",0);
        intent.putExtra("configchange",0);
        Uri uri=Uri.fromFile(localFile);
        intent.setDataAndType(uri,"video/*");
        return intent;
    }


    /**
     * android获取一个用于打开音频文件的intent  
     * @param localFile
     * @return
     */
    public static Intent getAudioFileIntent(File localFile){
        Intent intent=new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot",0);
        intent.putExtra("configchange",0);
        Uri uri=Uri.fromFile(localFile);
        intent.setDataAndType(uri,"audio/*");
        return intent;
    }


    /**
     * android获取一个用于打开文本文件的intent  
     * @param localFile
     * @return
     */
    public static Intent getTextFileIntent(File localFile){
        Intent intent=new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri=Uri.fromFile(localFile);
        intent.setDataAndType(uri,"text/plain");
        return intent;
    }


    /**
     * android获取一个用于打开PDF文件的intent  
     * @param localFile
     * @return
     */
    public static Intent getPdfFileIntent(File localFile){
        Intent intent=new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri=Uri.fromFile(localFile);
        intent.setDataAndType(uri,"application/pdf");
        return intent;
    }


    /**
     * android获取一个用于打开图片文件的intent  
     * @param localFile
     * @return
     */
    public static Intent getImageFileIntent(File localFile){
        Intent intent=new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri=Uri.fromFile(localFile);
        intent.setDataAndType(uri,"image/*");
        return intent;
    }


    /**
     * android获取一个用于打开Html文件的intent
     * @param localFile
     * @return
     */
    public static Intent getHtmlFileIntent(File localFile){

        Uri uri=Uri.parse(localFile.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(localFile.toString()).build();
        Intent intent=new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri,"text/html");
        return intent;
    }


    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
