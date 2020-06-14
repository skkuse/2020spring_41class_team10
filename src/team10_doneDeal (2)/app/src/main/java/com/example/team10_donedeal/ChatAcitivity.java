package com.example.team10_donedeal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class ChatAcitivity extends AppCompatActivity {

    ListView lv;
    String id_key,s;
    Integer btype=0,buy,sell,system=0,auth,temp;
    private DatabaseReference mPostReference;
    ArrayList<String> data;
    ArrayAdapter adapter;
    boolean searching=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent=getIntent();
        temp=intent.getIntExtra("temp",0);
        auth=intent.getIntExtra("auth",0);
        TextView mytemp=findViewById(R.id.temp);
        mytemp.setText("내 매너온도 : "+temp);
        Button buysell=findViewById(R.id.buysell_button);
        if(auth==1){
            buysell.setText("판매");
            btype=0;
            id_key="seller";
        }
        else{
            buysell.setText("구매");
            btype=1;
            id_key="buyer";
        }
        mPostReference = FirebaseDatabase.getInstance().getReference().child("chat_list");

        data = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        lv = (ListView) findViewById(R.id.listView);
        getFirebaseDatabase();
        lv.setAdapter(adapter);

        Button btn=findViewById(R.id.submit_button);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText et=findViewById(R.id.chat_text);
                s=et.getText().toString();
                s=s.trim();
                if(s.getBytes().length>0){
                    updatechat(searching);
                    searching=true;
                    et.setText("");
                }
            }
        });

        Button validButton =findViewById(R.id.valid_button);

        validButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(buy==1 && sell==1){
                    Toast.makeText(ChatAcitivity.this, "평가하기", Toast.LENGTH_SHORT).show();
                    Intent validIntent = new Intent(ChatAcitivity.this, AssessActivity.class);
                    validIntent.putExtra("auth", auth);
                    startActivity(validIntent);
                }
                else{
                    Toast.makeText(ChatAcitivity.this, "거래가 완료된 제품만 평가가 가능합니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button btnPage=findViewById(R.id.report_button);

        btnPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatAcitivity.this, "신고하기", Toast.LENGTH_SHORT).show();
                Intent intent2=new Intent(ChatAcitivity.this,ReportActivity.class);
                intent2.putExtra("auth",auth);
                startActivity(intent2);
            }
        });

        Button bsbtn=findViewById(R.id.buysell_button);
        bsbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(btype==0){
                    if(buy==0)
                        Toast.makeText(ChatAcitivity.this,"구매를 희망하는 사람이 없습니다.",Toast.LENGTH_SHORT).show();
                    else{
                        if(sell==0){
                            system=1;
                            sell=1;
                            s="판매가 완료되었습니다.";
                            updatechat(searching);
                            searching=true;
                        }
                        else
                            Toast.makeText(ChatAcitivity.this,"판매한 상품입니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if(sell==1)
                        Toast.makeText(ChatAcitivity.this,"판매가 완료된 상품입니다.",Toast.LENGTH_SHORT).show();
                    else{
                        system=1;
                        if(buy==0){
                            buy=1;
                            s="구매를 요청하였습니다.";
                        }
                        else{
                            buy=0;
                            s="구매요청을 취소하였습니다.";
                        }
                        updatechat(searching);
                        searching=true;
                        s="";
                    }
                }
            }
        });

    }

    public void getFirebaseDatabase() {
        try{
            final ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    data.clear();
                    buysell bsinfo=dataSnapshot.getValue(buysell.class);
                    buy=bsinfo.buy;
                    sell=bsinfo.sell;
                    for (DataSnapshot postSnapshot : dataSnapshot.child("chat_log").getChildren()) {
                        chat_log info = postSnapshot.getValue(chat_log.class);
                        data.add(info.ID+": "+info.log);
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
        }catch(java.lang.NullPointerException e){
        }
    }

    public void updatechat(boolean searching) {
        if(searching){
            try{
                Map<String, Object> childUpdates = new HashMap<>();
                long time = System.currentTimeMillis();
                SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String str = dayTime.format(new Date(time));
                childUpdates.put("/buy", buy);
                childUpdates.put("/sell", sell);
                if(system==1){
                    childUpdates.put("/chat_log/"+str+"/ID", "[System]");
                    system=0;
                }
                else
                    childUpdates.put("/chat_log/"+str+"/ID", id_key);
                childUpdates.put("/chat_log/"+str+"/log", s);
                mPostReference.updateChildren(childUpdates);
            }catch(java.lang.NullPointerException e){
            }
        }
    }
}
