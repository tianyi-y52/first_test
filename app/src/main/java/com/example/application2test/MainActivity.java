package com.example.application2test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button confirm;
    private Button cancel;
    private EditText editText1;
    private EditText editText2;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        confirm = findViewById(R.id.button1);
        cancel = findViewById(R.id.button2);
        editText1 = findViewById(R.id.edit_text1);
        editText2 = findViewById(R.id.edit_text2);
        progressBar = findViewById(R.id.progress_bar);
        ImageView imageView = findViewById(R.id.image_view);
        imageView.setAlpha(0.5f);

        // 确认按钮逻辑
        confirm.setOnClickListener(v -> {
            String inputText1 = editText1.getText().toString().trim();
            String inputText2 = editText2.getText().toString().trim();
            String expectedText1 = "123";
            String expectedText2 = "123456";

            if (inputText1.equals(expectedText1) && inputText2.equals(expectedText2)) {
                // 使用 ObjectAnimator 让进度条平滑增加到 100%
                animateProgressBar();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("输入错误")
                        .setMessage("您输入的内容不符合要求，请重新输入！")
                        .setCancelable(false)
                        .setPositiveButton("确定", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        // 取消按钮逻辑
        cancel.setOnClickListener(v -> {
            editText1.setText("");
            editText2.setText("");
        });
    }

    /**
     * 使用 ObjectAnimator 让进度条平滑增加到 100%
     */
    private void animateProgressBar() {
        // 从当前进度动画到 100%，持续 1 秒（1000ms）
        ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), 100);
        animator.setDuration(1000); // 动画时长
        animator.start();

        // 可选：动画结束后显示 Toast
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Toast.makeText(MainActivity.this, "验证成功，进度已更新", Toast.LENGTH_SHORT).show();
            }
        });
    }
}