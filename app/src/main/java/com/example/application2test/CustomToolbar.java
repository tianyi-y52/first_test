package com.example.application2test;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

public class CustomToolbar extends LinearLayout {

    private ImageView avatarImageView;
    private TextView titleTextView;
    private ImageButton settingsButton;
    private ImageButton backButton;
    private OnSettingsClickListener onSettingsClickListener;
    private OnBackClickListener onBackClickListener;

    // 设置按钮点击监听器接口
    public interface OnSettingsClickListener {
        void onSettingsClick();
    }

    // 返回按钮点击监听器接口
    public interface OnBackClickListener {
        void onBackClick();
    }

    public CustomToolbar(Context context) {
        super(context);
        init(context);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_toolbar, this, true);

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500));
        setPadding(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8));

        avatarImageView = findViewById(R.id.toolbar_avatar);
        titleTextView = findViewById(R.id.toolbar_title);
        settingsButton = findViewById(R.id.btn_settings);
        backButton = findViewById(R.id.btn_back);

        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSettingsClickListener != null) {
                    onSettingsClickListener.onSettingsClick();
                }
            }
        });

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBackClickListener != null) {
                    onBackClickListener.onBackClick();
                }
            }
        });
    }

    // 设置点击监听器
    public void setOnSettingsClickListener(OnSettingsClickListener listener) {
        this.onSettingsClickListener = listener;
    }

    // 设置返回按钮监听器
    public void setOnBackClickListener(OnBackClickListener listener) {
        this.onBackClickListener = listener;
    }

    // 显示/隐藏返回按钮
    public void showBackButton(boolean show) {
        backButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setAvatar(int avatarResId) {
        avatarImageView.setImageResource(avatarResId);
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}