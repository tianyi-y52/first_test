package com.example.application2test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private List<Message> messages;
    private LayoutInflater inflater;
    private int currentUserId; // 当前用户ID，用于区分发送和接收的消息

    // 用于标识布局类型的常量
    private static final int LAYOUT_SENT = 1;
    private static final int LAYOUT_RECEIVED = 2;

    public MessageAdapter(Context context, List<Message> messages, int currentUserId) {
        this.context = context;
        this.messages = messages;
        this.currentUserId = currentUserId;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = getItem(position);
        return message.getSenderId() == currentUserId ? LAYOUT_SENT : LAYOUT_RECEIVED;
    }

    @Override
    public int getViewTypeCount() {
        return 2; // 发送和接收两种类型
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Message message = getItem(position);

        // 判断消息类型（发送或接收）
        boolean isSentMessage = message.getSenderId() == currentUserId;
        int layoutRes = isSentMessage ? R.layout.item_message_sent : R.layout.item_message_received;
        int viewType = isSentMessage ? LAYOUT_SENT : LAYOUT_RECEIVED;

        if (convertView == null) {
            convertView = inflater.inflate(layoutRes, parent, false);
            holder = new ViewHolder();
            holder.content = convertView.findViewById(R.id.tv_message_content);
            holder.time = convertView.findViewById(R.id.tv_message_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.content.setText(message.getContent());
        holder.time.setText(message.getFormattedTime());

        return convertView;
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    public void updateMessages(List<Message> newMessages) {
        messages.clear();
        messages.addAll(newMessages);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView content;
        TextView time;
    }
}