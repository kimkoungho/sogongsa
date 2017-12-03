package com.example.koungho632.newsogongsaapplication.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.Context;

import com.example.koungho632.newsogongsaapplication.Fragment.MainFragment;
import com.example.koungho632.newsogongsaapplication.Fragment.GroupFragment;
import com.example.koungho632.newsogongsaapplication.Fragment.CalendarFragment;
import com.example.koungho632.newsogongsaapplication.R;

/**
 * Created by koungho632 on 2016. 5. 4..
 */
public class MainAdapter extends FragmentPagerAdapter {

    Fragment[] fragments;

    private Context context;
    public MainAdapter(Context context,FragmentManager fm) {
        super(fm);
        this.context=context;
        fragments=new Fragment[]{
                new MainFragment(),
                new GroupFragment(),
                new CalendarFragment()
        };
    }

    //프래그먼트 리턴
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragments[position];
    }

    @Override
    public int getCount(){
        // Show 3 total pages.
        return 3;
    }

    //탭 타이틀 지정하기
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.home);
            case 1:
                return context.getString(R.string.meeting);
            case 2:
                return context.getString(R.string.notice);
        }
        return null;
    }
}
