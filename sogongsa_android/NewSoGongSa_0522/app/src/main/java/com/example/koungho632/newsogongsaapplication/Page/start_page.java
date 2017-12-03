package com.example.koungho632.newsogongsaapplication.Page;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.example.koungho632.newsogongsaapplication.R;


/**
 * Created by NT-RC510 on 2016-04-29.
 */
public class start_page extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start_page);

        // 소공사 초기 스플래시 화면
        // 스플래시 액티비티를 2초동안 보여준다.
        Handler handler = new Handler() {
            public void handleMessage(Message msg){
                finish();
            }
        };

        handler.sendEmptyMessageDelayed(0, 2000);
        this.onDestroy();
    }

}
