package com.example.koungho632.newsogongsaapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koungho632.newsogongsaapplication.ListItem.GroupMeetingReviewListItem;
import com.example.koungho632.newsogongsaapplication.ListItem.Noti_listitem;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.View.ViewHolder;

import java.util.ArrayList;

/**
 * Created by koungho632 on 2016. 5. 25..
 */
public class Noti_Adapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Noti_listitem> data = null;
    private int layout;
    private ViewHolder viewHolder;

    private Noti_listitem noti_listitem;
    private ImageView userImg;
    private TextView noti;


    public Noti_Adapter(Context context, int layout, ArrayList<Noti_listitem> data){
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView==null ){
            //null이라면 재활용할 View가 없으므로 새로운 객체 생성
            //View의 모양은 res폴더>>layout폴더>>list.xml 파일로 객체를 생성
            //xml로 선언된 레이아웃(layout)파일을 객체로 부풀려주는 LayoutInflater 객체 활용.

            convertView= inflater.inflate(layout, null);

        }

        userImg=viewHolder.get(convertView, R.id.writer);
        noti=viewHolder.get(convertView,R.id.notification);

        noti_listitem=data.get(position);

        userImg.setImageBitmap(noti_listitem.getUserImg());
        noti.setText(noti_listitem.getNoti());

        return convertView;
    }
}
