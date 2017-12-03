package com.example.koungho632.newsogongsaapplication.Page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.koungho632.newsogongsaapplication.Dialog.CustomDialog;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Server_Connection.ServerConnect;

import java.util.Date;
import java.util.HashMap;
/**
 * Created by NT-RC510 on 2016-04-29.
 */

// 회원가입 액티비티
public class signup_page extends AppCompatActivity{
    String up_id;       // 아이디
    String up_pw;       // 패스워드
    String up_name;     // 이름
    String up_group;    // 소속학과
    String up_sex;      // 성별
    String up_age;      // 나이
    String up_first, up_second, up_third;   //   연락처

    Button enroll_btn; // 가입 버튼
    Button cancel_btn; // 취소 버튼

    Intent login_intent;
    HashMap signUp_HashMap; // 회원가입 해쉬맵

    boolean checked_id=true;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        enroll_btn=(Button)findViewById(R.id.new_signup_btn);
        enroll_btn.setOnClickListener(enrollListener);

        cancel_btn = (Button) findViewById(R.id.new_cancel_btn);
        cancel_btn.setOnClickListener(cancelListener); // 취소버튼 등록

    }

    // 가입버튼 리스너 객체생성 및 메소드 오버라이딩
    Button.OnClickListener enrollListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            up_id=((EditText)findViewById(R.id.new_id)).getText().toString();
            up_pw=((EditText)findViewById(R.id.new_pw)).getText().toString();
            up_name=((EditText)findViewById(R.id.new_name)).getText().toString();
            up_group=((EditText)findViewById(R.id.new_major)).getText().toString();
            if(((RadioButton)findViewById(R.id.new_man)).isChecked()){
                up_sex="1";
            }else{
                up_sex="2";
            }
            up_age=((EditText)findViewById(R.id.new_age)).getText().toString();

            up_first=((EditText)findViewById(R.id.first_num)).getText().toString();
            up_second=((EditText)findViewById(R.id.second_num)).getText().toString();
            up_third=((EditText)findViewById(R.id.thrid_num)).getText().toString();

            boolean signup_ok=true;

            if(idChecked()==false || up_id.equals("")){
                signup_ok=false;
                //다이얼로그
                AlertDialog.Builder alert = new AlertDialog.Builder(signup_page.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                alert.setMessage("아이디가 잘못되었습니다.");
                alert.show();


                        ((EditText) findViewById(R.id.new_id)).requestFocus();
                ((EditText)findViewById(R.id.new_id)).selectAll();
                return;
            }

            if(pwChecked()==false || up_pw.equals("")){
                signup_ok=false;
                //다이얼로그

                AlertDialog.Builder alert = new AlertDialog.Builder(signup_page.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                alert.setMessage("패스워드 양식이 잘못되었습니다.\n영문과 숫자와 _으로 된 조합만 가능합니다.");
                alert.show();

                ((EditText)findViewById(R.id.new_pw)).requestFocus();
                ((EditText)findViewById(R.id.new_pw)).selectAll();
                return;
            }

            if(up_name.equals("")){
                AlertDialog.Builder alert = new AlertDialog.Builder(signup_page.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                alert.setMessage("이름을 입력해주세요");
                alert.show();

                ((EditText)findViewById(R.id.new_name)).requestFocus();
                ((EditText)findViewById(R.id.new_name)).selectAll();
                return;
            }

            if(up_age.equals("")){
                AlertDialog.Builder alert = new AlertDialog.Builder(signup_page.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                alert.setMessage("나이를 입력해주세요");
                alert.show();

                ((EditText)findViewById(R.id.new_age)).requestFocus();
                ((EditText)findViewById(R.id.new_age)).selectAll();
                return;
            }

            if(up_group.equals("")){
                AlertDialog.Builder alert = new AlertDialog.Builder(signup_page.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                alert.setMessage("학과를 입력해주세요");
                alert.show();

                ((EditText)findViewById(R.id.new_major)).requestFocus();
                ((EditText)findViewById(R.id.new_major)).selectAll();
                return;
            }


            if(phoneNumChecked()==false || up_first.equals("") || up_second.equals("") || up_third.equals("")){
                signup_ok=false;
                //다이얼로그

                AlertDialog.Builder alert = new AlertDialog.Builder(signup_page.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                alert.setMessage("휴대폰 번호가 잘못되었습니다.");
                alert.show();
                ((EditText)findViewById(R.id.first_num)).requestFocus();
                ((EditText)findViewById(R.id.first_num)).selectAll();
                return;
            }

            if(signup_ok){//회원 가입 가능

                //서버에서 받은 데이터를 조작할 이벤트를 등록
               ServerConnect.DataDownloadListener dataDownloadListener = new ServerConnect.DataDownloadListener() {

                    @Override
                    public void dataDownloadedSuccessfully(Object data) {
                        String flag=(String)data;

                        String[] temp=flag.split("@");

                        if(flag.equals(MainActivity.TAG)) {

                            Log.d("signup_page.java :: ", "*****가입 완료*****");
                            //로그인 페이지로 이동
//                            login_intent=new Intent(signup_page.this, login_page.class);
//                            startActivity(login_intent);
                            finish();
                        }

                        // 데이터 로드가 실패된 경우 >> 다이얼로그 생성
                        else{
                            AlertDialog.Builder alert = new AlertDialog.Builder(signup_page.this);
                            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();     //닫기
                                }
                            });
                            alert.setMessage("서버에 정보가 없습니다");
                            alert.show();
                            return;
                        }
                    }
                    @Override
                    public void dataDownloadFailed() {
                        Log.d("132321312312","signup_datafail");
                    }
                };

                // 해쉬맵으로 값을 서버로 보낸다.
                // 이후 디비에 접속하여 값 저장
                signUp_HashMap = new HashMap();
                signUp_HashMap.put("cmd", "signup");
                signUp_HashMap.put("userId", up_id);
                signUp_HashMap.put("userPw", up_pw);
                signUp_HashMap.put("userName", up_name);
                signUp_HashMap.put("userMajor", up_group);
                signUp_HashMap.put("userSex", up_sex);
                signUp_HashMap.put("userAge", up_age);
                signUp_HashMap.put("userPhone", up_first + "-" + up_second + "-" + up_third);
                signUp_HashMap.put("userImage", "");
                signUp_HashMap.put("userComment", "");
                signUp_HashMap.put("userHobby", "0");
                // 가입시 유저이미지, 유저코멘트는 공백, 관심사는 0으로 설정

                login_page._serverConnect.setDataDownloadListener(dataDownloadListener);
                login_page._serverConnect.sendMsg(signUp_HashMap);
//
            }

        }
    };

    // 취소버튼 리스너 객체생성 및 취소버튼 메소드 오버라이딩
    Button.OnClickListener cancelListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
//            login_intent = new Intent(signup_page.this, login_page.class);
//            startActivity(login_intent);
            finish();
        }
    };

    boolean idChecked(){//db와 비교하여 학번이 맞는지 검사
        //자리수 검사
        if(up_id.length()!=7)
            return false;
        Log.d("id_length","ok");

        //양식 검사
        for(int i=0; i<up_id.length(); ++i){
            if(up_id.charAt(i)<'0' && up_id.charAt(i)>'9')
                return false;
        }
        Log.d("id_number","ok");

        Date now=new Date();
        int now_year=now.getYear();
        int year=Integer.parseInt(up_id.substring(0, 2));

        if(year>now_year)
            return false;
        Log.d("id_year","ok");

        return checked_id;
    }
    boolean pwChecked(){//패스워드 양식 검사 영문, 숫자 _ 만 가능
        //자리수 검사
        if(up_pw.length()<6)
            return false;
        
        //양식 검사
        for(int i=0; i<up_pw.length(); ++i){
            if( !((up_pw.charAt(i)>='0' && up_pw.charAt(i)<='9')
                    || (up_pw.charAt(i)>='A' && up_pw.charAt(i)<='z')
                    || up_pw.charAt(i)=='_')){
                return false;
            }
        }

        return true;
    }
    boolean phoneNumChecked(){
        //숫자 양식이 맞는지 검사
        if(up_first.equals("010")==false)
            return false;

        for(int i=0; i<up_second.length(); ++i){
            if(up_id.charAt(i)<'0' && up_id.charAt(i)>'9')
                return false;
        }

        for(int i=0; i<up_third.length(); ++i){
            if(up_id.charAt(i)<'0' && up_id.charAt(i)>'9')
                return false;
        }

        return true;
    }
}
