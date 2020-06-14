package com.example.team10_donedeal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportListActivity extends AppCompatActivity {

    ListView lv;
    private DatabaseReference mPostReference;
    ArrayList<String> data;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportlist);

        mPostReference = FirebaseDatabase.getInstance().getReference().child("report");

        data = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        lv = (ListView) findViewById(R.id.report_list);
        getFirebaseDatabase();
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s=data.get(position);
                Intent intent=new Intent(ReportListActivity.this,ReportResActivity.class);
                intent.putExtra("title",s);
                startActivity(intent);
            }
        });

    }

    public void getFirebaseDatabase() {
        try{
            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    data.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        PostReport info = postSnapshot.getValue(PostReport.class);
                        data.add(info.title);
                    }
                    adapter.clear();
                    adapter.addAll(data);
                    adapter.notifyDataSetChanged();
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
