package com.szpt.zzq.smarttrafficdemo.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ZhiQiang on 2017/9/13.
 */

public class SharedPreferenceUtil {
    private static final String TAG = "SharedPreferenceUtil";
    private static final String PREF_NAME = "smart_traffic_demo_pref";
    private static final String IS_FIRST_RUN = "isFirstRun";

    private SharedPreferences mSharedPreferences;
    private Context mContext;
    private SharedPreferences.Editor mEditor;

    public SharedPreferenceUtil(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void setIsFirstRun(boolean b) {
        mEditor.putBoolean(IS_FIRST_RUN, b);
        mEditor.commit();
    }

    public boolean IsFirstRun() {
        return mSharedPreferences.getBoolean(IS_FIRST_RUN, true);
    }
}
