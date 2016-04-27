package com.me.wish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhangyy on 2016/4/27.
 */
public class AchieveAdapter extends ArrayAdapter<Achieve>{
    private int resourceID;

    public AchieveAdapter(Context context,int textViewResourceID,List<Achieve> objects)
    {
        super(context,textViewResourceID,objects);
        resourceID=textViewResourceID;
    }

    public View getView(int position,View convertView,ViewGroup parent)
    {
        Achieve achieve=getItem(position);//获取当前Achieve数据
        View view;
        ViewHolder viewHolder;
        if(convertView==null)
        {
            view= LayoutInflater.from(getContext()).inflate(resourceID,null);
            viewHolder=new ViewHolder();
            viewHolder.achieveImage=(ImageView) view.findViewById(R.id.achieve_image);
            viewHolder.achieveText=(TextView)view.findViewById(R.id.achieve_text);
            view.setTag(viewHolder);
        }
        else
        {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.achieveImage.setImageResource(achieve.getImageID());
        viewHolder.achieveText.setText(achieve.getName());
        return view;
    }

    class ViewHolder{
        ImageView achieveImage;
        TextView achieveText;
    }
}
