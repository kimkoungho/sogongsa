package com.example.koungho632.newsogongsaapplication.ListItem;

import android.graphics.drawable.Drawable;

/**
 * Created by koungho632 on 2016. 5. 12..
 */
public class GroupMeetingListItem {

    private String id;
    private String month;//?월
    private String day;//11
    private String time;//오후 5시

    private Drawable category;//카테고리 이미지
    private String meetingName;//모임 이름
    private String meeting;//해당 만남내용
    private String location;//만남 위치
    private String pCnt;
    private Drawable btn;
    String cost;

    public GroupMeetingListItem(String id,String month, String day,String time, Drawable category, String meetingName, String meeting, String location,String pCnt,Drawable btn,String cost){
        this.id=id;
        this.month=month;
        this.day=day;
        this.time=time;
        this.category=category;
        this.meetingName=meetingName;
        this.meeting=meeting;
        this.location=location;
        this.pCnt=pCnt;
        this.btn=btn;
        this.cost=cost;
    }
    public String getCost(){return cost;}
    public Drawable getBtn(){return btn;}
    public String getpCnt(){return pCnt;}
    public String getId(){return id;}
    public String getMonth() {return month;}
    public String getDay() {return day;}
    public String getTime() {return time;}
    public Drawable getCategory() {return category;}
    public String getMeetingName() {return meetingName;}
    public String getMeeting() {return meeting;}
    public String getLocation() {return location;}

}
