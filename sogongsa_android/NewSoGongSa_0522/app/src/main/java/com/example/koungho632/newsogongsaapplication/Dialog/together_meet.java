package com.example.koungho632.newsogongsaapplication.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.koungho632.newsogongsaapplication.Page.login_page;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Server_Connection.ServerConnect;

import java.util.HashMap;

public class together_meet extends Dialog {

    private EditText fast_Title;        //  번개 제목

    private EditText fast_month;        //  번개 만드는 월 일 시 분
    private EditText fast_day;
    private EditText fast_hour;
    private EditText fast_minute;
    private String fast_date;

    private EditText fast_where;        //  번개 장소
    private EditText fast_money;        //  번개 금액
    private EditText fast_personNum;    //  번개 정원
    private EditText fast_content;      //  번개 내용 ( 간략한 )

    private Button makeBtn;
    private Button cancelBtn;

    String uId;
    String gId;

    public together_meet(Context context, int themeResId, String uId, String gId) {
        super(context, themeResId);

        this.uId=uId;
        this.gId=gId;


        // 다이얼로그 외부를 눌렀을때 없어지지 않게 설정
        this.setCanceledOnTouchOutside(false);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_together_meet);

        fast_Title = (EditText) findViewById(R.id.fast_meeting_title);
        fast_month = (EditText) findViewById(R.id.fast_meeting_month);
        fast_day = (EditText) findViewById(R.id.fast_meeting_day);
        fast_hour = (EditText) findViewById(R.id.fast_meeting_hour);
        fast_minute = (EditText) findViewById(R.id.fast_meeting_minute);
        fast_where = (EditText) findViewById(R.id.fast_meeting_where);
        fast_money = (EditText) findViewById(R.id.fast_meeting_money);
        fast_personNum = (EditText) findViewById(R.id.fast_meeting_personNum);
        fast_content = (EditText) findViewById(R.id.fast_meeting_content);

        makeBtn = (Button) findViewById(R.id.make_fast_okBtn);
        cancelBtn = (Button) findViewById(R.id.make_fast_cancelBtn);

        // 버튼리스너 등록하기
        makeBtn.setOnClickListener(btn_Listener);
        cancelBtn.setOnClickListener(btn_Listener);
    }

    protected Button.OnClickListener btn_Listener = new Button.OnClickListener(){
        @Override
        // 생성버튼일때 번개 모임 생성
        public void onClick(View v) {
            if(v.getId() == R.id.make_fast_okBtn){
                Toast.makeText(getContext(), "번개 생성", Toast.LENGTH_SHORT).show();

                ServerConnect.DataDownloadListener dataDownloadListener=new ServerConnect.DataDownloadListener() {
                    @Override
                    public void dataDownloadedSuccessfully(Object data) {
                        String flag=(String)data;
                        if(flag.equals(login_page.TAG)){
                            dismiss();
                        }
                    }

                    @Override
                    public void dataDownloadFailed() {

                    }
                };

                // 네개의 에디트 텍스트의 String 값을 추출하여 하나씩 fast_date에 이어붙인다.
                fast_date = fast_month.getText().toString();
                fast_date+= "-" + fast_day.getText().toString();
                fast_date+= "-" + fast_hour.getText().toString();
                fast_date+= fast_minute.getText().toString();

                // 번개를 만드는 다이얼로그의 정보를 추출하여
                // 해쉬맵으로 KEY 와 VALUE 값으로 삽입 후, 서버로 보낸다.
                HashMap hashMap=new HashMap();
                hashMap.put("cmd","createMeeting");
                hashMap.put("uId", uId);
                hashMap.put("gId", gId);
                hashMap.put("title", fast_Title.getText().toString());
                hashMap.put("date", fast_date);
                hashMap.put("location", fast_where.getText().toString());
                hashMap.put("cost", fast_money.getText().toString());
                hashMap.put("personNum", fast_personNum.getText().toString());
                hashMap.put("content",  fast_content.getText().toString());

                login_page._serverConnect.setDataDownloadListener(dataDownloadListener);
                login_page._serverConnect.sendMsg(hashMap);
            }

            else if(v.getId() == R.id.make_fast_cancelBtn){
                dismiss();
            }
        }
    };
}