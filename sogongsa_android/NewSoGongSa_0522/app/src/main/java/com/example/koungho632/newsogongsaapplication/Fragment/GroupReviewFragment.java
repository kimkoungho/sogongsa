package com.example.koungho632.newsogongsaapplication.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.koungho632.newsogongsaapplication.Adapter.MeetingGroupReviewAdapter;
import com.example.koungho632.newsogongsaapplication.ListItem.GroupMeetingReviewListItem;
import com.example.koungho632.newsogongsaapplication.Page.MeetingGroup;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Dialog.together_review;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupReviewFragment extends Fragment{


    public MeetingGroupReviewAdapter reviewAdapter = null;
    public ArrayList<GroupMeetingReviewListItem> reviewListItems = null;
    private ListView listview;

    public FloatingActionButton fab_Review = null;
    private together_review trDialog = null;

    String uId;
    String gId;

    MeetingGroup group;

    public GroupReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meeting_fragment_review, container, false);

        group=(MeetingGroup)getActivity();

        reviewListItems = new ArrayList<GroupMeetingReviewListItem>();

        // 임시로 유저의 프로필과, 임시로 게시글 사진 값을 받아온다.

        listview = (ListView) view.findViewById(R.id.review_listview);

        //  어댑터에 추가된 리스트들을 넣고 레이아웃도 포함시켜준다.
        //  그 후 리스트 뷰에 setAdapter를 통해 적용
        //  setAdapter 메소드 재정의 ( 다른 디렉토리.. )
        reviewAdapter = new MeetingGroupReviewAdapter(getContext(), R.layout.activity_meeting_group_review_listview, reviewListItems);
        listview.setAdapter(reviewAdapter);

        fab_Review = (FloatingActionButton) view.findViewById(R.id.fab_addReview);
        fab_Review.setOnClickListener(review_Listener);

        return view;
    }

    protected  FloatingActionButton.OnClickListener review_Listener = new FloatingActionButton.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getContext(),together_review.class);
            intent.putExtra("uId",group.uId);
            intent.putExtra("gId",group.gId);
            startActivity(intent);

        }
    };
}
