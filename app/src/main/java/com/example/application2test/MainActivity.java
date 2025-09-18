package com.example.application2test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private CustomToolbar toolbar;
    private ImageView ivAvatar1, ivAvatar2, ivAvatar3;
    private int selectedAvatarResId = R.drawable.avatar1;
    private DatabaseHelper databaseHelper; // 添加数据库帮助类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化数据库
        databaseHelper = new DatabaseHelper(this);

        // 初始化视图
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        toolbar = findViewById(R.id.toolbar);

        // 设置工具栏
        toolbar.setTitle("社交登录");
        toolbar.setAvatar(R.drawable.avatar1);

        // 初始化头像选择功能
        initAvatarSelection();

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

                // 验证用户（这里简化处理，实际应该验证数据库）
                boolean isValid = databaseHelper.validateUser(username, password);
                if (!isValid) {
                    Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 获取选中的头像
                User user = new User(username, password, selectedAvatarResId);

                // 跳转到好友列表界面
                Intent intent = new Intent(MainActivity.this, FriendListActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    private void initAvatarSelection() {
        // 1. 找到三个头像ImageView
        ivAvatar1 = findViewById(R.id.iv_avatar1);
        ivAvatar2 = findViewById(R.id.iv_avatar2);
        ivAvatar3 = findViewById(R.id.iv_avatar3);

        // 2. 设置第一个头像为默认选中状态
        ivAvatar1.setSelected(true);

        // 3. 创建统一的点击监听器
        View.OnClickListener avatarClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3.1 先取消所有头像的选中状态
                ivAvatar1.setSelected(false);
                ivAvatar2.setSelected(false);
                ivAvatar3.setSelected(false);

                // 3.2 设置被点击的头像为选中状态
                v.setSelected(true);

                // 3.3 更新选中的头像资源ID
                int viewId = v.getId();
                if (viewId == R.id.iv_avatar1) {
                    selectedAvatarResId = R.drawable.avatar1;
                } else if (viewId == R.id.iv_avatar2) {
                    selectedAvatarResId = R.drawable.avatar2;
                } else if (viewId == R.id.iv_avatar3) {
                    selectedAvatarResId = R.drawable.avatar3;
                }

                // 可选：立即更新工具栏头像预览
                toolbar.setAvatar(selectedAvatarResId);
            }
        };

        // 4. 为每个头像设置点击监听器
        ivAvatar1.setOnClickListener(avatarClickListener);
        ivAvatar2.setOnClickListener(avatarClickListener);
        ivAvatar3.setOnClickListener(avatarClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭数据库连接
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}