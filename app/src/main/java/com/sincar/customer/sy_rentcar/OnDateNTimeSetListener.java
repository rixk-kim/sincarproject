package com.sincar.customer.sy_rentcar;

//렌터카 메인프래그,예약프래그,반납프래그먼트로부터 MapsActivity로 시간 데이터 전달을 위한 인터페이스
public interface OnDateNTimeSetListener {

    void onDateNTimePickerSet(String date, String time, int timeCheck, int yearCheck);
}
