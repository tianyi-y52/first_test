package com.example.application2test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private RadioGroup avatarRadioGroup;
    private CustomToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        avatarRadioGroup = findViewById(R.id.avatar_radio_group);
        toolbar = findViewById(R.id.toolbar);

        // 设置工具栏
        toolbar.setTitle("社交登录");
        toolbar.setAvatar(R.drawable.avatar1);

        // 直接设置登录按钮点击事件
        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 获取选中的头像
                int selectedAvatarId = getSelectedAvatar();

                // 创建User对象
                User user = new User(username, password, selectedAvatarId);

                // 跳转到好友列表界面
                Intent intent = new Intent(MainActivity.this, FriendListActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    private int getSelectedAvatar() {
        int selectedId = avatarRadioGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.rb_avatar1) {
            return R.drawable.avatar1;
        } else if (selectedId == R.id.rb_avatar2) {
            return R.drawable.avatar2;
        } else if (selectedId == R.id.rb_avatar3) {
            return R.drawable.avatar3;
        }
        return R.drawable.avatar1;
    }
}