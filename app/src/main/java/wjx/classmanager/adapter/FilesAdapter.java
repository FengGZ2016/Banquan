package wjx.classmanager.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.util.TextFormater;

import java.util.Date;
import java.util.List;

import wjx.classmanager.R;

/**
 * 作者：国富小哥
 * 日期：2017/10/15
 * Created by Administrator
 * 班级资料页面ListView的适配器
 */

public class FilesAdapter extends ArrayAdapter<EMMucSharedFile>{

    private Context mContext;
    private int resourceId;
    List<EMMucSharedFile> list;


    public FilesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<EMMucSharedFile> objects) {
        super(context, resource, objects);
        mContext=context;
        resourceId=resource;
        list=objects;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EMMucSharedFile file=getItem(position);
        final ViewHolder holder;
        View view;
        if (convertView==null){
            view= LayoutInflater.from(mContext).inflate(resourceId,parent,false);
            holder=new ViewHolder();
            holder.tv_file_name= (TextView) view.findViewById(R.id.tv_file_name);
            holder.tv_update_time= (TextView) view.findViewById(R.id.tv_update_time);
            holder.tv_file_size= (TextView) view.findViewById(R.id.tv_file_size);
            view.setTag(holder);
        }else {
            view=convertView;
            holder= (ViewHolder) view.getTag();
        }
        holder.tv_file_name.setText(file.getFileName());
        holder.tv_update_time.setText(new Date(file.getFileUpdateTime()).toString());
        holder.tv_file_size.setText(TextFormater.getDataSize(file.getFileSize()));

        return view;
    }


    private static class ViewHolder {
        TextView tv_file_name;
        TextView tv_file_size;
        TextView tv_update_time;
    }
}
