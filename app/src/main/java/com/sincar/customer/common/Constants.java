package com.sincar.customer.common;
/**
 * 2020.02.17 박정기
 * 앱 공통 정리
 * 1. 서버 URL
 * 2. 상수
 */
public class Constants {

    // 실 사용 Socket 연결 포트 1
    public static final int REAL_SOCEKT_PORT_01 = 7061;
    // 실 사용 Socket 연결 포트 2
    public static final int REAL_SOCKET_PORT_02 = 7062;

    public static final String PREFERENCE_STRING = "SINCAR_PERF";
    public static final String PREFERENCE_PERMISSION_CHERCK = "checkPermission";

    // 실서버  http://sincar.co.kr/api/IF_SINCAR_AGENT_001.asp?PHONE_NUMBER=01054490789&PASSWORD=1234
//    public static final String BASE_URL = "http://14.63.214.44:7060";

    public static final String BASE_URL = "http://sincar.co.kr/api";


    // 연결 서버 변경
    public static final String REAL_SERVER = BASE_URL;

    // 업데이트 체크
    public static final String UPDATE_CHECK_URL = REAL_SERVER + "/Begin/MTD/TVerCheck.do?";

    // 로그인   http://droad.iptime.org:8080/login_check_mobile.php?id=test2019&pw=1234
    public static final String LOGIN_REQUEST                = REAL_SERVER + "/IF_SINCAR_AGENT_001.asp?";   //로그인
    public static final String AUTH_NUMBER_REQUEST          = REAL_SERVER + "/IF_SINCAR_AGENT_002.asp?";   //인증번호 발송 요청
    public static final String CHANGE_PASSWORD              = REAL_SERVER + "/IF_SINCAR_AGENT_004.asp?";   //비밀번호 변경
    public static final String USE_HISTORY_REQUEST          = REAL_SERVER + "/IF_SINCAR_AGENT_005.asp?";   //이용내역 조회
    public static final String RESERVE_CANCEL_REQUEST       = REAL_SERVER + "/IF_SINCAR_AGENT_006.asp?";   //예약취소
    public static final String RECOMMEND_REGISTER_REQUEST   = REAL_SERVER + "/IF_SINCAR_AGENT_007.asp?";   //추천인 코드 등록
    public static final String NOTICE_LIST_REQUEST          = REAL_SERVER + "/IF_SINCAR_AGENT_008.asp?";   //공지사항 요청
    public static final String COUPONE_LIST_REQUEST         = REAL_SERVER + "/IF_SINCAR_AGENT_011.asp?";   //쿠폰 리스트 요청
    public static final String CAR_REGISTER_REQUEST         = REAL_SERVER + "/IF_SINCAR_AGENT_012.asp?";   //차량등록
    public static final String CAR_LIST_REQUEST             = REAL_SERVER + "/IF_SINCAR_AGENT_013.asp?";   //등록차량 리스트 요청
    public static final String INFO_CHANGE_REQUEST          = REAL_SERVER + "/IF_SINCAR_AGENT_014.asp?";   //알림변경 요청

    public static final String MEMBER_WITHRAW_REQUEST       = REAL_SERVER + "/IF_SINCAR_AGENT_020.asp?";   //회원탈퇴

    public static final String POINT_LIST_REQUEST       = REAL_SERVER + "/IF_SINCAR_AGENT_022.asp?";        //포인트 리스트 요청


    //리스트 요청
    public static final String FREIGHT_LIST_REQUEST = REAL_SERVER + "/freight_list.php?";

     // 에러 업로드
    public static final String ERROR_UPLOAD_URL = REAL_SERVER + "/Begin/MTD/MtdErrorCode.do?";

    ////

}