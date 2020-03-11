package com.sincar.customer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.multidex.MultiDexApplication;

import com.sincar.customer.entity.LoginDataEntity;
import com.sincar.customer.preference.PreferenceManager;

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

        voLoginData = new LoginDataEntity();

        // 기본 폰트 : 나눔 고딕
        // 기본 볼드 폰트 : 나눔 고딕
//        Typekit.getInstance().addNormal(Typekit.createFromAsset(this, "NanumBarunGothic.otf"))
//                .addBold(Typekit.createFromAsset(this, "NanumBarunGothicBold.otf"));
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
