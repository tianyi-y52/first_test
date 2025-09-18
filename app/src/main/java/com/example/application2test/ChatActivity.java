package com.example.application2test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity
        implements CustomToolbar.OnBackClickListener {

    private DatabaseHelper databaseHelper;
    private ListView messagesListView;
    private EditText messageInput;
    private Button sendButton;
    private MessageAdapter adapter;
    private String currentFriend;
    private int currentFriendId;
    private static final int CURRENT_USER_ID = 1;
    private List<Message> messages = new ArrayList<>();
    private CustomToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        databaseHelper = new DatabaseHelper(this);

        currentFriend = getIntent().getStringExtra("friend_name");
        currentFriendId = getIntent().getIntExtra("friend_id", -1);

        toolbar = findViewById(R.id.toolbar);
        messagesListView = findViewById(R.id.messages_list);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);

        setupToolbar();

        adapter = new MessageAdapter(this, messages, CURRENT_USER_ID);
        messagesListView.setAdapter(adapter);

        loadMessages();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void setupToolbar() {
        toolbar.setTitle("与 " + currentFriend + " 聊天");
        toolbar.setOnBackClickListener(this);
        toolbar.showBackButton(true);
    }

    private void loadMessages() {
        if (currentFriendId != -1) {
            List<Message> newMessages = databaseHelper.getMessagesWithFriend(currentFriendId);
            messages.clear();
            messages.addAll(newMessages);
            adapter.notifyDataSetChanged();
            scrollToBottom();
        }
    }

    private void sendMessage() {
        String messageContent = messageInput.getText().toString().trim();
        if (!messageContent.isEmpty() && currentFriendId != -1) {
            databaseHelper.addMessage(CURRENT_USER_ID, currentFriendId, messageContent);
            messageInput.setText("");
            loadMessages();
        }
    }

    private void scrollToBottom() {
        messagesListView.post(new Runnable() {
            @Override
            public void run() {
                messagesListView.setSelection(adapter.getCount() - 1);
            }
        });
    }

    // 返回按钮点击事件
    @Override
    public void onBackClick() {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMessages();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}