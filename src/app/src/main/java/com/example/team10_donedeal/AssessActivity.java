package com.example.team10_donedeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AssessActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private int auth, temp;
    String notMe_idKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess);

        Intent get_intent = getIntent();
        auth = get_intent.getIntExtra("auth", 0);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("id_list");

        Button veryGood = findViewById(R.id.veryGood);
        Button good = findViewById(R.id.good);
        Button soSo = findViewById(R.id.soSo);
        Button bad = findViewById(R.id.bad);
        Button veryBad = findViewById(R.id.veryBad);

        if (auth == 1){
            notMe_idKey = "buyer";
        }
        else{
            notMe_idKey = "seller";
        }

        mDatabaseRef.child(notMe_idKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebasePost basepost = dataSnapshot.getValue(FirebasePost.class);
                temp = basepost.temp;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        veryGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseRef.child(notMe_idKey).child("temp").setValue(temp + 2.0);
                Toast.makeText(AssessActivity.this, "평가가 반영되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseRef.child(notMe_idKey).child("temp").setValue(temp + 1.0);
                Toast.makeText(AssessActivity.this, "평가가 반영되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        soSo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AssessActivity.this, "평가가 반영되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseRef.child(notMe_idKey).child("temp").setValue(temp -1.0);
                Toast.makeText(AssessActivity.this, "평가가 반영되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        veryBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseRef.child(notMe_idKey).child("temp").setValue(temp - 2.0);
                Toast.makeText(AssessActivity.this, "평가가 반영되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}