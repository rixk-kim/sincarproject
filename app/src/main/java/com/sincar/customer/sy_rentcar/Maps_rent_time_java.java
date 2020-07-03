package com.sincar.customer.sy_rentcar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sincar.customer.MapsActivity;
import com.sincar.customer.R;
import com.sincar.customer.databinding.ActivityMapsRentcarTimeBinding;
import com.super_rabbit.wheel_picker.WheelPicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Maps_rent_time_java extends Fragment {

    //findViewById를 사용하지 않고 xml에 있는 뷰를 바인딩 하기 위한 클래스
    ActivityMapsRentcarTimeBinding binding; //activity_maps_rent_car_time.xml의 이름을 가져옴

    //MapsActivity에 날짜, 시간(설정에 따라 예약 또는 반납) 데이터를 넘기기위한 클래스
    OnDateNTimeSetListener onDateNTimeSetListener = null;
    //현재 화면이 예약인지 반납인지 구분 짓기 위한 변수
    rCodeCheck rCodeCk = null;

    //OnDateNTimeSetListener를 사용하기 위한 초기화 메소드
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnDateNTimeSetListener) {
            onDateNTimeSetListener = (OnDateNTimeSetListener) context;
        }  else {
            throw new RuntimeException(context.toString() + " must implement OnDateNTimeSetListener");
        }
        if (context instanceof rCodeCheck) {
                rCodeCk = (rCodeCheck) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement rCodeCheck");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onDateNTimeSetListener = null;
        rCodeCk = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityMapsRentcarTimeBinding.inflate(inflater, container, false);
        View view = binding.getRoot(); //바인딩이 필요없어짐

        WPDayPickerAdapterForJava wpDayPickerAdapterForJava = new WPDayPickerAdapterForJava();
        binding.nPick1.setAdapter(wpDayPickerAdapterForJava);
        WPHourPickerAdapterForJava wpHourPickerAdapterForJava = new WPHourPickerAdapterForJava();
        binding.nPick2.setAdapter(wpHourPickerAdapterForJava);
        binding.nPick1.setUnselectedTextColor(R.color.agent_color_1Opp);
        binding.nPick2.setUnselectedTextColor(R.color.agent_color_1Opp);
        binding.nPick2.scrollTo(23); //초기화 중 시간 넘버피커의 마지막 아이템으로 이동

        final Date curDate = new Date(System.currentTimeMillis());
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d일 (E)");
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH");
        final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        final int curYear = Integer.parseInt(yearFormat.format(curDate));

        //예약, 반납 시간 설정을 위한 변수 선언
        int rCode = 0;
        String start_dateCheck = null;
        String start_timeCheck = null;
        int start_timeCheckInt = 0; //예약시간 인트화(0~23)
        String return_dateCheck = null;
        int return_timeCheckInt = 0;

        String dateCheck = null;
        int timeCheck = 0;

        //번들로부터 넘어온 데이터를 변수에 대입
        if(getArguments() != null) {
            start_dateCheck = getArguments().getString("start_date");
            start_timeCheck = getArguments().getString("start_time");
            start_timeCheckInt = getArguments().getInt("start_timeInt");
            return_dateCheck = getArguments().getString("return_date");
            return_timeCheckInt = getArguments().getInt("return_timeInt");
            rCode = getArguments().getInt("reOrRe"); //현재 화면이 예약인지 반납인지 구분 짓기 위한 변수
            if(rCode == 1) {
                dateCheck = start_dateCheck;
                timeCheck = start_timeCheckInt;
            } else if(rCode == 2) {
                dateCheck = return_dateCheck;
                timeCheck = return_timeCheckInt;
            }
           rCodeCk.rCodechk(rCode);

            //변수에 대입된 데이터를 Date변수로 변환
            try {
                Date dateCheck_date = dateFormat.parse(dateCheck);
                Date date_now_date = dateFormat.parse(dateFormat.format(curDate));
                //예약날짜와 현재 날짜의 일수 차이를 계산
                long dffday = (dateCheck_date.getTime() - date_now_date.getTime()) / (24 * 60 * 60 * 1000);

                binding.nPick1.scrollTo((int)dffday);
                binding.nPick2.scrollTo(timeCheck);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        //뷰의 setOnclickListener의 내부에선 final 변수이거나 내부에서 생성된 변수만 사용 가능
        final String finalStart_dateCheck = start_dateCheck;
        final String finalStart_timeCheck = start_timeCheck;
        final int finalRCode = rCode;
        final rCodeCheck finalrCodeck = rCodeCk;


        binding.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = binding.nPick1.getCurrentItem();
                String time = binding.nPick2.getCurrentItem();
                int currentYear = curYear;

                try {
                    Date date_check = dateFormat.parse(date);
                    Date time_check = timeFormat.parse(time);

                    //현재 날짜 인트화
                    Date date_now = dateFormat.parse(dateFormat.format(curDate));
                    int now_dayCheck = (int)(date_now.getTime()/ (24 * 60 * 60 * 1000));
                    //현재 시간 인트화
                    Date time_now = timeFormat.parse(timeFormat.format(curDate));
                    int now_dfftimeCheck = (int)((time_check.getTime() - time_now.getTime()) / (60 * 60 * 1000));

                    //예약 프래그먼트일 경우
                    if (finalRCode == 1) {
                        //현재 날짜, 시간과 체크된 날짜, 시간 차이 비교
                        int chk_dayCheck = (int)(date_check.getTime()/ (24 * 60 * 60 * 1000));
                        int now_dffdayCheck = chk_dayCheck - now_dayCheck;
                        if(now_dffdayCheck < 0 && 365 - now_dayCheck < 90) {
                            chk_dayCheck += 365;
                            now_dffdayCheck = chk_dayCheck - now_dayCheck;
                            currentYear++;
                        }

                        if(now_dffdayCheck == 0) {
                            //예약날짜가 현재시간과 같고 예약시간이 현재시간과 같거나 작을 경우 화면 전환 하지 않음
                            if(now_dfftimeCheck < 0) {
                                Toast.makeText(getContext(), "현재 시간 이후의 시간으로 설정하십시오.", Toast.LENGTH_LONG).show();
                            } else {
                                onDateNTimeSetListener.onDateNTimePickerSet(date, time, now_dfftimeCheck, currentYear);
                                ((MapsActivity)getActivity()).replaceFragment(1);
                            }
                        } else {
                            onDateNTimeSetListener.onDateNTimePickerSet(date, time, now_dfftimeCheck, currentYear);
                            ((MapsActivity)getActivity()).replaceFragment(1);
                        }
                        //반납 프래그먼트일 경우
                    } else if(finalRCode == 2) {

                        //예약시간 날짜, 시간과 체크된 날짜, 시간 차이 비교
                        Date date_reserve = dateFormat.parse(finalStart_dateCheck);
                        int resToret_DayCheck = (int)((date_check.getTime() - date_reserve.getTime()) / (24 * 60 * 60 * 1000));
                        int return_dayCheck = (int)(date_check.getTime()/ (24 * 60 * 60 * 1000));
                        int return_dffdayCheck = return_dayCheck - now_dayCheck;
                        if(return_dffdayCheck < 0 && 365 - now_dayCheck < 90) {
                            currentYear++;
                        }
                        Date time_reserve = timeFormat.parse(finalStart_timeCheck);
                        int resToret_TimCheck = (int)((time_check.getTime() - time_reserve.getTime()) / (60 * 60 * 1000));

                        String strMent;
                        if(resToret_DayCheck < 0) {
                            strMent = "예약 날짜보다 반납 날짜가 더 빠릅니다. \n예약 시간은 ";
                            Toast.makeText(getContext(), strMent + finalStart_dateCheck + finalStart_timeCheck + "입니다", Toast.LENGTH_LONG).show();

                            //반납날짜가 예약날짜와 같을 경우 시간을 비교하여 화면 전환 결정
                        } else if (resToret_DayCheck == 0) {
                            if(resToret_TimCheck <= 0 ) {
                                strMent = "예약 시간으로부터 최소한 1시간 경과된 시간으로 설정하십시오.\n예약 시간은 ";
                                Toast.makeText(getContext(), strMent + finalStart_dateCheck + finalStart_timeCheck + "입니다", Toast.LENGTH_LONG).show();
                            } else {
                                onDateNTimeSetListener.onDateNTimePickerSet(date, time, now_dfftimeCheck, currentYear);
                                ((MapsActivity)getActivity()).replaceFragment(1);
                            }
                        } else {
                            onDateNTimeSetListener.onDateNTimePickerSet(date, time, now_dfftimeCheck, currentYear);
                            ((MapsActivity)getActivity()).replaceFragment(1);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                finalrCodeck.rCodechk(0); //메인 프래그먼트로 넘어가므로 rCode를 다시 0으로 전환
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
