package com.sincar.customer.database;

/**
 * Created by junggipark on 2020. 04. 01..
 */
public class DBCommon {
    // Labels table name
    public static final String TABLE_PAY_APPROVE = "pay";

    // Labels Table Columns names comm_mst
    public static final String KEY_PAY_SEND_RESULT          = "send_result";        //전송결과
    public static final String KEY_PAY_APPROVE_NUMBER       = "approve_number";     //예약번호
    public static final String KEY_PAY_MEMBER_NO            = "member_no";          //회원번호
    public static final String KEY_PAY_RESERVE_ADDRESS      = "reserve_address";    //예약주소
    public static final String KEY_PAY_RESERVE_YEAR         = "reserve_year";       //년
    public static final String KEY_PAY_RESERVE_MONTH        = "reserve_month";      //월
    public static final String KEY_PAY_RESERVE_DAY          = "reserve_day";        //일
    public static final String KEY_PAY_AGENT_SEQ            = "agent_seq";          //대리점 SEQ
    public static final String KEY_PAY_AGENT_COMPANY        = "agent_company";      //대리점
    public static final String KEY_PAY_AGENT_TIME           = "agent_time";         //예약시간
    public static final String KEY_PAY_WASH_PLACE           = "wash_place";         //세차장소
    public static final String KEY_PAY_ADD_SERVICE          = "add_service";        //부가 서비스
    public static final String KEY_PAY_CAR_COMPANY          = "car_company";        //제조사
    public static final String KEY_PAY_CAR_MODEL            = "car_model";          //모델명
    public static final String KEY_PAY_CAR_NUMBER           = "car_number";         //차량번호
    public static final String KEY_PAY_POINT_USE            = "point_use";          //사용 포인트
    public static final String KEY_PAY_COUPONE_SEQ          = "coupone_seq";        //사용 쿠폰번호
    public static final String KEY_PAY_CHARGE_PAY           = "charge_pay";         //총 결재 요금
    public static final String KEY_PAY_SAVE_DATE            = "save_date";          //저장날짜

    // property help us to keep data comm_mst.
//    public String mst_Brand_Cd;
//    public String mst_Gr_Cd;
//    public String mst_Cd;
//    public String mst_Nm;
//    public String mst_Dt1;
//    public String mst_Dt2;
//    public String mst_Dt3;
//    public String mst_Dt4;
//    public String mst_Dt5;
//    public String mst_Dt6;
//    public String mst_Msg;
//    public String mst_Use_Yn;
//    public String mst_Upd_Dt;

}
