package com.example.chatactivity2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    CircleImageView imageView;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    TextView textView;
    Intent intent;
    String userid;
    ImageButton imageButton;
    EditText editText;
    MessageAdapter messageAdapter;
    List<Chat> chats;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        textView=findViewById(R.id.usermsg);
        imageView=findViewById(R.id.profmsg);
        imageButton=findViewById(R.id.imgbutton);
        editText=findViewById(R.id.textsend);
        recyclerView=findViewById(R.id.recylv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
//        Toolbar toolbar=findViewById(R.id.toolbarmsg);
     //   getSupportActionBar(toolbar);
    intent=getIntent();
    userid=intent.getStringExtra("userid");
    firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    databaseReference= FirebaseDatabase.getInstance().getReference("users").child(userid.trim());
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            user usera=dataSnapshot.getValue(user.class);
            //Toast.makeText(MessageActivity.this, userid,Toast.LENGTH_SHORT).show();
            textView.setText(usera.getUsername());
            if(usera.getImage().equals("default")){
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
            else {
                Glide.with(MessageActivity.this).load(usera.getImage()).into(imageView);
            }
            readMessage(firebaseUser.getUid(),userid,usera.getImage());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    imageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg=editText.getText().toString();
            if(!msg.equals("")){
                sendMessage(firebaseUser.getUid(),userid,msg);
            }
            editText.setText("");
        }
    });
    }
    private void sendMessage(String sender,String reciever,String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciever",reciever);
        hashMap.put("message",message);
        reference.child("chats").push().setValue(hashMap);
    }
    private void readMessage(final String myid, final String userid, final String imgurl){
        chats=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference("chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if((chat.getReciever().equals(myid)&&chat.getSender().equals(userid))||(chat.getReciever().equals(userid)&&chat.getSender().equals(myid))){
                    chats.add(chat);
                    }
                    messageAdapter=new MessageAdapter(MessageActivity.this,chats,imgurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
