package com.example.chatapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.example.chatapp.db.DatabaseHelper;
import com.example.chatapp.db.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    private ListView messageListView;
    private EditText searchMessage;
    private ImageButton addMessageButton;
    private ArrayAdapter<String> adapter;
    private List<String> messageList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        dbHelper = new DatabaseHelper(this);

        messageListView = findViewById(R.id.message_list_view);
        searchMessage = findViewById(R.id.search_message);
        addMessageButton = findViewById(R.id.add_message_button);

        messageList = new ArrayList<>();
        loadMessagesFromDatabase();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
        messageListView.setAdapter(adapter);

        // 添加通讯录按钮
        ImageView tongxunluButton = findViewById(R.id.tongxunlu_button);
        tongxunluButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到联系人列表页面
                Intent intent = new Intent(MessageListActivity.this, ContactListActivity.class);
                intent.putExtra("currentUser", getIntent().getStringExtra("currentUser"));
                startActivity(intent);
            }
        });

        searchMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

//        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // 跳转到聊天页面
//                Intent intent = new Intent(MessageListActivity.this, ChatActivity.class);
//                String selectedMessage = messageList.get(position);
//                String[] parts = selectedMessage.split(":");
//                String sender = parts[0].trim();
//                String receiver = parts[1].trim();
//
//                intent.putExtra("currentUser", sender);
//                intent.putExtra("chatPartner", receiver);
//                startActivity(intent);
//            }
//        });
    }

    private void loadMessagesFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_MESSAGES, null, null, null, null, null, DatabaseHelper.COLUMN_TIMESTAMP + " DESC");

        while (cursor.moveToNext()) {
            String sender = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SENDER));
            String receiver = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RECEIVER));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT));
            messageList.add(sender + ": " + receiver + " - " + content);
        }
        cursor.close();
    }
}
