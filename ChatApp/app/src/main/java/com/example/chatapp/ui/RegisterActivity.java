package com.example.chatapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chatapp.R;
import com.example.chatapp.db.User;
import com.example.chatapp.db.UserDao;

public class RegisterActivity extends AppCompatActivity {

    private EditText nicknameEditText;//昵称
    private EditText phoneEditText;//手机号码
    private EditText passwordEditText;//密码
    private ImageView ivPhoto;//头像
    private Button registerButton;//注册按钮

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化控件
        nicknameEditText = findViewById(R.id.nickname);//获取昵称
        phoneEditText = findViewById(R.id.phone);//获取手机号
        passwordEditText = findViewById(R.id.password);//获取密码
        ivPhoto = findViewById(R.id.change_avatar_button);//换头像
        registerButton = findViewById(R.id.register_button);//获取注册按钮

        userDao = new UserDao(this);

        //返回
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //不用跳转到登录页面，直接销毁当前页面
                finish();
            }
        });

        //注册按钮点击事件
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String nickname = nicknameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请填写完整的注册信息", Toast.LENGTH_SHORT).show();
        } else {
            User newUser = new User(nickname, phone, password);
            long result = userDao.addUser(newUser);
            if (result != -1) {
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                finish(); // 关闭注册页面，回到登录页面
            } else {
                Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
