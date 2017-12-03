package com.example.koungho632.newsogongsaapplication.Page;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Server_Connection.ServerConnect;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;


public class CreatingGroup extends AppCompatActivity{
    private Bitmap groupBitmap = null;       // 이미지 비트맵
    private ImageView groupImage = null;     // 사용자 이미지

    private ImageButton cameraButton = null;
    private TextView complete_id=null;//완료

    private EditText createTitle=null;//제목
    private EditText createContent=null; //내용

    private Spinner meetingSelect=null;//카테고리 선택
    private EditText createNum=null;//모집인원

    public Uri mImageCaptureUri=null;

    Intent intent;
    //private CompleteDialog cdialog; // 커스텀 다이얼로그를 불러오기 위한 변수
    private Dialog cdialog;
    String uId;

    // 스크롤 뷰 안에 에디트 텍스트 스크롤이 안 움직이는 문제를 해결하기 위함
    private ScrollView ccScrollView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_content);

        //////////// Scroll View 안에 TextView의 스크롤을 쉽게 움직이기 위한 코드 //////////////
        ccScrollView = (ScrollView) findViewById(R.id.ccScrollView_id);

        //cameraButton = (ImageButton) findViewById(R.id.camera_Btn);
        //카메라 버튼으로 사진이 등록되게 하면 좋을텐데.. 05.22

        groupImage = (ImageView) findViewById(R.id.userImage2);
        groupImage.setOnClickListener(new View.OnClickListener() {
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

                new AlertDialog.Builder(CreatingGroup.this)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("앨범선택",albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            }
        });

        uId=getIntent().getStringExtra("uId");

        // 이전 액티비티에서 받아온 이미지를 본 액티비티에서 설정

        complete_id=(TextView)findViewById(R.id.complete_id);
        complete_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean flag=true;

                if(meetingSelect.equals("")){
                    //다이얼로그
                    AlertDialog.Builder alert = new AlertDialog.Builder(CreatingGroup.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("카테고리를 선택해주세요.");
                    alert.show();
                    flag=false;
                    meetingSelect.requestFocus();
                }
                if(createNum.equals("선택")){
                    //다이얼로그
                    AlertDialog.Builder alert = new AlertDialog.Builder(CreatingGroup.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("모집 인원을 적어주세요.\n제한이 없으면 0");
                    alert.show();
                    flag=false;
                    createNum.requestFocus();
                }
                if(createTitle.equals("")){
                    //다이얼로그
                    AlertDialog.Builder alert = new AlertDialog.Builder(CreatingGroup.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("모집 제목을 적어주세요.");
                    alert.show();
                    flag=false;
                    createTitle.requestFocus();
                }
                if(createContent.equals("")){
                    //다이얼로그
                    AlertDialog.Builder alert = new AlertDialog.Builder(CreatingGroup.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("모임 내용을 적어주세요.");
                    alert.show();
                    flag=false;
                    createContent.requestFocus();
                }


                if(flag) {
                    //다이얼로그

                    //
                    int meetIndex = meetingSelect.getSelectedItemPosition();
                    Log.d("meetIndex - ", Integer.toString(meetIndex));


                    String restrictNum = createNum.getText().toString();
                    Log.d("restrictNum - ", restrictNum);

                    String title = createTitle.getText().toString();
                    Log.d("title - ", title);

                    String content = createContent.getText().toString();
                    Log.d("content - ", content);


                    HashMap hashMap=new HashMap();
                    hashMap.put("cmd","createGroup");
                    hashMap.put("name",title);
                    hashMap.put("content",content);
                    hashMap.put("uId",uId);
                    hashMap.put("category",Integer.toString(meetIndex));
                    if(groupBitmap!=null){
                        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
                        groupBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream) ;
                        byte[] byteArray = stream.toByteArray() ;
                        hashMap.put("image",byteArray);
                    }else
                        hashMap.put("image",null);



                    //서버에서 전송받은 결과
                    ServerConnect.DataDownloadListener dataDownloadListener = new ServerConnect.DataDownloadListener() {
                        @Override
                        public void dataDownloadedSuccessfully(Object data) {
                            String ret = (String) data;
                            if(ret.equals(login_page.TAG)){
                                Log.d("성공",".......");
                                finish();
                            }
                        }

                        @Override
                        public void dataDownloadFailed() {

                        }
                    };

                    login_page._serverConnect.setDataDownloadListener(dataDownloadListener);
                    login_page._serverConnect.sendMsg(hashMap);
                }
            }
        });

        meetingSelect=(Spinner)findViewById(R.id.meeting_select);
        createNum=(EditText)findViewById(R.id.create_num);
        createTitle=(EditText)findViewById(R.id.create_title);

        createContent=(EditText)findViewById(R.id.create_content);
        createContent.setMovementMethod(new ScrollingMovementMethod()); // 스크롤 선언
        // 스크롤 뷰에서 에디트 텍스트로 터치되었을때 스크롤은 이제 에디트 텍스가 가능하다.
        createContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ccScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////
    }

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
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == 1) {


            Toast.makeText(getApplicationContext(), "앨범", Toast.LENGTH_SHORT).show();

            final Bundle extras = intent.getExtras();

            if (extras != null)
                mImageCaptureUri = intent.getData();//intent uri를 가져와서

            try {
                BitmapFactory.Options options = new BitmapFactory.Options();//비트맵의 옵션 선택
                options.inJustDecodeBounds = true;
//                    BitmapFactory.decodeFile(mImageCaptureUri.getPath(),options);

                BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(mImageCaptureUri), null, options);

                int width = options.outWidth;
                int height = options.outHeight;

                int scaleFactor = Math.min(width / groupImage.getWidth(), height / groupImage.getHeight());

                Log.d(Integer.toString(width), Integer.toString(height));
                Log.d("scale", Integer.toString(scaleFactor));
//                    options.inPreferredConfig = bmConfig;
                options.inJustDecodeBounds = false;
                options.inSampleSize = scaleFactor * 2;
                options.inPurgeable = true;


                groupBitmap = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(mImageCaptureUri), null, options);
                //비트맵 객체 생성 ~

                //
//                    Bitmap resized = Bitmap.createScaledBitmap(mImageBitmap, dstWidth, dstHeight, true);


                groupImage.setImageBitmap(groupBitmap);
                //비트맵 이미지 뷰에 등록
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
