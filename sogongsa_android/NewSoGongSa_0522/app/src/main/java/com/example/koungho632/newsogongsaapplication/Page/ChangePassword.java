package com.example.koungho632.newsogongsaapplication.Page;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;

import com.example.koungho632.newsogongsaapplication.R;

public class ChangePassword extends AppCompatActivity {
    private TextView goHome_Text;
    private EditText input_PwText;
    private Button input_PwBtn;

    String userId;
    String userPw;
    String userHobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        userId=getIntent().getStringExtra("userId");
        userPw=getIntent().getStringExtra("userPw");
        userHobby=getIntent().getStringExtra("userHobby");

        goHome_Text = (TextView) findViewById(R.id.previous_id2);
        input_PwText = (EditText) findViewById(R.id.input_PW);
        input_PwBtn = (Button) findViewById(R.id.input_PW_Btn);

        input_PwBtn.setOnClickListener(btn_listener);
        goHome_Text.setOnClickListener(View_Listener);
    }

    protected Button.OnClickListener btn_listener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            ///////////////////
            // 비밀번호 회원 비밀번호와 일치하는 지 확인
            ///////////////////
            Log.d("tag",userPw);
            //  일치하는 경우
            if(userPw.equals(input_PwText.getText().toString())){
                Intent intent = new Intent(ChangePassword.this, ChangePassword2.class);
                intent.putExtra("userId",userId);
                intent.putExtra("userPw",userPw);
                intent.putExtra("userHobby", userHobby);
                startActivity(intent);
                finish();
            }

            // 일치하지 않는 경우
            else{
                // 다이얼로그 띄우기ㅋ
            }
        }
    };

    protected TextView.OnClickListener View_Listener = new TextView.OnClickListener(){
        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(ChangePassword.this, MainActivity.class);
//            startActivity(intent);
            finish();
        }
    };
}


