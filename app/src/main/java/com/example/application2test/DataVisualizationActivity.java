package com.example.application2test;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View; // 添加这行导入
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;

public class DataVisualizationActivity extends AppCompatActivity
        implements CustomToolbar.OnBackClickListener {

    private DatabaseHelper databaseHelper;
    private TextView tvOnlineCount, tvOfflineCount, tvBusyCount;
    private ProgressBar pbOnline, pbOffline, pbBusy;
    private LinearLayout chartContainer;
    private CustomToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_visualization);

        databaseHelper = new DatabaseHelper(this);

        tvOnlineCount = findViewById(R.id.tv_online_count);
        tvOfflineCount = findViewById(R.id.tv_offline_count);
        tvBusyCount = findViewById(R.id.tv_busy_count);
        pbOnline = findViewById(R.id.pb_online);
        pbOffline = findViewById(R.id.pb_offline);
        pbBusy = findViewById(R.id.pb_busy);
        chartContainer = findViewById(R.id.chart_container);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("数据统计");
        toolbar.setOnBackClickListener(this);
        toolbar.showBackButton(true);

        loadStatistics();
    }

    private void loadStatistics() {
        Map<String, Integer> statusData = databaseHelper.getFriendStatusStats();

        int onlineCount = statusData.getOrDefault("在线", 0);
        int offlineCount = statusData.getOrDefault("离线", 0);
        int busyCount = statusData.getOrDefault("忙碌", 0);

        tvOnlineCount.setText(String.valueOf(onlineCount));
        tvOfflineCount.setText(String.valueOf(offlineCount));
        tvBusyCount.setText(String.valueOf(busyCount));

        int total = onlineCount + offlineCount + busyCount;
        if (total > 0) {
            pbOnline.setProgress((onlineCount * 100) / total);
            pbOffline.setProgress((offlineCount * 100) / total);
            pbBusy.setProgress((busyCount * 100) / total);
        }

        createTextChart(onlineCount, offlineCount, busyCount);
    }

    private void createTextChart(int onlineCount, int offlineCount, int busyCount) {
        chartContainer.removeAllViews();

        int total = onlineCount + offlineCount + busyCount;
        if (total == 0) {
            TextView noDataText = new TextView(this);
            noDataText.setText("暂无数据");
            noDataText.setTextSize(16);
            noDataText.setGravity(android.view.Gravity.CENTER);
            chartContainer.addView(noDataText);
            return;
        }

        addStatusBar("在线", onlineCount, total, Color.parseColor("#4CAF50"));
        addStatusBar("离线", offlineCount, total, Color.parseColor("#9E9E9E"));
        addStatusBar("忙碌", busyCount, total, Color.parseColor("#FF9800"));
    }

    private void addStatusBar(String status, int count, int total, int color) {
        if (count == 0) return;

        float percentage = (count * 100f) / total;

        LinearLayout barLayout = new LinearLayout(this);
        barLayout.setOrientation(LinearLayout.VERTICAL);
        barLayout.setPadding(0, 8, 0, 8);

        TextView label = new TextView(this);
        label.setText(String.format("%s: %d人 (%.1f%%)", status, count, percentage));
        label.setTextSize(14);

        LinearLayout progressContainer = new LinearLayout(this);
        progressContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 20));
        progressContainer.setBackgroundColor(Color.LTGRAY);

        // 这里使用android.view.View
        android.view.View progressBar = new android.view.View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = percentage;
        progressBar.setLayoutParams(params);
        progressBar.setBackgroundColor(color);

        progressContainer.addView(progressBar);
        barLayout.addView(label);
        barLayout.addView(progressContainer);

        chartContainer.addView(barLayout);
    }

    // 返回按钮点击事件
    @Override
    public void onBackClick() {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStatistics();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}