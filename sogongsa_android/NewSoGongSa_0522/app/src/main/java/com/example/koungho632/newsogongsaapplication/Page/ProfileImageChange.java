package com.example.koungho632.newsogongsaapplication.Page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Server_Connection.ServerConnect;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class ProfileImageChange extends AppCompatActivity{

    ImageView userProfileImg=null;
    ImageButton change=null;

    EditText inputUserName = null;
    EditText inputUserAge=null;
    EditText inputUserContent=null;

    Button profileSaveBtn=null;


    private static final int PICK_FROM_ALBUM = 1;


    public Uri mImageCaptureUri=null;
    String url;
    Bitmap bitmap=null;

    boolean start=false;

    int size;//이미지 변경시 이미지의 크기

    String userId;
    String userName;
    String userBirth;
    String userContent;
    Bitmap userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image_change);

        userProfileImg=(ImageView)findViewById(R.id.user_profile_img);
        change=(ImageButton)findViewById(R.id.img_change);

        inputUserName = (EditText) findViewById(R.id.input_user_name);
        inputUserAge = (EditText) findViewById (R.id.input_user_age);
        inputUserContent = (EditText) findViewById (R.id.input_content);

        profileSaveBtn=(Button)findViewById(R.id.profile_save_btn);

        userId=getIntent().getStringExtra("userId");
        userName=getIntent().getStringExtra("userName");
        userBirth=getIntent().getStringExtra("userAge");
        userContent=getIntent().getStringExtra("userContent");
        //userProfile=(Bitmap)getIntent().getExtras().get("userProfile");

        userProfile=MainActivity.profile;

        Log.d(userId,userName);

        //사용자의 기본 정보를 삽입
        userProfileImg.setImageBitmap(userProfile);
        inputUserName.setText(userName);
        inputUserAge.setText(userBirth);
        inputUserContent.setText(userContent);

        change.setOnClickListener(new View.OnClickListener() {
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

                new AlertDialog.Builder(ProfileImageChange.this)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("앨범선택",albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            }
        });

        // 변경된 정보를 저장
        // 그 전에 스트링으로 이름과 나이 소개하는 말을 '&' 로 구분시켜준다.
        // 프로필 사진도 같이 전송
        profileSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName=inputUserName.getText().toString();
                userBirth=inputUserAge.getText().toString();
                userName=inputUserName.getText().toString();
                userContent=inputUserContent.getText().toString();
                //서버로 데이터 전송
                sendDataServer();
            }
        });
    }


    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return (cursor.getString(column_index));
    }


    /**
     * 앨범에서 이미지 가져오기
     */
    private void doTakeAlbumAction()
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        //
//        Environment.DIRECTORY_DCIM
        intent.setData(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //
        /*intent.setAction(Intent.ACTION_GET_CONTENT);*/
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

        if(requestCode==PICK_FROM_ALBUM) {


            Toast.makeText(getApplicationContext(), "앨범", Toast.LENGTH_SHORT).show();

            final Bundle extras = intent.getExtras();

            if (extras != null)
                mImageCaptureUri = intent.getData();//intent uri를 가져와서

            try {
                BitmapFactory.Options options = new BitmapFactory.Options();//비트맵의 옵션 선택
                options.inJustDecodeBounds = true;
//                    BitmapFactory.decodeFile(mImageCaptureUri.getPath(),options);

//                BitmapFactory.decodeStream(
//                        getContentResolver().openInputStream(mImageCaptureUri), null, options);

                int width = options.outWidth;
                int height = options.outHeight;

                int scaleFactor = Math.min(width / userProfileImg.getWidth(), height / userProfileImg.getHeight());

                Log.d(Integer.toString(width), Integer.toString(height));
                Log.d("scale", Integer.toString(scaleFactor));
//                    options.inPreferredConfig = bmConfig;
                options.inJustDecodeBounds = false;
                options.inSampleSize = scaleFactor * 2;
                options.inPurgeable = true;


                bitmap = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(mImageCaptureUri), null, options);
                //비트맵 객체 생성 ~

                //
//                    Bitmap resized = Bitmap.createScaledBitmap(mImageBitmap, dstWidth, dstHeight, true);

                url = getPath(mImageCaptureUri);

                Log.d("url", url);

                userProfileImg.setImageBitmap(bitmap);
                //비트맵 이미지 뷰에 등록


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

//    public Bitmap rotateImage(Bitmap src, float degree) {
//
//        // Matrix 객체 생성
//        Matrix matrix = new Matrix();
//        // 회전 각도 셋팅
//        matrix.postRotate(degree);
//        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
//        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
//                src.getHeight(), matrix, true);
//    }

    private void sendDataServer(){

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
        hashMap.put("cmd","profile");
        hashMap.put("id",userId);
        if(bitmap!=null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream) ;
            byte[] byteArray = stream.toByteArray() ;
            hashMap.put("profile",byteArray);
        }else{
        }
        hashMap.put("name",userName);
        hashMap.put("age",userBirth);
        hashMap.put("content",userContent);

        login_page._serverConnect.setDataDownloadListener(dataDownloadListener);
        login_page._serverConnect.sendMsg(hashMap);

    }
}
