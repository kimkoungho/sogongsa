package com.example.koungho632.newsogongsaapplication.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.koungho632.newsogongsaapplication.ListItem.GroupMeetingListItem;
import com.example.koungho632.newsogongsaapplication.ListItem.MainListItem;
import com.example.koungho632.newsogongsaapplication.Page.MainActivity;
import com.example.koungho632.newsogongsaapplication.Page.MeetingGroup;
import com.example.koungho632.newsogongsaapplication.Page.login_page;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Server_Connection.ServerConnect;
import com.example.koungho632.newsogongsaapplication.View.ViewHolder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by koungho632 on 2016. 5. 12..
 */
public class GroupMainAdapter extends BaseAdapter {

    private Context context = null;
    private LayoutInflater inflater;
    private ArrayList<GroupMeetingListItem> data = null;
    private int layout;

    private ViewHolder viewHolder=null;
    private GroupMeetingListItem listItem;

    private TextView monthMeet=null;
    private TextView dayMeet=null;
    private TextView timeMeet=null;
    private com.example.koungho632.newsogongsaapplication.View.CircularImageView hobbyMeet=null;
    private TextView groupMeet=null;
    private TextView groupContentMeet=null;
    private TextView locationMeet=null;
    private TextView personCnt=null;
    public Button btn=null;
    private TextView money=null;

    public GroupMainAdapter(Context context, int layout, ArrayList<GroupMeetingListItem> data){
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.meeting_group_listview, parent,false);
//            convertView= inflater.inflate(layout, parent,false);
        }

//        Log.d("!11112323","그려보자");
        //뷰홀더에서 가져옴(있으면)

        monthMeet=viewHolder.get(convertView,R.id.monthMeet);
        dayMeet=viewHolder.get(convertView,R.id.dayMeet);
        timeMeet=viewHolder.get(convertView,R.id.timeMeet);
        hobbyMeet=viewHolder.get(convertView,R.id.hobbyMeet);
        groupMeet=viewHolder.get(convertView,R.id.groupMeet);
        groupContentMeet=viewHolder.get(convertView,R.id.groupContentMeet);
        locationMeet=viewHolder.get(convertView,R.id.locationMeet);
        personCnt=viewHolder.get(convertView,R.id.personCnt);
        btn=viewHolder.get(convertView, R.id.request_Btn);

        money=viewHolder.get(convertView,R.id.moneyMeet);

        listItem = data.get(position);

        //그리기
        monthMeet.setText(listItem.getMonth());
        dayMeet.setText(listItem.getDay());
        timeMeet.setText(listItem.getTime());
        hobbyMeet.setImageDrawable(listItem.getCategory());
        groupMeet.setText(listItem.getMeetingName());
        groupContentMeet.setText(listItem.getMeeting());
        locationMeet.setText(listItem.getLocation());
        personCnt.setText(listItem.getpCnt());
        btn.setBackground(context.getResources().getDrawable(R.drawable.btn_click));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(listItem.getId(),"1111111");
                apply("applyMeeting");
            }
        });
        money.setText(listItem.getCost());

        return convertView;
    }

    public void apply(String cmd){

        ServerConnect.DataDownloadListener dataDownloadListener1=new ServerConnect.DataDownloadListener() {
            @Override
            public void dataDownloadedSuccessfully(Object data) {
                String flag=(String)data;
                if(flag.equals(login_page.TAG)){
                    Log.d("신청 성공", ".......");

                }
            }

            @Override
            public void dataDownloadFailed() {

            }
        };
        login_page._serverConnect.setDataDownloadListener(dataDownloadListener1);
        HashMap hashMap=new HashMap();
        hashMap.put("cmd",cmd);
        hashMap.put("uId", MainActivity.UserId);
        hashMap.put("mId",listItem.getId());
        Log.d("1111111",hashMap.toString());
        login_page._serverConnect.sendMsg(hashMap);
    }
}
