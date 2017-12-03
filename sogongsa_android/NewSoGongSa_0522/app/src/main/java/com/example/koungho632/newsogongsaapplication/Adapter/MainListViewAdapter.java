package com.example.koungho632.newsogongsaapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koungho632.newsogongsaapplication.ListItem.MainListItem;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.View.CircularImageView;
import com.example.koungho632.newsogongsaapplication.View.ViewHolder;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by NT-RC510 on 2016-05-05.
 */
public class MainListViewAdapter extends BaseAdapter{
    private Context context = null;
    private LayoutInflater inflater;
    private ArrayList<MainListItem> data = null;
    private int layout;

    // main_listview에 구성된 요소
    // 리스트 뷰에 구성된 아이템들 lv
    private ImageView main_BigImg;
    private ImageView main_SmallImg;
    private TextView main_Text;
    private MainListItem listview;

    private ViewHolder viewHolder=null;

    public MainListViewAdapter(Context context, int layout, ArrayList<MainListItem> data){
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


    /*
    * 이 메소드의 리턴값인 View 가 ListView의 한 항목을 의미
    * 첫번째 파라미터 position : ListView에 놓여질 목록의 위치.
    * 두번째 파라미터 convertview : 리턴될 View로서 List의 한 함목의 View 객체
    * 세번째 파라미터 parent : 이 Adapter 객체가 설정된 AdapterView객체
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        // 1. New View
        if( convertView==null ){
            //null이라면 재활용할 View가 없으므로 새로운 객체 생성
            //View의 모양은 res폴더>>layout폴더>>list.xml 파일로 객체를 생성
            //xml로 선언된 레이아웃(layout)파일을 객체로 부풀려주는 LayoutInflater 객체 활용.

            convertView= inflater.inflate(layout, null);
        }


        //뷰홀더에서 가져옴(있으면)
        main_BigImg = viewHolder.get(convertView,R.id.main_Frame_bigImage);
        main_SmallImg = viewHolder.get(convertView,R.id.main_Frame_smallImage);
        main_Text = viewHolder.get(convertView,R.id.main_Frame_text);


        listview = data.get(position);

        //그리기
        main_BigImg.setImageBitmap(listview.getBigImage());
        main_SmallImg.setImageDrawable(listview.getsmallImage());
        main_Text.setText(listview.getMainText());


        return convertView;
    }
}
