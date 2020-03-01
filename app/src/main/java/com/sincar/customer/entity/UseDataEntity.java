package com.sincar.customer.entity;

/**
 * 이용내역
 */
public class UseDataEntity {
    private String totalPage;               // 전체페이지
    private String currentPage;             // 현재페이지
    private String currentNum;              // 현재갯수

    private String[] seq;                   // seq
    private String[] reserve_status;        // 진행상태(0-대기,1-완료,2-취소)
    private String[] reserve_time;          // 예약시간
    private String[] wash_address;          // 세차장소
    private String[] agent;                 // 대리점

    private String[] use_pay;               //이용요금(결제요금)
    private String[] reserve_name;          //고객명
    private String[] reserve_phone;         //고객전화번호
    private String[] common_pay;            //기본요금
    private String[] coupone;               //쿠폰이용요금

    private String[] charge_info;           //결제정보(은행,계좌이체등…)
    private String[] car_model;             //차량정보(차종)
    private String[] car_number;            //차량정보(번호)


    public String getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getCurrentNum() {
        return currentNum;
    }
    public void setCurrentNum(String currentNum) {
        this.currentNum = currentNum;
    }

    public String[] getSeq() {
        return seq;
    }
    public void setSeq(String[] seq) {
        this.seq = seq;
    }

    public String[] getReserve_status() {
        return reserve_status;
    }
    public void setReserve_status(String[] reserve_status) {
        this.reserve_status = reserve_status;
    }

    public String[] getReserve_time() {
        return reserve_time;
    }
    public void setReserve_time(String[] reserve_time) {
        this.reserve_time = reserve_time;
    }

    public String[] getWash_address() {
        return wash_address;
    }
    public void setWash_address(String[] wash_address) {
        this.wash_address = wash_address;
    }

    public String[] getAgent() {
        return agent;
    }
    public void setAgent(String[] agent) {
        this.agent = agent;
    }

    public String[] getUse_pay() {
        return use_pay;
    }
    public void setUse_pay(String[] use_pay) {
        this.use_pay = use_pay;
    }

    public String[] getReserve_name() {
        return reserve_name;
    }
    public void setReserve_name(String[] reserve_name) {
        this.reserve_name = reserve_name;
    }

    public String[] getReserve_phone() {
        return reserve_phone;
    }
    public void setReserve_phone(String[] reserve_phone) {
        this.reserve_phone = reserve_phone;
    }

    public String[] getCommon_pay() {
        return common_pay;
    }
    public void setCommon_pay(String[] common_pay) {
        this.common_pay = common_pay;
    }

    public String[] getCoupone() {
        return coupone;
    }
    public void setCoupone(String[] coupone) {
        this.coupone = coupone;
    }

    public String[] getCharge_info() {
        return charge_info;
    }
    public void setCharge_info(String[] charge_info) {
        this.charge_info = charge_info;
    }

    public String[] getCar_model() {
        return car_model;
    }
    public void setCar_model(String[] car_model) {
        this.car_model = car_model;
    }

    public String[] getCar_number() {
        return car_number;
    }
    public void setCar_number(String[] car_number) {
        this.car_number = car_number;
    }
}

