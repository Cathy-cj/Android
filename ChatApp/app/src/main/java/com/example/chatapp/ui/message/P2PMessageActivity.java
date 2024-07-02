package com.example.chatapp.ui.message;

import static org.koin.java.KoinJavaComponent.inject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatapp.base.BaseActivity;
import com.example.chatapp.databinding.ActivityP2pMessageBinding;
import com.example.chatapp.db.contacts.Contacts;
import com.example.chatapp.db.message.Message;
import com.example.chatapp.db.message.MessageDao;
import com.example.chatapp.ui.message.adapter.P2PMessageAdapter;
import com.example.chatapp.util.UserCache;
import com.kongzue.dialogx.dialogs.BottomMenu;
import com.kongzue.dialogx.dialogs.MessageDialog;

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
            showMessageLongClickDialog(messageAdapter.getItem(i));
            return true;
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

    /**
     * 长按消息弹窗
     *
     * @param message 消息
     */
    private void showMessageLongClickDialog(Message message) {
        BottomMenu.show(List.of("删除", "撤回"))
                .setOnMenuItemClickListener((dialog, text, index) -> {
                    if (index == 0) {
                        showDeleteMessageDialog(message);
                    } else if (index == 1) {
                        showRevokeMessageDialog(message);
                    }
                    return false;
                });
    }

    /**
     * 撤回消息
     *
     * @param message
     */
    private void showRevokeMessageDialog(Message message) {
        new MessageDialog("提示", "确认撤回该消息吗？", "确定", "取消")
                .setOkButtonClickListener((dialog, v) -> {
                    launchIO(() -> {
                        try {
                            messageDao.getValue().revoke(message.getId());
                            runOnUiThread(() -> {
                                messageAdapter.remove(message);
                                showToast("撤回成功");
                            });
                        } catch (Exception e) {
                            showToast("撤回失败");
                        }
                        return null;
                    });
                    return false;
                })
                .setCancelButtonClickListener((dialog, v) -> {
                    dialog.dismiss();
                    return false;
                }).show();
    }

    /**
     * 删除消息
     *
     * @param message
     */
    private void showDeleteMessageDialog(Message message) {
        new MessageDialog("提示", "确认删除该消息吗？", "确定", "取消")
                .setOkButtonClickListener((dialog, v) -> {
                    launchIO(() -> {
                        try {
                            messageDao.getValue().delete(message.getId(), UserCache.INSTANCE.getUser().getPhone());
                            runOnUiThread(() -> {
                                messageAdapter.remove(message);
                                showToast("删除成功");
                            });
                        } catch (Exception e) {
                            showToast("删除失败");
                        }
                        return null;
                    });
                    return false;
                })
                .setCancelButtonClickListener((dialog, v) -> {
                    dialog.dismiss();
                    return false;
                }).show();
    }
}
