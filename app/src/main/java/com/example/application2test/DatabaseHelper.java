package com.example.application2test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "social_app.db";
    private static final int DATABASE_VERSION = 1;

    // 用户表
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_AVATAR = "avatar";

    // 好友表
    private static final String TABLE_FRIENDS = "friends";
    private static final String COLUMN_FRIEND_ID = "friend_id";
    private static final String COLUMN_FRIEND_NAME = "friend_name";
    private static final String COLUMN_STATUS = "status";

    // 消息表
    private static final String TABLE_MESSAGES = "messages";
    private static final String COLUMN_MESSAGE_ID = "message_id";
    private static final String COLUMN_SENDER_ID = "sender_id";
    private static final String COLUMN_RECEIVER_ID = "receiver_id";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建用户表
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_AVATAR + " INTEGER" + ")";
        db.execSQL(createUsersTable);

        // 创建好友表
        String createFriendsTable = "CREATE TABLE " + TABLE_FRIENDS + "(" +
                COLUMN_FRIEND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_FRIEND_NAME + " TEXT," +
                COLUMN_STATUS + " TEXT," +
                COLUMN_AVATAR + " INTEGER" + ")";
        db.execSQL(createFriendsTable);

        // 创建消息表
        String createMessagesTable = "CREATE TABLE " + TABLE_MESSAGES + "(" +
                COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_SENDER_ID + " INTEGER," +
                COLUMN_RECEIVER_ID + " INTEGER," +
                COLUMN_CONTENT + " TEXT," +
                COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(createMessagesTable);

        // 插入初始数据
        insertInitialData(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        // 插入默认用户
        ContentValues userValues = new ContentValues();
        userValues.put(COLUMN_USERNAME, "testuser");
        userValues.put(COLUMN_PASSWORD, "password");
        userValues.put(COLUMN_AVATAR, R.drawable.avatar1);
        db.insert(TABLE_USERS, null, userValues);

        // 插入示例好友数据
        String[] friendNames = {"张三", "李四", "王五", "赵六", "钱七", "孙八"};
        String[] statuses = {"在线", "离线", "忙碌", "在线", "离线", "在线"};
        int[] avatars = {R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3,
                R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3};

        for (int i = 0; i < friendNames.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_FRIEND_NAME, friendNames[i]);
            values.put(COLUMN_STATUS, statuses[i]);
            values.put(COLUMN_AVATAR, avatars[i]);
            db.insert(TABLE_FRIENDS, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(db);
    }

    // 好友相关操作
    public List<Friend> getAllFriends() {
        List<Friend> friends = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FRIENDS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_FRIEND_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_FRIEND_NAME);
                int statusIndex = cursor.getColumnIndex(COLUMN_STATUS);
                int avatarIndex = cursor.getColumnIndex(COLUMN_AVATAR);

                if (idIndex != -1 && nameIndex != -1 && statusIndex != -1 && avatarIndex != -1) {
                    Friend friend = new Friend(
                            cursor.getString(nameIndex),
                            cursor.getString(statusIndex),
                            cursor.getInt(avatarIndex)
                    );
                    friends.add(friend);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return friends;
    }

    public void deleteFriend(String friendName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIENDS, COLUMN_FRIEND_NAME + " = ?", new String[]{friendName});
    }

    public void addFriend(String name, String status, int avatarResId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FRIEND_NAME, name);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_AVATAR, avatarResId);
        db.insert(TABLE_FRIENDS, null, values);
    }

    public int getFriendIdByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FRIENDS, new String[]{COLUMN_FRIEND_ID},
                COLUMN_FRIEND_NAME + " = ?", new String[]{name},
                null, null, null);

        int friendId = -1;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_FRIEND_ID);
            if (idIndex != -1) {
                friendId = cursor.getInt(idIndex);
            }
        }
        cursor.close();
        return friendId;
    }

    // 消息相关操作
    public void addMessage(int senderId, int receiverId, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SENDER_ID, senderId);
        values.put(COLUMN_RECEIVER_ID, receiverId);
        values.put(COLUMN_CONTENT, content);
        db.insert(TABLE_MESSAGES, null, values);
    }

    public List<Message> getMessagesWithFriend(int friendId) {
        List<Message> messages = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MESSAGES +
                " WHERE (" + COLUMN_SENDER_ID + " = 1 AND " + COLUMN_RECEIVER_ID + " = ?) OR " +
                "(" + COLUMN_SENDER_ID + " = ? AND " + COLUMN_RECEIVER_ID + " = 1) " +
                "ORDER BY " + COLUMN_TIMESTAMP + " ASC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(friendId), String.valueOf(friendId)});

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_MESSAGE_ID);
                int senderIndex = cursor.getColumnIndex(COLUMN_SENDER_ID);
                int receiverIndex = cursor.getColumnIndex(COLUMN_RECEIVER_ID);
                int contentIndex = cursor.getColumnIndex(COLUMN_CONTENT);
                int timeIndex = cursor.getColumnIndex(COLUMN_TIMESTAMP);

                if (idIndex != -1 && senderIndex != -1 && receiverIndex != -1 &&
                        contentIndex != -1 && timeIndex != -1) {

                    Message message = new Message(
                            cursor.getInt(idIndex),
                            cursor.getInt(senderIndex),
                            cursor.getInt(receiverIndex),
                            cursor.getString(contentIndex),
                            java.sql.Timestamp.valueOf(cursor.getString(timeIndex))
                    );
                    messages.add(message);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return messages;
    }

    // 数据统计方法
    public Map<String, Integer> getFriendStatusStats() {
        Map<String, Integer> stats = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_STATUS + ", COUNT(*) as count FROM " +
                TABLE_FRIENDS + " GROUP BY " + COLUMN_STATUS;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int statusIndex = cursor.getColumnIndex(COLUMN_STATUS);
                int countIndex = cursor.getColumnIndex("count");

                if (statusIndex != -1 && countIndex != -1) {
                    String status = cursor.getString(statusIndex);
                    int count = cursor.getInt(countIndex);
                    stats.put(status, count);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        // 确保所有状态都有默认值
        if (!stats.containsKey("在线")) stats.put("在线", 0);
        if (!stats.containsKey("离线")) stats.put("离线", 0);
        if (!stats.containsKey("忙碌")) stats.put("忙碌", 0);

        return stats;
    }

    // 用户验证
    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{username, password},
                null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

}