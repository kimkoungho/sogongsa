package com.example.koungho632.newsogongsaapplication.Fragment;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.koungho632.newsogongsaapplication.Page.CreatingGroup;
import com.example.koungho632.newsogongsaapplication.ListItem.Meeting_ListItem;
import com.example.koungho632.newsogongsaapplication.Adapter.Group_ListViewAdapter;
import com.example.koungho632.newsogongsaapplication.Page.MainActivity;
import com.example.koungho632.newsogongsaapplication.Page.MeetingGroup;
import com.example.koungho632.newsogongsaapplication.R;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
/**
 * Created by koungho632 on 2016. 5. 4..
 */
public class GroupFragment extends Fragment{
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    MainActivity activity;

    private FloatingActionButton floatingActionButton=null;
    private ListView listView=null;
    public ArrayList<Meeting_ListItem> myMeetingList=null;
    public static Group_ListViewAdapter adapter=null;

    String gId;


    public static GroupFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        GroupFragment fragment = new GroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_meeting, container, false);

        myMeetingList=new ArrayList<Meeting_ListItem>();

        activity=(MainActivity)getActivity();


        //  어댑터에 추가된 리스트들을 넣고 레이아웃도 포함시켜준다.
        //  그 후 리스트 뷰에 setAdapter를 통해 적용
        listView=(ListView)view.findViewById(R.id.myMeeting_list);
        adapter=new Group_ListViewAdapter(getContext(), R.layout.meeting_listview,myMeetingList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //ListView의 아이템 중 하나가 클릭될 때 호출되는 메소드
            //첫번째 파라미터: 클릭된 아이템을 보여주고 있는 AdapterView 객체(여기서는 ListView객체)
            //두번째 파라미터: 클릭된 아이템 뷰
            //세번째 파라미터: 클릭된 아이템의 위치(ListView이 첫번째 아이템(가장위쪽)부터 차례대로 0,1,2,3...)
            //네번째 파라미터: 클릭된 아이템의 아이디(특별한 설정이 없다면 세번째 파라미터인 position과 같은 값)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("click", Integer.toString(position));

                gId=myMeetingList.get(position).getId();

                //아이템 클릭시 신청 페이지로 이동
                Intent intent=new Intent(getContext(), MeetingGroup.class);
                intent.putExtra("gId",gId);
                intent.putExtra("uId",activity.UserId);
                startActivity(intent);
            }
        });

        floatingActionButton=(FloatingActionButton)view.findViewById(R.id.fab_addMenu);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), CreatingGroup.class);
                intent.putExtra("uId",activity.UserId);
                startActivity(intent);
            }
        });

        return view;
    }



}
