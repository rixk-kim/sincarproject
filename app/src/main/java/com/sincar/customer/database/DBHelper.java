package com.sincar.customer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "/mnt/sdcard/" + "sincar.db";
//    private static final String DATABASE_NAME = "sincar.db";

    private SQLiteDatabase mDB;

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        System.out.println("[spirit] DBHelper  ::::: ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        System.out.println("[spirit]create table  ::::: ");

        //comm_mst
        String CREATE_TABLE_COMM_MST = "CREATE TABLE " + DBCommon.TABLE_PAY_APPROVE  + "("
                + DBCommon.KEY_PAY_SEND_RESULT + " CHAR(1) NOT NULL, "
                + DBCommon.KEY_PAY_APPROVE_NUMBER + " NVARCHAR(20) NOT NULL, "
                + DBCommon.KEY_PAY_MEMBER_NO + " NVARCHAR(10) NOT NULL, "
                + DBCommon.KEY_PAY_RESERVE_ADDRESS + " NVARCHAR(100) NOT NULL, "
                + DBCommon.KEY_PAY_RESERVE_YEAR + " NVARCHAR(4) NOT NULL, "
                + DBCommon.KEY_PAY_RESERVE_MONTH + " NVARCHAR(20) NOT NULL, "
                + DBCommon.KEY_PAY_RESERVE_DAY + " NVARCHAR(20) NOT NULL, "
                + DBCommon.KEY_PAY_AGENT_SEQ + " NVARCHAR(10) NOT NULL, "
                + DBCommon.KEY_PAY_AGENT_COMPANY + " NVARCHAR(50) NOT NULL, "
                + DBCommon.KEY_PAY_AGENT_TIME + " NVARCHAR(10) NOT NULL, "
                + DBCommon.KEY_PAY_WASH_PLACE + " CHAR(1) NOT NULL, "
                + DBCommon.KEY_PAY_ADD_SERVICE + " NVARCHAR(100), "
                + DBCommon.KEY_PAY_CAR_COMPANY + " NVARCHAR(20) NOT NULL, "
                + DBCommon.KEY_PAY_CAR_MODEL + " NVARCHAR(1) NOT NULL, "
                + DBCommon.KEY_PAY_CAR_NUMBER + " NVARCHAR(20) NOT NULL, "
                + DBCommon.KEY_PAY_POINT_USE + " NVARCHAR(10), "
                + DBCommon.KEY_PAY_COUPONE_SEQ + " NVARCHAR(10), "
                + DBCommon.KEY_PAY_CHARGE_PAY + " NVARCHAR(20) NOT NULL, "
                + DBCommon.KEY_PAY_SAVE_DATE + " DATETIME NOT NULL, "
                + "PRIMARY KEY("+ DBCommon.KEY_PAY_APPROVE_NUMBER +"))";

        db.execSQL(CREATE_TABLE_COMM_MST);

        System.out.println("[spirit] db create");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
/*
        db.execSQL("DROP TABLE IF EXISTS " + DBCommon.TABLE_COMM_MST);

        System.out.println("[spirit] onUpgrade oldVersion  ::::: " + oldVersion);
        System.out.println("[spirit] onUpgrade newVersion  ::::: " + newVersion);

        // Create tables again
        onCreate(db);
*/
        String sql_droptable = "DROP TABLE IF EXISTS " + "kingreturn_dialog;";
        db.execSQL(sql_droptable);
        sql_droptable = "DROP TABLE IF EXISTS " + "kingreturn_expression;";
        db.execSQL(sql_droptable);
    }

}
