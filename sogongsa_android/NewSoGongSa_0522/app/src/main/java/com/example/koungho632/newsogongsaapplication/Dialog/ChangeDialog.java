package com.example.koungho632.newsogongsaapplication.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koungho632.newsogongsaapplication.R;

/**
 * Created by NT-RC510 on 2016-05-11.
 */
public class ChangeDialog extends Dialog {

    /*
    *   저장과 취소가 있는 다이얼로그
    */


    private Button save_Btn;
    private Button cancel_Btn;
    private TextView text;

    public ChangeDialog(Context context, int themeResId) {
        super(context, themeResId);

        // 다이얼로그 외부를 눌렀을때 없어지지 않게 설정
        this.setCanceledOnTouchOutside(false);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_change_dialog);

        text=(TextView)findViewById(R.id.text);
        save_Btn = (Button) findViewById(R.id.SAVE_Btn);
        cancel_Btn = (Button) findViewById(R.id.CANCEL_Btn);


    }

    public void setCustomText(String str){
        this.text.setText(str);
    }
    public void setOkBtn(String str){
        this.save_Btn.setText(str);
    }
    public void setCancel_Btn(String str){
        this.cancel_Btn.setText(str);
    }
    public void okListener(View.OnClickListener listener){
        this.save_Btn.setOnClickListener(listener);
    }
    public void cancelListener(View.OnClickListener listener){
        this.cancel_Btn.setOnClickListener(listener);
    }
}
