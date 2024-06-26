package com.example.twoactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // 创建一个新的Intent对象，用于启动SecondActivity
        Intent intent = new Intent(this,SecondActivity.class);
        // 向Intent中添加一个名为"name"的字符串数据
        intent.putExtra("name", "陈婧");

        // 向Intent中添加一个名为"id"的整数数据
        intent.putExtra("id", 2022011132);

        // 使用Intent启动SecondActivity
        startActivity(intent);
    }
}