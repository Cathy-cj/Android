package com.example.chatapp.db;

public class Message {
    private int id;
    private String content;
    private String sender;
    private String receiver;
    private long timestamp;

    public Message() {
        // 无参构造函数
    }

    public Message(String content, String sender, String receiver, long timestamp) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }

    public Message(int id, String content, String sender, String receiver, long timestamp) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
