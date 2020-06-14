package com.example.team10_donedeal;

public class buysell {
    public Integer buy;
    public Integer sell;

    public buysell(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public buysell(Integer buy, Integer sell) {
        this.buy =buy;
        this.sell = sell;
    }
}
