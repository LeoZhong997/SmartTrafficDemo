package com.szpt.zzq.smarttrafficdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.szpt.zzq.smarttrafficdemo.util.SharedPreferenceUtil;

public class LoginActivity extends AppCompatActivity {

    private Spinner mConnectTargetSpinner;
    private EditText mIPEditText;
    private EditText mPortEditText;
    private Button mConnectButton;

    private SharedPreferenceUtil mSharedPreferenceUtil;

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

        mConnectTargetSpinner = (Spinner) findViewById(R.id.login_activity_spinner);

        mIPEditText = (EditText) findViewById(R.id.login_activity_ip_edit_text);

        mPortEditText = (EditText) findViewById(R.id.login_activity_port_edit_text);

        mConnectButton = (Button) findViewById(R.id.login_activity_connect_button);
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = mIPEditText.getText().toString();
                String port = mPortEditText.getText().toString();
                Toast.makeText(LoginActivity.this, "ip: " + ip + " port: " + port, Toast.LENGTH_SHORT).show();
            }
        });

        changeStatusBarColor();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
