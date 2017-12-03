package com.example.koungho632.newsogongsaapplication.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.Context;

import com.example.koungho632.newsogongsaapplication.Fragment.GroupMeetingFragment;
import com.example.koungho632.newsogongsaapplication.Fragment.GroupReviewFragment;
import com.example.koungho632.newsogongsaapplication.R;


/**
 * Created by koungho632 on 2016. 5. 10..
 */
public class MeetingGroupAdapter extends FragmentPagerAdapter{
    Fragment[] fragments;

    private Context context;

    public MeetingGroupAdapter(Context context,FragmentManager fm) {
        super(fm);
        this.context=context;
        fragments=new Fragment[]{
                new GroupMeetingFragment(),
                new GroupReviewFragment()
        };
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
    //탭 타이틀 지정하기
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.home);
            case 1:
                return context.getString(R.string.meeting);
        }
        return null;
    }
}
