package com.example.koungho632.newsogongsaapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koungho632.newsogongsaapplication.ListItem.Meeting_ListItem;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.View.ViewHolder;

import java.util.ArrayList;

/**
 * Created by koungho632 on 2016. 5. 4..
 */
public class Group_ListViewAdapter extends BaseAdapter{

    private Context context = null;
    private LayoutInflater inflater;
    private ArrayList<Meeting_ListItem> data = null;
    private int layout;

    // custom_listview의 텍스트 뷰 값을 얻어오기 위한 변수들
    // 리스트 뷰에 구성된 아이템들 ml
    private ImageView image;
    private TextView title_text;
    private TextView person_text;
    private TextView date_text;
    private TextView personNum_text;
    private ImageView icon;
    private Meeting_ListItem meeting_listItem;

    public Group_ListViewAdapter(Context context, int layout, ArrayList<Meeting_ListItem> data){
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    private ViewHolder viewHolder=null;

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Meeting_ListItem getItem(int position) {
        return data.get(position);
    }

    //id 어카지
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView= inflater.inflate(layout, null);
        }

        //뷰홀더에서 가져옴(있으면)
        image=viewHolder.get(convertView,R.id.meeting_image_id);
        title_text=viewHolder.get(convertView,R.id.meeting_title_id);
        person_text=viewHolder.get(convertView,R.id.meeting_id);
        personNum_text=viewHolder.get(convertView,R.id.meeting_personNum_id);
        icon=viewHolder.get(convertView,R.id.crown);

        meeting_listItem = data.get(position);

        //그리기
        image.setImageDrawable(meeting_listItem.getImage());
        title_text.setText(meeting_listItem.getTitle());
        person_text.setText(meeting_listItem.getPerson());
        personNum_text.setText(meeting_listItem.getPersonNum());

        if(Integer.parseInt(meeting_listItem.getPersonNum())<10)
            icon.setVisibility(View.INVISIBLE);
        else {
            icon.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
