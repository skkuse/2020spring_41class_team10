package com.example.team10_donedeal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportResActivity extends AppCompatActivity {

    Button btRes;
    ListView lv;
    private DatabaseReference mPostReference;
    ArrayList<String> data;
    ArrayAdapter adapter;
    String title, person;
    Integer period,temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportres);

        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        mPostReference = FirebaseDatabase.getInstance().getReference();

        data = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        lv = (ListView) findViewById(R.id.res_listView);
        getFirebaseDatabase();
        lv.setAdapter(adapter);

        btRes = (Button) findViewById(R.id.res_button);

        btRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et=findViewById(R.id.res_edit);
                try{
                    if(et.getText().toString().getBytes().length>0){
                        period=Integer.parseInt(et.getText().toString());
                        long time = System.currentTimeMillis();
                        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd");
                        String str = dayTime.format(new Date(time+86400000*period));
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference myRef=database.getReference().child("id_list/"+person+"/res");
                        myRef.setValue(str);

                        myRef=database.getReference().child("id_list/"+person+"/temp");
                        myRef.setValue(temp-5);

                        SimpleDateFormat resTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String resChat = resTime.format(new Date(time));
                        myRef=database.getReference().child("chat_list/chat_log/"+resChat+"/ID");
                        myRef.setValue("[System]");
                        myRef=database.getReference().child("chat_list/chat_log/"+resChat+"/log");
                        myRef.setValue("고객님의 신고로 "+person+"님이 "+str+"까지 제재되었습니다.");

                        myRef=database.getReference().child("report/"+title);
                        myRef.setValue(null);

                        Toast.makeText(ReportResActivity.this, "제재되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch(NumberFormatException e){
                    Toast.makeText(ReportResActivity.this, "숫자만 입력해주세요.", Toast.LENGTH_SHORT).show();
                    et.setText("");
                }
            }
        });

    }

    public void getFirebaseDatabase() {
        try{
            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        DataSnapshot  postSnapshot=dataSnapshot.child("report").child(title);
                        PostReport info = postSnapshot.getValue(PostReport.class);
                        person=info.person;
                        DataSnapshot  tempSnapshot=dataSnapshot.child("id_list").child(person).child("temp");
                        temp=tempSnapshot.getValue(Integer.class);
                        data.clear();
                        data.add("제목 : "+info.title);
                        data.add("내용 : "+info.content);
                        data.add("채팅 로그"+info.chat_log);
                        adapter.clear();
                        adapter.addAll(data);
                        adapter.notifyDataSetChanged();
                    }catch (NullPointerException e){

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mPostReference.addValueEventListener(postListener);
        }catch(NullPointerException e){
        }
    }

}