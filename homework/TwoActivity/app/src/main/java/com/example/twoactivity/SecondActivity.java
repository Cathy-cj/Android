package com.example.twoactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // 获取启动SecondActivity的Intent
        Intent intent=getIntent();
        // 从Intent中获取名为"name"的字符串数据
        String name = intent.getStringExtra("name");
        // 从Intent中获取名为"id"的整数数据，如果找不到则返回默认值0
        int id = intent.getIntExtra("id",0);
        // 将获取到的数据打印到日志中，用于调试或查看
        Toast.makeText(this,"name:"+name+"id:"+id,1).show();
    }
}