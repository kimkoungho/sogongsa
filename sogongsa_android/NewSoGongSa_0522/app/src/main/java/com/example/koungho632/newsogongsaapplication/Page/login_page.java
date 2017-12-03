package com.example.koungho632.newsogongsaapplication.Page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Server_Connection.ServerConnect;

import java.util.HashMap;


/**
 * Created by NT-RC510 on 2016-04-29.
 */
public class login_page extends AppCompatActivity{

    public static final String TAG="q*%u*%i*%t";

    String macaddr="100.100.106.188";//서버 아이피
    int port=3333;

    public static ServerConnect _serverConnect;

    EditText user_id,user_pw;     // 아이디 입력란, 패스워드 입력란
    Button login_btn,signup_btn;
    public static CheckBox save_id,auto_login;

    Intent mainMenu_intent; // 메인메뉴로 넘어가기 위한 인텐트
    Intent signup_intent; // 회원가입으로 넘어가기 위한 인텐트

    public static String idName, pwName;//자동 로그인
    public static final String PREFS_NAME = "Login_Prefs";

    SharedPreferences setting=null;
    SharedPreferences.Editor editor=null;
    boolean success_login=false;

    //사용자 정보를 서버로 부터 받는다

    public static login_page activity;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, start_page.class));
        setContentView(R.layout.login_page);

        activity=this;

        _serverConnect =new ServerConnect(getApplicationContext(),macaddr,port);
        _serverConnect.execute();

        login_btn = (Button) findViewById (R.id.login_button);
        signup_btn = (Button) findViewById (R.id.signup_button);

        login_btn.setOnClickListener(clickListener1);
        signup_btn.setOnClickListener(clickListener2);

        user_id=(EditText)findViewById(R.id.id_text);
        user_pw=(EditText)findViewById(R.id.pw_text);

        setting = getSharedPreferences("Login_Setting", 0);
        editor= setting.edit();

        save_id=(CheckBox)findViewById(R.id.save_id);
        save_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auto_login.isChecked()&& save_id.isChecked()==false){
                    //자동로그인은 클릭되었는데 아이디 저장이 없을 때
                    AlertDialog.Builder alert = new AlertDialog.Builder(login_page.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            auto_login.setChecked(false);
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            save_id.setChecked(true);
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("자동 로그인도 해제 해야 합니다.\n 해제 하시겠습니까?");
                    alert.show();

                    save_id.requestFocus();
                }
            }
        });

        auto_login=(CheckBox)findViewById(R.id.auto_login);
        auto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auto_login.isChecked()&& save_id.isChecked()==false){
                //자동로그인은 클릭되었는데 아이디 저장이 없을 때
                    AlertDialog.Builder alert = new AlertDialog.Builder(login_page.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();     //닫기
                            }
                        });
                    alert.setMessage("아이디 저장을 체크 하셔야합니다");
                    alert.show();

                    save_id.requestFocus();
                }
            }
        });
        //setting 정보 가져오기
        boolean save_id_checked=setting.getBoolean("save_id_check",false);
        boolean auto_login_checked=setting.getBoolean("auto_login_check", false);

        save_id.setChecked(save_id_checked);
        auto_login.setChecked(auto_login_checked);

        if(save_id_checked) {
            String user_id_text = setting.getString("save_id_setting", "");
            user_id.setText(user_id_text);
        }

        if(auto_login_checked) {
            String user_pw_text = setting.getString("save_pw_setting", "");
            user_pw.setText(user_pw_text);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        editor.putBoolean("save_id_check", save_id.isChecked());
        Log.d("save_id_check", "저장했다");

        //아이디 정보 저장
        if(save_id.isChecked()) {
            editor.putString("save_id_setting",user_id.getText().toString());
            Log.d("save_id_setting",setting.getString("save_id_setting",""));
        }else{
            editor.remove("save_id_setting");
        }

        editor.putBoolean("auto_login_check",auto_login.isChecked());
        Log.d("auto_login_setting","저장했다");
        //자동로그인을 위한 패스워드 정보 저장
        if(auto_login.isChecked()){
            if(save_id.isChecked()){
                editor.putString("save_pw_setting", user_pw.getText().toString());
            }
        }else{
            editor.remove("save_pw_setting");
        }

        editor.commit();

    }

    // 버튼 리스너 객체 생성 ( 로그인 하는 경우 )
    Button.OnClickListener clickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainMenu_intent = new Intent(login_page.this, MainActivity.class);
            startActivity(mainMenu_intent);

            final String user_id_val=user_id.getText().toString();
            final String user_pw_val=user_pw.getText().toString();
//////
//            mainMenu_intent = new Intent(login_page.this, MainActivity.class);
//            startActivity(mainMenu_intent);
//            finish();
/////

            if(user_id_val.equals("")){
                Toast.makeText(getApplicationContext(), "학번을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
            else if(user_pw_val.equals("")){
                Toast.makeText(getApplicationContext(), "패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
            else{
                // 두개다 입력이 된 경우
                if(isChecked(user_id_val,user_pw_val)){

                    //서버에서 받은 데이터를 조작할 이벤트를 등록
                    ServerConnect.DataDownloadListener dataDownloadListener=new ServerConnect.DataDownloadListener() {
                        @SuppressWarnings("unchecked")
                        @Override
                        public void dataDownloadedSuccessfully(Object data) {
                            String flag = (String) data;
                            String[] temp=flag.split("@");
                            String str=flag;

                            Log.d("server - ",flag);
                            Log.d(str,TAG);

                            if (flag.equals(TAG)) {
                                Log.d("ssss", "로긴 성공");
                                mainMenu_intent = new Intent(login_page.this, MainActivity.class);
                                mainMenu_intent.putExtra("userId",user_id_val);
                                mainMenu_intent.putExtra("userPw", user_pw_val);
                                startActivity(mainMenu_intent);

                            } else {
                                user_id.requestFocus();
                                user_id.selectAll();
                                Log.d("ssss", "로긴 실패");

                                //다이얼 로그
                                AlertDialog.Builder alert = new AlertDialog.Builder(login_page.this);
                                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();     //닫기
                                    }
                                });
                                alert.setMessage("학번 또는 비밀번호를 다시 확인하세요.");
                                alert.show();

                            }
                        }

                        @Override
                        public void dataDownloadFailed() {
                            Log.d("111111", "down_fail");
                        }
                    };

                    _serverConnect.setDataDownloadListener(dataDownloadListener);


                    Log.d("1111", "hahaha");
                    HashMap hashMap=new HashMap();
                    hashMap.put("cmd","login");
                    hashMap.put("id", user_id_val);
                    hashMap.put("pw", user_pw_val);


                    _serverConnect.sendMsg(hashMap);

                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(login_page.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기show
                        }
                    });
                    alert.setMessage("학번은 숫자\n비밀번호는 영문과 숫자 조합으로만 가능합니다.");
                    alert.show();
                }
            }

            // 아이디와 패스워드가 입력이 된 경우
            /////////////////////////////////
            /////////////////////////////////
        }
    };

    // 버튼 리스너 객체 생성 ( 회원 가입 하는 경우 )
    Button.OnClickListener clickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 현재 액티비티와 전환될 액티비티를 매개변수로 가진다.
            signup_intent = new Intent(login_page.this, signup_page.class);
            user_id.setText("");
            user_pw.setText("");
            startActivity(signup_intent);

        }
    };

    boolean isChecked(String user_id_val,String user_pw_val){

        for(int i=0; i<user_id.length(); ++i){
            if(user_id_val.charAt(i)<'0' && user_id_val.charAt(i)>'9')
                return false;
        }

        for(int i=0; i<user_pw.length(); ++i){
            if( !((user_pw_val.charAt(i)>='0' && user_pw_val.charAt(i)<='9')
                    || (user_pw_val.charAt(i)>='A' && user_pw_val.charAt(i)<='z')
                    || user_pw_val.charAt(i)=='_')){
                return false;
            }
        }

        return true;
    }

    @Override
    protected void onResume() {

        super.onResume();

        if(auto_login.isChecked()){
            login_btn.callOnClick();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        _serverConnect.isCancelled();
        finish();
    }
}
