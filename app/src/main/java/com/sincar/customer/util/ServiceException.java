package com.sincar.customer.util;

/**
 * Created by Administrator on 2016-08-21.
 */
@SuppressWarnings("serial")
public class ServiceException extends Exception {
    private int iErrCode=0;

    private static final int nProcessError=-1;//처리하지못함
    private static final int nNonError=0;//
    private static final int nInvalid=1;//처리과정중 몇건의 에러
    private static final int nIsValidFaie=2;//파라미터 검증실패
    private static final int nTransFail=3;//통신 실패

    private static final int nInsertError=100;
    private static final int nUpdateError=101;
    private static final int nSelectError=102;
    private static final int nDeleteError=103;
    private static final int nCreateError=104;

    public ServiceException(String message,Exception cause){
        super(message,cause);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message,int iErrCode) {
        super(message);
        this.iErrCode = iErrCode ;
    }

    public int GetErrCode(){ return iErrCode;}

    public static int GetProcessError(){ return nProcessError;}
    public static int GetNonError(){ return nNonError;}
    public static int GetInvalid(){ return nInvalid;}
    public static int GetIsValidFaie(){ return nIsValidFaie;}
    public static int GetTransFail(){ return nTransFail;}

    public static int GetInsertErrCode(){ return nInsertError;}
    public static int GetUpdateErrCode(){ return nUpdateError;}
    public static int GetSelectErrCode(){ return nSelectError;}
    public static int GetDeleteErrCode(){ return nDeleteError;}
    public static int GetCreateErrCode(){ return nCreateError;}

}
