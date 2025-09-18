package com.example.application2test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class FriendListActivity extends AppCompatActivity
        implements CustomToolbar.OnSettingsClickListener, CustomToolbar.OnBackClickListener {

    private CustomToolbar toolbar;
    private TextView tvWelcome;
    private ListView lvFriends;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        databaseHelper = new DatabaseHelper(this);

        initViews();
        User user = getUserData();
        setupToolbar(user);
        setupFriendList(user);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvWelcome = findViewById(R.id.tv_welcome);
        lvFriends = findViewById(R.id.lv_friends);
    }

    private User getUserData() {
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        if (user == null) {
            user = new User("游客", "", R.drawable.avatar1);
        }
        return user;
    }

    private void setupToolbar(User user) {
        toolbar.setTitle(user.getUsername() + " 的好友");
        toolbar.setAvatar(user.getAvatarResId());
        toolbar.setOnSettingsClickListener(this);
        toolbar.setOnBackClickListener(this);
        toolbar.showBackButton(true); // 显示返回按钮
        tvWelcome.setText("欢迎回来，" + user.getUsername() + "！");
    }

    private void setupFriendList(User user) {
        List<Friend> friends = databaseHelper.getAllFriends();

        FriendAdapter adapter = new FriendAdapter(this, friends);
        lvFriends.setAdapter(adapter);

        lvFriends.setOnItemClickListener((parent, view, position, id) -> {
            Friend friend = friends.get(position);
            openChatWithFriend(friend);
        });

        lvFriends.setOnItemLongClickListener((parent, view, position, id) -> {
            Friend friend = friends.get(position);
            showDeleteDialog(friend);
            return true;
        });
    }

    private void openChatWithFriend(Friend friend) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("friend_name", friend.getName());
        intent.putExtra("friend_id", databaseHelper.getFriendIdByName(friend.getName()));
        startActivity(intent);
    }

    private void showDeleteDialog(Friend friend) {
        new AlertDialog.Builder(this)
                .setTitle("删除好友")
                .setMessage("确定要删除 " + friend.getName() + " 吗?")
                .setPositiveButton("删除", (dialog, which) -> {
                    databaseHelper.deleteFriend(friend.getName());
                    setupFriendList(getUserData());
                    Toast.makeText(this, "已删除好友: " + friend.getName(), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    // 设置按钮点击事件
    @Override
    public void onSettingsClick() {
        showSettingsDialog();
    }

    // 返回按钮点击事件
    @Override
    public void onBackClick() {
        onBackPressed();
    }

    private void showSettingsDialog() {
        String[] options = {"修改个人信息", "添加新好友", "清除聊天记录", "退出登录"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置选项");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        editProfile();
                        break;
                    case 1:
                        showAddFriendDialog();
                        break;
                    case 2:
                        clearChatHistory();
                        break;
                    case 3:
                        logout();
                        break;
                }
            }
        });
        builder.show();
    }

    private void editProfile() {
        Toast.makeText(this, "修改个人信息功能", Toast.LENGTH_SHORT).show();
    }

    private void showAddFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加好友");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("输入好友用户名");
        builder.setView(input);

        builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String friendName = input.getText().toString().trim();
                if (!friendName.isEmpty()) {
                    databaseHelper.addFriend(friendName, "离线", R.drawable.avatar1);
                    setupFriendList(getUserData());
                    Toast.makeText(FriendListActivity.this, "已添加好友: " + friendName, Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void clearChatHistory() {
        new AlertDialog.Builder(this)
                .setTitle("确认清除")
                .setMessage("确定要清除所有聊天记录吗？")
                .setPositiveButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(FriendListActivity.this, "聊天记录已清除", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("退出登录")
                .setMessage("确定要退出当前账号吗？")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(FriendListActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friend_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_visualization) {
            Intent intent = new Intent(this, DataVisualizationActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}