package com.example.koungho632.newsogongsaapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.koungho632.newsogongsaapplication.Adapter.MainListViewAdapter;
import com.example.koungho632.newsogongsaapplication.ListItem.MainListItem;
import com.example.koungho632.newsogongsaapplication.Page.MainActivity;
import com.example.koungho632.newsogongsaapplication.Page.MeetingGroup;
import com.example.koungho632.newsogongsaapplication.R;

import java.util.ArrayList;

/**
 * Created by koungho632 on 2016. 5. 4..
 */
public class MainFragment extends Fragment{
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public MainListViewAdapter adapter = null;
    public ArrayList<MainListItem> data = null;
    private android.widget.ListView listview;

    String gId;
    MainActivity activity;

    public static MainFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPage = getArguments().getInt(ARG_PAGE);
        activity=(MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //  drawable디렉토리의 소스를 Drawable 클래스 객체로 변환
        data = new ArrayList<MainListItem>();
        //서버에서 데이터를 받음 list아이템

        //main_listView 객체 찾아와서 참조
        listview = (android.widget.ListView) view.findViewById(R.id.main_listView);

        //  어댑터에 추가된 리스트들을 넣고 레이아웃도 포함시켜준다.
        //  그 후 리스트 뷰에 setAdapter를 통해 적용
        //  setAdapter 메소드 재정의 ( 다른 디렉토리.. )
        adapter = new MainListViewAdapter(getContext(), R.layout.main_listview, data);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //ListView의 아이템 중 하나가 클릭될 때 호출되는 메소드
            //첫번째 파라미터: 클릭된 아이템을 보여주고 있는 AdapterView 객체(여기서는 ListView객체)
            //두번째 파라미터: 클릭된 아이템 뷰
            //세번째 파라미터: 클릭된 아이템의 위치(ListView이 첫번째 아이템(가장위쪽)부터 차례대로 0,1,2,3...)
            //네번째 파라미터: 클릭된 아이템의 아이디(특별한 설정이 없다면 세번째 파라미터인 position과 같은 값)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("click", Integer.toString(position));
                // 아이템 클릭시 신청 페이지로 이동
                gId=data.get(position).getId();

                //아이템 클릭시 신청 페이지로 이동
                Intent intent=new Intent(getContext(), MeetingGroup.class);
                intent.putExtra("gId",gId);
                intent.putExtra("uId",activity.UserId);
                startActivity(intent);
            }
        });
        return view;
    }
}
