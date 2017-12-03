package com.example.koungho632.newsogongsaapplication.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koungho632.newsogongsaapplication.Adapter.CalendarAdapter;
import com.example.koungho632.newsogongsaapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by koungho632 on 2016. 5. 4..
 */
public class CalendarFragment extends Fragment {
    private static final String LOGTAG = "Calendar View";

    // how many days to show, defaults to six weeks, 42 days
    // 총 42일 ( 6주 * 7일 = 42 일 )
    // 6주 => 6줄을 보여주기 위해서 상수 형태로 설정
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // date format
    private String dateFormat=DATE_FORMAT;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance();


    // internal components
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private GridView grid;

    // 시즌 별로 색깔을 다르게 표시함.
    // seasons' rainbow
    int[] rainbow = new int[] {
            R.color.summer,
            R.color.fall,
            R.color.winter,
            R.color.spring
    };
    // month-season association (northern hemisphere, sorry australia :)
    int[] monthSeason = new int[] {2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};
    private static final String ARG_SECTION_NUMBER = "section_number";

    public ArrayList event=null;
    public ArrayList<Date> cells=null;
    public CalendarAdapter adapter=null;

    public static final String ARG_PAGE = "ARG_PAGE";

    public static CalendarFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        CalendarFragment fragment = new CalendarFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        header=(LinearLayout)rootView.findViewById(R.id.calendar_header);
        btnPrev=(ImageView)rootView.findViewById(R.id.calendar_prev_button);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
                adapter=new CalendarAdapter(getContext(), cells, event);
                grid.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        btnNext=(ImageView)rootView.findViewById(R.id.calendar_next_button);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, +1);
                updateCalendar();
                adapter=new CalendarAdapter(getContext(), cells, event);
                grid.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        txtDate=(TextView)rootView.findViewById(R.id.calendar_date_display);
        grid=(GridView)rootView.findViewById(R.id.calendar_grid);


        updateCalendar(event);

        adapter=new CalendarAdapter(getContext(), cells, event);
        grid.setAdapter(adapter);


        return rootView;
    }

    public void updateCalendar()
    {
        updateCalendar(null);
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(ArrayList<Date> events)
    {
        Log.d("curentdate",currentDate.toString());
        // private Calendar currentDate를 Calendar 객체에 복사하고
        // calendar를 달을 -1 값으로 설정한다.
        cells = new ArrayList<>();
        Calendar calendar = (Calendar)currentDate.clone();

        // determine the cell for current month's beginning
        // 시작하는 month의 값을 설정한다.
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT)
        {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        // 캘린더로 이루어진 gridView를 setAdapter
        adapter=new CalendarAdapter(getContext(), cells, events);
        grid.setAdapter(adapter);

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        txtDate.setText(sdf.format(currentDate.getTime()));

        // set header color according to current season
        int month = currentDate.get(Calendar.MONTH);
        int season = monthSeason[month];
        int color = rainbow[season];

        header.setBackgroundColor(getResources().getColor(color));

    }

}