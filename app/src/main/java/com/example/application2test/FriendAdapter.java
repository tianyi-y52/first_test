package com.example.application2test;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class FriendAdapter extends BaseAdapter {

    private Context context;
    private List<Friend> friends;
    private LayoutInflater inflater;

    public FriendAdapter(Context context, List<Friend> friends) {
        this.context = context;
        this.friends = friends;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Friend getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_friend, parent, false);
            holder = new ViewHolder();
            holder.avatar = convertView.findViewById(R.id.iv_avatar);
            holder.name = convertView.findViewById(R.id.tv_name);
            holder.status = convertView.findViewById(R.id.tv_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Friend friend = getItem(position);
        holder.avatar.setImageResource(friend.getAvatarResId());
        holder.name.setText(friend.getName());
        holder.status.setText(friend.getStatus());

        return convertView;
    }

    private static class ViewHolder {
        ImageView avatar;
        TextView name;
        TextView status;
    }
}
