package com.example.chatapp.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.ContactListActivity;
import com.example.chatapp.R;
import com.example.chatapp.db.DatabaseHelper;

public class AddContactActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String currentUserPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        dbHelper = new DatabaseHelper(this);

        // 获取当前登录用户的手机号
        currentUserPhone = getIntent().getStringExtra("currentUser");

        EditText contactNameEditText = findViewById(R.id.contact_name);
        EditText contactPhoneEditText = findViewById(R.id.contact_phone);
        Button addContactButton = findViewById(R.id.add_contact_button);
        ImageView backAddContact = findViewById(R.id.back_addcontact);

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactName = contactNameEditText.getText().toString().trim();
                String contactPhone = contactPhoneEditText.getText().toString().trim();

                if (contactName.isEmpty() || contactPhone.isEmpty()) {
                    Toast.makeText(AddContactActivity.this, "请输入完整的联系人信息", Toast.LENGTH_SHORT).show();
                } else {
                    if (isPhoneRegistered(contactPhone)) {
                        if (isContactExists(contactPhone)) {
                            Toast.makeText(AddContactActivity.this, "已存在此联系人", Toast.LENGTH_SHORT).show();
                        } else {
                            saveContact(contactName, contactPhone);
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("newContact", contactName + " (" + contactPhone + ")");
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    } else {
                        Toast.makeText(AddContactActivity.this, "没有此用户", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        backAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddContactActivity.this, ContactListActivity.class);
                intent.putExtra("currentUser", currentUserPhone);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean isPhoneRegistered(String phone) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {DatabaseHelper.COLUMN_PHONE};
        String selection = DatabaseHelper.COLUMN_PHONE + "=?";
        String[] selectionArgs = {phone};
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        boolean isRegistered = cursor.getCount() > 0;
        cursor.close();
        return isRegistered;
    }

    private boolean isContactExists(String phone) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {DatabaseHelper.COLUMN_CONTACT_PHONE};
        String selection = DatabaseHelper.COLUMN_CONTACT_PHONE + "=?";
        String[] selectionArgs = {phone};
        Cursor cursor = db.query(DatabaseHelper.TABLE_CONTACTS, columns, selection, selectionArgs, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private void saveContact(String name, String phone) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CONTACT_NAME, name);
        values.put(DatabaseHelper.COLUMN_CONTACT_PHONE, phone);
        values.put(DatabaseHelper.COLUMN_PHONE, currentUserPhone); // 保存当前用户的手机号
        db.insert(DatabaseHelper.TABLE_CONTACTS, null, values);
    }
}
