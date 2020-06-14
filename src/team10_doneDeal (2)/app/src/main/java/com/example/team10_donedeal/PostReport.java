package com.example.team10_donedeal;


import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class PostReport {

    public String title;
    public String content;
    public String person;
    public String chat_log;

    public PostReport () {}

    public PostReport (String content, String person,String title, String chat_log) {
        this.title = title;
        this.content=content;
        this.person = person;
        this.chat_log = chat_log;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("content", content);
        result.put("person", person);
        result.put("chat_log", chat_log);
        return result;
    }
}