package com.example.application2test;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private CustomToolbar toolbar;
    private TextView tvWelcome;
    private ListView lvFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

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
        tvWelcome.setText("欢迎回来，" + user.getUsername() + "！");
    }

    private void setupFriendList(User user) {
        List<Friend> friends = createFriendList();

        FriendAdapter adapter = new FriendAdapter(this, friends);
        lvFriends.setAdapter(adapter);

        lvFriends.setOnItemClickListener((parent, view, position, id) -> {
            Friend friend = friends.get(position);
            Toast.makeText(this, "点击了: " + friend.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    private List<Friend> createFriendList() {
        List<Friend> friends = new ArrayList<>();
        friends.add(new Friend("张三", "在线", R.drawable.avatar1));
        friends.add(new Friend("李四", "离线", R.drawable.avatar2));
        friends.add(new Friend("王五", "忙碌", R.drawable.avatar3));
        friends.add(new Friend("赵六", "在线", R.drawable.avatar1));
        friends.add(new Friend("钱七", "离线", R.drawable.avatar2));
        friends.add(new Friend("孙八", "在线", R.drawable.avatar3));
        return friends;
    }
}
