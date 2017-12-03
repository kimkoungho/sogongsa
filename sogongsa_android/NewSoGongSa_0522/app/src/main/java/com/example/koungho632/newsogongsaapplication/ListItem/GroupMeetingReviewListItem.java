package com.example.koungho632.newsogongsaapplication.ListItem;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;

import com.example.koungho632.newsogongsaapplication.Adapter.MeetingGroupReviewAdapter;

/**
 * Created by NT-RC510 on 2016-05-12.
 */
public class GroupMeetingReviewListItem {
    private String id;
    private Bitmap userProfile;         //  유저 프로필 사진
    private String userName;              //  유저 이름
    private String reviewDate;            //  유저가 올린 게시글의 날짜
    private String reviewContent;         //  유저가 올린 게시글의 내용
    private Bitmap userPicture;         //  유저가 올린 게시글의 사진
    private Drawable repleBtn;            //  게시글의 사진내에 이미지 버튼(리플)

    public GroupMeetingReviewListItem(String id, String userName,Bitmap userProfile,
                                      String reviewDate, String reviewContent,
                                      Bitmap userPicture,Drawable repleBtn) {
        this.id=id;
        this.userName = userName;
        this.userProfile=userProfile;
        this.reviewDate = reviewDate;
        this.reviewContent = reviewContent;
        this.userPicture=userPicture;
        this.repleBtn=repleBtn;
    }

    public String getId(){return id;}

    public Bitmap getUserProfile(){
        return this.userProfile;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getReviewDate(){
        return this.reviewDate;
    }

    public String getReviewContent(){
        return this.reviewContent;
    }

    public Bitmap getUserPicture(){
        return this.userPicture;
    }

    public Drawable getRepleBtn(){return this.repleBtn;}

    public void setAdapter(MeetingGroupReviewAdapter adapter) {
        this.setAdapter(adapter);
    }
}
