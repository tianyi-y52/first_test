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

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500));
        setPadding(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8));

        avatarImageView = findViewById(R.id.toolbar_avatar);
        titleTextView = findViewById(R.id.toolbar_title);

        ImageButton settingsButton = findViewById(R.id.btn_settings);
        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 可以在这里添加设置按钮的点击事件
            }
        });
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
