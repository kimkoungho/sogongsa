package com.example.koungho632.newsogongsaapplication.Page;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koungho632.newsogongsaapplication.Adapter.MainAdapter;
import com.example.koungho632.newsogongsaapplication.Dialog.CustomDialog;
import com.example.koungho632.newsogongsaapplication.Fragment.CalendarFragment;
import com.example.koungho632.newsogongsaapplication.Fragment.MainFragment;
import com.example.koungho632.newsogongsaapplication.Fragment.GroupFragment;
import com.example.koungho632.newsogongsaapplication.ListItem.MainListItem;
import com.example.koungho632.newsogongsaapplication.ListItem.Meeting_ListItem;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Server_Connection.ServerConnect;
import com.example.koungho632.newsogongsaapplication.View.CircularImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG="q*%u*%i*%t";


    private MainAdapter mainAdapter;

    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationview;
    private ActionBarDrawerToggle toggle;

    View navigationHeader;

    CircularImageView userProfile;
    TextView navUserName;
    TextView navUserBirthday;
    TextView navUserContent;

    ImageView[] nav_user_hobby;

    public HashMap userInfo;
    public ArrayList<HashMap> fragMain;
    public ArrayList<HashMap> fragMeet;
    public static ArrayList<HashMap> userNoti;
    public ArrayList fragCalendar;


    public static String UserId;
    public static String UserPw;
    public static String UserName;
    public static Bitmap profile;
    public static byte[] bytes;
    public static int myDay;
    public static int myMonth;

    boolean startFlag=true;

    private CustomDialog cusDialog; // 백 버튼을 눌렀을 경우 나타나는 다이얼로그(05.22)

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        UserId=getIntent().getStringExtra("userId");
        UserPw=getIntent().getStringExtra("userPw");

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mainAdapter = new MainAdapter(getApplicationContext(),getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mainAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        // ActionBarDrawerToggle 을 통해 Toolbar 와 DrawerLayout 을 연결
        // Toolbar 에 DrawerArrowDrawerbleToggle 이 NavigationIcon 로 등록되고 (hamburger icon)
        // NavigationOnClickListner 에 DrawerLayout open, close 관련 기능이 연결됨.
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // DrawerLayout의 DrawerListener 로 ActionBarDrawerToggle 이 연결되고, 기본적으로는 DrawerLayout 상태에 따라
        // DrawerArrowDrawerbleToggle 이 back icon 과 hamburger icon 으로 변경되는 코드가 등록되어있음.
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();// 현재 Drawerlayout 상태와 ActionBarDrawerToggle 의 상태를 sync

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        mNavigationview=(NavigationView)findViewById(R.id.nav_view);
        mNavigationview.setNavigationItemSelectedListener(this);

        ///nav 헤더 부분
        navigationHeader = mNavigationview.inflateHeaderView(R.layout.nav_header_main);
        userProfile=(CircularImageView)navigationHeader.findViewById(R.id.user_profile);
        navUserBirthday=(TextView)navigationHeader.findViewById(R.id.nav_user_birthday);
        navUserName=(TextView)navigationHeader.findViewById(R.id.nav_user_name);
        navUserContent=(TextView)navigationHeader.findViewById(R.id.nav_user_content);
        nav_user_hobby=new ImageView[]{((ImageView)navigationHeader.findViewById(R.id.nav_user_hobby_1)),
                ((ImageView)navigationHeader.findViewById(R.id.nav_user_hobby_2)),
                ((ImageView)navigationHeader.findViewById(R.id.nav_user_hobby_3))};

    }

    @Override
    protected void onResume() {
        super.onResume();
        getServer();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu=menu;
        super.onCreateOptionsMenu(menu);

        if(startFlag){
            getMenuInflater().inflate(R.menu.menu_main, menu);// xml menu
        }else
            getMenuInflater().inflate(R.menu.menu_main2, menu);// xml menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.action_search){
            Intent intent=new Intent(getApplicationContext(),search_page.class);
            startActivity(intent);
        }
        else if(id==R.id.action_noti){
            Intent intent=new Intent(getApplicationContext(),Notification_page.class);
            //intent.putExtra("userNoti",userNoti);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            onResume();
        }

        else if (id == R.id.nav_profile) {
            Intent intent=new Intent(getApplicationContext(),ProfileImageChange.class);
            intent.putExtra("userId",UserId);
            intent.putExtra("userName",(String)userInfo.get("name"));
            intent.putExtra("userAge",(String)userInfo.get("age"));
            intent.putExtra("userContent",(String)userInfo.get("comment"));
            startActivity(intent);
        }

        else if (id == R.id.nav_alterInfo) {
            Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
            intent.putExtra("userId",UserId);
            intent.putExtra("userPw",UserPw);
            intent.putExtra("userHobby",(String)userInfo.get("hobby"));
            startActivity(intent);
        }

        else if(id==R.id.nav_logout){
            final CustomDialog cuDialog = new CustomDialog(MainActivity.this, R.style.Theme_Dialog);
            cuDialog.setCustomText(" Logout ? ");
            cuDialog.show();
            // 다이얼로그의 텍스트에 Logout? 글자를 설정하고 다이얼로그를 띄운다.
            // OK버튼을 눌렀을 시 로그인화면으로 이동, CANCEL버튼을 눌렀을 시 다이얼로그 닫기
            cuDialog.getOk_Btn().setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    login_page.save_id.setChecked(false);
                    login_page.auto_login.setChecked(false);
                    finish();
                }
            });

            cuDialog.getCancel_Btn().setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    cuDialog.dismiss();
                }
            });

        }

        return true;
    }

    public void getServer(){


        ServerConnect.DataDownloadListener dataDownloadListener=new ServerConnect.DataDownloadListener() {
            @Override
            public void dataDownloadedSuccessfully(Object data) {
                if(data==null){
                    Log.d("data  ","null");
                    return;
                }else{
                    Log.d("type",data.toString());
                }


                HashMap serverData=(HashMap)data;
                userNoti=(ArrayList)serverData.get("userNoti");
                //사용자의 노티를 받는다(알림아이콘 의 토글 여부)

                if(userNoti.size()>1){
                    startFlag=false;
                    menu.clear();
                    onCreateOptionsMenu(menu);
                }

                //사용자의 정보를 받는다 네비게이션 헤더
                userInfo=(HashMap)serverData.get("userInfo");
                bytes=(byte[])userInfo.get("img");
                profile= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                UserName=(String)userInfo.get("name");


                userProfile.setImageBitmap(profile);
                navUserName.setText(UserName);
                navUserBirthday.setText((String)userInfo.get("age"));
                navUserContent.setText((String) userInfo.get("comment"));


                String tmp=(String)userInfo.get("hobby");
                Log.d("1111",tmp);



                for(int i=0; i<3; i++){
                    if(i<tmp.length()){
                        int ind=tmp.charAt(i)-'0';
                        nav_user_hobby[i].setImageDrawable(setDrawable(ind));
                    }else{
                        nav_user_hobby[i].setVisibility(View.INVISIBLE);
                    }
                }

                //사용자가 등록되어 있는 그룹의 정보를 받는다
                fragMain=(ArrayList)serverData.get("myGroup");
                MainFragment mainFragment=(MainFragment)mainAdapter.getItem(0);
                mainFragment.data.clear();
                for(HashMap item:fragMain) {
                    String gId=(String)item.get("gId");
                    String name=(String)item.get("name");
                    byte[] bytes1=(byte [])item.get("img");
                    Bitmap gImg=BitmapFactory.decodeByteArray(bytes1,0,bytes1.length);
                    String category=(String)item.get("category");

                    MainListItem listItem=new MainListItem(gId,gImg,setDrawable(Integer.parseInt(category)),name);
                    mainFragment.data.add(listItem);
                }
                mainFragment.adapter.notifyDataSetChanged();

                //전체 모임정보를 받는다
                fragMeet=(ArrayList)serverData.get("groupAll");
                GroupFragment mf=(GroupFragment)mainAdapter.getItem(1);

                mf.myMeetingList.clear();
                for(HashMap item:fragMeet) {
                    String id = (String) item.get("id");
                    String category = (String) item.get("category");
                    Drawable image = setDrawable(Integer.parseInt(category));
                    String title = (String) item.get("name");
                    String person = (String) item.get("uName");
                    String personNum = (String) item.get("personNum");
                    Meeting_ListItem item1 = new Meeting_ListItem(id, image, title, person, personNum);
                    mf.myMeetingList.add(item1);
                }
                mf.adapter.notifyDataSetChanged();

                //사용자의 오프라인 만남 정보를 받아서 캘린더에 뿌린다
                ArrayList userMeet = (ArrayList)serverData.get("userMeet");
                CalendarFragment cf=(CalendarFragment)mainAdapter.getItem(2);

                cf.event=new ArrayList();

                for(int i=0; i<userMeet.size(); i++){
                    String meetingDate=(String)userMeet.get(i);
                    int month=Integer.parseInt(meetingDate.substring(0,2));
                    int day=Integer.parseInt(meetingDate.substring(3,5));
                    int hour=Integer.parseInt(meetingDate.substring(6,8));
                    int min=Integer.parseInt(meetingDate.substring(9));
                    Date date=new Date();
                    date.setYear(date.getYear());
                    date.setMonth(month-1);
                    date.setDate(day);
                    date.setHours(hour);
                    date.setMinutes(min);
                    cf.event.add(date);
                }

            }

            @Override
            public void dataDownloadFailed() {

            }
            public Drawable setDrawable(int index){
                Drawable img=null;
                switch (index){
                    case 1:img=getResources().getDrawable(R.drawable.category_study);break;
                    case 2:img=getResources().getDrawable(R.drawable.category_book);break;
                    case 3:img=getResources().getDrawable(R.drawable.category_travel);break;
                    case 4:img=getResources().getDrawable(R.drawable.category_music);break;
                    case 5:img=getResources().getDrawable(R.drawable.category_movie);break;
                    case 6:img=getResources().getDrawable(R.drawable.category_love);break;
                    default:break;
                }
                return img;
            }
        };


        login_page._serverConnect.setDataDownloadListener(dataDownloadListener);
        HashMap hashMap=new HashMap();
        hashMap.clear();
        hashMap.put("cmd", "main");
        hashMap.put("id", UserId);


        login_page._serverConnect.sendMsg(hashMap);
    }

    // 휴대폰의 백버튼을 눌렀을 경우 이벤트 처리를 담당하는 코드아래
    // 메인 액티비티에서 백 버튼을 눌렀을 경우 다이얼로그 출력 후, 사용자 대답에 따라 유지 혹은 종료
    @Override
    public void onBackPressed(){
        cusDialog = new CustomDialog(MainActivity.this, R.style.Theme_Dialog);
        cusDialog.setCustomText("Exit ?");
        cusDialog.setImage(profile);
        cusDialog.setUserName(UserName);

        cusDialog.getOk_Btn().setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_SHORT).show();
                finish();
                login_page.activity.finish();

            }
        });

        cusDialog.getCancel_Btn().setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                cusDialog.dismiss();
            }
        });

        // cusDialog의 위치와 내용 설정 및 리스너 등록 후, 다이얼로그 출력
        cusDialog.show();
    }
}