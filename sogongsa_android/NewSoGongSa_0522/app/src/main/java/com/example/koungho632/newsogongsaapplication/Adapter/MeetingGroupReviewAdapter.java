package com.example.koungho632.newsogongsaapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koungho632.newsogongsaapplication.ListItem.GroupMeetingReviewListItem;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.View.ViewHolder;
import com.example.koungho632.newsogongsaapplication.Dialog.review_reple_view;

import java.util.ArrayList;

/**
 * Created by NT-RC510 on 2016-05-13.
 */
public class MeetingGroupReviewAdapter extends BaseAdapter {
    private Context context = null;
    private LayoutInflater inflater;
    private ArrayList<GroupMeetingReviewListItem> data = null;
    private int layout;
    private ViewHolder viewHolder;

    // 리스트 뷰에 구성된 아이템들 lv;
    private ImageView userProfile = null;   //  유저 프로필 사진
    private TextView userName = null;               //  유저 이름
    private TextView reviewDate = null;             //  유저가 올린 게시글의 날짜
    private TextView reviewContent = null;          //  유저가 올린 게시글의 내용
    private ImageView userPicture = null;           //  유저가 올린 게시글의 사진
    private ImageButton repleBtn = null;            //  게시글의 사진내에 이미지 버튼(리플)

    private GroupMeetingReviewListItem reviewListView = null;

    public MeetingGroupReviewAdapter(Context context, int layout, ArrayList<GroupMeetingReviewListItem> data){
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
        // 1. New View
        if( convertView==null ){
            //null이라면 재활용할 View가 없으므로 새로운 객체 생성
            //View의 모양은 res폴더>>layout폴더>>list.xml 파일로 객체를 생성
            //xml로 선언된 레이아웃(layout)파일을 객체로 부풀려주는 LayoutInflater 객체 활용.

            convertView= inflater.inflate(layout, parent,false);
        }

        userProfile = viewHolder.get(convertView, R.id.review_userProfile);
        userName = viewHolder.get(convertView, R.id.review_userName);
        reviewDate = viewHolder.get(convertView, R.id.review_date);
        reviewContent = viewHolder.get(convertView, R.id.review_userContent);
        userPicture = viewHolder.get(convertView, R.id.review_userPicture);
        repleBtn = viewHolder.get(convertView, R.id.review_reple);


        reviewListView = data.get(position);

        userProfile.setImageBitmap(reviewListView.getUserProfile());
        userName.setText(reviewListView.getUserName());
        reviewDate.setText(reviewListView.getReviewDate());
        reviewContent.setText(reviewListView.getReviewContent());
        userPicture.setImageBitmap(reviewListView.getUserPicture());
        repleBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.image_reple));
        repleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review_reple_view reple=new review_reple_view(context,R.style.Theme_Dialog,reviewListView.getId());
                reple.show();
            }
        });

        return convertView;
    }
}
