package com.example.koungho632.newsogongsaapplication.ListItem;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by NT-RC510 on 2016-05-05.
 */
public class MainListItem {
    private String id;
    private Bitmap bigImage;
    private Drawable smallImage;
    private String mainText;

    public MainListItem(String id,Bitmap bigImage, Drawable smallImage, String mainText){
        this.id=id;
        this.bigImage = bigImage;
        this.smallImage = smallImage;
        this.mainText = mainText;
    }

    public String getId(){return this.id;}
    public Bitmap getBigImage(){
        return this.bigImage;
    }

    public Drawable getsmallImage(){
        return this.smallImage;
    }

    public String getMainText(){
        return this.mainText;
    }
}
