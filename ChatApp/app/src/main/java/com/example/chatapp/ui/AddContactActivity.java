package com.example.chatapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.chatapp.R;

public class AddContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // 初始化控件
        EditText contactNameEditText = findViewById(R.id.contact_name);
        EditText contactPhoneEditText = findViewById(R.id.contact_phone);
        Button addContactButton = findViewById(R.id.add_contact_button);

        // 添加联系人按钮点击事件
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactName = contactNameEditText.getText().toString().trim();
                String contactPhone = contactPhoneEditText.getText().toString().trim();

                if (contactName.isEmpty() || contactPhone.isEmpty()) {
                    Toast.makeText(AddContactActivity.this, "请输入完整的联系人信息", Toast.LENGTH_SHORT).show();
                } else {
                    // 返回新联系人信息
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("newContact", contactName + " (" + contactPhone + ")");
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}
