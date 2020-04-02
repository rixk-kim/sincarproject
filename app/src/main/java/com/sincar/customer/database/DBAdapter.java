package com.sincar.customer.database;

/**
 * Created by junggipark on 2020. 04. 01..
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import com.sincar.customer.util.Util;

import java.text.DecimalFormat;
import java.util.Calendar;

public class DBAdapter {
    public DBHelper dbHelper;

    //데이터 베이스를 이용하는 컨텍스트
    private Context context;
    //데이터 연동객체
    private SQLiteDatabase db;
    
    private static DBAdapter mInstance; //20200401 by spirit add
    private int mOpenCounter;

    public DBAdapter(Context context){
        dbHelper = new DBHelper(context);
        this.open();

    }

    //20200401 by spirit add
    public static DBAdapter getInstance(Context context){
        if(mInstance == null){
            mInstance = new DBAdapter(context);
        }
        return mInstance;
    }

    public SQLiteDatabase getDb()
    {
        return db;
    }

    public synchronized void open(){
        mOpenCounter++;
        if(mOpenCounter == 1) {
            try {
                if (db == null) {
                    db = dbHelper.getWritableDatabase();//(new DBHelper(context).getWritableDatabase());
                }
            } catch (SQLiteException e) {
                db = dbHelper.getReadableDatabase();//(new DBHelper(context).getReadableDatabase());
            }
        }
//        return db;
    }

    public void close(){
        mOpenCounter--;
        if(mOpenCounter == 0)
        {
            db.close();
        }
    }

    public void openTransjection(){
        if(db == null){
            db = dbHelper.getWritableDatabase();
        }
        db.beginTransaction();
    }

    public void closeTransjection(){
        if(db != null) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
    }

    /**
     * @Method Name  : insert
     * @작성일   : 2015. 12. 03
     * @작성자   : spirit
     * @변경이력  :
     * @Method 설명 : Table Insert
     * @param TABLE_NAME
     * @param col_value
     * @param
     */
    public long insert(String TABLE_NAME, ContentValues col_value) {
        System.out.println("[spirit] insert  ========> " + col_value.toString());
        long idx = db.insert(TABLE_NAME, null, col_value);
        // Inserting Row
        System.out.println("[spirit] insert  ========> " + idx);

        return idx;
    }

    /**
     * @Method Name  : delete
     * @작성일   : 2015. 12. 03
     * @작성자   : spirit
     * @변경이력  :
     * @Method 설명 : Table delete
     * @param
     * @param
     * @param
     */
    public void delete(String approve_number) {
//        String c_time = "";
//
//        DecimalFormat df = new DecimalFormat("00");
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.add(Calendar.MONTH, -3);
//
//        String year = Integer.toString(calendar.get(Calendar.YEAR)); //년도를 구한다
//        String month = df.format(calendar.get(Calendar.MONTH) + 1); //달을 구한다
//        String day = df.format(calendar.get(Calendar.DATE)); //날짜를 구한다
//
//        c_time = year + month + day;

        try {

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            db.execSQL("DELETE FROM pay WHERE " + DBCommon.KEY_PAY_APPROVE_NUMBER + " = '" + approve_number + "'");
        }catch(Exception e){}
    }

    /**
     * @Method Name  : update
     * @작성일   : 2015. 12. 03
     * @작성자   : spirit
     * @변경이력  :
     * @Method 설명 : Table update
     * @param TABLE_NAME
     * @param col_data_value
     * @param wherecause
     * @param where_value
     */
    public long update(String TABLE_NAME, ContentValues col_data_value, String wherecause, String[] where_value) {
        long idx = db.update(TABLE_NAME, col_data_value, wherecause, where_value);

//        System.out.println("[test] update result :"+l);

        return idx;
    }

    /**
     * approve_number   예약번호
     * member_no        회원번호
     * reserve_address  예약주소
     * reserve_year     년
     * reserve_month    월
     * reserve_day      일
     * agent_seq        대리점 SEQ
     * agent_company    대리점
     * agent_time       예약시간
     * wash_area        세차장소
     * car_wash_option  부가 서비스
     * car_company      제조사
     * car_name         모델명
     * car_number       차량번호
     * use_my_point     사용 포인트
     * use_coupone_seq  사용 쿠폰번호
     * total_amt        총 결재 요금
     */
    public void setDataServerSend(String approve_number, String member_no, String reserve_address , String reserve_year, String reserve_month, String reserve_day, String agent_seq
            , String agent_company, String agent_time, String wash_area, String car_wash_option, String car_company, String car_name, String car_number, String use_my_point
            , String use_coupone_seq, String total_amt)
    {

        String selectQuery =  "";

        selectQuery = "INSERT INTO "+ DBCommon.TABLE_PAY_APPROVE
                + " VALUES ( 'N','" + approve_number + "','" + member_no + "','" + reserve_address + "','" + reserve_year + "','"+ reserve_month + "','" +
                reserve_day + "','" + agent_seq + "','" + agent_company + "','" + agent_time + "','" +wash_area + "','" + car_wash_option + "','" + car_company + "','" + car_name + "','" +
                car_number + "','" + use_my_point + "','" + use_coupone_seq + "','" + total_amt + "','" + Util.getYearMonthDay1() + "')";

        System.out.println("[spirit] selectQuery => " + selectQuery);

        db.execSQL(selectQuery);
    }

    /**
     * @Method Name  : getYetSendInfo
     * @작성일   : 2020. 04. 01
     * @작성자   : spirit
     * @변경이력  :
     * @Method 설명 : 미전송 예약 데이터
     * @param
     */
    public String[][]  getYetSendInfo() {
        int count = 0;
        //Open connection to read only
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "";

        selectQuery = "SELECT * FROM " + DBCommon.TABLE_PAY_APPROVE
                + " WHERE " +  DBCommon.KEY_PAY_SEND_RESULT + "='N' ORDER BY " + DBCommon.KEY_PAY_SAVE_DATE + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(selectQuery, null);

        int total_data = cursor.getCount();

        String[][] reserve_data = new String[total_data][17];

        if (cursor.moveToFirst()) {
            do {
                reserve_data[count][0] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_APPROVE_NUMBER));      //예약번호
                reserve_data[count][1] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_MEMBER_NO));           //회원번호
                reserve_data[count][2] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_RESERVE_ADDRESS));     //예약주소
                reserve_data[count][3] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_RESERVE_YEAR));        //년
                reserve_data[count][4] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_RESERVE_MONTH));       //월
                reserve_data[count][5] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_RESERVE_DAY));         //일
                reserve_data[count][6] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_AGENT_SEQ));           //대리점 SEQ
                reserve_data[count][7] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_AGENT_COMPANY));       //대리점
                reserve_data[count][8] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_AGENT_TIME));          //예약시간
                reserve_data[count][9] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_WASH_PLACE));          //세차장소
                reserve_data[count][10] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_ADD_SERVICE));        //부가 서비스
                reserve_data[count][11] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_CAR_COMPANY));        //제조사
                reserve_data[count][12] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_CAR_MODEL));          //모델명
                reserve_data[count][13] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_CAR_NUMBER));         //차량번호
                reserve_data[count][14] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_POINT_USE));          //사용 포인트
                reserve_data[count][15] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_COUPONE_SEQ));        //사용 쿠폰번호
                reserve_data[count][16] = cursor.getString(cursor.getColumnIndex(DBCommon.KEY_PAY_CHARGE_PAY));         //총 결재 요금

                count+=1;
            } while (cursor.moveToNext());
        }

        cursor.close();

        return reserve_data;

    }

    public boolean reserve_data_update(String approve_number)
    {
        String selectQuery =  "";
        selectQuery = "UPDATE "+ DBCommon.TABLE_PAY_APPROVE
                + " SET  " + DBCommon.KEY_PAY_SEND_RESULT + "='Y' WHERE " + DBCommon.KEY_PAY_APPROVE_NUMBER + "="+ approve_number;

        System.out.println("[spirit] updateQuery => " + selectQuery);

        db.execSQL(selectQuery);
        return true;
    }
}

