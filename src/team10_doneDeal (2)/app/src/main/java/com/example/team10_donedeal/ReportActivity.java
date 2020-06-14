package com.example.team10_donedeal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportActivity extends AppCompatActivity {

    private static final String TAG = "ReportActivity";
    private DatabaseReference myRef;
    private DatabaseReference mPostReference;

    FirebaseDatabase database;

    EditText report_title;
    EditText report_content;
    Button btReport;
    Button btPage;
    Integer auth;
    String newContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent intent=getIntent();
        auth=intent.getIntExtra("auth",0);

        report_title = (EditText) findViewById(R.id.report_title);
        report_content = (EditText) findViewById(R.id.report_content);
        btReport = (Button) findViewById(R.id.submit_report);
        btPage = (Button) findViewById(R.id.no_report);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("report");

        btPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ReportActivity.this, "신고가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newContent = report_title.getText().toString();
                String newContent2 = report_content.getText().toString();
                String newContent3;
                if(auth==1)
                    newContent3 = "buyer";
                else
                    newContent3 = "seller";

                myRef.child(newContent).child("title").setValue(newContent);
                myRef.child(newContent).child("content").setValue(newContent2);
                myRef.child(newContent).child("person").setValue(newContent3);
                mPostReference = FirebaseDatabase.getInstance().getReference().child("chat_list").child("chat_log");
                getFirebaseDatabase();
                Toast.makeText(ReportActivity.this, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    public void getFirebaseDatabase() {
        try{
            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String s="";
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        chat_log info = postSnapshot.getValue(chat_log.class);
                        s=s+info.ID+": "+info.log+"\n";
                        myRef.child(newContent).child("chat_log").setValue(s);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mPostReference.addListenerForSingleValueEvent(postListener);
        }catch(java.lang.NullPointerException e){
        }
    }
}