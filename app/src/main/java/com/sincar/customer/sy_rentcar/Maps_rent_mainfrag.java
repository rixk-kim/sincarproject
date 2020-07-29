package com.sincar.customer.sy_rentcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sincar.customer.MapsActivity;
import com.sincar.customer.R;
import com.sincar.customer.ReservationAddressActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Maps_rent_mainfrag extends Fragment {

    androidx.constraintlayout.widget.ConstraintLayout tvAddress, reserveTime, returnTime; //맵표시 주소, 예약시간, 반납시간의 버튼화
    TextView tvReserveDate, tvReserveTime, tvReturnDate, tvReturnTime, currentAddress; //예약날짜,시간, 반납날짜,시간, 맵표시 주소텍스트
    Button btnCheck;    //최종 확인 버튼
    Date reserverDateNTime, returnDateNTime; //예약날짜,반납날짜의 Date
    String start_date, start_time, return_date, return_time, reserve_address, start_year, return_year; // 예약날짜,시간, 반납날짜,시간,연도, 맵표시 주소 String
    double latitude, longitude; // 맵표시 주소 위경도

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_maps_rentcar_mainfrag, container, false);

        tvAddress = (androidx.constraintlayout.widget.ConstraintLayout) view.findViewById(R.id.btnReserveAddress);
        reserveTime = (androidx.constraintlayout.widget.ConstraintLayout) view.findViewById(R.id.btnReserveDate);
        returnTime = (androidx.constraintlayout.widget.ConstraintLayout) view.findViewById(R.id.btnReturnDate);
        btnCheck = (Button) view.findViewById(R.id.btnCheck);
        currentAddress = (TextView)view.findViewById(R.id.current_address);

        tvReserveDate = (TextView) view.findViewById(R.id.reserve_date);
        tvReserveTime = (TextView) view.findViewById(R.id.reserve_time);
        tvReturnDate = (TextView) view.findViewById(R.id.return_date);
        tvReturnTime = (TextView) view.findViewById(R.id.return_time);

        //MapsActivity의 Bundle로부터 데이터를 받아옴
        if (getArguments() != null) {
            start_date = getArguments().getString("start_date");
            start_time = getArguments().getString("start_time");
            return_date = getArguments().getString("return_date");
            return_time = getArguments().getString("return_time");
            reserve_address = getArguments().getString("reserve_address");
            latitude = getArguments().getDouble("reserve_lat");
            longitude = getArguments().getDouble("reserve_lon");
            start_year = String.valueOf(getArguments().getInt("start_yearInt"));
            return_year = String.valueOf(getArguments().getInt("return_yearInt"));
            //받아온 데이터를 텍스트뷰에 표시
            tvReserveDate.setText(start_date);
            tvReserveTime.setText(start_time);
            tvReturnDate.setText(return_date);
            tvReturnTime.setText(return_time);
            currentAddress.setText(reserve_address);

            //예약 날짜,시간 과 반납 날짜,시간의 String을 파싱
            SimpleDateFormat dateNtimeFormat = new SimpleDateFormat("yyyyMMM d일 (E)HH:00");

            try {
                reserverDateNTime = dateNtimeFormat.parse(start_year + start_date + start_time);
                returnDateNTime = dateNtimeFormat.parse(return_year + return_date + return_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //주소 검색 액티비티로 화면 전환
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReservationAddressActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION); // intent가 호출될때 onUserLeaveHint()가 실행되는것을 차단
                getActivity().startActivityForResult(intent, ((MapsActivity)getActivity()).MAP_REQ_CODE);
            }
        });

        //예약 날짜,시간 설정 프래그먼트로 화면 전환
        reserveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MapsActivity) getActivity()).start_reserveDate();
            }
        });
        //반납 날짜,시간 설정 프래그먼트로 화면 전환
        returnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MapsActivity) getActivity()).start_returnDate();
            }
        });
        //최종 확인 버튼
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            //파싱한 예약 시간과 반납 시간을 비교
            //예약 시간과 반납 시간이 같거나 반납시간잉 예약시간보다 빠르면 오류 토스트
            public void onClick(View v) {
                //현재시간과 예약시간과 반납시간을 비교하여 화면 전환 결정하는 알고리즘
                int compare = reserverDateNTime.compareTo(returnDateNTime);
                if(compare == 0) {
                    Toast.makeText(getContext(), "예약 시간과 반납 시간이 같습니다\n반납시간이 예약 시간보다 최소한 1시간 경과 되어있어야 합니다", Toast.LENGTH_LONG).show();
                } else if (compare > 0 ) {
                    Toast.makeText(getContext(), "반납 시간이 예약 시간보다 빠릅니다.\n반납시간이 예약 시간보다 최소한 1시간 경과 되어있어야 합니다", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getContext(), Rental_list.class);
                    //MapsActivity에서 넘어온 예약시간,반납시간,주소등의 데이터를 번들로 Rental_list에 넘김
                    intent.putExtra("start_date", start_date);
                    intent.putExtra("start_time", start_time);
                    intent.putExtra("return_date", return_date);
                    intent.putExtra("return_time", return_time);
                    intent.putExtra("reserve_address", reserve_address);
                    intent.putExtra("reserve_lat", latitude);
                    intent.putExtra("reserve_lon", longitude);
                    intent.putExtra("start_year", start_year);
                    intent.putExtra("return_year", return_year);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION); // intent가 호출될때 onUserLeaveHint()가 실행되는것을 차단
                    getActivity().startActivity(intent);
//                Toast.makeText(getContext(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    //MapsActivity에서 화면중앙 위치의 주소를 표현하기 위한 메소드
    public void AddressChange() {
        String curAddress = getArguments().getString("reserve_address");
        if(curAddress != null && currentAddress != null)
            currentAddress.setText(curAddress);
    }
}
