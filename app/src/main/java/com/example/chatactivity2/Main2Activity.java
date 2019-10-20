package com.example.chatactivity2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Main2Activity extends AppCompatActivity {
EditText name;
EditText email;
EditText password;
Button register;
FirebaseAuth firebaseAuth;
DatabaseReference databaseReference;
SharedPreferences sharedPreferences;
String saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        firebaseAuth=FirebaseAuth.getInstance();
        sharedPreferences=getSharedPreferences("userid",0);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nam=name.getText().toString();
                String emal=email.getText().toString();
                String pass=password.getText().toString();
                if(nam.isEmpty()||emal.isEmpty()||pass.isEmpty()){
                    Toast.makeText(Main2Activity.this,"All fields are required",Toast.LENGTH_SHORT).show();
                }
                else{
                    register(nam,emal,pass);
                }
            }
        });
    }
    private void register(final String name, String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser;
                    firebaseUser=firebaseAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid=firebaseUser.getUid();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id",userid);
                    saved=sharedPreferences.getString("id","");
                    databaseReference= FirebaseDatabase.getInstance().getReference("users").child(userid);
                    user user=new user();
                    user.setId(userid);
                    user.setImage("default");
                    user.setUsername(name);
                   // hashMap.put("id",userid);
                    //hashMap.put("username",name);
                    //hashMap.put("image","default");
                    databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent=new Intent(Main2Activity.this,chatdb.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(Main2Activity.this,"You can't register",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
