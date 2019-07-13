package xin.banghua.beiyuan.Signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import xin.banghua.beiyuan.MainActivity;
import xin.banghua.beiyuan.R;

public class SignupActivity extends Activity {

    //账号密码输入框
    EditText userAccount;
    EditText userPassword;
    //验证码按钮和提交按钮
    Button authCode_btn,submit_btn;

    //属性
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        submit_btn = findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAccount = (EditText) findViewById(R.id.userAccount);
                userPassword = (EditText) findViewById(R.id.userPassword);
                Intent intent = new Intent(SignupActivity.this, Userset.class);
                intent.putExtra("logtype","1");
                intent.putExtra("userAccount",userAccount.getText().toString());
                intent.putExtra("userPassword",userPassword.getText().toString());
                startActivity(intent);
            }
        });
    }
}
