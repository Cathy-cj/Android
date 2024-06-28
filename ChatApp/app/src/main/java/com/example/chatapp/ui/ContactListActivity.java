package com.example.chatapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.example.chatapp.db.DatabaseHelper;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    private ArrayList<String> contacts;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        dbHelper = new DatabaseHelper(this);

        // 初始化联系人列表
        contacts = new ArrayList<>();
        loadContactsFromDatabase();

        // 设置适配器
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        ListView listView = findViewById(R.id.contact_list_view);
        listView.setAdapter(adapter);

        // 添加联系人按钮
        ImageButton addContactButton = findViewById(R.id.add_contact_button);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ContactListActivity", "Add contact button clicked");
                // 跳转到添加联系人页面
                Intent intent = new Intent(ContactListActivity.this, AddContactActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // 搜索联系人
        EditText searchContact = findViewById(R.id.search_contact);
        searchContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 点击联系人
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 跳转到联系人详情页面
                Intent intent = new Intent(ContactListActivity.this, ContactDetailActivity.class);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.query(DatabaseHelper.TABLE_CONTACTS, null, null, null, null, null, null);

                cursor.moveToPosition(position);
                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_ID));
                String contactName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_NAME));
                String contactPhone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_PHONE));
                cursor.close();

                intent.putExtra("contact_id", contactId);
                intent.putExtra("contact_name", contactName);
                intent.putExtra("contact_phone", contactPhone);
                startActivity(intent);
            }
        });

        // 长按联系人删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 弹出删除确认对话框
                showDeleteConfirmDialog(position);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 重新加载联系人数据
        contacts.clear();
        loadContactsFromDatabase();
        adapter.notifyDataSetChanged();
    }

    private void showDeleteConfirmDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("删除联系人")
                .setMessage("确定要删除这个联系人吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    deleteContactFromDatabase(contacts.get(position));
                    contacts.remove(position);
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 获取新联系人
            String newContact = data.getStringExtra("newContact");
            contacts.add(newContact);
            adapter.notifyDataSetChanged();
        }
    }

    private void loadContactsFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_PHONE));
            contacts.add(name + " (" + phone + ")");
        }
        cursor.close();
    }

    private void deleteContactFromDatabase(String contact) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String phone = contact.substring(contact.indexOf("(") + 1, contact.indexOf(")"));
        db.delete(DatabaseHelper.TABLE_CONTACTS, DatabaseHelper.COLUMN_CONTACT_PHONE + "=?", new String[]{phone});
    }
}
