package com.example.chatapp.ui;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;

public class ContactDetailActivity extends AppCompatActivity {

    private TextView contactNameTextView;
    private TextView contactPhoneTextView;
    private TextView contactEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        // 初始化界面元素
        contactNameTextView = findViewById(R.id.contact_name);
        contactPhoneTextView = findViewById(R.id.contact_phone);
        contactEmailTextView = findViewById(R.id.contact_email);

        // 从Intent中获取联系人信息，这里假设通过Intent传递
        String contactName = getIntent().getStringExtra("contact_name");
        String contactPhone = getIntent().getStringExtra("contact_phone");
        String contactEmail = getIntent().getStringExtra("contact_email");

        // 设置联系人信息到界面上
        contactNameTextView.setText("姓名：" + contactName);
        contactPhoneTextView.setText("电话：" + contactPhone);
        contactEmailTextView.setText("邮箱：" + contactEmail);
    }
}
