package com.example.koungho632.newsogongsaapplication.Page;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koungho632.newsogongsaapplication.Dialog.ChangeDialog;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Server_Connection.ServerConnect;

import java.util.HashMap;

public class ChangePassword2 extends AppCompatActivity{
 /*
    * InputPW 액티비티에서 비밀번호 입력 후
    * 회원정보를 변경하기 위한 페이지로 넘어간다.
    * 그 액티비티가 현재 이 액티비티이다.
    *
    * 이 액티비티에서 하는 역할이라곤 비밀변호를 변경하는것 외에
    * 모임의 사용자의 관심사를 변경하거나 선택하는 역할을 담당하고 있다.
    * */

    private EditText changing_PW;       // 비밀번호 입력
    private EditText changed_PW;        // 비밀번호 확인
    private TextView change_Text;       // 비밀번호 일치여부 확인
    private boolean check_PW;           // 비밀번호 체크

    private Button save_Btn;            // 저장 버튼

    private ImageButton study_imgBtn = null;
    private ImageButton book_imgBtn = null;
    private ImageButton travel_imgBtn = null;
    private ImageButton music_imgBtn = null;
    private ImageButton movie_imgBtn = null;
    private ImageButton love_imgBtn = null;

    private boolean hobbyFlag[]=new boolean[7];

    String userId;
    String userPw;
    String userHobby;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password2);

        userId=getIntent().getStringExtra("userId");
        userPw=getIntent().getStringExtra("userPw");
        userHobby=getIntent().getStringExtra("userHobby");


        changing_PW = (EditText) findViewById(R.id.changing_PW_id);
        changed_PW = (EditText) findViewById(R.id.changed_PW_id);
        change_Text = (TextView) findViewById(R.id.PW_Confirm);
        save_Btn = (Button) findViewById(R.id.save_btn);


        /********** 이미지 버튼의 값을 추출하고, 백그라운드 설정, 리스너 등록 **********/
        study_imgBtn = (ImageButton) findViewById(R.id.imageBtn_study_id);
        book_imgBtn = (ImageButton) findViewById(R.id.imageBtn_book_id);
        travel_imgBtn = (ImageButton) findViewById(R.id.imageBtn_travel_id);
        music_imgBtn = (ImageButton) findViewById(R.id.imageBtn_music_id);
        movie_imgBtn = (ImageButton) findViewById(R.id.imageBtn_movie_id);
        love_imgBtn = (ImageButton) findViewById(R.id.imageBtn_love_id);

        study_imgBtn.setOnClickListener(imgBtn_Listener);
        book_imgBtn.setOnClickListener(imgBtn_Listener);
        travel_imgBtn.setOnClickListener(imgBtn_Listener);
        music_imgBtn.setOnClickListener(imgBtn_Listener);
        movie_imgBtn.setOnClickListener(imgBtn_Listener);
        love_imgBtn.setOnClickListener(imgBtn_Listener);
        /************************************************************************/

        for(int i=1; i<hobbyFlag.length; i++)
            hobbyFlag[i] = false;

        final ImageButton []hobby=new ImageButton[]{
                null,
                study_imgBtn,
                book_imgBtn,
                travel_imgBtn,
                music_imgBtn,
                movie_imgBtn,
                love_imgBtn
        };


        for(int i=0; i<userHobby.length(); i++){
            int index=Integer.parseInt(userHobby.substring(i,i+1))-1;
            Log.d(userHobby,Integer.toString(index));
            if(index<0)
                break;
            hobbyFlag[index]=true;
            hobby[index].setBackgroundResource(R.drawable.selected_btn);
        }




        save_Btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                final ChangeDialog change_dialog = new ChangeDialog(ChangePassword2.this, R.style.Theme_Dialog);

                // 비밀번호가 제대로 입력되고, 최소 한개의 관심사를 선택해야 다이얼로그가 나타난다.
                // 아닌경우에는 토스트로 실패라는 메세지를 출력한다.
                String cnt="";
                for(int i=1; i<hobbyFlag.length; i++){
                    if(hobbyFlag[i])
                        cnt+=i;
                }
                if(check_PW == false || cnt==""){

                    Toast.makeText(getApplication(), "Fail", Toast.LENGTH_SHORT).show();
                }
                else{

                    View.OnClickListener ok=new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ServerConnect.DataDownloadListener dataDownloadListener=new ServerConnect.DataDownloadListener() {
                                @Override
                                public void dataDownloadedSuccessfully(Object data) {

                                    String flag=(String)data;
                                    if (flag.equals(login_page.TAG)) {
                                        Log.d("ssss", "회원정보 성공");

                                        Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();

                                        change_dialog.dismiss();
                                    }
                                }

                                @Override
                                public void dataDownloadFailed() {

                                }
                            };
                            MainActivity.UserPw=changed_PW.getText().toString();

                            HashMap hashMap=new HashMap();
                            hashMap.put("cmd","pass");
                            hashMap.put("id",userId);
                            hashMap.put("pw",changed_PW.getText().toString());
                            String cate="";
                            for(int i=1; i<hobbyFlag.length; i++){
                                if(hobbyFlag[i])
                                    cate+=i;
                            }
                            Log.d("hobby",cate);
                            hashMap.put("hobby", cate);

                            login_page._serverConnect.setDataDownloadListener(dataDownloadListener);
                            login_page._serverConnect.sendMsg(hashMap);
                        }
                    };

                    View.OnClickListener cancel= new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change_dialog.dismiss();
                        }
                    };

                    change_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    });

                    change_dialog.okListener(ok);
                    change_dialog.cancelListener(cancel);
                    change_dialog.show();
                }

            }
        });

        // 비밀번호 입력 에디트 텍스트
        changing_PW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                changed_PW.setText("");
            }
        });

        // 비밀번호가 변경되고 이후에 변경된 비밀번호가 일치하는지 여부를 확인하는 코드
        changed_PW.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String pwConfirmStr =  changed_PW.getText().toString();
                String pwStr = changing_PW.getText().toString();
                check_PW = false;

                if( pwConfirmStr.length() == 0 )
                    change_Text.setText("");
                else if( pwStr.equals(pwConfirmStr) )
                {
                    change_Text.setText("일치");
                    change_Text.setTextColor(Color.GREEN);
                    check_PW = true;
                }
                else
                {
                    change_Text.setText("미일치");
                    change_Text.setTextColor(Color.RED);
                }
            }
        });
    }

    // 이미지 버튼을 클릭시 색깔 변경을 한다. 그리고 그 색깔을 유지
    // 이후 플래그 값을 계산하여 다시 버튼의 이미지를 변경한다.
    // getResource()나 getDrawable로 하려고 헀으나 안됨
    public ImageButton.OnClickListener imgBtn_Listener = new ImageButton.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.imageBtn_study_id:

                    hobbyFlag[1]=!hobbyFlag[1];
                    if(hobbyFlag[1])
                        v.setBackgroundResource(R.drawable.selected_btn);
                    else
                        v.setBackgroundResource(R.drawable.selecting_btn);
                    break;

                case R.id.imageBtn_book_id:

                    hobbyFlag[2]=!hobbyFlag[2];
                    if(hobbyFlag[2])
                        v.setBackgroundResource(R.drawable.selected_btn);
                    else
                        v.setBackgroundResource(R.drawable.selecting_btn);
                    break;

                case R.id.imageBtn_travel_id:

                    hobbyFlag[3]=!hobbyFlag[3];
                    if(hobbyFlag[3])
                        v.setBackgroundResource(R.drawable.selected_btn);
                    else
                        v.setBackgroundResource(R.drawable.selecting_btn);
                    break;

                case R.id.imageBtn_music_id:
                    hobbyFlag[4]=!hobbyFlag[4];
                    if(hobbyFlag[4])
                        v.setBackgroundResource(R.drawable.selected_btn);
                    else
                        v.setBackgroundResource(R.drawable.selecting_btn);
                    break;


                case R.id.imageBtn_movie_id:
                    hobbyFlag[5]=!hobbyFlag[5];
                    if(hobbyFlag[5])
                        v.setBackgroundResource(R.drawable.selected_btn);
                    else
                        v.setBackgroundResource(R.drawable.selecting_btn);
                    break;

                case R.id.imageBtn_love_id:
                    hobbyFlag[6]=!hobbyFlag[6];
                    if(hobbyFlag[6])
                        v.setBackgroundResource(R.drawable.selected_btn);
                    else
                        v.setBackgroundResource(R.drawable.selecting_btn);
                    break;
            }

        }

    };
}
