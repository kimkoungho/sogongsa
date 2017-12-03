package com.example.koungho632.newsogongsaapplication.Fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.koungho632.newsogongsaapplication.Adapter.GroupMainAdapter;
import com.example.koungho632.newsogongsaapplication.ListItem.GroupMeetingListItem;
import com.example.koungho632.newsogongsaapplication.Page.MeetingGroup;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Dialog.together_meet;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupMeetingFragment extends Fragment {

    private ListView listView=null;
    public ArrayList<GroupMeetingListItem> meetingListItems=null;
    public GroupMainAdapter adapter=null;

    public ImageView groupImage=null;
    public TextView groupComent=null;
    public FloatingActionButton fab_addfast = null;

    private together_meet tmDialog = null;
    MeetingGroup group;

    String uId;
    String gId;
    MeetingGroup activity;

    public GroupMeetingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_meeting_group_main, container, false);

        group=(MeetingGroup)getActivity();

        //그룹 이미지와 내용
        groupImage=(ImageView)view.findViewById(R.id.group_image);
        groupComent=(TextView)view.findViewById(R.id.group_content);

        meetingListItems=new ArrayList<GroupMeetingListItem>();

        //  어댑터에 추가된 리스트들을 넣고 레이아웃도 포함시켜준다.
        //  그 후 리스트 뷰에 setAdapter를 통해 적용
        listView=(ListView)view.findViewById(R.id.group_meeting);
        adapter=new GroupMainAdapter(getContext(), R.layout.meeting_group_listview,meetingListItems);
        listView.setAdapter(adapter);

        activity=(MeetingGroup)getActivity();
        uId=activity.uId;
        gId=activity.gId;

        // 번개 모임 만드는 플로팅 버튼
        fab_addfast = (FloatingActionButton) view.findViewById(R.id.fab_addFast);
        fab_addfast.setOnClickListener(fast_Listener);

        return view;
    }

    protected FloatingActionButton.OnClickListener fast_Listener = new FloatingActionButton.OnClickListener(){
        @Override
        public void onClick(View v) {
            tmDialog = new together_meet(getContext(), R.style.Theme_Dialog,uId,gId);
            // 정 가운데 화면에 출력
            tmDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
            tmDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
            tmDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    activity.getGroupInfo();
                }
            });
            tmDialog.show();
        }
    };
}
