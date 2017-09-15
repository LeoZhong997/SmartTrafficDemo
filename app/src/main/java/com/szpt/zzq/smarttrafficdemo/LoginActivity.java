package com.szpt.zzq.smarttrafficdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.szpt.zzq.smarttrafficdemo.util.ActivityManager;
import com.szpt.zzq.smarttrafficdemo.util.SharedPreferenceUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends ActivityManager {

    private Spinner mConnectTargetSpinner;
    private EditText mIPEditText;
    private EditText mPortEditText;
    private Button mConnectButton;

    private SharedPreferenceUtil mSharedPreferenceUtil;
    private SpinnerAdapter mSpinnerAdapter;
    private String[] requestArray;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        setContentView(R.layout.activity_login);

        requestArray = getResources().getStringArray(R.array.request_array);
        mConnectTargetSpinner = (Spinner) findViewById(R.id.login_activity_spinner);
        mSpinnerAdapter = new SpinnerAdapter(this, requestArray);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mConnectTargetSpinner.setAdapter(mSpinnerAdapter);
        mConnectTargetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSharedPreferenceUtil = new SharedPreferenceUtil(this);
        mIPEditText = (EditText) findViewById(R.id.login_activity_ip_edit_text);
        if (mSharedPreferenceUtil.getLastIpAddress() != null) {
            mIPEditText.setText(mSharedPreferenceUtil.getLastIpAddress());
        }

        mPortEditText = (EditText) findViewById(R.id.login_activity_port_edit_text);
        if (mSharedPreferenceUtil.getLastPortAddress() != null) {
            mPortEditText.setText(mSharedPreferenceUtil.getLastPortAddress());
        }

        mConnectButton = (Button) findViewById(R.id.login_activity_connect_button);
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = mIPEditText.getText().toString();
                String port = mPortEditText.getText().toString();
                String type = requestArray[mConnectTargetSpinner.getSelectedItemPosition()];
                checkAvailableConnection(ip, port, type);
            }
        });

        changeStatusBarColor();
    }

    // 检查连接是否可用
    private void checkAvailableConnection(String ip, String port, String type) {
        if (isIPAddress(ip)) {
            if (isPortNum(port)) {
                mSharedPreferenceUtil.setLastIpAddress(ip);
                mSharedPreferenceUtil.setLastPortAddress(port);
                showMessage(this, "correct ip: " + ip + ", port: " + port + ", select position: " + mConnectTargetSpinner.getSelectedItemPosition());
            } else {
                showMessage(this, getString(R.string.incorrect_port));
            }
        } else {
            showMessage(this, getString(R.string.incorrect_ip));
        }
    }

    // 检查IP是否可用
    private boolean isIPAddress(String ip) {
        String REGEX_FOR_IP = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +
                                "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                                "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                                "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern pattern = Pattern.compile(REGEX_FOR_IP);
        Matcher matcher = pattern.matcher(ip);
        return matcher.find();
    }

    private boolean isPortNum(String port) {
        String REGEX_FOR_PORT = "^\\d|[1-9]\\d|[1-9]\\d{2}|[1-9]\\d{3}|[1-5]\\d{4}|6[0-4]//d{3}|65[0-4]\\d{2}|655[0-2]//d|6553[0-5]$";
        Pattern pattern = Pattern.compile(REGEX_FOR_PORT);
        Matcher matcher = pattern.matcher(port);
        return matcher.find();
    }


    // 设置状态栏背景为透明
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private class SpinnerAdapter extends ArrayAdapter<String> {
        private Context mContext;
        private String[] mRequestArray;

        public SpinnerAdapter(Context context, String[] requestArray) {
            super(context, R.layout.spinner_item, requestArray);
            mContext = context;
            mRequestArray = requestArray;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.spinner_item, parent, false);
            }

            convertView.setBackgroundColor(Color.TRANSPARENT);
            TextView textView = convertView.findViewById(R.id.spinner_item_text_view);
            textView.setText(mRequestArray[position]);
            textView.setTextColor(Color.WHITE);
            return convertView;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.spinner_item, parent, false);
            }

            convertView.setBackgroundColor(getColor(R.color.spinner_item_background));
            TextView textView = convertView.findViewById(R.id.spinner_item_text_view);
            textView.setText(mRequestArray[position]);
            textView.setTextColor(Color.WHITE);
            return convertView;
        }
    }
}
