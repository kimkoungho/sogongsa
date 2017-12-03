package com.example.koungho632.newsogongsaapplication.Adapter;

/**
 * Created by koungho632 on 2016. 5. 12..
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.koungho632.newsogongsaapplication.ListItem.Meeting_ListItem;
import com.example.koungho632.newsogongsaapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by NT-RC510 on 2016-05-09.
 */
public class CalendarAdapter extends ArrayAdapter<Date>
{
    // days with events
    private ArrayList<Date> eventDays;
    private ArrayList<Date> days;

    // for view inflation
    private LayoutInflater inflater;

    Context context;

    // CalendarAdapter는 ArrayAdapter<Date>를 상속받아
    public CalendarAdapter(Context context, ArrayList<Date> days, ArrayList<Date> eventDays)
    {
        super(context, R.layout.control_calendar_day, days);
        this.days=days;
        this.eventDays = eventDays;
        inflater = LayoutInflater.from(context);
        this.context=context;
    }
    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Date getItem(int position) {
        return days.get(position);
    }

    //id 어카지
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        // day in question
        Date date = getItem(position);
        int day = date.getDate();
        int month = date.getMonth();
        int year = date.getYear();

        // today
        Date today = new Date();

        // inflate item if it does not exist yet
        if (view == null)
            view = inflater.inflate(R.layout.control_calendar_day, parent, false);

        // if this day has an event, specify event image
        view.setBackgroundResource(0);

        // clear styling
        ((TextView)view).setTypeface(null, Typeface.NORMAL);
        ((TextView)view).setTextColor(Color.BLACK);

        // 만약 이번달의 날짜가 아닌 날짜들은 모두 색깔을 grayed_out으로 변경한다.
        if (month != today.getMonth() || year != today.getYear())
        {
            // if this day is outside current month, grey it out
            ((TextView)view).setTextColor(context.getResources().getColor(R.color.greyed_out));
        }

        // 만약 오늘 날짜가 당일과 일치한다면, 당일의 날짜를 today Color로 변경한다.
        else if (day == today.getDate())
        {
            // if it is today, set it to blue/bold
            ((TextView)view).setTypeface(null, Typeface.BOLD);
            ((TextView)view).setTextColor(context.getResources().getColor(R.color.today));
        }

        // set text
        ((TextView)view).setText(String.valueOf(date.getDate()));

        if (eventDays != null)
        {
            for (Date eventDate : eventDays)
            {
                // 만약 오늘 일, 월, 년도가 eventDate와 같다면 따로 당일을 따로 표시한다.
                if (eventDate.getDate() == day && eventDate.getMonth() == month && eventDate.getYear() == year)
                {
                    Log.d("eventDate",eventDate.toString());
                    Log.d("date",date.toString());
                    // mark this day for event
                    ((TextView)view).setTextColor(context.getResources().getColor(R.color.sogongsa_Basic_Color));
                    ((TextView)view).setTypeface(null, Typeface.BOLD);
                }
            }
        }


            /*
            * 현재의 날짜와 그 외의 날짜의 색깔을 선정하고 정한다.
            * 여기서 회원이 등록한 일정을 만든 날짜에 대해서 색깔을 입히고
            * 버튼 리스너를 달아 그 색깔이 변경된 색깔과 일치한다면 !,
            * 일정의 목록을 확인하는 다이얼로그를 띄운다.
            */

        return view;
    }
}

