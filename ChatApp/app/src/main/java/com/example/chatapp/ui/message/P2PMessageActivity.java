package com.example.chatapp.ui.message;

import static org.koin.java.KoinJavaComponent.inject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatapp.base.BaseActivity;
import com.example.chatapp.databinding.ActivityP2pMessageBinding;
import com.example.chatapp.db.message.Message;
import com.example.chatapp.db.message.MessageDao;
import com.example.chatapp.ui.message.adapter.P2PMessageAdapter;
import com.example.chatapp.util.UserCache;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import kotlin.Lazy;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class P2PMessageActivity extends BaseActivity<ActivityP2pMessageBinding> {

    Lazy<MessageDao> messageDao = inject(MessageDao.class);
    private String friendPhone = "";
    private P2PMessageAdapter messageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 确保软键盘弹出时布局调整
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void viewBound() {
        String contactName = getIntent().getStringExtra("friendName");
        friendPhone = getIntent().getStringExtra("friendPhone");
        getBinding().contactName.setText(contactName == null ? "UnKnow Contact" : contactName);

        messageAdapter = new P2PMessageAdapter();
        getBinding().chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getBinding().chatRecyclerView.setAdapter(messageAdapter);

        // 发送
        getBinding().sendButton.setOnClickListener(view -> {
            String content = getBinding().messageEditText.getText().toString();
            if (TextUtils.isEmpty(content)) {
                showToast("你未输入任何消息");
            } else {
                launchIO(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        try {
                            String myPhone = Objects.requireNonNull(UserCache.INSTANCE.getUser()).getPhone();
                            Message message = new Message(
                                    UUID.randomUUID().toString(),
                                    myPhone,
                                    friendPhone,
                                    content,
                                    "",
                                    false,
                                    System.currentTimeMillis());
                            messageDao.getValue().insert(message);
                            runOnUiThread(() -> {
                                getBinding().messageEditText.getText().clear();
                                messageAdapter.add(message);
                            });
                        } catch (Exception e) {
                            showToast("消息发送失败");
                        }
                        return null;
                    }
                });
            }
        });

        // 消息长按
        messageAdapter.setOnItemLongClickListener((baseQuickAdapter, view, i) -> {
            // todo
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoadingDialog(null, true);
        launchIO(() -> {
            String userPhone = Objects.requireNonNull(UserCache.INSTANCE.getUser()).getPhone();
            List<Message> messages = messageDao.getValue().queryMessages(userPhone, friendPhone);
            runOnUiThread(() -> {
                messageAdapter.submitList(messages);
                closeLoadingDialog();
            });
            return null;
        });
    }
}
