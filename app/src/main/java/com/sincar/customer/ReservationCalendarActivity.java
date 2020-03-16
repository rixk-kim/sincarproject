package com.sincar.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ReservationCalendarActivity extends AppCompatActivity implements View.OnClickListener {
    private Context calContext;

    private String reserve_address; //예약주소
    private String reserve_year;    //예약년도
    private String reserve_month;   //예약월
    private String reserve_day;     //예약일


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_calendar);
        calContext = this;

        Intent intent = getIntent(); /*데이터 수신*/
        reserve_address    = intent.getExtras().getString("reserve_address");    /*String형*/

        // 화면 초기화
        init();

        //현재 날짜 가져오기
//        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
//        Date time = new Date();
//
//        String time1 = format1.format(time);    //2020-3-14

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();;
        reserve_year    = String.valueOf(today.year);
        reserve_month   = String.valueOf(today.month + 1);
        reserve_day     = String.valueOf(today.monthDay);
    }

    /**
     * 화면 초기화
     */
    private void init() {

        findViewById(R.id.btnPrev).setOnClickListener(this);
        findViewById(R.id.btnNext).setOnClickListener(this);

        // Calendar layout 및 이벤트 처리
//        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
//        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            public void onSelectedDayChange(CalendarView view, int year,
//                                            int month, int dayOfMonth) {
//                reserve_year    = String.valueOf(year);
//                reserve_month   = String.valueOf(month);
//                reserve_day     = String.valueOf(dayOfMonth);
//
//                Toast.makeText(calContext, "" + year + "/" +
//                        (month + 1) + "/" + dayOfMonth, Toast.LENGTH_LONG).show();
//            }
//        });


        // TODO - Calendar layout 및 이벤트 처리
        final int GET_CALENDAR_AFTER_MONTH_VALUE = 3;

        CalendarView calendar = findViewById(R.id.calendarView);
        Calendar todayCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        calendar.setMinDate(todayCalendar.getTimeInMillis());

        Calendar maxCalendar = todayCalendar;
        maxCalendar.set(maxCalendar.get(Calendar.YEAR),
                maxCalendar.get(Calendar.MONTH) + GET_CALENDAR_AFTER_MONTH_VALUE,
                maxCalendar.get(Calendar.DAY_OF_MONTH));
        calendar.setMaxDate(maxCalendar.getTimeInMillis());

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                reserve_year    = String.valueOf(year);
                reserve_month   = String.valueOf(month);
                reserve_day     = String.valueOf(dayOfMonth);

                Toast.makeText(calContext, "" + year + "/" +
                        (month + 1) + "/" + dayOfMonth, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnPrev:
                finish();
                break;
            case R.id.btnNext:
                //startActivity(new Intent(this, ReservationTimeActivity.class));
                intent = new Intent(this, ReservationTimeActivity.class);
                intent.putExtra("reserve_address", reserve_address);
                intent.putExtra("reserve_year", reserve_year);
                intent.putExtra("reserve_month", reserve_month);
                intent.putExtra("reserve_day", reserve_day);
                startActivity(intent);
                break;
        }
    }
}
