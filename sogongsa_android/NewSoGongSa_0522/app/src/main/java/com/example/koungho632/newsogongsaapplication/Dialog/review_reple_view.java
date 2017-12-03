package com.example.koungho632.newsogongsaapplication.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.koungho632.newsogongsaapplication.Adapter.ReviewRepleAdapter;
import com.example.koungho632.newsogongsaapplication.ListItem.ReviewRepleListItem;
import com.example.koungho632.newsogongsaapplication.Page.MainActivity;
import com.example.koungho632.newsogongsaapplication.Page.MeetingGroup;
import com.example.koungho632.newsogongsaapplication.Page.login_page;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Server_Connection.ServerConnect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class review_reple_view extends Dialog {

    private ReviewRepleAdapter repleAdapter = null;
    private ArrayList<ReviewRepleListItem> repleListItems = null;
    private ListView reple_Listview;

    private EditText input_reple;       // 댓글 입력란

    private Button reple_Button;
    private Button reple_CancelButton;

    SimpleDateFormat formatter = new SimpleDateFormat ( "yy-MM-dd HH:mm:ss", Locale.KOREA );
    Date currentTime = new Date ( );
    String dTime = formatter.format ( currentTime );

    String rId;
    public review_reple_view(Context context, int themeResId,String rId) {
        super(context, themeResId);

        this.rId=rId;

        // 다이얼로그 외부를 눌렀을때 없어지지 않게 설정
        this.setCanceledOnTouchOutside(false);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        getWindow().setGravity(Gravity.CENTER_VERTICAL);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.activity_review_reple_view);

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.85); //Display 사이즈의 70%
        int height = (int) (display.getHeight() * 0.95);  //Display 사이즈의 90%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        input_reple = (EditText) findViewById(R.id.input_reple);
        reple_Button = (Button) findViewById(R.id.reple_btn);
        reple_CancelButton = (Button) findViewById(R.id.reple_cancelbtn);

        // 다이얼로그에 버튼 리스너 등록
        reple_Button.setOnClickListener(btn_Listener);
        reple_CancelButton.setOnClickListener(btn_Listener);

        /// 리플을 다는 사용자의 임시 이미지 설정


        repleListItems = new ArrayList<ReviewRepleListItem>();
//        repleListItems.add(new ReviewRepleListItem(userDraw, "박성동", "1등이다 !"));
//        repleListItems.add(new ReviewRepleListItem(userDraw, "김경호", "2등 댓글이다"));
//        repleListItems.add(new ReviewRepleListItem(userDraw, "김재호", "난 3등으로 댓글 달았어"));
//        repleListItems.add(new ReviewRepleListItem(userDraw, "전영욱", "나는 4등이네"));

        reple_Listview = (ListView) findViewById(R.id.reple_listview);
        repleAdapter = new ReviewRepleAdapter(getContext(), R.layout.activity_review_reple_list_item, repleListItems);
        reple_Listview.setAdapter(repleAdapter);

        getReple();
    }

    protected Button.OnClickListener btn_Listener = new Button.OnClickListener(){
        @Override
        // 생성버튼일때 번개 모임 생성
        public void onClick(View v) {
            if(v.getId() == R.id.reple_btn){
                Toast.makeText(getContext(), "댓글 등록", Toast.LENGTH_SHORT).show();

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
                HashMap hashMap=new HashMap();
                hashMap.put("cmd","writeReply");
                hashMap.put("uId", MainActivity.UserId);
                hashMap.put("rId", rId);
                hashMap.put("content",input_reple.getText().toString());
                hashMap.put("date",dTime);

                login_page._serverConnect.setDataDownloadListener(dataDownloadListener);
                login_page._serverConnect.sendMsg(hashMap);
                // 예제코드
                //dismiss();
            }

            else if(v.getId() == R.id.reple_cancelbtn){
                dismiss();
            }
        }
    };

    protected void getReple(){
        ServerConnect.DataDownloadListener dataDownloadListener=new ServerConnect.DataDownloadListener() {
            @Override
            public void dataDownloadedSuccessfully(Object data) {
                ArrayList<HashMap> reple=(ArrayList<HashMap>)data;

                for(HashMap item:reple){
                    byte[] img=(byte[])item.get("uImg");
                    Bitmap uImg= BitmapFactory.decodeByteArray(img,0,img.length);
                    String uName=(String)item.get("uName");
                    String rCont=(String)item.get("rCont");
                    String rDate=(String)item.get("rDate");

                    ReviewRepleListItem item1=new ReviewRepleListItem(uImg,uName,rCont);
                    repleListItems.add(item1);
                }
                repleAdapter.notifyDataSetChanged();

            }

            @Override
            public void dataDownloadFailed() {

            }
        };
        HashMap hashMap=new HashMap();
        hashMap.put("cmd","reply");
        hashMap.put("rId",rId);
        hashMap.put("gId",MeetingGroup.gId);
        login_page._serverConnect.setDataDownloadListener(dataDownloadListener);
        login_page._serverConnect.sendMsg(hashMap);
    }
}
