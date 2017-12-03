package com.example.koungho632.newsogongsaapplication.Page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koungho632.newsogongsaapplication.Adapter.MeetingGroupAdapter;
import com.example.koungho632.newsogongsaapplication.Dialog.ChangeDialog;
import com.example.koungho632.newsogongsaapplication.Dialog.CustomDialog;
import com.example.koungho632.newsogongsaapplication.Fragment.GroupMeetingFragment;
import com.example.koungho632.newsogongsaapplication.Fragment.GroupReviewFragment;
import com.example.koungho632.newsogongsaapplication.ListItem.GroupMeetingListItem;
import com.example.koungho632.newsogongsaapplication.ListItem.GroupMeetingReviewListItem;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Server_Connection.ServerConnect;

import java.util.ArrayList;
import java.util.HashMap;

//각 모임마다 다르게 보여야 함
public class MeetingGroup extends AppCompatActivity {

    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private MeetingGroupAdapter meetingGroupAdapter;

    private ImageView groupImg;
    private TextView groupText;

    // appBar Layout 의 ToolBar에 속하는 버튼, 이미지, 글
    private ImageButton home_Btn;
    private ImageView gCate;
    private TextView gName;

    private Toolbar toolbar;

    public static String gId;
    public String uId;

    public HashMap group;
    public ArrayList<HashMap> meeting;
    public ArrayList<HashMap> review;

    public Drawable[] hobby;
    Drawable btnBack;

    ChangeDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_meeting_group);

        btnBack=getDrawable(R.drawable.btn_click);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        groupImg=(ImageView)findViewById(R.id.group_image);
        groupText=(TextView)findViewById(R.id.group_content);

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout_group);



        home_Btn = (ImageButton) findViewById(R.id.app_HomeBtn);
        gCate=(ImageView)findViewById(R.id.gCategory);
        gName=(TextView)findViewById(R.id.gName);
        home_Btn.setOnClickListener(go_HomeListener);   // 홈으로 돌아가는 버튼 키 구현

        meetingGroupAdapter = new MeetingGroupAdapter(getApplicationContext(),getSupportFragmentManager());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_group);
        mViewPager = (ViewPager) findViewById(R.id.container_group);
        mViewPager.setAdapter(meetingGroupAdapter);

        tabLayout.setupWithViewPager(mViewPager);


        gId=getIntent().getStringExtra("gId");
        uId=getIntent().getStringExtra("uId");
    }

    @Override
    protected void onResume() {
        super.onResume();

        getGroupInfo();
    }

    public void getGroupInfo(){


        ServerConnect.DataDownloadListener dataDownloadListener=new ServerConnect.DataDownloadListener() {
            @Override
            public void dataDownloadedSuccessfully(Object data) {
                HashMap val=(HashMap)data;

                Log.d("serverDATA", data.toString());



                group=(HashMap)val.get("group");
                int i=Integer.parseInt((String)group.get("category"));
                gCate.setImageDrawable(setDrawable(i));
                gName.setText((String) group.get("name"));
                String attr=(String)group.get("attr");


                GroupMeetingFragment mg=(GroupMeetingFragment)meetingGroupAdapter.getItem(0);
                mg.groupComent.setText((String) group.get("content"));
                byte[] image=(byte[])group.get("img");
                Bitmap groupImg= BitmapFactory.decodeByteArray(image, 0, image.length);
                mg.groupImage.setImageBitmap(groupImg);


                meeting=(ArrayList)val.get("meeting");

                mg.meetingListItems.clear();
                for(HashMap item:meeting) {
                    String id = (String) item.get("id");
                    String date = (String) item.get("date");
                    String title = (String) item.get("title");
                    String location = (String) item.get("location");
                    String personCnt = (String) item.get("personCnt");
                    String content = (String) item.get("content");
                    String cost = (String) item.get("cost");

                    String mon = date.split("-")[0];
                    String day = date.split("-")[1];
                    String time = date.split("-")[2];

                    GroupMeetingListItem item1 = new GroupMeetingListItem(id, mon, day, time, setDrawable(i), title, content, location, personCnt, btnBack, cost);
                    mg.meetingListItems.add(item1);
                }
                mg.adapter.notifyDataSetChanged();

                review=(ArrayList)val.get("review");
                Drawable replyBtn=getResources().getDrawable(R.drawable.image_reple);
                GroupReviewFragment mgf=(GroupReviewFragment)meetingGroupAdapter.getItem(1);
                mgf.reviewListItems.clear();
                for(HashMap hashMap:review){
                    String rId=(String)hashMap.get("rId");

                    byte[] rImg=(byte[])hashMap.get("rImg");
                    Bitmap usrPicture=BitmapFactory.decodeByteArray(rImg,0,rImg.length);

                    String rContent=(String)hashMap.get("rCont");
                    String rDate=(String)hashMap.get("rDate");

                    byte[] uImg=(byte[])hashMap.get("uImg");
                    Bitmap usrProfile=BitmapFactory.decodeByteArray(uImg,0,uImg.length);

                    String uName=(String)hashMap.get("uName");

                    GroupMeetingReviewListItem item=new GroupMeetingReviewListItem(rId,uName,usrProfile,rDate,rContent,usrPicture,replyBtn);
                    mgf.reviewListItems.add(item);
                }
                mgf.reviewAdapter.notifyDataSetChanged();


                if(attr.equals("ok")==false){
                    Log.d("1111111","attr=false");
                    mg.fab_addfast.setImageDrawable(getResources().getDrawable(R.drawable.apply));
                    mg.fab_addfast.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog=new ChangeDialog(MeetingGroup.this,R.style.Theme_Dialog);
                            dialog.setCustomText("해당 모임에 가입하시겠습니까?");
                            dialog.setOkBtn("ok");
                            View.OnClickListener okListener=new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    apply("applyGroup");
                                    dialog.dismiss();
                                }
                            };
                            dialog.okListener(okListener);

                            dialog.setCancel_Btn("cancel");
                            View.OnClickListener cancelListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            };
                            dialog.cancelListener(cancelListener);
                            dialog.show();
                        }
                    });
                    mgf.fab_Review.setImageDrawable(getResources().getDrawable(R.drawable.apply));
                    mgf.fab_Review.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog=new ChangeDialog(MeetingGroup.this,R.style.Theme_Dialog);
                            dialog.setCustomText("해당 모임에 가입하시겠습니까?");
                            dialog.setOkBtn("ok");
                            View.OnClickListener okListener=new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    apply("applyGroup");
                                    //dialog.dismiss();
                                }
                            };
                            dialog.okListener(okListener);

                            dialog.setCancel_Btn("cancel");
                            View.OnClickListener cancelListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            };
                            dialog.cancelListener(cancelListener);
                            dialog.show();
                        }
                    });
                }else
                    Log.d("11111111","attr=true");
            }

            public void apply(String cmd){

                ServerConnect.DataDownloadListener dataDownloadListener1=new ServerConnect.DataDownloadListener() {
                    @Override
                    public void dataDownloadedSuccessfully(Object data) {
                        String flag=(String)data;
                        if(flag.equals(login_page.TAG)){
                            Log.d("신청 성공",".......");
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void dataDownloadFailed() {

                    }
                };
                login_page._serverConnect.setDataDownloadListener(dataDownloadListener1);
                HashMap hashMap=new HashMap();
                hashMap.put("cmd",cmd);
                hashMap.put("uId",MainActivity.UserId);
                hashMap.put("gId",gId);
                hashMap.put("uImg",MainActivity.bytes);
                hashMap.put("uName",MainActivity.UserName);
                hashMap.put("gName", (String) group.get("name"));
                Log.d("1111111",hashMap.toString());
                login_page._serverConnect.sendMsg(hashMap);
            }

            @Override
            public void dataDownloadFailed() {
                Log.d("dataDown"," - fail");
            }


            public Drawable setDrawable(int index){
                Drawable img=null;
                switch (index){
                    case 1:img=getResources().getDrawable(R.drawable.category_study);break;
                    case 2:img=getResources().getDrawable(R.drawable.category_book);break;
                    case 3:img=getResources().getDrawable(R.drawable.category_music);break;
                    case 4:img=getResources().getDrawable(R.drawable.category_travel);break;
                    case 5:img=getResources().getDrawable(R.drawable.category_movie);break;
                    case 6:img=getResources().getDrawable(R.drawable.category_love);break;
                    default:break;
                }
                return img;
            }
        };

        HashMap hashMap=new HashMap();
        hashMap.put("cmd","group");
        hashMap.put("id",gId);
        hashMap.put("uId",MainActivity.UserId);

        login_page._serverConnect.setDataDownloadListener(dataDownloadListener);
        login_page._serverConnect.sendMsg(hashMap);
    }

    // 홈 버튼이 눌러졌을때 메인으로 넘어간다.
    private ImageButton.OnClickListener go_HomeListener = new ImageButton.OnClickListener(){
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
