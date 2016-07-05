package com.github.clearable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText mAccountEt;
    EditText mPasswordEt;
    EditText mEmailEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccountEt = (EditText) findViewById(R.id.et_account);
        mPasswordEt = (EditText) findViewById(R.id.et_password);
        mEmailEt = (EditText) findViewById(R.id.et_email);
    }


    public void onClick(View view) {

        mAccountEt.setError("账号不对！");
        mPasswordEt.setError("密码不对！");
        mEmailEt.setError("邮箱地址不对！");
    }
}
