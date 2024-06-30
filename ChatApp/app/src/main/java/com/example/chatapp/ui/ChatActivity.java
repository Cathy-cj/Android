//package com.example.chatapp.ui;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.chatapp.R;
//import com.example.chatapp.adapters.MsgAdapter;
//import com.example.chatapp.db.DatabaseHelper;
//import com.example.chatapp.models.PersonChat;
//import com.example.chatapp.utils.SPUtils;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class ChatActivity extends AppCompatActivity {
//    private DatabaseHelper helper;
//    private SQLiteDatabase db;
//    private List<PersonChat> chatList = new ArrayList<>();
//    private RecyclerView msgRecyclerView;
//    private MsgAdapter adapter;
//    private EditText etMessage;
//    private Button btnSend;
//    private TextView tvNickName;
//    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    private int userId;
//    private int toUserId;
//    private String toUserName;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        helper = new DatabaseHelper(this);
//        db = helper.getWritableDatabase();
//
//        userId = (int) SPUtils.get(this, SPUtils.USER_ID, 0);
//
//        initView();
//        initData();
//        initAdapter();
//        initEvent();
//        etMessage.requestFocus();
//    }
//
//    private void initView() {
//        msgRecyclerView = findViewById(R.id.chat_recyclerView);
//        btnSend = findViewById(R.id.btn_chat_message_send);
//        etMessage = findViewById(R.id.et_chat_message);
//        tvNickName = findViewById(R.id.tv_nickName);
//
//        toUserId = getIntent().getIntExtra("toUserId", -1);
//        toUserName = getIntent().getStringExtra("toUserName");
//    }
//
//    private void initData() {
//        if (toUserName != null) {
//            tvNickName.setText(toUserName);
//        }
//    }
//
//    private void initAdapter() {
//        loadChatData();
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        msgRecyclerView.setLayoutManager(layoutManager);
//        adapter = new MsgAdapter(chatList);
//        msgRecyclerView.setAdapter(adapter);
//    }
//
//    private void loadChatData() {
//        String sql = "SELECT * FROM messages WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?)";
//        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId), String.valueOf(toUserId), String.valueOf(toUserId), String.valueOf(userId)});
//
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                int senderId = cursor.getInt(cursor.getColumnIndex("sender"));
//                int receiverId = cursor.getInt(cursor.getColumnIndex("receiver"));
//                String content = cursor.getString(cursor.getColumnIndex("content"));
//                String date = cursor.getString(cursor.getColumnIndex("timestamp"));
//
//                // Get sender and receiver details
//                Cursor senderCursor = db.rawQuery("SELECT nickname FROM users WHERE _id = ?", new String[]{String.valueOf(senderId)});
//                Cursor receiverCursor = db.rawQuery("SELECT nickname FROM users WHERE _id = ?", new String[]{String.valueOf(receiverId)});
//
//                String senderName = "";
//                String receiverName = "";
//
//                if (senderCursor.moveToFirst()) {
//                    senderName = senderCursor.getString(senderCursor.getColumnIndex("nickname"));
//                }
//                if (receiverCursor.moveToFirst()) {
//                    receiverName = receiverCursor.getString(receiverCursor.getColumnIndex("nickname"));
//                }
//
//                PersonChat chat = new PersonChat(senderId, senderName, "", receiverId, receiverName, "", content, date, 0);
//                chatList.add(chat);
//
//                senderCursor.close();
//                receiverCursor.close();
//            }
//            cursor.close();
//        }
//    }
//
//    private void initEvent() {
//        btnSend.setOnClickListener(v -> sendMessage());
//
//        etMessage.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!s.toString().isEmpty() && s.length() > 499) {
//                    Toast.makeText(ChatActivity.this, "最多输入500个字", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) { }
//        });
//
//        etMessage.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_SEND) {
//                sendMessage();
//                return true;
//            }
//            return false;
//        });
//    }
//
//    private void sendMessage() {
//        hintKeyBoard();
//
//        String message = etMessage.getText().toString();
//        if (message.isEmpty()) {
//            Toast.makeText(this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String timestamp = sf.format(new Date());
//        String insertSql = "INSERT INTO messages (sender, receiver, content, timestamp) VALUES (?, ?, ?, ?)";
//        db.execSQL(insertSql, new Object[]{userId, toUserId, message, timestamp});
//
//        PersonChat newMessage = new PersonChat(userId, "", "", toUserId, "", "", message, timestamp, 0);
//        chatList.add(newMessage);
//        adapter.notifyItemInserted(chatList.size() - 1);
//        msgRecyclerView.scrollToPosition(chatList.size() - 1);
//        etMessage.setText("");
//    }
//
//    private void hintKeyBoard() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null && imm.isActive() && getCurrentFocus() != null) {
//            if (getCurrentFocus().getWindowToken() != null) {
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (db != null && db.isOpen()) {
//            db.close();
//        }
//    }
//
//    public void back(View view) {
//        finish();
//    }
//}
