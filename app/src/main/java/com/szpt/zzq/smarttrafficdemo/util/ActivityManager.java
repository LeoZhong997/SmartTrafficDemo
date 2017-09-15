package com.szpt.zzq.smarttrafficdemo.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhiQiang on 2017/9/15.
 */

public class ActivityManager extends AppCompatActivity {
    private static ArrayList<Activity> mActivities = new ArrayList<>();
    private static Toast mToast;

    public static final boolean mIsDebug = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        addActivity(this);
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onDestroy() {
        removeActivity(this);
        super.onDestroy();
    }

    private void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    private void removeActivity(Activity activity) {
        mActivities.remove(activity);
    }

    public static void showMessage(Context context, String message){
        if (mToast == null) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(message);
        mToast.show();
    }

    public static void removeAll() {
        for (Activity activity : mActivities) {
            activity.finish();
        }
    }
}
