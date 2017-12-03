package com.example.koungho632.newsogongsaapplication.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.koungho632.newsogongsaapplication.Page.login_page;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Server_Connection.ServerConnect;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/*
*   액티비티이지만 매니페스트에 다이얼로그로 등록하여 다이얼로그로 사용
*  */

public class together_review extends Activity {

    private ImageView reviewImage = null;       // 후기 사진
    private ImageButton img_ChangeBtn = null;   // 후기 사진 올리는 이미지 버튼

    private EditText review_WriteText = null;   // 후기 작성란
    private Button review_WriteBtn = null;      // 작성 버튼
    private Button review_CancelBtn = null;     // 취소 버튼

    private Uri mImageCaptureUri=null;
    private String url;
    private Bitmap bitmap = null;

    private static final int PICK_FROM_ALBUM = 1;

    String userId;
    String groupId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;

        // 테두리 제거, 타이틀 제거, 정가운데 출력
        getWindow().setAttributes(lpWindow);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        getWindow().setGravity(Gravity.CENTER_VERTICAL);
        getWindow().

        setContentView(R.layout.activity_together_review);

        reviewImage = (ImageView) findViewById(R.id.review_Image);
        img_ChangeBtn = (ImageButton) findViewById(R.id.review_change_btn);

        review_WriteText = (EditText) findViewById(R.id.review_write_text);

        review_WriteBtn = (Button) findViewById(R.id.review_write_btn);
        review_CancelBtn = (Button) findViewById(R.id.review_cancel_btn);

        review_WriteBtn.setOnClickListener(review_WriteListener);
        review_CancelBtn.setOnClickListener(review_WriteListener);

        // 카메라버튼에 리스너 등록하기
        img_ChangeBtn.setOnClickListener(camera_BtnListener);

        userId=getIntent().getStringExtra("uId");
        groupId=getIntent().getStringExtra("gId");
    }

    protected Button.OnClickListener review_WriteListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.review_write_btn){
                Toast.makeText(getApplicationContext(), "후기 작성 완료", Toast.LENGTH_SHORT).show();
                // 작성되고 리스트뷰에 등록
                sendDataServer();
            }

            else if(v.getId() == R.id.review_cancel_btn){
                finish();
            }
        }
    };

    // 이미지 버튼을 눌렀을 경우 사진앨범에서 사진을 가져올 것인지
    // 혹은 직접 사진촬영을 해서 가져올 것인지 물어본다.
    protected ImageButton.OnClickListener camera_BtnListener = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakeAlbumAction();//앨범 이용
                }
            };

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            new AlertDialog.Builder(together_review.this)
                    .setTitle("업로드할 이미지 선택")
                    .setPositiveButton("앨범선택", albumListener)
                    .setNegativeButton("취소", cancelListener)
                    .show();
        }
    };


    /*
     * 앨범에서 이미지 가져오기
     */
    protected void doTakeAlbumAction()
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");

//        Environment.DIRECTORY_DCIM
        intent.setData(MediaStore.Images.Media.INTERNAL_CONTENT_URI);

//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    //startActivityForResult의 수행 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(resultCode != RESULT_OK)
        {
            return;
        }

        if(requestCode==1) {
            Toast.makeText(getApplicationContext(), "앨범", Toast.LENGTH_SHORT).show();

            final Bundle extras = intent.getExtras();

            if (extras != null)
                mImageCaptureUri = intent.getData();//intent uri를 가져와서

            try {
                BitmapFactory.Options options = new BitmapFactory.Options();//비트맵의 옵션 선택
                options.inJustDecodeBounds = true;

                int width = options.outWidth;
                int height = options.outHeight;

                int scaleFactor = Math.min(width / reviewImage.getWidth(), height / reviewImage.getHeight());

                Log.d(Integer.toString(width), Integer.toString(height));
                Log.d("scale", Integer.toString(scaleFactor));
//                    options.inPreferredConfig = bmConfig;
                options.inJustDecodeBounds = false;
                options.inSampleSize = scaleFactor * 2;
                options.inPurgeable = true;


                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageCaptureUri), null, options);
                //비트맵 객체 생성 ~
                reviewImage.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    private void sendDataServer(){
        SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy-MM-dd-HH:mm:ss", Locale.KOREA );
        Date currentTime = new Date();
        String dTime = formatter.format ( currentTime );

        ServerConnect.DataDownloadListener dataDownloadListener=new ServerConnect.DataDownloadListener() {
            @Override
            public void dataDownloadedSuccessfully(Object data) {
                String flag=(String)data;


                if (flag.equals(login_page.TAG)) {
                    Log.d("ssss", "프로필 성공");
                    finish();
                }
            }

            @Override
            public void dataDownloadFailed() {
                Log.d("dataDown"," - fail");
            }
        };
        HashMap hashMap=new HashMap();
        hashMap.put("cmd","writeReview");
        hashMap.put("uId",userId);
        hashMap.put("gId",groupId);
        hashMap.put("content",review_WriteText.getText().toString());
        hashMap.put("date",dTime);
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        if(bitmap!=null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream) ;
            byte[] byteArray = stream.toByteArray() ;
            hashMap.put("img",byteArray);
        }else{
            Drawable tmp=reviewImage.getDrawable();
            Bitmap defa=Bitmap.createBitmap(tmp.getIntrinsicWidth(), tmp.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            defa.compress(Bitmap.CompressFormat.JPEG, 100, stream) ;
            byte[] byteArray = stream.toByteArray() ;
            hashMap.put("img",byteArray);
        }

        login_page._serverConnect.setDataDownloadListener(dataDownloadListener);
        login_page._serverConnect.sendMsg(hashMap);

    }
}
