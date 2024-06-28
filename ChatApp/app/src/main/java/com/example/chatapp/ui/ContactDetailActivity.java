package com.example.chatapp.ui;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.chatapp.R;
import com.example.chatapp.db.DatabaseHelper;

public class ContactDetailActivity extends AppCompatActivity {

    private EditText contactNameEditText;
    private TextView contactPhoneTextView;
    private Button confirmButton;

    private DatabaseHelper dbHelper;
    private String contactId;  // 存储联系人ID，以便在数据库中识别联系人

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        dbHelper = new DatabaseHelper(this);

        // 初始化界面元素
        contactNameEditText = findViewById(R.id.contact_name_edit);
        contactPhoneTextView = findViewById(R.id.contact_phone);
        confirmButton = findViewById(R.id.confirm_button);

        // 从Intent中获取联系人信息
        contactId = getIntent().getStringExtra("contact_id");  // 确保通过Intent传递联系人ID
        String contactName = getIntent().getStringExtra("contact_name");
        String contactPhone = getIntent().getStringExtra("contact_phone");

        // 设置联系人信息到界面上
        contactNameEditText.setText(contactName);
        contactPhoneTextView.setText(contactPhone);

        // 设置确认按钮点击事件
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newContactName = contactNameEditText.getText().toString().trim();
                if (!newContactName.isEmpty()) {
                    updateContactName(contactId, newContactName);
                } else {
                    Toast.makeText(ContactDetailActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateContactName(String contactId, String newContactName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CONTACT_NAME, newContactName);
        String selection = DatabaseHelper.COLUMN_CONTACT_ID + "=?";
        String[] selectionArgs = { contactId };

        int count = db.update(DatabaseHelper.TABLE_CONTACTS, values, selection, selectionArgs);
        if (count > 0) {
            Toast.makeText(this, "联系人姓名更新成功", Toast.LENGTH_SHORT).show();
            finish();  // 更新后关闭当前Activity
        } else {
            Toast.makeText(this, "更新失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }
}
