package com.sincar.customer.preference;


import android.content.Context;
import android.content.SharedPreferences;

import com.sincar.customer.common.Constants;


public class PreferenceData implements PreferenceModule {

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public static PreferenceData newInstance(Context context) {
        return new PreferenceData(context);
    }

    private PreferenceData(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(Constants.PREFERENCE_STRING, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    @Override
    public boolean saveKey() {
        mEditor.apply();
        return true;
    }

    @Override
    public void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        saveKey();
    }

    @Override
    public void putInteger(String key, int value) {
        mEditor.putInt(key, value);
        saveKey();
    }

    @Override
    public void putString(String key, String value) {
        mEditor.putString(key, value);
        saveKey();
    }

    @Override
    public String getString(String key, String value) {
        return mSharedPreferences.getString(key, value);
    }

    @Override
    public boolean getBoolean(String key, boolean value) {
        return mSharedPreferences.getBoolean(key, value);
    }

    @Override
    public int getInteger(String key, int value) {
        return mSharedPreferences.getInt(key, value);
    }
}
