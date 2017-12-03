package com.example.koungho632.newsogongsaapplication.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koungho632.newsogongsaapplication.R;

public class CreatingDialog extends Dialog {

    private ImageView userImage; // 다이얼로그에 포함된 사진
    private TextView userName;  // 다이얼로그에 포함된 유저 이름

    private Button ok_Btn;
    private Button cancel_Btn;

    public CreatingDialog(Context context,int themeResId) {
        super(context, themeResId);
        // 다이얼로그 외부를 눌렀을때 없어지지 않게 설정
        this.setCanceledOnTouchOutside(false);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_custom_dialog);

        userImage = (ImageView) findViewById(R.id.creating_userImage3);
        userName = (TextView) findViewById(R.id.userName);

        ok_Btn = (Button) findViewById(R.id.OK_Btn);
        cancel_Btn = (Button) findViewById(R.id.CANCEL_Btn);

        ok_Btn.setOnClickListener(ok_Listener);
        cancel_Btn.setOnClickListener(cancel_Listener);
    }

    // 커스텀 다이얼로그에 나타나는 사용자 이미지를 set해주기 위한 메소드
    public void setImage(Bitmap bitmap){
        this.userImage.setImageBitmap(bitmap);
    }

    // 커스텀 다이얼로그에 나타나는 사용자 이름을 set해주기 위한 메소드
    public void setUserName(String UserName){
        // DB에 저장된 회원의 이름을 가지고 넣어야하는데..
        this.userName.setText(UserName);
    }

    public View.OnClickListener ok_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            Toast.makeText(getContext(), "Create", Toast.LENGTH_SHORT).show();
            // 오케이를 눌렀을 때 CreatingContent의 정보가 모두 서버로 보내지고
            // 데이터베이스에 저장되어야 한다.

            dismiss();
        }
    };

    public View.OnClickListener cancel_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            Toast.makeText(getContext(), "CANCEL", Toast.LENGTH_SHORT).show();
            dismiss();  // 사라짐. 취소
        }
    };
}
