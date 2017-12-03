package com.example.koungho632.newsogongsaapplication.ListItem;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ReviewRepleListItem {
    private Bitmap repleUserImage;
    private String repleUserName;
    private String repleUserComment;

    public ReviewRepleListItem(Bitmap repleUserImage, String repleUserName, String repleUserComment){
        this.repleUserImage = repleUserImage;
        this.repleUserName = repleUserName;
        this.repleUserComment = repleUserComment;
    }

    public Bitmap getRepleUserImage(){
        return this.repleUserImage;
    }

    public String getRepleUserName(){
        return this.repleUserName;
    }

    public String getRepleUserComment(){
        return this.repleUserComment;
    }
}
