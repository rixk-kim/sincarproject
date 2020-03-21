package com.sincar.customer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.multidex.MultiDexApplication;

import com.sincar.customer.entity.LoginDataEntity;
import com.sincar.customer.item.AddressDataItem;
import com.sincar.customer.item.AddressItem;
import com.sincar.customer.item.AddressResult;
import com.sincar.customer.item.AgentDataItem;
import com.sincar.customer.item.AgentItem;
import com.sincar.customer.item.AgentResult;
import com.sincar.customer.item.CarDataItem;
import com.sincar.customer.item.CarItem;
import com.sincar.customer.item.CarListDataItem;
import com.sincar.customer.item.CarRegisterItem;
import com.sincar.customer.item.CarRegisterResult;
import com.sincar.customer.item.CarResult;
import com.sincar.customer.item.CardDataItem;
import com.sincar.customer.item.CardItem;
import com.sincar.customer.item.CardResult;
import com.sincar.customer.item.CompanyListDataItem;
import com.sincar.customer.item.CouponeDataItem;
import com.sincar.customer.item.CouponeItem;
import com.sincar.customer.item.CouponeResult;
import com.sincar.customer.item.LoginAdvertiseItem;
import com.sincar.customer.item.LoginDataItem;
import com.sincar.customer.item.LoginItem;
import com.sincar.customer.item.LoginResult;
import com.sincar.customer.item.NoticeDataItem;
import com.sincar.customer.item.NoticeItem;
import com.sincar.customer.item.NoticeResult;
import com.sincar.customer.item.OptionDataItem;
import com.sincar.customer.item.OptionItem;
import com.sincar.customer.item.OptionResult;
import com.sincar.customer.item.ReserveItem;
import com.sincar.customer.item.ReserveResult;
import com.sincar.customer.item.TimeItem;
import com.sincar.customer.item.UseDataItem;
import com.sincar.customer.item.UseItem;
import com.sincar.customer.item.UseResult;
import com.sincar.customer.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by spirit on 2019-10-26 <p/>
 */
public class HWApplication extends MultiDexApplication {

    private Context context;
    private static HWApplication mInstance = null;
//    private XApplication mXApplication;
    private MainActivity mMainActivity;

//    public ErrorFactory errorFactory;

//    public static boolean isStartApp = false;
//    public static boolean isLogout = false;
//    // isLogout랑 같이 쓰려 했지만 코드 영향도 위험땜에 그냥 따로 만듬
//    public static boolean loggedin = false;

    public static LoginDataEntity voLoginData;
    public static LoginResult loginResult;
    public static LoginItem voLoginItem;                        //로그인 정보
    //public static LoginDataItem voLoginDataItem;              //친구 정보
    public static LoginAdvertiseItem voLoginAdvertiseItem;      //광고 정보

    public static ArrayList<LoginDataItem> voLoginDataItem;         //포인트 리스트
    public static ArrayList<LoginAdvertiseItem> voAdvertiseItem;    //광고 리스트
    public static ArrayList<CompanyListDataItem> voCompanyListDataItem;     //제조사 리스트
    public static ArrayList<CarListDataItem> voCarListDataItem;     //차량 리스트
    public static ArrayList<NoticeDataItem> voNoticeDataItem;       //공지 리스트
    public static ArrayList<CouponeDataItem> voCouponeDataItem;     //쿠폰 리스트
    public static ArrayList<CarDataItem> voCarDataItem;             //등록차량 리스트
    public static ArrayList<CardDataItem> voCardDataItem;           //등록카드 리스트
    public static ArrayList<AddressDataItem> voAddressDataItem;     //주소검색 리스트
    public static AgentDataItem voAgentDataItem;                    //대리점 리스트
    public static ArrayList<UseDataItem> voUseDataItem;             //이용내역
    public static ArrayList<OptionDataItem> voOptionDataItem;       //부가서비스 리스트

    //공지사항
    public static NoticeResult noticeResult;
    public static NoticeItem voNoticeItem;

    //등록쿠폰
    public static CouponeResult couponeResult;
    public static CouponeItem voCouponeItem;

    //등록차량
    public static CarResult carResult;
    public static CarItem voCarItem;

    //등록카드
    public static CardResult cardResult;
    public static CardItem voCardItem;

    //주소 검색
    public static AddressResult addressResult;
    public static AddressItem voAddressItem;

    //대리점 검색
    public static AgentResult agentResult;
    public static AgentItem voAgentItem;
    public static TimeItem voTimeItem;

    //이용내역
    public static UseResult useResult;
    public static UseItem voUseItem;

    //부가서비스 리스트
    public static OptionResult optionResult;
    public static OptionItem voOptionItem;

    //차량등록
    public static CarRegisterResult carRegisterResult;
    public static CarRegisterItem voCarRegisterItem;

    //예약결과
    public static ReserveResult reserveResult;
    public static ReserveItem voReserveItem;

    AppStatus mAppStatus;
    @Override
    public void onCreate() {
        super.onCreate();

//        DAPSecureStorage.init(this);
        context = getApplicationContext();
        setInstance(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            HWNotificationManager.getInstance().createChannel(this);
        }

//        mXApplication = XApplication.getInstance();
//        mXApplication.setContext(MainApp.this);
//
//        errorFactory = new ErrorFactory();

        registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());

        // 프리퍼런스 셋팅
        PreferenceManager.getInstance().init(getApplicationContext());

        voLoginItem = new LoginItem();
//        voLoginDataItem = new LoginDataItem();
        voLoginAdvertiseItem = new LoginAdvertiseItem();

        voNoticeItem    = new NoticeItem();
        voCouponeItem   = new CouponeItem();
        voCarItem       = new CarItem();
        voCardItem      = new CardItem();
        voAddressItem   = new AddressItem();
        voAgentItem     = new AgentItem();
        voTimeItem      = new TimeItem();
        voOptionItem    = new OptionItem();
        voCarRegisterItem       = new CarRegisterItem();
        voReserveItem   = new ReserveItem();

    }

    public static HWApplication getInstance() {
        if(null == mInstance){
            synchronized ( HWApplication.class ) {
                if ( null == mInstance ) {
                    mInstance = new HWApplication();
                }
            }
        }
        checkInstance();
        return mInstance;
    }

    private final void setInstance(HWApplication application){
        mInstance = application;
    }

    public enum AppStatus {
        BACKGROUND, // app is background
        RETURNED_TO_FOREGROUND, // app returned to foreground(or first launch)
        FOREGROUND; // app is foreground
    }

    private static void checkInstance() {
        if (null == mInstance) {
            throw new IllegalStateException("Application not created yet!");
        }
    }

    public  Context getContext() {
        return context;
    }

    public void setMain(MainActivity activity){
        mMainActivity = activity;
    }

    public MainActivity getMainActivity(){
        if(mMainActivity!=null){
            return mMainActivity;
        }

        return null;
    }

    // Get app is foreground
    public AppStatus getAppStatus() {
        return mAppStatus;
    }

    // check if app is return foreground
    public boolean isReturnedForground() {
        return mAppStatus.ordinal() == AppStatus.RETURNED_TO_FOREGROUND.ordinal();
    }


    public class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        // running activity count
        private int running = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (++running == 1) {
                mAppStatus = AppStatus.RETURNED_TO_FOREGROUND;
            } else if (running > 1) {
                mAppStatus = AppStatus.FOREGROUND;
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (--running == 0) {
                mAppStatus = AppStatus.BACKGROUND;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }

    }
}
