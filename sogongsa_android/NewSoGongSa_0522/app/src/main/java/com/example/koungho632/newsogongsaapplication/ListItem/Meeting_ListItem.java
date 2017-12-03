package com.example.koungho632.newsogongsaapplication.ListItem;

import android.graphics.drawable.Drawable;

/**
 * Created by koungho632 on 2016. 5. 4..
 */
public class Meeting_ListItem {
    private Drawable image;     // 리스트 이미지
    private String title;       // 작성 제목
    private String person;      // 작성자
    private String personNum;   // 모임정원
    private String id;

    // 리스트 생성시 필요한 값을
    // 삽입하기위한 생성자

    /*
    *   현재 이미지를 넣는 생성자를 만들지 않았다.
    */
    public Meeting_ListItem(String id,Drawable image, String title, String person, String personNum){
        this.id=id;
        this.image = image;
        this.title = title;
        this.person = person;
        this.personNum = personNum;
    }


    // 리스트아이템의 내용값들을 반환
    public String getId(){return this.id;}
    public Drawable getImage() { return this.image; }
    public String getTitle(){
        return this.title;
    }
    public String getPerson(){
        return this.person;
    }
    public String getPersonNum(){
        return this.personNum;
    }
    //public String getDate() { return this.date; }
}
