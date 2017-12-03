package com.example.koungho632.newsogongsaapplication.ListItem;

import android.graphics.Bitmap;

/**
 * Created by koungho632 on 2016. 5. 25..
 */
public class Noti_listitem {
    Bitmap userImg;
    String noti;
    String sw;
    String uId;
    String id;

    public Noti_listitem(Bitmap userImg, String noti,String sw,String uId,String id){
        this.noti=noti;
        this.userImg=userImg;
        this.sw=sw;
        this.uId=uId;
        this.id=id;
    }
    public Bitmap getUserImg(){return this.userImg;}
    public String getNoti(){return this.noti;}
    public String getSw(){return this.sw;}
    public String getuId(){return this.uId;}
    public String getId(){return this.id;}
}
