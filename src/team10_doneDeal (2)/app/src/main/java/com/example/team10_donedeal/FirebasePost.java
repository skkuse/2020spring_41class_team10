package com.example.team10_donedeal;


import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class FirebasePost {

    public String ID;
    public String PW;
    public Integer auth;
    public Integer temp;
    public String res;

    public FirebasePost() {}

    public FirebasePost(String ID, String PW,Integer auth,Integer temp,String res) {
        this.ID = ID;
        this.PW=PW;
        this.auth = auth;
        this.temp = temp;
        this.res = res;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ID", ID);
        result.put("PW", PW);
        result.put("auth", auth);
        result.put("temp", temp);
        result.put("res", res);
        return result;
    }
}