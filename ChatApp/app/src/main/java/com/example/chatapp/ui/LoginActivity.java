package com.example.chatapp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.example.chatapp.db.User;
import com.example.chatapp.db.UserDao;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView buttonRegister;
    private UserDao userDao;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.Loginbutton);
        buttonRegister = findViewById(R.id.register);

        userDao = new UserDao(this);
        userDao.open();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = editTextPhone.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "手机号或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isValidUser = userDao.checkUserExists(phoneNumber);

                if (isValidUser) {
                    User user = userDao.getUser(phoneNumber);
                    if (user != null && user.getPassword().equals(password)) {
                        Intent intent = new Intent(LoginActivity.this, ContactListActivity.class);
                        intent.putExtra("currentUser",phoneNumber);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "手机号未注册！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userDao.close();
    }
}
