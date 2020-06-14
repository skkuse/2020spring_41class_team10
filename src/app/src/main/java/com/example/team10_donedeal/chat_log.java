package com.example.team10_donedeal;

public class chat_log {
    public String ID;
    public String log;

    public chat_log(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public chat_log(String ID, String log) {
        this.ID = ID;
        this.log = log;
    }
}
