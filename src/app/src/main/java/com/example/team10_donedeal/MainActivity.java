package com.example.team10_donedeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mPostReference;
    String ID,PW,iid="",ipw="";
    Integer auth,temp;
    EditText idtext, pwtext;
    Intent intent;
    String res=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent=new Intent(MainActivity.this, ChatAcitivity.class);

        idtext=findViewById(R.id.idE);
        pwtext=findViewById(R.id.pwE);
        Button btn=findViewById(R.id.login);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                iid=idtext.getText().toString();
                ipw=pwtext.getText().toString();
                if(iid.equals("")){
                    Toast.makeText(MainActivity.this,"ID를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(ipw.equals("")){
                    Toast.makeText(MainActivity.this,"PW를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    mPostReference = FirebaseDatabase.getInstance().getReference().child("id_list").child(iid);
                    getFirebaseDatabase();
                }
            }
        });
    }

    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebasePost get = dataSnapshot.getValue(FirebasePost.class);
                try{
                    ID=get.ID;
                    PW=get.PW;
                    auth=get.auth;
                    temp=get.temp;
                    res=get.res;
                }catch(java.lang.NullPointerException e){}
                if(iid.equals(ID)){
                    if(ipw.equals(PW)){
                        if(res!=null){
                            long time = System.currentTimeMillis();
                            SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd");
                            String str = dayTime.format(new Date(time));
                            try {
                                Date today= dayTime.parse(str);
                                Date resday= dayTime.parse(res);
                                int compare=today.compareTo(resday);
                                if(compare>0){
                                    intent.putExtra("auth",auth);
                                    intent.putExtra("temp",temp);
                                    if(auth==0)
                                        intent=new Intent(MainActivity.this, ReportListActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(MainActivity.this,res+"까지 제재된 ID 입니다.",Toast.LENGTH_SHORT).show();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            intent.putExtra("auth",auth);
                            intent.putExtra("temp",temp);
                            if(auth==0)
                                intent=new Intent(MainActivity.this, ReportListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else{
                        idtext.setText("");
                        pwtext.setText("");
                        Toast.makeText(MainActivity.this,"PW가 틀립니다",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    idtext.setText("");
                    pwtext.setText("");
                    Toast.makeText(MainActivity.this,"ID가 틀립니다",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.addValueEventListener(postListener);
    }

}
