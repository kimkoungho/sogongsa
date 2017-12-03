package com.example.koungho632.newsogongsaapplication.Page;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.koungho632.newsogongsaapplication.Adapter.Group_ListViewAdapter;
import com.example.koungho632.newsogongsaapplication.ListItem.Meeting_ListItem;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Server_Connection.ServerConnect;

import java.util.ArrayList;
import java.util.HashMap;

public class search_page extends AppCompatActivity {
    private Spinner search_spinner = null; // 모임 검색의 스피너
    private EditText search_Text = null;   // 모임 검색의 에디트 텍스트
    private Button search_Btn = null;      // 모임 검색의 버튼
    private ListView search_List = null;   // 모임 검색의 리스트뷰
    private ArrayList<Meeting_ListItem> myMeetingList=null;
    private Group_ListViewAdapter adapter=null;


    private int index = 0;              // 스피너의 스트링 값과 인덱스 값을 받아오기 위함
    private String menu_Value = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        search_spinner = (Spinner) findViewById(R.id.search_spinner);
        search_Text = (EditText) findViewById(R.id.search_text);
        search_Btn = (Button) findViewById(R.id.search_Btn);
        search_List = (ListView) findViewById(R.id.search_list);

        search_spinner.setOnItemSelectedListener(menu_searchListener);
        search_Btn.setOnClickListener(Btn_Listener);

        myMeetingList=new ArrayList<Meeting_ListItem>();
        adapter=new Group_ListViewAdapter(getApplicationContext(), R.layout.meeting_listview,myMeetingList);
        search_List.setAdapter(adapter);

        search_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //ListView의 아이템 중 하나가 클릭될 때 호출되는 메소드
            //첫번째 파라미터: 클릭된 아이템을 보여주고 있는 AdapterView 객체(여기서는 ListView객체)
            //두번째 파라미터: 클릭된 아이템 뷰
            //세번째 파라미터: 클릭된 아이템의 위치(ListView이 첫번째 아이템(가장위쪽)부터 차례대로 0,1,2,3...)
            //네번째 파라미터: 클릭된 아이템의 아이디(특별한 설정이 없다면 세번째 파라미터인 position과 같은 값)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("click", Integer.toString(position));
                //아이템 클릭시 신청 페이지로 이동
                Intent intent = new Intent(getApplicationContext(), MeetingGroup.class);
                startActivity(intent);
                finish();
            }
        });



    }
    // 검색되는 기능을 삽입한다.
    protected Button.OnClickListener Btn_Listener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            // 버튼이 클릭되고 이후에
            // 스피너의 선택된 인덱스 값을 확인하여 분류된 검색 리스트를 추출한다.
           onResume();
        }
    };

    protected Spinner.OnItemSelectedListener menu_searchListener = new Spinner.OnItemSelectedListener(){
        // 스피너의 선택된 아이템의 인덱스와 스트링 값을 받아온다.
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            index = position;
            menu_Value = parent.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    @Override
    protected void onResume() {
        super.onResume();
        getGroupList();

    }

    void getGroupList(){
        ServerConnect.DataDownloadListener dataDownloadListener=new ServerConnect.DataDownloadListener() {
            @Override
            public void dataDownloadedSuccessfully(Object data) {
                ArrayList<HashMap> groupAll=(ArrayList)data;
                Log.d("000000000",groupAll.toString());

                myMeetingList.clear();
                for(HashMap item:groupAll) {
                    String id = (String) item.get("id");
                    String category = (String) item.get("category");
                    Drawable image = setDrawable(Integer.parseInt(category));
                    String title = (String) item.get("name");
                    String person = (String) item.get("uName");
                    String personNum = (String) item.get("personNum");
                    Meeting_ListItem item1 = new Meeting_ListItem(id, image, title, person, personNum);
                    myMeetingList.add(item1);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void dataDownloadFailed() {

            }
            public Drawable setDrawable(int index){
                Drawable img=null;
                switch (index){
                    case 1:img=getResources().getDrawable(R.drawable.category_study);break;
                    case 2:img=getResources().getDrawable(R.drawable.category_book);break;
                    case 3:img=getResources().getDrawable(R.drawable.category_music);break;
                    case 4:img=getResources().getDrawable(R.drawable.category_travel);break;
                    case 5:img=getResources().getDrawable(R.drawable.category_movie);break;
                    case 6:img=getResources().getDrawable(R.drawable.category_love);break;
                    default:break;
                }
                return img;
            }
        };

        HashMap hashMap=new HashMap();
        hashMap.put("cmd","search");
        hashMap.put("category",Integer.toString(index));
        hashMap.put("text",search_Text.getText().toString());

        login_page._serverConnect.setDataDownloadListener(dataDownloadListener);
        login_page._serverConnect.sendMsg(hashMap);
    }
}
